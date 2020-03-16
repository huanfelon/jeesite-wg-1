/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * zl_houseTableDAO接口
 * @author LY
 * @version 2019-07-19
 */
@MyBatisDao
public interface ZlHouseTableDao extends CrudDao<ZlHousesource> {

	//查询房源表数据
	@Select("select a.pk_housesource,a.unit,a.floorn,a.roomnumber,a.estatename,a.buildarea,(select cf.pk_customer from zl_customer_fcxx cf where nvl(cf.dr,0)=0 and cf.pk_house=a.pk_housesource) customer,"
			+ "(case when replace(wm_concat(to_char(a.zt)),',','') is null or instr(replace(wm_concat(to_char(a.zt)),',',''),'N')>0 then 'N' else 'Y' end) jfzt "
			+ "from (select h.pk_housesource,h.unit,h.floorn,h.roomnumber,h.estatename,h.buildarea,'' zt from zl_housesource h "
			+ "where nvl(h.status,0)=0 and h.pk_org=#{pkOrg} and h.projectname=#{pkPro} and h.buildname=#{pkBuild} "
			+ "union all "
			+ "select h.pk_housesource,h.unit,h.floorn,h.roomnumber,h.estatename,h.buildarea,(case when zz.nrealmny!=0 and zz.nrealmny is not null then 'Y' else 'N' end) "
			+ "from zl_zyzd_zjmx zz left join zl_zyzd z on z.pk_zyzd=zz.pk_zyzd left join zl_housesource h on h.pk_housesource=z.pk_house "
			+ "where nvl(zz.dr,0)=0 and nvl(z.dr,0)=0 and nvl(h.dr,0)=0 and h.pk_org=#{pkOrg} and h.projectname=#{pkPro} and h.buildname=#{pkBuild} "
			+ "and TO_DATE(to_char(zz.dstartdate,'yyyy-mm-dd'),'yyyy-mm-dd') <= TO_DATE(#{busidate}, 'yyyy-mm-dd')) a "
			+ "group by a.pk_housesource,a.unit,a.floorn,a.roomnumber,a.estatename,a.buildarea "
			+ "order by a.floorn DESC,a.unit ASC,a.roomnumber ASC")
	List<Map<String,Object>> findTableData(@Param("pkOrg") String pkOrg,@Param("pkPro") String pkPro,@Param("pkBuild") String pkBuild,@Param("busidate") String busidate);
}