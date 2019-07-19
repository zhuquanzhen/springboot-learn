package com.huixdou.api.dao;

import java.util.List;

import com.huixdou.api.bean.AppOrder;
import com.huixdou.common.base.BaseDao;

public interface AppOrderDao extends BaseDao<AppOrder>{

	Integer selectCount();
	
	List<AppOrder> selectMonthCount();
	
	Integer selectByCreateIdList(Integer id);
}
