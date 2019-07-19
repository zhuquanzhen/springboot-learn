package com.huixdou.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpContextUtils {

	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static String getDomain() {
		HttpServletRequest request = getHttpServletRequest();
		StringBuffer url = request.getRequestURL();
		return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
	}

	public static String getOrigin() {
		HttpServletRequest request = getHttpServletRequest();
		return request.getHeader("Origin");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> GetPostForMap(HttpServletRequest req) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("HttpType", req.getMethod());
		if ("post".equalsIgnoreCase(req.getMethod())) {
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			} catch (Exception e) {
				/* report an error */ }
			Gson gson = new Gson();

			try {
				map = gson.fromJson(jb.toString(), map.getClass());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			map.put("id", req.getParameter("id"));
		}
		return map;

	}

}
