package com.huixdou.api.service;

import com.huixdou.api.bean.Oss;
import com.huixdou.api.dao.OssDao;
import com.huixdou.api.oss.config.CloudStorageConfig;
import com.huixdou.common.base.BaseService;
import com.google.common.collect.Maps;
import com.huixdou.common.utils.Constant;
import com.huixdou.common.utils.IdGen;
import com.huixdou.common.utils.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OssService extends BaseService<OssDao,Oss> {
	
	@Autowired
	private SysConfigService configService;
	
	@Transactional(readOnly = false)
	public boolean insert(Oss entity) {
		entity.setId(IdGen.getObjectId());
		if (getCloudStorageConfig().getType() == Constant.CloudService.LOCAL.getValue()) {
			entity.setUrl(entity.getUrl() + entity.getId());
		}
		return super.insert(entity);
	}

	@Transactional(readOnly = false)
	public boolean deleteById(Serializable id) {
		Oss oss = selectById(id);
		String path = oss.getPath();
		if (StringUtils.isNotBlank(path)) {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
		return super.deleteById(id);
	}
	
	public String getAddress(String id) {
		String url = "";
		Oss oss = this.selectById(id);
		if (oss != null) {
			CloudStorageConfig storageConfig = getCloudStorageConfig();
			if (storageConfig.getType() == Constant.CloudService.LOCAL.getValue()) {
				StringBuilder sb = new StringBuilder();
				String domain = storageConfig.getLocal().getDomain();
				sb.append(domain);
				if (!domain.endsWith("/")) {
					sb.append("/");
				}
				url = sb.append(id).toString();
			} else {
				url = oss.getUrl();
			}
		}
		return url;
	}
	
	public List<String> getAddress(List<String> idList) {
		List<String> urls = Lists.newArrayList();
		for (String id : idList) {
			if (StringUtils.isNotBlank(id)) {
				urls.add(getAddress(id));
			}
		}
		return urls;
	}
	
	public Map<String, String> getAddressMap(String id) {
		Map<String, String> result = Maps.newHashMap();
		String url = "";
		Oss oss = this.selectById(id);
		if (oss != null) {
			CloudStorageConfig storageConfig = getCloudStorageConfig();
			if (storageConfig.getType() == Constant.CloudService.LOCAL.getValue()) {
				StringBuilder sb = new StringBuilder();
				String domain = storageConfig.getLocal().getDomain();
				sb.append(domain);
				if (!domain.endsWith("/")) {
					sb.append("/");
				}
				url = sb.append(id).toString();
			} else {
				url = oss.getUrl();
			}
			result.put("url", url);
			result.put("type", getFileType(oss.getName()));
		}
		return result;
	}
	
	public List<Map<String, String>> getAddressMap(List<String> idList) {
		List<Map<String, String>> list = Lists.newArrayList();
		for (String id : idList) {
			if (StringUtils.isNotBlank(id)) {
				list.add(getAddressMap(id));
			}
		}
		return list;
	}
	
	private String getFileType(String name) {
		String type ="other";
		
		if (isImage(name)) {
			type = "image";
		} else if (isVedio(name)) {
			type = "vedio";
		} 
		
		return type;
	}

	public boolean isImage(String fileName) {
		Pattern p = Pattern.compile("^(jpeg|jpg|png|gif|bmp|webp)([\\w-./?%&=]*)?$");
		Matcher m = p.matcher(fileName.substring(fileName.lastIndexOf(".") + 1));
		if (m.find()) {
			return true;
		}
		return false;
	}
	
	public boolean isVedio(String fileName) {
		Pattern p = Pattern.compile("^(avi|mov|asf|wmv|navi|3gp|mkv|flv|f4v|rmvb|mp4)([\\w-./?%&=]*)?$");
		Matcher m = p.matcher(fileName.substring(fileName.lastIndexOf(".") + 1));
		if (m.find()) {
			return true;
		}
		return false;
	}
	
	private CloudStorageConfig getCloudStorageConfig() {
		return configService.getConfigObject("oss.config", CloudStorageConfig.class);
	}
	
}
