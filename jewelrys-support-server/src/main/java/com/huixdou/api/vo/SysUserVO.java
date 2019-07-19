package com.huixdou.api.vo;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SysUserVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotBlank(message = "用户账号不能为空")
	@Size(max = 16, message="用户账号最多输入16个字符")
	private String username;
	
	@NotBlank(message = "密码不能为空")
	private String password;
	
	@NotBlank(message = "确认密码不能为空")
	private String verifyPassword;
	
	@NotBlank(message = "昵称不能为空")
	@Size(max = 10, message="昵称最多输入10个字符")
	private String nickname;
	
	@NotBlank(message = "联系电话不能为空")
	private String phone;
	
	@NotBlank(message = "头像不能为空")
	private String avatar;
	
	@Email(message = "邮箱地址不正确")
	@Size(max = 50, message="邮箱最多输入50个字符")
	private String email;
	
	@Size(max = 60, message="备注最多输入60个字符")
	private String remarks;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
