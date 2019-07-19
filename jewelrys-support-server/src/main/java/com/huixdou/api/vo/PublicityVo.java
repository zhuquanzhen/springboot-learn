package com.huixdou.api.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PublicityVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String publicity;
	private String publicityStatus;
	private String publicityPerm;
	private String publicityDate;
	private String publicityReject;
	private String publicityStatusName;
	private Map<String, String> urlMap = new HashMap<String, String>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublicity() {
		return publicity;
	}

	public void setPublicity(String publicity) {
		this.publicity = publicity;
	}

	public String getPublicityStatus() {
		return publicityStatus;
	}

	public void setPublicityStatus(String publicityStatus) {
		this.publicityStatus = publicityStatus;
	}

	public String getPublicityPerm() {
		return publicityPerm;
	}

	public void setPublicityPerm(String publicityPerm) {
		this.publicityPerm = publicityPerm;
	}

	public String getPublicityDate() {
		return publicityDate;
	}

	public void setPublicityDate(String publicityDate) {
		this.publicityDate = publicityDate;
	}

	public String getPublicityReject() {
		return publicityReject;
	}

	public void setPublicityReject(String publicityReject) {
		this.publicityReject = publicityReject;
	}

	public Map<String, String> getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(Map<String, String> urlMap) {
		this.urlMap = urlMap;
	}

	public String getPublicityStatusName() {
		return publicityStatusName;
	}

	public void setPublicityStatusName(String publicityStatusName) {
		this.publicityStatusName = publicityStatusName;
	}

}
