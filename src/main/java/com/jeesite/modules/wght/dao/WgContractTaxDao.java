/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.wght.entity.WgContractTax;

/**
 * 合同税率变更DAO接口
 * @author LY
 * @version 2019-12-17
 */
@MyBatisDao
public interface WgContractTaxDao extends CrudDao<WgContractTax> {
	
	@Select("${sql}")
	List<WgContractTax> fingListBySql(@Param("sql") String sql);
	
}