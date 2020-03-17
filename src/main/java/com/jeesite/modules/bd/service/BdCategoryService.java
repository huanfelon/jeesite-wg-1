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
import com.jeesite.modules.bd.dao.BdCategoryDao;
import com.jeesite.modules.bd.entity.BdCategory;

/**
 * bd_categoryService
 * @author GJ
 * @version 2019-10-16
 */
@Service
@Transactional(readOnly=true)
public class BdCategoryService extends TreeService<BdCategoryDao, BdCategory> {
	@Autowired
	private BdCategoryDao bdCategoryDao;
	/**
	 * 获取单条数据
	 * @param bdCategory
	 * @return
	 */
	@Override
	public BdCategory get(BdCategory bdCategory) {
		return super.get(bdCategory);
	}
	
	/**
	 * 查询列表数据
	 * @param bdCategory
	 * @return
	 */
	@Override
	public List<BdCategory> findList(BdCategory bdCategory) {
		return super.findList(bdCategory);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdCategory
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdCategory bdCategory) {
		BdCategory zl1 =new BdCategory();
		if(StringUtils.isNotBlank(bdCategory.getParentCode())){ //有上级编码时新增和修改
			BdCategory pro=this.get(bdCategory.getParentCode());
			zl1.setParentCode(bdCategory.getParentCode());
			List<BdCategory> zl = this.findList(zl1);
			for(BdCategory co :zl){
				//有上级编码时新增
				if(bdCategory.getCode().equals(co.getCode())&&StringUtils.isBlank(bdCategory.getPkCategory())){
					throw new ValidationException("编码不符合唯一性!");
				}else if (StringUtils.isNotBlank(bdCategory.getPkCategory())) {//有上级编码时修改
					//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
					if (co.getCode().equals(bdCategory.getCode())&&!co.getPkCategory().equals(bdCategory.getPkCategory())) {
			    		 throw new ValidationException("编码不符合唯一性!");
					}
				}
			}
			if(bdCategory.getCode().equals(pro.getCode())){
				throw new ValidationException("编码规则应为上级编码和两位字符的组合!");
			}
		}else if(StringUtils.isNotBlank(bdCategory.getPkCategory())){//没上级编码时修改
			BdCategory zlCostproject2 = new BdCategory();
			List<BdCategory> zl = this.findList(zlCostproject2);
			for(BdCategory co :zl){
				//修改时判断时候修改编码，没修改则判断后保存，若修改是否重复，重复则返回，没重复则保存
				if (co.getCode().equals(bdCategory.getCode())&&!co.getPkCategory().equals(bdCategory.getPkCategory())) {
		    		 throw new ValidationException("编码不符合唯一性!");
				}
			}
		}else{//没上级编码时新增
			List<BdCategory> zlf=this.findList(zl1);
			for(BdCategory f1:zlf){
				if(bdCategory.getCode().equals(f1.getCode())&& StringUtils.isBlank(bdCategory.getPkCategory())){
					throw new ValidationException("编码不符合唯一性!");
				}
			}
		}
		super.save(bdCategory);
	}
	
	/**
	 * 更新状态
	 * @param bdCategory
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdCategory bdCategory) {
		super.updateStatus(bdCategory);
	}
	
	/**
	 * 删除数据
	 * @param bdCategory
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdCategory bdCategory) {
		BdCategory bdCategory1=new BdCategory();
		bdCategory1.setParentCode(bdCategory.getPkCategory());
		BdCategory bdCategory2 = this.getLastByParentCode(bdCategory1);
		if (bdCategory2!=null) {
			throw new ValidationException("当前预算类别存在下级,不可删除!");
		}
		super.delete(bdCategory);
	}
}