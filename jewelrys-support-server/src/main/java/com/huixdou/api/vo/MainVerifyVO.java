package com.huixdou.api.vo;

import java.io.Serializable;

public class MainVerifyVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
