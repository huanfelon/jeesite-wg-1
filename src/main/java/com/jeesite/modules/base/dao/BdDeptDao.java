/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.dao;

import java.util.List;
import java.util.Map;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.base.entity.BdDept;

/**
 * bd_deptDAO接口
 * @author gj
 * @version 2019-04-24
 */
@MyBatisDao
public interface BdDeptDao extends TreeDao<BdDept> {
	//查询部门
	List<Map<String,String>> findOrgBycode(String pk_url);
	//查询经办人
	List<Map<String,String>> findDeptBycode(String user_code);
	//查询部门管理员
      String selectNameByCode(List<String> code);
    //查询岗位
  	List<Map<String,String>> findPostBycode(String user_code);
      
      
}