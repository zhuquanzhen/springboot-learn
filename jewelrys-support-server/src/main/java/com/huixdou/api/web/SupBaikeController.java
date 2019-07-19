package com.huixdou.api.web;

import com.huixdou.api.bean.SupBaike;
import com.huixdou.api.service.SupBaikeService;
import com.huixdou.common.base.BaseController;

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
import javax.validation.Valid;

@RestController
@RequestMapping("/sup/sup-baike")
public class SupBaikeController extends BaseController {

	@Autowired
	private SupBaikeService supBaikeService;

	/**
	 * 显示和查询显示List
	 * 
	 * @param typeId
	 *            类型id
	 * @param status
	 *            状态
	 * @param title
	 *            标题
	 * @return
	 */
	@GetMapping("/list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(supBaikeService.list(params));
	}

	/**
	 * 这是新增
	 * 
	 * @param content
	 *            内容
	 * @param title
	 *            标题
	 * @param fileId
	 *            海报id
	 * @param typeId
	 *            类别id
	 * @param summary
	 *            简介
	 * @param status
	 *            状态
	 * @return
	 */
	@PostMapping("/insert")
	public Object insert(@Valid @RequestBody SupBaike supBaike) throws Exception {
		return supBaikeService.insertSupBaike(supBaike);
	}

	/**
	 * 删除
	 * 
	 * @param list
	 *            删除的id集合
	 * @return
	 */

	@PostMapping("/delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		// 获取当前用户的ID
		if (StringUtils.isEmpty(params.get("list")))
			return failure("请至少选择一个");
		params.put("delectId", getUserId());
		return supBaikeService.delete(params);
	}

	/**
	 * 发布
	 * 
	 * @param list
	 *            发布的id集合
	 * @return
	 */
	@PostMapping("/publish")
	public Object publish(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		if (StringUtils.isEmpty(params.get("list")))
			return failure("请至少选择一个");
		return supBaikeService.publish(params, request);
	}

	/**
	 * 撤回
	 * 
	 * @param list
	 *            撤回的id集合
	 * @return
	 */
	@PostMapping("/recall")
	public Object recall(@RequestBody Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("list")))
			return failure("请至少选择一个");
		return supBaikeService.recall(params);
	}

	/**
	 * 查看
	 * 
	 * @param id
	 *            查看详情id
	 * @return
	 */
	@GetMapping("/details")
	public Object details(@RequestParam Map<String, Object> params) {
		return supBaikeService.details(params);
	}

	/**
	 * 编辑
	 * 
	 * @param params
	 * @return
	 */
	@PostMapping("/update")
	public Object update(@Valid @RequestBody SupBaike supBaike) {
		return supBaikeService.updateSupBaike(supBaike);
	}
	/**
	 *  取消 
	 * @param params list  ID 集合
	 * @return
	 */
	@PostMapping("/cancel")
	public Object cancel(@RequestBody Map<String, Object> params) {
		if (StringUtils.isEmpty(params.get("list")))
			return failure("请至少选择一个");
		return supBaikeService.cancel(params);
	}
	
	/**
	 * 信息推送
	 * @param params
	 * @return
	 */
	@PostMapping("/push")
	public Object pushMessage(@RequestBody Map<String, Object> params) {
		return StringUtils.isEmpty(params.get("id")) ? failure("参数不能为空") : supBaikeService.pushMessage(params);
	}
	/**
	 * 
	 * @param params
	 * @return
	 */
	@GetMapping("/copyUrl")
	public Object copyUrl(@RequestParam Map<String, Object> params) {
		return supBaikeService.copyUrl(params);
	}
}
