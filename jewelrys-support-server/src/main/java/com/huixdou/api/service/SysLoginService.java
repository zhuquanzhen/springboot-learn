package com.huixdou.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.huixdou.api.bean.SysUser;
import com.huixdou.api.bean.SysUserToken;
import com.huixdou.api.vo.LoginForm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;

@Service
public class SysLoginService {
	
	@Autowired
	private SysUserService userService;
	
	@Autowired
	private SysUserTokenService tokenService;
	
	@Autowired
	private SysMenuService menuService;
	
	@Transactional(readOnly = false)
	public Map<String, Object> login(LoginForm form) {
		SysUser user = userService.selectByUserName(form.getUsername());
		if (ObjectUtil.isNull(user)) 
			throw new RuntimeException("账号或密码不正确");
		if (ObjectUtil.isNull(user.getPassword()) || !user.getPassword().equalsIgnoreCase(SecureUtil.md5(user.getSalt() + form.getPassword())))
			throw new RuntimeException("账号或密码不正确");
		if (userService.getUserRole(user.getId()).size() == 0)
			throw new RuntimeException("账户信息获取失败, 请联系管理员");
		if (user.getStatus() == 0)
			throw new RuntimeException("账号已被锁定,请联系管理员");

		SysUserToken userToken = tokenService.createToken(user.getId());
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("token", userToken == null ? null : userToken.getToken());
		return result;
	}
	
	public Map<String, Object> getLoginInfo(Integer id) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("nav", menuService.getUserMenuList(id));
		result.put("perms", menuService.getMenuPerms(id));
		result.put("user", userService.getUser(id));
		return result;
	}
	
}	
