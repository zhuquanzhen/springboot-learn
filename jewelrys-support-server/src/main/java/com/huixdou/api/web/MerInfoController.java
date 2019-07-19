package com.huixdou.api.web;

import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Maps;
import com.huixdou.api.bean.MerInfo;
import com.huixdou.api.service.MerInfoService;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.utils.Constant;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

@RestController
@RequestMapping("/sup/mer-info")
public class MerInfoController extends BaseController {

	@Autowired
	private MerInfoService merInfoService;
	
	/**
	 * 珠宝商列表
	 * 
	 * @param params
	 * @return
	 */
	@GetMapping("list")
	public Object list(Map<String, Object> params) {
		return success(merInfoService.selectPageVO(params));
	}

	/**
	 * 珠宝商列表 树形结构
	 * 
	 * @return
	 */
	@GetMapping("list-tree")
	public Object listTree() {
		return success(merInfoService.getTreeVO(0));
	}

	/**
	 * 根据ID获取珠宝商详情
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("get")
	public Object get(String id) {
		Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
			return failure("请输入合法的id参数");
		}
		return success(merInfoService.get(pk));
	}
	
	/**
	 * 珠宝商信息新增
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping("create")
	public Object create(@Valid @RequestBody MerInfo entity) {
		return merInfoService.create(entity);
	}

	/**
	 * 珠宝商信息更新
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping("update")
	public Object update(@Valid @RequestBody MerInfo entity) {
		return merInfoService.updateMerInfo(entity);
	}

	/**
	 * 判断管理员账号是否存在
	 * 
	 * @param username
	 * @return
	 */
	@GetMapping("exist-username")
	public Object existUserName(String username) {
		MerInfo info = merInfoService.selectByUserName(username);
		Map<String, Object> map = Maps.newHashMap();
		map.put("flag", ObjectUtil.isNull(info) ? 0 : 1);
		return success(map);
	}

	/**
	 * 判断管理员手机号是否存在
	 * 
	 * @param username
	 * @return
	 */
	@GetMapping("exist-phone")
	public Object existPhone(String phone) {
		MerInfo info = merInfoService.selectByPhone(phone);
		Map<String, Object> map = Maps.newHashMap();
		map.put("flag", ObjectUtil.isNull(info) ? 0 : 1);
		return success(map);
	}

	/**
	 * 根据ID删除珠宝商
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("delete")
	public Object delete(String id) {
		Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
			return failure("请输入合法的id参数");
		}

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", pk);
		params.put("deleteId", getUserId());
		params.put("deleteTime", System.currentTimeMillis());
		merInfoService.deleteMerInfo(params);
		return success();
	}

	/**
	 * 珠宝商启用
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("enable")
	public Object enable(String id) {
		Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
			return failure("请输入合法的id参数");
		}
		merInfoService.updateState(pk, Constant.ENABLE);
		return success();
	}

	/**
	 * 珠宝商禁用
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("disable")
	public Object disable(String id) {
		Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
			return failure("请输入合法的id参数");
		}
		merInfoService.updateState(pk, Constant.DISABLE);
		return success();
	}

	/**
	 * 获得区域列表
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("area")
	public Object area(Integer id) {
		return merInfoService.area(id);
	}
	
	/**
	 * 获得珠宝商配置列表
	 * 
	 * @param val 查询参数
	 * @param pageNum   页数       
	 * @param pageSize  每页显示大小
	 * 
	 * @return
	 */
	@GetMapping("configure-list")
	public Object configureList(@RequestParam Map<String, Object> params){
		return success(merInfoService.selectConfigurePageVO(params));
	}
	
	/**
	 * 通过id查寻珠宝商配置详情
	 * @param id
	 * @return
	 *
	 */
	@GetMapping("configure-details")
    public Object configureDetails(String id){
    	Integer pk = Convert.toInt(id);
		if (ObjectUtil.isNull(pk)) {
			return failure("请输入合法的id参数");
		}
		return merInfoService.details(pk);
	}
	
	/**
	 * 获取指定id下的珠宝商所关联的送检关联厂商
	 * @param id 
	 * @param name 送检关联厂商
	 * @return
	 *
	 */
	@GetMapping("relation")
	public Object relation(@RequestParam Map<String, Object> params){
		return merInfoService.relation(params);
	}
	
	/**
	 * 获取未关联的送检厂商
	 * @param id 
	 * @param name 送检关联厂商
	 * @return
	 *
	 */
	@GetMapping("select-vendor")
	public Object selectVendorList(@RequestParam Map<String, Object> params){
		return merInfoService.selectVendorList(params);
	}
	
	
	/**
	 * 保存关联送检厂商
	 * @param params
	 * @return
	 *
	 */
	@PostMapping("save-relation")
	public Object saveRelation(@RequestBody Map<String, Object> params) {
		return merInfoService.saveRelation(params);
	}
	
	// TODO 获取珠宝商关联的送检厂商

	// TODO 珠宝商送检厂商更新

}
