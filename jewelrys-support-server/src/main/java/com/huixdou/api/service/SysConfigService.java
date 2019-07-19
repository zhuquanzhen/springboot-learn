package com.huixdou.api.service;

import com.google.gson.Gson;
import com.huixdou.api.bean.SysConfig;
import com.huixdou.api.dao.SysConfigDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysConfigService extends BaseService<SysConfigDao,SysConfig> {
	
	@Transactional(readOnly = false)
	public Result create(SysConfig config) {
		SysConfig sysConfig = dao.selectByKey(config.getcKey());
		if (ObjectUtil.isNotNull(sysConfig)) {
			return Result.failure("cKey 参数重复");
		}
		dao.insert(config);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result updateConfig(SysConfig config) {
		if (StringUtils.isBlank(Convert.toStr(config.getId()))) {
			return Result.failure("id 参数不能为空");
		}
		SysConfig sysConfig = dao.selectByKey(config.getcKey());
		if (ObjectUtil.isNotNull(sysConfig)) {
			if (!config.getId().equals(sysConfig.getId())) {
				return Result.failure("cKey 参数重复");
			}
		}

		dao.update(config);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		params.put("deleteTime", System.currentTimeMillis());
		dao.deleteByMap(params);
		return Result.success();
	}
	
	// 首页统计App下载量
	public Integer getDownLoadNum() {
		return Convert.toInt(dao.selectDownLoadNum());
	}
	
	public SysConfig selectByKey(String key) {
		return dao.selectByKey(key);
	}
	
	public String getValue(String key) {
		SysConfig config = this.selectByKey(key);
		return config == null ? null : config.getcValue();
	}
	
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("获取参数失败");
		}
	}
	
}
