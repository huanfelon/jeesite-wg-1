/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.service;

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
import com.jeesite.modules.base.dao.CommonBaseDao;
import com.jeesite.modules.ct.dao.CtChargeYsDao;
import com.jeesite.modules.ct.entity.CtChargeYs;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.wght.entity.WgContract;
import com.jeesite.modules.wght.entity.WgContractHouse;
import com.jeesite.modules.wght.entity.WgContractWytype;
import com.jeesite.modules.wght.entity.WgContractYj;
import com.jeesite.modules.wght.entity.WgContractZltype;
import com.jeesite.modules.wght.entity.WgContractZqfy;
import com.jeesite.modules.wght.entity.WgThrowalease;
import com.jeesite.modules.wght.dao.WgThrowaleaseDao;
import com.jeesite.modules.wght.entity.WgThrowaleaseFyqs;
import com.jeesite.modules.wght.dao.WgThrowaleaseFyqsDao;
import com.jeesite.modules.wght.entity.WgThrowaleaseBzj;
import com.jeesite.modules.wght.dao.WgThrowaleaseBzjDao;
import com.jeesite.modules.wght.entity.WgThrowaleaseKhfc;
import com.jeesite.modules.wght.dao.WgThrowaleaseKhfcDao;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 退租管理Service
 * @author LY
 * @version 2019-12-19
 */
@Service
@Transactional(readOnly=true)
public class WgThrowaleaseService extends CrudService<WgThrowaleaseDao, WgThrowalease> {
	
	@Autowired
	private WgThrowaleaseFyqsDao wgThrowaleaseFyqsDao;
	@Autowired
	private WgThrowaleaseBzjDao wgThrowaleaseBzjDao;
	@Autowired
	private WgThrowaleaseKhfcDao wgThrowaleaseKhfcDao;
	@Autowired
	private WgThrowaleaseDao wgThrowaleaseDao;
	@Autowired
	private WgContractService wgContractService;
	@Autowired
	private CtChargeYsDao ctChargeYsDao;
	@Autowired
	private CommonBaseDao commonBaseDao;
	
	/**
	 * 获取单条数据
	 * @param wgThrowalease
	 * @return
	 */
	@Override
	public WgThrowalease get(WgThrowalease wgThrowalease) {
		WgThrowalease entity = super.get(wgThrowalease);
		if (entity != null){
			WgThrowaleaseFyqs wgThrowaleaseFyqs = new WgThrowaleaseFyqs(entity);
			wgThrowaleaseFyqs.setStatus(WgThrowaleaseFyqs.STATUS_NORMAL);
			entity.setWgThrowaleaseFyqsList(wgThrowaleaseFyqsDao.findList(wgThrowaleaseFyqs));
			WgThrowaleaseBzj wgThrowaleaseBzj = new WgThrowaleaseBzj(entity);
			wgThrowaleaseBzj.setStatus(WgThrowaleaseBzj.STATUS_NORMAL);
			entity.setWgThrowaleaseBzjList(wgThrowaleaseBzjDao.findList(wgThrowaleaseBzj));
			WgThrowaleaseKhfc wgThrowaleaseKhfc = new WgThrowaleaseKhfc(entity);
			wgThrowaleaseKhfc.setStatus(WgThrowaleaseKhfc.STATUS_NORMAL);
			entity.setWgThrowaleaseKhfcList(wgThrowaleaseKhfcDao.findList(wgThrowaleaseKhfc));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param wgThrowalease 查询条件
	 * @param wgThrowalease.page 分页对象
	 * @return
	 */
	@Override
	public Page<WgThrowalease> findPage(WgThrowalease wgThrowalease) {
		return super.findPage(wgThrowalease);
	}
	
	/**
	 * 参照转换
	 * @param pk
	 * @return
	 */
	public WgThrowalease refDataRender(String pk){
		WgContract wght=wgContractService.get(new WgContract(pk));//合同数据
		//表头
		WgThrowalease newtz=new WgThrowalease();
		newtz.setPkOrg(wght.getPkOrg());
		newtz.setPkProject(wght.getPkProject());
		newtz.setHtcode(wght.getHtcode());
		newtz.setDstartdate(wght.getDstartdate());
		newtz.setDenddate(wght.getDenddate());
		newtz.setDtzdate(new Date());
		if(wght.getDenddate().compareTo(new Date())>0){
			newtz.setTztype(1);//提前退租
		}
		newtz.setVbillstatus(AbsEnumType.BillStatus_ZY);
		newtz.setBilltype(AbsEnumType.BillType_TZ);
		newtz.setVsrcid(pk);
		newtz.setVsrctype(AbsEnumType.BillType_HT);
		newtz.setCreator(UserUtils.getUser());
		newtz.setCreatedtime(new Date());
		
		//客户房产页签
		WgThrowaleaseKhfc khfc=new WgThrowaleaseKhfc();
		khfc.setPkCustomer(wght.getWgContractCustList().get(0).getPkCustomer());
		String pkhouse="";
		String housename="";
		for (WgContractHouse house : wght.getWgContractHouseList()) {
			pkhouse+=house.getPkHouse().getPkHousesource()+",";
			housename+=house.getPkHouse().getEstatename()+",";
		}
		ZlHousesource house=new ZlHousesource();
		house.setPkHousesource(pkhouse.substring(0, pkhouse.length()-1));
		house.setEstatename(housename.substring(0, housename.length()-1));
		khfc.setPkHouse(house);
		List<WgThrowaleaseKhfc> listkhfc=new ArrayList<WgThrowaleaseKhfc>();
		listkhfc.add(khfc);
		newtz.setWgThrowaleaseKhfcList(listkhfc);
		
		//保证金结算
		List<WgThrowaleaseBzj> listbzj=new ArrayList<WgThrowaleaseBzj>();
		for (WgContractYj yj : wght.getWgContractYjList()) {
			WgThrowaleaseBzj bzj=new WgThrowaleaseBzj();
			bzj.setPkCostproject(yj.getPkYsproject());
			bzj.setNskmny(getDbObj(yj.getNrealmny()));
			bzj.setNygtmny(new Double(0));
			bzj.setDytdate(new Date());
			listbzj.add(bzj);
		}
		newtz.setWgThrowaleaseBzjList(listbzj);
		
		//费用结算
		List<WgThrowaleaseFyqs> listfyqs=new ArrayList<WgThrowaleaseFyqs>();
		List<WgContractZqfy> listzqfy=wght.getWgContractZqfyList();
		if(listzqfy!=null&&listzqfy.size()>0){
			for (WgContractZqfy zqfy : listzqfy) {
				WgThrowaleaseFyqs fyqs=new WgThrowaleaseFyqs();
				fyqs.setPkCostproject(zqfy.getPkYsproject());
				fyqs.setNygtmny(new Double(0));
				fyqs.setDytdate(new Date());
				fyqs.setNtaxrate(zqfy.getNtaxrate());
				fyqs.setNnotaxmny(new Double(0));
				fyqs.setNtaxmny(new Double(0));
				fyqs.setNygqrmny(new Double(0));
				listfyqs.add(fyqs);
			}
		}
		List<WgContractZltype> listzlt=wght.getWgContractZltypeList();
		if(listzlt!=null&&listzlt.size()>0){
			WgThrowaleaseFyqs fyqs=new WgThrowaleaseFyqs();
			fyqs.setPkCostproject(listzlt.get(0).getPkYsproject());
			fyqs.setNygtmny(new Double(0));
			fyqs.setDytdate(new Date());
			fyqs.setNtaxrate(wght.getNtaxrate());
			fyqs.setNnotaxmny(new Double(0));
			fyqs.setNtaxmny(new Double(0));
			fyqs.setNygqrmny(new Double(0));
			listfyqs.add(fyqs);
		}
		List<WgContractWytype> listwyt=wght.getWgContractWytypeList();
		if(listwyt!=null&&listwyt.size()>0){
			WgThrowaleaseFyqs fyqs=new WgThrowaleaseFyqs();
			fyqs.setPkCostproject(listwyt.get(0).getPkYsproject());
			fyqs.setNygtmny(new Double(0));
			fyqs.setDytdate(new Date());
			fyqs.setNtaxrate(wght.getWgContractWyfList().get(0).getNtaxrate());
			fyqs.setNnotaxmny(new Double(0));
			fyqs.setNtaxmny(new Double(0));
			fyqs.setNygqrmny(new Double(0));
			listfyqs.add(fyqs);
		}
		newtz.setWgThrowaleaseFyqsList(listfyqs);
		return newtz;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param wgThrowalease
	 */
	@Transactional(readOnly=false)
	public String saveData(WgThrowalease wgThrowalease) {
		//校验
		String msg=checkData(wgThrowalease);
		if(!msg.equals("")){
			return msg;
		}
		//更新时间戳
		wgThrowalease.setTs(new Date());
		if(wgThrowalease.getIsNewRecord()){//新增
			wgThrowaleaseDao.insert(wgThrowalease);
		}else{//修改
			wgThrowaleaseDao.update(wgThrowalease);
		}
		// 保存 WgThrowalease子表
		for (WgThrowaleaseKhfc wgThrowaleaseKhfc : wgThrowalease.getWgThrowaleaseKhfcList()){
			//更新时间戳
			wgThrowaleaseKhfc.setTs(new Date());
			if (!WgThrowaleaseKhfc.STATUS_DELETE.equals(wgThrowaleaseKhfc.getStatus())){
				wgThrowaleaseKhfc.setPkThrowalease(wgThrowalease);
				if (wgThrowaleaseKhfc.getIsNewRecord()){
					wgThrowaleaseKhfcDao.insert(wgThrowaleaseKhfc);
				}else{
					wgThrowaleaseKhfcDao.update(wgThrowaleaseKhfc);
				}
			}else{
				commonBaseDao.deleteSql("delete from wg_throwalease_khfc where pk_throwalease_cust='"+wgThrowaleaseKhfc.getPkThrowaleaseKhfc()+"'");
			}
		}
		// 保存 WgThrowalease子表
		for (WgThrowaleaseBzj wgThrowaleaseBzj : wgThrowalease.getWgThrowaleaseBzjList()){
			//更新时间戳
			wgThrowaleaseBzj.setTs(new Date());
			if (!WgThrowaleaseBzj.STATUS_DELETE.equals(wgThrowaleaseBzj.getStatus())){
				wgThrowaleaseBzj.setPkThrowalease(wgThrowalease);
				if (wgThrowaleaseBzj.getIsNewRecord()){
					wgThrowaleaseBzjDao.insert(wgThrowaleaseBzj);
				}else{
					wgThrowaleaseBzjDao.update(wgThrowaleaseBzj);
				}
			}else{
				commonBaseDao.deleteSql("delete from wg_throwalease_bzj where pk_throwalease_bzj='"+wgThrowaleaseBzj.getPkThrowaleaseBzj()+"'");
			}
		}
		// 保存 WgThrowalease子表
		for (WgThrowaleaseFyqs wgThrowaleaseFyqs : wgThrowalease.getWgThrowaleaseFyqsList()){
			//更新时间戳
			wgThrowaleaseFyqs.setTs(new Date());
			if (!WgThrowaleaseFyqs.STATUS_DELETE.equals(wgThrowaleaseFyqs.getStatus())){
				wgThrowaleaseFyqs.setPkThrowalease(wgThrowalease);
				if (wgThrowaleaseFyqs.getIsNewRecord()){
					wgThrowaleaseFyqsDao.insert(wgThrowaleaseFyqs);
				}else{
					wgThrowaleaseFyqsDao.update(wgThrowaleaseFyqs);
				}
			}else{
				commonBaseDao.deleteSql("delete from wg_throwalease_fyqs where pk_throwalease_fyqs='"+wgThrowaleaseFyqs.getPkThrowaleaseFyqs()+"'");
			}
		}
		return "";
	}
	
	/**
	 * 校验数据
	 */
	public String checkData(WgThrowalease wgThrowalease){
		return "";
	}
	
	/**
	 * 删除数据
	 * @param wgThrowalease
	 */
	@Transactional(readOnly=false)
	public String deleteData(List<String> listpk) {
		String msg="";
		for(String pk : listpk){
			//校验
			WgThrowalease newtz=this.get(pk);
			if(!newtz.getVbillstatus().equals(AbsEnumType.BillStatus_ZY)){
				msg+="单据号"+newtz.getVbillno()+"不是自由态，无法删除！</br>";
			}else{
				commonBaseDao.updateSql("update wg_throwalease set dr=1,ts=sysdate where pk_throwalease='"+pk+"'");
				commonBaseDao.updateSql("update wg_throwalease_khfc set dr=1,ts=sysdate where pk_throwalease='"+pk+"'");
				commonBaseDao.updateSql("update wg_throwalease_bzj set dr=1,ts=sysdate where pk_throwalease='"+pk+"'");
				commonBaseDao.updateSql("update wg_throwalease_fyqs set dr=1,ts=sysdate where pk_throwalease='"+pk+"'");
			}
		}
		return msg;
	}
	
	/**
	 * 批量审批数据
	 * @param wgContract
	 */
	@Transactional(readOnly=false)
	public String approveAllData(List<String> listpk) {
		SimpleDateFormat df=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String msg="";
		for(String pk:listpk){
			WgThrowalease newtz=this.get(pk);
			if(newtz.getVbillstatus().equals(AbsEnumType.BillStatus_SPTG)){
				continue;
			}
			
			//生成应收
			sendToYs(newtz);
			//回写合同
			commonBaseDao.updateSql("update wg_contract set htstatus="+AbsEnumType.HtStatus_TZ+",dtzdate=to_date('"+df.format(newtz.getDtzdate())+"','yyyy-mm-dd hh24:mi:ss'),ts=sysdate "
					+ "where pk_contract='"+newtz.getVsrcid()+"'");
			//回写房产
			WgThrowaleaseKhfc khfc=newtz.getWgThrowaleaseKhfcList().get(0);
			String pkhouse=khfc.getPkHouse().getPkHousesource();
			String pks="";
			for (String pkh : pkhouse.split(",")) {
				pks+="'"+pkh+"',";
			}
			commonBaseDao.updateSql("update zl_housesource set housestate="+AbsEnumType.HOUSE_KZ+" where pk_housesource in ("+pks.substring(0,pks.length()-1)+")");
			//更新自身
			newtz.setTs(new Date());
			newtz.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			newtz.setApprover(UserUtils.getUser());
			newtz.setApprovedtime(new Date());
			wgThrowaleaseDao.update(newtz);
		}
		return msg;
	}
	
	/**
	 * 批量取消审批数据
	 * @param wgContract
	 */
	@Transactional(readOnly=false)
	public String unApproveAllData(List<String> listpk) {
		String msg="";
		for(String pk:listpk){
			WgThrowalease newtz=this.get(pk);
			if(newtz.getVbillstatus().equals(AbsEnumType.BillStatus_ZY)){
				continue;
			}
			//判断应收是否被收款
			String sqlyj="select count(*) from ct_charge_sk_b skb where nvl(skb.dr,0)=0 and skb.lyvbillno in (select ys.pk_charge_ys from ct_charge_ys ys "
					+ "where nvl(ys.dr,0)=0 and ys.vpid='"+newtz.getPkThrowalease()+"')";
			Integer count2=commonBaseDao.selectCount(sqlyj);
			if(count2>0){
				msg+="生成应收单已经被收款参照，无法取消审批！</br>";
				continue;
			}
			
			//删除应收
			commonBaseDao.deleteSql("delete from ct_charge_ys where vpid='"+newtz.getPkThrowalease()+"'");
			//回写合同
			commonBaseDao.updateSql("update wg_contract set htstatus="+AbsEnumType.HtStatus_QZ+",dtzdate='',ts=sysdate where pk_contract='"+newtz.getVsrcid()+"'");
			//回写房产
			WgThrowaleaseKhfc khfc=newtz.getWgThrowaleaseKhfcList().get(0);
			String pkhouse=khfc.getPkHouse().getPkHousesource();
			String pks="";
			for (String pkh : pkhouse.split(",")) {
				pks+="'"+pkh+"',";
			}
			commonBaseDao.updateSql("update zl_housesource set housestate="+AbsEnumType.HOUSE_YZ+" where pk_housesource in ("+pks.substring(0,pks.length()-1)+")");
			
			//更新自身
			newtz.setTs(new Date());
			newtz.setVbillstatus(AbsEnumType.BillStatus_ZY);
			newtz.setApprover(null);
			newtz.setApprovedtime(null);
			wgThrowaleaseDao.update(newtz);
		}
		return msg;
	}
	
	public void sendToYs(WgThrowalease wgThrowalease){
		WgThrowaleaseKhfc khfc=wgThrowalease.getWgThrowaleaseKhfcList().get(0);
		for (WgThrowaleaseBzj bzj : wgThrowalease.getWgThrowaleaseBzjList()) {
			//金额为0不传
			if(bzj.getNygtmny().compareTo(new Double(0))==0){
				continue ;
			}
			CtChargeYs ys=new CtChargeYs();
			ys.setPkProject(wgThrowalease.getPkProject());
			ys.setHtcode(wgThrowalease.getHtcode());
			ys.setPkCustomer(khfc.getPkCustomer());
			ys.setPkBuild(khfc.getPkHouse().getBuildname());
			ys.setPkHouse(khfc.getPkHouse());
			ys.setPkSfProject(bzj.getPkCostproject());
			ys.setYfdate(bzj.getDytdate());
			ys.setFyksdate(bzj.getDytdate());
			ys.setKjyears(bzj.getDytdate());
			ys.setNysmny(bzj.getNygtmny());
			ys.setTaxRate(new Double(0));
			ys.setNoTaxAmount(bzj.getNygtmny());
			ys.setTaxAmount(new Double(0));
			ys.setNys1mny(new Double(0));
			ys.setNqsmny(bzj.getNygtmny());
			ys.setLyvbilltype(AbsEnumType.BillType_TZ);
			ys.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			ys.setApprover(UserUtils.getUser());
			ys.setApprovetime(new Date());
			ys.setCreator(UserUtils.getUser());
			ys.setCreationtime(new Date());
			ys.setPkOrg(wgThrowalease.getPkOrg());
			ys.setPkDept(wgThrowalease.getPkDept());
			ys.setDr(0);
			ys.setTs(new Date());
			ys.setVsrcid2(bzj.getPkThrowaleaseBzj());
			ys.setVpid(wgThrowalease.getPkThrowalease());
			ys.setVsrcid2name("pk_throwalease_bzj");
			ctChargeYsDao.insert(ys);
		}
		for (WgThrowaleaseFyqs fyqs : wgThrowalease.getWgThrowaleaseFyqsList()) {
			//金额为0不传
			if(fyqs.getNygtmny().compareTo(new Double(0))==0){
				continue ;
			}
			CtChargeYs ys=new CtChargeYs();
			ys.setPkProject(wgThrowalease.getPkProject());
			ys.setHtcode(wgThrowalease.getHtcode());
			ys.setPkCustomer(khfc.getPkCustomer());
			ys.setPkBuild(khfc.getPkHouse().getBuildname());
			ys.setPkHouse(khfc.getPkHouse());
			ys.setPkSfProject(fyqs.getPkCostproject());
			ys.setYfdate(fyqs.getDytdate());
			ys.setFyksdate(fyqs.getDytdate());
			ys.setKjyears(fyqs.getDytdate());
			ys.setNysmny(fyqs.getNygtmny());
			ys.setTaxRate(new Double(fyqs.getNtaxrate()));
			ys.setNoTaxAmount(fyqs.getNnotaxmny());
			ys.setTaxAmount(fyqs.getNtaxmny());
			ys.setNys1mny(new Double(0));
			ys.setNqsmny(fyqs.getNygtmny());
			ys.setLyvbilltype(AbsEnumType.BillType_TZ);
			ys.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			ys.setApprover(UserUtils.getUser());
			ys.setApprovetime(new Date());
			ys.setCreator(UserUtils.getUser());
			ys.setCreationtime(new Date());
			ys.setPkOrg(wgThrowalease.getPkOrg());
			ys.setPkDept(wgThrowalease.getPkDept());
			ys.setDr(0);
			ys.setTs(new Date());
			ys.setVsrcid2(fyqs.getPkThrowaleaseFyqs());
			ys.setVpid(wgThrowalease.getPkThrowalease());
			ys.setVsrcid2name("pk_throwalease_fyqs");
			ctChargeYsDao.insert(ys);
		}
	}
	
	public Double getDbObj(Object obj){
		return obj==null?new Double(0):new Double(obj.toString());
	}
	
}