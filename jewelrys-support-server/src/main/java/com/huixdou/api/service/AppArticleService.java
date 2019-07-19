package com.huixdou.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.AppArticle;
import com.huixdou.api.dao.AppArticleDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.utils.DateUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

@Service
public class AppArticleService extends BaseService<AppArticleDao, AppArticle> {
	
	@Autowired
	private AppResultKeywordService keywordService;
	
	public AppArticle selectById(Integer id) {
		AppArticle entity = dao.selectById(id);
		if (ObjectUtil.isNotNull(entity)) {
			entity.setCreateDate(DateUtils.formatDate(Convert.toLong(entity.getCreateDate()), "yyyy-MM-dd HH:mm"));
		}
		return entity;
	}
	
	@Transactional(readOnly = false)
	public void create(AppArticle entity) {
		entity.setCreateDate(Convert.toStr(System.currentTimeMillis()));
		dao.insert(entity);
	}
	
	@Transactional(readOnly = false)
	public void updateArticle(AppArticle entity) {
		if (ObjectUtil.isNull(entity.getId()))
			throw new RuntimeException("id 参数不能为空");
		dao.update(entity);
	}
	
	@Transactional(readOnly = false)
	public void delete(Map<String, Object> params) {
		if (ObjectUtil.isNull(params.get("id")))
			throw new RuntimeException("id 参数不能为空");
		
		params.put("deleteTime", System.currentTimeMillis());
		dao.deleteByMap(params);
		keywordService.deleteByArtId(params);
	}
	
}
