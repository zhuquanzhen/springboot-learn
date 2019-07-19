package com.huixdou.api.bean;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    @NotBlank(message = "模块名称不能为空")
    @Size(max = 60, message="模块名称最多输入60字符")
    private String name;
    
    @NotBlank(message = "配置键不能为空")
    @Size(max = 60, message="配置键最多输入60字符")
    private String cKey;
    
    @NotBlank(message = "配置值不能为空")
    @Size(max = 512, message="配置值最多输入512字符")
    private String cValue;
    
    @Size(max = 60, message="备注最多输入60字符")
    private String remarks;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getcKey() {
		return cKey;
	}

	public void setcKey(String cKey) {
		this.cKey = cKey;
	}

	public String getcValue() {
		return cValue;
	}

	public void setcValue(String cValue) {
		this.cValue = cValue;
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
