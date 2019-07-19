package com.huixdou.module.encrypt.advice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huixdou.api.service.SysConfigService;
import com.huixdou.module.encrypt.utils.AesEncryptUtils;
import com.huixdou.module.encrypt.utils.EncryptConst;
import com.huixdou.module.encrypt.utils.RSAUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @desc 请求数据解密
 */
@ControllerAdvice
public class DecodeRequestBodyAdvice implements RequestBodyAdvice {

    private static final Logger logger = LoggerFactory.getLogger(DecodeRequestBodyAdvice.class);
    

    @Value("${server.private.key}")
    private String SERVER_PRIVATE_KEY;


    @Value("${aes.private.key}")
    private String AES_PRIVATE_KEY;
    
	@Autowired
	private SysConfigService sysConfigService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
    	//加密开关
    	String encrypt_value = sysConfigService.getValue(EncryptConst.ENCRYPTED_KEY);
    	if(EncryptConst.ENCRYPTED_NO_NEED.equals(encrypt_value)||encrypt_value==null) {
        	//兼容明文模式
        	List<String>  encrypteds = inputMessage.getHeaders().get(EncryptConst.ENCRYPTED_HEAD);
        	if(encrypteds==null||encrypteds.size()<=0) {
        		return inputMessage;
        	}
    	}
    	

        try {
//            if (methodParameter.getMethod().isAnnotationPresent(AesSecurityParameter.class)) {
//                //获取注解配置的包含和去除字段
//                AesSecurityParameter serializedField = methodParameter.getMethodAnnotation(AesSecurityParameter.class);
//                //入参是否需要解密
//                if(serializedField.inDecode()){
//                    logger.info("注解AesSecurityParameter,对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
//                    return new AesHttpInputMessage(inputMessage);
//                }
//            }
//            if (methodParameter.getMethod().isAnnotationPresent(RsaSecurityParameter.class)) {
//                //获取注解配置的包含和去除字段
//                RsaSecurityParameter serializedField = methodParameter.getMethodAnnotation(RsaSecurityParameter.class);
//                //入参是否需要解密
//                if(serializedField.inDecode()){
//                    logger.info("注解RsaSecurityParameter,对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
//                    return new RsaHttpInputMessage(inputMessage);
//                }
//            }
        	
//            if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
//                //获取注解配置的包含和去除字段
//                SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
//                //入参是否需要解密
//                if(serializedField.inDecode()){
                    logger.info("注解SecurityParameter,对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
                    return new MyHttpInputMessage(inputMessage);
//                }
//            }
//                return inputMessage;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常："+e.getMessage());
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(easpString(IOUtils.toString(inputMessage.getBody(),"utf-8")));
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         *
         * @param requestData
         * @return
         */
        public String easpString(String requestData) {
            if(requestData != null && !requestData.equals("")){
                Map<String,String> requestMap = new Gson().fromJson(requestData,new TypeToken<Map<String,String>>() {
                }.getType());
                // 密文
                String data = requestMap.get(EncryptConst.ENCRYPTED_JSON_KEY);
                // 加密的ase秘钥
//                String encrypted = requestMap.get("encrypted");
                List<String>  encrypteds = headers.get(EncryptConst.ENCRYPTED_HEAD);
                String encrypted = null;
                if(encrypteds!=null&&encrypteds.size()>0) {
                	encrypted = encrypteds.get(0);
                }
                if(StringUtils.isEmpty(data) || StringUtils.isEmpty(encrypted)){
                    throw new RuntimeException("参数【requestData】缺失异常！");
                }else{
                    String content = null ;
                    String aseKey = null;
                    try {
                        aseKey = RSAUtils.decryptDataOnJava(encrypted,SERVER_PRIVATE_KEY);
//            			RSA rsa = SecureUtil.rsa(SERVER_PRIVATE_KEY, null);
//            			aseKey = new String(rsa.decryptFromBase64(encrypted, KeyType.PrivateKey), CharsetUtil.UTF_8);
                    }catch (Exception e){
                        throw  new RuntimeException("参数【aseKey】解析异常！");
                    }
                    try {
                    	logger.info("request 解密前数据: " + data);
                        content  = AesEncryptUtils.decrypt(data, aseKey);
                        logger.info("request 解密后数据: " + content);
//                    	AES aes = SecureUtil.aes(aseKey.getBytes());
//                    	// 加密为16进制表示
//                    	String encryptHex = aes.encryptHex(content);
//                    	content = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
                    	
                    }catch (Exception e){
                        throw  new RuntimeException("参数【content】解析异常！");
                    }
                    if (StringUtils.isEmpty(content) || StringUtils.isEmpty(aseKey)){
                        throw  new RuntimeException("参数【requestData】解析参数空指针异常!");
                    }
                    return content;
                }
            }
            throw new RuntimeException("参数【requestData】不合法异常！");
        }
    }
    class RsaHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public RsaHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(easpString(IOUtils.toString(inputMessage.getBody(),"utf-8")));
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         *
         * @param requestData
         * @return
         */
        public String easpString(String requestData) {
            if(requestData != null && !requestData.equals("")){
                Map<String,String> requestMap = new Gson().fromJson(requestData,new TypeToken<Map<String,String>>() {
                }.getType());
                // 密文
                String data = requestMap.get(EncryptConst.ENCRYPTED_JSON_KEY);
                if(StringUtils.isEmpty(data)){
                    throw new RuntimeException("参数【requestData】缺失异常！");
                }else{
                    String content = null ;
                    try {
                        content = RSAUtils.decryptDataOnJava(data,SERVER_PRIVATE_KEY);
                    }catch (Exception e){
                        throw  new RuntimeException("参数【aseKey】解析异常！");
                    }
                    try {
                    }catch (Exception e){
                        throw  new RuntimeException("参数【content】解析异常！");
                    }
                    if (StringUtils.isEmpty(content)){
                        throw  new RuntimeException("参数【requestData】解析参数空指针异常!");
                    }
                    return content;
                }
            }
            throw new RuntimeException("参数【requestData】不合法异常！");
        }
    }
    class AesHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;


        public AesHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(easpString(IOUtils.toString(inputMessage.getBody(),"utf-8")));
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         *
         * @param requestData
         * @return
         */
        public String easpString(String requestData) {
            if(requestData != null && !requestData.equals("")){
                Map<String,String> requestMap = new Gson().fromJson(requestData,new TypeToken<Map<String,String>>() {
                }.getType());
                // 密文
                String data = requestMap.get("requestData");
                if(StringUtils.isEmpty(data)){
                    throw new RuntimeException("参数【requestData】缺失异常！");
                }else{
                    String content = null ;
                    try {
                        content  = AesEncryptUtils.decrypt(data, AES_PRIVATE_KEY);
                    }catch (Exception e){
                        throw  new RuntimeException("参数【content】解析异常！");
                    }
                    if (StringUtils.isEmpty(content)){
                        throw  new RuntimeException("参数【requestData】解析参数空指针异常!");
                    }
                    return content;
                }
            }
            throw new RuntimeException("参数【requestData】不合法异常！");
        }
    }
}