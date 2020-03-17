/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.dao;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ct.entity.CtInvoiceapplySkmx;

/**
 * 开票申请DAO接口
 * @author tcl
 * @version 2019-11-11
 */
@MyBatisDao
public interface CtInvoiceapplySkmxDao extends CrudDao<CtInvoiceapplySkmx> {
	//根据应收单主键查询数据(取消审批时有没有被开票申请参照)
		@Select("select * from ct_invoiceapply_skmx where nvl(dr,0)=0 and vsrcid=#{pks} ")
		List<CtInvoiceapplySkmx> getCtInvoiceapplySkmxByCode(@Param("pks") String pks);
}