package com.huixdou.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import com.huixdou.api.bean.SysUser;
import com.huixdou.api.bean.SysUserToken;
import com.huixdou.api.service.SysUserService;
import com.huixdou.api.service.SysUserTokenService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.HttpContextUtils;
import com.huixdou.common.utils.JsonMapper;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.util.StrUtil;

public class SupFilter extends OncePerRequestFilter {

	private UrlPathHelper pathHelper = new UrlPathHelper();

	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	String[] excludeUrls = { "/sup/login", "/sup/sys-dict/get?typeCode={typeCode}"};
	//,"/check/test/result" 

	@Autowired
	private SysUserTokenService tokenService;
	
	@Autowired
	private SysUserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1.请求过滤
		String path = pathHelper.getServletPath(request);

		for (String url : excludeUrls) {
			if (antPathMatcher.match(url, path)) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		
		// 2.token 认证
		String token = getRequestToken(request);
		if (StrUtil.isBlank(token)) {
			renderToUnauthorized(response, "invalid token");
			return;
		}
		
		SysUserToken userToken = tokenService.selectByToken(token);
		if (userToken == null || userToken.getExpireTime().getTime() < System.currentTimeMillis()) {
			renderToUnauthorized(response, "token失效，请重新登录");
			return;
		}
		
		SysUser user = userService.selectById(userToken.getUserId());
		if (user.getStatus() == 0) {
			renderToUnauthorized(response, "账号已被锁定,请联系管理员");
			return;
		}
		
		request.setAttribute(Constant.SUP_SYS_USER, user);
		
		filterChain.doFilter(request, response);
		
	}

	private String getRequestToken(HttpServletRequest httpRequest) {
		// 从header中获取token
		String token = httpRequest.getHeader("token");
		// 如果header中不存在token，则从参数中获取token
		if (StringUtils.isBlank(token)) {
			token = httpRequest.getParameter("token");
		}
		return token;
	}
	
	private void renderToUnauthorized(ServletResponse response, String msg) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json;charset=utf-8");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
		
		try {
			httpResponse.getWriter().print(JsonMapper.toJsonString(new Result(HttpStatus.UNAUTHORIZED.value(), msg)));
			httpResponse.flushBuffer();
		} catch (IOException e) {

		}
	}
	
}
