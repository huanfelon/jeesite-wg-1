/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.bd.entity.BdFeescale;

/**
 * 收费标准DAO接口
 * @author GJ
 * @version 2019-10-25
 */
@MyBatisDao
public interface BdFeescaleDao extends CrudDao<BdFeescale> {
}