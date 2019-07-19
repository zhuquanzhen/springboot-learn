package com.huixdou.api.bean;

import java.io.Serializable;

public class CheckParameter implements Serializable {
	/** 易盾内容安全文本在线检测接口地址 */
	private static final long serialVersionUID = 1L;
	// 文本检查url
	public final static String API_Text_URL = "https://as.dun.163yun.com/v3/text/check";
	// 图片检查url
	public final static String API_Image_URL = "https://as.dun.163yun.com/v3/image/check";
	// 图片检查url
	public final static String API_Video_submit_URL = "https://as.dun.163yun.com/v3/video/submit";
	// 图片结果url
	public final static String API_Video_RESULT_URL = "https://as.dun.163yun.com/v3/video/callback/results";
	/** 产品密钥ID，产品标识 */
	private final static String SECRETID = "8e596dad15263db5d20b9e95cf2d6933";
	/** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
	private final static String SECRETKEY = "6fc6d386af00ec5de6202a0172e9b557";
	/** 业务ID，易盾根据产品业务特点分配 */
	private final static String TEXT_BUSINESSID = "df170f42e33dc6ad54817b820cc71dab";

	/** 业务ID，易盾根据产品业务特点分配 */
	private final static String IMAGE_BUSINESSID = "f59a55abe1a64438934359b7037483d6";

	/** 业务ID，易盾根据产品业务特点分配 */
	private final static String VIDEO_BUSINESSID = "7848de5d19fc933d4ed1708db3394d54";

	public final static String Text_Version = "v3.1";

	public final static String Image_Version = "v3.2";

	public final static String Video_Version = "v3";
	
	public final static String result="/sup/check/check-result";
	
	public final static Long FirstSleepTime=30000L;
	
	public final static Long TwoSleepTime=5000L;

	public static String getSecretid() {
		return SECRETID;
	}

	public static String getSecretkey() {
		return SECRETKEY;
	}

	public static String getTextBusinessid() {
		return TEXT_BUSINESSID;
	}

	public static String getImageBusinessid() {
		return IMAGE_BUSINESSID;
	}

	public static String getVideoBusinessid() {
		return VIDEO_BUSINESSID;
	}

}
