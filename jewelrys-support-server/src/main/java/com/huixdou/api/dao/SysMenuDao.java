package com.huixdou.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huixdou.api.bean.SysMenu;
import com.huixdou.common.base.BaseDao;

public interface SysMenuDao extends BaseDao<SysMenu> {

	List<Integer> selectAllMenuId(Integer userId);

	List<SysMenu> findByParentId(Integer parentId);
	
	Integer selectParentIdCount();
	
	List<SysMenu> selectAllBtn(Integer parentId);
	
	Integer selectBtnCount(Integer parentId);
	
	SysMenu selectByParentIdAndName(@Param("parentId") Integer parentId, @Param("name") String name);
	
	List<SysMenu> selectByIdList(@Param("idList") List<Integer> idList);
	
}
