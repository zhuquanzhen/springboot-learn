package com.huixdou.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SysDict implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotBlank(message = "数据项值不能为空")
	@Size(max = 20, message="数据项值最多输入20个字符")
	private String code;
	
	@NotBlank(message = "数据项名不能为空")
	@Size(max = 20, message="数据项名最多输入20个字符")
	private String name;
	
	@Max(value = 1000, message="排序值最大值为1000")
	private Integer orderNum;
	
	@Size(max = 60, message="备注最多输入60个字符")
	private String remarks;
	
	@NotNull(message = "父级id 不能为空")
	private Integer parentId;
	
	@NotBlank(message = "字典代码不能为空")
	@Size(max = 20, message="数据项名最多输入20个字符")
	private String typeCode;
	
	@NotNull(message = "状态不能为空")
	private Integer status;
	
	private String statusName;
	
	private String parentName;
	
	private List<SysDict> childTree = new ArrayList<SysDict>();

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<SysDict> getChildTree() {
		return childTree;
	}

	public void setChildTree(List<SysDict> childTree) {
		this.childTree = childTree;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

}
