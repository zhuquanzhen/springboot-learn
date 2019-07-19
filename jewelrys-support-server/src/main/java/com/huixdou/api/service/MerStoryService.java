package com.huixdou.api.service;

import com.huixdou.api.bean.CerInfo;
import com.huixdou.api.bean.MerStory;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.MerStoryDao;
import com.huixdou.api.service.CerInfoService;
import com.huixdou.api.service.ContentService;
import com.huixdou.api.service.OssService;
import com.huixdou.api.service.SysDictService;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerStoryService extends BaseService<MerStoryDao, MerStory> {
	
	@Autowired
	private OssService ossService;
	
	@Autowired
	private MerStoryFileService storyFileService;
	
	@Autowired
	private SysDictService dictService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private CerInfoService cerInfoService;
	
	@Autowired
	private MerStoryCertService storyCertService;
	
	// 运营平台列表
	public List<MerStory> selectList(Map<String, Object> params) {
		List<MerStory> story = dao.selectList(params);
		for (MerStory item : story) {
			item.setFiles(null);
			SysDict dict = dictService.selectByTypeAndCode("mer-story-status", item.getStatus());
			if (ObjectUtil.isNotNull(dict)) {
				item.setStatusName(dict.getName());
			}
			item.setCreateDate(DateUtils.formatDate(Convert.toLong(item.getCreateDate()), "yyyy-MM-dd HH:mm"));
		}
		return story;
	}
	
	// 运营平台更新状态
	@Transactional(readOnly = false)
	public Result updateStatus(Map<String, Object> params, String status) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		params.put("status", status);
		params.put("auditDate", System.currentTimeMillis());
		dao.updateByMap(params);
		return Result.success();
	}
	
	// 运营平台通过故事ID查看证书列表
	public List<CerInfo> getCert(Map<String, Object> params) {
		// CerInfo 中证书编号为no, no为List<String>
		List<String> no = dao.selectCertNo(Convert.toStr(params.get("id")));
		if (no.size() == 0)
			// 可以优化为直接返回Map
			no.add("");
		params.put("no", no);
		return cerInfoService.selectPage(params);
	}
	
	// 运营平台查看故事详情
	public Result selectById(String id) {
		MerStory story = dao.selectById(id);	
		if (ObjectUtil.isNull(story)) {
			return Result.failure("请输入正确的参数 id");
		}
		story.setFiles(ossService.getAddressMap(dao.selectFile(story.getId())));
		story.setCreateDate(DateUtils.formatDate(Convert.toLong(story.getCreateDate()), "yyyy-MM-dd HH:mm"));
		story.setLogo(ossService.getAddress(story.getLogo()));
		story.setCertNoCount(dao.selectCertNoCount(story.getId()));
		SysDict dict = dictService.selectByTypeAndCode("mer-story-status", story.getStatus());
		if (ObjectUtil.isNotNull(dict)) {
			story.setStatusName(dict.getName());
		}
		return Result.success(story);
	}	
	
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		
		params.put("deleteTime", System.currentTimeMillis());
		params.put("contentId", dao.selectContentId(params));
		
		storyFileService.deleteByMap(params);
		contentService.deleteByMap(params);
		storyCertService.deleteByIdMap(params);
		super.deleteByMap(params);
		
		return Result.success();
	}
	
	public List<SysDict> getStatus(String typeCode) {
		final String[] status = {"yfb", "dcl", "ych"};
		List<SysDict> list = dictService.selectByTypeCodeSimpleness(typeCode);
		Iterator<SysDict> it = list.iterator();
		while (it.hasNext()) {
			SysDict sysDict = it.next();
			if (!ArrayUtil.contains(status, sysDict.getCode())) {
				it.remove();
			}
		}
		return list;
	}
	
}
