package com.huixdou.api.web;

import com.huixdou.api.service.MerNewsService;
import com.huixdou.common.base.BaseController;

import cn.hutool.core.convert.Convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 珠宝商资讯
 */
@RestController
@RequestMapping("/sup/mer-news")
public class MerNewsController extends BaseController {

	@Autowired
	private MerNewsService merNewsService;

	/**
	 * 显示和查询显示List
	 * 
	 * @param startDate
	 *            endDate 开始时间 结束时间
	 * @param status
	 *            状态
	 * @param title
	 *            标题
	 * @return
	 */
	@GetMapping("/list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(merNewsService.list(params));
	}

	/**
	 * 发布
	 * 
	 * @param list
	 *            发布的list
	 * @return
	 */
	@PostMapping("/publish")
	public Object publish(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception{
		return StringUtils.isEmpty(params.get("list")) ? failure("请至少选择一个") : merNewsService.publish(params,request);
	}

	/**
	 * 删除
	 * 
	 * @param list
	 *            删除的list
	 * @return
	 */

	@PostMapping("/delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("list")))
			return failure("请至少选择一个");
		params.put("delectId", getUserId());
		return merNewsService.delete(params);
	}

	/**
	 * 撤回
	 * 
	 * @param list
	 *            撤回的list
	 * @return
	 */

	@PostMapping("/recall")
	public Object recall(@RequestBody Map<String, Object> params) {
		return StringUtils.isEmpty(params.get("list")) ? failure("请至少选择一个") : merNewsService.recall(params);
	}

	/**
	 * 查看详情
	 * 
	 * @param id
	 *            id
	 * @return
	 */
	@GetMapping("/details")
	public Object details(@RequestParam Map<String, Object> params) {
		String id = Convert.toStr(params.get("id"));
		return StringUtils.isEmpty(id) ? failure("未传入参数") : merNewsService.details(id);
	}

	/**
	 * 获取数据项（非树形结构）
	 * 
	 * @param typeCode
	 *            字典类型
	 * @return
	 */
	@GetMapping("get")
	public Object get(@RequestParam("typeCode") String typeCode) {
		return StringUtils.isEmpty(typeCode) ? failure("typeCode 参数不能为空") : success(merNewsService.runGetTypeCode(typeCode));
	}

}
