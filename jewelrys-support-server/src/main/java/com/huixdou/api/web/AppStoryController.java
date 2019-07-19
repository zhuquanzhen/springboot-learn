package com.huixdou.api.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.AppStoryService;
import com.huixdou.common.annotation.RequiresPermissions;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

@RestController
@RequestMapping("/sup/app-story")
public class AppStoryController extends BaseController {
	
	@Autowired
	private AppStoryService storyService;
	
	/**
	 * 获取故事列表
	 * @param pageNum   页码
	 * @param pageSize  每页数据量
	 * @param phone     发布人
	 * @param merName   珠宝商
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param status    状态
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(storyService.selectPage(params));
	}
	
	/**
	 * 发布故事
	 * @param id[] 		主键ID数组
	 * @return
	 */
	@RequiresPermissions(value = "mer-story-account-fb")
	@PostMapping("publish")
	public Object publish(@RequestBody Map<String, Object> params) {
		params.put("auditId", getUserId());
		return storyService.updateStatus(params, Constant.PUBLISH);
	}
	
	/**
	 * 撤回故事
	 * @param id[] 		主键ID数组
	 * @return
	 */
	@PostMapping("failure")
	public Object failure(@RequestBody Map<String, Object> params) {
		if (StrUtil.isBlank(Convert.toStr(params.get("reject"))))
			return failure("请填写撤回原因");
		params.put("auditId", getUserId());
		return storyService.updateStatus(params, Constant.REVOKE);
	}
	
	/**
	 * 获取文章详情
	 * @param id 主键ID
	 * @return
	 */
	@GetMapping("detail")
	public Object detail(@RequestParam("id") String id) {
		if (StringUtils.isBlank(id)) {
			return failure("id 参数不能为空");
		}
		return storyService.selectById(id);
	}
	
	/**
	 * 删除故事
	 * @param id[] 主键ID数组
	 * @return
	 */
	@PostMapping("delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		return storyService.delete(params);
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
		return success(storyService.getStatus(typeCode));	
	}
	
	/**
	 * 获取状态
	 * @param typeCode
	 * @return
	 */
	@GetMapping("get-switch-status")
	public Object getSwitchStatus() {
		return success(storyService.getSwitchStatus());	
	}
	
}
