package com.huixdou.api.bean;

import java.io.Serializable;

public class SysApiLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String name;
	
	private String url;
	
	private String requestTime;
	
	private String ip;
	
	private String time;
	
	private String inParams;
	
	private String outParams;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getInParams() {
		return inParams;
	}

	public void setInParams(String inParams) {
		this.inParams = inParams;
	}

	public String getOutParams() {
		return outParams;
	}

	public void setOutParams(String outParams) {
		this.outParams = outParams;
	}

}
