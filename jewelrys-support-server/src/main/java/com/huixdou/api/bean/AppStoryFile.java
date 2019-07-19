package com.huixdou.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppStoryFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String storyId;
	
	private String fileId;
	
	private List<String> fileIds = new ArrayList<String>();

	public List<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<String> fileIds) {
		this.fileIds = fileIds;
	}

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

}
