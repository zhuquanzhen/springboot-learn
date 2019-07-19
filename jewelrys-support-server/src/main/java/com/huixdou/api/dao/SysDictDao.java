package com.huixdou.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huixdou.api.bean.SysDict;
import com.huixdou.common.base.BaseDao;

public interface SysDictDao extends BaseDao<SysDict> {
	
	List<SysDict> selectByTypeCode(String typeCode);
	
	List<SysDict> selectByTypeCodeSimpleness(String typeCode);
	
	List<SysDict> selectByParentId(Integer id);
	
	// 与selectByTypeAndCode查询结果不一致
	SysDict selectByTypeCodeAndCode(@Param("typeCode") String typeCode, @Param("code") String code);

	SysDict selectByTypeAndCode(@Param("typeCode") String typeCode, @Param("code") String code);
	
	List<Integer> findByParentId(Integer parentId);
	
	String selectParentName(Integer id);

}
