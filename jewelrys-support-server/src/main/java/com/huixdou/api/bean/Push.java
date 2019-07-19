/**
 * 
 */
package com.huixdou.api.bean;

import java.io.Serializable;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * @author jinxin 2019年1月11日上午10:29:55   
 * 发送信息IOS版 实体类
 */
public class Push implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 推送目标 
	 * 1）all：全量推送 
	 * 2）tag：标签推送 
	 * 3）token：单设备推送 
	 * 4）token_list：设备列表推送
	 * 5）account：单账号推送
	 * 6）account_list：账号列表推送
	 */

	private String audience_type;

	/**
	 * 用户指定推送环境，仅限iOS平台推送使用 
	 * 1）product： 推送生产环境 
	 * 2）dev： 推送开发环境
	 */
	private String environment;

	/**
	 * 若单账号推送 
	 * 1）要求 audience_type=account 
	 * 2）参数格式：[“account1”] 若账号列表推送
	 * 1）参数格式：[“account1”,”account2”] 
	 * 2）最多1000 个account
	 */
	private JSONArray account_list;
	/**
	 * 客户端平台类型 
	 * 1）android：安卓 
	 * 2）ios：苹果 
	 * 3）all：安卓&&苹果，仅支持全量推送和标签推送（预留参数，暂不可用）
	 */
	private String platform;

	/**
	 * 发送信息的实体类
	 */
	private JSONObject message;

	/**
	 * 消息类型 
	 * 1）notify：通知 
	 * 2）message：透传消息/静默消息
	 */
	private String message_type;

	public String getAudience_type() {
		return audience_type;
	}

	public void setAudience_type(String audience_type) {
		this.audience_type = audience_type;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public JSONArray getAccount_list() {
		return account_list;
	}

	public void setAccount_list(JSONArray account_list) {
		this.account_list = account_list;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public JSONObject getMessage() {
		return message;
	}

	public void setMessage(JSONObject message) {
		this.message = message;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	@Override
	public String toString() {
		return "PushIos [audience_type=" + audience_type + ", environment=" + environment + ", account_list="
				+ account_list + ", platform=" + platform + ", message=" + message + ", message_type=" + message_type
				+ "]";
	}

}
