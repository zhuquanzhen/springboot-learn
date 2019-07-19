package com.huixdou.api.web;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.CerInfoService;
import com.huixdou.common.base.BaseController;


@RestController
@RequestMapping("/sup/cer-info")
public class CerInfoController extends BaseController {
	
	@Autowired
	private CerInfoService cerInfoService;
	
	/**
	 * 查看证书详情
	 * @param id 主键ID
	 * @return
	 */
	@GetMapping("detail")
	public Object detail(@RequestParam("id") String id) {
		if (StringUtils.isBlank(id)) {
			return failure("id 参数不能为空");
		}
		return success(cerInfoService.selectById(id));
	}
	
	/**
	 * 通过证书编号获取证书列表
	 * @param no[]	证书编号数组
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(cerInfoService.selectPage(params));
	}
}
