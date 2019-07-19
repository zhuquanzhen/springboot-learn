package com.huixdou.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.SysDict;
import com.huixdou.api.bean.SysDictType;
import com.huixdou.api.dao.SysDictDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class SysDictService extends BaseService<SysDictDao, SysDict> {
	
	@Autowired
	private SysDictTypeService dictTypeService;
	
	public List<SysDict> getTree(String typeCode) {
		List<SysDict> sysDict = dao.selectByTypeCode(typeCode);
		for (SysDict dict : sysDict) {
			SysDict item = selectByTypeAndCode("yes-or-no", dict.getStatus()+"");
			if (ObjectUtil.isNotNull(item)) {
				dict.setStatusName(item.getName());
			}
			dict.setChildTree(getChild(dict.getId()));
		}
		return sysDict;
	}

	public List<SysDict> getChild(Integer id) {
		List<SysDict> dictList = dao.selectByParentId(id);
		List<SysDict> childList = new ArrayList<SysDict>();
		if (dictList.size() != 0) {
			for (SysDict dict : dictList) {
				SysDict item = selectByTypeAndCode("yes-or-no", dict.getStatus()+"");
				if (ObjectUtil.isNotNull(item)) {
					dict.setStatusName(item.getName());
				}
				dict.setParentName(dao.selectParentName(dict.getParentId()));
				dict.setChildTree(getChild(dict.getId()));
				childList.add(dict);
			}
		}
		return childList;
	}
	
	public List<SysDict> selectByTypeCodeSimpleness(String typeCode) {
		return dao.selectByTypeCodeSimpleness(typeCode);
	}
	
	public SysDict select(Integer id) {
		SysDict dict = dao.selectById(id);
		SysDict dicts = selectByTypeAndCode("yes-or-no", dict.getStatus()+"");
		if (ObjectUtil.isNotNull(dicts)) {
			dict.setStatusName(dicts.getName());
		}
		dict.setParentName(dao.selectParentName(dict.getParentId()));
		dict.setChildTree(null);
		return dict;
	}
	
	@Transactional(readOnly = false)
	public Result create(SysDict dict) {
		if (ObjectUtil.isNotNull(dao.selectByTypeCodeAndCode(dict.getTypeCode(), dict.getCode()))) {
			return Result.failure("code 值重复");
		}
		SysDictType dictType = dictTypeService.getDictType(dict.getTypeCode());
		if ("ro".equals(dictType.getOperate()))
			return Result.failure("此数据项无法操作");
		dao.insert(dict);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result updateDict(SysDict dict) {
		if (StrUtil.isBlank(Convert.toStr(dict.getId())))
			return Result.failure("id 参数不能为空");
		SysDictType dictType = dictTypeService.getDictType(dict.getTypeCode());
		if ("ro".equals(dictType.getOperate()))
			return Result.failure("此数据项无法操作");
		SysDict entity = dao.selectByTypeCodeAndCode(dict.getTypeCode(), dict.getCode());
		if (ObjectUtil.isNotNull(entity)) {
			if (!dict.getId().equals(entity.getId())) {
				return Result.failure("code 值重复");
			}
		}
		dao.update(dict);
		return Result.success();
	}
		
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		if (StringUtils.isBlank(Convert.toStr(params.get("id"))) || Convert.convert(List.class, params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		Integer id = Convert.toInt(params.get("id"));
		List<Integer> idList = dao.findByParentId(id);
		idList.add(id);
		params.replace("id", idList);
		params.put("deleteTime", System.currentTimeMillis());
		dao.deleteByMap(params);
		return Result.success();
	}
	
	public SysDict selectByTypeAndCode(String typeCode, String code) {
		return dao.selectByTypeAndCode(typeCode, code);
	}
	
}
