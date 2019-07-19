package com.huixdou.api.vo;

import java.io.Serializable;

public class MenuListVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String name;
	
	private String perms;
	
	private String url;
	
	private Integer orderNum;

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
