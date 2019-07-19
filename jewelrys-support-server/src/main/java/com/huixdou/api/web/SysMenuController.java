package com.huixdou.api.web;

import com.huixdou.api.service.SysMenuService;
import com.huixdou.api.vo.MenuDetailVO;
import com.huixdou.common.base.BaseController;

import cn.hutool.core.convert.Convert;

import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sup/sys-menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;
    
    /**
     * 导航菜单
     * @return
     */
    @GetMapping("nav")
    public Object nav() {
    	return success(menuService.getUserMenuList(getUserId()));
    }
    
    /**
     * 获取菜单(模块)列表  不包括按钮的树形结构
     * @param params
     * @return
     */
    @GetMapping("list")
    public Object list() {
    	return success(menuService.getMenuTreeVO(0));
    }
    
    /**
     * 菜单列表    树形结构
     * @return
     */
    @GetMapping("list-tree")
    public Object listTree() {
    	return success(menuService.getMenuVO());
    }
    
    /**
     * 增加菜单
     * @param menu
     * @return
     */
    @PostMapping("insert-dir")
    public Object insertDir(@RequestBody MenuDetailVO detailVO) {
    	return menuService.create(detailVO);
    }
    
    /**
     * 增加模块
     * @param menu
     * @return
     */
    @PostMapping("insert-menu")
    public Object insertMenu(@RequestBody MenuDetailVO detailVO) {
    	return menuService.create(detailVO);
    }
    
    /**
     * 增加操作列表
     * @param detailVO
     * @return
     */
    @PostMapping("insert-btn")
    public Object insertBtn(@RequestBody @Valid MenuDetailVO detailVO) {
    	return menuService.createBtn(detailVO);
    }
    
    /**
     * 删除菜单(模块)
     * @param id	主键ID
     * @return
     */
    @GetMapping("delete")
    public Object delete(@RequestParam Map<String, Object> params) {
    	params.put("deleteId", getUserId());
    	return menuService.delete(params);
    }
    
    /**
     * 根据模块ID查询模块操作列表
     * @param id	模块主键ID
     * @return
     */
    @GetMapping("btn-list")
    public Object buttonList(@RequestParam("id") Integer id) {
    	if (StringUtils.isBlank(Convert.toStr(id)))
    		return failure("id 参数不能为空");
    	return success(menuService.getBtnList(id));
    }
    
    /**
     * 获取模块详情
     * @param id	主键ID
     * @return
     */
    @GetMapping("get")
    public Object get(@RequestParam("id") Integer id) {
    	if (StringUtils.isBlank(Convert.toStr(id)))
    		return failure("id 参数不能为空");
    	return success(menuService.selectById(id));
    }
    
    /**
     * 更新模块
     * @param detailVO
     * @return
     */
    @PostMapping("update")
    public Object update(@RequestBody @Valid MenuDetailVO detailVO) {
    	return menuService.update(detailVO);
    }
    
}
