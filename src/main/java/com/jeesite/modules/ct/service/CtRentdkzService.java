/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.dao.CommonBaseDao;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.ct.dao.CtRentdkzDao;
import com.jeesite.modules.ct.entity.CtChargeYs;
import com.jeesite.modules.ct.entity.CtRentbill;
import com.jeesite.modules.ct.entity.CtRentbillSrft;
import com.jeesite.modules.ct.entity.CtRentbillZjmx;
import com.jeesite.modules.ct.entity.CtRentdkz;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.wght.config.CalendarUtls;
import com.jeesite.modules.wght.dao.WgContractDao;
import com.jeesite.modules.wght.dao.WgContractSrcfDao;
import com.jeesite.modules.wght.dao.WgContractWyfcfDao;
import com.jeesite.modules.wght.dao.WgContractYjDao;
import com.jeesite.modules.wght.dao.WgContractYwcfDao;
import com.jeesite.modules.wght.dao.WgContractZqfycfDao;
import com.jeesite.modules.wght.entity.WgContract;
import com.jeesite.modules.wght.entity.WgContractSrcf;
import com.jeesite.modules.wght.entity.WgContractWyfcf;
import com.jeesite.modules.wght.entity.WgContractYj;
import com.jeesite.modules.wght.entity.WgContractYwcf;
import com.jeesite.modules.wght.entity.WgContractZqfycf;

/**
 * 租约待开账Service
 * @author tcl
 * @version 2019-11-06
 */
@Service
@Transactional(readOnly=true)
public class CtRentdkzService extends CrudService<CtRentdkzDao, CtRentdkz> {
	
	@Autowired
	private CommonBaseDao baseDao;
	
	@Autowired
	private CommonBaseService  baseService;
	
	@Autowired
	private WgContractDao htDao;
	
	@Autowired
	private WgContractYwcfDao ywcfDao;
	
	@Autowired
	private WgContractWyfcfDao wyfcfDao;
	
	@Autowired
	private WgContractZqfycfDao zqcfDao;
	
	@Autowired
	private WgContractSrcfDao cwcfDao;
	
	@Autowired
	private WgContractYjDao yjDao;
	
	@Autowired
	private CtRentbillService rentService;
	
	@Autowired
	private CtChargeYsService ysService;
	
	/**
	 * 获取单条数据
	 * @param ctRentdkz
	 * @return
	 */
	@Override
	public CtRentdkz get(CtRentdkz ctRentdkz) {
		return super.get(ctRentdkz);
	}
	
	/**
	 * 查询分页数据
	 * @param ctRentdkz 查询条件
	 * @param ctRentdkz.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtRentdkz> findPage(CtRentdkz ctRentdkz) {
		return super.findPage(ctRentdkz);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctRentdkz
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtRentdkz ctRentdkz) {
		//更新时间戳
		ctRentdkz.setTs(new Date());
		super.save(ctRentdkz);
	}
	
	/**
	 * 更新状态
	 * @param ctRentdkz
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtRentdkz ctRentdkz) {
		super.updateStatus(ctRentdkz);
	}
	
	/**
	 * 删除数据
	 * @param ctRentdkz
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtRentdkz ctRentdkz) {
		super.delete(ctRentdkz);
	}
	
	/**
	 * 查询分页数据
	 * @return
	 */
	public Page<CtRentdkz> findPageData(CtRentdkz ctRentdkz,String busidate) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		try {
			//获取业务日期
			Date sdate=df.parse(busidate);
			/*//获取提前开账天数参数
			Object days=Global.getConfig("zl_preday");//获取参数设置
			int preday=days==null?0:new Integer(days.toString());
			String kzdate=df.format(CalendarUtls.getBeforeNDay(sdate, preday));*/
			String kzdate=df.format(sdate);
			//1.查询合同业务拆分,押金，周期费用拆分，物业费拆分，在当前时间节点未标记的数据的合同
			//2.保证金不需要根据合同日期提前
			
			String where="  1=1 and exists(select 1 from wg_contract g where g.pk_contract=a.pk_contract and nvl(g.dr,0)=0 "
					+ "and g.vbillstatus=1 and (exists(select 1 from wg_contract_ywcf f where f.pk_contract=g.pk_contract "
					+ "and nvl(f.dr,0)=0 and nvl(f.vdef1,'N')<>'Y' and to_char(f.dstartdate,'yyyy-mm-dd')<=to_char(to_date('"+kzdate+"','yyyy-mm-dd'),'yyyy-mm-dd') ) "
							+ " or exists(select 1 from wg_contract_yj f where f.pk_contract=g.pk_contract "
					+ "and nvl(f.dr,0)=0 and nvl(f.vdef1,'N')<>'Y' and to_char(f.drecdate,'yyyy-mm-dd')<=to_char(to_date('"+kzdate+"','yyyy-mm-dd'),'yyyy-mm-dd') )"
							+ " or exists(select 1 from wg_contract_wyfcf f where f.pk_contract=g.pk_contract "
					+ "and nvl(f.dr,0)=0 and nvl(f.vdef1,'N')<>'Y' and to_char(f.dstartdate,'yyyy-mm-dd')<=to_char(to_date('"+kzdate+"','yyyy-mm-dd'),'yyyy-mm-dd') )"
							+ " or exists(select 1 from wg_contract_zqfycf f where f.pk_contract=g.pk_contract "
					+ "and nvl(f.dr,0)=0 and nvl(f.vdef1,'N')<>'Y' and to_char(f.dstartdate,'yyyy-mm-dd')<='"+kzdate+"'))  )";
			
			ctRentdkz.getSqlMap().getDataScope().addFilter("dsf",where);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return super.findPage(ctRentdkz);
	}
	
	/**
	 * 批量开账
	 * @param pkzds
	 * @param busidate
	 * @return
	 */
	public String batchOpening(String[] pkzds, String busidate) {
		
		String result="";
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			//获取业务日期
			Date sdate=df.parse(busidate);
			//开账处理====生成对应的租约账单
			/*//获取提前开账天数参数
			Object days=Global.getConfig("zl_preday");//获取参数设置
			int preday=days==null?0:new Integer(days.toString());
			Date kzdate1=CalendarUtls.getBeforeNDay(sdate, preday);*/
			String kzdate=df.format(sdate);
			//获取期初期间参数
			Object qc=Global.getConfig("zl_qcdate");//获取参数设置
			Date qcdate=df.parse(qc==null?"2019-01-01":qc.toString());
			//根据合同个数生成对应的开账单
			for (String pk : pkzds) {
				CtRentdkz dkz1=new CtRentdkz();
				dkz1.setPkRentdkz(pk);
				CtRentdkz dkz2=this.get(dkz1);
				
				//重新构造Dao,防止查询子表
				WgContract convo2=new WgContract();
				convo2.setPkContract(dkz2.getPkContract());
				List<WgContract> list=htDao.findList(convo2);
				WgContract convo=list.get(0);
				//设置开账日期提前天数
				Integer day=0;
				if(convo.getDfirstfkdate()!=null){
					day=CalendarUtls.getBetweenTwoDays2(convo.getDfirstfkdate(), convo.getDstartdate());
				}
				//设置表头
				CtRentbill ctbill=new CtRentbill();
				ctbill.setPkOrg(dkz2.getPkOrg());
				ctbill.setPkProject(dkz2.getPkProject());
				ctbill.setPkCustomer(dkz2.getPkCustomer());
				ctbill.setPkBuilding(dkz2.getPkBuilding());
				ctbill.setPkHouse(dkz2.getPkHouse());
				String dadastr=busidate.replaceAll("-", "");
				String vbillno=CodeFactoryUtils.createBillCodeByDr("ZYZD"+dadastr, "ct_rentbill", "vbillno", 5);
				ctbill.setVbillno(vbillno);
				ctbill.setHtcode(dkz2.getHtcode());
				ctbill.setPkDept(dkz2.getPkDept());
				ctbill.setVsrcid(dkz2.getPkContract());
				ctbill.setVsrcno(convo.getVbillno());
				ctbill.setVbillstatus(AbsEnumType.BillStatus_ZY.toString());
				ctbill.setCreator(UserUtils.getUser());
				ctbill.setCreationtime(new Date());
				ctbill.setDr(0);
				ctbill.setTs(new Date());
				ctbill.setDbilldate(sdate);
				
				//根据日期查找合同业务拆分数据
				WgContractYwcf ywcf=new WgContractYwcf();
				ywcf.getSqlMap().getDataScope().addFilter("dsf", " 1=1 and a.pk_contract='"+dkz2.getPkContract()+"'"
						+ " and nvl(a.dr,0)=0 and nvl(a.vdef1,'N')='N' and to_char(a.dstartdate,'yyyy-mm-dd')<=to_char(to_date('"+kzdate+"','yyyy-mm-dd'),'yyyy-mm-dd') ");
				
				List<WgContractYwcf> ywList=ywcfDao.findList(ywcf);
				List<CtRentbillZjmx> list_zjmx=new ArrayList<CtRentbillZjmx>();
				String ywstr="";
				Double ysmny=new Double(0);
				Double taxmny=new Double(0);
				Double notaxmny=new Double(0);
				for(int i=0;i<ywList.size();i++){
					WgContractYwcf yw=ywList.get(i);
					CtRentbillZjmx zj=new CtRentbillZjmx();
					Date start=yw.getDstartdate();
					if(start.before(qcdate)){
						zj.setIsQc("Y");
					}else{
						zj.setIsQc("N");
					}
					zj.setPkYsproject(yw.getPkCostproject());
					if(yw.getDstartdate().compareTo(convo.getDstartdate())==0){//第一行
						zj.setDyfdate(CalendarUtls.getBeforeNDay(yw.getDstartdate(), day));
					}else{
						zj.setDyfdate(CalendarUtls.getBeforeNDay(yw.getDstartdate(), 15));
					}
					zj.setDstartdate(yw.getDstartdate());
					zj.setDenddate(yw.getDenddate());
					zj.setKjqj(df.format(yw.getDstartdate()).substring(0,7));
					zj.setNplanskmny(getUfd(yw.getNrecmny()));
					zj.setNyhmny(0.00);
					zj.setNysmny(getUfd(yw.getNrecmny()));
					zj.setNtaxmny(getUfd(yw.getNtaxmny()));
					zj.setNnotaxmny(getUfd(yw.getNnotaxmny()));
					zj.setTaxrate(getUfd(yw.getNtaxrate()));
					
					ysmny=ysmny+getUfd(yw.getNrecmny());
					taxmny=taxmny+getUfd(yw.getNtaxmny());
					notaxmny=notaxmny+getUfd(yw.getNnotaxmny());
					zj.setTs(new Date());
					zj.setDr(0);
					zj.setVsrcid(yw.getPkContractYwcf());
					zj.setVsrctbname("pk_contract_ywcf");
					list_zjmx.add(zj);
					ywstr+="'"+yw.getPkContractYwcf()+"',";
				}
				
				//根据日期查找物业业务拆分数据
				WgContractWyfcf wyfcf=new WgContractWyfcf();
				wyfcf.getSqlMap().getDataScope().addFilter("dsf", " 1=1 and a.pk_contract='"+dkz2.getPkContract()+"'"
						+ " and nvl(a.dr,0)=0 and nvl(a.vdef1,'N')='N' and to_char(a.dstartdate,'yyyy-mm-dd')<=to_char(to_date('"+kzdate+"','yyyy-mm-dd'),'yyyy-mm-dd') ");
				
				String wyfstr="";
				List<WgContractWyfcf> wyfList=wyfcfDao.findList(wyfcf);
				for(int i=0;i<wyfList.size();i++){
					WgContractWyfcf yw=wyfList.get(i);
					CtRentbillZjmx zj=new CtRentbillZjmx();
					Date start=yw.getDstartdate();
					if(start.before(qcdate)){
						zj.setIsQc("Y");
					}else{
						zj.setIsQc("N");
					}
					zj.setPkYsproject(yw.getPkCostproject());
					if(yw.getDstartdate().compareTo(convo.getDstartdate())==0){//第一行
						zj.setDyfdate(CalendarUtls.getBeforeNDay(yw.getDstartdate(), day));
					}else{
						zj.setDyfdate(CalendarUtls.getBeforeNDay(yw.getDstartdate(), 15));
					}
					zj.setDstartdate(yw.getDstartdate());
					zj.setDenddate(yw.getDenddate());
					zj.setKjqj(df.format(yw.getDstartdate()).substring(0,7));
					zj.setNplanskmny(getUfd(yw.getNrecmny()));
					zj.setNyhmny(0.00);
					zj.setNysmny(getUfd(yw.getNrecmny()));
					zj.setNtaxmny(getUfd(yw.getNtaxmny()));
					zj.setNnotaxmny(getUfd(yw.getNnotaxmny()));
					zj.setTaxrate(getUfd(yw.getNtaxrate()));
					
					ysmny=ysmny+getUfd(yw.getNrecmny());
					taxmny=taxmny+getUfd(yw.getNtaxmny());
					notaxmny=notaxmny+getUfd(yw.getNnotaxmny());
					zj.setTs(new Date());
					zj.setDr(0);
					zj.setVsrcid(yw.getPkContractWyfcf());
					zj.setVsrctbname("pk_contract_wyfcf");
					list_zjmx.add(zj);
					wyfstr+="'"+yw.getPkContractWyfcf()+"',";
				}
				
				//根据日期查找周期业务拆分数据
				WgContractZqfycf zqfcf=new WgContractZqfycf();
				zqfcf.getSqlMap().getDataScope().addFilter("dsf", " 1=1 and a.pk_contract='"+dkz2.getPkContract()+"'"
						+ " and nvl(a.dr,0)=0 and nvl(a.vdef1,'N')='N' and to_char(a.dstartdate,'yyyy-mm-dd')<=to_char(to_date('"+kzdate+"','yyyy-mm-dd'),'yyyy-mm-dd') ");
				
				String zqstr="";
				List<WgContractZqfycf> zqfList=zqcfDao.findList(zqfcf);
				for(int i=0;i<zqfList.size();i++){
					WgContractZqfycf yw=zqfList.get(i);
					CtRentbillZjmx zj=new CtRentbillZjmx();
					Date start=yw.getDstartdate();
					if(start.before(qcdate)){
						zj.setIsQc("Y");
					}else{
						zj.setIsQc("N");
					}
					zj.setPkYsproject(yw.getPkCostproject());
					if(yw.getDstartdate().compareTo(convo.getDstartdate())==0){//第一行
						zj.setDyfdate(CalendarUtls.getBeforeNDay(yw.getDstartdate(), day));
					}else{
						zj.setDyfdate(CalendarUtls.getBeforeNDay(yw.getDstartdate(), 15));
					}
					zj.setDstartdate(yw.getDstartdate());
					zj.setDenddate(yw.getDenddate());
					zj.setKjqj(df.format(yw.getDstartdate()).substring(0,7));
					zj.setNplanskmny(getUfd(yw.getNrecmny()));
					zj.setNyhmny(0.00);
					zj.setNysmny(getUfd(yw.getNrecmny()));
					zj.setNtaxmny(getUfd(yw.getNtaxmny()));
					zj.setNnotaxmny(getUfd(yw.getNnotaxmny()));
					zj.setTaxrate(getUfd(yw.getNtaxrate()));
					
					ysmny=ysmny+getUfd(yw.getNrecmny());
					taxmny=taxmny+getUfd(yw.getNtaxmny());
					notaxmny=notaxmny+getUfd(yw.getNnotaxmny());
					zj.setTs(new Date());
					zj.setDr(0);
					zj.setVsrcid(yw.getPkContractZqfycf());
					zj.setVsrctbname("pk_contract_zqfycf");
					list_zjmx.add(zj);
					zqstr+="'"+yw.getPkContractZqfycf()+"',";
				}
				
				ctbill.setNysmny(ysmny);
				ctbill.setNtaxmny(taxmny);
				ctbill.setNnotaxmny(notaxmny);
				ctbill.setCtRentbillZjmxList(list_zjmx);
				
				//根据日期查找收入拆分数据
				WgContractSrcf cwcf=new WgContractSrcf();
				cwcf.getSqlMap().getDataScope().addFilter("dsf", " 1=1 and a.pk_contract='"+dkz2.getPkContract()+"'"
						+ " and nvl(a.dr,0)=0 and nvl(a.vdef1,'N')='N' and to_char(a.dstartdate,'yyyy-mm-dd')<=to_char(to_date('"+kzdate+"','yyyy-mm-dd'),'yyyy-mm-dd') ");
				String srstr="";
				List<WgContractSrcf> cwList=cwcfDao.findList(cwcf);
				List<CtRentbillSrft> list_srft=new ArrayList<CtRentbillSrft>();
				for(int i=0;i<cwList.size();i++){
					WgContractSrcf yw=cwList.get(i);
					String vsrctype=yw.getVsrctype();
					if(!vsrctype.equals("房租")){
						continue;
					}
					CtRentbillSrft sr=new CtRentbillSrft();
					Date start=yw.getDstartdate();
					if(start.before(qcdate)){
						sr.setIsQc("Y");
					}else{
						sr.setIsQc("N");
					}
					sr.setPkYsproject(yw.getPkCostproject());
					sr.setDstartdate(yw.getDstartdate());
					sr.setDenddate(yw.getDenddate());
					sr.setKjqj(df.format(yw.getDstartdate()).substring(0,7));
					sr.setNyqrmny(getUfd(yw.getNrecmny()));
					sr.setNplanmny(getUfd(yw.getNrecmny()));
					sr.setNyhmny(0.00);
					sr.setNtaxmny(getUfd(yw.getNtaxmny()));
					sr.setNnotaxmny(getUfd(yw.getNnotaxmny()));
					sr.setTaxrate(getUfd(yw.getNtaxrate()));
					sr.setTs(new Date());
					sr.setDr(0);
					sr.setVsrcid(yw.getPkContractSrcf());
					sr.setVywcfid(yw.getVsrcid());//插入业务拆分的id
					sr.setVsrctbname("pk_contract_srcf");
					list_srft.add(sr);
					srstr+="'"+yw.getPkContractSrcf()+"',";
				}
				ctbill.setCtRentbillSrftList(list_srft);
				//保存租约账单
				rentService.save(ctbill);
				//最后回写合同业务信息==根据日期回写合同业务页签的是否开账标识
				if(!"".equals(ywstr)){
					ywstr=ywstr.substring(0, ywstr.lastIndexOf(","));
					String sqlup="update wg_contract_ywcf set vdef1='Y' where pk_contract_ywcf in("+ywstr+")";
					baseDao.updateSql(sqlup);
				}
				if(!"".equals(wyfstr)){
					wyfstr=wyfstr.substring(0, wyfstr.lastIndexOf(","));
					String sqlup2="update wg_contract_wyfcf set vdef1='Y' where pk_contract_wyfcf in("+wyfstr+")";
					baseDao.updateSql(sqlup2);
				}
				if(!"".equals(zqstr)){
					zqstr=zqstr.substring(0, zqstr.lastIndexOf(","));
					String sqlup3="update wg_contract_zqfycf set vdef1='Y' where pk_contract_zqfycf in("+zqstr+")";
					baseDao.updateSql(sqlup3);
				}
				if(!"".equals(srstr)){
					srstr=srstr.substring(0, srstr.lastIndexOf(","));
					String sqlup4="update wg_contract_srcf set vdef1='Y' where pk_contract_srcf in("+srstr+")";
					baseDao.updateSql(sqlup4);
				}
				
				//保证金直接推审批的应收数据
				WgContractYj yj=new WgContractYj();
				yj.getSqlMap().getDataScope().addFilter("dsf", " 1=1 and a.pk_contract='"+dkz2.getPkContract()+"' "
						+ "and nvl(a.dr,0)=0 and a.pk_contract_yj not in (select ys.vsrcid2 from ct_charge_ys ys where nvl(ys.dr,0)=0) ");
				
				List<WgContractYj> yjList=yjDao.findList(yj);
				for(int i=0;i<yjList.size();i++){
					WgContractYj yw=yjList.get(i);
					
					CtChargeYs ys=new CtChargeYs();
					ys.setPkProject(ctbill.getPkProject());
					ys.setHtcode(ctbill.getHtcode());
					ys.setPkCustomer(yw.getPkCustomer()==null?ctbill.getPkCustomer():yw.getPkCustomer());
					ys.setPkBuild(ctbill.getPkBuilding());
					ys.setPkHouse(ctbill.getPkHouse());
					ys.setPkSfProject(yw.getPkYsproject());
					ys.setYfdate(convo.getDfirstfkdate());
					ys.setFyksdate(convo.getDstartdate());
					ys.setFyjzdate(null);
					ys.setKjyears(convo.getDfirstfkdate());
					ys.setNysmny(yw.getNrecmny());
					ys.setTaxRate(convo.getNtaxrate()==null?new Double(0):Double.parseDouble(convo.getNtaxrate().toString()));//按合同税率来
					ys.setNoTaxAmount(ys.getNysmny()/(100+ys.getTaxRate())*100);
					ys.setTaxAmount(ys.getNysmny()-ys.getNoTaxAmount());
					ys.setNys1mny(new Double(0));
					ys.setLyvbilltype(AbsEnumType.BillType_HT);
					ys.setVbillstatus(AbsEnumType.BillStatus_SPTG);
					ys.setApprover(UserUtils.getUser());
					ys.setApprovetime(new Date());
					ys.setCreator(UserUtils.getUser());
					ys.setCreationtime(new Date());
					ys.setPkOrg(ctbill.getPkOrg());
					ys.setPkDept(ctbill.getPkDept());
					ys.setDr(0);
					ys.setTs(new Date());
					ys.setNqsmny(ys.getNysmny());
					ys.setVsrcid(null);
					ys.setVpid(null);//此处无账单信息
					ys.setVsrcid2(yw.getPkContractYj());
					ys.setVsrcid2name("pk_contract_yj");
					ysService.save(ys);
				}
			}
		} catch (Exception e) {
			result="Error:"+e.getMessage();
		}
		return result;
	}
	
	private Double getUfd(Object obj){
		return obj==null?new Double(0):Double.parseDouble(obj.toString());
	}
	
}