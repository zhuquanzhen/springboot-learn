package com.huixdou.common.utils;

public class Constant {
	
	public static final Integer SUPER_ADMIN = 1;
	
	public static final Integer ENABLE = 1;
	
	public static final Integer DISABLE = 0;
	
	public static final String PUBLISH = "yfb";
	
	public static final String REVOKE = "ych";
	
	public static final String FAILURE = "shsb";
	
	public static final String SUP_SYS_USER = "CONSTANT_SUP_SYS_USER";
	
	public static final String MERT_INFO = "CONSTANT_MERT_INFO";

	/**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$"; 

	
	
	// 云服务商
	public enum CloudService {
		// 本地
		LOCAL(0),
		// 七牛云
		QINIU(1),
		// 阿里云
		ALIYUN(2),
		// 腾讯云
		QCLOUD(3);

		private int value;

		private CloudService(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
