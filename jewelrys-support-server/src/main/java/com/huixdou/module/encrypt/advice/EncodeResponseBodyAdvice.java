package com.huixdou.module.encrypt.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huixdou.api.service.SysConfigService;
import com.huixdou.module.encrypt.annotation.AesSecurityParameter;
import com.huixdou.module.encrypt.annotation.RsaSecurityParameter;
import com.huixdou.module.encrypt.annotation.SecurityParameter;
import com.huixdou.module.encrypt.utils.AesEncryptUtils;
import com.huixdou.module.encrypt.utils.EncryptConst;
import com.huixdou.module.encrypt.utils.RSAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @desc 返回数据加密
 */
@ControllerAdvice
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice {

    private final static Logger logger = LoggerFactory.getLogger(EncodeResponseBodyAdvice.class);

    @Value("${client.public.key}")
    private String CLIENT_PUBLIC_KEY;

    @Value("${aes.private.key}")
    private String AES_PRIVATE_KEY;
    
   @Value("${server.private.key}")
   private String SERVER_PRIVATE_KEY;
   
   //需要加密
   private static final String ENCRYPTED_NEED = "0";
   //兼容加密明文
   private static final String ENCRYPTED_NO_NEED = "1";
   
	@Autowired
	private SysConfigService sysConfigService;


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
    	//加密开关
    	String encrypt_value = sysConfigService.getValue(EncryptConst.ENCRYPTED_KEY);
    	List<String>  encrypteds = serverHttpRequest.getHeaders().get(EncryptConst.ENCRYPTED_HEAD);
    	if(ENCRYPTED_NO_NEED.equals(encrypt_value)||encrypt_value==null) {
        	//兼容明文模式
        	if(encrypteds==null||encrypteds.size()<=0) {
        		return body;
        	}
    	}

    	
//        if (methodParameter.getMethod().isAnnotationPresent(AesSecurityParameter.class)) {
//            //获取注解配置的包含和去除字段
//            AesSecurityParameter serializedField = methodParameter.getMethodAnnotation(AesSecurityParameter.class);
//            //出参是否需要加密
//            if (serializedField.outEncode()){
//                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行加密");
//                return encodeAes(methodParameter,body);
//            }
//        }
//        if (methodParameter.getMethod().isAnnotationPresent(RsaSecurityParameter.class)) {
//            //获取注解配置的包含和去除字段
//            RsaSecurityParameter serializedField = methodParameter.getMethodAnnotation(RsaSecurityParameter.class);
//            //出参是否需要加密
//            if (serializedField.outEncode()){
//                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行加密");
//                return encodeRsa(methodParameter,body);
//            }
//        }
        
//        if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
            //获取注解配置的包含和去除字段
//            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //出参是否需要加密
//            if (serializedField.outEncode()){
                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行加密");
                if(encrypteds==null||encrypteds.size()<=0) {
                   //返回加密错误码
                   Map<String, String> map = new HashMap<>();
                   map.put("code", "555");
                   map.put("msg", "加密公钥错误");
                   return map;
                }
                String encrypted = encrypteds.get(0);
                String aseKey = null;
                try {
                    aseKey = RSAUtils.decryptDataOnJava(encrypted,SERVER_PRIVATE_KEY);
                }catch (Exception e){
//                    throw  new RuntimeException("参数【aseKey】解析异常！");
                    //返回加密错误码
                    Map<String, String> map = new HashMap<>();
                   map.put("code", "555");
                   map.put("msg", "加密公钥错误");
                   return map;
                }
                return encodeAesRsa(methodParameter,body,aseKey);
//            }
//        }
//        return body;
    }


    /**
     * AES加密
     * @param methodParameter
     * @param body
     * @return
     */
    private Object encodeAes(MethodParameter methodParameter,Object body){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
            // aes加密
            String requestData = AesEncryptUtils.encrypt(result, AES_PRIVATE_KEY);
            Map<String, String> map = new HashMap<>();
            map.put("requestData", requestData);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
        }
        return body;
    }

    /**
     * RSA加密
     * @param methodParameter
     * @param body
     * @return
     */
    private Object encodeRsa(MethodParameter methodParameter,Object body){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
            // rsa加密
            String requestData = RSAUtils.encryptedDataOnJava(result, CLIENT_PUBLIC_KEY);
            Map<String, String> map = new HashMap<>();
            map.put("requestData", requestData);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
        }
        return body;
    }

    /**
     * 混合加密
     * @return
     */
    private Object encodeAesRsa(MethodParameter methodParameter,Object body,String aseKey){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
//            // 生成aes秘钥
//            String aseKey = getRandomString(16);
//            // rsa加密
//            String encrypted = RSAUtils.encryptedDataOnJava(aseKey, CLIENT_PUBLIC_KEY);
            // aes加密
            logger.info("response 加密前数据: " + result);
            String requestData = AesEncryptUtils.encrypt(result, aseKey);
            logger.info("response 加密后数据: " + requestData);
            Map<String, String> map = new HashMap<>();
//            map.put("encrypted", encrypted);
            map.put("data", requestData);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
            //返回加密错误码
            Map<String, String> map = new HashMap<>();
            map.put("code", "555");
            map.put("msg", "加密公钥错误");
            return map;
        }
//        return body;
    }



    /**
     * 创建指定位数的随机字符串
     * @param length 表示生成字符串的长度
     * @return 字符串
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandomString(16));
    }
}