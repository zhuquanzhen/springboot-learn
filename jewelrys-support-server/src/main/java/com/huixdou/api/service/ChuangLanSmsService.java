/**
 * 
 */
package com.huixdou.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aliyuncs.exceptions.ClientException;
import com.huixdou.api.bean.SmsSendRequest;
import com.huixdou.api.bean.SysConfig;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;


/**
 * @author jinxin 2019年4月23日下午3:18:41
 * 创蓝短信发送
 */
@Service
public class ChuangLanSmsService {

	@Autowired
	private SysConfigService configService;
	
	/**
	 * 短信发送
	 * @param phone 接受手机号码
	 * @param code 验证码
	 * @throws ClientException 
	 *
	 */
	public String sendCode(String phone, String code) throws ClientException {
		// 短信模板
		SysConfig AppTemplateContent = configService.selectByKey("chuanglan_AppTemplateContent");
		// 短信账号
		SysConfig account = configService.selectByKey("chuanglan_account");
		// 短信密码
		SysConfig passWord = configService.selectByKey("chuanglan_password");
		// 发生的url
		SysConfig smsSingleRequestServerUrl = configService.selectByKey("chuanglan_smsSingleRequestServerUrl");
		
		if (ObjectUtil.isNull(AppTemplateContent) || ObjectUtil.isNull(account) || ObjectUtil.isNull(passWord) || ObjectUtil.isNull(smsSingleRequestServerUrl)) {
			throw new ClientException("请检查短信配置项");
		}
		String msg = StrUtil.format(AppTemplateContent.getcValue(),code);
		
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account.getcValue(), passWord.getcValue(), msg, phone);
		
		String requestJson = JSONUtil.toJsonStr(smsSingleRequest);
		
		return sendSmsByPost(smsSingleRequestServerUrl.getcValue(),requestJson);
	}
	
	/**
	 * 短信发送
	 * @param phone 接受手机号码
	 * @param msg 发送内容
	 * @throws ClientException 
	 *
	 */
	public String sendMag(String phone, String msg) throws ClientException {
		// 短信账号
		String account = configService.selectByKey("chuanglan_account").getcValue();
		// 短信密码
		String passWord = configService.selectByKey("chuanglan_password").getcValue();
		//发生的url
		String smsSingleRequestServerUrl = configService.selectByKey("chuanglan_smsSingleRequestServerUrl").getcValue();
		
		if (ObjectUtil.isNull(account) || ObjectUtil.isNull(passWord) || ObjectUtil.isNull(smsSingleRequestServerUrl)) {
			throw new ClientException("请检查短信配置项");
		}
		
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, passWord, msg, phone);
		
		String requestJson = JSONUtil.toJsonStr(smsSingleRequest);
		
		return sendSmsByPost(smsSingleRequestServerUrl,requestJson);
	}
	
	
	
	/**
	 * 获取发送短信内容
	 * @param code
	 * @return
	 * @throws ClientException 
	 *
	 */
	public String getMsg(String code) throws ClientException{
		// 短信模板
		String templateContent = configService.selectByKey("chuanglan_AppTemplateContent").getcValue();
		if (ObjectUtil.isNull(templateContent)) {
			throw new ClientException("请检查短信配置项");
		}
		String msg = StrUtil.format(templateContent,code);
		
		return msg;
	}
	

	
	/**
	 * 短信发送工具
	 * @param path
	 * @param postContent
	 * @return
	 *
	 */
	private static String sendSmsByPost(String path, String postContent) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
			httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			
//			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
//			printWriter.write(postContent);
//			printWriter.flush();

			httpURLConnection.connect();
			OutputStream os=httpURLConnection.getOutputStream();
			os.write(postContent.getBytes("UTF-8"));
			os.flush();
			
			StringBuilder sb = new StringBuilder();
			int httpRspCode = httpURLConnection.getResponseCode();
			if (httpRspCode == HttpURLConnection.HTTP_OK) {
				// 开始获取数据
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
