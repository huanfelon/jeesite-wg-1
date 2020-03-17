/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.entity.BdFormattype;
import com.jeesite.modules.bd.service.BdFormattypeService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.zl.dao.ZlBuildingfileDao;
import com.jeesite.modules.zl.dao.ZlFamilyfileDao;
import com.jeesite.modules.zl.dao.ZlHousesourceDao;
import com.jeesite.modules.zl.entity.CurrectMergeHouseVO;
import com.jeesite.modules.zl.entity.SplitHouseVO;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlFamilyfile;
import com.jeesite.modules.zl.entity.ZlHousesource;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * zl_housesourceService
 * @author GuoJ
 * @version 2019-07-19
 */
@Service
@Transactional(readOnly=true)
public class ZlHousesourceService extends CrudService<ZlHousesourceDao, ZlHousesource> {

	@Autowired
	private ZlHousesourceDao zlHousesourceDao;

	@Autowired
	private ZlHousesourceService zlHousesourceService;

	@Autowired
	private ZlBuildingfileService zlBuildingfileService;
	@Autowired
	private ZlProjectService zlProjectService;
	@Autowired
	private ZlFamilyfileService zlFamilyfileService;
	
	@Autowired
	private ZlFamilyfileDao zlFamilyfileDao;
	@Autowired
	private ZlBuildingfileDao zlBuildingfileDao;
	@Autowired
	private BdFormattypeService  zlFormattypeService;
	@Autowired
	private CommonBaseService commonService;
	
	/**
	 * 获取单条数据
	 * @param zlHousesource
	 * @return
	 */
	@Override
	public ZlHousesource get(ZlHousesource zlHousesource) {
		return super.get(zlHousesource);
	}
	
	/**
	 * 查询分页数据
	 * @param zlHousesource 查询条件
	 * @param zlHousesource.page 分页对象
	 * @return
	 */
	@Override
	public Page<ZlHousesource> findPage(ZlHousesource zlHousesource) {
			//房源查询：项目、楼栋多选  QueryType.IN 支持List、Object 
		if(zlHousesource.getProjectname()!=null){
			 String  str = zlHousesource.getProjectname().getId();
			 if (StringUtils.isNotBlank(str)) {
					String [] strings =str.split(",");
					zlHousesource.setProjectname(null);
					zlHousesource.getSqlMap().getWhere().and("projectname", QueryType.IN, strings);
			}
		}
	   if (zlHousesource.getBuildname()!=null) {
		   String str1 =zlHousesource.getBuildname().getId();
			 if (StringUtils.isNotBlank(str1)) {
					String [] strings1 =str1.split(",");
					zlHousesource.setBuildname(null);
					zlHousesource.getSqlMap().getWhere().and("buildname", QueryType.IN, strings1);
			}
	 }
	   if (zlHousesource.getPkFamilyfile()!=null) {
		   String str2 =zlHousesource.getPkFamilyfile().getId();
			 if (StringUtils.isNotBlank(str2)) {
					String [] strings2 =str2.split(",");
					zlHousesource.setPkFamilyfile(null);
					zlHousesource.getSqlMap().getWhere().and("pk_familyfile", QueryType.IN, strings2);
			}
	}
	   if (zlHousesource.getPkFormattype()!=null) {
		   String str3 =zlHousesource.getPkFormattype().getId();
			 if (StringUtils.isNotBlank(str3)) {
					String [] strings3 =str3.split(",");
					zlHousesource.setPkFormattype(null);
					zlHousesource.getSqlMap().getWhere().and("pk_formattype", QueryType.IN, strings3);
			}
	}
		return super.findPage(zlHousesource);
	}
	
	/**
	 * 保存数据（未用系统保存）
	 * @param zlHousesource
	 */
	@Override
	@Transactional(readOnly=false)
	public  void save(ZlHousesource zlHousesource) {
			//新增和修改更新编码和名称
			zlHousesource.setEstatecode(createCode(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
			zlHousesource.setEstatename(createName(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getName(),zlHousesource.getBuildname().getName(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
		if (zlHousesource.getIsNewRecord()) {
			zlHousesource.setCreator(UserUtils.getUser());
			zlHousesource.setCreationtime(new Timestamp((new Date()).getTime()));
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
			zlHousesource.setZstroom(zlHousesource.getRoomnumber());
			
		     //产证面积默认为租赁面积
		     zlHousesource.setInnerarea(zlHousesource.getBuildarea());
		}else {
			zlHousesource.setModifier(UserUtils.getUser());
			zlHousesource.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		ZlHousesource zlFeescale2=new ZlHousesource();
	     List<ZlHousesource> zlList=this.findList(zlFeescale2);
	     for(ZlHousesource zl:zlList){
	    	 if(StringUtils.isBlank(zlHousesource.getPkHousesource())){
	    		 if (zl.getEstatecode().equals(zlHousesource.getEstatecode())&& StringUtils.isBlank(zlHousesource.getPkHousesource())) {
		 				 throw new ValidationException("编码不符合唯一性!");
		    		//同一楼栋下同一单元房号不能相同
				} else if(zl.getBuildname().getPkBuildingfile().equals(zlHousesource.getBuildname().getPkBuildingfile())&&zl.getUnit().equals(zlHousesource.getUnit())&&zl.getRoomnumber().equals(zlHousesource.getRoomnumber())){
	 				throw new ValidationException("同一单元下房间号不能重复，请重新填写房号或重新选择楼栋!");
	 			}
	    	 }else if (StringUtils.isNotBlank(zlHousesource.getPkHousesource())) {//如果是修改
				if (zl.getEstatecode().equals(zlHousesource.getEstatecode())&&!zl.getPkHousesource().equals(zlHousesource.getPkHousesource())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}else if(zl.getBuildname().getPkBuildingfile().equals(zlHousesource.getBuildname().getPkBuildingfile())&&zl.getUnit().equals(zlHousesource.getUnit())&&zl.getRoomnumber().equals(zlHousesource.getRoomnumber())&&!zl.getPkHousesource().equals(zlHousesource.getPkHousesource())){
					throw new ValidationException("同一单元下房间号不能重复，请重新填写房号或重新选择楼栋!");
				}
			}
	     }
	
	
		super.save(zlHousesource);
		//回写楼栋
		 ZlBuildingfile buildingfile  = zlBuildingfileService.get(zlHousesource.getBuildname().getPkBuildingfile());
		 Map<String, Integer> count=zlBuildingfileService.getZxCountFy(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile());
		 Object sc=count.get("COUNT");	
		 Object sb=count.get("SUMB");
    	 Object si=count.get("SUMI");
    	 buildingfile.setNproperty(new Integer(sc.toString()));
    	 buildingfile.setBuiltuparea(getDubObj(sb));
    	 if (buildingfile.getInnerarea()==null) {
	    	 buildingfile.setInnerarea(new Double(0));
		 }
	     buildingfile.setInnerarea(getDubObj(si));
	     zlBuildingfileService.save1(buildingfile);
	     
	     //回写项目中总面积
	     Map<String, Integer> count2= zlBuildingfileService.getZxCountFy2(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject());
	     Object sb2=count2.get("SUMB");
	     ZlProject zlProject = zlProjectService.get(zlHousesource.getProjectname().getPkProject());
	     if (zlProject.getTotalarea()==null) {
	    	 zlProject.setTotalarea(new Double(0));
		}
	     zlProject.setTotalarea(getDubObj(sb2));
	     zlProjectService.save1(zlProject);
	}
	/**
	 * 保存数据（插入或更新）在用保存方法
	 * @param zlHousesource
	 */
	public  String save1(ZlHousesource zlHousesource) {
			//新增和修改更新编码和名称
			zlHousesource.setEstatecode(createCode(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
			zlHousesource.setEstatename(createName(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getName(),zlHousesource.getBuildname().getName(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
		if (zlHousesource.getIsNewRecord()) {
			zlHousesource.setCreator(UserUtils.getUser());
			zlHousesource.setCreationtime(new Timestamp((new Date()).getTime()));
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
			zlHousesource.setZstroom(zlHousesource.getRoomnumber());
			
		     //产证面积默认为租赁面积
		     zlHousesource.setInnerarea(zlHousesource.getBuildarea());
		}else {
			zlHousesource.setModifier(UserUtils.getUser());
			zlHousesource.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		String pk=zlHousesource.getPkHousesource();
		if (StringUtils.isBlank(pk)) {
			pk="***";
		}
		Integer countc=zlHousesourceDao.getHouseCountByCode(zlHousesource.getEstatecode(),pk);
		if (countc>0) {
			return "编码不符合唯一性! 请检查单元、楼栋、房号。";
		}
/*		ZlHousesource zlFeescale2=new ZlHousesource();
	     List<ZlHousesource> zlList=this.findList(zlFeescale2);
	     for(ZlHousesource zl:zlList){
	    	 if(StringUtils.isBlank(zlHousesource.getPkHousesource())){
	    		 if (zl.getEstatecode().equals(zlHousesource.getEstatecode())&& StringUtils.isBlank(zlHousesource.getPkHousesource())) {
		 			return "编码不符合唯一性!";
		    		//同一楼栋下同一单元房号不能相同
				} else if(zl.getBuildname().getPkBuildingfile().equals(zlHousesource.getBuildname().getPkBuildingfile())&&zl.getUnit().equals(zlHousesource.getUnit())&&zl.getRoomnumber().equals(zlHousesource.getRoomnumber())){
	 				return "同一单元下房间号不能重复，请重新填写房号或重新选择楼栋!";
				}
	    	 }else if (StringUtils.isNotBlank(zlHousesource.getPkHousesource())) {//如果是修改
				if (zl.getEstatecode().equals(zlHousesource.getEstatecode())&&!zl.getPkHousesource().equals(zlHousesource.getPkHousesource())) {
		    		 return "编码不符合唯一性!";
				}else if(zl.getBuildname().getPkBuildingfile().equals(zlHousesource.getBuildname().getPkBuildingfile())&&zl.getUnit().equals(zlHousesource.getUnit())&&zl.getRoomnumber().equals(zlHousesource.getRoomnumber())&&!zl.getPkHousesource().equals(zlHousesource.getPkHousesource())){
					return "同一单元下房间号不能重复，请重新填写房号或重新选择楼栋!";
				}
			}
	     }*/
		super.save(zlHousesource);
		//回写楼栋
		 ZlBuildingfile buildingfile  = zlBuildingfileService.get(zlHousesource.getBuildname().getPkBuildingfile());
		 Map<String, Integer> count=zlBuildingfileService.getZxCountFy(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile());
		 Object sc=count.get("COUNT");	
		 Object sb=count.get("SUMB");
    	 Object si=count.get("SUMI");
    	 buildingfile.setNproperty(new Integer(sc.toString()));
    	 buildingfile.setBuiltuparea(getDubObj(sb.toString()));
    	 if (buildingfile.getInnerarea()==null) {
	    	 buildingfile.setInnerarea(new Double(0));
		 }
	     buildingfile.setInnerarea(getDubObj(si.toString()));
	     zlBuildingfileService.save1(buildingfile);
	     
	     //回写项目中总面积
	     Map<String, Integer> count2= zlBuildingfileService.getZxCountFy2(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject());
	     Object sb2=count2.get("SUMB");
	     ZlProject zlProject = zlProjectService.get(zlHousesource.getProjectname().getPkProject());
	     if (zlProject.getTotalarea()==null) {
	    	 zlProject.setTotalarea(new Double(0));
		}
	     zlProject.setTotalarea(getDubObj(sb2.toString()));
	     zlProjectService.save1(zlProject);
	     return zlHousesource.getEstatecode();
	}
	/**
	 * 导入保存数据
	 * @param zlHousesource
	 */
	@Transactional(readOnly=false)
	public  String saveD(ZlHousesource zlHousesource) {
			zlHousesource.setCreator(UserUtils.getUser());
			zlHousesource.setCreationtime(new Timestamp((new Date()).getTime()));
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
			zlHousesource.setEstatecode(createCode(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
			zlHousesource.setEstatename(createName(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getName(),zlHousesource.getBuildname().getName(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
			zlHousesource.setInnerarea(zlHousesource.getBuildarea());
			zlHousesource.setZstroom(zlHousesource.getRoomnumber());
			String pk=zlHousesource.getPkHousesource();
			if (StringUtils.isBlank(pk)) {
				pk="***";
			}
			Integer countc=zlHousesourceDao.getHouseCountByCode(zlHousesource.getEstatecode(),pk);
			if (countc>0) {
				return "编码不符合唯一性! 请检查单元、楼栋、房号。";
			}
			super.save(zlHousesource);
			//回写楼栋信息中的房产数量、面积；
	    		 ZlBuildingfile zlBuildingfile =zlBuildingfileService.get(zlHousesource.getBuildname().getPkBuildingfile());
	    		 Map<String, Integer> count=zlBuildingfileService.getZxCountFy(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile());
	    		Object sc=count.get("COUNT");
	    		Object sb=count.get("SUMB");
	    		Object si=count.get("SUMI");
	    	     zlBuildingfile.setNproperty(new Integer(sc.toString()));
	    		 zlBuildingfile.setBuiltuparea(getDubObj(sb));
	    		     if (zlBuildingfile.getInnerarea()==null) {
	    		    	 zlBuildingfile.setInnerarea(new Double(0));
	    			 }
	    		     zlBuildingfile.setInnerarea(getDubObj(si));
	    		     zlBuildingfileService.save1(zlBuildingfile);
	     //回写项目中总面积
	    Map<String, Integer> count2= zlBuildingfileService.getZxCountFy2(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject());
	    Object sb2=count2.get("SUMB");
	     ZlProject zlProject = zlProjectService.get(zlHousesource.getProjectname().getPkProject());
	     
	     if (zlProject.getTotalarea()==null) {
	    	 zlProject.setTotalarea(new Double(0));
		}
	     zlProject.setTotalarea(getDubObj(sb2));
	     zlProjectService.save1(zlProject);
		 
		 
	 
		return "";
	}
	/**
	 * 批改保存
	 * @param zlHousesource
	 */
	@Transactional(readOnly=false)
	public  void saveC(ZlHousesource zlHousesource) {
	
			zlHousesource.setModifier(UserUtils.getUser());
			zlHousesource.setModifiedtime(new Timestamp((new Date()).getTime()));
		
		super.save(zlHousesource);
		//回写楼栋信息中的房产数量、面积；
		 ZlBuildingfile zlBuildingfile =zlBuildingfileService.get(zlHousesource.getBuildname().getPkBuildingfile());
		 Map<String, Integer> count=zlBuildingfileService.getZxCountFy(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile());
		Object sc=count.get("COUNT");
		Object sb=count.get("SUMB");
		Object si=count.get("SUMI");
	     zlBuildingfile.setNproperty(new Integer(sc.toString()));
		 zlBuildingfile.setBuiltuparea(getDubObj(sb));
		     if (zlBuildingfile.getInnerarea()==null) {
		    	 zlBuildingfile.setInnerarea(new Double(0));
			 }
		     zlBuildingfile.setInnerarea(getDubObj(si));
		     zlBuildingfileService.save1(zlBuildingfile);
		//回写项目中总面积
		Map<String, Integer> count2= zlBuildingfileService.getZxCountFy2(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject());
		Object sb2=count2.get("SUMB");
		ZlProject zlProject = zlProjectService.get(zlHousesource.getProjectname().getPkProject());
		
		if (zlProject.getTotalarea()==null) {
			 zlProject.setTotalarea(new Double(0));
		}
		zlProject.setTotalarea(getDubObj(sb2));
		zlProjectService.save1(zlProject);
	}
	/**
	 * 房源导入覆盖新增
	 * @param zlHousesource
	 */
	@Transactional(readOnly=false)
	public  String saveP(ZlHousesource zlHousesource) {
	
		zlHousesource.setCreator(UserUtils.getUser());
		zlHousesource.setCreationtime(new Timestamp((new Date()).getTime()));
		zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
		zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
		zlHousesource.setEstatecode(createCode(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
		zlHousesource.setEstatename(createName(zlHousesource.getPkFamilyfile().getPkFamilyfile(),zlHousesource.getProjectname().getName(),zlHousesource.getBuildname().getName(),zlHousesource.getUnit(),zlHousesource.getRoomnumber()));
		zlHousesource.setInnerarea(zlHousesource.getBuildarea());
		zlHousesource.setZstroom(zlHousesource.getRoomnumber());
		 super.save(zlHousesource);
		//回写楼栋信息中的房产数量、面积；
    		 ZlBuildingfile zlBuildingfile =zlBuildingfileService.get(zlHousesource.getBuildname().getPkBuildingfile());
	    	 Map<String, Integer> count1=zlBuildingfileService.getZxCountFy(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile());
			 Object sc1=count1.get("COUNT");	
			 Object sb1=count1.get("SUMB");
	    	 Object si1=count1.get("SUMI");
	    	 if(sb1==null){
	    		 sb1=new Double(0);
	    	 }
	    	 if (si1==null) {
	    		 si1=new Double(0);
			}
    	     zlBuildingfile.setNproperty(new Integer(sc1.toString()));
    		 zlBuildingfile.setBuiltuparea(getDubObj(sb1));
    		     if (zlBuildingfile.getInnerarea()==null) {
    		    	 zlBuildingfile.setInnerarea(new Double(0));
    			 }
    		     zlBuildingfile.setInnerarea(getDubObj(si1));
    		     zlBuildingfileService.save1(zlBuildingfile);
     //回写项目中总面积
     ZlProject zlProject = zlProjectService.get(zlHousesource.getProjectname().getPkProject());
	 Map<String, Integer> count=zlBuildingfileService.getZxCountFy2(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject());
		Object sc=count.get("COUNT");
		Object sb=count.get("SUMB");
		Object si=count.get("SUMI");
		 if(sb==null){
 		 sb=new Double(0);
 	 }
 	 if (si==null) {
 		 si=new Double(0);
		}
     if (zlProject.getTotalarea()==null) {
    	 zlProject.setTotalarea(new Double(0));
	}
     zlProject.setTotalarea(getDubObj(sb));
     zlProjectService.save1(zlProject);
	return null;
	}
	/**
	 * 更新状态
	 * @param zlHousesource
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZlHousesource zlHousesource) {
		super.updateStatus(zlHousesource);
	}
	
	/**
	 * 删除数据
	 * @param zlHousesource
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZlHousesource zlHousesource) {
		   //查询合同管理房产页签
		   String sql1="select count(*) from wg_contract_house z  where z.dr=0 and z.pk_house='"+zlHousesource.getPkHousesource()+"'";
		   Integer count1= commonService.selectCount(sql1);
	    	if (count1>0) {
			 throw new ValidationException("当前房源信息已被合同管理房产页签引用，不可删除!请检查");
		    }
          zlHousesourceDao.deleteData(zlHousesource.getPkHousesource());
		//回写楼栋信息中的房产数量、面积；
		 ZlBuildingfile zlBuildingfile =zlBuildingfileService.get(zlHousesource.getBuildname().getPkBuildingfile());
		 Map<String, Integer> count=zlBuildingfileService.getZxCountFy(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile());
		Object sc=count.get("COUNT");
		Object sb=count.get("SUMB");
		Object si=count.get("SUMI");
	     zlBuildingfile.setNproperty(new Integer(sc.toString()));
		 zlBuildingfile.setBuiltuparea(getDubObj(sb));
		     if (zlBuildingfile.getInnerarea()==null) {
		    	 zlBuildingfile.setInnerarea(new Double(0));
			 }
		     zlBuildingfile.setInnerarea(getDubObj(si));
		     zlBuildingfileService.save(zlBuildingfile);
		//回写项目中总面积
		Map<String, Integer> count2= zlBuildingfileService.getZxCountFy2(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject());
		Object sb2=count2.get("SUMB");
		ZlProject zlProject = zlProjectService.get(zlHousesource.getProjectname().getPkProject());
		
		if (zlProject.getTotalarea()==null) {
			 zlProject.setTotalarea(new Double(0));
		}
		zlProject.setTotalarea(getDubObj(sb2));
		zlProjectService.save(zlProject);
		
		
	     //super.delete(zlHousesource);
	}
	/**
	 * 房源拆分删除校验
	 * @param zlHousesource
	 */
	@Transactional(readOnly=false)
	public String deleteData(ZlHousesource zlHousesource) {
		   //查询合同管理房产页签
		   String sql1="select count(*) from wg_contract_house z  where z.dr=0 and z.pk_house='"+zlHousesource.getPkHousesource()+"'";
		   Integer count1= commonService.selectCount(sql1);
	    	if (count1>0) {
			 return "当前房源信息已被合同管理房产页签引用，不可拆分!请检查";
		    }
          zlHousesourceDao.deleteData(zlHousesource.getPkHousesource());
		//回写楼栋信息中的房产数量、面积；
		 ZlBuildingfile zlBuildingfile =zlBuildingfileService.get(zlHousesource.getBuildname().getPkBuildingfile());
		 Map<String, Integer> count=zlBuildingfileService.getZxCountFy(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile());
		Object sc=count.get("COUNT");
		Object sb=count.get("SUMB");
		Object si=count.get("SUMI");
	     zlBuildingfile.setNproperty(new Integer(sc.toString()));
		 zlBuildingfile.setBuiltuparea(getDubObj(sb));
		     if (zlBuildingfile.getInnerarea()==null) {
		    	 zlBuildingfile.setInnerarea(new Double(0));
			 }
		     zlBuildingfile.setInnerarea(getDubObj(si));
		     zlBuildingfileService.save(zlBuildingfile);
		//回写项目中总面积
		Map<String, Integer> count2= zlBuildingfileService.getZxCountFy2(zlHousesource.getPkOrg().getOfficeCode(),zlHousesource.getProjectname().getPkProject());
		Object sb2=count2.get("SUMB");
		ZlProject zlProject = zlProjectService.get(zlHousesource.getProjectname().getPkProject());
		
		if (zlProject.getTotalarea()==null) {
			 zlProject.setTotalarea(new Double(0));
		}
		zlProject.setTotalarea(getDubObj(sb2));
		zlProjectService.save(zlProject);
		
		return "";
	     //super.delete(zlHousesource);
	}
	/**
	 * 导入数据
	 */
	@Transactional(readOnly=false)
	public String importData(MultipartFile file, Boolean isUpdateSupport)
	{
		if (file == null) {
			throw new ServiceException("请选择导入的数据文件！");
		}
		int successNum = 0;int failureNum = 0; int hnum =0; int inum=2;  int inum3=2; int inum1=2;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try {
			ExcelImport ei = new ExcelImport(file, 2, Integer.valueOf(0));Throwable localThrowable3 = null;
			try {
				List<ZlHousesource> list = ei.getDataList(ZlHousesource.class, new String[0]);
				
				for (int i = 0; i < list.size(); i++) {
					inum3++;
					if(list.get(i).getPkOrg()==null){
			    		 failureNum++;
						 failureMsg.append("<br/>"+"第"+inum3+"条,导入有误,没有找到此组织!");
						 continue;
			    	 }
			    		 
			     if(list.get(i).getProjectname()==null){
			    		 failureNum++;
						 failureMsg.append("<br/>"+"第"+inum3+"条,导入有误,没有找到此项目!");
						 continue;
			    	 }
			    		 if(list.get(i).getPkFamilyfile()==null){
			    		 failureNum++;
						 failureMsg.append("<br/>"+"第"+inum3+"条,导入有误,没有找到此户型!");
						 continue;
			    	 }else {
			    		 List<ZlFamilyfile> zlFamilyfile =zlFamilyfileDao.getFamilyByPk(list.get(i).getProjectname().getPkProject(),list.get(i).getPkFamilyfile().getName());
			    	 if (zlFamilyfile.size()==0) {
			    		 failureNum++;
						 failureMsg.append("<br/>"+"第"+inum3+"条,导入有误,该项目下没有此户型!");
						 continue;
				      }else{
				    	  list.get(i).setPkFamilyfile(zlFamilyfile.get(0));
				    	  ZlFamilyfile zlFamilyfile2 =zlFamilyfileService.get(zlFamilyfile.get(0));
				    	  BdFormattype zlFormattype =zlFormattypeService.get(zlFamilyfile2.getPkFormattypeid().getPkFormattype());
				    	  list.get(i).setPkFormattype(zlFormattype);
				      }
			    	 }
			    		 if(list.get(i).getBuildname()==null){
			    		 failureNum++;
						 failureMsg.append("<br/>"+"第"+inum3+"条,导入有误,没有找到此楼栋!");
						 continue;
			    	 }else {
			    		 List<ZlBuildingfile> buildingfile  = zlBuildingfileDao.getBuildByPk(list.get(i).getProjectname().getPkProject(),list.get(i).getBuildname().getName());
			    		 if (buildingfile.size()==0) {
			    			 failureNum++;
							 failureMsg.append("<br/>"+"第"+inum3+"条,导入有误,该项目下没有此楼栋!");
							 continue;
						}else {
							list.get(i).setBuildname(buildingfile.get(0));
						}
			    	 }
				}
				if (failureNum > 0) {
					failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
					  return failureMsg.toString();
				}
				for (int i = 0; i < list.size()-1; i++) {
					inum++;
					int jnum=i+3; 
					for (int j=i+1; j < list.size(); j++) {
						jnum++;
						//同一楼栋下同一单元房号不能相同
						 if(list.get(i).getPkOrg().equals(list.get(j).getPkOrg())&&list.get(i).getProjectname().equals(list.get(j).getProjectname())&&list.get(i).getBuildname().getPkBuildingfile().equals(list.get(j).getBuildname().getPkBuildingfile())&&list.get(i).getUnit().equals(list.get(j).getUnit())&&list.get(i).getRoomnumber().equals(list.get(j).getRoomnumber())){
							 failureNum++;
							 failureMsg.append("<br/>"+"第"+inum+"条数据和第"+jnum+"条数据有误:"+"同一组织同一项目同一楼栋同一单元下房间号不能重复，请重新填写房号或重新选择楼栋、项目、组织");
						   continue;
						 }
					 }
				}
				if (failureNum > 0) {
					failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
					  return failureMsg.toString();
				}else {
					/*List<ZlHousesource> zlist=new ArrayList<ZlHousesource>();
					   ZlHousesource zlFeescale2=new ZlHousesource();
					     List<ZlHousesource> zlList=this.findList(zlFeescale2);*/
					for (ZlHousesource cust : list) {
					    int inum2=2; 
						inum1++;
						inum2++;
							ValidatorUtils.validateWithException(cust, new Class[0]);
							 if(cust.getProjectname()==null){
					    		 failureNum++;
								 failureMsg.append("<br/>"+"第"+inum2+"条,导入有误,没有找到此项目!");
								 continue;
					    	 }else if(cust.getPkFamilyfile()==null){
					    		 failureNum++;
								 failureMsg.append("<br/>"+"第"+inum2+"条,导入有误,没有找到此户型!");
								 continue;
					    	 }else if(cust.getBuildname()==null){
					    		 failureNum++;
								 failureMsg.append("<br/>"+"第"+inum2+"条,导入有误,没有找到此楼栋!");
								 continue;
					    	 }
				/*		     for(ZlHousesource zl:zlList){
							    	//同一楼栋下同一单元房号不能相同
									 if(zl.getPkOrg().getOfficeCode().equals(cust.getPkOrg().getOfficeCode())&&zl.getProjectname().getPkProject().equals(cust.getProjectname().getPkProject())&&zl.getBuildname().getPkBuildingfile().equals(cust.getBuildname().getPkBuildingfile())&&zl.getUnit().equals(cust.getUnit())&&zl.getRoomnumber().equals(cust.getRoomnumber())){
										 zlist.add(cust);
										 if (isUpdateSupport) {
											 zl.setBuildarea(cust.getBuildarea());
											   String msg=	this.saveP(zl);
												if (msg!=null) {
													failureNum++;
													failureMsg.append("<br/>" + failureNum+"、第"+hnum+"行, " +msg + "导入失败!");
												}else {
													successNum++;
												}
										}else {
										 failureNum++;
										 failureMsg.append("<br/>"+"第"+inum1+"条数据有误,同一组织同一项目同一楼栋同一单元下房间号不能重复，请重新填写房号或重新选择楼栋、项目、组织");
										 break;
									 }
										
									 }
						     }*/
					}
					if (failureNum > 0) {
						failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
						  return failureMsg.toString();
					}else {
					//list.removeAll(zlist);
						for (ZlHousesource cust : list) {
							   String msg=	this.saveD(cust);
								if (!msg.equals("")) {
									failureNum++;
									failureMsg.append("<br/>" + failureNum+"、第"+hnum+"行, " +msg + "导入失败!");
								}else {
									successNum++;
								}
						}
					}
				}
			}
			catch (Throwable localThrowable1){
				localThrowable3 = localThrowable1;throw localThrowable1;
			}
			finally
			{
				if (ei != null) {
					if (localThrowable3 != null) {
						try { ei.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } 
					}else {
						ei.close();
					}
				}
			} 
		} catch (Exception e) {
			failureMsg.append(e.getMessage());
			this.logger.error(e.getMessage(), e);
		}
		successMsg.insert(0, "数据已全部导入成功！共 " + successNum + " 条。</br>");
		if(failureMsg!=null&&failureMsg.length()>0){
			successMsg.append("错误信息：</br>"+failureMsg.toString());
		}
		return successMsg.toString();
	}
	/**
	 * 房源批改
	 * @param zlHousesource
	 */
	public void houseCurrect(CurrectMergeHouseVO vo){
		//接受参数
		List<String> housecode =vo.getHousecode();
		
		List<Map<String, Object>> map =vo.getHouseList();
		List<ZlHousesource> houselist=new ArrayList<ZlHousesource>();
		//遍历
		ZlHousesource zlHousesource = new ZlHousesource();
        for(String zlhousecode:housecode){
		zlHousesource.setPkHousesource(zlhousecode);
	     for(Map<String, Object> map1 :map){
	 		ZlFamilyfile zlFamilyfile =new ZlFamilyfile();
	 		zlFamilyfile.setPkFamilyfile(map1.get("filecode").toString());
	 		zlFamilyfile.setName(map1.get("filename").toString());
	 		zlHousesource.setPkFamilyfile(zlFamilyfile);
	 		zlHousesource.setBuildarea(new Double(map1.get("buildarea").toString()));
	 		zlHousesource.setInnerarea(new Double(map1.get("innerarea").toString()));
	 		zlHousesource.setHousestate(new Integer(map1.get("hstatus").toString()));
	 		houselist.add(zlHousesource);
	         }
	        for(ZlHousesource zlHousesource2 :houselist){
				this.saveC(zlHousesource2);
			}
		}
   
	}
	/**
	 * 房源拆分
	 * @param zlHousesource
	 */
	public String houseSplit(SplitHouseVO vo) {
       //先插入数据
		List<Map<String, Object>> map =vo.getHouseList();
		List<ZlHousesource> houselist=new ArrayList<ZlHousesource>();
		//遍历
	     for(Map<String, Object> map1 :map){
	 		ZlHousesource zlHousesource1 = new ZlHousesource();
	    	 Office office = new Office();
	    	 office.setOfficeCode(map1.get("orgCode").toString());
	    	 office.setOfficeName(map1.get("orgName").toString());
	 	     zlHousesource1.setPkOrg(office);
	 	     ZlProject zlProject=new ZlProject();
	 	     zlProject.setPkProject(map1.get("projectCode").toString());
	 	     zlProject.setName(map1.get("projectName").toString());
	 	     zlHousesource1.setProjectname(zlProject);
	 	     ZlBuildingfile zlBuildingfile =new ZlBuildingfile();
	 	     zlBuildingfile.setPkBuildingfile(map1.get("buildCode").toString());
	 	     zlBuildingfile.setName(map1.get("buildName").toString());
	 	     zlHousesource1.setBuildname(zlBuildingfile);
	 	     zlHousesource1.setEstatecode(map1.get("estatecode").toString());
	 	     zlHousesource1.setEstatename(map1.get("estatename").toString());
	 	     BdFormattype  zlFormattype=new BdFormattype();
	 	     zlFormattype.setPkFormattype(map1.get("formatCode").toString());
	 	     zlFormattype.setName(map1.get("formatName").toString());
	 	     zlHousesource1.setPkFormattype(zlFormattype);
	 	     zlHousesource1.setUnit(map1.get("unit").toString());
	 	     zlHousesource1.setFloorn(map1.get("floorn").toString());
	 	     zlHousesource1.setRoomnumber(map1.get("roomnumber").toString());
	 	     zlHousesource1.setBuildarea(new Double(map1.get("buildarea").toString()));
	 	     zlHousesource1.setInnerarea(new Double(map1.get("innerarea").toString()));
	 	     ZlFamilyfile zlFamilyfile = new ZlFamilyfile();
	 	     zlFamilyfile.setPkFamilyfile(map1.get("familyCode").toString());
	 	     zlFamilyfile.setName(map1.get("familyName").toString());
	 	     zlHousesource1.setPkFamilyfile(zlFamilyfile);
	 	     if (map1.get("hstatus").toString().equals("空置")) {
	 	    	zlHousesource1.setHousestate(new Integer(0));
			 }else {
				 throw new ServiceException("房源状态异常，请检查!");
			}
	 	     houselist.add(zlHousesource1);
	         }
	        //取出拆分保存后的房产编码
            List<String> list=new ArrayList<String>();
	        for(ZlHousesource zlHousesource2 :houselist){
	        	//拆分时保存失败则删除已保存的房源并返回
		       String estate=zlHousesourceService.save1(zlHousesource2);
		       if (estate.lastIndexOf("!")!=-1) {
		    		if (list.size()>0) {
						for(String string:list){
						ZlHousesource zlHousesource21=zlHousesourceDao.getZlHousesourceByCode(string);
						ZlHousesource zlHousesource3=zlHousesourceService.get(zlHousesource21.getPkHousesource());
						String msg1=zlHousesourceService.deleteData(zlHousesource3);
						if (!msg1.equals("")) {
							return "删除已拆分的房源时:"+msg1;
						    }else {
						    	return "拆分失败，房源编码重复。";
							}
						}
					} else {
						return "拆分失败，房源编码重复。";
					}
			}else {
			       list.add(estate);
			}
			}
	        //删除数据
			String splitCode= vo.getHouseCode();
			ZlHousesource zlHousesource = this.get(splitCode);
			String msg=zlHousesourceService.deleteData(zlHousesource);
			//拆分后上级房源删除失败则删除已拆分的房源
			if (!msg.equals("")) {
				if (list.size()>0) {
					for(String string:list){
					ZlHousesource zlHousesource2=zlHousesourceDao.getZlHousesourceByCode(string);
					ZlHousesource zlHousesource3=zlHousesourceService.get(zlHousesource2.getPkHousesource());
					String msg1=zlHousesourceService.deleteData(zlHousesource3);
					if (!msg1.equals("")) {
						return "删除已拆分的房源时:"+msg1;
					    }
					}
				}
				return msg;
			}
			
	        return "";
	}
	/**
	 * 房源合并
	 * @param zlHousesource
	 */
	public boolean houseMerge(CurrectMergeHouseVO vo) {

		
		List<Map<String, Object>> map =vo.getHouseList();
		List<ZlHousesource> houselist=new ArrayList<ZlHousesource>();
		//遍历
	     for(Map<String, Object> map1 :map){
	 		ZlHousesource zlHousesource1 = new ZlHousesource();
	    	 Office office = new Office();
	    	 office.setOfficeCode(map1.get("orgCode").toString());
	    	 office.setOfficeName(map1.get("orgName").toString());
	 	     zlHousesource1.setPkOrg(office);
	 	     ZlProject zlProject=new ZlProject();
	 	     zlProject.setPkProject(map1.get("projectCode").toString());
	 	     zlProject.setName(map1.get("projectName").toString());
	 	     zlHousesource1.setProjectname(zlProject);
	 	     ZlBuildingfile zlBuildingfile =new ZlBuildingfile();
	 	     zlBuildingfile.setPkBuildingfile(map1.get("buildCode").toString());
	 	     zlBuildingfile.setName(map1.get("buildName").toString());
	 	     zlHousesource1.setBuildname(zlBuildingfile);
	 	     zlHousesource1.setEstatecode(map1.get("estatecode").toString());
	 	     zlHousesource1.setEstatename(map1.get("estatename").toString());
	 	     BdFormattype  zlFormattype=new BdFormattype();
	 	     zlFormattype.setPkFormattype(map1.get("formatCode").toString());
	 	     zlFormattype.setName(map1.get("formatName").toString());
	 	     zlHousesource1.setPkFormattype(zlFormattype);
	 	     zlHousesource1.setUnit(map1.get("unit").toString());
	 	     zlHousesource1.setFloorn(map1.get("floorn").toString());
	 	     zlHousesource1.setRoomnumber(map1.get("roomnumber").toString());
	 	     zlHousesource1.setBuildarea(new Double(map1.get("buildarea").toString()));
	 	     zlHousesource1.setInnerarea(new Double(map1.get("innerarea").toString()));
	 	     ZlFamilyfile zlFamilyfile = new ZlFamilyfile();
	 	     zlFamilyfile.setPkFamilyfile(map1.get("familyCode").toString());
	 	     zlFamilyfile.setName(map1.get("familyName").toString());
	 	     zlHousesource1.setPkFamilyfile(zlFamilyfile);
	 	     zlHousesource1.setZstroom(map1.get("roomnumber").toString());
	 	     zlHousesource1.setHbcfstatus(AbsEnumType.HbcfStatus_0);
	 	     zlHousesource1.setParentroom(map1.get("parentroom").toString());
	 	     zlHousesource1.setZstpk(map1.get("zstpk").toString());
	 	     if (map1.get("hstatus").toString().equals("空置")) {
	 	    	zlHousesource1.setHousestate(AbsEnumType.HOUSE_KZ);
			 }else {
				 return false;
			}
	 	     houselist.add(zlHousesource1);
	         }
	        for(ZlHousesource zlHousesource2 :houselist){
			this.save(zlHousesource2);
			}
			List<String> mergeCode= vo.getHousecode();
			for(String  megecode:mergeCode){
				ZlHousesource zlHousesource = zlHousesourceService.get(megecode);
				if (StringUtils.isBlank(zlHousesource.getZstpk())) {
					zlHousesource.setHbcfstatus(AbsEnumType.HbcfStatus_1);
				}else {
					String string []=zlHousesource.getZstpk().split("-");
					List<Integer> listI=new ArrayList<Integer>();
                    if (string.length>2) {
                        for(String string2:string){
                        	ZlHousesource zlHousesource2 = zlHousesourceService.get(string2);
                            listI.add(zlHousesource2.getHbcfstatus());
                        }
                        //list中取出最小值
                        Integer min=Collections.min(listI);
						zlHousesource.setHbcfstatus(min-1);
						
					}else {
						Integer integer = zlHousesourceDao.getMinHbcfstatusByZstpk(zlHousesource.getProjectname().getPkProject(),zlHousesource.getBuildname().getPkBuildingfile(),zlHousesource.getZstpk());
						if(integer==0){
							zlHousesource.setHbcfstatus(AbsEnumType.HbcfStatus_2);
						}else{
							zlHousesource.setHbcfstatus(integer-1);
						}
					}
					
					
				}
				super.save(zlHousesource);//保存合并拆分状态
				zlHousesourceDao.deleteData(zlHousesource.getPkHousesource());
			}
	        return true;
	}
	
	/**
	 * 过滤查询数据
	 */
	public void queryFilter(ZlHousesource entity) {
		if(entity.getVdef1()!=null&&!entity.getVdef1().equals("")){
			String pkContract=entity.getVdef1().split("_")[0];
			String pkh=entity.getVdef1().split("_")[1];
			String pkHouse="";
			if(pkh!=null){
				String[] pkhs=pkh.split(",");
				for (int i=0;i<pkhs.length;i++) {
					pkHouse+="'"+pkhs[i]+"'";
					if(i<pkhs.length-1){
						pkHouse+=",";
					}
				}
			}
			if(pkHouse.equals("")){
				pkHouse="'null'";
			}
			entity.getSqlMap().getDataScope().addFilter("wghtqueryhouse", "a.pk_housesource not in ("+pkHouse+") and a.pk_housesource not in "
					+ "(select house.pk_house from wg_contract_house house where nvl(house.dr,0)=0 and house.pk_contract in (select con.pk_contract "
					+ "from wg_contract con where nvl(con.dr,0)=0 and con.htstatus<>"+AbsEnumType.HtStatus_TZ+") and house.pk_contract!='"+pkContract+"')");
		}
		//重置
		entity.setVdef1(null);
	}
	/**
	 * 生成房源编码
	 */
	public String createCode(String pkFamily,String pkProject,String pkBuild,String unit,String number){
		ZlFamilyfile zlFamilyfile = zlFamilyfileService.get(pkFamily);
		
		ZlBuildingfile zlBuildingfile = zlBuildingfileService.get(pkBuild);
       if (zlFamilyfile.getIsCw()!=null) {
    	  String houseCode=zlBuildingfile.getCode()+number;
    	  return houseCode;
		}else {
			String houseCode=zlBuildingfile.getCode()+unit+number;
			return houseCode;
		}
	}
	/**
	 * 生成房源名称
	 */
	public String createName(String pkFamily,String projectName,String buildName,String unit,String number){
		ZlFamilyfile zlFamilyfile = zlFamilyfileService.get(pkFamily);
		  if (zlFamilyfile.getIsCw()!=null) {
				String houseName=projectName+"-"+buildName+"-"+number;
				return houseName;
		  }else {
				String houseName=projectName+"-"+buildName+"-"+unit+"单元-"+number;
				return houseName;
		}
	
	}
	
	/**
	 * 建房
	 * @param cwfHouse
	 */
	
	
	@Transactional(readOnly=false)
	public void build(List<Object> pk_cwf) {
		Integer area=0;
		Integer housenum=0;
		
		ZlProject pro=new ZlProject();
		ZlBuildingfile build=new ZlBuildingfile();
		
		pro.setPkProject(((List<Object>) pk_cwf.get(0)).get(4).toString());
		pro=zlProjectService.get(pro);
		build.setPkBuildingfile(((List<Object>) pk_cwf.get(0)).get(5).toString());
		build=zlBuildingfileService.get(build);
		for(Object obj:pk_cwf){
			List<Object> str=(List<Object>) obj;
			ZlHousesource house=new ZlHousesource();
			house.setPkOrg(pro.getPkOrg());
			house.setPkFamilyfile(zlFamilyfileService.findList(new ZlFamilyfile()).get(0));
			house.setPkFormattype(zlFormattypeService.findList(new BdFormattype()).get(0));
			house.setProjectname(pro);
			house.setBuildname(build);
			house.setUnit(str.get(0).toString());
			house.setRoomnumber(str.get(1).toString());
			house.setEstatename(str.get(1).toString());
			house.setFloorn(str.get(2).toString());
			house.setVdef1(0+"");
			house.setBuildarea(new Double(str.get(3).toString()));
			house.setInnerarea(new Double(str.get(3).toString()));
			house.setEstatecode(pro.getCode()+build.getCode()+str.get(0).toString()+str.get(1).toString()+str.get(2).toString());
			house.setCreator(UserUtils.getUser());
			house.setDbilldate(new Date());
			house.setZstroom(str.get(1).toString());
			super.insert(house);
			
			area+=Integer.parseInt(str.get(3).toString());
			housenum+=1;
		}
		build.setBuiltuparea(getDubObj(area));
		build.setInnerarea(getDubObj(area));
		build.setNproperty(housenum);
		build.setIsbuild("1");
		zlBuildingfileService.update(build);
		
		Map<String, Integer> count2= zlBuildingfileService.getZxCountFy2(pro.getPkOrg().getOfficeCode(),pro.getPkProject());
		Object sb2=count2.get("SUMB");
		if (pro.getTotalarea()==null) {
			pro.setTotalarea(new Double(0));
		}
		pro.setTotalarea(getDubObj(sb2));
		zlProjectService.save(pro);
	}
	
	public Double getDubObj(Object obj){
		return obj==null?new Double(0):new Double(obj.toString());
	}
	public ZlHousesource houseBody(String room){
		ZlHousesource list = zlHousesourceDao.getHouseBody(room);
		return list;
	}
	//根据主实体主键查询数据
	public ZlHousesource getZstPkByzl(String pk){
		ZlHousesource zlHousesource = zlHousesourceDao.getZstPkByzl(pk);
		return zlHousesource;
	}
}