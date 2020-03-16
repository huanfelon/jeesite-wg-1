/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.base.entity.BdPsndoc;
import com.jeesite.modules.base.entity.BdPsndocJob;
import com.jeesite.modules.base.dao.BdPsndocDao;
import com.jeesite.modules.base.dao.BdPsndocJobDao;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 人员基本信息Service
 * @author LY
 * @version 2019-09-26
 */
@Service
@Transactional(readOnly=true)
public class BdPsndocService extends CrudService<BdPsndocDao, BdPsndoc> {
	
	@Autowired
	private BdPsndocDao bdPsndocDao;
	
	@Autowired
	private BdPsndocJobDao bdPsndocJobDao;
	
	/**
	 * 获取单条数据
	 * @param bdPsndoc
	 * @return
	 */
	@Override
	public BdPsndoc get(BdPsndoc bdPsndoc) {
		BdPsndoc entity = super.get(bdPsndoc);
		if (entity != null){
			BdPsndocJob bdPsndocJob = new BdPsndocJob(entity);
			bdPsndocJob.setStatus(BdPsndocJob.STATUS_NORMAL);
			entity.setBdPsndocJobList(bdPsndocJobDao.findList(bdPsndocJob));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param bdPsndoc 查询条件
	 * @param bdPsndoc.page 分页对象
	 * @return
	 */
	@Override
	public Page<BdPsndoc> findPage(BdPsndoc bdPsndoc) {
		return super.findPage(bdPsndoc);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdPsndoc
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdPsndoc bdPsndoc) {
		String pk=bdPsndoc.getPkPsndoc();
		if(pk==null||pk.equals("")){//新增
			bdPsndoc.setCreator(UserUtils.getUser());
			bdPsndoc.setCreateDate(new Date());
		}else{//更新
			bdPsndoc.setModifier(UserUtils.getUser());
			bdPsndoc.setUpdateDate(new Date());
		}
		super.save(bdPsndoc);
		
		// 保存 BdPsndoc子表
		for (BdPsndocJob bdPsndocJob : bdPsndoc.getBdPsndocJobList()){
			if (!BdPsndocJob.STATUS_DELETE.equals(bdPsndocJob.getStatus())){
				bdPsndocJob.setPkPsndoc(bdPsndoc);
				if (bdPsndocJob.getIsNewRecord()){
					bdPsndocJobDao.insert(bdPsndocJob);
				}else{
					bdPsndocJobDao.update(bdPsndocJob);
				}
			}else{
				bdPsndocJobDao.delete(bdPsndocJob);
			}
		}
	}
	
	/**
	 * 更新状态
	 * @param bdPsndoc
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdPsndoc bdPsndoc) {
		super.updateStatus(bdPsndoc);
	}
	
	/**
	 * 修改数据
	 * @param bdPsndoc
	 * @return
	 */
	public String edit(BdPsndoc bdPsndoc){
		String msg="";
		Integer count=bdPsndocDao.getCustCountByPsndoc(bdPsndoc.getPkPsndoc(), "");
		if(count>0){
			msg="该人员已被用户关联身份，不可修改！"; 
		}
		return msg;
	}
	
	/**
	 * 删除数据
	 * @param bdPsndoc
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdPsndoc bdPsndoc) {
		super.delete(bdPsndoc);
		BdPsndocJob bdPsndocJob = new BdPsndocJob();
		bdPsndocJob.setPkPsndoc(bdPsndoc);
		bdPsndocJobDao.deleteByEntity(bdPsndocJob);
	}
	
	public Map<String,Object> getUserPsndoc(String userCode){
		return bdPsndocDao.getUserPsndoc(userCode);
	}
	
	/**
	 * by tcl
	 * @param userCode
	 * @return
	 */
	public BdPsndoc getPsndocByUserCode(String userCode){
		return bdPsndocDao.getPsndocByUserCode(userCode);
	}
	
	/**
	 * 判断当前人员是否已被其他用户绑定
	 * @param pkPsndoc
	 * @return
	 */
	public Integer getCustCountByPsndoc(String pkPsndoc, String userCode){
		return bdPsndocDao.getCustCountByPsndoc(pkPsndoc,userCode);
	}
	/**
	 * 获取登录人所在部门
	 * */
	public BdPsndoc  getDeptByUser(String userCode){
		BdPsndoc bdPsndoc =bdPsndocDao.getPsndocByUserCode(userCode);
		if (bdPsndoc!=null) {
			BdPsndoc bdPsndoc2 = this.get(bdPsndoc);
			return bdPsndoc2;
		}
		return bdPsndoc;
	}
	
	/**
	 * 根据部门ids查询表头部门名称
	 */
	public void setDeptNameByPks(BdPsndoc bdPsndoc){
		
		BdDept dept=bdPsndoc.getPkDeptall();
		if(dept==null){
			return ;
		}
		
		String deptpks=bdPsndoc.getPkDeptall().getId();
		if(StringUtils.isEmpty(deptpks)){
			return ;
		}
		
		String[] codes=deptpks.split(",");
		String str="";
		for(String code:codes){
			str+="'"+code+"',";
		}
		str=str.substring(0, str.length()-1);
		
		String deptName=dao.getDeptNameByPks(str);
		
		bdPsndoc.getPkDeptall().setDeptName(deptName);
	}
	
	/**
	 * 根据部门ids查询表体部门名称
	 */
	public void setBodyDeptNameByPks(BdPsndocJob bdPsndocjob){
		
		BdDept dept=bdPsndocjob.getPkDeptcall();
		if(dept==null){
			return ;
		}
		
		String deptpks=bdPsndocjob.getPkDeptcall().getId();
		if(StringUtils.isEmpty(deptpks)){
			return ;
		}
		
		String[] codes=deptpks.split(",");
		String str="";
		for(String code:codes){
			str+="'"+code+"',";
		}
		str=str.substring(0, str.length()-1);
		
		String deptName=dao.getDeptNameByPks(str);
		
		bdPsndocjob.getPkDeptcall().setDeptName(deptName);
	}
	
	/**
	 * 数据导入
	 * @param file
	 * @param isUpdateSupport
	 * @return
	 */
	public String importData(MultipartFile file, Boolean isUpdateSupport)
	  {
	    if (file == null) {
	      throw new ServiceException("请选择导入的数据文件！");
	    }
	    int successNum = 0; int failureNum = 0;
	    StringBuilder successMsg = new StringBuilder();
	    StringBuilder failureMsg = new StringBuilder();
	    try { 
	    	ExcelImport ei = new ExcelImport(file, 2, Integer.valueOf(0)); 
	    	Throwable localThrowable3 = null;
	      try { 
	    	  List<BdPsndoc> list = ei.getDataList(BdPsndoc.class, new String[0]);
	        for (BdPsndoc user : list)
	          try
	          {
	            ValidatorUtils.validateWithException(user, new Class[0]);

	            BdPsndoc car2=new BdPsndoc();
	            car2.setCode(user.getCode());
	            List<BdPsndoc> list2 = this.findList(car2);
	            if (list2 == null||list2.size()<=0) {
	              save(user);
	              successNum++;
	              successMsg.append(new StringBuilder().append("<br/>").append(successNum).append("、人员 ").append(user.getCode()).append(" 导入成功").toString());
	            } else if (isUpdateSupport.booleanValue()) {
	              user.setPkPsndoc(list2.get(0).getPkPsndoc());
	              user.setCode(list2.get(0).getCode());
	              save(user);
	              successNum++;
	              successMsg.append(new StringBuilder().append("<br/>").append(successNum).append("、人员 ").append(user.getCode()).append(" 更新成功").toString());
	            } else {
	              failureNum++;
	              failureMsg.append(new StringBuilder().append("<br/>").append(failureNum).append("、人员 ").append(user.getCode()).append(" 已存在").toString());
	            }
	          } catch (Exception e) {
	            failureNum++;
	            String msg = new StringBuilder().append("<br/>").append(failureNum).append("、人员 ").append(user.getCode()).append(" 导入失败：").toString();
	            if ((e instanceof ConstraintViolationException)) {
	              List<String> messageList = ValidatorUtils.extractPropertyAndMessageAsList((ConstraintViolationException)e, ": ");
	              for (String message : messageList)
	                msg = new StringBuilder().append(msg).append(message).append("; ").toString();
	            }
	            else {
	              msg = new StringBuilder().append(msg).append(e.getMessage()).toString();
	            }
	            failureMsg.append(msg);
	            this.logger.error(msg, e);
	          }
	      }
	      catch (Throwable localThrowable1)
	      {
	        localThrowable3 = localThrowable1; throw localThrowable1;
	      }
	      finally
	      {
	        if (ei != null) if (localThrowable3 != null) try { ei.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else ei.close();  
	      }
	    } catch (Exception e) {
	      failureMsg.append(e.getMessage());
	      this.logger.error(e.getMessage(), e);
	    }
	    if (failureNum > 0) {
	      failureMsg.insert(0, new StringBuilder().append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：").toString());
	      throw new ServiceException(failureMsg.toString());
	    }
	    successMsg.insert(0, new StringBuilder().append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条，数据如下：").toString());

	    return successMsg.toString();
	  }
}