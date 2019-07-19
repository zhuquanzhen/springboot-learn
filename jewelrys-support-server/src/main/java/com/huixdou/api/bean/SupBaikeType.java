package com.huixdou.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 百科类别
 */
public class SupBaikeType implements Serializable {
    private static final long serialVersionUID = 1L;

    //    主键ID
    private String id;
    //    分类名称
	@NotBlank(message="请填写分类名称")
    @Size(max= 16,message="名字不能超过16个字符")
    private String name;
    //    分类英文
    private String enname;
    //    备注
	@Size(max= 60,message="备注不能超过60个字符")
    private String remarks;
    //删除标准
    @JsonIgnore
    private Integer deleteFlag;
    //删除时间
    @JsonIgnore
    private String deleteTime;

    //删除人的ID
    @JsonIgnore
    private String deleteId;

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

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public SupBaikeType() {
        super();
    }
}
