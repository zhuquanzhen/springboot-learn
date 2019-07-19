package com.huixdou.api.dao;

import com.huixdou.api.bean.SysConfig;
import com.huixdou.common.base.BaseDao;

public interface SysConfigDao extends BaseDao<SysConfig> {

	SysConfig selectByKey(String key);
	
	String selectDownLoadNum();
}
