package com.huixdou.api.oss;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.huixdou.api.oss.config.CloudStorageConfig;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import cn.hutool.core.util.StrUtil;

//七牛云存储
public class QiniuCloudStorageService extends CloudStorageService {

	private UploadManager uploadManager;
	private String token;

	public QiniuCloudStorageService(CloudStorageConfig config) {
		this.config = config;
		// 初始化
		init();
	}

	private void init() {
		uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
		// 生成上传凭证
		token = Auth.create(config.getQiniu().getAccessKey(), config.getQiniu().getSecretKey()).uploadToken(config.getQiniu().getBucketName());
	}

	@Override
	public String upload(byte[] data, String path) {
		try {
			Response res = uploadManager.put(data, path, token);
			if (!res.isOK()) {
				throw new RuntimeException("上传七牛出错：" + res.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException("上传文件失败，请核对七牛配置信息", e);
		}
		
		StringBuilder sb = new StringBuilder(config.getQiniu().getDomain());
		if (!StrUtil.endWith(config.getQiniu().getDomain(), "/")) {
			sb.append("/");
		}
		sb.append(path).append("|").append(path);

		return sb.toString();
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		try {
			byte[] data = IOUtils.toByteArray(inputStream);
			return this.upload(data, path);
		} catch (IOException e) {
			throw new RuntimeException("上传文件失败", e);
		}
	}

	@Override
	public String uploadSuffix(byte[] data, String suffix) {
		return upload(data, getPath(config.getQiniu().getPrefix(), suffix));
	}

	@Override
	public String uploadSuffix(InputStream inputStream, String suffix) {
		return upload(inputStream, getPath(config.getQiniu().getPrefix(), suffix));
	}
}
