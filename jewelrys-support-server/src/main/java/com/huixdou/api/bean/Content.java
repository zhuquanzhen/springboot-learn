package com.huixdou.api.bean;

import java.io.Serializable;

public class Content implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String typeCode;
	
	private String content;
	
	private String apiLogId;
	
	private Integer checkStatus;

	
	public String getApiLogId() {
		return apiLogId;
	}

	public void setApiLogId(String apiLogId) {
		this.apiLogId = apiLogId;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
