package com.huixdou.api.service;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageHelper;
import com.huixdou.api.bean.MerNews;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.MerNewsDao;
import com.huixdou.api.service.OssService;
import com.huixdou.api.service.SysDictService;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Query;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jinxin on 2018/12/21 15:53.
 */
@Service
public class MerNewsService extends BaseService<MerNewsDao, MerNews> {

	@Autowired
	private OssService ossService;

	@Autowired
	private SysDictService sysDictService;

	// 列表
	public List<MerNews> list(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		List<MerNews> list = dao.selectList(query);
		if (list.size() > 0) {
			for (MerNews merNews : list) {
				SysDict sysDict = sysDictService.selectByTypeAndCode("mer-news-status", merNews.getStatus());
				if (!StringUtils.isEmpty(sysDict)) {
					merNews.setStatusName(sysDict.getName());
				}
				merNews.setCreateDate(
						DateUtils.formatDate(new Date(Convert.toLong(merNews.getCreateDate())), "yyyy-MM-dd HH:mm"));
			}
		}
		return list;
	}

	// 发布 // 未发布:wfb 审核中:shz;已发布待处理：dcl；审核失败：shsb；已发布：yfb；已撤回：ych
	@Transactional(readOnly = false)
	public Result publish(Map<String, Object> params, HttpServletRequest request) throws Exception {
		// 保存:bc;提交:tj;已发布待处理：shz；审核失败：shsb；已发布：yfb；已撤回：ych
		List<String> statusList = new ArrayList<String>();
		statusList.add("yfb");
		statusList.add("wfb");
		statusList.add("shz");
		statusList.add("shsb");
		//statusList.add("ych");
		params.put("statusList", statusList);
		params.put("status", "yfb");
		return returnResult(params);
	}

	// 删除
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || 0 == list.size())
			return Result.failure("参数类型有误");
		params.put("list", list);
		List<MerNews> merNews = dao.selectMerNewsListById(params);
		if (list.size() > merNews.size())
			return Result.failure("有些参数未找到或输入参数不对");
		Integer deleteFlag = 1;
		params.put("deleteFlag", deleteFlag);
		String deleteTime = System.currentTimeMillis() + "";
		params.put("deleteTime", deleteTime);
		dao.delete(params);
		return Result.success();
	}

	// 撤回 // 未发布:wfb 审核中:shz;已发布待处理：dcl；审核失败：shsb；已发布：yfb；已撤回：ych
	@Transactional(readOnly = false)
	public Result recall(Map<String, Object> params) {
		String reject = Convert.toStr(params.get("reject"));
		if (StringUtils.isEmpty(reject))
			return Result.failure("请输入原因");
		if (reject.length() > 100)
			return Result.failure("输入原因字符过长");
		List<String> statusList = new ArrayList<String>();
		statusList.add("wfb");
		statusList.add("shz");
		statusList.add("shsb");
		statusList.add("ych");
		params.put("statusList", statusList);
		params.put("status", "ych");
		return returnResult(params);
	}

	// 查看详情
	public Result details(String id) {
		MerNews merNewsOne = dao.selectById(id);
		if (StringUtils.isEmpty(merNewsOne))
			return Result.failure("未找到珠宝商咨询");
		String url = "";
		if (!StringUtils.isEmpty(merNewsOne.getFileId())) {
			url = ossService.getAddress(merNewsOne.getFileId());
		}
		if (!StringUtils.isEmpty(merNewsOne.getStatus())) {
			if(merNewsOne.getStatus().equals("shz")){
				merNewsOne.setStatus("yfb");
			}
			SysDict sysDict = sysDictService.selectByTypeAndCode("mer-news-status", merNewsOne.getStatus());
			if (!StringUtils.isEmpty(sysDict)) {
				merNewsOne.setStatusName(sysDict.getName());
			}
		}
		merNewsOne.setUrl(url);
		return Result.success(merNewsOne);
	}

	protected Result returnResult(Map<String, Object> params) {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || 0 == list.size())
			return Result.failure("参数类型有误");
		params.put("list", list);
		List<MerNews> merNews = dao.selectMerNewsListById(params);
		if (list.size() > merNews.size())
			return Result.failure("有些参数未找到或输入参数不对");
		List<String> passList = dao.selectListByMap(params);
		if (list.size() == passList.size()) {
			return Result.success();
		}
		for (int i = 0; i < list.size(); i++) {
			for (String string : passList) {
				if (string.equals(list.get(i))) {
					list.remove(i);
				}
			}
		}
		params.put("publishDate", System.currentTimeMillis() + "");
		params.put("list", list);
		dao.updateByMap(params);
		return Result.success();
	}

	public List<SysDict> runGetTypeCode(String typeCode) {
		Map<String, String> table = new HashMap<String, String>();
		table.put("typeCode", typeCode);
		table.put("key", "1");
		return getTypeCode(table);
	}

	public List<SysDict> getTypeCode(Map<String, String> table) {
		String typeCode = table.get("typeCode");
		String key = table.get("key");
		if (StringUtils.isEmpty(typeCode) || StringUtils.isEmpty(key)) {
			return null;
		}
		List<SysDict> list = sysDictService.selectByTypeCodeSimpleness(typeCode);
		Iterator<SysDict> it = list.iterator();
		while (it.hasNext()) {
			SysDict sysDict = it.next();
			String code = sysDict.getCode();
			if ("1".equals(key)) {
				if ("shz".equalsIgnoreCase(code) || "wfb".equalsIgnoreCase(code)|| "shsb".equalsIgnoreCase(code)) {
					it.remove();
				}
			} else if ("0".equals(key)) {

				if ("shz".equalsIgnoreCase(code)) {
					it.remove();
				} else if ("shz".equalsIgnoreCase(code)) {
					it.remove();

				}
			}
		}
		return list;
	}

	// 批量的提交 (直接把能提交的给提交了)
	@Transactional(readOnly = false)
	public Result submit(Map<String, Object> params) {
		List<String> list = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(list) || 0 == list.size())
			return Result.failure("参数类型有误");
		// 先判断否非存在 然后再判断是否是什么状态 如果不是已发布状态 其他的都能进行再发布
		List<String> statusList = new ArrayList<String>();
		// 这是不能通过的状态
		statusList.add("shz");
		statusList.add("yfb");
		params.put("statusList", statusList);
		// 这是要改成的状态
		params.put("status", "shz");
		return returnResult(params);
	}
}
