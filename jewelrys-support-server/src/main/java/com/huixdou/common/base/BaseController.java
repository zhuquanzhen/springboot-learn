package com.huixdou.common.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import com.huixdou.api.bean.SysUser;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.HttpContextUtils;

public abstract class BaseController {
	
	protected Object success() {
		return Result.success();
	}
	
	protected Object success(Object data) {
		Result result = Result.success();
		if (data != null) {
			if (data instanceof Page) {
				Page<?> page = (Page<?>) data;
				Map<String, Object> table = Maps.newHashMap();
				table.put("total", page.getTotal());
				table.put("list", page.getResult());
				result.setData(table);
			} else {
				result.setData(data);
			}
		}
		return result;
	}
	
	protected Object failure(String message) {
		return Result.failure(message);
	}
	
	protected Integer getUserId() {
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		SysUser user = (SysUser) request.getAttribute(Constant.SUP_SYS_USER);
		return user.getId();
	}
	
}
