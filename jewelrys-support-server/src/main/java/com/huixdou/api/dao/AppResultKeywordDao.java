package com.huixdou.api.dao;

import java.util.Map;

import com.huixdou.api.bean.AppResultKeyword;
import com.huixdou.common.base.BaseDao;

public interface AppResultKeywordDao extends BaseDao<AppResultKeyword> {
	
	Integer deleteByArtId(Map<String, Object> params);
	
	AppResultKeyword selectByKeyword(String keyword);
	
	Integer selectByArtId(Integer artId);
}
