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
import com.huixdou.api.bean.SupBaike;
import com.huixdou.api.bean.SysConfig;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.SupBaikeDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Query;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.AsynHttpPostTask;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.HttpClient4Utils;
import com.huixdou.common.utils.IdGen;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConverterRegistry;

@Service
public class SupBaikeService extends BaseService<SupBaikeDao, SupBaike> {

	@Autowired
	private OssService ossService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private SupBaikeTypeService supBaikeTypeService;
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private CheckService checkService;
	@Autowired
	private AppMessageNewService appMessageNewService;

	// 删除所属类别下所有知识
	public void deleteByTypeId(Map<String, Object> params) {
		String typeId = Convert.toStr(params.get("id"));
		params.put("typeId", typeId);
		params.put("deleteFlag", 1);
		params.put("deleteTime", System.currentTimeMillis() + "");
		dao.updateByTypeId(params);
	}

	// 查看所属有多少知识
	public Integer selectNumberByTypeId(String typeId) {
		return dao.selectNumberByTypeId(typeId);
	}

	@Transactional(readOnly = false)
	public Result insertSupBaike(SupBaike supBaike) throws Exception {
		String typeId = supBaike.getTypeId();
		if (StringUtils.isEmpty(typeId))
			return Result.failure("类型不能为空");
		if (StringUtils.isEmpty(supBaikeTypeService.selectById(typeId)))
			return Result.failure("类别不存在");
		// 内容表的插入
		Content content = new Content();
		String contentid = IdGen.getObjectId();
		content.setId(contentid);
		content.setContent(supBaike.getContent());
		content.setTypeCode("bk");
		contentService.insert(content);
		supBaike.setContentId(contentid);
		String id = IdGen.getObjectId();
		supBaike.setId(id);
		supBaike.setPublishDate(System.currentTimeMillis() + "");
		supBaike.setCreateDate(System.currentTimeMillis() + "");
		supBaike.setStatus("wfb");
		dao.insert(supBaike);
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		return Result.success(map);
	}

	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || 0 == list.size())
			return Result.failure("参数类型有误");
		params.put("list", list);
		List<SupBaike> supBaikes = dao.selectNumber(params);
		if (list.size() > supBaikes.size())
			return Result.failure("输入的参数不存在或者已被删除");
		// 删除时间
		Integer deleteFlag = 1;
		String deleteTime = System.currentTimeMillis() + "";
		params.put("deleteFlag", deleteFlag);
		params.put("deleteTime", deleteTime);
		dao.delete(params);
		return Result.success();
	}

	// 发布
	@Transactional(readOnly = false)
	public Result publish(Map<String, Object> params, HttpServletRequest request) throws Exception {
		// 前台传过来的要发布的集合
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || list.size() < 1)
			return Result.failure("参数类型有误");
		// 把审核中的给去除掉
		for (int i = 0; i < list.size(); i++) {
			SupBaike supBaike = dao.selectById(list.get(i));
			if (StringUtils.isEmpty(supBaike)) {
				list.remove(i);
			} else {
				if ("yfb".equals(supBaike.getStatus()) || "shz".equals(supBaike.getStatus())) {
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
		// 查找审核开关 如果审核开关不存在 那么默认审核
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
				SupBaike supBaike = dao.selectById(id);
				// 如果存在 就进行审核
				String content = supBaike.getTitle() + supBaike.getSummary();
				// 内容
				String supNewsContent = supBaike.getContent();
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
						Oss oss = ossService.selectById(supBaike.getFileId());
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
												if (resultMap.get("action").equals("pushSuccess")) {
													// 推送成功 发送接口请求
													// 这里获得返回查询的id

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
											supBaike.setStatus("shz");
											dao.updateSupBaike(supBaike);
											// id 是这条平台资讯的id
											publishList.put("id", id);
											publishList.put("type", "bk");
											publishList.put("video", publishIdList.toString());
											AsynHttpPostTask postTask = new AsynHttpPostTask();
											// 这步是把令牌放进去
											postTask.setRequest(request);
											// 这不是发起异步的请求
											postTask.post(httpClient, publishList);
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
									// 在发布之前把状态变成审核中
									supBaike.setStatus("shz");
									dao.updateSupBaike(supBaike);
									publishList.put("id", id);
									publishList.put("type", "ptzx");
									publishList.put("video", publishIdList.toString());
									AsynHttpPostTask postTask = new AsynHttpPostTask();
									// 这步是把令牌放进去
									postTask.setRequest(request);
									// 这不是发起异步的请求
									postTask.post(httpClient, publishList);
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
				Content updateContent = contentService.selectById(supBaike.getContentId());
				if (!StringUtils.isEmpty(updateContent)) {
					updateContent.setApiLogId(textLoginId);
					updateContent.setCheckStatus(textCheckStatus);
					contentService.update(updateContent);

				}
				Oss oss = ossService.selectById(supBaike.getFileId());
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

	// 撤回
	@Transactional(readOnly = false)
	public Result recall(Map<String, Object> params) {
		params.put("status", "wfb");
		return returnResult(params);
	}

	// 取消
	@Transactional(readOnly = false)
	public Result cancel(Map<String, Object> params) {
		params.put("status", "wfb");
		return returnResult(params);
	}

	public Result details(Map<String, Object> params) {
		String id = Convert.toStr(params.get("id"));
		if (StringUtils.isEmpty(id)) {
			return Result.failure("请输入id");
		}
		SupBaike supBaike = dao.selectById(id);
		if (StringUtils.isEmpty(supBaike))
			return Result.failure("百科不存在");
		String url = "";
		if (!StringUtils.isEmpty(supBaike.getFileId())) {
			url = ossService.getAddress(supBaike.getFileId());
		}
		String status = Convert.toStr(supBaike.getStatus());
		if (!StringUtils.isEmpty(status)) {
			SysDict sysDict = sysDictService.selectByTypeAndCode("sup-baike-status", status);
			if (!StringUtils.isEmpty(sysDict)) {
				supBaike.setStatusName(sysDict.getName());
			}
		}
		supBaike.setUrl(url);
		return Result.success(supBaike);
	}

	@Transactional(readOnly = false)
	public void updateBy(Map<String, Object> params) {
		dao.updateByMap(params);
	}

	@Transactional(readOnly = false)
	public Result updateSupBaike(SupBaike supBaike) {
		String id = supBaike.getId();
		if (StringUtils.isEmpty(id))
			return Result.failure("请输入id");
		SupBaike SupBaikeById = dao.selectById(id);
		if (StringUtils.isEmpty(SupBaikeById))
			return Result.failure("知识不存在,请查看是否被删除");
		supBaike.setStatus(SupBaikeById.getStatus());
		dao.updateSupBaike(supBaike);
		return Result.success();
	}

	public List<SupBaike> list(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		List<SupBaike> supBaikes = dao.selectList(query);
		if (supBaikes.size() > 0) {
			for (SupBaike supBaike : supBaikes) {
				SysDict sysDict = sysDictService.selectByTypeAndCode("sup-baike-status", supBaike.getStatus());
				if (!StringUtils.isEmpty(sysDict)) {
					supBaike.setStatusName(sysDict.getName());
				}
				supBaike.setPublishDate(
						DateUtils.formatDate(new Date(Convert.toLong(supBaike.getPublishDate())), "yyyy-MM-dd HH:mm"));
			}
		}
		return supBaikes;
	}

	// 发布 撤回的方法
	protected Result returnResult(Map<String, Object> params) {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || 0 == list.size())
			return Result.failure("参数类型有误");
		params.put("list", list);
		List<SupBaike> supBaikes = dao.selectNumber(params);
		if (list.size() > supBaikes.size())
			return Result.failure("输入的参数不存在或者已被删除");
		params.put("publishDate", System.currentTimeMillis() + "");
		List<String> passList = dao.selectListByMap(params);
		// 输入的状态和要改变的状态是一样的
		if (list.size() == passList.size())
			return Result.success();
		// 把不能改变的状态id给去掉
		for (int i = 0; i < list.size(); i++) {
			for (String string : passList) {
				if (string.equals(list.get(i))) {
					list.remove(i);
				}
			}
		}
		params.put("list", list);
		dao.updateByMap(params);
		return Result.success();

	}

	// 推送
	@Transactional(readOnly = false)
	public Result pushMessage(Map<String, Object> params) {
		String id = Convert.toStr(params.get("id"));
		SupBaike supBaike = dao.selectById(id);
		if (StringUtils.isEmpty(supBaike)) {
			return Result.failure("珠宝知识不存在");
		}
		if(!supBaike.getStatus().equals("yfb")){
			return Result.failure("珠宝知识尚未发布,无法推送");
		}
		if(supBaike.getPublishAmount()!=0){
			return Result.failure("已结推送过了 请勿重复操作");
		}
		GetUrlUtils getUtils=new GetUrlUtils();
		String url=getUtils.getUrl("bk", id);
		if(StringUtils.isEmpty(url)){
			return Result.failure("珠宝知识不存在或者类型输入错误");
		}
		url=getUtils.getHearUrl()+url;
		Result result=appMessageNewService.supMessageInsertAndSend("珠宝百科", supBaike.getTitle(), url);
		if(result.getCode()==200){
			//变更推送数量
			supBaike.setPublishAmount(supBaike.getPublishAmount()+1);
			dao.updatePublishAmount(supBaike);
		}
		return result;

	}
	public Result copyUrl(Map<String, Object> params){
		String id=Convert.toStr(params.get("id"));
		if(StringUtils.isEmpty(id)){
			return Result.failure("参数不能为空");
		}
		SupBaike supBaike = dao.selectById(id);
		if (StringUtils.isEmpty(supBaike)) {
			return Result.failure("珠宝知识不存在");
		}
		GetUrlUtils getUtils=new GetUrlUtils();
		String url=getUtils.getUrl("bk", id);
		if(StringUtils.isEmpty(url)){
			return Result.failure("珠宝知识不存在或者类型输入错误");
		}
		url=getUtils.getHearUrl()+url;
		Map<String,String> map=new HashMap<String,String>();
		map.put("url", url);
		return Result.success(map);
		
	}

}
