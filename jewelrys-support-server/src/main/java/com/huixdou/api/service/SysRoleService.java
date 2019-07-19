package com.huixdou.api.service;

import com.google.common.collect.Maps;
import com.huixdou.api.bean.SysRole;
import com.huixdou.api.dao.SysRoleDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysRoleService extends BaseService<SysRoleDao, SysRole> {
	
	@Autowired
	private SysMenuService menuService;
	
	@Autowired
	private SysUserService userService;
	
	public List<SysRole> selectList(Map<String, Object> params) {
		return dao.selectList(params);
	}
    
	public SysRole detail(Integer id) {
		return dao.selectById(id);
	}
	
	@Transactional(readOnly = false)
	public Result create(SysRole role) {
		if (ObjectUtil.isNotNull(dao.selectByName(role.getName())))
			return Result.failure("角色已存在");
		dao.insert(role);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result updateRole(SysRole role) {
		if (StringUtils.isBlank(Convert.toStr(role.getId()))) {
			return Result.failure("id 参数不能为空");
		}
		SysRole roles = dao.selectByName(role.getName());
		if (ObjectUtil.isNotNull(roles)) {
			if (!role.getId().equals(roles.getId())) {
				return Result.failure("角色已存在");
			}
		}
		dao.update(role);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id")))) {
			return Result.failure("id 参数不能为空");
		}
		params.put("deleteTime", System.currentTimeMillis());
		dao.deleteRoleMenu(Convert.toInt(params.get("id")));
		dao.deleteByMap(params);
		userService.deleteUserRole(Convert.toInt(params.get("id")));
		return Result.success();
	}
    
	public Map<String, Object> getRole(Map<String, Object> params) {
		Map<String, Object> result = Maps.newHashMap();
		
		result.put("total", dao.selectCount());
		result.put("list", dao.selectList(params));
		return result;
	}
	
    public List<String> getName(Integer id) {
    	return dao.selectName(id);
    }
    
    public Map<String, Object> getMenu(Integer id) {
    	Map<String, Object> result = Maps.newHashMap();
    	result.put("list", dao.selectByRoleId(id));
    	return result;
    }
    
    @Transactional(readOnly = false)
    public Result insertRoleMenu(Map<String, Object> params) {
    	if (StrUtil.isBlank(Convert.toStr(params.get("roleId"))))
    		return Result.failure("roleId 参数不能为空");
    	dao.deleteRoleMenu(Convert.toInt(params.get("roleId")));
    	if (StrUtil.isBlank(Convert.toStr(params.get("menuId"))) || Convert.convert(List.class, params.get("menuId")).size() == 0) 
    		return Result.success();
    	List<Integer> menuId = Convert.toList(Integer.class, params.get("menuId"));
    	Set<Integer> menuIdList = Sets.newHashSet();
    	for (Integer id : menuId) {
    		menuIdList.addAll(menuService.getId(id));
    	}
    	params.replace("menuId", menuIdList);
    	dao.insertRoleMenu(params);
    	return Result.success();
    }
     
}
