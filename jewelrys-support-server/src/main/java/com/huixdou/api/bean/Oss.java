package com.huixdou.api.bean;

import java.io.Serializable;

public class Oss implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String url;

	private String path;

	private String suffix; // 后缀

	private String thumbpath; // 缩略图路径

	private String createDate;

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

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getThumbpath() {
		return thumbpath;
	}

	public void setThumbpath(String thumbpath) {
		this.thumbpath = thumbpath;
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Oss{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", url='" + url + '\'' + ", path='" + path
				+ '\'' + ", createDate='" + createDate + '\'' + '}';
	}
}
