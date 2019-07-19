package com.huixdou.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MerStoryFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String storyId;
	
	private String fileId;
	
	private List<?> fileIds = new ArrayList<String>();

	public List<?> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<?> fileIds) {
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
