package com.huixdou.api.bean;

import java.io.Serializable;
import java.util.Date;

public class SysUserToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer userId;

	// token
	private String token;

	// 过期时间
	private Date expireTime;

	// 更新时间
	private Date updateTime;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
