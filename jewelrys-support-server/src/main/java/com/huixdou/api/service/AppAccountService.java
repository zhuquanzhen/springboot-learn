package com.huixdou.api.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huixdou.api.bean.AppAccount;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.AppAccountDao;
import com.huixdou.api.vo.AppAccountVO;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Query;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.StringUtils;

@Service
public class AppAccountService extends BaseService<AppAccountDao, AppAccount> {
	
	@Autowired
	private SysDictService dictService;
	
	@Autowired
	private AppStoryService appStoryService;
	
	@Autowired
	private CerInfoService cerInfoService;
	
	public List<AppAccount> selectList(Map<String, Object> params) {
		List<AppAccount> list = dao.selectList(params);

		for (AppAccount item : list) {
			SysDict dict = dictService.selectByTypeAndCode("app-account-status", item.getStatus() + "");
			if (ObjectUtil.isNotNull(dict)) {
				item.setStatusName(dict.getName());
			}
			if (StringUtils.isNotBlank(item.getRegisterDate())) {
				item.setRegisterDate(DateUtils.formatDate(Convert.toLong(item.getRegisterDate()), "yyyy-MM-dd HH:mm"));
			}
			if (StringUtils.isNotBlank(item.getLoginDate())) {
				item.setLoginDate(DateUtils.formatDate(Convert.toLong(item.getLoginDate()), "yyyy-MM-dd HH:mm"));
			}
			//发布故事的数量
			item.setStoryNum(appStoryService.selectStoryNum(item.getId()));
			//证书持有数量
			item.setNum(cerInfoService.selectNumByAccountId(item.getId()));	
		}
		return list;
	}

	/**
	 * 启用禁用
	 * @param ids	主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result updateStatus(Map<String, Object> params) {
		if (ObjectUtil.isNull(params.get("Ids"))) {
			return Result.failure("请输入id参数");
		}
		List<Integer> ids = Convert.toList(Integer.class, params.get("Ids"));
		if (ids.size() == 0) {
			return Result.failure("请输入id参数");
		}
		Integer status = Convert.toInt(params.get("type").toString()) == 1 ? 0 : 1;
		params.put("status", String.valueOf(status));
		params.put("Ids", ids);
		Integer count = dao.selectNumber(params);
		if (ids.size()!=count) {
			return Result.failure("传入参数异常请重新选择");
		}
		dao.updateStatus(params);
		return Result.success();
	}
	
	/**
	 * 通过手机（会员账号）去查询用户信息
	 * 
	 * @param phone
	 * @return
	 *
	 */
	public AppAccount selectByPhone(String phone) {
		return dao.selectByPhone(phone);
	}
	
	/**
	 * 获取详细信息
	 * @param id
	 * @return
	 *
	 */
	public Result selectById(String id) {
		Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
		return Result.failure("请输入合法的id参数");
		}
		AppAccountVO appAccountVO = new AppAccountVO();
		AppAccount appAccount = dao.selectById(pk);
		if (ObjectUtil.isNull(appAccount)) {
			return Result.failure("查询的用户不存在");
		}
		SysDict dict = dictService.selectByTypeAndCode("app-account-status", appAccount.getStatus() + "");
		if (ObjectUtil.isNotNull(dict)) {
			appAccount.setStatusName(dict.getName());
		}
		appAccount.setRegisterDate(DateUtils.formatDate(Convert.toLong(appAccount.getRegisterDate()), "yyyy-MM-dd HH:mm"));
		BeanUtil.copyProperties(appAccount,appAccountVO);
		return Result.success(appAccountVO);
	}
	
	/**
	 * 批量删除
	 * @param params
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result deleteByIds(Map<String, Object> params){
		if (ObjectUtil.isNull(params.get("Ids"))) {
			return Result.failure("请检查 ids 参数列表");
		}
		List<Integer> ids = Convert.toList(Integer.class, params.get("Ids"));
		if (ids.size() == 0) {
			return Result.failure("请检查 ids 参数列表");
		}
		Integer count = dao.selectNumber(params);
		if (ids.size()!=count) {
			return Result.failure("传入参数异常请重新选择");
		}
		params.put("Ids", ids);
		params.put("deleteTime", new Date().getTime());
		dao.deleteByIds(params);
		return Result.success();
	}

	
	/**
	 * 获取信息推送接收人   用户都为启用状态
	 * @param params
	 * @return
	 *
	 */
	public Map<String, Object> selectReceiver(Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		Page<AppAccount> page = (Page<AppAccount>) dao.selectReceiver(query);
		List<AppAccount> list = page.getResult();
		for (AppAccount item : list) {
			SysDict dict = dictService.selectByTypeAndCode("app-account-status", item.getStatus() + "");
			if (ObjectUtil.isNotNull(dict)) {
				item.setStatusName(dict.getName());
			}
			if (StringUtils.isNotBlank(item.getRegisterDate())) {
				item.setRegisterDate(DateUtils.formatDate(Convert.toLong(item.getRegisterDate()), "yyyy-MM-dd HH:mm"));
			}
			if (StringUtils.isNotBlank(item.getLoginDate())) {
				item.setLoginDate(DateUtils.formatDate(Convert.toLong(item.getLoginDate()), "yyyy-MM-dd HH:mm"));
			}
		}
		Map<String, Object> result = Maps.newHashMap();
		result.put("total", page.getTotal());
		result.put("list", list);
		return result;
	}
	
	// 首页统计会员数量
	public Integer getAccountCount() {
		return dao.selectCount();
	}
	
	// 首页获取店员信息
	public List<Integer> getAppAccount(List<String> phone) {
		List<Integer> result = Lists.newArrayList();
		for (String str : phone) {
			AppAccount item = selectByPhone(str);
			if (ObjectUtil.isNotNull(item))
				result.add(item.getId());
		}
		return result;
	}
}
