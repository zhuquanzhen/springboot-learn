package com.huixdou.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.huixdou.api.bean.SysConfig;
import com.huixdou.common.utils.JsonMapper;

@Service
public class AliyunSmsService {

	@Autowired
	private SysConfigService configService;
	String alidayu_TemplateCode = "alidayu_TemplateCode";

	public SendSmsResponse send(String phone, Map<String, String> templateParam) throws ClientException {
		if (templateParam.size() < 1) {
			throw new ClientException("参数异常");
		}
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		String product = "Dysmsapi";
		String domain = "dysmsapi.aliyuncs.com";
		SysConfig appkey = configService.selectByKey("alidayu_appkey");
		SysConfig secretKey = configService.selectByKey("alidayu_secretKey");
		SysConfig signName = configService.selectByKey("alidayu_FreeSignName");
		SysConfig templateCode = configService.selectByKey(alidayu_TemplateCode);

		if (appkey == null || secretKey == null || signName == null || templateCode == null) {
			throw new ClientException("请检查短信配置项");
		}

		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appkey.getcValue(), secretKey.getcValue());
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		SendSmsRequest request = new SendSmsRequest();
		request.setMethod(MethodType.POST);
		request.setPhoneNumbers(phone);
		request.setSignName(signName.getcValue());
		request.setTemplateCode(templateCode.getcValue());

		request.setTemplateParam(JsonMapper.toJsonString(templateParam));
		// request.setSmsUpExtendCode("");
		// request.setOutId("");
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		return sendSmsResponse;
	}

}
