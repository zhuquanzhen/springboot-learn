package com.huixdou.api.dao;

import java.util.List;
import java.util.Map;

import com.huixdou.api.bean.MerClerk;
import com.huixdou.common.base.BaseDao;

public interface MerClerkDao extends BaseDao<MerClerk>{
	
	List<String> selectByMertId(Integer id);
	
	Integer deleteMerClerkByMertId(Map<String, Object> params);
}
