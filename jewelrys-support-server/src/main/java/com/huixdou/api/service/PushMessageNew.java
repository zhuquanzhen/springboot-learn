/**
 * 
 */
package com.huixdou.api.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.huixdou.api.bean.AppMessageNew;
import com.huixdou.api.bean.Push;
import com.huixdou.common.base.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author jinxin 2019年1月11日上午9:38:34
 * 
 *         消息发送 
 *         1,判断是账号发送还是群发创建好发送实体类对象 
 *         2,整理消息（IOS,ANDROID）
 *         3,整理头文件
 *         4,发送信息判断返回结果
 */
@Service
public class PushMessageNew {

	@Value("${xinge.push-url}")
	private String url;

	@Value("${xinge.ios-appid}")
	private String IosAppId;

	@Value("${xinge.ios-secretkey}")
	private String IosSecretKey;

	@Value("${xinge.message-type}")
	private String messageType;

	@Value("${xinge.android-appid}")
	private String androidAppId;

	@Value("${xinge.android-secretkey}")
	private String androidSecretKey;

	@Value("${xinge.ios-environment}")
	private String iosEnvironment;

	@Autowired
	private AppAccountService accountService;

	
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 消息发送调用
	 * @param appMessageNew
	 * @return
	 *
	 */
	public Result pushMessage(AppMessageNew appMessageNew){
		Result iosResult = messageIos(appMessageNew);
		Result androidResult = messageAndroid(appMessageNew);
		JSONObject iosData = JSONUtil.parseObj(iosResult.getData());
		JSONObject androidData = JSONUtil.parseObj(androidResult.getData());
		logger.info("安卓消息推送返回结果："+androidData);
		logger.info("IOS消息推送返回结果："+iosData);
	    String ioStrCode= iosData.getStr("ret_code", "404");
	    String androidStrCode= androidData.getStr("ret_code", "404");
		//判断发送结果
		if (appMessageNew.getGroupFlag()==1) {
			//群发消息有一种消息发送失败都为失败
            if (ioStrCode.equals("404")||androidStrCode.equals("404")) {
                return Result.failure("消息发送平台异常请稍后再发");
            }
            if (iosResult.getCode() != 200 || androidResult.getCode() != 200) {
                return Result.failure("群发消息发送失败");
            }
            if (!ioStrCode.equals("0") || !androidStrCode.equals("0")){
                return Result.failure("群发消息发送失败");
            }

		}else if (appMessageNew.getGroupFlag()==0) {
			//账号单发消息，两种系统都做发送有一个成功就为成功
            if (ioStrCode.equals("404") && androidStrCode.equals("404")) {
                return Result.failure("单发消息发送平台异常请稍后再发");
            }
            if (iosResult.getCode() != 200 && androidResult.getCode() != 200) {
                Result result = (!ioStrCode.equals("0"))?iosResult:androidResult;
                return result;
            }
            if (!ioStrCode.equals("0") && !androidStrCode.equals("0")){
                return Result.failure("单发消息发送失败");
            }
		}
			return Result.success();
	}
	
	
	
	/**
	 * 4,发送信息
	 * 
	 * @param
	 * @return
	 *
	 */
	private String push(Object pushEntity,String Authorization) {
		Push push = (Push) pushEntity;
		push.setMessage_type(messageType);//消息类型 1）notify：通知 2）message：透传消息/静默消息
		// TODO:测试
		System.out.println(JSONUtil.parseObj(push));
		String result = HttpRequest.post(url).header("Authorization", Authorization)
				.header("Content-Type", "application/json").body(JSONUtil.parseObj(push)).execute().body();
		return result;
	}

	
/************************************************ios*************************************************/	
	/**
	 * 1,判断是账号发送还是群发创建好发送实体类对象 --IOS
	 * 
	 *  GroupFlag 群发标志(0：单发消息；1：群发系统)
	 * @return
	 *
	 */
	private Result messageIos(AppMessageNew appMessageNew) {
		String platForm = "";
		String audienceType = "";
		
		if (appMessageNew.getGroupFlag() ==1) {
			platForm = "ios";
			audienceType="all";
		}else {
			platForm = "ios";
			audienceType="account";
		}
		JSONArray accountList = new JSONArray();// 存放推送账号
		
		Push push = new Push();// 1,整理发送实体类

		push.setPlatform(platForm);// 客户端平台类型 1）android：安卓 2）ios：苹果 3）all：安卓&&苹果，仅支持全量推送和标签推送（预留参数，暂不可用）

		push.setEnvironment(iosEnvironment);// 用户指定推送环境，仅限iOS平台推送使用 1）product： 推送生产环境 2）dev： 推送开发环境

		push.setAudience_type(audienceType);// 推送目标 1）all：全量推送 2）tag：标签推送 3）token：单设备推送 4）token_list：设备列表推送 5）account：单账号推送 6）account_list：账号列表推送
		
		if(appMessageNew.getGroupFlag() == 0) {
			if (accountService.selectByPhone(appMessageNew.getMember()) != null) {
				push.setAccount_list(accountList.put(appMessageNew.getMember()));//若单账号推送 1）要求 audience_type=account 2）参数格式：[“account1”] 若账号列表推送3）参数格式：[“account1”,”account2”] 4）最多1000 个account
			} else {
				Result.failure("发送失败，请联系管理员");
			}
		}
		
		 push.setMessage(arrangeMessageIos(appMessageNew));

		 return Result.success(push(push,getAuthorizationIos()));
	}
	
	
	/**
	 * 2,ios消息整理
	 *
	 */
	private JSONObject arrangeMessageIos(AppMessageNew appMessageNew) {

		JSONObject alert = new JSONObject();// 包含标题和消息内容(必选)

		//alert.put("subtitle", appMessage.getTitle() + appMessage.getContent());

		JSONObject aps = new JSONObject();// ios下的aps 苹果推送服务(APNs)特有的消息体字段，其中比较重要的键值对如下 alert：包含标题和消息内容(必选) badge_type：App显示的角标数(可选) category：下拉消息时显示的操作标识(可选)
		aps.put("alert", alert);
		if (StrUtil.isNotBlank(appMessageNew.getAddOne()+"") && appMessageNew.getAddOne() == 1){
			aps.put("badge_type", -2);// App显示的角标数(可选) -2:角标数字自动加1
		}else {
			aps.put("badge_type", -1);// App显示的角标数(可选) -1:角标数字不变
		}
		aps.put("sound", "Tassel.wav"); // 希望系统播放声音时包括此键。 此键的值是应用程序主包中或Library/Sounds 应用程序数据容器文件夹中声音文件的名称。 如果找不到声音文件，或者您指定default了该值， 系统将播放默认警报声音。
		aps.put("category", "INVITE_CATEGORY");// 下拉消息时显示的操作标识(可选)

		JSONObject custom = new JSONObject();
		//如果链接为空添加我的消息路由地址
		if (StrUtil.isNotBlank(appMessageNew.getUrl())) {
			custom.put("url", appMessageNew.getUrl());
		}else {
			custom.put("url", "ngtc://myMessage?login=1");
		}

		//消息
		custom.put("id",appMessageNew.getId());
		//app端 如果未读数量  1 就加1 0就不加
		custom.put("addOne",appMessageNew.getAddOne());
		JSONObject ios = new JSONObject();// message下的ios
		ios.put("aps", aps);
		ios.put("custom", custom);
		ios.put("xg", "oops"); // 系统保留key，应避免使用

		JSONObject message = new JSONObject(); // 存放信息
		message.put("title", appMessageNew.getTitle());
		message.put("content", appMessageNew.getContent());
		message.put("ios", ios);

		return message;

	}

	
	/**
	 * 3,整理+拼接请求文件头 ios
	 * 
	 * @return AuthorizationIos Base64 加密过后的文件头
	 *
	 */
	private String getAuthorizationIos() {
			String authorizationIos = "Basic " + Base64.encode(IosAppId.trim() + ":" + IosSecretKey.trim());
			return authorizationIos;
	}
	
/**************************************	Android ****************************************************/
	
	/**
	 * 1,判断是账号发送还是群发创建好发送实体类对象 --Android
	 * 
	 *  GroupFlag 群发标志(0：单发消息；1：群发系统)
	 * @return
	 *
	 */
	private Result messageAndroid(AppMessageNew appMessageNew){
		String platForm = "";
		String audienceType = "";
		
		if (appMessageNew.getGroupFlag() ==1) {
			platForm = "android";
			audienceType="all";
		}else {
			platForm = "android";
			audienceType="account";
		}
		
		// 存放推送账号
		JSONArray accountList = new JSONArray();
		
		Push push = new Push();
			
		push.setPlatform(platForm);// 客户端平台类型 1）android：安卓 2）ios：苹果 3）all：安卓&&苹果，仅支持全量推送和标签推送（预留参数，暂不可用）
			
		push.setAudience_type(audienceType);// 推送目标 1）all：全量推送 2）tag：标签推送 3）token：单设备推送 4）token_list：设备列表推送 5）account：单账号推送 6）account_list：账号列表推送
		
		// 群发标志(0：单发消息；1：群发系统)
		if (appMessageNew.getGroupFlag()==0) {
			
			if (accountService.selectByPhone(appMessageNew.getMember()) != null) {
				push.setAccount_list(accountList.put(appMessageNew.getMember()));//若单账号推送 1）要求 audience_type=account 2）参数格式：[“account1”] 若账号列表推送3）参数格式：[“account1”,”account2”] 4）最多1000 个account
			} else {
				Result.failure("发送失败，请联系管理员");
			}
			
		}
			push.setMessage(arrangeMessageAndroid(appMessageNew));
			return Result.success(push(push,getAuthorizationAndroid()));
	}
	

	/**
	 * 2,整理需要发送的信息--ANDROID
	 * 
	 * @return
	 */
	private JSONObject arrangeMessageAndroid(AppMessageNew appMessageNew) {
		// custom_content 用户自定义的键值对
		JSONObject custom_content = new JSONObject();
		//如果链接为空添加我的消息路由地址
		if (StrUtil.isNotBlank(appMessageNew.getUrl())) {
			custom_content.put("url", appMessageNew.getUrl());
		}else {
			custom_content.put("url", "ngtc://myMessage?login=1");
		}

		//消息
		custom_content.put("id",appMessageNew.getId());
		//app端 如果未读数量  1 就加1 0就不加
		custom_content.put("addOne",appMessageNew.getAddOne());
		JSONObject android = new JSONObject();
		android.put("custom_content",custom_content);

		// 存放信息
		JSONObject message = new JSONObject();
		message.put("title", appMessageNew.getTitle());
		message.put("content", appMessageNew.getContent());
		message.put("android", android);
		return message;
	}
	
	
	/**
	 * 3,整理+拼接请求文件头 Android
	 * 
	 * @return AuthorizationIos Base64 加密过后的文件头
	 *
	 */
	private String getAuthorizationAndroid() {
		String authorizationAndroid = "Basic " + Base64.encode(androidAppId.trim() + ":" + androidSecretKey.trim());
		return authorizationAndroid;
	}

}
