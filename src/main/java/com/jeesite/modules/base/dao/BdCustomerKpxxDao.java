/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.base.entity.BdCustomerKpxx;

/**
 * 客户信息中心DAO接口
 * @author tcl
 * @version 2019-11-05
 */
@MyBatisDao
public interface BdCustomerKpxxDao extends CrudDao<BdCustomerKpxx> {
	
}