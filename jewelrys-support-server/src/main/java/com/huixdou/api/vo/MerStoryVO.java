package com.huixdou.api.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class MerStoryVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private Integer createId;
	
	@NotBlank(message = "标题不能为空")
	@Length(max = 60)
	private String title;
	
	@NotBlank(message = "内容不能为空")
	@Length(max = 10000)
	private String content;
	
	private List<String> fileId = new ArrayList<String>();
	
	private List<String> no = new ArrayList<String>();

	public List<String> getNo() {
		return no;
	}

	public void setNo(List<String> no) {
		this.no = no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
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

	public List<String> getFileId() {
		return fileId;
	}

	public void setFileId(List<String> fileId) {
		this.fileId = fileId;
	}

}
