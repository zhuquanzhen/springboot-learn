package com.huixdou.api.dao;

import org.apache.ibatis.annotations.Param;

import com.huixdou.api.bean.SysIpBlacklist;
import com.huixdou.common.base.BaseDao;

public interface SysIpBlacklistDao extends BaseDao<SysIpBlacklist> {
	
	SysIpBlacklist selectByIp(@Param("ip") String ip);
	

}
