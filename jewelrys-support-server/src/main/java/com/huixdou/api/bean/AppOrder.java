package com.huixdou.api.bean;

import java.io.Serializable;

public class AppOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 月份
	private Integer month;
	
	// 数量
	private Integer count;

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
