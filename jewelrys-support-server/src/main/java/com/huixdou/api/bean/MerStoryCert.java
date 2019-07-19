package com.huixdou.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MerStoryCert implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String storyId;
	
	private String certNo;
	
	private List<String> certNoList = new ArrayList<String>();

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public List<String> getCertNoList() {
		return certNoList;
	}

	public void setCertNoList(List<String> certNoList) {
		this.certNoList = certNoList;
	}

}
