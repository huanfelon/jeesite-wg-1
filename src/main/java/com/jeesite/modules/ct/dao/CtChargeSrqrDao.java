/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.dao;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ct.entity.CtChargeSrqr;

/**
 * 收入确认DAO接口
 * @author GJ
 * @version 2019-11-07
 */
@MyBatisDao
public interface CtChargeSrqrDao extends CrudDao<CtChargeSrqr> {
	//根据主键删除数据
	@Update("update ct_charge_srqr set dr=1 where pk_charge_srqr=#{pk_charge_srqr}")
	void deleteData(@Param("pk_charge_srqr") String pk_charge_srqr);
	//确认收入取消审批判断是不是最新一笔
	@Select("select count(*) from CT_CHARGE_SRQR t where nvl(t.dr,0)=0  and exists(select s.approvetime from CT_CHARGE_SRQR s where nvl(s.dr,0)=0 and t.creationtime>s.approvetime and  s.pk_charge_srqr=#{pk_charge_srqr})")
	Integer getSrqrCountUnsub(@Param("pk_charge_srqr") String pk_charge_srqr);
}