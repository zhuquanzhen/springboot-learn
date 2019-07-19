package com.huixdou.api.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.bean.SysApiLog;
import com.huixdou.api.service.SysApiLogService;
import com.huixdou.common.base.BaseController;

@RestController
@RequestMapping("/sup/sys-api-log")
public class SysApiLogController extends BaseController {
	
	@Autowired
	SysApiLogService apiLogService;
	
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(apiLogService.selectPage(params));
	}
	
	@PostMapping("insert")
	public Object insert(@RequestBody SysApiLog apiLog) {	
		return apiLogService.create(apiLog);
	}
}
