package com.huixdou.api.web;


import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.AppAccountService;
import com.huixdou.common.base.BaseController;

import static com.huixdou.common.utils.Constant.*;

@RestController
@RequestMapping("/sup/app-account")
public class AppAccountController extends BaseController {
	
	@Autowired
	private AppAccountService accountService;
	
	/**
	 * 获取会员列表
	 * @param pageNum	页数
	 * @param pageSize	每页显示大小
	 * @param beginDate	注册开始时间
	 * @param endDate	注册结束时间
	 * @param status	状态
	 * @param val		查询字段项
	 * @return
	 */
	//@RequiresPermissions("customer-account-list")
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(accountService.selectPage(params));
	}
	
	/**
	 * 获取会员详情
	 * @param id	主键
	 * @return
	 */
	@GetMapping("detail")
	public Object detail(String id) {
		return accountService.selectById(id);
	}
	
	/**
	 * 启用
	 * @param ids[]	id数组
	 * @return
	 */
	//@Requirepermin("JCZXGL0001")
	@PostMapping("enable")
	public Object enable(@RequestBody Map<String, Object> params) {
		params.put("type",ENABLE);
		return accountService.updateStatus(params);
	}
	
	/**
	 * 禁用
	 * @param ids[]	id数组
	 * @return
	 */
	//@Requirepermin("JCZXGL0001")
	@PostMapping("prohibit")
	public Object prohibit(@RequestBody Map<String, Object> params) {
		params.put("type",DISABLE);
		return accountService.updateStatus(params);
	}
	
	/**
	 * 删除
	 * @param ids[]	id数组	
	 * @return
	 */
	@PostMapping("delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		return accountService.deleteByIds(params);
	}
	
}
