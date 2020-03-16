/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test2.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.test2.entity.TestTree2;

/**
 * 测试树表DAO接口
 * @author tcl
 * @version 2019-02-14
 */
@MyBatisDao
public interface TestTree2Dao extends TreeDao<TestTree2> {
	
}