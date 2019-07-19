package com.huixdou.api.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.SysUserToken;
import com.huixdou.api.dao.SysUserTokenDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.utils.IdGen;


@Service
public class SysUserTokenService extends BaseService<SysUserTokenDao, SysUserToken> {

	// 8小时后过期
	private final static int EXPIRE = 8 * 3600;
	
	public SysUserToken selectByToken(String token) {
		return dao.selectByToken(token);
	}
	
	@Transactional(readOnly = false)
	public SysUserToken createToken(Integer userId) {
		// 生成一个token
		String token = IdGen.uuid();

		// 当前时间
		Date now = new Date();
		// 过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		// 判断是否生成过token
		SysUserToken userToken = this.selectById(userId);

		if (userToken != null) {
			userToken.setToken(token);
			userToken.setUpdateTime(now);
			userToken.setExpireTime(expireTime);

			// 更新token
			this.update(userToken);
		} else {
			userToken = new SysUserToken();
			userToken.setUserId(userId);
			userToken.setToken(token);
			userToken.setUpdateTime(now);
			userToken.setExpireTime(expireTime);

			// 保存token
			this.insert(userToken);
		}
		
		return userToken;

	}
	
	@Transactional(readOnly = false)
	public void removeToken(Integer userId) {
		// 生成一个token
		String token = IdGen.uuid();

		// 修改token
		SysUserToken userToken = new SysUserToken();
		userToken.setUserId(userId);
		userToken.setToken(token);
		this.update(userToken);
	}

}
