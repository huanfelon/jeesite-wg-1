/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.dao;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ct.entity.CtChargeYs;

/**
 * 应收单DAO接口
 * @author GJ
 * @version 2019-11-04
 */
@MyBatisDao
public interface CtChargeYsDao extends CrudDao<CtChargeYs> {
	//根据主键删除数据
	@Update("update ct_charge_ys set dr=1 where pk_charge_ys=#{pk_charge_ys}")
	void deleteData(@Param("pk_charge_ys") String pk_charge_ys);
		
	//收款单参照应收单,已参照但未审批的应收单不可再次被参照
	String  sql2="select a.pk_charge_ys from ct_charge_ys a where vbillstatus=1 and nvl(a.dr,0)=0 and a.nqsmny<>0 and not exists( select  1 from ct_charge_sk_b b,"
			+ "ct_charge_sk s where b.lyvbillno=a.pk_charge_ys and nvl(b.dr,0)=0  and nvl(s.dr,0)=0 and b.pk_charge_sk=s.pk_charge_sk and s.vbillstatus<>1)";
	@Select(sql2)
	List<String> getCtChargeYsByCode2();
	
}