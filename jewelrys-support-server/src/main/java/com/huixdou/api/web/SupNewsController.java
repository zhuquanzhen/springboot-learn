package com.huixdou.api.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.bean.SupNews;
import com.huixdou.api.service.SupNewsService;
import com.huixdou.common.annotation.RequiresPermissions;
import com.huixdou.common.base.BaseController;
import cn.hutool.core.convert.Convert;

/**
 * 平台资讯
 */
@RestController
@RequestMapping("/sup/sup-news")
public class SupNewsController extends BaseController {

	@Autowired
	private SupNewsService supNewsService;

	/**
	 * 显示和查询显示List
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param status
	 *            状态
	 * @param title
	 *            标题
	 * @return
	 */
	@GetMapping("/list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(supNewsService.list(params));
	}

	/**
	 * 发布
	 * 
	 * @param list
	 *            要发布的ID List集合 单个发布可以是String类型
	 * @return
	 */
	@RequiresPermissions(value = "sup-platform-news-fb")
	@PostMapping("/publish")
	public Object publish(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
		return StringUtils.isEmpty(params.get("list")) ? failure("请至少选择一个吧") : supNewsService.publish(params,request);
	}

	/**
	 * 删除
	 * 
	 * @param list
	 *            要发布的ID List集合 单个发布可以是String类型
	 * @return
	 */
	@RequiresPermissions(value = "sup-platform-news-delete")
	@PostMapping("/delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("list"))) {
			return failure("请至少选择一个");
		}
		// 删除人的id
		params.put("delectId", getUserId());
		return supNewsService.delete(params);
	}

	/**
	 * 撤回
	 * 
	 * @param list
	 *            要发布的ID List集合 单个发布可以是String类型
	 * @return
	 */
	@RequiresPermissions(value = "sup-platform-news-ych")
	@PostMapping("/recall")
	public Object recall(@RequestBody Map<String, Object> params) {
		return StringUtils.isEmpty(params.get("list")) ? failure("请至少选择一个") : supNewsService.recall(params);
	}

	/**
	 * 查看详情
	 * 
	 * @param id
	 *            需要查询详情的ID
	 * @return
	 */
	@GetMapping("/details")
	public Object details(@RequestParam Map<String, Object> params) {
		String id = Convert.toStr(params.get("id"));
		return StringUtils.isEmpty(id) ? failure("未传入参数") : supNewsService.details(id);
	}

	/**
	 * 新增
	 * 
	 * @param content
	 *            内容
	 * @param title
	 *            标题
	 * @param label
	 *            标签
	 * @param fileId
	 *            海报id
	 * @param suorce
	 *            来源
	 * @param status
	 *            状态
	 * @return
	 */
	@RequiresPermissions(value = "sup-platform-news-add")
	@PostMapping("/insert")
	public Object insert(@Valid @RequestBody SupNews supNews)throws Exception {
		supNews.setCreateId(getUserId());
		return supNewsService.insertSupNews(supNews);
	}

	/**
	 * 编辑
	 * 
	 * @param content
	 *            内容
	 * @param title
	 *            标题
	 * @param label
	 *            标签
	 * @param fileId
	 *            海报id
	 * @param suorce
	 *            来源
	 * @param status
	 *            状态
	 * @return
	 */
	@PostMapping("/update")
	public Object update(@Valid @RequestBody SupNews supNews) {
		return supNewsService.updateSupNews(supNews);
	}

	
	@PostMapping("/cancel")
	public Object cancel(@RequestBody Map<String, Object> params) {
		return StringUtils.isEmpty(params.get("list")) ? failure("请至少选择一个") : supNewsService.cancel(params);
	}
	/**
	 * 信息推送
	 * @param params
	 * @return
	 */
	@PostMapping("/push")
	public Object pushMessage(@RequestBody Map<String, Object> params) {
		return StringUtils.isEmpty(params.get("id")) ? failure("参数不能为空") : supNewsService.pushMessage(params);
	}
	/**
	 * 复制url
	 * @param params
	 * @return
	 */
	@GetMapping("/copyUrl")
	public Object copyUrl(@RequestParam Map<String, Object> params) {
		return supNewsService.copyUrl(params);
	}
}
