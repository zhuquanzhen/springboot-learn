package com.huixdou.api.vo;

import java.io.Serializable;

public class MerInfoListVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String shortName;
	private String contact;
	private String contactNumber;
	private Integer vendorNum = 0;
	private Integer cerNum = 0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Integer getVendorNum() {
		return vendorNum;
	}

	public void setVendorNum(Integer vendorNum) {
		this.vendorNum = vendorNum;
	}

	public Integer getCerNum() {
		return cerNum;
	}

	public void setCerNum(Integer cerNum) {
		this.cerNum = cerNum;
	}
	
}
