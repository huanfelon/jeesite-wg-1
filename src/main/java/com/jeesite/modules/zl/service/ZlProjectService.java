/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.base.dao.CommonBaseDao;
import com.jeesite.modules.zl.dao.ZlProjectDao;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * zl_projectService
 * @author GuoJ
 * @version 2019-07-19
 */
@Service
@Transactional(readOnly=true)
public class ZlProjectService extends TreeService<ZlProjectDao, ZlProject> {

	@Autowired
	private ZlFamilyfileService zlFamilyfileService;
	@Autowired
	private ZlBuildingfileService zlBuildingfileService;
	@Autowired
	private ZlHousesourceService zlHousesourceService;
	@Autowired
	private ZlProjectDao  zlProjectDao;
	@Autowired
	private CommonBaseDao commonBaseDao;
	/**
	 * 获取单条数据
	 * @param zlProject
	 * @return
	 */
	@Override
	public ZlProject get(ZlProject zlProject) {
		return super.get(zlProject);
	}
	
	/**
	 * 查询列表数据
	 * @param zlProject
	 * @return
	 */
	@Override
	public List<ZlProject> findList(ZlProject zlProject) {
		return super.findList(zlProject);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zlProject
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ZlProject zlProject) {
		if (zlProject.getIsNewRecord()) {
			zlProject.setDbilldate(new Timestamp((new Date()).getTime()));
		}
		ZlProject zl1 =new ZlProject();
		//判断是否有上级，项目节点点击修改时上级为null,回写带入Pk查询上级则为0.判断不为空和不为0则是有上级项目时修改
		if(StringUtils.isNotBlank(zlProject.getParentCode())&&!zlProject.getParentCode().equals("0")){ 
			ZlProject pro=this.get(zlProject.getParentCode());
			zl1.setParentCode(zlProject.getParentCode());
			List<ZlProject> zl = this.findList(zl1);
			for(ZlProject co :zl){
				 if(zlProject.getCode().equals(co.getCode())&&StringUtils.isBlank(zlProject.getPkProject())){
					throw new ValidationException("编码不符合唯一性!");
				}else if (StringUtils.isNotBlank(zlProject.getPkProject())) {//如果是修改
					if (co.getCode().equals(zlProject.getCode())&&!co.getPkProject().equals(zlProject.getPkProject())) {
			    		 throw new ValidationException("编码不符合唯一性!");
					}
				}
			}
			if(zlProject.getCode().equals(pro.getCode())){
				throw new ValidationException("编码规则应为上级编码和两位字符的组合!");
			}
		}else if(StringUtils.isNotBlank(zlProject.getPkProject())){//没上级时修改
			ZlProject zlProject2 =new ZlProject();
			List<ZlProject> zl = this.findList(zlProject2);
			for(ZlProject co :zl){
				//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
				if (co.getCode().equals(zlProject.getCode())&&!co.getPkProject().equals(zlProject.getPkProject())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
		}else{
			List<ZlProject> zlf=this.findList(zl1);
			for(ZlProject f1:zlf){
				if(zlProject.getCode().equals(f1.getCode())&& StringUtils.isBlank(zlProject.getPkProject())){
					throw new ValidationException("编码不符合唯一性!");
				}
			}
		}
		//更新房源名称
		if(!getStgObj(zlProject.getPkProject()).equals("")&&!getStgObj(zlProject.getName()).equals("")){
			String sql="update zl_housesource set estatename='"+zlProject.getName()+"'||substr(estatename,instr(estatename,'-')) where projectname='"+zlProject.getPkProject()+"'";
			commonBaseDao.updateSql(sql);
		}
		super.save(zlProject);
	}
	/**
	 * 保存数据（回写更新）
	 * @param zlProject
	 */
	@Transactional(readOnly=false)
	public void save1(ZlProject zlProject) {
		super.save(zlProject);
	}
	/**
	 * 更新状态
	 * @param zlProject
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZlProject zlProject) {
		super.updateStatus(zlProject);
	}
	
	/**
	 * 删除数据
	 * @param zlProject
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZlProject zlProject) {
		
		ZlProject zl=new ZlProject();
		zl.setParentCode(zlProject.getPkProject());
		ZlProject zlCostproject2 = this.getLastByParentCode(zl);
		if (zlCostproject2 !=null) {
				throw new ValidationException("当前项目信息存在下级，不可删除!请检查");
		}
		     //查询户型
			 String sql="select count(*) from zl_familyfile z where z.dr=0 and z.pk_projectid='"+zlProject.getPkProject()+"'";
			  Integer count= commonBaseDao.selectCount(sql);
			if (count>0) {
				 throw new ValidationException("当前项目信息已被户型引用，不可删除!请检查");
			}
			   //查询楼栋
			 String sql1="select count(*) from zl_buildingfile z where z.dr=0 and z.pk_projectid='"+zlProject.getPkProject()+"'";
			  Integer count1= commonBaseDao.selectCount(sql1);
			 if (count1>0) {
				 throw new ValidationException("当前项目信息已被楼栋引用，不可删除!请检查");
			 }
			   //查询客户信息表体
			 String sql2="select count(*) from bd_customer_proj z where z.pk_project='"+zlProject.getPkProject()+"'";
			  Integer count2= commonBaseDao.selectCount(sql2);
			 if (count2>0) {
				 throw new ValidationException("当前项目信息已被客户信息项目信息页签引用，不可删除!请检查");
			 }
		 zlProjectDao.deleteData(zlProject.getPkProject());
		//super.delete(zlProject);
	}
	
	public String getStgObj(Object obj){
		return obj==null?"":obj.toString();
	}
	
}