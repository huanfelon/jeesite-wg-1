/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.dao;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * zl_projectDAO接口
 * @author GuoJ
 * @version 2019-07-19
 */
@MyBatisDao
public interface ZlProjectDao extends TreeDao<ZlProject> {
	//根据主键删除数据
		@Update("update zl_project set dr=1 where pk_project=#{pk_project}")
		void deleteData(@Param("pk_project") String pk_project);
}