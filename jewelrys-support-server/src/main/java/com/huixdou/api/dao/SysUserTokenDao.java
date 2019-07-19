package com.huixdou.api.dao;

import com.huixdou.api.bean.SysUserToken;
import com.huixdou.common.base.BaseDao;

public interface SysUserTokenDao extends BaseDao<SysUserToken> {

	SysUserToken selectByToken(String accessToken);

}
