package com.huixdou.api.bean;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    @NotBlank(message = "角色昵称不能为空")
    @Size(max = 20, message="角色昵称最多输入20个字符")
    private String name;
    
    @Max(value = 1000, message="排序值最大值为1000")
    private Integer orderNum;
    
    @Size(max = 60, message="备注最多输入60个字符")
    private String remarks;

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
