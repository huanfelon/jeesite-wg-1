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
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.ct.dao.CtChargeSrqrBDao;
import com.jeesite.modules.ct.dao.CtChargeSrqrDao;
import com.jeesite.modules.ct.entity.CtChargeSrqr;
import com.jeesite.modules.ct.entity.CtChargeSrqrB;
import com.jeesite.modules.ct.entity.CtChargeYqrsr;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 收入确认Service
 * @author GJ
 * @version 2019-11-07
 */
@Service
@Transactional(readOnly=true)
public class CtChargeSrqrService extends CrudService<CtChargeSrqrDao, CtChargeSrqr> {
	
	@Autowired
	private CtChargeSrqrDao ctChargeSrqrDao;
	@Autowired
	private CtChargeSrqrBDao ctChargeSrqrBDao;
	@Autowired
	private CtChargeYqrsrService ctChargeYqrsrService;
	@Autowired
	private CommonBaseService commonService;
	
	/**
	 * 获取单条数据
	 * @param ctChargeSrqr
	 * @return
	 */
	@Override
	public CtChargeSrqr get(CtChargeSrqr ctChargeSrqr) {
		CtChargeSrqr entity = super.get(ctChargeSrqr);
		if (entity != null){
			CtChargeSrqrB ctChargeSrqrB = new CtChargeSrqrB(entity);
			ctChargeSrqrB.setStatus(CtChargeSrqrB.STATUS_NORMAL);
			entity.setCtChargeSrqrBList(ctChargeSrqrBDao.findList(ctChargeSrqrB));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param ctChargeSrqr 查询条件
	 * @param ctChargeSrqr.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtChargeSrqr> findPage(CtChargeSrqr ctChargeSrqr) {
		return super.findPage(ctChargeSrqr);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctChargeSrqr
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtChargeSrqr ctChargeSrqr) {
		super.save(ctChargeSrqr);
		// 保存 CtChargeSrqr子表
		for (CtChargeSrqrB ctChargeSrqrB : ctChargeSrqr.getCtChargeSrqrBList()){
			if (!CtChargeSrqrB.STATUS_DELETE.equals(ctChargeSrqrB.getStatus())){
				ctChargeSrqrB.setPkChargeSrqr(ctChargeSrqr);
				if (ctChargeSrqrB.getIsNewRecord()){
					ctChargeSrqrBDao.insert(ctChargeSrqrB);
				}else{
					ctChargeSrqrBDao.update(ctChargeSrqrB);
				}
			}else{
				ctChargeSrqrBDao.delete(ctChargeSrqrB);
			}
		}
	}
	
	/**
	 * 更新状态
	 * @param ctChargeSrqr
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtChargeSrqr ctChargeSrqr) {
		super.updateStatus(ctChargeSrqr);
	}
	
	/**
	 * 删除数据
	 * @param ctChargeSrqr
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtChargeSrqr ctChargeSrqr) {
		ctChargeSrqrDao.deleteData(ctChargeSrqr.getPkChargeSrqr());
		ctChargeSrqrBDao.deleteData(ctChargeSrqr.getPkChargeSrqr());
		//super.delete(ctChargeSrqr);
		/*CtChargeSrqrB ctChargeSrqrB = new CtChargeSrqrB();
		ctChargeSrqrB.setPkChargeSrqr(ctChargeSrqr);
		ctChargeSrqrBDao.deleteByEntity(ctChargeSrqrB);*/
	}
	//审批
	public void subData(String pks){
		CtChargeSrqr chargeSk =this.get(pks);
		chargeSk.setVbillstatus(AbsEnumType.BillStatus_SPTG);
		chargeSk.setApprover(UserUtils.getUser());
		chargeSk.setApprovetime(new Date());
		this.save(chargeSk);
		for(CtChargeSrqrB chargeSrqrB :chargeSk.getCtChargeSrqrBList()){
			//更新应确认收入
			String sql="update ct_charge_yqrsr c set c.nsrqrmny =(select sum(nvl(s.no_tax_amount,0)) "
					+ "from ct_charge_srqr_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+chargeSrqrB.getLyvbillno()+"' "
					+ " and exists(select 1 from ct_charge_srqr q where q.pk_charge_srqr=s.pk_charge_srqr "
					+ " and nvl(q.dr,0)=0 and q.vbillstatus=1)) ,c.nqsmny=c.no_tax_amount-(select sum(nvl(s.no_tax_amount,0)) "
					+ "from ct_charge_srqr_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+chargeSrqrB.getLyvbillno()+"' "
					+ "and exists(select 1 from ct_charge_srqr q where q.pk_charge_srqr=s.pk_charge_srqr "
					+ "and nvl(q.dr,0)=0 and q.vbillstatus=1)) where c.pk_charge_yqrsr='"+chargeSrqrB.getLyvbillno()+"'";
			commonService.updateSql(sql);
			//更新合同页签收入分摊实收金额
			String sql1= "update wg_contract_srcf f set f.nrealmny =(select sum(nvl(s.no_tax_amount,0)) from ct_charge_srqr_b "
					+ "s where nvl(s.dr,0)=0 and s.vsrcid2 ='"+chargeSrqrB.getVsrcid2()+"'"
							+ " and exists(select 1 from ct_charge_srqr q where q.pk_charge_srqr=s.pk_charge_srqr "
							+ "and nvl(q.dr,0)=0 and q.vbillstatus=1)) where f.pk_contract_srcf='"+chargeSrqrB.getVsrcid2()+"'";
			commonService.updateSql(sql1);
		}
	}
	//取消审批
	public boolean unSubData(String pks){
		//判断是不是最新一笔收入确认单
		Integer countInteger = ctChargeSrqrDao.getSrqrCountUnsub(pks);
		if (countInteger>0) {
			return false;
		}
		CtChargeSrqr chargeSk = this.get(pks);
		chargeSk.setVbillstatus(AbsEnumType.BillStatus_ZY);
		chargeSk.setApprover(null);
		chargeSk.setApprovetime(null);
		this.save(chargeSk);
		for(CtChargeSrqrB chargeSrqrB :chargeSk.getCtChargeSrqrBList()){
			//更新应确认收入已收金额，欠收金额
			String sql="update ct_charge_yqrsr c set c.nsrqrmny =(select nvl(sum(nvl(s.no_tax_amount,0)),0) "
					+ "from ct_charge_srqr_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+chargeSrqrB.getLyvbillno()+"' "
					+ " and exists(select 1 from ct_charge_srqr q where q.pk_charge_srqr=s.pk_charge_srqr "
					+ " and nvl(q.dr,0)=0 and q.vbillstatus=1)) ,c.nqsmny=c.no_tax_amount-(select nvl(sum(nvl(s.no_tax_amount,0)),0) "
					+ "from ct_charge_srqr_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+chargeSrqrB.getLyvbillno()+"' "
					+ "and exists(select 1 from ct_charge_srqr q where q.pk_charge_srqr=s.pk_charge_srqr "
					+ "and nvl(q.dr,0)=0 and q.vbillstatus=1)) where c.pk_charge_yqrsr='"+chargeSrqrB.getLyvbillno()+"'";
			commonService.updateSql(sql);
			//更新合同页签收入分摊实收金额
			String sql1= "update wg_contract_srcf f set f.nrealmny =(select nvl(sum(nvl(s.no_tax_amount,0)),0) from ct_charge_srqr_b "
					+ "s where nvl(s.dr,0)=0 and s.vsrcid2 ='"+chargeSrqrB.getVsrcid2()+"'"
							+ " and exists(select 1 from ct_charge_srqr q where q.pk_charge_srqr=s.pk_charge_srqr "
							+ "and nvl(q.dr,0)=0 and q.vbillstatus=1)) where f.pk_contract_srcf='"+chargeSrqrB.getVsrcid2()+"'";
			commonService.updateSql(sql1);
		}
		return true;
	}
	//应收单穿透收款单转换
	public CtChargeSrqr refData(String pkChargeYs){
		   String [] pkStrings=pkChargeYs.split("-");
		   CtChargeYqrsr chargeYs = ctChargeYqrsrService.get(pkStrings[0]);
		   CtChargeSrqr ctChargeSk=new CtChargeSrqr();
		   ctChargeSk.setPkOrg(chargeYs.getPkOrg());
		   ctChargeSk.setPkDept(chargeYs.getPkDept());
		   ctChargeSk.setPkProject(chargeYs.getPkProject());
		   ctChargeSk.setIszz(AbsEnumType.CT_CHARGE_iSZZ);
           Double double1= new Double(0);
		   //穿透制单转换表体
		   List<CtChargeSrqrB> list= new ArrayList<CtChargeSrqrB>(); 
		   for(String string:pkStrings){
			   CtChargeYqrsr chargeYs1 = ctChargeYqrsrService.get(string);
			   CtChargeSrqrB ctChargeSkB = new CtChargeSrqrB();
			   ctChargeSkB.setPkCustomer(chargeYs1.getPkCustomer());
			   ctChargeSkB.setPkBuild(chargeYs1.getPkBuild());
			   ctChargeSkB.setPkHouse(chargeYs1.getPkHouse());
			   ctChargeSkB.setPkSfProject(chargeYs1.getPkSfProject());
			   ctChargeSkB.setFyksdate(chargeYs1.getFyksdate());
			   ctChargeSkB.setFyjzdate(chargeYs1.getFyjzdate());
			   ctChargeSkB.setKjyears(chargeYs1.getKjyears());
			   ctChargeSkB.setNyqrsrmny(chargeYs1.getNyqrsrmny());
			   ctChargeSkB.setNqsmny(chargeYs1.getNqsmny());
			   ctChargeSkB.setTaxRate(chargeYs1.getTaxRate());
			   ctChargeSkB.setLyvbillno(string);
			   ctChargeSkB.setVsrcid2(chargeYs1.getVsrcid2());
			   ctChargeSkB.setVsrcid2name(chargeYs1.getVsrcid2name());
			   ctChargeSkB.setNbcsrqrmny(chargeYs1.getNyqrsrmny());
			   Double double2 =chargeYs1.getNyqrsrmny()/(1+chargeYs1.getTaxRate()/100);
			   double2=(double) Math.round(double2 * 100) / 100;
			   ctChargeSkB.setNoTaxAmount(double2);
			   Double double3=ctChargeSkB.getNbcsrqrmny()-ctChargeSkB.getNoTaxAmount();
			   double3=(double) Math.round(double3 * 100) / 100;
			   ctChargeSkB.setTaxAmount(double3);
			   double1=double1+chargeYs1.getNyqrsrmny();
	           list.add(ctChargeSkB);
		   }
		   ctChargeSk.setCtChargeSrqrBList(list);
		   ctChargeSk.setNyqrsrmny(double1);
		   ctChargeSk.setNbcsrqrmny(double1);
		   return ctChargeSk;
		
	}
	

}