/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeesite.modules.base.dao.CommonBaseDao;

/**
 * 客户信息中心Service
 * @author tcl
 * @version 2019-11-05
 */
@Service
public class CommonBaseService{
	
	@Autowired
	private CommonBaseDao baseDao;
	
	public Integer selectCount(String sql){
		return baseDao.selectCount(sql);
	}
	
	public Object selectObject(String sql){
		return baseDao.selectObject(sql);
	}
	
	public Map<String, Object> selectMapResult(String sql){
		return baseDao.selectMapResult(sql);
	}
	
	public List<Map<String, Object>> selectMapListResult(String sql){
		return baseDao.selectMapListResult(sql);
	}
	
	public Integer updateSql(String sql){
		return baseDao.updateSql(sql);
	}
	
	public Integer deleteSql(String sql){
		return baseDao.deleteSql(sql);
	}
	
}