package com.huixdou.api.bean;

import java.io.Serializable;

//这是地址实体类
public class ComArea implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String areaName;
    private String areaCode;
    private String areaShort;
    private String areaIsHot;
    private String areaSequence;
    private String areaParentId;
    private String initDate;
    private String initAddr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaShort() {
        return areaShort;
    }

    public void setAreaShort(String areaShort) {
        this.areaShort = areaShort;
    }

    public String getAreaIsHot() {
        return areaIsHot;
    }

    public void setAreaIsHot(String areaIsHot) {
        this.areaIsHot = areaIsHot;
    }

    public String getAreaSequence() {
        return areaSequence;
    }

    public void setAreaSequence(String areaSequence) {
        this.areaSequence = areaSequence;
    }

    public String getAreaParentId() {
        return areaParentId;
    }

    public void setAreaParentId(String areaParentId) {
        this.areaParentId = areaParentId;
    }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getInitAddr() {
        return initAddr;
    }

    public void setInitAddr(String initAddr) {
        this.initAddr = initAddr;
    }

}
