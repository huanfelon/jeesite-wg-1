/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.service;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.dao.BdCategoryDao;
import com.jeesite.modules.bd.dao.BdProjectDao;
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.ct.dao.CtChargeSkBDao;
import com.jeesite.modules.wght.dao.WgContractWytypeDao;
import com.jeesite.modules.wght.dao.WgContractYjDao;
import com.jeesite.modules.wght.dao.WgContractZltypeDao;
import com.jeesite.modules.wght.dao.WgContractZqfyDao;

/**
 * 预算项目Service
 * @author GJ
 * @version 2019-10-25
 */
@Service
@Transactional(readOnly=true)
public class BdProjectService extends TreeService<BdProjectDao, BdProject> {
	@Autowired
	private BdCategoryDao bdCategoryDao;
	@Autowired
	private WgContractZqfyDao wgContractZqfyDao;
	@Autowired
	private WgContractYjDao wgContractYjDao;
	@Autowired
	private WgContractZltypeDao wgContractZltypeDao;
	@Autowired
	private WgContractWytypeDao wgContractWytypeDao;
	@Autowired 
	private CtChargeSkBDao ctChargeSkBDao;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取单条数据
	 * @param bdProject
	 * @return
	 */
	@Override
	public BdProject get(BdProject bdProject) {
		return super.get(bdProject);
	}
	
	/**
	 * 查询列表数据
	 * @param bdProject
	 * @return
	 */
	@Override
	public List<BdProject> findList(BdProject bdProject) {
		return super.findList(bdProject);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdProject
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdProject bdProject) {
		BdProject zl1 =new BdProject();
		if(StringUtils.isNotBlank(bdProject.getParentCode())){ //有上级编码时新增和修改
			BdProject pro=this.get(bdProject.getParentCode());
			zl1.setParentCode(bdProject.getParentCode());
			List<BdProject> zl = this.findList(zl1);
			for(BdProject co :zl){
				//有上级编码时新增
				if(bdProject.getCode().equals(co.getCode())&&StringUtils.isBlank(bdProject.getPkProject())){
					throw new ValidationException("编码不符合唯一性!");
				}else if (StringUtils.isNotBlank(bdProject.getPkProject())) {//有上级编码时修改
					//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
					if (co.getCode().equals(bdProject.getCode())&&!co.getPkProject().equals(bdProject.getPkProject())) {
			    		 throw new ValidationException("编码不符合唯一性!");
					}
				}
			}
			if(bdProject.getCode().equals(pro.getCode())){
				throw new ValidationException("编码规则应为上级编码和两位字符的组合!");
			}
		}else if(StringUtils.isNotBlank(bdProject.getPkProject())){//没上级编码时修改
			BdProject zlCostproject2 = new BdProject();
			List<BdProject> zl = this.findList(zlCostproject2);
			for(BdProject co :zl){
				//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
				if (co.getCode().equals(bdProject.getCode())&&!co.getPkProject().equals(bdProject.getPkProject())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
		}else{//没上级编码时新增
			List<BdProject> zlf=this.findList(zl1);
			for(BdProject f1:zlf){
				if(bdProject.getCode().equals(f1.getCode())&& StringUtils.isBlank(bdProject.getPkProject())){
					throw new ValidationException("编码不符合唯一性!");
				}
			}
		}
		super.save(bdProject);
	}
	
	/**
	 * 更新状态
	 * @param bdProject
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdProject bdProject) {
		super.updateStatus(bdProject);
	}
	
	/**
	 * 删除数据
	 * @param bdProject
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdProject bdProject) {
		BdProject bdCategory1=new BdProject();
		bdCategory1.setParentCode(bdProject.getPkProject());
		BdProject bdCategory2 = this.getLastByParentCode(bdCategory1);
		if (bdCategory2!=null) {
			throw new ValidationException("当前预算项目存在下级,不可删除!");
		}
		
		//查询其他周期费用
		 String sql="select count(*) from wg_contract_zqfy z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
		  Integer count= commonService.selectCount(sql);
		if (count>0) {
			 throw new ValidationException("当前预算项目已被合同管理其他周期费用页签引用，不可删除!请检查");
		}
		//查询合同管理押金页签
		 String sql1="select count(*) from wg_contract_yj z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
		  Integer count1= commonService.selectCount(sql1);
		if (count1>0) {
			throw new ValidationException("当前预算项目已被合同管理押金页签引用，不可删除!请检查");
		}
		//查询租赁支付方式页签
		 String sql2="select count(*) from wg_contract_zltype z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
		  Integer count2= commonService.selectCount(sql2);
		if (count2>0) {
			throw new ValidationException("当前预算项目已被合同管理租赁支付方式页签引用，不可删除!请检查");
		}
		//查询物业支付方式页签
		 String sql3="select count(*) from wg_contract_wytype z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
		  Integer count3= commonService.selectCount(sql3);
		if (count3>0) {
			throw new ValidationException("当前预算项目已被合同管理物业支付方式页签引用，不可删除!请检查");
		}
		//查询收款单收款明细页签
		 String sql4="select count(*) from ct_charge_sk_b z where z.dr=0 and z.pk_sf_project='"+bdProject.getPkProject()+"'";
		  Integer count4= commonService.selectCount(sql4);
		if (count4>0) {
			throw new ValidationException("当前预算项目已被收款单收款明细页签引用，不可删除!请检查");
		}
		//查询收款单收款明细页签
		 String sql5="select count(*) from ct_rentbill_zjmx z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
		  Integer count5= commonService.selectCount(sql5);
		 if (count5>0) {
				throw new ValidationException("当前预算项目已被租约账单租金明细页签引用，不可删除!请检查");

		}
		super.delete(bdProject);
	}
}