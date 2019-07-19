package com.huixdou.api.bean;

import java.io.Serializable;

public class MerClerk implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String jobNum;
	
	private String name;
	
	private String phone;

	private String statusName;
	
	private Integer status;
	
	private String remarks;

	private Integer mertId;

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobNum() {
		return jobNum;
	}

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getMertId() {
		return mertId;
	}

	public void setMertId(Integer mertId) {
		this.mertId = mertId;
	}
}
