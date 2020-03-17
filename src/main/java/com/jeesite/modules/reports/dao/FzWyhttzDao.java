/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.reports.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.reports.entity.FzWyhttz;

/**
 * 房租物业合同台账
 * @author LY
 * @version 2019-10-31
 */
@MyBatisDao
public interface FzWyhttzDao extends CrudDao<FzWyhttz> {
	
	@Select("${sql}")
	List<FzWyhttz> queryFzWyhttz(@Param("sql") String sql);
	
}