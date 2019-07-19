/**
 * 
 */
package com.huixdou.api.bean;

import java.io.Serializable;

/**
 * @author jinxin 2019年1月22日下午4:23:46
 * 送检厂商表
 */
public class CerVendorInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	 
	private Integer id;
	
	private String name;

	private String vendorState;
	
	private String elec;
	
	
	public String getVendorState() {
		return vendorState;
	}

	public String getElec() {
		return elec;
	}

	public void setVendorState(String vendorState) {
		this.vendorState = vendorState;
	}

	public void setElec(String elec) {
		this.elec = elec;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
