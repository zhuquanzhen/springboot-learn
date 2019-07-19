package com.huixdou.api.web;

import com.huixdou.api.service.SysSmsService;
import com.huixdou.common.annotation.RequiresPermissions;
import com.huixdou.common.base.BaseController;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sup/sys-sms")
public class SysSmsController extends BaseController {
	
	@Value("${sms.privateKey}")
	private String privateKey;
	
	@Autowired
	private SysSmsService smsService;
	
	/**
	 * 获取列表
	 * @param pageNum  	页码
	 * @param pageSize 	页面大小
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param typeCode 	短信类型
	 * @param phone  	手机号码
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(smsService.selectPage(params));
	}
	
	/**
	 * 删除短信
	 * @param id[] 主键ID数组
	 * @return
	 */
	@RequiresPermissions(value = "sys-message-delete")
	@PostMapping("delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		return smsService.delete(params);
	}
	
	
	/**
	 * 发送短信验证码
	 * @param 	phone		手机号码（需解密）
	 * @param	typeCode	短信类型（登陆/注册/找回密码/修改密码）
	 * @return
	 */
	@PostMapping("send")
	public Object send(@RequestBody Map<String, String> params) {
		String phone = params.get("phone");
		String typeCode = params.get("typeCode");
		
		if (StrUtil.isBlank(phone)) {
			return failure("请输入手机号码");
		}
		
		if (StrUtil.isBlank(typeCode)) {
			return failure("请输入短信类型");
		}
		
		try {
			RSA rsa = SecureUtil.rsa(privateKey, null);
			phone = new String(rsa.decryptFromBase64(phone, KeyType.PrivateKey), CharsetUtil.UTF_8);
			if (StrUtil.isBlank(phone) || phone.length() != 11) {
				return failure("请输入正确的手机号码");
			}
			params.put("phone", phone);
		} catch (Exception e) {
			return failure("手机号码解密失败");
		}
		
		return smsService.send(params);
	}
	
}
