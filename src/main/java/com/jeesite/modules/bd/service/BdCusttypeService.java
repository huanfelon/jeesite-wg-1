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
import com.jeesite.modules.base.service.BdCustomerService;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.dao.BdCusttypeDao;
import com.jeesite.modules.bd.entity.BdCusttype;

/**
 * 客户类型Service
 * @author GJ
 * @version 2019-10-30
 */
@Service
@Transactional(readOnly=true)
public class BdCusttypeService extends TreeService<BdCusttypeDao, BdCusttype> {
	@Autowired
	private BdCustomerService bdCustomerService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取单条数据
	 * @param bdCusttype
	 * @return
	 */
	@Override
	public BdCusttype get(BdCusttype bdCusttype) {
		return super.get(bdCusttype);
	}
	
	/**
	 * 查询列表数据
	 * @param bdCusttype
	 * @return
	 */
	@Override
	public List<BdCusttype> findList(BdCusttype bdCusttype) {
		return super.findList(bdCusttype);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdCusttype
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdCusttype bdCusttype) {
		BdCusttype zl1 =new BdCusttype();
		if(StringUtils.isNotBlank(bdCusttype.getParentCode())){ //有上级编码时新增和修改
			BdCusttype pro=this.get(bdCusttype.getParentCode());
			zl1.setParentCode(bdCusttype.getParentCode());
			List<BdCusttype> zl = this.findList(zl1);
			for(BdCusttype co :zl){
				//有上级编码时新增
				if(bdCusttype.getCode().equals(co.getCode())&&StringUtils.isBlank(bdCusttype.getPkCusttype())){
					throw new ValidationException("编码不符合唯一性!");
				}else if (StringUtils.isNotBlank(bdCusttype.getPkCusttype())) {//有上级编码时修改
					//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
					if (co.getCode().equals(bdCusttype.getCode())&&!co.getPkCusttype().equals(bdCusttype.getPkCusttype())) {
			    		 throw new ValidationException("编码不符合唯一性!");
					}
				}
			}
			if(bdCusttype.getCode().equals(pro.getCode())){
				throw new ValidationException("编码规则应为上级编码和两位字符的组合!");
			}
		}else if(StringUtils.isNotBlank(bdCusttype.getPkCusttype())){//没上级编码时修改
			BdCusttype zlCostproject2 = new BdCusttype();
			List<BdCusttype> zl = this.findList(zlCostproject2);
			for(BdCusttype co :zl){
				//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
				if (co.getCode().equals(bdCusttype.getCode())&&!co.getPkCusttype().equals(bdCusttype.getPkCusttype())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
		}else{//没上级编码时新增
			List<BdCusttype> zlf=this.findList(zl1);
			for(BdCusttype f1:zlf){
				if(bdCusttype.getCode().equals(f1.getCode())&& StringUtils.isBlank(bdCusttype.getPkCusttype())){
					throw new ValidationException("编码不符合唯一性!");
				}
			}
		}
		super.save(bdCusttype);
	}
	
	/**
	 * 更新状态
	 * @param bdCusttype
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdCusttype bdCusttype) {
		super.updateStatus(bdCusttype);
	}
	
	/**
	 * 删除数据
	 * @param bdCusttype
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdCusttype bdCusttype) {
		BdCusttype bdHttype1=new BdCusttype();
		bdHttype1.setParentCode(bdCusttype.getPkCusttype());
		BdCusttype bdHttype2 = this.getLastByParentCode(bdHttype1);
		if (bdHttype2!=null) {
			throw new ValidationException("当前客户类型存在下级,不可删除!");
		}
		//查询客户信息
		 String sql="select count(*) from bd_customer z where z.status=0 and z.pk_custtype='"+bdCusttype.getPkCusttype()+"'";
		  Integer count= commonService.selectCount(sql);
		if (count>0) {
			 throw new ValidationException("当前客户类型已被客户信息中心引用，不可删除!请检查");
		}
		super.delete(bdCusttype);
	}
	
}