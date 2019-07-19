package com.huixdou.api.bean;

public enum CheckEnum {
	status_0(0,"pass"),
	status_1(1,"impeach"),
	status_2(2,"noPass"),
	status_3(3,"pushSuccess"),
	status_4(4,"pushDefeated"),
	status_200(200, "ok"),
	status_400(400,"请求缺少 secretId 或 businessId"),
	status_401(401, "businessId无效或过期"),
	status_402(402,"业务已下线"),
	status_404(404, "业务配置不存在"),
	status_405(405,"请求参数异常"),
	status_410(410, "签名验证失败，请重新参考demo签名代码"),
	status_411(411,"请求频率(即QPS, 文本200/s, 图片 64张/s, 视频 20/s)或数量(试用期间,文本3万/天,图片2万/天,视频200个/天, 音频200个/天,直播电视墙 38个/天)超过限制"),
	status_414(414, "请求长度超过限制"),
	status_415(415,"业务版本限制"),
	status_416(416	, "图片下载失败"),
	status_420(420,"请求过期"),
	status_430(430, "重放攻击"),
	status_503(503,"接口异常"),;
	private Integer key;
	private String value;

	private CheckEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}


	public String getValue() {
		return value;
	}

	public static String getMsg(Integer code) {
		for (CheckEnum opcode : CheckEnum.values()) {
			if (opcode.getKey().equals(code)) {
				return opcode.getValue();
			}
		}
		return null;
	}

}
