package com.huixdou.api.dao;

import java.util.List;

import com.huixdou.api.bean.CerVerify;
import com.huixdou.common.base.BaseDao;

public interface CerVerifyDao extends BaseDao<CerVerify> {
	
	List<CerVerify> selectMonthCount();
	
	Integer selectByAppChannel();
	
	Integer selectByOtherChannel();
	
	List<CerVerify> selectTypeCodeCount();
	
	List<CerVerify> selectMertCount();
	
	Integer selectNum(Integer createId);
}
