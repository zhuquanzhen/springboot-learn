package com.huixdou.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.huixdou.api.bean.AppAccount;
import com.huixdou.common.base.BaseDao;

public interface AppAccountDao extends BaseDao<AppAccount> {
	
	Integer updateStatus(Map<String, Object> params);
	
	AppAccount selectByPhone(@Param("phone") String phone);
	
	Integer selectNumber(Map<String, Object> params);
	
	Integer deleteByIds (Map<String, Object> params);
	
	List<AppAccount> selectReceiver(Map<String, Object> params);
	
	Integer selectCount();
}
