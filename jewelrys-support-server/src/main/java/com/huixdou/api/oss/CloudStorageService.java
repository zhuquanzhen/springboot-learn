package com.huixdou.api.oss;

import java.io.InputStream;

import com.huixdou.api.oss.config.CloudStorageConfig;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.IdGen;

import cn.hutool.core.util.StrUtil;

//云存储（本地存储、支持七牛、阿里云、腾讯云）
public abstract class CloudStorageService {
	
	// 云存储配置信息
    CloudStorageConfig config;

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
	public String getPath(String prefix, String suffix) {
		// 生成uuid
		String uuid = IdGen.uuid();
		
		// 文件路径
		StringBuilder path = new StringBuilder();
		
		if (StrUtil.isNotBlank(prefix)) {
			path.append(prefix);
			
			if (!StrUtil.endWith(prefix, "/")) {
				path.append("/");
			}
		}
		
		path.append(DateUtils.getYear()).append("/").append(DateUtils.getMonth() + 1).append("/").append(DateUtils.getDay()).append("/").append(uuid);
		
		return path.toString() + suffix;
	}

    /**
     * 文件上传
     * @param data    文件字节数组
     * @param path    文件路径，包含文件名
     * @return        返回http地址
     */
    public abstract String upload(byte[] data, String path);

    /**
     * 文件上传
     * @param data     文件字节数组
     * @param suffix   后缀
     * @return         返回http地址
     */
    public abstract String uploadSuffix(byte[] data, String suffix);

    /**
     * 文件上传
     * @param inputStream   字节流
     * @param path          文件路径，包含文件名
     * @return              返回http地址
     */
    public abstract String upload(InputStream inputStream, String path);

    /**
     * 文件上传
     * @param inputStream  字节流
     * @param suffix       后缀
     * @return             返回http地址
     */
    public abstract String uploadSuffix(InputStream inputStream, String suffix);

}
