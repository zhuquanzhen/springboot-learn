package com.huixdou.api.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.huixdou.api.bean.AppResultKeyword;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.AppResultKeywordDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.utils.DateUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

@Service
public class AppResultKeywordService extends BaseService<AppResultKeywordDao, AppResultKeyword> {
	
	@Autowired
	private SysDictService dictService;
	
	public List<AppResultKeyword> selectList(Map<String, Object> params) {
		if (ObjectUtil.isNull(params.get("artId")))
			throw new RuntimeException("artId 参数不能为空");
		List<AppResultKeyword> result = dao.selectList(params);
		for (AppResultKeyword item : result) {
			SysDict dict = dictService.selectByTypeAndCode("keyword-status", item.getStatus()+"");
			if (ObjectUtil.isNotNull(dict)) {
				item.setStatusName(dict.getName());
			}
			item.setCreateDate(DateUtils.formatDate(Convert.toLong(item.getCreateDate()), "yyyy-MM-dd HH:mm"));
		}
		return result;
	}
	
	public AppResultKeyword selectById(Integer id) {
		AppResultKeyword entity = dao.selectById(id);
		if (ObjectUtil.isNotNull(entity)) {
			SysDict dict = dictService.selectByTypeAndCode("keyword-status", entity.getStatus()+"");
			if (ObjectUtil.isNotNull(dict)) {
				entity.setStatusName(dict.getName());
			}
			entity.setCreateDate(DateUtils.formatDate(Convert.toLong(entity.getCreateDate()), "yyyy-MM-dd HH:mm"));
		}
		return entity;
	}
	
	public Map<String, Integer> selectByArtId(Integer artId) {
		Map<String, Integer> count = Maps.newHashMap();
		count.put("count", dao.selectByArtId(artId));
		return count;
	}
	
	@Transactional(readOnly = false)
	public void updateStatus(Map<String, Object> params, Integer status) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			throw new RuntimeException("id 参数不能为空");
		}
		
		params.put("status", status);
		dao.updateByMap(params);
	}
	
	@Transactional(readOnly = false)
	public void create(AppResultKeyword entity) {
		if (ObjectUtil.isNotNull(dao.selectByKeyword(entity.getKeyword())))
			throw new RuntimeException("关键字重复");
		entity.setCreateDate(Convert.toStr(System.currentTimeMillis()));
		dao.insert(entity);
	}
	
	@Transactional(readOnly = false)
	public void updateEntity(AppResultKeyword entity) {
		if (ObjectUtil.isNull(entity.getId()))
			throw new RuntimeException("id 参数不能为空");
		dao.update(entity);
	}
	
	@Transactional(readOnly = false)
	public void delete(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			throw new RuntimeException("id 参数不能为空");
		}
		
		params.put("deleteTime", System.currentTimeMillis());
		dao.deleteByMap(params);
	}
	
	@Transactional(readOnly = false)
	public void deleteByArtId(Map<String, Object> params) {
		dao.deleteByArtId(params);
	}
	
}
