package com.huixdou.api.bean;

import java.io.Serializable;

public class SysSms implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String phone;
	
	private String content;
	
	private String typeCode;
	
	private String createDate;
	
	private Integer status;
	
	private String statusName;
	
	private String code;

	private String responseLog;
	
	private String sendIp;
	
	public String getResponseLog() {
		return responseLog;
	}

	public void setResponseLog(String responseLog) {
		this.responseLog = responseLog;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSendIp() {
		return sendIp;
	}

	public void setSendIp(String sendIp) {
		this.sendIp = sendIp;
	}

}
