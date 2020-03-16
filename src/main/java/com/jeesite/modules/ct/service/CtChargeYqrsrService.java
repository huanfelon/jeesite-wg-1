/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.ct.dao.CtChargeSrqrBDao;
import com.jeesite.modules.ct.dao.CtChargeYqrsrDao;
import com.jeesite.modules.ct.entity.CtChargeSrqrB;
import com.jeesite.modules.ct.entity.CtChargeYqrsr;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 应确认收入Service
 * @author GJ
 * @version 2019-11-07
 */
@Service
@Transactional(readOnly=true)
public class CtChargeYqrsrService extends CrudService<CtChargeYqrsrDao, CtChargeYqrsr> {
	
	@Autowired
	private CtChargeSrqrBDao ctChargeSrqrBDao;
    @Autowired
    private CtChargeYqrsrDao ctChargeYqrsrDao;
	/**
	 * 获取单条数据
	 * @param ctChargeYqrsr
	 * @return
	 */
	@Override
	public CtChargeYqrsr get(CtChargeYqrsr ctChargeYqrsr) {
		return super.get(ctChargeYqrsr);
	}
	
	/**
	 * 查询分页数据
	 * @param ctChargeYqrsr 查询条件
	 * @param ctChargeYqrsr.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtChargeYqrsr> findPage(CtChargeYqrsr ctChargeYqrsr) {
		return super.findPage(ctChargeYqrsr);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctChargeYqrsr
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtChargeYqrsr ctChargeYqrsr) {
		super.save(ctChargeYqrsr);
	}
	
	/**
	 * 更新状态
	 * @param ctChargeYqrsr
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtChargeYqrsr ctChargeYqrsr) {
		super.updateStatus(ctChargeYqrsr);
	}
	
	/**
	 * 删除数据
	 * @param ctChargeYqrsr
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtChargeYqrsr ctChargeYqrsr) {
		ctChargeYqrsrDao.deleteData(ctChargeYqrsr.getPkChargeYqrsr());
		//super.delete(ctChargeYqrsr);
	}
	//审批应确认收入
	public void subData(String pks){
	CtChargeYqrsr chargeYs = this.get(pks);
    chargeYs.setVbillstatus(AbsEnumType.BillStatus_SPTG);
    chargeYs.setApprover(UserUtils.getUser());
    chargeYs.setApprovetime(new Date());
    this.save(chargeYs);
	}
	//校验有没有被下游收款单参照，被参照则不可取消审批
	 public boolean  unSubYqrsr(String pks){
		   List<CtChargeSrqrB> chargeSks = ctChargeSrqrBDao.getCtChargeSrqrBByCode(pks);
		   if(chargeSks.size()>0){
				return false;
			}
			return true;
	 }
	//取消审批
	public void unSubData(String pks){
		CtChargeYqrsr chargeYs = this.get(pks);
		    chargeYs.setVbillstatus(AbsEnumType.BillStatus_ZY);
		    chargeYs.setApprover(null);
		    chargeYs.setApprovetime(null);
		    this.save(chargeYs);
	}
	//收入确认参照应确认收入,已参照但未审批的应确认收入不可再次被参照
	public List<CtChargeYqrsr> refListData(){
		List<CtChargeYqrsr> list= new ArrayList<CtChargeYqrsr>();
		List<String> pklist = ctChargeYqrsrDao.getCtChargeYqrsrByCode2();
		if (pklist.size()<=0) {
			return null;
		}
		for(String s:pklist){
			list.add(this.get(s));
		}
		return list;
	}
}