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
import com.jeesite.modules.ct.entity.CtChargeSrqrB;

/**
 * 收入确认DAO接口
 * @author GJ
 * @version 2019-11-07
 */
@MyBatisDao
public interface CtChargeSrqrBDao extends CrudDao<CtChargeSrqrB> {
	//根据主键删除数据
	@Update("update ct_charge_srqr_b set dr=1 where pk_charge_srqr=#{pk_charge_srqr}")
	void deleteData(@Param("pk_charge_srqr") String pk_charge_srqr);
	
	//根据应收单主键查询数据(取消审批时有没有被收款单参照)
	@Select("select * from ct_charge_srqr_b where nvl(dr,0)=0 and lyvbillno=#{pks} ")
	List<CtChargeSrqrB> getCtChargeSrqrBByCode(@Param("pks") String pks);
}