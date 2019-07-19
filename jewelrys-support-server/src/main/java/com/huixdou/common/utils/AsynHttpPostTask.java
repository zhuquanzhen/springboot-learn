package com.huixdou.common.utils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;
import org.springframework.util.StringUtils;

import com.huixdou.api.bean.CheckParameter;

import cn.hutool.extra.servlet.ServletUtil;

public class AsynHttpPostTask {
	private ExecutorService executor = Executors.newCachedThreadPool();

	private HttpServletRequest request;

	private Long sleepTime = 5000L;

	private String url = "";

	public Long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void post(HttpClient httpClient, Map<String, String> params) {
		url = HttpContextUtils.getDomain() + CheckParameter.result;
		String token = "";
		if (!StringUtils.isEmpty(request)) {
			token = request.getHeader("token");
			if (StringUtils.isEmpty(token)) {
				token = request.getParameter("token");
			}
			if (StringUtils.isEmpty(token)) {
				Cookie cookie = ServletUtil.getCookie(request, "token");
				if (cookie != null) {
					token = cookie.getValue();
				}
			}
			params.put("token", token);
		}

		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(sleepTime);
					HttpClient4Utils.sendPost(httpClient, url, params);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

}
