package com.huixdou.api.bean;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 消息中心
 * Created by jinxin on 2018/12/27 9:55.
 */
public class AppMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String label;
    
	private String labelName;
    
    @NotBlank(message="标题不能为空")
    private String title;
   
    @NotBlank(message="内容不能为空")
    @Size(max = 200,message="内容不能超过200个字符")
    private String content;

    @Size(max = 60,message="链接地址不能超过60个字符")
    private String url;

    @NotBlank(message="请选择接收人")
    private String member;

    private String statusName;
    @JsonIgnore
    private Integer status;

    private String createDate;

    private String sendDate;
    
    private Integer groupFlag = 0;
    @JsonIgnore
    private String typeCode;
    @JsonIgnore
    private String typeName;

    
    
    
    public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getGroupFlag() {
        return groupFlag;
    }

    public void setGroupFlag(Integer groupFlag) {
        this.groupFlag = groupFlag;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "AppMessage{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", member=" + member +
                ", status=" + status +
                ", createDate='" + createDate + '\'' +
                ", groupFlag=" + groupFlag +
                ", typeCode='" + typeCode + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
