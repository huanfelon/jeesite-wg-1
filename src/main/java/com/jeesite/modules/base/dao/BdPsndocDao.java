/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.base.entity.BdPsndoc;

/**
 * 人员基本信息DAO接口
 * @author LY
 * @version 2019-09-26
 */
@MyBatisDao
public interface BdPsndocDao extends CrudDao<BdPsndoc> {
	
	@Select("select p.pk_psndoc,p.name from bd_psndoc p where p.pk_psndoc=(select u.pk_psndoc from js_sys_user u where u.user_code=#{userCode})")
	Map<String,Object> getUserPsndoc(@Param("userCode") String userCode);
	
	@Select("select * from bd_psndoc p where p.pk_psndoc=(select u.pk_psndoc from js_sys_user u where u.user_code=#{userCode})")
	BdPsndoc getPsndocByUserCode(@Param("userCode") String userCode);
	
	//查询人员是否被用户选用
	@Select("select count(*) from js_sys_user u where u.pk_psndoc=#{pkPsndoc} and u.user_code<>#{userCode} and u.status<>1")
	Integer getCustCountByPsndoc(@Param("pkPsndoc") String pkPsndoc,@Param("userCode") String userCode);
	
	//查询最大单据号(所有档案节点)
	@Select("select max(${itemName}) from ${tableName} where nvl(dr,0)=0 and ${itemName} like '${djName}%' ")
	String getMaxBillCodeByDr(@Param("tableName") String tableName,@Param("itemName") String itemName,@Param("djName") String djName);
	
	//查询最大单据号(所有业务节点)
	@Select("select max(${itemName}) from ${tableName} where status<>1 and ${itemName} like '${djName}%' ")
	String getMaxBillCode(@Param("tableName") String tableName,@Param("itemName") String itemName,@Param("djName") String djName);
		
	//根据部门ids查询部门名称
	@Select("select wm_concat(to_char(dept_name)) from bd_dept r where r.pk_dept in(${pkDept})")
	public String getDeptNameByPks(@Param("pkDept") String pkDept);
	
}