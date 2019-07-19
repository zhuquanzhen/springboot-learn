package com.huixdou.api.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.huixdou.api.bean.MerInfo;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.vo.PublicityVo;
import com.huixdou.common.base.Query;
import com.huixdou.common.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class PublicityService  {

	@Autowired
	private MerInfoService merInfoService;

	@Autowired
	private OssService ossService;
	
	@Autowired 
	private SysDictService sysDictService;
	
	public 	Map<String, Object> selectPage(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		List<PublicityVo> publicityVos = new ArrayList<PublicityVo>();
		List<MerInfo> list = merInfoService.selectPublicityList(query);
		@SuppressWarnings("resource")
		Page<MerInfo> page = (Page<MerInfo>) list;
		if (list.size() > 0) {
			for (int i = 0, length = list.size(); i < length; i++) {
				MerInfo item = list.get(i);
				PublicityVo publicityVo = new PublicityVo();
				BeanUtil.copyProperties(item, publicityVo);
				Map<String, String> map = ossService.getAddressMap(publicityVo.getPublicity());
				publicityVo.setUrlMap(map);
				if(!StringUtils.isEmpty(publicityVo.getPublicityStatus())){
					SysDict	sysDict=sysDictService.selectByTypeAndCode("mer-info-publicity-status", publicityVo.getPublicityStatus());
					if(!StringUtils.isEmpty(sysDict)){
						publicityVo.setPublicityStatusName(sysDict.getName());
					}
				}
				publicityVos.add(publicityVo);
				
			}

		}
		Map<String, Object> table = Maps.newHashMap();
		table.put("total", page.getTotal());
		table.put("list", publicityVos);

		return table;

	}
	
	// 通过
	@Transactional(readOnly = false)
	public Result update(Map<String, Object> params, String status) {
		if (StringUtils.isEmpty(params.get("id")) || Convert.toList(params.get("id")).size() == 0) {
			return Result.failure("id 参数不能为空");
		}
		params.put("publicityStatus", status);
		merInfoService.updatePublicityStatus(params);	
		return Result.success();
	}
	
	public List<SysDict> getStatus(String typeCode) {
		final String[] status = {"yfb", "dcl", "ych"};
		List<SysDict> list = sysDictService.selectByTypeCodeSimpleness(typeCode);
		Iterator<SysDict> it = list.iterator();
		while (it.hasNext()) {
			SysDict sysDict = it.next();
			if (!ArrayUtil.contains(status, sysDict.getCode())) {
				it.remove();
			}
		}
		return list;
	}
}
