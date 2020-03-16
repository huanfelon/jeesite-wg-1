/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.dao.BdCustomerKpxxDao;
import com.jeesite.modules.base.entity.BdCustomerKpxx;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.ct.dao.CtInvoiceapplyDao;
import com.jeesite.modules.ct.dao.CtInvoiceapplyKpxxDao;
import com.jeesite.modules.ct.dao.CtInvoiceapplySkmxDao;
import com.jeesite.modules.ct.entity.CtChargeYs;
import com.jeesite.modules.ct.entity.CtInvoiceapply;
import com.jeesite.modules.ct.entity.CtInvoiceapplyKpxx;
import com.jeesite.modules.ct.entity.CtInvoiceapplySkmx;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 开票申请Service
 * @author tcl
 * @version 2019-11-11
 */
@Service
@Transactional(readOnly=true)
public class CtInvoiceapplyService extends CrudService<CtInvoiceapplyDao, CtInvoiceapply> {
	
	@Autowired
	private CtInvoiceapplyKpxxDao ctInvoiceapplyKpxxDao;
	
	@Autowired
	private CtInvoiceapplySkmxDao ctInvoiceapplySkmxDao;
	
	@Autowired
	private CommonBaseService commonService;
	
	@Autowired
	private CtChargeYsService ysService;
	
	@Autowired
	private BdCustomerKpxxDao custkpDao;
	
	/**
	 * 获取单条数据
	 * @param ctInvoiceapply
	 * @return
	 */
	@Override
	public CtInvoiceapply get(CtInvoiceapply ctInvoiceapply) {
		CtInvoiceapply entity = super.get(ctInvoiceapply);
		if (entity != null){
			CtInvoiceapplyKpxx ctInvoiceapplyKpxx = new CtInvoiceapplyKpxx(entity);
			ctInvoiceapplyKpxx.setStatus(CtInvoiceapplyKpxx.STATUS_NORMAL);
			entity.setCtInvoiceapplyKpxxList(ctInvoiceapplyKpxxDao.findList(ctInvoiceapplyKpxx));
			CtInvoiceapplySkmx ctInvoiceapplySkmx = new CtInvoiceapplySkmx(entity);
			ctInvoiceapplySkmx.setStatus(CtInvoiceapplySkmx.STATUS_NORMAL);
			entity.setCtInvoiceapplySkmxList(ctInvoiceapplySkmxDao.findList(ctInvoiceapplySkmx));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param ctInvoiceapply 查询条件
	 * @param ctInvoiceapply.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtInvoiceapply> findPage(CtInvoiceapply ctInvoiceapply) {
		return super.findPage(ctInvoiceapply);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctInvoiceapply
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtInvoiceapply ctInvoiceapply) {
		if(ctInvoiceapply.getVbillstatus()==null){
			ctInvoiceapply.setVbillstatus(AbsEnumType.BillStatus_ZY);
		}
		if(ctInvoiceapply.getIsNewRecord()){
			ctInvoiceapply.setCreator(UserUtils.getUser());
			ctInvoiceapply.setCreationtime(new Date());
		}else{
			ctInvoiceapply.setModifier(UserUtils.getUser());
			ctInvoiceapply.setModifiedtime(new Date());
		}
		ctInvoiceapply.setDr(0);
		ctInvoiceapply.setTs(new Date());
		super.save(ctInvoiceapply);
		// 保存 CtInvoiceapply子表
		for (CtInvoiceapplyKpxx ctInvoiceapplyKpxx : ctInvoiceapply.getCtInvoiceapplyKpxxList()){
			if (!CtInvoiceapplyKpxx.STATUS_DELETE.equals(ctInvoiceapplyKpxx.getStatus())){
				ctInvoiceapplyKpxx.setPkInvoiceapply(ctInvoiceapply);
				ctInvoiceapplyKpxx.setTs(new Date());
				ctInvoiceapplyKpxx.setDr(0);
				if (ctInvoiceapplyKpxx.getIsNewRecord()){
					ctInvoiceapplyKpxxDao.insert(ctInvoiceapplyKpxx);
				}else{
					ctInvoiceapplyKpxxDao.update(ctInvoiceapplyKpxx);
				}
			}else{
				ctInvoiceapplyKpxxDao.delete(ctInvoiceapplyKpxx);
			}
		}
		// 保存 CtInvoiceapply子表
		for (CtInvoiceapplySkmx ctInvoiceapplySkmx : ctInvoiceapply.getCtInvoiceapplySkmxList()){
			if (!CtInvoiceapplySkmx.STATUS_DELETE.equals(ctInvoiceapplySkmx.getStatus())){
				ctInvoiceapplySkmx.setPkInvoiceapply(ctInvoiceapply);
				ctInvoiceapplySkmx.setTs(new Date());
				ctInvoiceapplySkmx.setDr(0);
				if (ctInvoiceapplySkmx.getIsNewRecord()){
					ctInvoiceapplySkmxDao.insert(ctInvoiceapplySkmx);
				}else{
					ctInvoiceapplySkmxDao.update(ctInvoiceapplySkmx);
				}
			}else{
				ctInvoiceapplySkmxDao.delete(ctInvoiceapplySkmx);
			}
		}
	}
	
	/**
	 * 更新状态
	 * @param ctInvoiceapply
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtInvoiceapply ctInvoiceapply) {
		super.updateStatus(ctInvoiceapply);
	}
	
	/**
	 * 删除数据
	 * @param ctInvoiceapply
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtInvoiceapply ctInvoiceapply) {
		//更新语句
		String pk=ctInvoiceapply.getPkInvoiceapply();
		String delheadsql="update ct_invoiceapply set dr=1 where pk_invoiceapply='"+pk+"'";
		String delbodysql1="update ct_invoiceapply_skmx set dr=1 where pk_invoiceapply='"+pk+"'";
		String delbodysql2="update ct_invoiceapply_kpxx set dr=1 where pk_invoiceapply='"+pk+"'";
		commonService.updateSql(delheadsql);
		commonService.updateSql(delbodysql1);
		commonService.updateSql(delbodysql2);
	}
	
	//开票申请参照应收单数据
	public CtInvoiceapply refAddFromYS(String pks){
		
		String [] pkStrings=pks.split("-");
		CtChargeYs chargeYs = ysService.get(pkStrings[0]);
		CtInvoiceapply ctapply=new CtInvoiceapply();
		ctapply.setCreator(UserUtils.getUser());
		ctapply.setCreationtime(new Date());
		ctapply.setDsqdate(new Date());
		ctapply.setPkSqr(UserUtils.getUser());
		ctapply.setVsrctype("应收单");
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String sdate=df.format(new Date()).replaceAll("-", "");
		String vbillno=CodeFactoryUtils.createBillCodeByDr("KPDJ"+sdate, "ct_invoiceapply", "vbillno", 5);
		ctapply.setVbillno(vbillno);
		ctapply.setVbillstatus(0);
		ctapply.setPkOrg(chargeYs.getPkOrg());
		ctapply.setPkDept(chargeYs.getPkDept());
		ctapply.setPkProject(chargeYs.getPkProject());
		Double ncount = new Double(0);
		
		List<CtInvoiceapplySkmx> list1= new ArrayList<CtInvoiceapplySkmx>(); 
		for(String string:pkStrings){
			CtChargeYs chargeYs1 = ysService.get(string);
			CtInvoiceapplySkmx skmx=new CtInvoiceapplySkmx();
			skmx.setPkBuilding(chargeYs1.getPkBuild());
			skmx.setPkCustomer(chargeYs1.getPkCustomer());
			skmx.setPkHouse(chargeYs1.getPkHouse());
			skmx.setPkYsproject(chargeYs1.getPkSfProject());
			skmx.setKpcostp(chargeYs1.getPkSfProject());
			skmx.setDstartdate(chargeYs1.getFyksdate());
			skmx.setDenddate(chargeYs1.getFyjzdate());
			skmx.setKjqj(df.format(chargeYs1.getKjyears()));
			skmx.setVsrcid(chargeYs1.getPkChargeYs());
			//查询已开票金额
			String sql="select sum(ks.nsqkpmny) from ct_invoiceapply_skmx ks where nvl(ks.dr,0)=0 and ks.vsrcid='"+chargeYs1.getPkChargeYs()+"'";
			Object obj=commonService.selectObject(sql);
			Double ud=obj==null?0:Double.parseDouble(obj.toString());
			skmx.setNsqkpmny(chargeYs1.getNysmny()-ud);
			skmx.setNsykpmny(skmx.getNsqkpmny());
			skmx.setTaxrate(chargeYs1.getTaxRate());
			skmx.setNnotaxmny(skmx.getNsqkpmny()/(skmx.getTaxrate()+100)*100);
			skmx.setNtaxmny(skmx.getNsqkpmny()-skmx.getNnotaxmny());
			skmx.setNkpdjmny(0.00);
			
			list1.add(skmx);
			ncount=ncount+skmx.getNsqkpmny();
		}
		ctapply.setCtInvoiceapplySkmxList(list1);
		ctapply.setNsqmny(ncount);
		
		List<CtInvoiceapplyKpxx> list2= new ArrayList<CtInvoiceapplyKpxx>();
		BdCustomerKpxx kpxx=new BdCustomerKpxx();
		kpxx.setPkCustomer(chargeYs.getPkCustomer());
		List<BdCustomerKpxx> kpxxlist=custkpDao.findList(kpxx);
		if(kpxxlist!=null&&kpxxlist.size()>0){
			CtInvoiceapplyKpxx kp=new CtInvoiceapplyKpxx();
			BdCustomerKpxx vo=kpxxlist.get(0);
			kp.setPkOrg(vo.getPkOrg());
			kp.setTaxno(vo.getTaxno());
			kp.setAddr(vo.getAddr());
			kp.setPhone(vo.getPhone());
			kp.setBankname(vo.getBankname());
			kp.setAccnum(vo.getAccnum());
			list2.add(kp);
			ctapply.setCtInvoiceapplyKpxxList(list2);
		}
		return ctapply;
	}
	
	public void approveUpdate(CtInvoiceapply ctapply){
		
		//审批之后回写金额
		List<CtInvoiceapplySkmx> sklist=ctapply.getCtInvoiceapplySkmxList();
		for(CtInvoiceapplySkmx sk:sklist){
			String sql3="update ct_charge_ys s set s.nkpmny=(select sum(nvl(b.nsqkpmny,0)) from "
					+ "ct_invoiceapply_skmx b left join ct_invoiceapply r on b.pk_invoiceapply=r.pk_invoiceapply "
					+ "where nvl(b.dr,0)=0 and nvl(r.dr,0)=0 and r.vbillstatus=1 and b.vsrcid='"+sk.getVsrcid()+"') "
					+ "where s.pk_charge_ys='"+sk.getVsrcid()+"' ";
			commonService.updateSql(sql3);
			
			//更新发票号
			String sql4="update ct_charge_ys s set s.kph=(select wm_concat(fpcode) from (select distinct r.fpcode from "
					+ "ct_invoiceapply_skmx b left join ct_invoiceapply r on b.pk_invoiceapply=r.pk_invoiceapply "
					+ "where nvl(b.dr,0)=0 and nvl(r.dr,0)=0 and r.vbillstatus=1 and b.vsrcid='"+sk.getVsrcid()+"' "
					+ "order by r.fpcode asc)) "
					+ "where s.pk_charge_ys='"+sk.getVsrcid()+"' ";
			commonService.updateSql(sql4);
		}
		
	}
	
}