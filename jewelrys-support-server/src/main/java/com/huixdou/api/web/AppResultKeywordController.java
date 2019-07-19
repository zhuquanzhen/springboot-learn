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

import com.huixdou.api.bean.AppResultKeyword;
import com.huixdou.api.service.AppResultKeywordService;
import com.huixdou.common.base.BaseController;

@RestController
@RequestMapping("/sup/app-result-keyword")
public class AppResultKeywordController extends BaseController {
	
	@Autowired
	private AppResultKeywordService keywordService;
	
	/**
	 * 获取关键字列表
	 * @param artId		关联文章id
	 * @param pageSize	页面大小
	 * @param pageNum	页码
	 * @param status	状态
	 * @param keyword	关键字
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(keywordService.selectPage(params));
	}
	
	/**
	 * 查看详情
	 * @param id	主键ID
	 * @return
	 */
	@GetMapping("get")
	public Object get(@RequestParam Integer id) {
		return success(keywordService.selectById(id));
	}
	
	/**
	 * 新增关键字
	 * @param entity
	 * @return
	 */
	@PostMapping("insert")
	public Object insert(@RequestBody @Valid AppResultKeyword entity) {
		entity.setCreateId(getUserId());
		keywordService.create(entity);
		return success();
	}
	
	/**
	 * 修改关键字
	 * @param entity
	 * @return
	 */
	@PostMapping("update")
	public Object update(@RequestBody @Valid AppResultKeyword entity) {
		keywordService.updateEntity(entity);
		return success();
	}
	
	/**
	 * 启用
	 * @param id[]	主键ID数组
	 * @return
	 */
	@PostMapping("enable")
	public Object enable(@RequestBody Map<String, Object> params) {
		keywordService.updateStatus(params, 0);
		return success();
	}
	
	/**
	 * 禁用
	 * @param id[]	主键ID数组
	 * @return
	 */
	@PostMapping("disable")
	public Object disable(@RequestBody Map<String, Object> params) {
		keywordService.updateStatus(params, 1);
		return success();
	}
	
	/**
	 * 删除
	 * @param id[]	主键ID数组
	 * @return
	 */
	@PostMapping("delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		keywordService.delete(params);
		return success();
	}
}
