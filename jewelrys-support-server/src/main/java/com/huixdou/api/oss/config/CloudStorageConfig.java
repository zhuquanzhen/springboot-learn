package com.huixdou.api.oss.config;

import java.io.Serializable;

//云存储配置信息
//@ConfigurationProperties(prefix = "oss")
//@Component
public class CloudStorageConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	// 类型  0:本地  1:七牛  2:阿里云  3:腾讯云
	private Integer type;

	private LocalStorageConfig local;

	private QiniuStorageConfig qiniu;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public LocalStorageConfig getLocal() {
		return local;
	}

	public void setLocal(LocalStorageConfig local) {
		this.local = local;
	}

	public QiniuStorageConfig getQiniu() {
		return qiniu;
	}

	public void setQiniu(QiniuStorageConfig qiniu) {
		this.qiniu = qiniu;
	}

}
