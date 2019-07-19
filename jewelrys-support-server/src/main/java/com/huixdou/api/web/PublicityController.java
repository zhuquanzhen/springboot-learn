package com.huixdou.api.web;

import com.huixdou.api.service.PublicityService;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sup/mer-publicity")
public class PublicityController extends BaseController {

	@Autowired
	private PublicityService publicityService;

	/**
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param publicityStatus
	 *            状态
	 * @param name
	 *            名称
	 * @return
	 */
	// 显示和查询显示List
	@GetMapping("/list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(publicityService.selectPage(params));
	}

	/**
	 * 
	 * @param id
	 *            审核的id
	 * @return
	 */
	// 审核通过
	@PostMapping("/publish")
	public Object publish(@RequestBody Map<String, Object> params) {
		return publicityService.update(params, Constant.PUBLISH);
	}

	/**
	 * 
	 * @param publicityReject
	 *            撤回理由
	 * @param id
	 *            要撤回的id
	 * @return
	 */
	// 撤回
	@PostMapping("failure")
	public Object failure(@RequestBody Map<String, Object> params) {
		if (StrUtil.isBlank(Convert.toStr(params.get("publicityReject"))))
			return failure("请填写撤回原因");
		return publicityService.update(params, Constant.REVOKE);
	}
	
	/**
	 * 获取状态
	 * @param typeCode
	 * @return
	 */
	@GetMapping("get-status")
	public Object getStatus(@RequestParam("typeCode") String typeCode) {
		if (StringUtils.isBlank(typeCode))
			return failure("typeCode 参数不能为空");
		return success(publicityService.getStatus(typeCode));	
	}
}
