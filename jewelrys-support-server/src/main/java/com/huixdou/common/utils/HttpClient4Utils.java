/*
 * @(#) HttpClientUtils.java 2016年2月3日
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.huixdou.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HttpClient工具类
 * 
 */
public class HttpClient4Utils {
	/**
	 * 实例化HttpClient
	 * 
	 * @param maxTotal
	 * @param maxPerRoute
	 * @param socketTimeout
	 * @param connectTimeout
	 * @param connectionRequestTimeout
	 * @return
	 */
	public static HttpClient createHttpClient(int maxTotal, int maxPerRoute, int socketTimeout, int connectTimeout,
			int connectionRequestTimeout) {
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(maxTotal);
		cm.setDefaultMaxPerRoute(maxPerRoute);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setDefaultRequestConfig(defaultRequestConfig).build();
		return httpClient;
	}

	/**
	 * 发送post请求
	 * 
	 * @param httpClient
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static String sendPost(HttpClient httpClient, String url, Map<String, String> params, Charset encoding) {
		String resp = "";
		HttpPost httpPost = new HttpPost(url);
		if (params != null && params.size() > 0) {
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, String> entry = itr.next();
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formParams, encoding);
			httpPost.setEntity(postEntity);
		}
		CloseableHttpResponse response = null;
		try {
			response = (CloseableHttpResponse) httpClient.execute(httpPost);
			resp = EntityUtils.toString(response.getEntity(), encoding);
		} catch (Exception e) {
			// log
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// log
					e.printStackTrace();
				}
			}
		}
		return resp;
	}

	/**
	 * 发送post请求
	 * 
	 * @param httpClient
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static String sendPost(HttpClient httpClient, String url, Map<String, String> params) {

		String resp = "";
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("token",params.get("token"));
		if (params != null && params.size() > 0) {
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, String> entry = itr.next();
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			ObjectMapper json = new ObjectMapper();
			String abcdd = "";
			try {
				abcdd = json.writeValueAsString(params);
				// PostMethod postMethod = new PostMethod(Configure.PAY_API);
				httpPost.setEntity(new StringEntity(abcdd));
				// httpPost.setEntity(postEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// UrlEncodedFormEntity postEntity = new
			// UrlEncodedFormEntity(formParams,encoding );

		}
		// CloseableHttpResponse response = null;
		try {
			// response = (CloseableHttpResponse)
			// String abc=httpClient.sendasync(httpPost);
			// httpPost.reset();

			httpClient.execute(httpPost);
			// resp = EntityUtils.toString(response.getEntity(), encoding);
		} catch (Exception e) {
			// log
			e.printStackTrace();
		} finally {
			// if (response != null) {
			// try {
			// response.close();
			// } catch (IOException e) {
			// // log
			// e.printStackTrace();
			// }
			// }
			
		}
		return resp;

		// // 构造HTTP请求
		// HttpClient httpclient = new HttpClient();
		//
		// PostMethod postMethod = new PostMethod(Configure.PAY_API);
		//
		// WxPayReqData wxdata = new
		// WxPayReqData(body,detail,outTradeNo,totalFee,spBillCreateIP);
		//
		// String requestStr="";
		// requestStr=Util.ConvertObj2Xml(wxdata);
		// // 发送请求
		// String strResponse = null;
		// try {
		// RequestEntity entity = new StringRequestEntity(
		// requestStr.toString(), "text/xml", "UTF-8");
		// postMethod.setRequestEntity(entity);
		// httpclient.executeMethod(postMethod);
		// strResponse = new String(postMethod.getResponseBody(), "utf-8");
		// Logger.getLogger(getClass()).debug(strResponse);
		// } catch (HttpException e) {
		// Logger.getLogger(getClass()).error("sendWxPayRequest", e);
		// } catch (IOException e) {
		// Logger.getLogger(getClass()).error("sendWxPayRequest", e);
		// } finally {
		// postMethod.releaseConnection();
		// }
	}

//	public static Object abc(String ip) {
//		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(50000).setSocketTimeout(50000)
//				.setConnectionRequestTimeout(1000).build();
//
//		// 配置io线程
//		IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
//				.setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(true).build();
//		// 设置连接池大小
//		ConnectingIOReactor ioReactor = null;
//		try {
//			ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
//		} catch (IOReactorException e) {
//			e.printStackTrace();
//		}
//		PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
//		connManager.setMaxTotal(100);
//		connManager.setDefaultMaxPerRoute(100);
//
//		final CloseableHttpAsyncClient client = HttpAsyncClients.custom().setConnectionManager(connManager)
//				.setDefaultRequestConfig(requestConfig).build();
//
//		// 构造请求
//		String url = ip;
//		HttpPost httpPost = new HttpPost(url);
//		httpPost.addHeader("Content-Type", "application/json");
//		StringEntity entity = null;
//		try {
//			String a = "{ \"index\": { \"_index\": \"test\", \"_type\": \"test\"} }\n"
//					+ "{\"name\": \"上海\",\"age\":33}\n";
//			entity = new StringEntity(a);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		httpPost.setEntity(entity);
//
//		// start
//		client.start();
//
//		// 异步请求
//		client.execute(httpPost, new Back());
//
//		while (true) {
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}

//	static class Back implements FutureCallback<HttpResponse> {
//
//		private long start = System.currentTimeMillis();
//
//		Back() {
//		}
//
//		public void completed(HttpResponse httpResponse) {
//			try {
//				System.out.println("cost is:" + (System.currentTimeMillis() - start) + ":"
//						+ EntityUtils.toString(httpResponse.getEntity()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		public void failed(Exception e) {
//			System.err.println(" cost is:" + (System.currentTimeMillis() - start) + ":" + e);
//		}
//
//		public void cancelled() {
//
//		}
//	}
//
//	public static String abc1111(String requestUrl, Map<String, String> params) {
//
//		try {
//
//			// org.apache.commons.httpclient.HttpClient httpclient = new
//			// org.apache.commons.httpclient.HttpClient();
//			//
//			// PostMethod postMethod = new PostMethod(requestUrl);
//			// ObjectMapper json = new ObjectMapper();
//			// String outputStr=json.writeValueAsString(params);
//			//
//			//
//			// RequestEntity entity = new StringRequestEntity(
//			// outputStr, "application/json", "UTF-8");
//			// postMethod.setRequestEntity(entity);
//			// httpclient.executeMethod(postMethod);
//			// ObjectMapper json = new ObjectMapper();
//			// Charset encoding = Charset.forName("UTF-8");
//			// String resp = "";
//			// HttpPost httpPost = new HttpPost(requestUrl);
//			// httpPost.addHeader("Content-Type", "application/json");
//			// String abcdd= json.writeValueAsString(params);
//			// // PostMethod postMethod = new PostMethod(Configure.PAY_API);
//			// httpPost.setEntity(new StringEntity(abcdd));
//			// CloseableHttpAsyncClient httpclient =
//			// HttpAsyncClients.createDefault();
//			//
//			// httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {
//			//
//			// @Override
//			// public void failed(Exception arg0) {
//			// // TODO Auto-generated method stub
//			//
//			// }
//			//
//			// @Override
//			// public void completed(HttpResponse arg0) {
//			// // TODO Auto-generated method stub
//			//
//			// }
//			//
//			// @Override
//			// public void cancelled() {
//			// // TODO Auto-generated method stub
//			//
//			// }
//			// });
//
//			URL url = new URL(requestUrl);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setUseCaches(false);
//			// 设置请求方式（GET/POST）
//			// conn.setRequestMethod("GET");
//			conn.setRequestProperty("content-type", "application/json");
//			// conn.
//			ObjectMapper json = new ObjectMapper();
//			String outputStr = json.writeValueAsString(params);
//			// 当outputStr不为null时向输出流写数据
//			if (null != outputStr) {
//				OutputStream outputStream = conn.getOutputStream();
//				// 注意编码格式
//				outputStream.write(outputStr.getBytes("UTF-8"));
//				outputStream.close();
//			}
//			// conn.connect();
//			// conn.
//			// 从输入流读取返回内容
//			// conn.sendasync(abc)
//
//			// conn.getInputStream();
//			// InputStream inputStream =
//			// InputStreamReader inputStreamReader = new
//			// InputStreamReader(inputStream, "utf-8");
//			// BufferedReader bufferedReader = new
//			// BufferedReader(inputStreamReader);
//			// String str = null;
//			// StringBuffer buffer = new StringBuffer();
//			// while ((str = bufferedReader.readLine()) != null) {
//			// buffer.append(str);
//			// }
//			// // 释放资源
//			// bufferedReader.close();
//			// inputStreamReader.close();
//			// inputStream.close();
//			// inputStream = null;
//			// conn.disconnect();
//			return "";
//		} catch (Exception ce) {
//			System.out.println("连接超时：{}" + ce);
//		}
//		return null;
//	}
}