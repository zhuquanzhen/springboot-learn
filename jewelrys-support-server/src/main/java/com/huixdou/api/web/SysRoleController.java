package com.huixdou.api.web;

import com.huixdou.api.bean.SysRole;
import com.huixdou.api.service.SysRoleService;
import com.huixdou.common.annotation.RequiresPermissions;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import javax.validation.Valid;

@RestController
@RequestMapping("/sup/sys-role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService roleService;
    
    /**
     * 获取角色列表
     * @param params
     * @return
     */
    @GetMapping("list")
    public Object list(@RequestParam Map<String, Object> params) {
        return success(roleService.selectList(params));
    }
    
    /**
     * 获取角色详细信息
     * @param params
     * @return
     */
    @GetMapping("detail")
    public Object detail(@RequestParam("id") Integer id) {
    	if (StringUtils.isBlank(Convert.toStr(id))) {
    		return failure("id 参数不能为空");
    	}
    	return success(roleService.detail(id));
    }
    
    /**
     * 新增角色
     * @param role
     * @return
     */
    @RequiresPermissions(value = "sys-permission-add-role")
    @PostMapping("insert")
    public Object insert(@RequestBody @Valid SysRole role) {
    	return roleService.create(role);
    }
    
    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @PostMapping("update")
    public Object update(@RequestBody @Valid SysRole role) {
    	return roleService.updateRole(role);
    }
    
    /**
     * 删除角色
     * @param params
     * @return
     */
    @PostMapping("delete")
    public Object delete(@RequestBody Map<String, Object> params) {
    	params.put("deleteId", getUserId());
    	return roleService.delete(params);
    }
    
    /**
     * 获取角色的菜单列表
     * @param id	角色ID
     * @return
     */
    @GetMapping("get-menu")
    public Object getMenu(@RequestParam("id") Integer id) {
    	if (StrUtil.isBlank(Convert.toStr(id))) {
    		return failure("id 参数不能为空");
    	}
    	return success(roleService.getMenu(id));
    }
    
    /**
     * 保存角色的菜单列表
     * @param roleId	角色ID
     * @param menuId[]	菜单ID数组
     * @return
     */
    @RequiresPermissions(value = "sys-permission-add-btn")
    @PostMapping("save-menu")
    public Object saveMenu(@RequestBody Map<String, Object> params) {
    	return roleService.insertRoleMenu(params);
    }

}
