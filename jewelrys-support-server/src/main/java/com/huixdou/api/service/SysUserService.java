package com.huixdou.api.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.SysDict;
import com.huixdou.api.bean.SysUser;
import com.huixdou.api.dao.SysUserDao;
import com.huixdou.api.vo.LoginUserVO;
import com.huixdou.api.vo.SysUserUpdateVO;
import com.huixdou.api.vo.SysUserVO;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.Constant;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;

@Service
public class SysUserService extends BaseService<SysUserDao, SysUser>{
	
	// private final String passwordPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
	
	private final String passwordPattern = "^[0-9a-zA-Z]{6,18}";
	
	@Autowired
	private SysRoleService roleService;
	
	@Autowired
	private SysDictService dictService;
	
	@Autowired
	private OssService ossService;
	
	public List<SysUser> selectList(Map<String, Object> params) {
		List<SysUser> user = dao.selectList(params);
		for (SysUser item : user) {
			item.setRoleName(roleService.getName(item.getId()));
			SysDict dict = dictService.selectByTypeAndCode("sys-user-status", item.getStatus()+"");
			if (ObjectUtil.isNotNull(dict)) {
				item.setStatusName(dict.getName());
			}
			item.setFiles(null);
		}
		return user;
	}	
	
	public SysUser getUser(Integer id) {
		SysUser user = dao.selectById(id);
		user.setRoleName(roleService.getName(user.getId()));
		SysDict dict = dictService.selectByTypeAndCode("sys-user-status", user.getStatus()+"");
		if (ObjectUtil.isNotNull(dict)) {
			user.setStatusName(dict.getName());
		}
		user.setFiles(ossService.getAddressMap(user.getAvatar()));
		return user;
	}
	
	public SysUser selectByUserName(String username) {
		return dao.selectByUserName(username);
	}
	
	@Transactional(readOnly = false)
	public Result create(SysUserVO userVO) {
		if (ObjectUtil.isNotNull(dao.selectByUserName(userVO.getUsername())))
			return Result.failure("用户账号已存在");
		String password = userVO.getPassword();
		SysUser user = new SysUser();
		BeanUtil.copyProperties(userVO, user);
		if (verifyPassword(user) && verifyOther(user)) {
			String salt = RandomUtil.randomString(32);
			user.setPassword(SecureUtil.md5(salt + password));
			user.setSalt(salt);
			dao.insert(user);
		}
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result updateUser(SysUserUpdateVO userVO) {
		SysUser entity = dao.selectByUserName(userVO.getUsername());
		if (ObjectUtil.isNotNull(entity)) {
			if (!entity.getId().equals(userVO.getId())) {
				return Result.failure("用户账号已存在");
			}
		}
		SysUser user = new SysUser();
		BeanUtil.copyProperties(userVO, user);
		if (StringUtils.isNoneBlank(user.getPassword(), user.getVerifyPassword())) {
			verifyPassword(user);
			user.setPassword(SecureUtil.md5(dao.selectById(user.getId()).getSalt() + user.getPassword()));
		}
		verifyOther(user);
		super.update(user);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		params.put("deleteTime", System.currentTimeMillis());
		dao.deleteUserRole(params);
		dao.deleteByMap(params);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result updateStatus(Map<String, Object> params, Integer status) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		params.put("status", status);
		dao.updateByMap(params);
		return Result.success();
	}
	
	public Map<String, Object> getRole(Map<String, Object> params) {
		return roleService.getRole(params);
	}
	
	@Transactional(readOnly = false)
	public Result insertUserRole(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id")))) {
			return Result.failure("id 参数不能为空");
		}
		if (StringUtils.isBlank(Convert.toStr(params.get("roleId"))) || Convert.convert(List.class, params.get("roleId")).size() == 0) {
			return Result.failure("roleId 参数不能为空");
		}
		// 删除操作需要id 为数组，更新时需要的id 为Integer
		Integer id = Convert.toInt(params.get("id"));
		params.replace("id", Convert.convert(List.class, params.get("id")));
		dao.deleteUserRole(params);
		params.replace("id", id);
		dao.insertUserRole(params);
		return Result.success();
	}
	
	// 运营平台当前登录用户修改密码
	@Transactional(readOnly = false)
	public Result changePassword(LoginUserVO userVO) {
		SysUser user = dao.selectById(userVO.getId());
		if (!user.getPassword().equals(SecureUtil.md5(user.getSalt() + userVO.getOldPassword()))) {
			return Result.failure("原密码输入错误");
		}
		user.setPassword(userVO.getNewPassword());
		user.setVerifyPassword(userVO.getVerifyPassword());
		verifyPassword(user);
		SysUser item = new SysUser();
		item.setId(userVO.getId());
		item.setPassword(SecureUtil.md5(user.getSalt() + userVO.getNewPassword()));
		super.update(item);
		return Result.success();
	}
	
	// 删除角色的同时删除用户的角色
	@Transactional(readOnly = false)
	public void deleteUserRole(Integer id) {
		dao.deleteByRoleId(id);
	}
	
	// 登录时检查用户角色设置情况
	public List<Integer> getUserRole(Integer id) {
		return dao.selectUserRole(id);
	}
	
	private boolean verifyPassword(SysUser user) {
		if (!Pattern.matches(passwordPattern, user.getPassword())) {
			throw new RuntimeException("密码为6~18位数字或字母的组合");
		}
		if (!user.getPassword().equals(user.getVerifyPassword())) {
			throw new RuntimeException("密码输入不一致");
		}
		return true;
	}
	
	private boolean verifyOther(SysUser user) {
		if (StringUtils.isNotBlank(user.getPhone())) {
			if (!Pattern.matches(Constant.REGEX_MOBILE, user.getPhone())) {
				throw new RuntimeException("请输入正确的手机号");
			}
		}
		return true;
	}
	
}
