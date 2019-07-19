package com.huixdou.api.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.SysUserService;
import com.huixdou.api.vo.LoginUserVO;
import com.huixdou.api.vo.SysUserUpdateVO;
import com.huixdou.api.vo.SysUserVO;
import com.huixdou.common.annotation.RequiresPermissions;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;

@RestController
@RequestMapping("/sup/sys-user")
public class      SysUserController extends BaseController {
	
	@Autowired
	private SysUserService userService;
	
	/**
	 * 获取用户列表
	 * @param pageNum 	页码
	 * @param pageSize 	每页数据量
	 * @param status  	状态
	 * @param val		查询条件
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(userService.selectPage(params));
	}
	
	/**
	 * 当前登录用户修改密码
	 * @param userVO
	 * @return
	 */
	@PostMapping("change-pwd")
	public Object changePassword(@RequestBody LoginUserVO userVO) {
		userVO.setId(getUserId());
		return userService.changePassword(userVO);
	}
	
	/**
	 * 查看某个用户的详细信息
	 * @param id
	 * @return
	 */
	@GetMapping("get")
	public Object get(@RequestParam("id") Integer id) {
		if (StringUtils.isBlank(Convert.toStr(id))) {
			return failure("id 参数不能为空");
		}
		return success(userService.getUser(id));
	}
	
	/**
	 * 获取角色列表
	 * @param params
	 * @return
	 */
	@GetMapping("get-role")
	public Object getRole(@RequestParam Map<String, Object> params) {
		return success(userService.getRole(params));
	}
	
	
	/**
	 * 增加用户信息
	 * @param user
	 * @return
	 */
	@RequiresPermissions(value = "sys-user-add")
	@PostMapping("insert")
	public Object insert(@RequestBody @Valid SysUserVO userVO) {
		return userService.create(userVO);
	}
	
	/**
	 * 删除用户
	 * @param id[] 主键ID数组
	 * @return
	 */
	@RequiresPermissions(value = "sys-user-delete")
	@PostMapping("delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		return userService.delete(params);
	}
	
	/**
	 * 启用
	 * @param id[] 主键ID数组
	 * @return
	 */
	@RequiresPermissions(value = "sys-user-enable")
	@PostMapping("enable")
	public Object enable(@RequestBody Map<String, Object> params) {
		return userService.updateStatus(params, Constant.ENABLE);
	}
	
	/**
	 * 禁用
	 * @param id[] 主键ID数组
	 * @return
	 */
	@RequiresPermissions(value = "sys-user-disable")
	@PostMapping("disable")
	public Object disable(@RequestBody Map<String, Object> params) {
		return userService.updateStatus(params, Constant.DISABLE);
	}
	
	/**
	 * 设置用户的角色
	 * @param id  用户主键id
	 * @param roleId[]  角色主键ID数组
	 * @return
	 */
	@RequiresPermissions(value = "sys-user-edit-role")
	@PostMapping("insert-user-role")
	public Object insertUserRole(@RequestBody Map<String, Object> params) {
		return userService.insertUserRole(params);
	}
	
	/**
	 * 更新用户信息
	 * @param userVO
	 * @return
	 */
	@RequiresPermissions(value = "sys-user-edit")
	@PostMapping("update")
	public Object update(@RequestBody @Valid SysUserUpdateVO userVO) {
		return userService.updateUser(userVO);
	}
}
