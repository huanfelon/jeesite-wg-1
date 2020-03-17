/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.zl.dao.ZlFamilyfileDao;
import com.jeesite.modules.zl.entity.ZlFamilyfile;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * zl_familyfileService
 * @author GuoJ
 * @version 2019-07-19
 */
@Service
@Transactional(readOnly=true)
public class ZlFamilyfileService extends CrudService<ZlFamilyfileDao, ZlFamilyfile> {

	@Autowired
	private ZlHousesourceService zlHousesourceService;
	@Autowired
	private ZlFamilyfileDao zlFamilyfileDao;
	@Autowired
	private ZlProjectService zlProjectService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取单条数据
	 * @param zlFamilyfile
	 * @return
	 */
	@Override
	public ZlFamilyfile get(ZlFamilyfile zlFamilyfile) {
		return super.get(zlFamilyfile);
	}
	
	/**
	 * 查询分页数据
	 * @param zlFamilyfile 查询条件
	 * @param zlFamilyfile.page 分页对象
	 * @return
	 */
	@Override
	public Page<ZlFamilyfile> findPage(ZlFamilyfile zlFamilyfile) {
		//户型档案查询：项目多选  QueryType.IN 支持List、Object 
		if (zlFamilyfile.getPkProjectid()!=null) {
			 String  str = zlFamilyfile.getPkProjectid().getId();
			 if (StringUtils.isNotBlank(str)) {
					String [] strings =str.split(",");
					zlFamilyfile.setPkProjectid(null);
					zlFamilyfile.getSqlMap().getWhere().and("pk_projectid", QueryType.IN, strings);
			}
		}
//		if (zlFamilyfile.getPkFormattypeid()!=null) {
//		 String  str1 = zlFamilyfile.getPkFormattypeid().getId();
//		 if (StringUtils.isNotBlank(str1)) {
//				String [] strings =str1.split(",");
//				zlFamilyfile.setPkFormattypeid(null);
//				zlFamilyfile.getSqlMap().getWhere().and("pk_formattypeid", QueryType.IN, strings);
//		}
//		}
		return super.findPage(zlFamilyfile);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zlFamilyfile
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ZlFamilyfile zlFamilyfile) {
		ZlProject zlProject =zlProjectService.get(zlFamilyfile.getPkProjectid().getPkProject());
		if (zlFamilyfile.getIsNewRecord()) {
			zlFamilyfile.setDbilldate(new Timestamp((new Date()).getTime()));
		}
		zlFamilyfile.setCode(createCode(zlFamilyfile.getPkOrg().getOfficeCode(),zlProject.getCode(),zlFamilyfile.getCode()));
		ZlFamilyfile zlFeescale2=new ZlFamilyfile();
	     List<ZlFamilyfile> zlList=this.findList(zlFeescale2);
	     for(ZlFamilyfile zl:zlList){
	    	 if (zl.getCode().equals(zlFamilyfile.getCode())&& StringUtils.isBlank(zlFamilyfile.getPkFamilyfile())) {
	    		 throw new ValidationException("编码不符合唯一性!");
			}else if (StringUtils.isNotBlank(zlFamilyfile.getPkFamilyfile())) {//如果是修改
				if (zl.getCode().equals(zlFamilyfile.getCode())&&!zl.getPkFamilyfile().equals(zlFamilyfile.getPkFamilyfile())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
	     }
	   zlFamilyfile.setInnerarea(zlFamilyfile.getBuiltuparea());
		super.save(zlFamilyfile);
	}
	/**
	 * 保存数据（插入或更新）
	 * @param zlFamilyfile
	 */
	public String save1(ZlFamilyfile zlFamilyfile) {
		ZlProject zlProject =zlProjectService.get(zlFamilyfile.getPkProjectid().getPkProject());
		if (zlFamilyfile.getIsNewRecord()) {
			zlFamilyfile.setDbilldate(new Timestamp((new Date()).getTime()));
		}
		zlFamilyfile.setCode(createCode(zlFamilyfile.getPkOrg().getOfficeCode(),zlProject.getCode(),zlFamilyfile.getCode()));
		ZlFamilyfile zlFeescale2=new ZlFamilyfile();
	     List<ZlFamilyfile> zlList=this.findList(zlFeescale2);
	     for(ZlFamilyfile zl:zlList){
	    	 if (zl.getCode().equals(zlFamilyfile.getCode())&& StringUtils.isBlank(zlFamilyfile.getPkFamilyfile())) {
	    		 throw new ValidationException("编码不符合唯一性!");
			}else if (StringUtils.isNotBlank(zlFamilyfile.getPkFamilyfile())) {//如果是修改
				if (zl.getCode().equals(zlFamilyfile.getCode())&&!zl.getPkFamilyfile().equals(zlFamilyfile.getPkFamilyfile())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
	     }
	   zlFamilyfile.setInnerarea(zlFamilyfile.getBuiltuparea());
		super.save(zlFamilyfile);
		return zlFamilyfile.getCode();
	}
	/**
	 * 更新状态
	 * @param zlFamilyfile
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZlFamilyfile zlFamilyfile) {
		super.updateStatus(zlFamilyfile);
	}
	
	/**
	 * 删除数据
	 * @param zlFamilyfile
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZlFamilyfile zlFamilyfile) {
		   //查询房源
		   String sql="select count(*) from zl_housesource z where z.dr=0 and z.pk_familyfile='"+zlFamilyfile.getPkFamilyfile()+"'";
		   Integer count= commonService.selectCount(sql);
	    	if (count>0) {
			 throw new ValidationException("当前户型档案已被房源引用，不可删除!请检查");
		    }
		 zlFamilyfileDao.deleteData(zlFamilyfile.getPkFamilyfile());
		//super.delete(zlFamilyfile);
	}
	/**
	 * 生成户型编码
	 */

	public String createCode(String pkOrg,String projectCode,String familyCode){
		 List<ZlFamilyfile> list =zlFamilyfileDao.getMaxCode();
			List<Integer> strings=new ArrayList<Integer>();
		 if (StringUtils.isBlank(familyCode)) {
				if(list.size()>0){
					for(ZlFamilyfile zlFamilyfile:list){

						Integer length=getStgObj(zlFamilyfile.getCode()).length();
						String str=zlFamilyfile.getCode().substring(1,length-8);
						//判断户型编码中有没有项目编码拼成的编码
						if (str.indexOf(projectCode)!=-1) {
							//将项目编码相同的截取出来
						    Integer zlInteger =new Integer(zlFamilyfile.getCode().substring(length-8,length)) ;
							strings.add(zlInteger);
						}
					}
					if (strings.size()>0) {
						//找出编码最大
						Integer max = Integer.MIN_VALUE;
				        for (int i = 0; i < strings.size(); i++) {
				            if (strings.get(i) > max)
				                max = strings.get(i);
				        }
				        //拼接
				    	String zero="";
						for(int i=0;i<8-max.toString().length();i++){
							zero+="0";
						}
							return pkOrg+projectCode+zero+(max+1);
					}else {
						return pkOrg+projectCode+"00000001";
					}
				}else {
					return pkOrg+projectCode+"00000001";
				}
		}else {
				if(list.size()>0){
					for(ZlFamilyfile zlFamilyfile:list){

						Integer length=getStgObj(zlFamilyfile.getCode()).length();
						String str=zlFamilyfile.getCode().substring(1,length-8);
						//判断户型编码中有没有项目编码拼成的编码
						if (str.indexOf(projectCode)!=-1) {
							//将项目编码相同的截取出来
						    Integer zlInteger =new Integer(zlFamilyfile.getCode().substring(length-8,length)) ;
							strings.add(zlInteger);
						}
					}
					if (strings.size()>0) {
						//找出编码最大
						Integer max = Integer.MIN_VALUE;
				        for (int i = 0; i < strings.size(); i++) {
				            if (strings.get(i) > max)
				                max = strings.get(i);
				        }
				        //拼接
				    	String zero="";
						for(int i=0;i<8-max.toString().length();i++){
							zero+="0";
						}
               //截取chu
						Integer length=getStgObj(familyCode).length();
						String str=familyCode.substring(1,length-8);
						

						Integer length1=getStgObj(familyCode).length();
						 Integer zlInteger =new Integer(familyCode.substring(length1-8,length1)) ;
						if (str.equals(projectCode)) {
							return pkOrg+projectCode+zero+zlInteger;
						}else{
							return pkOrg+projectCode+zero+(max+1);
						}
					}else {
						return pkOrg+projectCode+"00000001";
					}
				}else {
					return pkOrg+projectCode+"00000001";
				}
		}
	}
	public String getStgObj(Object obj){
		return obj==null?"":obj.toString();
	}
}