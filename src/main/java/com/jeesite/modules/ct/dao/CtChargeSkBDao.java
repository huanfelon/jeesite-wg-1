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
import com.jeesite.modules.ct.entity.CtChargeSkB;

/**
 * 收款单DAO接口
 * @author GJ
 * @version 2019-11-05
 */
@MyBatisDao
public interface CtChargeSkBDao extends CrudDao<CtChargeSkB> {
	//根据主键删除数据
	@Update("update ct_charge_sk_b set dr=1 where pk_charge_sk=#{pk_charge_sk}")
	void deleteData(@Param("pk_charge_sk") String pk_charge_sk);
	
	//根据应收单主键查询数据(取消审批时有没有被收款单参照)
	@Select("select * from ct_charge_sk_b where nvl(dr,0)=0 and lyvbillno=#{pks} ")
	List<CtChargeSkB> getCtChargeSkBByCode(@Param("pks") String pks);
}