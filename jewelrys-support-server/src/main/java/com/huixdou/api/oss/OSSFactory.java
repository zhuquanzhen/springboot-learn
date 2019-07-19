package com.huixdou.api.oss;

import com.huixdou.api.oss.config.CloudStorageConfig;
import com.huixdou.api.service.SysConfigService;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.SpringContextHolder;

public class OSSFactory {

	private static SysConfigService sysConfigService;
	
	static {
		OSSFactory.sysConfigService = SpringContextHolder.getBean(SysConfigService.class);
	}

	public static CloudStorageService build() {
		
		CloudStorageConfig config = sysConfigService.getConfigObject("oss.config", CloudStorageConfig.class);
		
		if (config != null) {
			if (config.getType() == Constant.CloudService.LOCAL.getValue()) {
				return new LocalCloudStorageService(config);
			}

			if (config.getType() == Constant.CloudService.QINIU.getValue()) {
				return new QiniuCloudStorageService(config);
			}
		}
		
		return null;
	}

}
