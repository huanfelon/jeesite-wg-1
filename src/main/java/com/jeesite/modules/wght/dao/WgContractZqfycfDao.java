/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.wght.entity.WgContractZqfycf;

/**
 * 合同管理表体-其他周期费用拆分页签（隐藏）DAO接口
 * @author LY
 * @version 2019-10-31
 */
@MyBatisDao
public interface WgContractZqfycfDao extends CrudDao<WgContractZqfycf> {
	
	//根据主键删除数据，更新DR=1
	@Update("update wg_contract_zqfycf set dr=1,ts=sysdate where pk_contract_zqfycf=#{pk_contract_zqfycf}")
	Integer deleteDataNewByPk(@Param("pk_contract_zqfycf") String pk_contract_zqfycf);
	//根据表头主键删除数据，更新DR=1
	@Update("update wg_contract_zqfycf set dr=1,ts=sysdate where pk_contract=#{pk_contract}")
	Integer deleteDataNewByFk(@Param("pk_contract") String pk_contract);
	
}