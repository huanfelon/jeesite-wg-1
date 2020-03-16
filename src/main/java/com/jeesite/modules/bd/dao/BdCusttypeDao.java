/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.bd.entity.BdCusttype;

/**
 * 客户类型DAO接口
 * @author GJ
 * @version 2019-10-30
 */
@MyBatisDao
public interface BdCusttypeDao extends TreeDao<BdCusttype> {
	
}