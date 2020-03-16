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
import com.jeesite.modules.bd.dao.BdFormattypeDao;
import com.jeesite.modules.bd.entity.BdFormattype;
import com.jeesite.modules.zl.service.ZlFamilyfileService;

/**
 * 业态类型Service
 * @author GJ
 * @version 2019-10-30
 */
@Service
@Transactional(readOnly=true)
public class BdFormattypeService extends TreeService<BdFormattypeDao, BdFormattype> {
	@Autowired
	private ZlFamilyfileService zlFamilyfileService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取单条数据
	 * @param bdFormattype
	 * @return
	 */
	@Override
	public BdFormattype get(BdFormattype bdFormattype) {
		return super.get(bdFormattype);
	}
	
	/**
	 * 查询列表数据
	 * @param bdFormattype
	 * @return
	 */
	@Override
	public List<BdFormattype> findList(BdFormattype bdFormattype) {
		return super.findList(bdFormattype);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdFormattype
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdFormattype bdFormattype) {
		BdFormattype zl1 =new BdFormattype();
		if(StringUtils.isNotBlank(bdFormattype.getParentCode())){ //有上级编码时新增和修改
			BdFormattype pro=this.get(bdFormattype.getParentCode());
			zl1.setParentCode(bdFormattype.getParentCode());
			List<BdFormattype> zl = this.findList(zl1);
			for(BdFormattype co :zl){
				//有上级编码时新增
				if(bdFormattype.getCode().equals(co.getCode())&&StringUtils.isBlank(bdFormattype.getPkFormattype())){
					throw new ValidationException("编码不符合唯一性!");
				}else if (StringUtils.isNotBlank(bdFormattype.getPkFormattype())) {//有上级编码时修改
					//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
					if (co.getCode().equals(bdFormattype.getCode())&&!co.getPkFormattype().equals(bdFormattype.getPkFormattype())) {
			    		 throw new ValidationException("编码不符合唯一性!");
					}
				}
			}
			if(bdFormattype.getCode().equals(pro.getCode())){
				throw new ValidationException("编码规则应为上级编码和两位字符的组合!");
			}
		}else if(StringUtils.isNotBlank(bdFormattype.getPkFormattype())){//没上级编码时修改
			BdFormattype zlCostproject2 = new BdFormattype();
			List<BdFormattype> zl = this.findList(zlCostproject2);
			for(BdFormattype co :zl){
				//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
				if (co.getCode().equals(bdFormattype.getCode())&&!co.getPkFormattype().equals(bdFormattype.getPkFormattype())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
		}else{//没上级编码时新增
			List<BdFormattype> zlf=this.findList(zl1);
			for(BdFormattype f1:zlf){
				if(bdFormattype.getCode().equals(f1.getCode())&& StringUtils.isBlank(bdFormattype.getPkFormattype())){
					throw new ValidationException("编码不符合唯一性!");
				}
			}
		}
		super.save(bdFormattype);
	}
	
	/**
	 * 更新状态
	 * @param bdFormattype
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdFormattype bdFormattype) {
		super.updateStatus(bdFormattype);
	}
	
	/**
	 * 删除数据
	 * @param bdFormattype
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdFormattype bdFormattype) {
		BdFormattype bdHttype1=new BdFormattype();
		bdHttype1.setParentCode(bdFormattype.getPkFormattype());
		BdFormattype bdHttype2 = this.getLastByParentCode(bdHttype1);
		if (bdHttype2!=null) {
			throw new ValidationException("当前业态类型存在下级,不可删除!");
		}
			//查询户型
		 String sql="select count(*) from zl_familyfile z where z.dr=0 and z.pk_formattypeid='"+bdFormattype.getPkFormattype()+"'";
		  Integer count= commonService.selectCount(sql);
		if (count>0) {
			 throw new ValidationException("当前业态类型已被户型引用，不可删除!请检查");
		}
		super.delete(bdFormattype);
	}
	
}