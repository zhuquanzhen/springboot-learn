package com.huixdou.api.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.SysDictTypeService;
import com.huixdou.common.base.BaseController;

@RestController
@RequestMapping("/sup/sys-dict-type")
public class SysDictTypeController extends BaseController {
	
	@Autowired
	private SysDictTypeService dictTypeService;
	
	/**
	 * 获取字典列表
	 * @param params
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(dictTypeService.selectList(params));
	}
}
