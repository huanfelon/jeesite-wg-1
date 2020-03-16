/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.wght.entity.WgContractZzq;

/**
 * 合同管理表体-增长期页签DAO接口
 * @author LY
 * @version 2019-10-31
 */
@MyBatisDao
public interface WgContractZzqDao extends CrudDao<WgContractZzq> {
	
	//根据主键删除数据，更新DR=1
	@Update("update wg_contract_zzq set dr=1,ts=sysdate where pk_contract_zzq=#{pk_contract_zzq}")
	Integer deleteDataNewByPk(@Param("pk_contract_zzq") String pk_contract_zzq);
	//根据表头主键删除数据，更新DR=1
	@Update("update wg_contract_zzq set dr=1,ts=sysdate where pk_contract=#{pk_contract}")
	Integer deleteDataNewByFk(@Param("pk_contract") String pk_contract);
	
}