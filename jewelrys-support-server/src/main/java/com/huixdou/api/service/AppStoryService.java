package com.huixdou.api.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.AppStory;
import com.huixdou.api.bean.SysConfig;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.AppStoryDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

@Service
public class AppStoryService extends BaseService<AppStoryDao, AppStory> {

	@Autowired
	private OssService ossService;

	@Autowired
	private SysDictService dictService;

	@Autowired
	private AppStoryFileService storyFileService;

	@Autowired
	private ContentService contentService;
	
	@Autowired 
	private SysConfigService SysConfigService;

	public List<AppStory> selectList(Map<String, Object> params) {
		List<AppStory> story = dao.selectList(params);
		for (AppStory item : story) {
			item.setFiles(null);
			SysDict dict = dictService.selectByTypeAndCode("app-story-status", item.getStatus());
			if (ObjectUtil.isNotNull(dict)) {
				item.setStatusName(dict.getName());
			}
			item.setCreateDate(DateUtils.formatDate(Convert.toLong(item.getCreateDate()), "yyyy-MM-dd HH:mm"));
		}
		return story;
	}

	// 更新故事状态
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

	public Result selectById(String id) {
		AppStory story = dao.selectById(id);
		if (ObjectUtil.isNull(story)) {
			return Result.failure("请输入正确的 id");
		}
		story.setFiles(ossService.getAddressMap(dao.selectFile(story.getId())));
		story.setCreateDate(DateUtils.formatDate(Convert.toLong(story.getCreateDate()), "yyyy-MM-dd HH:mm"));
		story.setLogo(ossService.getAddress(story.getLogo()));
		SysDict dict = dictService.selectByTypeAndCode("app-story-status", story.getStatus());
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
		storyFileService.delete(params);
		List<String> contentId = dao.selectContentId(params);
		params.put("contentId", contentId);
		contentService.deleteByMap(params);
		dao.deleteByMap(params);
		return Result.success();
	}

	@Transactional(readOnly = false)
	public void updatStatusByAppStory(AppStory appStory) {
		dao.updatStatusByAppStory(appStory);
	}
	
	
	/**
	 * 通过id会员发布的故事数量
	 * @param createId
	 * @return
	 *
	 */
	public Integer selectStoryNum(Integer createId){
		return dao.selectStoryNum(createId);
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
	
	public Map<String,String> getSwitchStatus(){
		Integer appStroyKey=0;
		SysConfig sysConfig=	SysConfigService.selectByKey("appStroy.switch");
		if(!org.springframework.util.StringUtils.isEmpty(sysConfig)){
			String cValue=sysConfig.getcValue();
			appStroyKey=Convert.toInt(cValue)==null?0:Convert.toInt(cValue);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("appStroyKey", appStroyKey+"");
		//0 是关闭显示 1 是开启显示
		return map;
	}
	
}
