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
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.ct.dao.CtInvoiceregisterDao;
import com.jeesite.modules.ct.dao.CtInvoiceregisterKpxxDao;
import com.jeesite.modules.ct.dao.CtInvoiceregisterSkmxDao;
import com.jeesite.modules.ct.entity.CtInvoiceapply;
import com.jeesite.modules.ct.entity.CtInvoiceapplyKpxx;
import com.jeesite.modules.ct.entity.CtInvoiceapplySkmx;
import com.jeesite.modules.ct.entity.CtInvoiceregister;
import com.jeesite.modules.ct.entity.CtInvoiceregisterKpxx;
import com.jeesite.modules.ct.entity.CtInvoiceregisterSkmx;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 开票登记Service
 * @author tcl
 * @version 2019-11-11
 */
@Service
@Transactional(readOnly=true)
public class CtInvoiceregisterService extends CrudService<CtInvoiceregisterDao, CtInvoiceregister> {
	
	@Autowired
	private CtInvoiceregisterKpxxDao ctInvoiceregisterKpxxDao;
	
	@Autowired
	private CtInvoiceregisterSkmxDao ctInvoiceregisterSkmxDao;
	
	@Autowired
	private CommonBaseService commonService;
	
	@Autowired
	private CtInvoiceapplyService applyService;
	
	/**
	 * 获取单条数据
	 * @param ctInvoiceregister
	 * @return
	 */
	@Override
	public CtInvoiceregister get(CtInvoiceregister ctInvoiceregister) {
		CtInvoiceregister entity = super.get(ctInvoiceregister);
		if (entity != null){
			CtInvoiceregisterKpxx ctInvoiceregisterKpxx = new CtInvoiceregisterKpxx(entity);
			ctInvoiceregisterKpxx.setStatus(CtInvoiceregisterKpxx.STATUS_NORMAL);
			entity.setCtInvoiceregisterKpxxList(ctInvoiceregisterKpxxDao.findList(ctInvoiceregisterKpxx));
			CtInvoiceregisterSkmx ctInvoiceregisterSkmx = new CtInvoiceregisterSkmx(entity);
			ctInvoiceregisterSkmx.setStatus(CtInvoiceregisterSkmx.STATUS_NORMAL);
			entity.setCtInvoiceregisterSkmxList(ctInvoiceregisterSkmxDao.findList(ctInvoiceregisterSkmx));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param ctInvoiceregister 查询条件
	 * @param ctInvoiceregister.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtInvoiceregister> findPage(CtInvoiceregister ctInvoiceregister) {
		return super.findPage(ctInvoiceregister);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctInvoiceregister
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtInvoiceregister ctInvoiceregister) {
		if(ctInvoiceregister.getVbillstatus()==null){
			ctInvoiceregister.setVbillstatus(AbsEnumType.BillStatus_ZY);
		}
		if(ctInvoiceregister.getIsNewRecord()){
			ctInvoiceregister.setCreator(UserUtils.getUser());
			ctInvoiceregister.setCreationtime(new Date());
		}else{
			ctInvoiceregister.setModifier(UserUtils.getUser());
			ctInvoiceregister.setModifiedtime(new Date());
		}
		ctInvoiceregister.setDr(0);
		ctInvoiceregister.setTs(new Date());
		super.save(ctInvoiceregister);
		// 保存 CtInvoiceregister子表
		for (CtInvoiceregisterKpxx ctInvoiceregisterKpxx : ctInvoiceregister.getCtInvoiceregisterKpxxList()){
			if (!CtInvoiceregisterKpxx.STATUS_DELETE.equals(ctInvoiceregisterKpxx.getStatus())){
				ctInvoiceregisterKpxx.setPkInvoiceregister(ctInvoiceregister);
				ctInvoiceregisterKpxx.setTs(new Date());
				ctInvoiceregisterKpxx.setDr(0);
				if (ctInvoiceregisterKpxx.getIsNewRecord()){
					ctInvoiceregisterKpxxDao.insert(ctInvoiceregisterKpxx);
				}else{
					ctInvoiceregisterKpxxDao.update(ctInvoiceregisterKpxx);
				}
			}else{
				ctInvoiceregisterKpxxDao.delete(ctInvoiceregisterKpxx);
			}
		}
		// 保存 CtInvoiceregister子表
		for (CtInvoiceregisterSkmx ctInvoiceregisterSkmx : ctInvoiceregister.getCtInvoiceregisterSkmxList()){
			if (!CtInvoiceregisterSkmx.STATUS_DELETE.equals(ctInvoiceregisterSkmx.getStatus())){
				ctInvoiceregisterSkmx.setPkInvoiceregister(ctInvoiceregister);
				ctInvoiceregisterSkmx.setTs(new Date());
				ctInvoiceregisterSkmx.setDr(0);
				if (ctInvoiceregisterSkmx.getIsNewRecord()){
					ctInvoiceregisterSkmxDao.insert(ctInvoiceregisterSkmx);
				}else{
					ctInvoiceregisterSkmxDao.update(ctInvoiceregisterSkmx);
				}
			}else{
				ctInvoiceregisterSkmxDao.delete(ctInvoiceregisterSkmx);
			}
		}
	}
	
	/**
	 * 更新状态
	 * @param ctInvoiceregister
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtInvoiceregister ctInvoiceregister) {
		super.updateStatus(ctInvoiceregister);
	}
	
	/**
	 * 删除数据
	 * @param ctInvoiceregister
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtInvoiceregister ctInvoiceregister) {
		//更新语句
		String pk=ctInvoiceregister.getPkInvoiceregister();
		String delheadsql="update ct_invoiceregister set dr=1 where pk_invoiceregister='"+pk+"'";
		String delbodysql1="update ct_invoiceregister_skmx set dr=1 where pk_invoiceregister='"+pk+"'";
		String delbodysql2="update ct_invoiceregister_kpxx set dr=1 where pk_invoiceregister='"+pk+"'";
		commonService.updateSql(delheadsql);
		commonService.updateSql(delbodysql1);
		commonService.updateSql(delbodysql2);
	}
	
	//开票登记参照开票申请数据
	public CtInvoiceregister refAddFromKPSQ(String pks){
		
		String [] pkStrings=pks.split("-");
		CtInvoiceapply chargeYs = applyService.get(pkStrings[0]);
		//参照第一条
		CtInvoiceregister ctapply=new CtInvoiceregister();
		ctapply.setCreator(UserUtils.getUser());
		ctapply.setCreationtime(new Date());
		ctapply.setDkpdate(new Date());
		ctapply.setPkKpr(UserUtils.getUser());
		ctapply.setVsrctype("开票申请");
		ctapply.setVsrcno(chargeYs.getVbillno());
		ctapply.setVsrcid(chargeYs.getPkInvoiceapply());
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String sdate=df.format(new Date()).replaceAll("-", "");
		String vbillno=CodeFactoryUtils.createBillCodeByDr("KPDJ"+sdate, "ct_invoiceregister", "vbillno", 5);
		ctapply.setVbillno(vbillno);
		ctapply.setVbillstatus(0);
		ctapply.setPkOrg(chargeYs.getPkOrg());
		ctapply.setPkDept(chargeYs.getPkDept());
		ctapply.setPkProject(chargeYs.getPkProject());
		Double ncount = new Double(0);
		
		List<CtInvoiceregisterSkmx> list=new ArrayList<CtInvoiceregisterSkmx>();
		List<CtInvoiceapplySkmx> list1= chargeYs.getCtInvoiceapplySkmxList(); 
		for(CtInvoiceapplySkmx appmx:list1){
			CtInvoiceregisterSkmx skmx=new CtInvoiceregisterSkmx();
			skmx.setPkBuilding(appmx.getPkBuilding());
			skmx.setPkCustomer(appmx.getPkCustomer());
			skmx.setPkHouse(appmx.getPkHouse());
			skmx.setPkYsproject(appmx.getPkYsproject());
			skmx.setDstartdate(appmx.getDstartdate());
			skmx.setDenddate(appmx.getDenddate());
			skmx.setKjqj(appmx.getKjqj());
			skmx.setVsrcid(appmx.getPkInvoiceapplySkmx());
			
			//查询已开票金额
			String sql="select sum(ks.nkpmny) from ct_invoiceregister_skmx ks where nvl(ks.dr,0)=0 and ks.vsrcid='"+appmx.getPkInvoiceapplySkmx()+"'";
			Object obj=commonService.selectObject(sql);
			Double ud=obj==null?0:Double.parseDouble(obj.toString());
			skmx.setNsykpmny(appmx.getNsqkpmny()-ud);
			skmx.setNkpmny(skmx.getNsykpmny());
			skmx.setTaxrate(appmx.getTaxrate());
			skmx.setNnotaxmny(skmx.getNkpmny()/(skmx.getTaxrate()+100)*100);
			skmx.setNtaxmny(skmx.getNkpmny()-skmx.getNnotaxmny());
			
			list.add(skmx);
			ncount=ncount+skmx.getNkpmny();
		}
		ctapply.setCtInvoiceregisterSkmxList(list);
		ctapply.setNkpmny(ncount);
		
		List<CtInvoiceregisterKpxx> listnew=new ArrayList<CtInvoiceregisterKpxx>();
		List<CtInvoiceapplyKpxx> list2= chargeYs.getCtInvoiceapplyKpxxList();
		if(list2!=null&&list2.size()>0){
			CtInvoiceapplyKpxx vo=list2.get(0);
			CtInvoiceregisterKpxx kpxx=new CtInvoiceregisterKpxx();
			kpxx.setPkOrg(vo.getPkOrg());
			kpxx.setTaxno(vo.getTaxno());
			kpxx.setAddr(vo.getAddr());
			kpxx.setPhone(vo.getPhone());
			kpxx.setBankname(vo.getBankname());
			kpxx.setAccnum(vo.getAccnum());
			listnew.add(kpxx);
			ctapply.setCtInvoiceregisterKpxxList(listnew);
		}
		return ctapply;
	}
	
	public void approveUpdate(CtInvoiceregister ctInvoiceregister){
		
		//审批之后回写金额
		List<CtInvoiceregisterSkmx> sklist=ctInvoiceregister.getCtInvoiceregisterSkmxList();
		for(CtInvoiceregisterSkmx sk:sklist){
			//开票申请id
			String kpid=sk.getVsrcid();
			String sql=" update ct_invoiceapply_skmx a set a.nkpdjmny=(select sum(nvl(b.nkpmny,0)) "
			+ "from ct_invoiceregister_skmx b left join ct_invoiceregister r on b.pk_invoiceregister=r.pk_invoiceregister "
			+ " where nvl(b.dr,0)=0 and nvl(r.dr,0)=0 and r.vbillstatus=1 and b.vsrcid='"+kpid+"') "
			+ "where a.pk_invoiceapply_skmx='"+kpid+"' ";
			commonService.updateSql(sql);
			
			//根据开票申请id查应收id
			String sql2="select vsrcid from ct_invoiceapply_skmx where nvl(dr,0)=0 and pk_invoiceapply_skmx='"+kpid+"'";
			Object obj=commonService.selectObject(sql2);
			if(obj==null||StringUtils.isEmpty(obj.toString())){//自制开票申请，不更新应收单
				continue ;
			}
			String ysid=obj.toString();
			String sql3="update ct_charge_ys s set s.nkpmny=(select sum(nvl(b.nkpmny,0)) from "
			+ "ct_invoiceregister_skmx b left join ct_invoiceregister r on b.pk_invoiceregister=r.pk_invoiceregister"
			+ " left join ct_invoiceapply_skmx x on x.pk_invoiceapply_skmx=b.vsrcid "
			+ "where nvl(b.dr,0)=0 and nvl(r.dr,0)=0 and nvl(x.dr,0)=0 and r.vbillstatus=1 and x.vsrcid='"+ysid+"')"
			+ " where s.pk_charge_ys='"+ysid+"' ";
			commonService.updateSql(sql3);
			
			//更新发票号
			String sql4="update ct_charge_ys s set s.kph=(select wm_concat(vbillno) from(select distinct r.vbillno from "
			+ "ct_invoiceregister_skmx b left join ct_invoiceregister r on b.pk_invoiceregister=r.pk_invoiceregister"
			+ " left join ct_invoiceapply_skmx x on x.pk_invoiceapply_skmx=b.vsrcid "
			+ "where nvl(b.dr,0)=0 and nvl(r.dr,0)=0 and nvl(x.dr,0)=0 and r.vbillstatus=1 and x.vsrcid='"+ysid+"'"
			+ " order by r.vbillno asc) )"
			+ " where s.pk_charge_ys='"+ysid+"' ";
			commonService.updateSql(sql4);
		}
		
	}
	
}