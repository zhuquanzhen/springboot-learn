package com.huixdou.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huixdou.api.bean.ComArea;
import com.huixdou.common.base.BaseDao;

public interface ComAreaDao extends BaseDao<ComArea> {
	
	List<ComArea> selectByParentId(@Param("areaParentId")Integer areaParentId);
}
