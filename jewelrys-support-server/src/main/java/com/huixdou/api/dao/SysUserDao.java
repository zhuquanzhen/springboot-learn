package com.huixdou.api.dao;

import java.util.List;
import java.util.Map;

import com.huixdou.api.bean.SysUser;
import com.huixdou.common.base.BaseDao;

public interface SysUserDao extends BaseDao<SysUser> {
	
	SysUser selectByUserName(String username);
	
	SysUser selectByPhone(String phone);
	
	Integer insertUserRole(Map<String, Object> params);
	
	Integer deleteUserRole(Map<String, Object> params);
	
	List<Integer> selectUserRole(Integer id);
	
	Integer deleteByRoleId(Integer id);
}
