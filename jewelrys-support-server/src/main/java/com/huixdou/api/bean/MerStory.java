package com.huixdou.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MerStory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String title;
	
	private String subTitle;
	
	private String status;
	
	private String statusName;
	
	private String createDate;
	
	private String reject;
	
	private Integer pv = 0;
	
	private Integer createId;
	
	private String contentId;
	
	private String content;
	
	private String logo;
	
	private Integer certNoCount;
	
	private String merName;
	
	private List<Map<String, String>> files = new ArrayList<Map<String, String>>();

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCertNoCount() {
		return certNoCount;
	}

	public void setCertNoCount(Integer certNoCount) {
		this.certNoCount = certNoCount;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public List<Map<String, String>> getFiles() {
		return files;
	}

	public void setFiles(List<Map<String, String>> files) {
		this.files = files;
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

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getReject() {
		return reject;
	}

	public void setReject(String reject) {
		this.reject = reject;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
}
