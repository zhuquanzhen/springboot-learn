package com.huixdou.api.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.bean.SupBaikeType;
import com.huixdou.api.service.SupBaikeTypeService;
import com.huixdou.common.base.BaseController;

/**
 * 珠宝知识库类别
 *
 * @author 范雪冰
 */
@RestController
@RequestMapping("/sup/sup-baike-type")
public class SupBaikeTypeController extends BaseController {

	@Autowired
	private SupBaikeTypeService supBaikeTypeService;

	/**
	 * 显示列表
	 * 
	 * @param
	 * @return
	 */
	@GetMapping("/list")
	public Object selectListByType() {
		Map<String, Object> params = new HashMap<>();
		return success(supBaikeTypeService.selectList(params));
	}

	/**
	 * 查看详情
	 * 
	 * @param id
	 *            要查询详情的id
	 * @return
	 */
	@GetMapping("/details")
	public Object details(@RequestParam Map<String, Object> params) {
		return supBaikeTypeService.details(params);
	}

	/**
	 * 编辑
	 * 
	 * @param id
	 *            修改的id
	 * @param remarks
	 *            备注
	 * @param name
	 *            名称
	 * @return
	 */
	@PostMapping("/update")
	public Object updateType(@Valid @RequestBody SupBaikeType supBaikeType) {
		return supBaikeTypeService.updateSupBaikeType(supBaikeType);
	}

	/**
	 * 新增
	 * 
	 * @param remarks
	 *            备注
	 * @param name
	 *            名称
	 * @return
	 */
	@PostMapping("/insert")
	public Object insert(@Valid @RequestBody SupBaikeType supBaikeType) {
		return supBaikeTypeService.insertSupBaikeType(supBaikeType);
	}

	/**
	 * 删除类型和他下面的
	 * 
	 * @param id
	 *            要删除的id
	 * @return
	 */
	@PostMapping("/delete")
	public Object delete(@RequestBody Map<String, Object> params) {
		params.put("deleteId", getUserId());
		return supBaikeTypeService.deleteTypeById(params);
	}

	/**
	 * 删除的时候 显示要删除的数字
	 * 
	 * @param id
	 *            删除的id
	 * @return
	 */
	@GetMapping("/count")
	public Object selectNumberBy(@RequestParam Map<String, Object> params) {
		return supBaikeTypeService.selectNumberByTypeId(params);
	}
}
