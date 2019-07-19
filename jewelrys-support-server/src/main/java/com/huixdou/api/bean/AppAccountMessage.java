package com.huixdou.api.bean;

import java.io.Serializable;

/**
 * 中间表
 * @author jinxin
 * @date 2019/4/25 11:58
 */
public class AppAccountMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String appAccountPhone;

    private String appMessageNewId;

    private Integer status;

    private String sendDate;

    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppAccountPhone() {
        return appAccountPhone;
    }

    public void setAppAccountPhone(String appAccountPhone) {
        this.appAccountPhone = appAccountPhone;
    }

    public String getAppMessageNewId() {
        return appMessageNewId;
    }

    public void setAppMessageNewId(String appMessageNewId) {
        this.appMessageNewId = appMessageNewId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public AppAccountMessage(){}
    public AppAccountMessage(String id, String appAccountPhone, String appMessageNewId, Integer status,String createDate) {
        this.id = id;
        this.appAccountPhone = appAccountPhone;
        this.appMessageNewId = appMessageNewId;
        this.status = status;
        this.createDate = createDate;
    }
}
