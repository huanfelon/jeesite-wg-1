/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.wght.entity.WgContract;

/**
 * 合同管理DAO接口
 * @author LY
 * @version 2019-10-31
 */
@MyBatisDao
public interface WgContractDao extends CrudDao<WgContract> {
	
	//查询最大单据号(所有业务节点)
	@Select("select max(${itemName}) from ${tableName} where nvl(dr,0)=0 and ${billtypeColumn}=#{billtype}")
	String getMaxBillCode(@Param("tableName") String tableName,@Param("itemName") String itemName,@Param("billtypeColumn") String billtypeColumn,@Param("billtype") String billtype);
	//查询个数
	@Select("${sqlcount}")
	Integer findCountBySql(@Param("sqlcount") String sqlcount);
	//删除数据，更新DR=1
	@Update("update wg_contract set dr=1,ts=sysdate where pk_contract=#{pk_contract}")
	Integer deleteDataNewByPk(@Param("pk_contract") String pk_contract);
	//根据PK查询是否有下游合同修订
	@Select("select count(*) from wg_contract where nvl(dr,0)=0 and billtype='ctxd' and vsrcid=#{vsrcid}")
	Integer checkExistXDByVsrcid(@Param("vsrcid") String vsrcid);
	//根据PK查询合同版本记录
	@Select("select * from wg_contract where nvl(dr,0)=0 and vsrcid=#{vsrcid} order by version")
	List<WgContract> findListXDByVsrcid(@Param("vsrcid") String vsrcid);
	//根据PK查询合同最大版本号
	@Select("select nvl(max(version),0) from wg_contract where nvl(dr,0)=0 and billtype='ct' and vsrcid=#{vsrcid}")
	Integer findMaxVersionByVsrcid(@Param("vsrcid") String vsrcid);
	//删除
	@Delete("${sql}")
	Integer deleteSql(@Param("sql") String sql);
	
}