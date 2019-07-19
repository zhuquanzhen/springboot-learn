package com.huixdou.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by jinxin
 */
public class MerNews implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;
    @NotBlank(message="标题不能为空")
	@Size(max= 60,message="不能超过60个字符")
    private String title;
    @NotBlank(message="标签不能为空")
    private String label;
    @NotBlank(message="海报不能为空")
    private String fileId;

    private String createDate;

    private Integer createId;
    @NotBlank(message="状态不能为空")
    private String status;

    private Integer pv;

    private Integer fav;

    private Integer auditId;

    private String auditDate;

    private String reject;

    private Boolean deleteFlag;

    private String deleteTime;

    private String deleteId;

    private String contentId;
    
    private String url;
    @NotBlank(message="内容不能为空")
    @Size(max= 10000,message="内容不能超过10000个字符")
    private String content;
    
    private String  labelCode;
    
    private String statusName;
    
  

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
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

    @JsonIgnore
    private Map<String, String> files = new HashMap<String, String>();

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getFav() {
        return fav;
    }

    public void setFav(Integer fav) {
        this.fav = fav;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate == null ? null : auditDate.trim();
    }

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject == null ? null : reject.trim();
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime == null ? null : deleteTime.trim();
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId == null ? null : deleteId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
    
    
}