package com.huixdou.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.huixdou.api.bean.Content;
import com.huixdou.api.bean.Oss;
import com.huixdou.api.bean.SupNews;
import com.huixdou.api.bean.SysConfig;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.SupNewsDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Query;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.AsynHttpPostTask;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.HttpClient4Utils;
import com.huixdou.common.utils.IdGen;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConverterRegistry;

/**
 * 国检咨询
 */
@Service
public class SupNewsService extends BaseService<SupNewsDao, SupNews> {

	@Autowired
	private OssService ossService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private CheckService checkService;
	// 列表

	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private AppMessageNewService appMessageNewService;

	public List<SupNews> list(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		List<SupNews> list = dao.selectList(query);
		if (list.size() > 0) {
			for (SupNews supNews : list) {
				SysDict sysDict = sysDictService.selectByTypeAndCode("sup-news-status", supNews.getStatus());
				if (!StringUtils.isEmpty(sysDict)) {
					supNews.setStatusName(sysDict.getName());
				}
				supNews.setCreateDate(
						DateUtils.formatDate(new Date(Convert.toLong(supNews.getCreateDate())), "yyyy-MM-dd HH:mm"));
			}
		}
		return list;
	}

	// 发布 已发布：yfb;未发布：wfb 可以批量的
	@Transactional(readOnly = false)
	public Result publish(Map<String, Object> params, HttpServletRequest request) throws Exception {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || list.size() < 1)
			return Result.failure("参数类型有误");
		for (int i = 0; i < list.size(); i++) {
			SupNews supNews = dao.selectById(list.get(i));
			if (StringUtils.isEmpty(supNews)) {
				list.remove(i);
			} else {
				if ("yfb".equals(supNews.getStatus()) || "shz".equals(supNews.getStatus())) {
					list.remove(i);
				}
			}

		}
		if (list.size() < 1) {
			return Result.success();
		}

		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		// 平台资讯通过审核和的id
		List<String> passList = new ArrayList<String>();
		Map<String, String> resultMap = new HashMap<String, String>();
		Result result = new Result();
		SysConfig sysConfig = sysConfigService.selectByKey("check.switch");
		boolean sysKey = true;
		if (!StringUtils.isEmpty(sysConfig)) {
			// 开关存在 判断值 0是关闭 1是开启
			if (!sysConfig.getcValue().equals("1")) {
				sysKey = false;
			}
		}
		if (sysKey) {
			HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 2000, 2000, 2000);
			// 循环 把内容只含有图片 没有视频的 并且都过审核的 给发布
			// 图片审核记录的id 和文本审核的id
			String ossLoginId = "";
			String textLoginId = "";
			// 0是没通过 1 是通过了
			Integer textCheckStatus = 0;
			Integer ossCheckStatus = 0;
			for (String id : list) {
				// 先去查询数字
				SupNews supNews = dao.selectById(id);
				// 如果存在 就进行审核
				String content = supNews.getTitle() + supNews.getSource();
				content += supNews.getLabel();
				// }
				// 内容
				String supNewsContent = supNews.getContent();
				content += com.huixdou.common.utils.StringUtils.replaceRichText(supNewsContent);
				// 文本检查的Map 需要放content字段
				Map<String, String> textParams = new HashMap<String, String>();
				textParams.put("content", content);
				// 文本检查 code200是通讯查询数据ok 其他为失败
				result = checkService.textCheck(textParams);
				// 文本审核 action pass 是审核通过
				if (result.getCode() == 200) {
					resultMap = converterRegistry.convert(Map.class, result.getData());
					textLoginId = resultMap.get("logId");
					if (resultMap.get("action").equals("pass")) {
						// 审核通过 这是提取照片 返回照片的集合
						textCheckStatus = 1;
						List<String> imageList = com.huixdou.common.utils.StringUtils.extractImg(supNewsContent);
						Oss oss = ossService.selectById(supNews.getFileId());
						// 平台咨询的海报是图片 不能使视频 如果其他地方是视频的话 需要不一样的查找
						if (!StringUtils.isEmpty(oss)) {
							// 获取海报的URL
							imageList.add(oss.getUrl());
						}
						if (imageList.size() > 0) {
							// 图片的审核
							if (imageList.size() > 32) {
								// 图片审核最多一次上传32张 内容如果有32张 那么提交就会失败
								result = checkService.ImageCheck(imageList.subList(0, 31));
								if (result.getCode() == 200) {
									resultMap = converterRegistry.convert(Map.class, result.getData());
									if (resultMap.get("action").equals("pass")) {
										result = checkService.ImageCheck(imageList.subList(32, imageList.size()));
									}
								}
							} else {
								result = checkService.ImageCheck(imageList);
							}

							if (result.getCode() == 200) {
								resultMap = converterRegistry.convert(Map.class, result.getData());
								// 视频是通过的话
								ossLoginId = resultMap.get("logId");
								if (resultMap.get("action").equals("pass")) {
									// 视频审核
									ossCheckStatus = 1;
									List<String> videoList = com.huixdou.common.utils.StringUtils
											.extractVideo(supNewsContent);
									if (videoList.size() > 0) {
										// 视频列表
										Map<String, String> publishList = new HashMap<String, String>();
										List<String> publishIdList = new ArrayList<String>();
										boolean key = true;
										for (String videoId : videoList) {
											// 便利提交视频
											Map<String, String> postParams = new HashMap<String, String>();
											postParams.put("video", videoId);
											result = checkService.videoSubmit(postParams);
											if (result.getCode() == 200) {
												// 通讯ok
												resultMap = converterRegistry.convert(Map.class, result.getData());
												;
												if (resultMap.get("action").equals("pushSuccess")) {
													// 推送成功 发送接口请求 这里获得返回查询的id
													String pushId = resultMap.get("logId");
													publishIdList.add(pushId);
												} else {
													key = false;
												}
											} else {
												if (result.getCode() == 400) {
													return Result.failure("网络连接异常,请稍后再进行发布");
												} else if (result.getCode() == 401) {
													return Result.failure("审核API到期");
												}
											}
										}
										if (key) {
											supNews.setStatus("shz");
											dao.update(supNews);
											// id 是这条平台资讯的id
											publishList.put("id", id);
											publishList.put("type", "ptzx");
											publishList.put("video", publishIdList.toString());
											System.out.println("发送请求开始时间" + System.currentTimeMillis());
											AsynHttpPostTask postTask = new AsynHttpPostTask();
											// 这步是把令牌放进去
											postTask.setRequest(request);
											// 这不是发起异步的请求
											postTask.post(httpClient, publishList);
											System.out.println("发送请求结束时间" + System.currentTimeMillis());
										}
									} else {
										// 如果没有视频 那前两个审核成功 那可就直接可以发布了
										passList.add(id);
									}

								}
							} else {
								if (result.getCode() == 400) {
									return Result.failure("网络连接异常,请稍后再进行发布");
								} else if (result.getCode() == 401) {
									return Result.failure("审核API到期");
								}
							}

						} else {
							// 没有图片 开始视频审核
							List<String> videoList = com.huixdou.common.utils.StringUtils.extractVideo(supNewsContent);
							if (videoList.size() > 0) {
								// 视频开始审核
								// 视频列表
								Map<String, String> publishList = new HashMap<String, String>();
								List<String> publishIdList = new ArrayList<String>();
								boolean key = true;
								for (String videoId : videoList) {
									// 便利提交视频
									Map<String, String> postParams = new HashMap<String, String>();
									postParams.put("video", videoId);
									result = checkService.videoSubmit(postParams);
									if (result.getCode() == 200) {
										// 通讯ok
										resultMap = converterRegistry.convert(Map.class, result.getData());
										;
										if (resultMap.get("action").equals("pushSuccess")) {
											// 推送成功 发送接口请求 这里获得返回查询的id
											String pushId = resultMap.get("logId");
											publishIdList.add(pushId);
										} else {
											key = false;
										}
									} else {
										if (result.getCode() == 400) {
											return Result.failure("网络连接异常,请稍后再进行发布");
										} else if (result.getCode() == 401) {
											return Result.failure("审核API到期");
										}
									}
								}
								if (key) {
									// id 是这条平台资讯的id
									supNews.setStatus("shz");
									dao.update(supNews);
									publishList.put("id", id);
									publishList.put("type", "ptzx");
									publishList.put("video", publishIdList.toString());
									System.out.println("发送请求开始时间" + System.currentTimeMillis());
									AsynHttpPostTask postTask = new AsynHttpPostTask();
									// 这步是把令牌放进去
									postTask.setRequest(request);
									// 这不是发起异步的请求
									postTask.post(httpClient, publishList);
									System.out.println("发送请求结束时间" + System.currentTimeMillis());
								}
							} else {
								passList.add(id);

							}
						}

					}

				} else {
					if (result.getCode() == 400) {
						return Result.failure("网络连接异常,请稍后再进行发布");
					} else if (result.getCode() == 401) {
						return Result.failure("审核API到期");
					}
				}
				// 这时候更新内容和图片表的字段和状态 如果是有视频的 那么在视频的检查结果的时候进行更新 图片和视频的字段信息
				Content updateContent = contentService.selectById(supNews.getContentId());
				if (!StringUtils.isEmpty(updateContent)) {
					updateContent.setApiLogId(textLoginId);
					updateContent.setCheckStatus(textCheckStatus);
					contentService.update(updateContent);

				}
				Oss oss = ossService.selectById(supNews.getFileId());
				if (!StringUtils.isEmpty(oss)) {
					oss.setApiLogId(ossLoginId);
					oss.setCheckStatus(ossCheckStatus);
					ossService.update(oss);

				}

			}
			if (passList.size() == 0) {
				// System.out.println("返回时间1" + System.currentTimeMillis());
				return Result.success();
			}
			params.put("list", passList);
		}
		params.put("status", "yfb");
		// System.out.println("返回时间1" + System.currentTimeMillis());
		return returnResult(params);
	}

	// 撤回 已发布：yfb；已撤回：wfb
	@Transactional(readOnly = false)
	public Result recall(Map<String, Object> params) {
		params.put("status", "wfb");
		return returnResult(params);
	}
	
	// 取消 已发布：yfb；已撤回：wfb
	@Transactional(readOnly = false)
	public Result cancel(Map<String, Object> params) {
		params.put("status", "wfb");
		return returnResult(params);
	}
	
	//  推送
		@Transactional(readOnly = false)
		public Result pushMessage(Map<String, Object> params) {
			String id=Convert.toStr(params.get("id"));
			SupNews supNews=dao.selectById(id);
			if(StringUtils.isEmpty(supNews)){
				return Result.failure("资讯不存在");
			}
			if(!supNews.getStatus().equals("yfb")){
				return Result.failure("资讯尚未发布,无法推送");
			}
			if(supNews.getPublishAmount()!=0){
				return Result.failure("已结推送过了 请勿重复操作");
			}
			GetUrlUtils getUtils=new GetUrlUtils();
			String url=getUtils.getUrl("ptzx", id);
			if(StringUtils.isEmpty(url)){
				return Result.failure("资讯不存在或者类型输入错误");
			}
			url=getUtils.getHearUrl()+url;
			Result result=appMessageNewService.supMessageInsertAndSend("平台资讯", supNews.getTitle(), url);
			if(result.getCode()==200){
				//变更推送数量
				supNews.setPublishAmount(supNews.getPublishAmount()+1);
				dao.updatePublishAmount(supNews);
			}
			return result;
			
		}
	
	
	// 删除
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || 0 == list.size())
			return Result.failure("参数类型有误");
		params.put("list", list);
		List<SupNews> supNews = dao.selectNumber(params);
		// 删除的id有已经被删除或者不存在的
		if (list.size() > supNews.size())
			return Result.failure("输入的参数不存在或者已被删除");
		String deleteTime = System.currentTimeMillis() + "";
		Integer deleteFlag = 1;
		params.put("deleteFlag", deleteFlag);
		params.put("deleteTime", deleteTime);
		dao.delete(params);
		return Result.success();
	}

	// 新增
	@Transactional(readOnly = false)
	public Result insertSupNews(SupNews supNews) throws Exception {
		Integer createId = supNews.getCreateId();
		// 内容表的新增
		Content content = new Content();
		content.setContent(supNews.getContent());
		String contentid = IdGen.getObjectId();
		content.setId(contentid);
		// 平台资讯
		String id = IdGen.getObjectId();
		content.setTypeCode("ptzx");
		contentService.insert(content);
		supNews.setContentId(contentid);
		supNews.setId(id);
		supNews.setCreateId(createId);
		supNews.setCreateDate(System.currentTimeMillis() + "");
		supNews.setStatus("wfb");
		dao.insert(supNews);
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		return Result.success(map);
	}

	// 编辑
	@Transactional(readOnly = false)
	public Result updateSupNews(SupNews supNews) {
		// 校验字段
		SupNews mySqlSupNews = dao.selectById(supNews.getId());
		if (StringUtils.isEmpty(mySqlSupNews))
			return Result.failure("该资讯不存在");
		supNews.setStatus(mySqlSupNews.getStatus());
		dao.updateSupNews(supNews);
		return Result.success();
	}

	// 查看详情
	public Result details(String id) {
		SupNews supNewsOne = dao.selectById(id);
		if (StringUtils.isEmpty(supNewsOne))
			return Result.failure("未找到平台资讯");
		String url = "";
		if (!StringUtils.isEmpty(supNewsOne.getFileId())) {
			url = ossService.getAddress(supNewsOne.getFileId());
		}
		if (!StringUtils.isEmpty(supNewsOne.getStatus())) {
			SysDict sysDict = sysDictService.selectByTypeAndCode("sup-news-status", supNewsOne.getStatus());
			if (!StringUtils.isEmpty(sysDict)) {
				supNewsOne.setStatusName(sysDict.getName());
			}
		}
		supNewsOne.setUrl(url);
		return Result.success(supNewsOne);
	}

	// 发布 撤回的方法
	protected Result returnResult(Map<String, Object> params) {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || 0 == list.size())
			return Result.failure("参数类型有误");
		params.put("list", list);
		List<SupNews> supNews = dao.selectNumber(params);
		// 存在已经被删除或者不存在的id
		if (list.size() > supNews.size())
			return Result.failure("有些参数不存在或者输入的参数已经被撤回");
		List<String> passList = dao.selectListByMap(params);
		// 输入的状态和要改变的状态是一样的
		if (list.size() == passList.size()) {
			return Result.success();
		}
		// 把不能改变的状态id给去掉
		for (int i = 0; i < list.size(); i++) {
			for (String string : passList) {
				if (string.equals(list.get(i))) {
					list.remove(i);
				}
			}
		}
		params.put("createDate", System.currentTimeMillis());
		params.put("list", list);
		dao.updateByMap(params);

		return Result.success();
	}
	public Result copyUrl(Map<String, Object> params){
		String id=Convert.toStr(params.get("id"));
		if(StringUtils.isEmpty(id)){
			return Result.failure("参数不能为空");
		}
		SupNews supNews = dao.selectById(id);
		if (StringUtils.isEmpty(supNews)) {
			return Result.failure("资讯不存在");
		}
		GetUrlUtils getUtils=new GetUrlUtils();
		String url=getUtils.getUrl("ptzx", id);
		if(StringUtils.isEmpty(url)){
			return Result.failure("资讯不存在或者类型输入错误");
		}
		url=getUtils.getHearUrl()+url;
		Map<String,String> map=new HashMap<String,String>();
		map.put("url", url);
		return Result.success(map);
		
	}
	
}
