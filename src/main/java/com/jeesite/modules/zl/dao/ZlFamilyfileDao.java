/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.zl.entity.ZlFamilyfile;

/**
 * zl_familyfileDAO接口
 * @author GuoJ
 * @version 2019-07-19
 */
@MyBatisDao
public interface ZlFamilyfileDao extends CrudDao<ZlFamilyfile> {
    //获取前台可见所有
	@Select("select * from zl_familyfile where nvl(dr,0)=0")
	List<ZlFamilyfile> getMaxCode();
	
	 //获取前台可见最大户型编码
	@Select("select * from zl_familyfile where nvl(dr,0)=0 and pk_projectid=#{pk_projectid} and name=#{name}")
	List<ZlFamilyfile> getFamilyByPk(@Param("pk_projectid") String pk_projectid,@Param("name") String name);
	
	//根据主键删除数据
	@Update("update zl_familyfile set dr=1 where pk_familyfile=#{pk_familyfile}")
	void deleteData(@Param("pk_familyfile") String pk_familyfile);
}