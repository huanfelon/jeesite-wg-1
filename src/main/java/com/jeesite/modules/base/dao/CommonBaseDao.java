/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jeesite.common.mybatis.annotation.MyBatisDao;

/**
 * 客户信息中心DAO接口
 * @author tcl
 * @version 2019-11-05
 */
@MyBatisDao
public interface CommonBaseDao {
	
	//查询数量结果集
	@Select("${sql}")
	Integer selectCount(@Param("sql") String sql);
	
	//查询单行单列结果集
	@Select("${sql}")
	Object selectObject(@Param("sql") String sql);
	
	//查询单行多列
	@Select("${sql}")
	Map<String, Object> selectMapResult(@Param("sql") String sql);
	
	//查询多行多列
	@Select("${sql}")
	List<Map<String, Object>> selectMapListResult(@Param("sql") String sql);
	
	//更新数据库,删除数据库
	@Update("${sql}")
	Integer updateSql(@Param("sql") String sql);
	
	//直接数据库删除数据
	@Delete("${sql}")
	Integer deleteSql(@Param("sql") String sql);
	
}