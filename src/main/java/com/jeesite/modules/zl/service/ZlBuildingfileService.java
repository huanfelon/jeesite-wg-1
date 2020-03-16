/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.base.dao.CommonBaseDao;
import com.jeesite.modules.zl.dao.ZlBuildingfileDao;
import com.jeesite.modules.zl.dao.ZlHousesourceDao;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * zl_buildingfileService
 * @author GuoJ
 * @version 2019-07-19
 */
@Service
@Transactional(readOnly=true)
public class ZlBuildingfileService extends CrudService<ZlBuildingfileDao, ZlBuildingfile> {

	@Autowired
	private ZlHousesourceService zlHousesourceService;
	@Autowired
	private ZlBuildingfileDao zlBuildingfileDao;
	
	@Autowired
	private ZlProjectService zlProjectService;
	@Autowired
	private ZlHousesourceDao zlHousesourceDao;
	@Autowired
	private CommonBaseDao commonBaseDao;
	/**
	 * 获取单条数据
	 * @param zlBuildingfile
	 * @return
	 */
	@Override
	public ZlBuildingfile get(ZlBuildingfile zlBuildingfile) {
		return super.get(zlBuildingfile);
	}
	
	/**
	 * 查询分页数据
	 * @param zlBuildingfile 查询条件
	 * @param zlBuildingfile.page 分页对象
	 * @return
	 */
	@Override
	public Page<ZlBuildingfile> findPage(ZlBuildingfile zlBuildingfile) {
		//楼栋查询：项目多选  QueryType.IN 支持List、Object 
		 String  str = zlBuildingfile.getPkProjectid().getId();
		 if (StringUtils.isNotBlank(str)) {
				String [] strings =str.split(",");
				zlBuildingfile.setPkProjectid(null);
				zlBuildingfile.getSqlMap().getWhere().and("pk_projectid", QueryType.IN, strings);
		}
		return super.findPage(zlBuildingfile);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zlBuildingfile
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ZlBuildingfile zlBuildingfile) {
		ZlProject zlProject =zlProjectService.get(zlBuildingfile.getPkProjectid().getId());
		if (zlBuildingfile.getIsNewRecord()) {
			zlBuildingfile.setIsbuild("0");
			zlBuildingfile.setDbilldate(new Timestamp((new Date()).getTime()));
			zlBuildingfile.setCode(createCode(zlBuildingfile.getPkOrg().getOfficeCode(),zlProject.getCode(),zlBuildingfile.getName()));
			zlBuildingfile.setInnerarea(zlBuildingfile.getBuiltuparea());
		}
		zlBuildingfile.setCode(createCode(zlBuildingfile.getPkOrg().getOfficeCode(),zlProject.getCode(),zlBuildingfile.getName()));
		ZlBuildingfile zlFeescale2=new ZlBuildingfile();
	     List<ZlBuildingfile> zlList=this.findList(zlFeescale2);
	     for(ZlBuildingfile zl:zlList){
	     if (zl.getCode().equals(zlBuildingfile.getCode())&& StringUtils.isBlank(zlBuildingfile.getPkBuildingfile())) {
	    		 throw new ValidationException("编码不符合唯一性!");
			}else if (StringUtils.isNotBlank(zlBuildingfile.getPkBuildingfile())) {//如果是修改
				if (zl.getCode().equals(zlBuildingfile.getCode())&&!zl.getPkBuildingfile().equals(zlBuildingfile.getPkBuildingfile())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
	     }
		super.save(zlBuildingfile);
	}
	/**
	 * 保存数据（回写更新）
	 * @param zlBuildingfile
	 */
	@Transactional(readOnly=false)
	public void save1(ZlBuildingfile zlBuildingfile) {
		super.save(zlBuildingfile);
	}
	/**
	 * 保存数据（插入或更新）
	 * @param zlBuildingfile
	 */
	public String  save2(ZlBuildingfile zlBuildingfile) {
		ZlProject zlProject =zlProjectService.get(zlBuildingfile.getPkProjectid().getId());
		if (zlBuildingfile.getIsNewRecord()) {
			zlBuildingfile.setIsbuild("0");
			zlBuildingfile.setDbilldate(new Timestamp((new Date()).getTime()));
			zlBuildingfile.setCode(createCode(zlBuildingfile.getPkOrg().getOfficeCode(),zlProject.getCode(),zlBuildingfile.getName()));
			zlBuildingfile.setInnerarea(zlBuildingfile.getBuiltuparea());
		}
		zlBuildingfile.setCode(createCode(zlBuildingfile.getPkOrg().getOfficeCode(),zlProject.getCode(),zlBuildingfile.getName()));
		ZlBuildingfile zlFeescale2=new ZlBuildingfile();
		List<ZlBuildingfile> zlList=this.findList(zlFeescale2);
		for(ZlBuildingfile zl:zlList){
			if (zl.getCode().equals(zlBuildingfile.getCode())&& StringUtils.isBlank(zlBuildingfile.getPkBuildingfile())) {
				throw new ValidationException("编码不符合唯一性!");
			}else if (StringUtils.isNotBlank(zlBuildingfile.getPkBuildingfile())) {//如果是修改
				if (zl.getCode().equals(zlBuildingfile.getCode())&&!zl.getPkBuildingfile().equals(zlBuildingfile.getPkBuildingfile())) {
					throw new ValidationException("编码不符合唯一性!");
				}
			}
		}
		if (!zlBuildingfile.getIsNewRecord()) {
			//更新房源名称
			Integer length=zlBuildingfile.getCode().length()+1;
			String sql="update zl_housesource set estatecode='"+zlBuildingfile.getCode()+"'||substr(estatecode,"+length+"),"
					+ "estatename=substr(estatename,0,instr(estatename,'-'))||'"+zlBuildingfile.getName()+"'||substr(estatename,instr(estatename,'-',1,2)) "
					+ "where buildname='"+zlBuildingfile.getPkBuildingfile()+"'";
			commonBaseDao.updateSql(sql);
			super.save(zlBuildingfile);
		}else {
			super.save(zlBuildingfile);
		}
		return zlBuildingfile.getCode();
	}
	/**
	 * 更新状态
	 * @param zlBuildingfile
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZlBuildingfile zlBuildingfile) {
		super.updateStatus(zlBuildingfile);
	}
	
	/**
	 * 删除数据
	 * @param zlBuildingfile
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZlBuildingfile zlBuildingfile) {
		   //查询房源
		   String sql="select count(*) from zl_housesource z where z.dr=0 and z.buildname='"+zlBuildingfile.getPkBuildingfile()+"'";
		   Integer count= commonBaseDao.selectCount(sql);
	    	if (count>0) {
			 throw new ValidationException("当前楼栋档案已被房源引用，不可删除!请检查");
		    }
    	   //查询合同管理房产页签
		   String sql1="select count(*) from wg_contract_house z  where z.dr=0 and z.pk_build='"+zlBuildingfile.getPkBuildingfile()+"'";
		   Integer count1= commonBaseDao.selectCount(sql1);
	    	if (count1>0) {
			 throw new ValidationException("当前楼栋档案已被合同管理房产页签引用，不可删除!请检查");
		    }
		 zlBuildingfileDao.deleteData(zlBuildingfile.getPkBuildingfile());
		//super.delete(zlBuildingfile);
	}

	/**
	 * 过滤查询数据
	 */
	public void queryFilter(ZlBuildingfile entity) {
		if(entity.getPkProjectid()!=null&&entity.getPkProjectid().getId()!=null
				&&!entity.getPkProjectid().getId().equals("")){
			String[] pkpro=entity.getPkProjectid().getId().split(",");
			if(pkpro!=null&&pkpro.length>1){
				entity.setPkProjectid(null);
				String pks="";
				for (int i=0;i<pkpro.length;i++) {
					pks+="'"+pkpro[i]+"'";
					if(i<pkpro.length-1){
						pks+=",";
					}
				}
				entity.getSqlMap().getDataScope().addFilter("extquery", "a.pk_projectid in ("+pks+")");
			}
		}
	}
	/**
	 * 生成楼栋编码
	 */

	public String createCode(String pkOrg,String project,String name){
		//项目编码+楼栋名称
		return pkOrg+project+name;
	    //楼栋编码拼接自增
		/*String maxcode=zlBuildingfileDao.getMaxCode(pkOrg);
		if(getStgObj(maxcode).equals("")){
			return pkOrg+"ZL00000001";
		}
		Integer length=getStgObj(maxcode).length();
		Integer num=Integer.parseInt(getStgObj(maxcode).substring(length-8,length))+1;
		String zero="";
		for(int i=0;i<8-num.toString().length();i++){
			zero+="0";
		}
		return pkOrg+"ZL"+zero+num;*/
	}
	public String getStgObj(Object obj){
		return obj==null?"":obj.toString();
	}
	/**
	 * 查询楼栋下最新房源个数
	 */
	public Map<String, Integer>  getZxCountFy(String pk_org, String projectname,String buildname){
		
		Map<String, Integer> count=zlBuildingfileDao.getZxCountFy(pk_org,projectname,buildname);
		return count;
	}
	
	/**
	 * 查询项目下最新房源个数
	 */
	public Map<String, Integer>  getZxCountFy2(String pk_org, String projectname){
		
		Map<String, Integer> count=zlBuildingfileDao.getZxCountFy2(pk_org,projectname);
		return count;
	}
}