package com.huixdou.api.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * app设置
 * Created by jinxin on 2018/12/27 9:55.
 */
public class AppSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotBlank(message = "请上传图片")
    private String fileId;

    private String url;
    
    @NotBlank(message = "请输入名称")
    @Size(max = 30,message="名称最多不能超过30个字符")
    private String name;
    
    @Size(max = 60,message="链接地址最多不能超过60个字符")
    private String link;

    private Integer showFlag;

    private String showFlagName;
    
    private Integer orderNum;

    private String dir;

    private String deleteId;

    private String remark;

    
    public String getShowFlagName() {
		return showFlagName;
	}

	public void setShowFlagName(String showFlagName) {
		this.showFlagName = showFlagName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Integer showFlag) {
        this.showFlag = showFlag;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "AppSetting{" +
                "id=" + id +
                ", fileId='" + fileId + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", showFlag=" + showFlag +
                ", orderNum=" + orderNum +
                ", dir='" + dir + '\'' +
                ", deleteId='" + deleteId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
