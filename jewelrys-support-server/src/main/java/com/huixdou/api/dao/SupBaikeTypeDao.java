package com.huixdou.api.dao;

import com.huixdou.api.bean.SupBaikeType;
import com.huixdou.common.base.BaseDao;

public interface SupBaikeTypeDao  extends BaseDao<SupBaikeType>{
	
	SupBaikeType selectByName(String name);
}
