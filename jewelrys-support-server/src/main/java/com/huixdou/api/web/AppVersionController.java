package com.huixdou.api.web;

import com.huixdou.api.bean.AppVersion;
import com.huixdou.api.service.AppVersionService;
import com.huixdou.common.base.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by jinxin on 2018/12/27 18:23.
 */
@RequestMapping("/sup/app-version")
@RestController
public class AppVersionController extends BaseController {

	@Autowired
	private AppVersionService appVersionService;

	/**
	 * 添加版本管理
	 * 
	 * @param version
	 *            版本号
	 * @param content
	 *            更新说明内容
	 * @return
	 */
	@PostMapping("insert")
	public Object insert(@Valid @RequestBody AppVersion appVersion) {
		return appVersionService.insertVersion(appVersion);
	}

	/**
	 * 查询版本管理列表
	 * 
	 * @param startDate
	 *            查询开始时间
	 * @param endDate
	 *            查询的结束时间
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            每页显示数据 如果不传，默认为5
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(appVersionService.selectPage(params));
	}

	/**
	 * 更新版本管理
	 * 
	 * @param version
	 *            版本号
	 * @param content
	 *            更新说明内容
	 * @param id
	 *            要更新的主键id
	 * @return
	 */
	@PostMapping("update")
	public Object update(@Valid @RequestBody AppVersion appVersion) {
		return appVersionService.updateVersion(appVersion);
	}

	/**
	 * 批量删除
	 * 
	 * @param Ids
	 *            app版本管理表主键id集合
	 * @return
	 */
	@PostMapping("delete")
	public Object deleteByIds(@RequestBody Map<String, Object> params) {
		Integer delectId = getUserId();
		params.put("deleteId", delectId);
		return appVersionService.deleteByIds(params);
	}
	
	/**
	 * 
	 * @param id app版本管理表主键id
	 * @return
	 *
	 */
	@GetMapping("details")
	public Object details(String id){
		return appVersionService.details(id);
	}
}
