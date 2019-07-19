package com.huixdou.api.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.huixdou.api.bean.SysMenu;
import com.huixdou.api.dao.SysMenuDao;
import com.huixdou.api.vo.MenuDetailVO;
import com.huixdou.api.vo.MenuListVO;
import com.huixdou.api.vo.MenuTreeVO;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.Constant;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SysMenuService extends BaseService<SysMenuDao, SysMenu> {
	
	public List<SysMenu> findByParentId(Integer parentId) {
		return dao.findByParentId(parentId);
	}

	public List<String> getMenuPerms(Integer userId) {
		List<Integer> menuIdList = dao.selectAllMenuId(userId);
		List<SysMenu> menuList = dao.selectByIdList(menuIdList);
		List<String> permsList = Lists.newArrayList();
		for (SysMenu item : menuList) {
			if ("btn".equals(item.getTypeCode())) {
				permsList.add(item.getPerms());
			}
		}
		return permsList;
	}
	
	public List<SysMenu> getUserMenuList(Integer userId) {
		// 系统管理员，拥有最高权限
		if (userId == Constant.SUPER_ADMIN) {
			return getAllMenuList(null);
		}

		// 用户菜单列表
		List<Integer> menuIdList = dao.selectAllMenuId(userId);
		
		return getAllMenuList(menuIdList);
	}

	private List<SysMenu> getAllMenuList(List<Integer> menuIdList) {
		// 查询根菜单列表
		List<SysMenu> menuList = findByParentId(0, menuIdList);
		// 递归获取子菜单
		getMenuTreeList(menuList, menuIdList);

		return menuList;
	}

	private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Integer> menuIdList) {
		List<SysMenu> subMenuList = Lists.newArrayList();
		for (SysMenu entity : menuList) {
			// 目录
			if ("dir".equals(entity.getTypeCode())) {
				entity.setChildren(getMenuTreeList(findByParentId(entity.getId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		return subMenuList;
	}

	private List<SysMenu> findByParentId(int parentId, List<Integer> menuIdList) {
		List<SysMenu> menuList = findByParentId(parentId);
		if (menuIdList == null) {
			return menuList;
		}

		List<SysMenu> userMenuList = Lists.newArrayList();
		for (SysMenu menu : menuList) {
			if (menuIdList.contains(menu.getId())) {
				userMenuList.add(menu);
			}
		}
		
		return userMenuList;
	}
	
	public Map<String, Object> getMenuVO() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("total", dao.selectParentIdCount());
		result.put("list", getTreeVO(0));
		return result;
	}
	
	public List<MenuTreeVO> getTreeVO(Integer parentId) {
		List<MenuTreeVO> treeList = Lists.newArrayList();
		List<SysMenu> list = dao.findByParentId(parentId);
		for (int i = 0, length = list.size(); i < length; i++) {
			SysMenu item = list.get(i);
			MenuTreeVO treeVO = new MenuTreeVO();
			BeanUtil.copyProperties(item, treeVO);
			treeVO.setChildren(getTreeVO(item.getId()));
			treeList.add(treeVO);
		}
		return treeList;
	}
	
	public List<MenuTreeVO> getMenuTreeVO(Integer parentId) {
		List<MenuTreeVO> treeList = Lists.newArrayList();
		List<SysMenu> menuList = dao.findByParentId(parentId);
		for (SysMenu item : menuList) {
			if ("btn".equals(item.getTypeCode())) {
				continue;
			}
			MenuTreeVO treeVO = new MenuTreeVO();
			BeanUtil.copyProperties(item, treeVO);
			treeVO.setChildren(getMenuTreeVO(item.getId()));
			treeList.add(treeVO);
		}
		return treeList;
	}
	
	public Map<String, Object> getBtnList(Integer parentId) {
		Map<String, Object> result = Maps.newHashMap();
		List<MenuListVO> menuList = Lists.newArrayList();
		List<SysMenu> list = dao.selectAllBtn(parentId);
		for (SysMenu item : list) {
			MenuListVO listVO = new MenuListVO();
			BeanUtil.copyProperties(item, listVO);
			menuList.add(listVO);
		}
		result.put("total", dao.selectBtnCount(parentId));
		result.put("list", menuList);
		
		return result;
	}
	
	public List<Integer> getId(Integer id) {
		SysMenu menu = dao.selectById(id);
		List<Integer> list = Lists.newArrayList();
		if (menu.getParentId() != 0) {
			if (ObjectUtil.isNotNull(menu)) {
				list.addAll(getId(menu.getParentId()));
			}
		}
		if (ObjectUtil.isNotNull(menu)) {
			list.add(menu.getId());
		}
		return list;
	}
	
	public MenuDetailVO selectById(Integer id) {
		MenuDetailVO detailVO = new MenuDetailVO();
		SysMenu menu = super.selectById(id);
		BeanUtil.copyProperties(menu, detailVO);
		return detailVO;
	}
	
	// 存目录
	@Transactional(readOnly = false)
	public Result create(MenuDetailVO detailVO) {
		if (detailVO.getParentId() != 0) {
			createMenu(detailVO);
			return Result.success();
		}
		List<SysMenu> list = dao.findByParentId(0);
		for (SysMenu item : list) {
			if (detailVO.getName().equals(item.getName())) {
				return Result.failure("目录已存在");
			}
		}
		insert(detailVO, "dir");
		return Result.success();
	}
	
	// 存菜单
	@Transactional(readOnly = false)
	private void createMenu(MenuDetailVO detailVO) {
		if (StringUtils.isBlank(detailVO.getUrl()))
			throw new RuntimeException("url 参数不能为空");
		SysMenu menu = dao.selectByParentIdAndName(detailVO.getParentId(), detailVO.getName());
		if (ObjectUtil.isNotNull(menu)) {
			throw new RuntimeException("菜单已存在");
		}
		insert(detailVO, "menu");
	}
	
	// 存按钮
	@Transactional(readOnly = false)
	public Result createBtn(MenuDetailVO detailVO) {
		if (StringUtils.isBlank(detailVO.getPerms()))
			throw new RuntimeException("perms 参数不能为空");
		SysMenu menu = dao.selectByParentIdAndName(detailVO.getParentId(), detailVO.getName());
		if (ObjectUtil.isNotNull(menu))
			throw new RuntimeException("按钮已存在");
		insert(detailVO, "btn");
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	private void insert(MenuDetailVO detailVO, String typeCode) {
		SysMenu item = new SysMenu();
		BeanUtil.copyProperties(detailVO, item);
		item.setTypeCode(typeCode);
		super.insert(item);
	}
	
	@Transactional(readOnly = false)
	public Result update(MenuDetailVO detailVO) {
		if (StrUtil.isBlank(Convert.toStr(detailVO.getId()))) {
			return Result.failure("id 参数不能为空");
		}
		SysMenu item = dao.selectByParentIdAndName(detailVO.getParentId(), detailVO.getName());
		if (ObjectUtil.isNotNull(item)) {
			if (!item.getId().equals(detailVO.getId())) {
				return Result.failure("该类型已存在");
			}
		}
		SysMenu menu = new SysMenu();
		BeanUtil.copyProperties(detailVO, menu);
		super.update(menu);
		return Result.success();
	}
	
	@Transactional(readOnly = false)
	public Result delete(Map<String, Object> params) {
		Integer id = Convert.toInt(params.get("id"));
		if (StrUtil.isBlank(Convert.toStr(id)))
			return Result.failure("id 参数不能为空");
		params.put("deleteTime", System.currentTimeMillis());
		// 获取全部父级
		Set<Integer> idList = getIdList(id);
		idList.add(id);
		params.replace("id", idList);
		super.deleteByMap(params);
		return Result.success();
	}
	
	private Set<Integer> getIdList(Integer id) {
		List<SysMenu> menuList = dao.findByParentId(id);
		Set<Integer> list = Sets.newHashSet();
		for (int i = 0, length = menuList.size(); i < length; i++) {
			SysMenu item = menuList.get(i);
			list.addAll(getIdList(item.getId()));
			list.add(item.getId());
		}
		return list;
	}
	
}
