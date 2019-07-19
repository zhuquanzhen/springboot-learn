package com.huixdou.api.bean;

import java.io.Serializable;

public class CerVerify implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String certNo;
	
	private String certType;
	
	private String typeCode;
	
	private String channel;
	
	private Integer mertId;
	
	private String createDate;
	
	private Integer createId;
	
	private Integer showFlag;
	
	private Integer month;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getMertId() {
		return mertId;
	}

	public void setMertId(Integer mertId) {
		this.mertId = mertId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public Integer getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

}
