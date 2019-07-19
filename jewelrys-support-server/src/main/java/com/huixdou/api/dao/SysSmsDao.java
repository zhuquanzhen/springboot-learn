package com.huixdou.api.dao;

import org.apache.ibatis.annotations.Param;

import com.huixdou.api.bean.SysSms;
import com.huixdou.common.base.BaseDao;

public interface SysSmsDao extends BaseDao<SysSms> {
	
	SysSms selectNewestByPhoneAndTypeCode(@Param("phone") String phone, @Param("typeCode") String typeCode);

	SysSms selectNewsByPhone(@Param("phone") String phone);
	
	Integer selectNumberByIpAndTime(@Param("ip") String ip,@Param("time") long time);
}
