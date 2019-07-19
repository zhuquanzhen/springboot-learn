package com.huixdou.api.vo;

import java.io.Serializable;

public class MainMerVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer num;
	
	private String name;
	
	private Integer saleNum;

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
