package com.huixdou.api.vo;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MenuDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotBlank(message = "名称不能为空")
	private String name;
	
	@NotBlank(message = "类型不能为空")
	private String typeCode;
	
	@Max(value = 1000, message="排序值最大值为1000")
	private Integer orderNum;
	
	@NotNull(message = "父级id 不能为空")
	private Integer parentId;
	
	private String url;
	
	private String perms;
	
	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
