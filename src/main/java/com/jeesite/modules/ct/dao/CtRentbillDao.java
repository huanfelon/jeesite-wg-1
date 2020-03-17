/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ct.entity.CtRentbill;

/**
 * 租约账单DAO接口
 * @author tcl
 * @version 2019-11-08
 */
@MyBatisDao
public interface CtRentbillDao extends CrudDao<CtRentbill> {
	
}