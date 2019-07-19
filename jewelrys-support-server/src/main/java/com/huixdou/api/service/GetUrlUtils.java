package com.huixdou.api.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.huixdou.api.bean.AppStory;
import com.huixdou.api.bean.MerNews;
import com.huixdou.api.bean.MerStory;
import com.huixdou.api.bean.SupBaike;
import com.huixdou.api.bean.SupNews;
import com.huixdou.api.bean.SysConfig;
import com.huixdou.common.base.Result;

import cn.hutool.core.convert.ConverterRegistry;

/**
 * 这是一个方法 根据传入的类型和id去返回后半段的url
 * 
 * @author 范雪冰
 *
 */
@Service
public class GetUrlUtils {
	// 配置是写在数据库里的 configuration.url
	final String configuration = "configuration.url";
	@Autowired(required=false)
	private SysConfigService sysConfigService;
	@Autowired(required=false)
	private SupNewsService supNewsService;
	@Autowired(required=false)
	private SupBaikeService supBaikeService;
	@Autowired(required=false)
	private MerNewsService merNewsService;
	@Autowired(required=false)
	private AppStoryService appStoryService;
	@Autowired(required=false)
	private MerStoryService merStoryService;
	public static GetUrlUtils getUrlUtils;
	
	@PostConstruct
	public void init() {
		getUrlUtils = this;
	}
	/**
	 * 
	 * @param typeName
	 *            ptzx 平台资讯 zbzx 珠宝商资讯 bk 百科(珠宝知识库) hygs 会员故事 zbsgs 珠宝商故事
	 * @param id
	 * @return
	 */
	public  String getUrl(String typeName, String id) {

		String endUrl = "";
		if ("ptzx".equals(typeName)) {
			endUrl = getSupNewUrl(id);
		} else if ("zbzx".equals(typeName)) {
			endUrl = getMerNewUrl(id);
		} else if ("bk".equals(typeName)) {
			endUrl = getBaikeUrl(id);
		} else if ("hygs".equals(typeName)) {
			endUrl = getAppStroyUrl(id);
		} else if ("zbsgs".equals(typeName)) {
			endUrl = getMerStroyUrl(id);
		}

		return endUrl;

	}

	/**
	 * 获取头url
	 * 
	 * @return
	 */
	public String getHearUrl() {
		SysConfig sysConfig = getUrlUtils.sysConfigService.selectByKey(configuration);
		String headUrl = "http://47.100.28.58:8080";
		if (!StringUtils.isEmpty(sysConfig)) {
			headUrl = sysConfig.getcValue();
		}
		return headUrl;
	}

	// 平台资讯
	public String getSupNewUrl(String id) {
		String url = "";
		
		SupNews supNews = getUrlUtils.supNewsService.selectById(id);
		if (!StringUtils.isEmpty(supNews)) {
			url = "/page/details?id=" + supNews.getId() + "&code=ptzx";
		}
		return url;
	}

	// 百科资讯
	public String getBaikeUrl(String id) {
		String url = "";
		SupBaike supBaike = getUrlUtils.supBaikeService.selectById(id);
		if (!StringUtils.isEmpty(supBaike)) {
			url = "/page/details?id=" + supBaike.getId() + "&code=bk";
		}
		return url;
	}

	// 珠宝商资讯
	public String getMerNewUrl(String id) {
		String url = "";
		MerNews merNews = getUrlUtils.merNewsService.selectById(id);
		if (!StringUtils.isEmpty(merNews)) {
			url = "/page/details?id=" + merNews.getId() + "&code=zbzx";
		}
		return url;
	}

	// 会员故事
	public String getAppStroyUrl(String id) {
		String url = "";
		Result result = getUrlUtils.appStoryService.selectById(id);
		if (result.getCode() == 200) {
			ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
			AppStory appStory = converterRegistry.convert(AppStory.class, result.getData());
			if (!StringUtils.isEmpty(appStory)) {
				url = "/page/story?id=" + appStory.getId() + "&code=hygs";
			}
		}

		return url;
	}

	// 珠宝商故事
	public String getMerStroyUrl(String id) {
		String url = "";
		Result result = getUrlUtils.merStoryService.selectById(id);
		if (result.getCode() == 200) {
			ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
			MerStory merStory = converterRegistry.convert(MerStory.class, result.getData());
			if (!StringUtils.isEmpty(merStory)) {
				url = "/page/story?id=" + merStory.getId() + "&code=zbsgs ";
			}
		}

		return url;
	}

}
