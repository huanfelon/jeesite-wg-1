/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.dao;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ct.entity.CtChargeSk;

/**
 * 收款单DAO接口
 * @author GJ
 * @version 2019-11-05
 */
@MyBatisDao
public interface CtChargeSkDao extends CrudDao<CtChargeSk> {
	
	//根据主键删除数据
	@Update("update ct_charge_sk set dr=1 where pk_charge_sk=#{pk_charge_sk}")
	void deleteData(@Param("pk_charge_sk") String pk_charge_sk);
	
	//收款单取消审批判断是不是最新一笔收款单
	@Select("select count(*) from ct_charge_sk_b b left join ct_charge_sk t on t.pk_charge_sk=b.pk_charge_sk where nvl(b.dr, 0) = 0 and nvl(t.dr,0)=0 and exists "
			+ "(select 1 from ct_charge_sk_b bb left join ct_charge_sk tt on tt.pk_charge_sk=bb.pk_charge_sk where nvl(bb.dr, 0) = 0 and nvl(tt.dr,0)=0 "
			+ "and tt.pk_charge_sk<>t.pk_charge_sk and bb.lyvbillno=b.lyvbillno and tt.creationtime>=t.approvetime) and t.pk_charge_sk = #{pk_charge_sk}")
	Integer getSkCountUnsub(@Param("pk_charge_sk") String pk_charge_sk);
	
}