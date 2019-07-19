package com.huixdou.api.service;

import com.huixdou.api.bean.AppVersion;
import com.huixdou.api.dao.AppVersionDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jinxin on 2018/12/27 18:22.
 */
@Service
public class AppVersionService extends BaseService<AppVersionDao, AppVersion> {

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
		params.put("deleteTime", new Date().getTime());
		dao.deleteByIds(params);
		return Result.success();
	}

	/**
	 * 添加版本管理
	 * 
	 * @param appVersion
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result insertVersion(AppVersion appVersion) {
		appVersion.setVersion(appVersion.getVersion().trim());
		appVersion.setCreateDate(String.valueOf(new Date().getTime()));
		if (dao.selectByVersion(appVersion.getVersion()) != null) {
			return Result.failure("此版本号已经存在");
		}
		dao.insert(appVersion);
		return Result.success();

	}

	/**
	 * 更新版本管理
	 * 
	 * @param appVersion
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result updateVersion(AppVersion appVersion) {
		if (ObjectUtil.isNull(dao.selectById(appVersion.getId()))) {
			return Result.failure("输入的id不存在");
		}
		AppVersion  oldAppVersion = dao.selectByVersion(appVersion.getVersion());
		if (ObjectUtil.isNotNull(oldAppVersion)) {
			//版本号更新 用过版本号去数据库查询如果不为空并且查询到的版本id和更新的不一样说明数据库中已经存在了
			if (oldAppVersion.getId() != appVersion.getId()) {
				return Result.failure("此版本号已经存在");
			}
		}
		appVersion.setVersion(appVersion.getVersion().trim());
		appVersion.setCreateDate(String.valueOf(new Date().getTime()));
		dao.update(appVersion);
		return Result.success();
	}

	/**
	 * 获取版本管理列表
	 * 
	 * @param appVersion
	 * @return
	 */
	public List<AppVersion> selectList(Map<String, Object> params) {
		List<AppVersion> appVersions = dao.selectList(params);
		for (AppVersion appVersion : appVersions) {
			if (appVersion.getCreateDate() != null && appVersion.getCreateDate() != "") {
				appVersion.setCreateDate(
						DateUtils.formatDate(Convert.toLong(appVersion.getCreateDate()), "yyyy-MM-dd HH:mm"));
			} else {
				appVersion.setCreateDate("");
			}
		}
		return appVersions;
	}

	/**
	 * 获取详情
	 * @param id
	 * @return
	 *
	 */
	public Result details(String id){
		Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
		return Result.failure("请输入合法的id参数");
		}
		AppVersion appVersion = dao.selectById(id);
		if (ObjectUtil.isNull(appVersion)) {
		 return Result.failure("当前版本号不存在");
		}
		if (appVersion.getCreateDate() != null && appVersion.getCreateDate() != "") {
			appVersion.setCreateDate(
					DateUtils.formatDate(Convert.toLong(appVersion.getCreateDate()), "yyyy-MM-dd HH:mm"));
		} else {
			appVersion.setCreateDate("");
		}
		return Result.success(appVersion);
	}
}
