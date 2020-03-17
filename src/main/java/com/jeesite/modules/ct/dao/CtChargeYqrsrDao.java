/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.dao;

import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ct.entity.CtChargeYqrsr;

/**
 * 应确认收入DAO接口
 * @author GJ
 * @version 2019-11-07
 */
@MyBatisDao
public interface CtChargeYqrsrDao extends CrudDao<CtChargeYqrsr> {
	//根据主键删除数据
	@Update("update ct_charge_yqrsr set dr=1 where pk_charge_yqrsr=#{pk_charge_yqrsr}")
	void deleteData(@Param("pk_charge_yqrsr") String pk_charge_yqrsr);
	
	//收入确认参照应确认收入,已参照但未审批的应确认收入不可再次被参照
	String  sql2="select a.pk_charge_yqrsr from ct_charge_yqrsr a where vbillstatus=1 and nvl(a.dr,0)=0 and a.nqsmny<>0 and not exists( select  1 from ct_charge_srqr_b b,"
			+ "ct_charge_srqr s where b.lyvbillno=a.pk_charge_yqrsr and nvl(b.dr,0)=0  and nvl(s.dr,0)=0 and b.pk_charge_srqr=s.pk_charge_srqr and s.vbillstatus<>1)";
	@Select(sql2)
	List<String> getCtChargeYqrsrByCode2();
}