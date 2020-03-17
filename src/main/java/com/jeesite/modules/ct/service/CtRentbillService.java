/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.ct.dao.CtRentbillDao;
import com.jeesite.modules.ct.dao.CtRentbillSrftDao;
import com.jeesite.modules.ct.dao.CtRentbillZjmxDao;
import com.jeesite.modules.ct.entity.CtChargeYqrsr;
import com.jeesite.modules.ct.entity.CtChargeYs;
import com.jeesite.modules.ct.entity.CtRentbill;
import com.jeesite.modules.ct.entity.CtRentbillSrft;
import com.jeesite.modules.ct.entity.CtRentbillZjmx;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.wght.dao.WgContractSrcfDao;
import com.jeesite.modules.wght.entity.WgContractSrcf;

/**
 * 租约账单Service
 * @author tcl
 * @version 2019-11-08
 */
@Service
@Transactional(readOnly=true)
public class CtRentbillService extends CrudService<CtRentbillDao, CtRentbill> {
	
	@Autowired
	private CtRentbillSrftDao ctRentbillSrftDao;
	
	@Autowired
	private CtRentbillZjmxDao ctRentbillZjmxDao;
	
	@Autowired
	private CommonBaseService commonService;
	
	@Autowired
	private CtChargeYsService ysService;
	
	@Autowired
	private CtChargeYqrsrService qrService;
	@Autowired
	private WgContractSrcfDao wgContractSrcfDao;
	
	/**
	 * 获取单条数据
	 * @param ctRentbill
	 * @return
	 */
	@Override
	public CtRentbill get(CtRentbill ctRentbill) {
		CtRentbill entity = super.get(ctRentbill);
		if (entity != null){
			CtRentbillSrft ctRentbillSrft = new CtRentbillSrft(entity);
			ctRentbillSrft.setStatus(CtRentbillSrft.STATUS_NORMAL);
			entity.setCtRentbillSrftList(ctRentbillSrftDao.findList(ctRentbillSrft));
			CtRentbillZjmx ctRentbillZjmx = new CtRentbillZjmx(entity);
			ctRentbillZjmx.setStatus(CtRentbillZjmx.STATUS_NORMAL);
			entity.setCtRentbillZjmxList(ctRentbillZjmxDao.findList(ctRentbillZjmx));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param ctRentbill 查询条件
	 * @param ctRentbill.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtRentbill> findPage(CtRentbill ctRentbill) {
		return super.findPage(ctRentbill);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctRentbill
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtRentbill ctRentbill) {
		if(ctRentbill.getVbillstatus()==null){
			ctRentbill.setVbillstatus(AbsEnumType.BillStatus_ZY.toString());
		}
		if(ctRentbill.getIsNewRecord()){
			ctRentbill.setCreator(UserUtils.getUser());
			ctRentbill.setCreationtime(new Date());
		}else{
			ctRentbill.setModifier(UserUtils.getUser());
			ctRentbill.setModifiedtime(new Date());
		}
		ctRentbill.setDr(0);
		ctRentbill.setTs(new Date());
		
		//保存之前根据优惠金额反算财务拆分明细，不存在且不考虑删除子表逻辑
		for (CtRentbillZjmx ctRentbillZjmx : ctRentbill.getCtRentbillZjmxList()){
			Double uh=getUd(ctRentbillZjmx.getNyhmny());
			if(uh==new Double(0)){
				continue ;
			}
			//如果存在优惠金额，则按反向顺序调整对应收入分摊的优惠金额
			String ywid=ctRentbillZjmx.getVsrcid();
			Double restmny=uh;
			List<CtRentbillSrft> listsr=ctRentbill.getCtRentbillSrftList();
			for(int i=listsr.size()-1;i>-1;i--){
				CtRentbillSrft sr=listsr.get(i);
				String ywid2=sr.getVywcfid();
				if(!ywid.equals(ywid2)){
					continue ;
				}
				Double planmny=getUd(sr.getNplanmny());
				if(planmny>=restmny){//此行可以分配优惠金额
					sr.setNyhmny(restmny);
					sr.setNyqrmny(planmny-restmny);
					Double sl=getUd(sr.getTaxrate());
					sr.setNnotaxmny(sr.getNyqrmny()/(100+sl)*100);
					sr.setNtaxmny(sr.getNyqrmny()-sr.getNnotaxmny());
					restmny=0.00;
				}else{//优惠有剩余分配到下一条
					sr.setNyhmny(planmny);
					sr.setNyqrmny(0.00);
					sr.setNnotaxmny(0.00);
					sr.setNtaxmny(0.00);
					restmny=restmny-planmny;
				}
				
				if(restmny==new Double(0)){
					break ;
				}
			}
		}
		
		//重算账单表头金额
		Double ud1=new Double(0);
		Double ud2=new Double(0);
		Double ud3=new Double(0);
		for (CtRentbillZjmx ctRentbillZjmx : ctRentbill.getCtRentbillZjmxList()){
			ud1+=ctRentbillZjmx.getNysmny();
			ud2+=ctRentbillZjmx.getNnotaxmny();
			ud3=ud1-ud2;
			
		}
		ctRentbill.setNysmny(ud1);
		ctRentbill.setNnotaxmny(ud2);
		ctRentbill.setNtaxmny(ud3);
		
		super.save(ctRentbill);
		// 保存 CtRentbill子表
		for (CtRentbillSrft ctRentbillSrft : ctRentbill.getCtRentbillSrftList()){
			if (!CtRentbillSrft.STATUS_DELETE.equals(ctRentbillSrft.getStatus())){
				ctRentbillSrft.setPkRentbill(ctRentbill);
				ctRentbillSrft.setTs(new Date());
				ctRentbillSrft.setDr(0);
				if (ctRentbillSrft.getIsNewRecord()){
					ctRentbillSrftDao.insert(ctRentbillSrft);
				}else{
					ctRentbillSrftDao.update(ctRentbillSrft);
				}
			}else{
				ctRentbillSrftDao.delete(ctRentbillSrft);
			}
		}
		// 保存 CtRentbill子表
		for (CtRentbillZjmx ctRentbillZjmx : ctRentbill.getCtRentbillZjmxList()){
			if (!CtRentbillZjmx.STATUS_DELETE.equals(ctRentbillZjmx.getStatus())){
				ctRentbillZjmx.setPkRentbill(ctRentbill);
				ctRentbillZjmx.setTs(new Date());
				ctRentbillZjmx.setDr(0);
				if (ctRentbillZjmx.getIsNewRecord()){
					ctRentbillZjmxDao.insert(ctRentbillZjmx);
				}else{
					ctRentbillZjmxDao.update(ctRentbillZjmx);
				}
			}else{
				ctRentbillZjmxDao.delete(ctRentbillZjmx);
			}
		}
	}
	
	/**
	 * 更新状态
	 * @param ctRentbill
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtRentbill ctRentbill) {
		super.updateStatus(ctRentbill);
	}
	
	/**
	 * 删除数据
	 * @param ctRentbill
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtRentbill ctRentbill) {
		//更新语句，只修改dr=1
		String pk=ctRentbill.getPkRentbill();
		String delheadsql="update ct_rentbill set dr=1 where pk_rentbill='"+pk+"'";
		String delbodysql1="update ct_rentbill_srft set dr=1 where pk_rentbill='"+pk+"'";
		String delbodysql2="update ct_rentbill_zjmx set dr=1 where pk_rentbill='"+pk+"'";
		//更新合同业务拆分页签
		String htsql="update wg_contract_ywcf set vdef1='N' where pk_contract_ywcf in(select vsrcid from ct_rentbill_zjmx"
				+ " where pk_rentbill='"+pk+"' and nvl(dr,0)=0 )";
		//更新合同周期页签
		String htsql2="update wg_contract_wyfcf set vdef1='N' where pk_contract_wyfcf in(select vsrcid from ct_rentbill_zjmx"
				+ " where pk_rentbill='"+pk+"' and nvl(dr,0)=0 )";
		//更新合同物业拆分页签
		String htsql3="update wg_contract_zqfycf set vdef1='N' where pk_contract_zqfycf in(select vsrcid from ct_rentbill_zjmx"
				+ " where pk_rentbill='"+pk+"' and nvl(dr,0)=0 )";
		//更新合同收入拆分页签
		String htsql4="update wg_contract_srcf set vdef1='N' where pk_contract_srcf in(select vsrcid from ct_rentbill_srft"
				+ " where pk_rentbill='"+pk+"' and nvl(dr,0)=0 )";
		commonService.updateSql(htsql);
		commonService.updateSql(htsql2);
		commonService.updateSql(htsql3);
		commonService.updateSql(htsql4);
		commonService.updateSql(delheadsql);
		commonService.updateSql(delbodysql1);
		commonService.updateSql(delbodysql2);
	}
	
	/**
	 * 审批回写合同
	 * @param ctRentbill
	 */
	public void approveUpdateHtPlayMny(CtRentbill ctRentbill){
		
		//更新业务页签
		String sql="update wg_contract_ywcf f set f.nrecmny=(select sum(nvl(x.nysmny,0)) from ct_rentbill_zjmx "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_ywcf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_zjmx z where z.vsrcid=f.pk_contract_ywcf and nvl(z.dr,0)=0),"
				+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_zjmx x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_ywcf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_zjmx "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_ywcf) where exists (select 1 from ct_rentbill_zjmx b "
				+ "where b.vsrcid=f.pk_contract_ywcf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
		commonService.updateSql(sql);
		
		//更新物业页签
		String sql3="update wg_contract_wyfcf f set f.nrecmny=(select sum(nvl(x.nysmny,0)) from ct_rentbill_zjmx "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_wyfcf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_zjmx z where z.vsrcid=f.pk_contract_wyfcf and nvl(z.dr,0)=0),"
				+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_zjmx x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_wyfcf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_zjmx "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_wyfcf) where exists(select 1 from ct_rentbill_zjmx b "
				+ "where b.vsrcid=f.pk_contract_wyfcf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
		commonService.updateSql(sql3);
		
		//更新周期页签
		String sql4="update wg_contract_zqfycf f set f.nrecmny=(select sum(nvl(x.nysmny,0)) from ct_rentbill_zjmx "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_zqfycf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_zjmx z where z.vsrcid=f.pk_contract_zqfycf and nvl(z.dr,0)=0),"
				+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_zjmx x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_zqfycf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_zjmx "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_zqfycf) where exists(select 1 from ct_rentbill_zjmx b "
				+ "where b.vsrcid=f.pk_contract_zqfycf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
		commonService.updateSql(sql4);
		
		//更新财务页签
		String sql2="update wg_contract_srcf f set f.nrecmny=(select sum(nvl(x.nyqrmny,0)) from ct_rentbill_srft "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_srcf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_srft z where z.vsrcid=f.pk_contract_srcf and nvl(z.dr,0)=0),"
				+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_srft x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_srcf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_srft "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_srcf) where exists(select 1 from ct_rentbill_srft b "
				+ "where b.vsrcid=f.pk_contract_srcf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
		commonService.updateSql(sql2);
	}
	
	/**
	 * 取消审批回写合同
	 * @param ctRentbill
	 */
	public void unapproveUpdateHtPlayMny(CtRentbill ctRentbill){
		
		//更新业务页签
		String sql="update wg_contract_ywcf f set f.nrecmny=(select sum(nvl(x.nplanskmny,0)) from ct_rentbill_zjmx "
			+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_ywcf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_zjmx z where z.vsrcid=f.pk_contract_ywcf and nvl(z.dr,0)=0),"
			+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_zjmx x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_ywcf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_zjmx "
			+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_ywcf) where exists (select 1 from ct_rentbill_zjmx b "
			+ "where b.vsrcid=f.pk_contract_ywcf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
		commonService.updateSql(sql);
		
		//更新物业页签
		String sql3="update wg_contract_wyfcf f set f.nrecmny=(select sum(nvl(x.nplanskmny,0)) from ct_rentbill_zjmx "
			+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_wyfcf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_zjmx z where z.vsrcid=f.pk_contract_wyfcf and nvl(z.dr,0)=0),"
			+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_zjmx x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_wyfcf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_zjmx "
			+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_wyfcf) where exists(select 1 from ct_rentbill_zjmx b "
			+ "where b.vsrcid=f.pk_contract_wyfcf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
			commonService.updateSql(sql3);
			
		//更新周期页签
		String sql4="update wg_contract_zqfycf f set f.nrecmny=(select sum(nvl(x.nplanskmny,0)) from ct_rentbill_zjmx "
			+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_zqfycf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_zjmx z where z.vsrcid=f.pk_contract_zqfycf and nvl(z.dr,0)=0),"
			+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_zjmx x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_zqfycf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_zjmx "
			+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_zqfycf) where exists(select 1 from ct_rentbill_zjmx b "
			+ "where b.vsrcid=f.pk_contract_zqfycf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
			commonService.updateSql(sql4);
		
		//更新财务页签
		String sql2="update wg_contract_srcf f set f.nrecmny=(select sum(nvl(x.nplanmny,0)) from ct_rentbill_srft "
				+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_srcf),f.ntaxrate=(select to_number(z.taxrate) from ct_rentbill_srft z where z.vsrcid=f.pk_contract_srcf and nvl(z.dr,0)=0),"
			+ "f.nnotaxmny=(select sum(nvl(x.nnotaxmny,0)) from ct_rentbill_srft x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_srcf),f.ntaxmny=(select sum(nvl(x.ntaxmny,0)) from ct_rentbill_srft "
			+ "x where nvl(x.dr,0)=0 and x.vsrcid=f.pk_contract_srcf) where exists(select 1 from ct_rentbill_srft b "
				+ "where b.vsrcid=f.pk_contract_srcf and b.pk_rentbill='"+ctRentbill.getPkRentbill()+"') ";
		commonService.updateSql(sql2);
	}
	
	/**
	 * 审批推送待收入确认
	 */
	public void sendToDqr(CtRentbill ctRentbill){
		
		List<CtRentbillSrft> zjlist=ctRentbill.getCtRentbillSrftList();
		//房租
		for(CtRentbillSrft zj:zjlist){
			//期初或金额为0不传递
			if(zj.getIsQc().equals(AbsEnumType.ISQC)||zj.getNyqrmny().compareTo(new Double(0))==0){
				continue ;
			}
			CtChargeYqrsr ys=new CtChargeYqrsr();
			ys.setPkProject(ctRentbill.getPkProject());
			ys.setHtcode(ctRentbill.getHtcode());
			ys.setPkCustomer(ctRentbill.getPkCustomer());
			ys.setPkBuild(ctRentbill.getPkBuilding());
			ys.setPkHouse(ctRentbill.getPkHouse());
			ys.setPkSfProject(zj.getPkYsproject());
			ys.setFyksdate(zj.getDstartdate());
			ys.setFyjzdate(zj.getDenddate());
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			Date sdate=new Date();
			try {
				sdate = df.parse(zj.getKjqj());
			} catch (ParseException e) {
			}
			ys.setKjyears(sdate);
			ys.setNyqrsrmny(zj.getNyqrmny());
			ys.setTaxRate(zj.getTaxrate());
			ys.setNoTaxAmount(zj.getNnotaxmny());
			ys.setTaxAmount(zj.getNtaxmny());
			ys.setNsrqrmny(new Double(0));
			ys.setNqsmny(zj.getNnotaxmny());
			ys.setLyvbilltype(AbsEnumType.BillType_HT);
			ys.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			ys.setApprover(UserUtils.getUser());
			ys.setApprovetime(new Date());
			ys.setCreator(UserUtils.getUser());
			ys.setCreationtime(new Date());
			ys.setPkOrg(ctRentbill.getPkOrg());
			ys.setPkDept(ctRentbill.getPkDept());
			ys.setDr(0);
			ys.setTs(new Date());
			ys.setNqsmny(zj.getNyqrmny());
			ys.setVsrcid(zj.getPkRentbillSrft());
			ys.setVpid(ctRentbill.getPkRentbill());
			ys.setVsrcid2(zj.getVsrcid());
			ys.setVsrcid2name(zj.getVsrctbname());
			ys.setIszz(AbsEnumType.CT_CHARGE_iSZZ);
			qrService.save(ys);
		}
		//物业费及其他停车费
		for(CtRentbillZjmx zj : ctRentbill.getCtRentbillZjmxList()){
			if(zj.getVsrctbname().equals("pk_contract_ywcf")||zj.getIsQc().equals(AbsEnumType.ISQC)
					||zj.getNysmny().compareTo(new Double(0))==0){
				continue;
			}
			WgContractSrcf srcf=new WgContractSrcf();
			srcf.setVsrcid(zj.getVsrcid());
			List<WgContractSrcf> listwyfcf=wgContractSrcfDao.findList(srcf);
			for (WgContractSrcf sr : listwyfcf) {
				CtChargeYqrsr ys=new CtChargeYqrsr();
				ys.setPkProject(ctRentbill.getPkProject());
				ys.setHtcode(ctRentbill.getHtcode());
				ys.setPkCustomer(ctRentbill.getPkCustomer());
				ys.setPkBuild(ctRentbill.getPkBuilding());
				ys.setPkHouse(ctRentbill.getPkHouse());
				ys.setPkSfProject(sr.getPkCostproject());
				ys.setFyksdate(sr.getDstartdate());
				ys.setFyjzdate(sr.getDenddate());
				ys.setKjyears(sr.getDstartdate());
				ys.setNyqrsrmny(sr.getNrecmny());
				ys.setTaxRate(sr.getNtaxrate());
				ys.setNoTaxAmount(sr.getNnotaxmny());
				ys.setTaxAmount(sr.getNtaxmny());
				ys.setNsrqrmny(new Double(0));
				ys.setNqsmny(sr.getNnotaxmny());
				ys.setLyvbilltype(AbsEnumType.BillType_HT);
				ys.setVbillstatus(AbsEnumType.BillStatus_SPTG);
				ys.setApprover(UserUtils.getUser());
				ys.setApprovetime(new Date());
				ys.setCreator(UserUtils.getUser());
				ys.setCreationtime(new Date());
				ys.setPkOrg(ctRentbill.getPkOrg());
				ys.setPkDept(ctRentbill.getPkDept());
				ys.setDr(0);
				ys.setTs(new Date());
				ys.setNqsmny(sr.getNrecmny());
				ys.setVsrcid(sr.getPkContractSrcf());
				ys.setVpid(ctRentbill.getPkRentbill());
				ys.setVsrcid2(sr.getPkContractSrcf());
				ys.setVsrcid2name("pk_contract_srcf");
				ys.setIszz(AbsEnumType.CT_CHARGE_iSZZ);
				qrService.save(ys);
			}
		}
	}
	
	/**
	 * 审批推送应收单
	 */
	public void sendToYs(CtRentbill ctRentbill){
		
		List<CtRentbillZjmx> zjlist=ctRentbill.getCtRentbillZjmxList();
		for(CtRentbillZjmx zj:zjlist){
			//期初或者金额为0不传
			if(zj.getIsQc().equals(AbsEnumType.ISQC)||zj.getNysmny().compareTo(new Double(0))==0){
				continue ;
			}
			CtChargeYs ys=new CtChargeYs();
			ys.setPkProject(ctRentbill.getPkProject());
			ys.setHtcode(ctRentbill.getHtcode());
			ys.setPkCustomer(ctRentbill.getPkCustomer());
			ys.setPkBuild(ctRentbill.getPkBuilding());
			ys.setPkHouse(ctRentbill.getPkHouse());
			ys.setPkSfProject(zj.getPkYsproject());
			ys.setYfdate(zj.getDyfdate());
			ys.setFyksdate(zj.getDstartdate());
			ys.setFyjzdate(zj.getDenddate());
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			Date sdate=new Date();
			try {
				sdate = df.parse(zj.getKjqj());
			} catch (ParseException e) {
			}
			ys.setKjyears(sdate);
			ys.setNysmny(zj.getNysmny());
			ys.setTaxRate(zj.getTaxrate());
			ys.setNoTaxAmount(zj.getNnotaxmny());
			ys.setTaxAmount(zj.getNtaxmny());
			ys.setNys1mny(new Double(0.00));
			ys.setNqsmny(zj.getNysmny());
			ys.setLyvbilltype(AbsEnumType.BillType_HT);
			ys.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			ys.setApprover(UserUtils.getUser());
			ys.setApprovetime(new Date());
			ys.setCreator(UserUtils.getUser());
			ys.setCreationtime(new Date());
			ys.setPkOrg(ctRentbill.getPkOrg());
			ys.setPkDept(ctRentbill.getPkDept());
			ys.setDr(0);
			ys.setTs(new Date());
			ys.setVsrcid(zj.getPkRentbillZjmx());
			ys.setVsrcid2(zj.getVsrcid());
			ys.setVpid(ctRentbill.getPkRentbill());
			ys.setVsrcid2name(zj.getVsrctbname());
			ysService.save(ys);
		}
	}
	
	private Double getUd(Double obj){
		return obj==null?new Double(0):obj;
	}
	
}