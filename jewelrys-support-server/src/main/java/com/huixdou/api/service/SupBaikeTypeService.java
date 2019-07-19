package com.huixdou.api.service;

import com.huixdou.common.base.Result;
import com.huixdou.common.utils.IdGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huixdou.api.bean.SupBaikeType;
import com.huixdou.api.dao.SupBaikeTypeDao;
import com.huixdou.common.base.BaseService;

import cn.hutool.core.convert.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
public class SupBaikeTypeService extends BaseService<SupBaikeTypeDao, SupBaikeType> {

	@Autowired
	private SupBaikeService supBaikeService;

	@Transactional(readOnly = false)
	public Result insertSupBaikeType(SupBaikeType supBaikeType) {
		if (StringUtils.isEmpty(supBaikeType))
			return Result.failure("输入参数有误");
		if (!StringUtils.isEmpty(dao.selectByName(supBaikeType.getName())))
			return Result.failure("分类名称不能重复");
		String id = IdGen.getObjectId();
		supBaikeType.setId(id);
		dao.insert(supBaikeType);
		return Result.success();
	}

	// 删除
	@Transactional(readOnly = false)
	public Result deleteTypeById(Map<String, Object> params) {
		String id = Convert.toStr(params.get("id"));
		if (StringUtils.isEmpty(id))
			return Result.failure("请输入要删除的id");
		if (StringUtils.isEmpty(dao.selectById(id)))
			return Result.failure("类别已经被删除或者不存在");
		SupBaikeType supBaikeType = new SupBaikeType();
		String deleteId = Convert.toStr(params.get("deleteId"));
		supBaikeType.setId(id);
		supBaikeType.setDeleteFlag(1);
		supBaikeType.setDeleteId(deleteId);
		supBaikeType.setDeleteTime(System.currentTimeMillis() + "");
		// 删除
		dao.update(supBaikeType);
		supBaikeService.deleteByTypeId(params);
		return Result.success();
	}

	public Result selectNumberByTypeId(Map<String, Object> params) {
		String id = Convert.toStr(params.get("id"));
		if (StringUtils.isEmpty(id))
			return Result.failure("请输入类型id");
		if (StringUtils.isEmpty(dao.selectById(id)))
			return Result.failure("类别id不存在");
		return StringUtils.isEmpty(dao.selectById(id)) ? Result.failure("类别id不存在")
				: Result.success(supBaikeService.selectNumberByTypeId(id));
	}

	// 更新 名称不能重复
	@Transactional(readOnly = false)
	public Result updateSupBaikeType(SupBaikeType supBaikeType) {
		String id = Convert.toStr(supBaikeType.getId());
		String name = supBaikeType.getName();
		if (StringUtils.isEmpty(id))
			return Result.failure("id不能为空");
		SupBaikeType dataBasePojo = dao.selectById(id);
		if (StringUtils.isEmpty(dataBasePojo))
			return Result.failure("数据不存在或者已删除");
		// 名称是否重复
		if (!name.equals(dataBasePojo.getName())) {
			dataBasePojo = dao.selectByName(name);
			if (!StringUtils.isEmpty(dataBasePojo)) {
				return Result.failure("分类名称不能重复");
			}
		}
		dao.update(supBaikeType);
		return Result.success();
	}

	// 详情
	public Result details(@RequestBody Map<String, Object> params) {
		String id = Convert.toStr(params.get("id"));
		if (StringUtils.isEmpty(id))
			return Result.failure("请输入需要查询的参数");
		SupBaikeType supBaikeType = dao.selectById(id);
		return StringUtils.isEmpty(supBaikeType) ? Result.failure("查询不存在") : Result.success(supBaikeType);
	}

}