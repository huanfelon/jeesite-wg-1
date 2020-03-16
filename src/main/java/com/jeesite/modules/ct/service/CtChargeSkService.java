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
import com.jeesite.modules.ct.dao.CtChargeSkBDao;
import com.jeesite.modules.ct.dao.CtChargeSkDao;
import com.jeesite.modules.ct.entity.CtChargeSk;
import com.jeesite.modules.ct.entity.CtChargeSkB;
import com.jeesite.modules.ct.entity.CtChargeYs;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 收款单Service
 * @author GJ
 * @version 2019-11-05
 */
@Service
@Transactional(readOnly=true)
public class CtChargeSkService extends CrudService<CtChargeSkDao, CtChargeSk> {
	
	@Autowired
	private CtChargeSkBDao ctChargeSkBDao;
	@Autowired
	private CtChargeSkDao ctChargeSkDao;
	@Autowired
	private CtChargeYsService ctChargeYsService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取单条数据
	 * @param ctChargeSk
	 * @return
	 */
	@Override
	public CtChargeSk get(CtChargeSk ctChargeSk) {
		CtChargeSk entity = super.get(ctChargeSk);
		if (entity != null){
			CtChargeSkB ctChargeSkB = new CtChargeSkB(entity);
			ctChargeSkB.setStatus(CtChargeSkB.STATUS_NORMAL);
			entity.setCtChargeSkBList(ctChargeSkBDao.findList(ctChargeSkB));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param ctChargeSk 查询条件
	 * @param ctChargeSk.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtChargeSk> findPage(CtChargeSk ctChargeSk) {
		return super.findPage(ctChargeSk);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctChargeSk
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtChargeSk ctChargeSk) {
		super.save(ctChargeSk);
		// 保存 CtChargeSk子表
		for (CtChargeSkB ctChargeSkB : ctChargeSk.getCtChargeSkBList()){
			if (!CtChargeSkB.STATUS_DELETE.equals(ctChargeSkB.getStatus())){
				ctChargeSkB.setPkChargeSk(ctChargeSk);
				if (ctChargeSkB.getIsNewRecord()){
					ctChargeSkBDao.insert(ctChargeSkB);
				}else{
					ctChargeSkBDao.update(ctChargeSkB);
				}
			}else{
				ctChargeSkBDao.delete(ctChargeSkB);
			}
		}
	}
	
	/**
	 * 更新状态
	 * @param ctChargeSk
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtChargeSk ctChargeSk) {
		super.updateStatus(ctChargeSk);
	}
	
	/**
	 * 删除数据
	 * @param ctChargeSk
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtChargeSk ctChargeSk) {
		ctChargeSkDao.deleteData(ctChargeSk.getPkChargeSk());
		ctChargeSkBDao.deleteData(ctChargeSk.getPkChargeSk());
	//	super.delete(ctChargeSk);
	/*	CtChargeSkB ctChargeSkB = new CtChargeSkB();
		ctChargeSkB.setPkChargeSk(ctChargeSk);
		ctChargeSkBDao.deleteByEntity(ctChargeSkB);*/
	}
	//应收单穿透收款单转换
	public CtChargeSk refData(String pkChargeYs){
		   String [] pkStrings=pkChargeYs.split("-");
		   CtChargeYs chargeYs = ctChargeYsService.get(pkStrings[0]);
		   CtChargeSk ctChargeSk=new CtChargeSk();
		   ctChargeSk.setPkOrg(chargeYs.getPkOrg());
		   ctChargeSk.setPkDept(chargeYs.getPkDept());
		   ctChargeSk.setPkProject(chargeYs.getPkProject());
		   ctChargeSk.setPkCustomer(chargeYs.getPkCustomer());
		   ctChargeSk.setIszz(AbsEnumType.CT_CHARGE_iSZZ);
		   ctChargeSk.setSkdate(new Date());

		   //穿透制单转换表体
		   List<CtChargeSkB> list= new ArrayList<CtChargeSkB>(); 
		   for(String string:pkStrings){
			   CtChargeYs chargeYs1 = ctChargeYsService.get(string);
			   CtChargeSkB ctChargeSkB = new CtChargeSkB();
			   ctChargeSkB.setPkBuild(chargeYs1.getPkBuild());
			   ctChargeSkB.setPkHouse(chargeYs1.getPkHouse());
			   ctChargeSkB.setPkSfProject(chargeYs1.getPkSfProject());
			   ctChargeSkB.setYfdate(chargeYs1.getYfdate());
			   ctChargeSkB.setFyksdate(chargeYs1.getFyksdate());
			   ctChargeSkB.setFyjzdate(chargeYs1.getFyjzdate());
			   ctChargeSkB.setKjyears(chargeYs1.getKjyears());
			   ctChargeSkB.setNysmny(chargeYs1.getNysmny());
			   ctChargeSkB.setNys1mny(chargeYs1.getNys1mny());
			   ctChargeSkB.setNqsmny(chargeYs1.getNqsmny());
			   ctChargeSkB.setTaxRate(chargeYs1.getTaxRate());
			   ctChargeSkB.setLyvbillno(string);
			   ctChargeSkB.setVsrcid2(chargeYs1.getVsrcid2());
			   ctChargeSkB.setVsrcid2name(chargeYs1.getVsrcid2name());
	           list.add(ctChargeSkB);
		   }
		   ctChargeSk.setCtChargeSkBList(list);
		   return ctChargeSk;
		
	}
/*	//根据应收单主键查询数据(取消审批时有没有被收款单参照)
	public List<CtChargeSkB> getCtChargeSkByCode(String pkChargeSk){
		List<CtChargeSkB> ctChargeSks = ctChargeSkBDao.getCtChargeSkBByCode(pkChargeSk);
	    return ctChargeSks;
	}*/
	//审批收款单
	public void subData(String pks){
		CtChargeSk chargeSk =this.get(pks);
		chargeSk.setVbillstatus(AbsEnumType.BillStatus_SPTG);
		chargeSk.setApprover(UserUtils.getUser());
		chargeSk.setApprovetime(new Date());
		this.save(chargeSk);
		for(CtChargeSkB ctChargeSkB:chargeSk.getCtChargeSkBList()){
			//更新应收单已收金额，欠收金额
			String sql="update ct_charge_ys c set c.nys1mny =(select sum(nvl(s.nbcskmny,0)) "
					+ "from ct_charge_sk_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+ctChargeSkB.getLyvbillno()+"' "
					+ " and exists(select 1 from ct_charge_sk q where q.pk_charge_sk=s.pk_charge_sk "
					+ " and nvl(q.dr,0)=0 and q.vbillstatus=1)) ,c.nqsmny=c.nysmny-(select sum(nvl(s.nbcskmny,0)) "
					+ "from ct_charge_sk_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+ctChargeSkB.getLyvbillno()+"' "
					+ " and exists(select 1 from ct_charge_sk q where q.pk_charge_sk=s.pk_charge_sk "
					+ " and nvl(q.dr,0)=0 and q.vbillstatus=1)) where c.pk_charge_ys='"+ctChargeSkB.getLyvbillno()+"'";
			commonService.updateSql(sql);
			//更新合同页签业务拆分实收金额
			String sql1= "update wg_contract_ywcf f set f.nrealmny =(select sum(nvl(s.nbcskmny,0)) from ct_charge_sk_b "
					+ "s where nvl(s.dr,0)=0 and s.vsrcid2 ='"+ctChargeSkB.getVsrcid2()+"'"
							+ " and exists(select 1 from ct_charge_sk q where q.pk_charge_sk=s.pk_charge_sk "
							+ "and nvl(q.dr,0)=0 and q.vbillstatus=1)) where f.pk_contract_ywcf='"+ctChargeSkB.getVsrcid2()+"'";
			commonService.updateSql(sql1);
		}
	}
	//取消审批
	public boolean unSubData(String pks){
		//判断是不是最新一笔收款单
		Integer countInteger = ctChargeSkDao.getSkCountUnsub(pks);
		if (countInteger>0) {
			return false;
		}
 		CtChargeSk chargeSk = this.get(pks);
		chargeSk.setVbillstatus(AbsEnumType.BillStatus_ZY);
		chargeSk.setApprover(null);
		chargeSk.setApprovetime(null);
		this.save(chargeSk);
		for(CtChargeSkB ctChargeSkB:chargeSk.getCtChargeSkBList()){
			//更新应收单已收金额，欠收金额
			String sql="update ct_charge_ys c set c.nys1mny =(select nvl(sum(nvl(s.nbcskmny,0)),0) "
					+ "from ct_charge_sk_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+ctChargeSkB.getLyvbillno()+"' "
					+ " and exists(select 1 from ct_charge_sk q where q.pk_charge_sk=s.pk_charge_sk "
					+ " and nvl(q.dr,0)=0 and q.vbillstatus=1)) ,c.nqsmny=c.nysmny-(select nvl(sum(nvl(s.nbcskmny,0)),0) "
					+ "from ct_charge_sk_b s where nvl(s.dr,0)=0 and s.lyvbillno ='"+ctChargeSkB.getLyvbillno()+"' "
					+ " and exists(select 1 from ct_charge_sk q where q.pk_charge_sk=s.pk_charge_sk "
					+ " and nvl(q.dr,0)=0 and q.vbillstatus=1)) where c.pk_charge_ys='"+ctChargeSkB.getLyvbillno()+"'";
			commonService.updateSql(sql);
			
			//更新合同页签业务拆分实收金额
			String sql1= "update wg_contract_ywcf f set f.nrealmny =(select nvl(sum(nvl(s.nbcskmny,0)),0) from ct_charge_sk_b "
					+ "s where nvl(s.dr,0)=0 and s.vsrcid2 ='"+ctChargeSkB.getVsrcid2()+"'"
							+ " and exists(select 1 from ct_charge_sk q where q.pk_charge_sk=s.pk_charge_sk "
							+ "and nvl(q.dr,0)=0 and q.vbillstatus=1)) where f.pk_contract_ywcf='"+ctChargeSkB.getVsrcid2()+"'";
			commonService.updateSql(sql1);
		}
		return true;
	}
}