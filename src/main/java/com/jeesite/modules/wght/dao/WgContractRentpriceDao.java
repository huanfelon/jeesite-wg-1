/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.wght.entity.WgContractRentprice;

/**
 * 合同管理表体-年租金页签（隐藏）DAO接口
 * @author LY
 * @version 2019-10-31
 */
@MyBatisDao
public interface WgContractRentpriceDao extends CrudDao<WgContractRentprice> {
	
	//根据主键删除数据，更新DR=1
	@Update("update wg_contract_rentprice set dr=1,ts=sysdate where pk_contract_rentprice=#{pk_contract_rentprice}")
	Integer deleteDataNewByPk(@Param("pk_contract_rentprice") String pk_contract_rentprice);
	//根据表头主键删除数据，更新DR=1
	@Update("update wg_contract_rentprice set dr=1,ts=sysdate where pk_contract=#{pk_contract}")
	Integer deleteDataNewByFk(@Param("pk_contract") String pk_contract);
	
}