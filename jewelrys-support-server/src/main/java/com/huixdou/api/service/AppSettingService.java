package com.huixdou.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.huixdou.api.bean.AppSetting;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.AppSettingDao;
import com.huixdou.api.vo.AppSettingVO;
import com.huixdou.api.vo.AppTree;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Query;
import com.huixdou.common.base.Result;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jinxin on 2018/12/27 10:13.
 */
@Service
public class AppSettingService extends BaseService<AppSettingDao, AppSetting> {

	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private OssService ossService;

	
	 /**
     * 获取app设置列表
     *
	 * @param name
	 *            查询参数（名称）
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            每页显示数据 如果不传，默认为5
     * @return
     */
	public Map<String, Object> selectAppSettingPage(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		Page<AppSetting> page = (Page<AppSetting>) selectList(query);
		List<AppSetting> appSettings = page.getResult();
		List<AppSettingVO> appSettingVOs = new ArrayList<AppSettingVO>();
		for( AppSetting appSetting : appSettings) {
			AppSettingVO appAccountVO = new AppSettingVO();
			SysDict dict = sysDictService.selectByTypeAndCode("app-setting-status", appSetting.getShowFlag() + "");
			if (ObjectUtil.isNotNull(dict)) {
				appSetting.setShowFlagName(dict.getName());
			}	
			BeanUtil.copyProperties(appSetting,appAccountVO);
			appSettingVOs.add(appAccountVO);
		}
		Map<String, Object> result = Maps.newHashMap();
		result.put("total", page.getTotal());
		result.put("list", appSettingVOs);
		return result;
	}
	
	
	/**
	 * 批量发布-批量测回
	 * 
	 * @param params
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result updateReleases(Map<String, Object> params) {
		if (params.get("Ids") == null && params.get("Ids") == "") {
			return Result.failure("请选择要操作的内容");
		}
		ArrayList<Integer> list = (ArrayList<Integer>) Convert.toList(Integer.class, params.get("Ids"));
		if (list.size()==0) {
			return Result.failure("请选择要操作的内容");
		}
		Integer showFlag = Integer.parseInt(params.get("type").toString()) == 1 ? 0 : 1;
		params.put("showFlag", String.valueOf(showFlag));
		Integer count= dao.selectReleases(params);
		if (count != list.size()) {
			return Result.failure("传入参数异常请重新选择");
		}
		dao.updateReleases(params);
		return Result.success();
	}

	/**
	 * 批量删除
	 * 
	 * @param params
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result deleteByIds(Map<String, Object> params) {
		if (Integer.parseInt(params.get("deleteId").toString()) == -1) {
			return Result.failure("请先登陆再进行操作");
		}
		if (params.get("Ids") == null && params.get("Ids") == "") {
			return Result.failure("请选择要删除内容");
		}
		ArrayList<String> list = (ArrayList<String>) Convert.toList(String.class, params.get("Ids"));
		if (list.size()==0) {
			return Result.failure("请选择要删除内容");
		}
		Integer number = dao.selectNumber(params);
		if (list.size() > number) {
			return Result.failure("有些参数已删除或不存在");
		}
		Long deleteTime = new Date().getTime();
		params.put("deleteTime", deleteTime);
		dao.deleteByIds(params);
		return Result.success();
	}

	/**
	 * 获取app设置 设置项列表
	 * 
	 * @param typeCode
	 * @return
	 * 
	 */
	public Result getTree(String typeCode) {
		List<SysDict> dict = sysDictService.getTree(typeCode);
		if (dict.size() == 0) {
			return Result.failure("获取列表失败请联系管理员");
		}
		List<AppTree> data = new ArrayList<>();
		for (SysDict sysDict : dict) {
			AppTree appTree = new AppTree();
			appTree.setCode(sysDict.getCode());
			appTree.setName(sysDict.getName());
			appTree.setId(sysDict.getId());
			data.add(appTree);
		}
		return Result.success(data);
	}

	/**
	 * 获取app设置详细接口
	 * @param id
	 * @return
	 *
	 */
	public Result details(String id) {
		Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
		return Result.failure("请输入合法的id参数");
		}
	 	AppSetting appSetting = dao.selectById(pk);
	 	if (ObjectUtil.isNull(appSetting)) {
	 	return Result.failure("没有查到此详情");	
		}
	 	appSetting.setUrl(ossService.getAddress(appSetting.getFileId()));
		return Result.success(appSetting);	
	}

}
