package com.huixdou.api.bean;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SupNews implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	@NotBlank(message = "请填写内容")
	@Size(max = 60, message = "标题不能超过60个字符")
	private String title;

	private String label;
	@NotBlank(message = "请填写海报")
	private String fileId;

	private String createDate;

	private Integer createId;
	private String status;

	private String statusName;

	private Integer pv;

	private Integer fav;

	private Boolean showIndex;

	private Boolean deleteFlag;

	private String deleteTime;

	private String deleteId;

	private String contentId;
	@Size(max = 10000, message = "内容不能超过10000个字符")
	@NotBlank(message = "请填写内容")
	private String content;
	// 来源
	@Size(max = 60, message = "内容不能超过60个字符")
	private String source;

	private String url;

	private String labelCode;

	// 推送次数
	private Integer publishAmount;

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Boolean getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(Boolean showIndex) {
		this.showIndex = showIndex;
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

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId == null ? null : contentId.trim();
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getPublishAmount() {
		return publishAmount;
	}

	public void setPublishAmount(Integer publishAmount) {
		this.publishAmount = publishAmount;
	}
}