/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * zl_housesourceDAO接口
 * @author GuoJ
 * @version 2019-07-19
 */
@MyBatisDao
public interface ZlHousesourceDao extends CrudDao<ZlHousesource> {

	//根据房源编码查询数据
	@Select("select * from zl_housesource where nvl(dr,0)=0 and estatecode=#{estatecode}")
	ZlHousesource getZlHousesourceByCode(@Param("estatecode") String estatecode);
	//根据主键删除数据
	@Update("update zl_housesource set dr=1 where pk_housesource=#{pk_housesource}")
	void deleteData(@Param("pk_housesource") String pk_housesource);
	//根据主实体房号查询版本记录
	@Select("select * from zl_housesource h where h.zstroom=#{zstroom}")
	ZlHousesource getHouseBody(@Param("zstroom") String zstroom);
	//合并时根据主实体房号查询相同项目 相同楼栋合并拆分状态最小值
	@Select("select min(hbcfstatus) from zl_housesource where nvl(dr,0)=0 and projectname=#{projectname} and buildname=#{buildname} and zstpk=#{zstpk}")
	Integer getMinHbcfstatusByZstpk(@Param("projectname") String projectname,@Param("buildname") String buildname,@Param("zstpk") String zstpk);
	//根据主实体主键查询数据
	@Select("select * from zl_housesource where pk_housesource=#{pk_housesource}")
	ZlHousesource getZstPkByzl(@Param("pk_housesource") String pk_housesource);
	//根据房源编码查询重复
	@Select("select count(*) from zl_housesource z where nvl(z.dr,0)=0 and z.estatecode=#{estatecode} and  z.pk_housesource!=#{pk_housesource}")
	Integer getHouseCountByCode(@Param("estatecode") String estatecode,@Param("pk_housesource") String pk_housesource);
}