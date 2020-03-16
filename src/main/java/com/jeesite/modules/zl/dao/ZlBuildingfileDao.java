/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.zl.entity.ZlBuildingfile;

/**
 * zl_buildingfileDAO接口
 * @author GuoJ
 * @version 2019-07-19
 */
@MyBatisDao
public interface ZlBuildingfileDao extends CrudDao<ZlBuildingfile> {
    //获取前台可见最大楼栋编码
	@Select("select max(code) from zl_buildingfile where nvl(dr,0)=0 and pk_org=#{pkOrg}")
	String getMaxCode(@Param("pkOrg") String pkOrg);
	
    //根据项目pk查找项目所在楼栋
	@Select("select * from zl_buildingfile where nvl(dr,0)=0 and pk_projectid=#{pk_projectid} and name=#{name}")
	List<ZlBuildingfile> getBuildByPk(@Param("pk_projectid") String pk_projectid,@Param("name") String name);

	//根据主键删除数据
	@Update("update zl_buildingfile set dr=1 where pk_buildingfile=#{pk_buildingfile}")
	void deleteData(@Param("pk_buildingfile") String pk_buildingfile);
	
	//获取最新房产数量和面积
		@Select("select count(*) as count, sum(z.BUILDAREA) as sumb,sum(z.INNERAREA) as sumi from zl_housesource  z where nvl(z.dr,0)=0 and z.PK_ORG=#{pk_org} and z.PROJECTNAME=#{projectname} and z.BUILDNAME=#{buildname} ")
		Map<String, Integer> getZxCountFy(@Param("pk_org") String  pk_org,@Param("projectname") String projectname,@Param("buildname") String buildname );
		
		//获取最新房产数量和面积
		@Select("select count(*) as count, sum(z.BUILDAREA) as sumb,sum(z.INNERAREA) as sumi from zl_housesource  z where nvl(z.dr,0)=0 and z.PK_ORG=#{pk_org} and z.PROJECTNAME=#{projectname}")
		Map<String, Integer> getZxCountFy2(@Param("pk_org") String  pk_org,@Param("projectname") String projectname);
}