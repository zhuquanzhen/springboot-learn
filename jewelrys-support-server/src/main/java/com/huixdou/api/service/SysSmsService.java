package com.huixdou.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.google.gson.Gson;
import com.huixdou.api.bean.SmsSendResponse;
import com.huixdou.api.bean.SysConfig;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.bean.SysIpBlacklist;
import com.huixdou.api.bean.SysSms;
import com.huixdou.api.dao.SysDictDao;
import com.huixdou.api.dao.SysSmsDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.IdGen;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class SysSmsService extends BaseService<SysSmsDao, SysSms> {

	@Autowired
	private SysDictService dictService;

	@Autowired
	private SysDictDao dictDao;

	@Autowired
	private SysConfigService configService;

	@Autowired
	private ChuangLanSmsService chuangLanSmsService;
	
	@Autowired
	private AliyunSmsService aliyunSmsService;
	
	@Autowired
	private SysIpBlacklistService sysIpBlacklistService;

	private String alidayu_TemplateContent = "alidayu_TemplateContent";

	private String chuanglan_TemplateContent = "chuanglan_AppTemplateContent";
	
	//短信发送切换（阿里：alidayu，创蓝：chuanglan）
	private String SmsSwitch;
	
	private Map<String, String> alidayuParamMap = new HashMap<>();

	
	
	public List<SysSms> selectList(Map<String, Object> params) {
		List<SysSms> sysSms = dao.selectList(params);
		for (SysSms sms : sysSms) {
			SysDict dict = dictService.selectByTypeAndCode("sys-sms-status", sms.getStatus() + "");
			if (ObjectUtil.isNotNull(dict)) {
				sms.setStatusName(dict.getName());
			}
			sms.setCreateDate(DateUtils.formatDate(Convert.toLong(sms.getCreateDate()), "yyyy-MM-dd HH:mm"));
		}
		return sysSms;
	}

	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id")))
				|| Convert.convert(List.class, params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		params.put("deleteTime", System.currentTimeMillis());
		super.deleteByMap(params);
		return Result.success();
	}

	
	/**
	 * 短信发送控制
	 * @param params
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result send(Map<String, String> params) {
		String ip = getIp();
		Result result=new Result();
		if(!ip.equals("")&&!ip.equals("127.0.0.1")){
		// 先判断有没有规定的次数 如果有就拉黑
		result = JudgementBlacklist(ip);
		if (result.getCode() != 200) {
			return result;
		}}
		params.put("ip", ip);
		SmsSwitch = configService.selectByKey("sms.change").getcValue();
		if (SmsSwitch.equals("chuanglan")) {
			return sendChuangLan(params);
		}else {
			return sendAlidayu(params);
		}	
	}
	
	
	/**
	 * 创蓝短信发送
	 * @param params
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result sendChuangLan(Map<String, String> params) {
		String typeCode = params.get("typeCode");
		String phone = params.get("phone");
		// 短信类型校验
		List<SysDict> dictList = dictDao.selectByTypeCode("message-type");
		List<String> dictCodes = Lists.newArrayList();
		for (int i = 0; i < dictList.size(); i++) {
			dictCodes.add(dictList.get(i).getCode());
		}
		if (!dictCodes.contains(typeCode)) {
			return Result.failure("请输入正确的 typeCode 参数");
		}
		// 判断是否超时
		Result resulttime = timeOut(phone, typeCode);
		if (resulttime.getCode() != 200) {
			return resulttime;
		}
		int length = 4;
		SysConfig config = configService.selectByKey("app.message.length");
		if (config != null) {
			length = Integer.parseInt(config.getcValue());
		}

		String code = RandomUtil.randomNumbers(length);

		SysConfig smsSwitch = configService.selectByKey("sms.switch");

		if (smsSwitch != null && StrUtil.equals("1", smsSwitch.getcValue())) {
			SysConfig smsCode = configService.selectByKey("sms.code");
			if (smsCode != null) {
				code = smsCode.getcValue();
			} else {
				code = "1024";
			}
		}

		Result result = Result.success();
	
		SysSms entity = new SysSms();
		entity.setId(IdGen.getObjectId());
		entity.setCreateDate(System.currentTimeMillis() + "");
		entity.setPhone(phone);
		entity.setTypeCode(typeCode);
		String ip = params.get("ip");
		entity.setSendIp(ip);
		String msg = null;
		SysConfig templateContent = configService.selectByKey(chuanglan_TemplateContent);
		if (alidayuParamMap.size() > 1) {
			entity.setCode(alidayuParamMap.get("pwd"));
			msg = StrUtil.format(templateContent.getcValue(), alidayuParamMap.get("platform"),
					alidayuParamMap.get("name"), alidayuParamMap.get("pwd"));
			entity.setContent(msg);
		} else {
			entity.setCode(code);
			msg = StrUtil.format(templateContent.getcValue(), code);
			entity.setContent(msg);

		}
        String responseLog;
		try {
			if (smsSwitch != null && StrUtil.equals("0", smsSwitch.getcValue())) {
				responseLog = chuangLanSmsService.sendMag(phone, msg);
				entity.setResponseLog(responseLog);
				SmsSendResponse smsSingleResponse =  new Gson().fromJson(responseLog, SmsSendResponse.class);
				//发送状态（0：失败；1：成功（数据字典：sys-sms-status)
				if(ObjectUtil.isNotNull(smsSingleResponse)){
				  if ((smsSingleResponse.getCode()).equals("0") && StrUtil.isBlank(smsSingleResponse.getErrorMsg())) {
					entity.setStatus(1);
				  }else {
				    entity.setStatus(0);
				  }
				}else {
					entity.setStatus(0);
				}
			}
			result.setData(entity);
		} catch (ClientException e) {
			entity.setStatus(0);
			result = Result.failure(e.getMessage());
		} finally {
			dao.insert(entity);
		}
		return result;	
	}
	
	
	
	
	/**
	 * 阿里短信发送
	 * @param params
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result sendAlidayu(Map<String, String> params) {
		String typeCode = params.get("typeCode");
		String phone = params.get("phone");
		// 短信类型校验
		List<SysDict> dictList = dictDao.selectByTypeCode("message-type");
		List<String> dictCodes = Lists.newArrayList();
		for (int i = 0; i < dictList.size(); i++) {
			dictCodes.add(dictList.get(i).getCode());
		}
		if (!dictCodes.contains(typeCode)) {
			return Result.failure("短信发送失败");
		}
		// 判断是否超时
//		Result resulttime = timeOut(phone, typeCode);
//		if (resulttime.getCode() != 200) {
//			return resulttime;
//		}
		int length = 4;
		SysConfig config = configService.selectByKey("app.message.length");
		if (config != null) {
			length = Integer.parseInt(config.getcValue());
		}

		String code = RandomUtil.randomNumbers(length);

		SysConfig smsSwitch = configService.selectByKey("sms.switch");

		if (smsSwitch != null && StrUtil.equals("1", smsSwitch.getcValue())) {
			SysConfig smsCode = configService.selectByKey("sms.code");
			if (smsCode != null) {
				code = smsCode.getcValue();
			} else {
				code = "1024";
			}
		}

		Result result = Result.success();

		SysSms entity = new SysSms();
		entity.setId(IdGen.getObjectId());
		entity.setCreateDate(System.currentTimeMillis() + "");
		entity.setPhone(phone);
		entity.setTypeCode(typeCode);
		String ip = params.get("ip");
		entity.setSendIp(ip);
		SysConfig templateContent = configService.selectByKey(alidayu_TemplateContent);
		if (alidayuParamMap.size() > 1) {
			entity.setCode(alidayuParamMap.get("pwd"));
			entity.setContent(StrUtil.format(templateContent.getcValue(), alidayuParamMap.get("platform"),
					alidayuParamMap.get("name"), alidayuParamMap.get("pwd")));
		} else {
			entity.setCode(code);
			entity.setContent(StrUtil.format(templateContent.getcValue(), code));

		}
		SendSmsResponse sendSmsResponse = null;
		try {
			if (smsSwitch != null && StrUtil.equals("0", smsSwitch.getcValue())) {
				alidayuParamMap.put("code", code);
				sendSmsResponse=aliyunSmsService.send(phone, alidayuParamMap);
				JSONObject responseLogJson = JSONUtil.parseObj(sendSmsResponse);
				entity.setResponseLog(responseLogJson.toString());
				if (!sendSmsResponse.getCode().equals("OK")) {
					if ("isv.BUSINESS_LIMIT_CONTROL".equals(sendSmsResponse.getCode())) {
						String message = "";
						if (sendSmsResponse.getMessage().indexOf("天") > 0) {
							message = "当天发送次数已到限制 请明天再发送";
						} else {
							message = "发送频繁，请稍后重试";
						}
						throw new ClientException(message);
					} else {
						throw new ClientException(sendSmsResponse.getMessage());
					}

				}
			}
			entity.setStatus(1);
			result.setData(entity);
		} catch (ClientException e) {
			entity.setStatus(0);
			result = Result.failure(e.getMessage());
		} finally {
			dao.insert(entity);
		}
		return result;
	}

	
	public Result verify(String id, String code) {
		SysSms sms = selectById(id);
		if (ObjectUtil.isNotNull(sms)) {

			Long live = 5 * 60 * 1000L;
			SysConfig config = configService.selectByKey("sms.time");
			if (config != null) {
				live = Convert.toLong(config.getcValue(), live);
			}

			if (System.currentTimeMillis() - Convert.toLong(sms.getCreateDate()) > live) {
				return Result.failure("当前验证码已过期");
			}

			SysSms sysSms = dao.selectNewestByPhoneAndTypeCode(sms.getPhone(), sms.getTypeCode());
			if (!StrUtil.equalsIgnoreCase(id, sysSms.getId())) {
				return Result.failure("请输入最新验证码");
			}

			if (!StrUtil.equalsIgnoreCase(code, sms.getCode())) {
				return Result.failure("验证码错误");
			}

			return Result.success(sms);

		}

		return Result.failure("当前验证码不存在");
	}

	/**
	 * 珠宝商初始短信的接口
	 * 
	 * @param phone
	 *            电话
	 * @param name
	 *            用户名称
	 * @param getCode
	 *            初始密码
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result passWordGeneration(String phone, String name, String getCode) {
			String typeCode = "chushimima";
			Result result = timeOut(phone, typeCode);
			if (result.getCode() != 200) {
				return result;
			}
			this.alidayu_TemplateContent = "alidayu_TemplateContentGeneration";
			aliyunSmsService.alidayu_TemplateCode = "alidayu_TemplateCodeGeneration";
			this.chuanglan_TemplateContent = "chuanglan_supportTemplateContent";
			Map<String, String> map = new HashMap<>();
			String platform = "珠宝商管理平台";
			SysConfig sysConfig = configService.selectByKey("ngtc.name");
			if (!org.springframework.util.StringUtils.isEmpty(sysConfig)) {
				platform = sysConfig.getcValue();
			}
			map.put("phone", phone);
			map.put("typeCode", typeCode);
			map.put("platform", platform);
			map.put("name", name);
			map.put("pwd", getCode);
			this.alidayuParamMap = map;
			return send(map);
	
	}
	
	
	
//	@Transactional(readOnly = false)
//	public Result passWordGeneration(String phone, String name, String getCode) {
//		String typeCode = "chushimima";
//		Result result = timeOut(phone, typeCode);
//		if (result.getCode() != 200) {
//			return result;
//		}
//		this.alidayu_TemplateContent = "alidayu_TemplateContentGeneration";
//		aliyunSmsService.alidayu_TemplateCode = "alidayu_TemplateCodeGeneration";
//		Map<String, String> map = new HashMap<>();
//		String platform = "珠宝商管理平台";
//		SysConfig sysConfig = configService.selectByKey("ngtc.name");
//		if (!org.springframework.util.StringUtils.isEmpty(sysConfig)) {
//			platform = sysConfig.getcValue();
//		}
//
//		map.put("phone", phone);
//		map.put("typeCode", typeCode);
//		map.put("platform", platform);
//		map.put("name", name);
//		map.put("pwd", getCode);
//		this.alidayuParamMap = map;
//		return send(map);
//
//	}

	public Result timeOut(String phone, String typeCode) {
		if (org.springframework.util.StringUtils.isEmpty(phone)
				|| org.springframework.util.StringUtils.isEmpty(typeCode)) {

			return Result.error("参数不能为空");
		}
		SysSms sms = dao.selectNewestByPhoneAndTypeCode(phone, typeCode);
		if (!org.springframework.util.StringUtils.isEmpty(sms)) {
			Long nowTime = System.currentTimeMillis();
			Long sysTime = Convert.toLong(sms.getCreateDate());
			Long differTime = nowTime - sysTime;
			Integer time = 60000;
			if (differTime < time) {
				//现在变更为429 短信发送频繁的错误
				//return Result.failure(" 短信发送频繁 请稍后重试");
				Result result=new Result();
				result.setCode(429);
				result.setMsg("验证码发送频繁 ，请稍后再试");
				Map<String, Object> map= new HashMap<String, Object>();
				map.put("id", sms.getId());
				result.setData(map);
				return result;
			}

		}
		return Result.success();
	}
	
	/**
	 * 获取当前请求的ip
	 * 
	 * @return
	 */
	public String getIp() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();	
        String ip = request.getHeader("x-forwarded-for"); 
      //  System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {  
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
//            System.out.println("Proxy-Client-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
//            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
//            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
//            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Real-IP");  
//            System.out.println("X-Real-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
//            System.out.println("getRemoteAddr ip: " + ip);
        } 
//        System.out.println("获取客户端ip: " + ip);
        return ip;  
	}
	/**
	 * 黑名单校验
	 * 
	 * @param ip
	 */
	public Result JudgementBlacklist(String ip) {
		SysIpBlacklist sysIpBlacklist = sysIpBlacklistService.selectByIp(ip);
		// 这里做一下 校验 判断是否已经存在这个ip的地址
			// 就去查一下 是否已经限制了 ip的发送数量
		if (org.springframework.util.StringUtils.isEmpty(sysIpBlacklist)) {
			long time = 300000;
			Integer number = 20;
			SysConfig sysConfigTime = configService.selectByKey("requestIp.time");
			SysConfig sysConfigNumber = configService.selectByKey("requestIp.count");
			if (!org.springframework.util.StringUtils.isEmpty(sysConfigTime)) {
				time = Convert.toLong(sysConfigTime.getcValue());
			}
			if (!org.springframework.util.StringUtils.isEmpty(sysConfigNumber)) {
				number = Convert.toInt(sysConfigNumber.getcValue());
			}
			Integer count = selectNumberByIpAndTime(ip, time);
			// 当前数据库中的数量多配置的亮
			if (count >= number) {
				// 就插入一条黑名单的数据
				SysIpBlacklist sysIpBlacklistEntity = new SysIpBlacklist();
				sysIpBlacklistEntity.setIp(ip);
				sysIpBlacklistEntity.setCreateDate(System.currentTimeMillis() + "");
				sysIpBlacklistEntity.setType(1);
				
				sysIpBlacklistService.insert(sysIpBlacklistEntity);
				return Result.failure("短消息发送频繁，已被纳入黑名单");
			}else{
				return Result.success();
			}
		}else{
			return Result.failure("短消息发送频繁，已被纳入黑名单");
		}
	}
	@Transactional(readOnly = false)
	public Integer selectNumberByIpAndTime(String ip, long time) {
		return dao.selectNumberByIpAndTime(ip, time);
	}

}
