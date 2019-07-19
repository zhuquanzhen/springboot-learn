package com.huixdou.common.aspect;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huixdou.api.bean.SysUser;
import com.huixdou.api.service.SysMenuService;
import com.huixdou.common.annotation.RequiresPermissions;
import com.huixdou.common.exception.ForbiddenException;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.HttpContextUtils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

@Aspect
@Component
public class PermissionsAspect {
	
	@Autowired
	private SysMenuService menuService;
	
	@Pointcut("@annotation(com.huixdou.common.annotation.RequiresPermissions)")
	public void permPointCut() { 
		
	}

	@Around("permPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		SysUser user = (SysUser) HttpContextUtils.getHttpServletRequest().getAttribute(Constant.SUP_SYS_USER);

		if (ObjectUtil.isNull(user)) {
			throw new ForbiddenException("权限非法，请认证当前用户");
		}

		List<String> perms = menuService.getMenuPerms(user.getId());

		// 获取注解内容
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		RequiresPermissions permissions = method.getAnnotation(RequiresPermissions.class);

		String value = permissions.value();
		if (StrUtil.isNotBlank(value) && !perms.contains(value)) {
			throw new ForbiddenException("权限非法，没有[" + value + "]访问权限");
		}

		// 执行方法
		Object result = point.proceed();

		return result;
	}

}
