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

import com.huixdou.api.bean.SysDict;
import com.huixdou.api.service.SysDictService;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;

@RestController
@RequestMapping("/sup/sys-dict")
public class SysDictController extends BaseController {
	
	@Autowired
	private SysDictService dictService;
	
	/**
	 * 获取数据项
	 * @param typeCode 字典类型
	 * @return
	 */
	@GetMapping("list")
	public Object list(String typeCode) {
		if (StringUtils.isBlank(typeCode))
			return failure("typeCode 参数不能为空");
		return success(dictService.getTree(typeCode));
	}
	
	/**
	 * 增加主项   新增子项
	 * @param dict
	 * @return
	 */
	@PostMapping("insert")
	public Object insert(@RequestBody @Valid SysDict dict) {
		return dictService.create(dict);
	}
	
	/**
	 * 查看数据项详情
	 * @param id
	 * @return
	 */
	@GetMapping("detail")
	public Object detail(@RequestParam("id") Integer id) {
		if (StringUtils.isBlank(Convert.toStr(id))) {
			return failure("id 参数不能为空");
		}
		return success(dictService.select(id));
	}
	
	/**
	 * 更新数据项
	 * @param dict
	 * @return
	 */
	@PostMapping("update")
	public Object update(@RequestBody @Valid SysDict dict) {
		return dictService.updateDict(dict);
	}
	
	/**
	 * 删除数据项
	 * @param id
	 * @return
	 */
	@PostMapping("delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		return dictService.delete(params);
	}
	
	/**
	 * 获取数据项（非树形结构）
	 * @param typeCode  字典类型
	 * @return
	 */
	@GetMapping("get")
	public Object get(@RequestParam("typeCode") String typeCode) {
		if (StringUtils.isBlank(typeCode)) {
			return failure("typeCode 参数不能为空");
		}
		return success(dictService.selectByTypeCodeSimpleness(typeCode));		
	}
	
}
