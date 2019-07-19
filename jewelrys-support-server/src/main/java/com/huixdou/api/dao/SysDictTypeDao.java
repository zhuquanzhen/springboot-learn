package com.huixdou.api.dao;

import com.huixdou.api.bean.SysDictType;
import com.huixdou.common.base.BaseDao;

public interface SysDictTypeDao extends BaseDao<SysDictType> {
	
	SysDictType selectByCode(String code);
	
}
