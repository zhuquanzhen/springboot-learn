package com.huixdou.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.MainService;
import com.huixdou.common.base.BaseController;

@RestController
@RequestMapping("/sup")
public class MainController extends BaseController {
	
	@Autowired
	private MainService mainService;
	
	@GetMapping("index")
	public Object index() {
		return success(mainService.index());
	}
	
}
