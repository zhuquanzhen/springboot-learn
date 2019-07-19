package com.huixdou.api.web;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.SysLoginService;
import com.huixdou.api.service.SysUserTokenService;
import com.huixdou.api.vo.LoginForm;
import com.huixdou.common.base.BaseController;

@RestController
@RequestMapping("/sup")
public class LoginController extends BaseController {
	
	@Autowired
	private SysLoginService loginService;
	
	@Autowired
	private SysUserTokenService tokenService;
	
	/**
	 * 运营平台用户登录
	 * @param username	用户名
	 * @param password	密码
	 * @return
	 */
	@PostMapping("/login")
	public Object login(@RequestBody @Valid LoginForm form) {
		return success(loginService.login(form));
	}
	
	/**
	 * 运营平台登录获取登录信息
	 * @return
	 */
	@GetMapping("get-login-info")
	public Object getLoginInfo() {
		return success(loginService.getLoginInfo(getUserId()));
	}
	
	/**
	 * 运营平台退出登录
	 * @return
	 */
	@GetMapping("logout")
	public Object logout() {
		tokenService.removeToken(getUserId());
		return success();
	}
	
}
