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

import com.huixdou.api.bean.AppArticle;
import com.huixdou.api.service.AppArticleService;
import com.huixdou.api.service.AppResultKeywordService;
import com.huixdou.common.base.BaseController;

@RestController
@RequestMapping("/sup/app-article")
public class AppArticleController extends BaseController {
	
	@Autowired
	private AppArticleService articleService;
	
	@Autowired
	private AppResultKeywordService keywordService;
	
	/**
	 * 文章列表
	 * @param params
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(articleService.selectList(params));
	}
	
	/**
	 * 查看详情
	 * @param id	主键ID
	 * @return
	 */
	@GetMapping("get")
	public Object get(@RequestParam Integer id) {
		return success(articleService.selectById(id));
	}
	
	/**
	 * 新增文章
	 * @param entity
	 * @return
	 */
	@PostMapping("insert")
	public Object insert(@RequestBody @Valid AppArticle entity) {
		entity.setCreateId(getUserId());
		articleService.create(entity);
		return success();
	}
	
	/**
	 * 修改文章
	 * @param entity
	 * @return
	 */
	@PostMapping("update")
	public Object update(@RequestBody @Valid AppArticle entity) {
		articleService.updateArticle(entity);
		return success();
	}
	
	/**
	 * 删除文章
	 * @param id	主键ID
	 * @return
	 */
	@PostMapping("delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		articleService.delete(params);
		return success();
	}
	
	/**
	 * 获取关联关键字数量
	 * @param id	主键ID
	 * @return
	 */
	@GetMapping("count")
	public Object count(Integer id) {
		return success(keywordService.selectByArtId(id));
	}
	
}
