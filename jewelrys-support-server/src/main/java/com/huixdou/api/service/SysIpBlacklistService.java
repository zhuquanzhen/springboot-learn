package com.huixdou.api.service;

import com.huixdou.common.base.BaseService;
import com.huixdou.api.bean.SysIpBlacklist;
import com.huixdou.api.dao.SysIpBlacklistDao;
import org.springframework.stereotype.Service;

@Service
public class SysIpBlacklistService extends BaseService<SysIpBlacklistDao,SysIpBlacklist> {
	
	public SysIpBlacklist  selectByIp(String ip){
		return dao.selectByIp(ip);
	}
	

}
