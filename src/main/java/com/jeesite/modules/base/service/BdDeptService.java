/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.base.dao.BdDeptDao;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * bd_deptService
 * @author gj
 * @version 2019-04-24
 */
@Service
@Transactional(readOnly=true)
public class BdDeptService extends TreeService<BdDeptDao, BdDept> {
	@Autowired
	private  BdDeptDao deptDao;
	/**
	 * 获取单条数据
	 * @param bdDept
	 * @return
	 */
	@Override
	public BdDept get(BdDept bdDept) {
		return super.get(bdDept);
	}
	
	/**
	 * 查询列表数据
	 * @param bdDept
	 * @return
	 */
	@Override
	public List<BdDept> findList(BdDept bdDept) {
		return super.findList(bdDept);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdDept
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdDept bdDept) {
		super.save(bdDept);
	}
	
	/**
	 * 更新状态
	 * @param bdDept
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdDept bdDept) {
		super.updateStatus(bdDept);
	}
	
	/**
	 * 删除数据
	 * @param bdDept
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdDept bdDept) {
		super.delete(bdDept);
	}
	/**
	 * 查询组织
	 * @param 
	 * @return
	 */
	public List<Map<String,String>> selectOrgBycode(String pk_url){
		List<Map<String,String>> dept=deptDao.findOrgBycode(pk_url);
		return dept;
	}
	
	
	public List<Map<String,String>> selectPostBycode(String bdept){
		List<Map<String,String>> post=deptDao.findPostBycode(bdept);
		return post;
	}
	/**
	 * 查询部门管理员
	 * @param 
	 * @return
	 */
	public String selectNameByCode(BdDept bdDept){

			     String bd=bdDept.getDeptAdmin().getUserCode();
					    	
				String [] bds=bd.split(",");
				List<String> list=new ArrayList<String>();
				for(int i=0 ;i<bds.length;i++){
			        list.add(bds[i]);
				  }
				String dept=deptDao.selectNameByCode(list);
					
			    bdDept.getDeptAdmin().setUserName(dept);
			    return dept;
				    

	}

		@Override
		public void addDataScopeFilter(BdDept entity) {
			Office cop=EmpUtils.getOffice();
			//entity.getSqlMap().getDataScope().addFilter("dsf", "Company", "u11.company_code",  DataScope.CTRL_PERMI_HAVE);
			if(!StringUtils.isEmpty(cop.getOfficeCode())){
				entity.getSqlMap().getDataScope().addFilter("dsf","d1.office_code ='"+cop.getOfficeCode()+"'");
			}
		}
}