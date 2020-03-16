/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.service;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.dao.BdFeescaleDao;
import com.jeesite.modules.bd.entity.BdFeescale;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.wght.dao.WgContractWyfDao;
import com.jeesite.modules.wght.dao.WgContractZqfyDao;

/**
 * 收费标准Service
 * @author GJ
 * @version 2019-10-25
 */
@Service
@Transactional(readOnly=true)
public class BdFeescaleService extends CrudService<BdFeescaleDao, BdFeescale> {
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private BdFeescaleDao bdFeescaleDao;

	@Autowired
	private WgContractWyfDao wgContractWyfDao;
	@Autowired
	private WgContractZqfyDao wgContractZqfyDao;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取单条数据
	 * @param bdFeescale
	 * @return
	 */
	@Override
	public BdFeescale get(BdFeescale bdFeescale) {
		return super.get(bdFeescale);
	}
	
	/**
	 * 查询分页数据
	 * @param bdFeescale 查询条件
	 * @param bdFeescale.page 分页对象
	 * @return
	 */
	@Override
	public Page<BdFeescale> findPage(BdFeescale bdFeescale) {
		return super.findPage(bdFeescale);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdFeescale
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdFeescale bdFeescale) {
		if (bdFeescale.getIsNewRecord()) {
			bdFeescale.setCode(createCode(bdFeescale.getPkOrg().getOfficeCode()));
		}
		super.save(bdFeescale);
	}
	
	/**
	 * 更新状态
	 * @param bdFeescale
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdFeescale bdFeescale) {
		super.updateStatus(bdFeescale);
	}
	
	/**
	 * 删除数据
	 * @param bdFeescale
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdFeescale bdFeescale) {
		
		//查询物业费页签
		 String sql="select count(*) from wg_contract_wyf w where w.dr=0 and w.pk_feescale='"+bdFeescale.getPkFeescale()+"'";
		  Integer count= commonService.selectCount(sql);
		if (count>0) {
				 throw new ValidationException("当前收费标准已被合同管理其他周期费用页签引用，不可删除!请检查");
		}
		//查询其他周期费用
		 String sql1="select count(*) from wg_contract_zqfy z where z.dr=0 and z.pk_feescale='"+bdFeescale.getPkFeescale()+"'";
		  Integer count1= commonService.selectCount(sql1);
		if (count1>0) {
			 throw new ValidationException("当前收费标准已被合同管理其他周期费用页签引用，不可删除!请检查");
		}
		super.delete(bdFeescale);
	}
	//生成收费标准编码
	public String createCode(String officeCode){
		 String sql2="select max(code) from bd_feescale b where nvl(b.status,0)=0 and b.code like '"+officeCode+"%'";
		Object maxCode = commonService.selectObject(sql2);
		if(getStgObj(maxCode).equals("")){
			return officeCode+"001";
		}
		int length=getStgObj(maxCode).length();
		Integer no= new Integer(getStgObj(maxCode).substring(length-3, length));
	    Integer maxno=no+1;
	    String strno = maxno.toString();
	    if (strno.length()==1) {
	    	return officeCode+"00"+maxno;
		} else if (strno.length()==2) {
			return officeCode+"0"+maxno;
		}
	    return officeCode+maxno;
	}
	public String getStgObj(Object obj){
		return obj==null?"":obj.toString();
	}
}