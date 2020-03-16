/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.wght.entity.WgContractHouse;

/**
 * 合同管理表体-房产信息页签DAO接口
 * @author LY
 * @version 2019-10-31
 */
@MyBatisDao
public interface WgContractHouseDao extends CrudDao<WgContractHouse> {
	
	//根据主键删除数据，更新DR=1
	@Update("update wg_contract_house set dr=1,ts=sysdate where pk_contract_house=#{pk_contract_house}")
	Integer deleteDataNewByPk(@Param("pk_contract_house") String pk_contract_house);
	//根据表头主键删除数据，更新DR=1
	@Update("update wg_contract_house set dr=1,ts=sysdate where pk_contract=#{pk_contract}")
	Integer deleteDataNewByFk(@Param("pk_contract") String pk_contract);
	
}