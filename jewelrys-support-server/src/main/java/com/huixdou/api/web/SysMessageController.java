package com.huixdou.api.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.SysMessageService;
import com.huixdou.common.base.BaseController;

@RestController
@RequestMapping("/sup/sys-message")
public class SysMessageController extends BaseController {

	@Autowired
	private SysMessageService messageService;

	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {

		return success(messageService.selectPage(params));
	}

	@GetMapping("details")
	public Object details(@RequestBody Map<String, Object> params) {
		return messageService.details(params);
	}

}
