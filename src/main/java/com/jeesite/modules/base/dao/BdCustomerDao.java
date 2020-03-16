/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.base.entity.BdCustomer;

/**
 * 客户信息中心DAO接口
 * @author tcl
 * @version 2019-11-05
 */
@MyBatisDao
public interface BdCustomerDao extends CrudDao<BdCustomer> {
	
	//查询项目是否被业务参照
	@Select("select count(*) from wg_contract u where nvl(u.dr,0)=0 and u.pk_project=#{projectid} ")
	Integer getXmIsRefByProjectid(@Param("projectid") String projectid);
	
}