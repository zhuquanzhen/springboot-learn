package com.huixdou.api.dao;

import com.huixdou.api.bean.SysRole;
import com.huixdou.common.base.BaseDao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysRoleDao extends BaseDao<SysRole>{

   Integer deleteRoleMenu(Integer id);

   SysRole selectByName(String name);
   
   Integer selectCount();
   
   Set<String> selectByRoleId(Integer id);
   
   Integer insertRoleMenu(Map<String, Object> params);
   
   List<String> selectName(Integer id);
  
 }
