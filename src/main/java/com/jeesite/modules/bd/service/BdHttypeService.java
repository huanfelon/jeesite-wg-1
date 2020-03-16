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
import com.jeesite.modules.bd.dao.BdHttypeDao;
import com.jeesite.modules.bd.entity.BdHttype;
import com.jeesite.modules.wght.service.WgContractService;

/**
 * 合同类型Service
 * @author GJ
 * @version 2019-10-28
 */
@Service
@Transactional(readOnly=true)
public class BdHttypeService extends TreeService<BdHttypeDao, BdHttype> {
	@Autowired
	private WgContractService wgContractService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取单条数据
	 * @param bdHttype
	 * @return
	 */
	@Override
	public BdHttype get(BdHttype bdHttype) {
		return super.get(bdHttype);
	}
	
	/**
	 * 查询列表数据
	 * @param bdHttype
	 * @return
	 */
	@Override
	public List<BdHttype> findList(BdHttype bdHttype) {
		return super.findList(bdHttype);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdHttype
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdHttype bdHttype) {
		BdHttype zl1 =new BdHttype();
		if(StringUtils.isNotBlank(bdHttype.getParentCode())){ //有上级编码时新增和修改
			BdHttype pro=this.get(bdHttype.getParentCode());
			zl1.setParentCode(bdHttype.getParentCode());
			List<BdHttype> zl = this.findList(zl1);
			for(BdHttype co :zl){
				//有上级编码时新增
				if(bdHttype.getCode().equals(co.getCode())&&StringUtils.isBlank(bdHttype.getPkHttype())){
					throw new ValidationException("编码不符合唯一性!");
				}else if (StringUtils.isNotBlank(bdHttype.getPkHttype())) {//有上级编码时修改
					//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
					if (co.getCode().equals(bdHttype.getCode())&&!co.getPkHttype().equals(bdHttype.getPkHttype())) {
			    		 throw new ValidationException("编码不符合唯一性!");
					}
				}
			}
			if(bdHttype.getCode().equals(pro.getCode())){
				throw new ValidationException("编码规则应为上级编码和两位字符的组合!");
			}
		}else if(StringUtils.isNotBlank(bdHttype.getPkHttype())){//没上级编码时修改
			BdHttype zlCostproject2 = new BdHttype();
			List<BdHttype> zl = this.findList(zlCostproject2);
			for(BdHttype co :zl){
				//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
				if (co.getCode().equals(bdHttype.getCode())&&!co.getPkHttype().equals(bdHttype.getPkHttype())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
		}else{//没上级编码时新增
			List<BdHttype> zlf=this.findList(zl1);
			for(BdHttype f1:zlf){
				if(bdHttype.getCode().equals(f1.getCode())&& StringUtils.isBlank(bdHttype.getPkHttype())){
					throw new ValidationException("编码不符合唯一性!");
				}
			}
		}
		super.save(bdHttype);
	}
	
	/**
	 * 更新状态
	 * @param bdHttype
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdHttype bdHttype) {
		super.updateStatus(bdHttype);
	}
	
	/**
	 * 删除数据
	 * @param bdHttype
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdHttype bdHttype) {
		BdHttype bdHttype1=new BdHttype();
		bdHttype1.setParentCode(bdHttype.getPkHttype());
		BdHttype bdHttype2 = this.getLastByParentCode(bdHttype1);
		if (bdHttype2!=null) {
			throw new ValidationException("当前合同类型存在下级,不可删除!");
		}
		//查询合同管理
		 String sql="select count(*) from wg_contract z where z.dr=0 and z.httype='"+bdHttype.getPkHttype()+"'";
		  Integer count= commonService.selectCount(sql);
		if (count>0) {
			 throw new ValidationException("当前合同类型已被合同管理引用，不可删除!请检查");
		}
		super.delete(bdHttype);
	}
	
}