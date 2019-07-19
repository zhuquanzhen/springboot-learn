package com.huixdou.api.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class LoginUserVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotBlank(message = "请输入旧密码")
	private String oldPassword;
	
	@NotBlank(message = "请输入新密码")
	private String newPassword;
	
	@NotBlank(message = "请确认密码")
	private String verifyPassword;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

}
