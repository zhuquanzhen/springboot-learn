package com.huixdou.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/** 百科 */
public class SupBaike implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键ID
	private String id;
	// 标题
	@NotBlank(message = "请填写标题")
	@Size(max = 60, message = "标题不能超过60个字符")
	private String title;
	// 英文
	private String en;
	// 简介
	@NotBlank(message = "请填写简介")
	@Size(max = 10000, message = "简介不能超过80个字符")
	private String summary;
	// 海报
	@NotBlank(message = "请填写海报id")
	private String fileId;

	// 浏览量
	private Integer pv;
	// 收藏量
	private Integer fav;
	// 类别ID
	private String typeId;
	// 推送次数
	private Integer publishAmount;

	// 内容
	@NotBlank(message = "请填写内容")
	@Size(max = 10000, message = "内容不能超过10000个字符")
	private String content;

	// URL
	private String url;

	// 是否收藏
	private Integer faved;

	// 发布时间

	private String publishDate;
	// 收藏的ID
	private String accountFavId;

	// 內容ID
	@JsonIgnore
	private String contentId;

	// 收藏的时间
	@JsonIgnore
	private String createDate;

	private String status;

	private String statusName;

	// 删除标准
	@JsonIgnore
	private Integer deleteFlag;
	// 删除时间
	@JsonIgnore
	private String deleteTime;

	// 删除人的ID
	@JsonIgnore
	private String deleteId;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	public Integer getFaved() {
		return faved;
	}

	public void setFaved(Integer faved) {
		this.faved = faved;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getAccountFavId() {
		return accountFavId;
	}

	public void setAccountFavId(String accountFavId) {
		this.accountFavId = accountFavId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getPublishAmount() {
		return publishAmount;
	}

	public void setPublishAmount(Integer publishAmount) {
		this.publishAmount = publishAmount;
	}

	public SupBaike() {
		super();
	}

}
