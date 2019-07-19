package com.huixdou.api.bean;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AppResultKeyword implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotBlank(message = "关键字不能为空")
	private String keyword;
	
	@NotNull(message = "文章id不能为空")
	private Integer appArticleId;
	
	@NotNull(message = "状态不能为空")
	private Integer status;
	
	private String statusName;
	
	private String createDate;
	
	private Integer createId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getAppArticleId() {
		return appArticleId;
	}

	public void setAppArticleId(Integer appArticleId) {
		this.appArticleId = appArticleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

}
