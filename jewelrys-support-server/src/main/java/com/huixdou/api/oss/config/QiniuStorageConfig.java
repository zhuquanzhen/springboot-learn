package com.huixdou.api.oss.config;

import java.io.Serializable;

public class QiniuStorageConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	// 七牛绑定的域名
	private String domain;
	// 七牛路径前缀
	private String prefix;
	// 七牛ACCESS_KEY
	private String accessKey;
	// 七牛SECRET_KEY
	private String secretKey;
	// 七牛存储空间名
	private String bucketName;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

}
