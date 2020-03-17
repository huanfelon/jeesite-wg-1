/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.dao.CommonBaseDao;
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.ct.dao.CtRentdkzDao;
import com.jeesite.modules.ct.entity.CtRentdkz;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.wght.entity.WgContract;
import com.jeesite.modules.wght.entity.WgContractRentprice;
import com.jeesite.modules.wght.entity.WgContractSrcf;
import com.jeesite.modules.wght.entity.WgContractTax;
import com.jeesite.modules.wght.entity.WgContractWyfcf;
import com.jeesite.modules.wght.entity.WgContractYwcf;
import com.jeesite.modules.wght.entity.WgContractZqfycf;
import com.jeesite.modules.wght.config.CalculateRentUtils;
import com.jeesite.modules.wght.config.CalendarUtls;
import com.jeesite.modules.wght.dao.WgContractDao;
import com.jeesite.modules.wght.dao.WgContractRentpriceDao;
import com.jeesite.modules.wght.dao.WgContractSrcfDao;
import com.jeesite.modules.wght.dao.WgContractTaxDao;
import com.jeesite.modules.wght.dao.WgContractWyfcfDao;
import com.jeesite.modules.wght.dao.WgContractYwcfDao;
import com.jeesite.modules.wght.dao.WgContractZqfycfDao;
import com.jeesite.modules.wght.entity.WgContractZzq;
import com.jeesite.modules.wght.dao.WgContractZzqDao;
import com.jeesite.modules.wght.entity.WgContractZqfy;
import com.jeesite.modules.wght.dao.WgContractZqfyDao;
import com.jeesite.modules.wght.entity.WgContractZltype;
import com.jeesite.modules.wght.dao.WgContractZltypeDao;
import com.jeesite.modules.wght.entity.WgContractYj;
import com.jeesite.modules.wght.dao.WgContractYjDao;
import com.jeesite.modules.wght.entity.WgContractWytype;
import com.jeesite.modules.wght.dao.WgContractWytypeDao;
import com.jeesite.modules.wght.entity.WgContractWyf;
import com.jeesite.modules.wght.dao.WgContractWyfDao;
import com.jeesite.modules.wght.entity.WgContractMzq;
import com.jeesite.modules.wght.dao.WgContractMzqDao;
import com.jeesite.modules.wght.entity.WgContractHouse;
import com.jeesite.modules.wght.dao.WgContractHouseDao;
import com.jeesite.modules.wght.entity.WgContractCust;
import com.jeesite.modules.wght.dao.WgContractCustDao;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 合同管理Service
 * @author LY
 * @version 2019-10-31
 */
@Service
@Transactional(readOnly=true)
public class WgContractService extends CrudService<WgContractDao, WgContract> {
	
	@Autowired
	private WgContractDao wgContractDao;
	@Autowired
	private WgContractCustDao wgContractCustDao;
	@Autowired
	private WgContractHouseDao wgContractHouseDao;
	@Autowired
	private WgContractWyfDao wgContractWyfDao;
	@Autowired
	private WgContractZqfyDao wgContractZqfyDao;
	@Autowired
	private WgContractYjDao wgContractYjDao;
	@Autowired
	private WgContractZltypeDao wgContractZltypeDao;
	@Autowired
	private WgContractWytypeDao wgContractWytypeDao;
	@Autowired
	private WgContractMzqDao wgContractMzqDao;
	@Autowired
	private WgContractZzqDao wgContractZzqDao;
	@Autowired
	private WgContractRentpriceDao wgContractRentpriceDao;
	@Autowired
	private WgContractYwcfDao wgContractYwcfDao;
	@Autowired
	private WgContractWyfcfDao wgContractWyfcfDao;
	@Autowired
	private WgContractZqfycfDao wgContractZqfycfDao;
	@Autowired
	private WgContractSrcfDao wgContractSrcfDao;
	@Autowired
	private CtRentdkzDao ctRentdkzDao;
	@Autowired
	private CommonBaseDao commonBaseDao;
	@Autowired
	private WgContractTaxDao wgContractTaxDao;
	
	/**
	 * 获取单条数据
	 * @param wgContract
	 * @return
	 */
	@Override
	public WgContract get(WgContract wgContract) {
		WgContract entity = super.get(wgContract);
		if (entity != null){
			WgContractCust wgContractCust = new WgContractCust(entity);
			wgContractCust.setStatus(WgContractCust.STATUS_NORMAL);
			entity.setWgContractCustList(wgContractCustDao.findList(wgContractCust));
			WgContractHouse wgContractHouse = new WgContractHouse(entity);
			wgContractHouse.setStatus(WgContractHouse.STATUS_NORMAL);
			entity.setWgContractHouseList(wgContractHouseDao.findList(wgContractHouse));
			WgContractWyf wgContractWyf = new WgContractWyf(entity);
			wgContractWyf.setStatus(WgContractWyf.STATUS_NORMAL);
			entity.setWgContractWyfList(wgContractWyfDao.findList(wgContractWyf));
			WgContractZqfy wgContractZqfy = new WgContractZqfy(entity);
			wgContractZqfy.setStatus(WgContractZqfy.STATUS_NORMAL);
			entity.setWgContractZqfyList(wgContractZqfyDao.findList(wgContractZqfy));
			WgContractYj wgContractYj = new WgContractYj(entity);
			wgContractYj.setStatus(WgContractYj.STATUS_NORMAL);
			entity.setWgContractYjList(wgContractYjDao.findList(wgContractYj));
			WgContractZltype wgContractZltype = new WgContractZltype(entity);
			wgContractZltype.setStatus(WgContractZltype.STATUS_NORMAL);
			entity.setWgContractZltypeList(wgContractZltypeDao.findList(wgContractZltype));
			WgContractWytype wgContractWytype = new WgContractWytype(entity);
			wgContractWytype.setStatus(WgContractWytype.STATUS_NORMAL);
			entity.setWgContractWytypeList(wgContractWytypeDao.findList(wgContractWytype));
			WgContractMzq wgContractMzq = new WgContractMzq(entity);
			wgContractMzq.setStatus(WgContractMzq.STATUS_NORMAL);
			entity.setWgContractMzqList(wgContractMzqDao.findList(wgContractMzq));
			WgContractZzq wgContractZzq = new WgContractZzq(entity);
			wgContractZzq.setStatus(WgContractZzq.STATUS_NORMAL);
			entity.setWgContractZzqList(wgContractZzqDao.findList(wgContractZzq));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param wgContract 查询条件
	 * @param wgContract.page 分页对象
	 * @return
	 */
	@Override
	public Page<WgContract> findPage(WgContract wgContract) {
		String cust="(select bc.name from bd_customer bc where bc.pk_customer=(select cc.pk_customer from wg_contract_cust cc "
				+ "where cc.pk_contract=a.pk_contract and nvl(cc.dr,0)=0 and rownum=1)) AS \"custname\"";
		wgContract.getSqlMap().add("custname", cust);
		if(wgContract.getVdef5()!=null&&!wgContract.getVdef5().equals("")){
			wgContract.getSqlMap().getDataScope().addFilter("custnamequery", " exists (select 1 from bd_customer bc where bc.pk_customer=(select cc.pk_customer from wg_contract_cust cc "
				+ "where cc.pk_contract=a.pk_contract and nvl(cc.dr,0)=0 and rownum=1) and bc.name like '%"+wgContract.getVdef5()+"%')");
			wgContract.setVdef5(null);
		}
		return super.findPage(wgContract);
	}
	
	/**
	 * 合同管理保存数据（插入或更新）
	 * @param wgContract
	 */
	@Transactional(readOnly=false)
	public String saveData(WgContract wgContract) {
		//校验
		String msg=checkData(wgContract);
		if(!msg.equals("")){
			return msg;
		}
		//更新时间戳
		wgContract.setTs(new Date());
		if(wgContract.getIsNewRecord()){//新增
			wgContract.setBilltype(AbsEnumType.BillType_HT);
			wgContract.setVersion(-1);
			wgContractDao.insert(wgContract);
		}else{//修改
			wgContractDao.update(wgContract);
		}
		// 保存 WgContract子表客户信息
		for (WgContractCust wgContractCust : wgContract.getWgContractCustList()){
			//更新时间戳
			wgContractCust.setTs(new Date());
			if (!WgContractCust.STATUS_DELETE.equals(wgContractCust.getStatus())){
				wgContractCust.setPkContract(wgContract);
				if (wgContractCust.getIsNewRecord()){
					wgContractCustDao.insert(wgContractCust);
				}else{
					wgContractCustDao.update(wgContractCust);
				}
			}else{
				wgContractCustDao.deleteDataNewByPk(wgContractCust.getPkContractCust());
			}
		}
		// 保存 WgContract子表房产信息
		for (WgContractHouse wgContractHouse : wgContract.getWgContractHouseList()){
			//更新时间戳
			wgContractHouse.setTs(new Date());
			if (!WgContractHouse.STATUS_DELETE.equals(wgContractHouse.getStatus())){
				//回写房源状态已租
				commonBaseDao.updateSql("update zl_housesource set housestate="+AbsEnumType.HOUSE_YZ+" where pk_housesource='"+wgContractHouse.getPkHouse()+"'");
				//保存自身
				wgContractHouse.setPkContract(wgContract);
				if (wgContractHouse.getIsNewRecord()){
					wgContractHouseDao.insert(wgContractHouse);
				}else{
					wgContractHouseDao.update(wgContractHouse);
				}
			}else{
				//回写房源状态空置
				commonBaseDao.updateSql("update zl_housesource set housestate="+AbsEnumType.HOUSE_KZ+" where pk_housesource='"+wgContractHouse.getPkHouse()+"'");
				wgContractHouseDao.deleteDataNewByPk(wgContractHouse.getPkContractHouse());
			}
		}
		// 保存 WgContract子表物业费
		for (WgContractWyf wgContractWyf : wgContract.getWgContractWyfList()){
			//更新时间戳
			wgContractWyf.setTs(new Date());
			if (!WgContractWyf.STATUS_DELETE.equals(wgContractWyf.getStatus())){
				wgContractWyf.setPkContract(wgContract);
				if (wgContractWyf.getIsNewRecord()){
					wgContractWyfDao.insert(wgContractWyf);
				}else{
					wgContractWyfDao.update(wgContractWyf);
				}
			}else{
				wgContractWyfDao.deleteDataNewByPk(wgContractWyf.getPkContractWyf());
			}
		}
		// 保存 WgContract子表其他周期费用
		for (WgContractZqfy wgContractZqfy : wgContract.getWgContractZqfyList()){
			//更新时间戳
			wgContractZqfy.setTs(new Date());
			if (!WgContractZqfy.STATUS_DELETE.equals(wgContractZqfy.getStatus())){
				wgContractZqfy.setPkContract(wgContract);
				if (wgContractZqfy.getIsNewRecord()){
					wgContractZqfyDao.insert(wgContractZqfy);
				}else{
					wgContractZqfyDao.update(wgContractZqfy);
				}
			}else{
				wgContractZqfyDao.deleteDataNewByPk(wgContractZqfy.getPkContractZqfy());
			}
		}
		// 保存 WgContract子表押金
		for (WgContractYj wgContractYj : wgContract.getWgContractYjList()){
			//更新时间戳
			wgContractYj.setTs(new Date());
			if (!WgContractYj.STATUS_DELETE.equals(wgContractYj.getStatus())){
				wgContractYj.setPkContract(wgContract);
				if (wgContractYj.getIsNewRecord()){
					wgContractYjDao.insert(wgContractYj);
				}else{
					wgContractYjDao.update(wgContractYj);
				}
			}else{
				wgContractYjDao.deleteDataNewByPk(wgContractYj.getPkContractYj());
			}
		}
		// 保存 WgContract子表租赁支付方式
		for (WgContractZltype wgContractZltype : wgContract.getWgContractZltypeList()){
			//更新时间戳
			wgContractZltype.setTs(new Date());
			if (!WgContractZltype.STATUS_DELETE.equals(wgContractZltype.getStatus())){
				wgContractZltype.setPkContract(wgContract);
				if(wgContractZltype.getDstartdate()==null){
					wgContractZltype.setDstartdate(wgContract.getDstartdate());
				}
				if (wgContractZltype.getIsNewRecord()){
					wgContractZltypeDao.insert(wgContractZltype);
				}else{
					wgContractZltypeDao.update(wgContractZltype);
				}
			}else{
				wgContractZltypeDao.deleteDataNewByPk(wgContractZltype.getPkContractZltype());
			}
		}
		// 保存 WgContract子表物业支付方式
		for (WgContractWytype wgContractWytype : wgContract.getWgContractWytypeList()){
			//更新时间戳
			wgContractWytype.setTs(new Date());
			if (!WgContractWytype.STATUS_DELETE.equals(wgContractWytype.getStatus())){
				wgContractWytype.setPkContract(wgContract);
				if(wgContractWytype.getDstartdate()==null){
					wgContractWytype.setDstartdate(wgContract.getDstartdate());
				}
				if (wgContractWytype.getIsNewRecord()){
					wgContractWytypeDao.insert(wgContractWytype);
				}else{
					wgContractWytypeDao.update(wgContractWytype);
				}
			}else{
				wgContractWytypeDao.deleteDataNewByPk(wgContractWytype.getPkContractWytype());
			}
		}
		
		// 保存 WgContract子表免租期
		for (WgContractMzq wgContractMzq : wgContract.getWgContractMzqList()){
			//更新时间戳
			wgContractMzq.setTs(new Date());
			if (!WgContractMzq.STATUS_DELETE.equals(wgContractMzq.getStatus())){
				wgContractMzq.setPkContract(wgContract);
				if (wgContractMzq.getIsNewRecord()){
					wgContractMzqDao.insert(wgContractMzq);
				}else{
					wgContractMzqDao.update(wgContractMzq);
				}
			}else{
				wgContractMzqDao.deleteDataNewByPk(wgContractMzq.getPkContractMzq());
			}
		}
		// 保存 WgContract子表增长期
		for (WgContractZzq wgContractZzq : wgContract.getWgContractZzqList()){
			//更新时间戳
			wgContractZzq.setTs(new Date());
			if (!WgContractZzq.STATUS_DELETE.equals(wgContractZzq.getStatus())){
				wgContractZzq.setPkContract(wgContract);
				if (wgContractZzq.getIsNewRecord()){
					wgContractZzqDao.insert(wgContractZzq);
				}else{
					wgContractZzqDao.update(wgContractZzq);
				}
			}else{
				wgContractZzqDao.deleteDataNewByPk(wgContractZzq.getPkContractZzq());
			}
		}
		
		return "";
	}
	
	/**
	 * 合同修订保存数据（插入或更新）
	 * @param wgContract
	 */
	@Transactional(readOnly=false)
	public String saveDataXD(WgContract wgContractXD) {
		//校验
		String msg=checkData(wgContractXD);
		if(!msg.equals("")){
			return msg;
		}
		//更新时间戳
		wgContractXD.setTs(new Date());
		if(wgContractXD.getIsNewRecord()){//新增
			wgContractDao.insert(wgContractXD);
		}else{//修改
			wgContractDao.update(wgContractXD);
		}
		// 保存 WgContract子表客户信息
		for (WgContractCust wgContractXDCust : wgContractXD.getWgContractCustList()){
			//更新时间戳
			wgContractXDCust.setTs(new Date());
			if (!WgContractCust.STATUS_DELETE.equals(wgContractXDCust.getStatus())){
				wgContractXDCust.setPkContract(wgContractXD);
				if (wgContractXDCust.getIsNewRecord()){
					wgContractCustDao.insert(wgContractXDCust);
				}else{
					wgContractCustDao.update(wgContractXDCust);
				}
			}else{
				wgContractCustDao.deleteDataNewByPk(wgContractXDCust.getPkContractCust());
			}
		}
		// 保存 WgContract子表房产信息
		for (WgContractHouse wgContractXDHouse : wgContractXD.getWgContractHouseList()){
			//更新时间戳
			wgContractXDHouse.setTs(new Date());
			if (!WgContractHouse.STATUS_DELETE.equals(wgContractXDHouse.getStatus())){
				wgContractXDHouse.setPkContract(wgContractXD);
				if (wgContractXDHouse.getIsNewRecord()){
					wgContractHouseDao.insert(wgContractXDHouse);
				}else{
					wgContractHouseDao.update(wgContractXDHouse);
				}
			}else{
				wgContractHouseDao.deleteDataNewByPk(wgContractXDHouse.getPkContractHouse());
			}
		}
		// 保存 WgContract子表物业费
		for (WgContractWyf wgContractXDWyf : wgContractXD.getWgContractWyfList()){
			//更新时间戳
			wgContractXDWyf.setTs(new Date());
			if (!WgContractWyf.STATUS_DELETE.equals(wgContractXDWyf.getStatus())){
				wgContractXDWyf.setPkContract(wgContractXD);
				if (wgContractXDWyf.getIsNewRecord()){
					wgContractWyfDao.insert(wgContractXDWyf);
				}else{
					wgContractWyfDao.update(wgContractXDWyf);
				}
			}else{
				wgContractWyfDao.deleteDataNewByPk(wgContractXDWyf.getPkContractWyf());
			}
		}
		// 保存 WgContract子表其他周期费用
		for (WgContractZqfy wgContractXDZqfy : wgContractXD.getWgContractZqfyList()){
			//更新时间戳
			wgContractXDZqfy.setTs(new Date());
			if (!WgContractZqfy.STATUS_DELETE.equals(wgContractXDZqfy.getStatus())){
				wgContractXDZqfy.setPkContract(wgContractXD);
				if (wgContractXDZqfy.getIsNewRecord()){
					wgContractZqfyDao.insert(wgContractXDZqfy);
				}else{
					wgContractZqfyDao.update(wgContractXDZqfy);
				}
			}else{
				wgContractZqfyDao.deleteDataNewByPk(wgContractXDZqfy.getPkContractZqfy());
			}
		}
		// 保存 WgContract子表押金
		for (WgContractYj wgContractXDYj : wgContractXD.getWgContractYjList()){
			//更新时间戳
			wgContractXDYj.setTs(new Date());
			if (!WgContractYj.STATUS_DELETE.equals(wgContractXDYj.getStatus())){
				wgContractXDYj.setPkContract(wgContractXD);
				if (wgContractXDYj.getIsNewRecord()){
					wgContractYjDao.insert(wgContractXDYj);
				}else{
					wgContractYjDao.update(wgContractXDYj);
				}
			}else{
				wgContractYjDao.deleteDataNewByPk(wgContractXDYj.getPkContractYj());
			}
		}
		// 保存 WgContract子表租赁支付方式
		for (WgContractZltype wgContractXDZltype : wgContractXD.getWgContractZltypeList()){
			//更新时间戳
			wgContractXDZltype.setTs(new Date());
			if (!WgContractZltype.STATUS_DELETE.equals(wgContractXDZltype.getStatus())){
				wgContractXDZltype.setPkContract(wgContractXD);
				if (wgContractXDZltype.getIsNewRecord()){
					wgContractZltypeDao.insert(wgContractXDZltype);
				}else{
					wgContractZltypeDao.update(wgContractXDZltype);
				}
			}else{
				wgContractZltypeDao.deleteDataNewByPk(wgContractXDZltype.getPkContractZltype());
			}
		}
		// 保存 WgContract子表物业支付方式
		for (WgContractWytype wgContractXDWytype : wgContractXD.getWgContractWytypeList()){
			//更新时间戳
			wgContractXDWytype.setTs(new Date());
			if (!WgContractWytype.STATUS_DELETE.equals(wgContractXDWytype.getStatus())){
				wgContractXDWytype.setPkContract(wgContractXD);
				if (wgContractXDWytype.getIsNewRecord()){
					wgContractWytypeDao.insert(wgContractXDWytype);
				}else{
					wgContractWytypeDao.update(wgContractXDWytype);
				}
			}else{
				wgContractWytypeDao.deleteDataNewByPk(wgContractXDWytype.getPkContractWytype());
			}
		}
		
		// 保存 WgContract子表免租期
		for (WgContractMzq wgContractXDMzq : wgContractXD.getWgContractMzqList()){
			//更新时间戳
			wgContractXDMzq.setTs(new Date());
			if (!WgContractMzq.STATUS_DELETE.equals(wgContractXDMzq.getStatus())){
				wgContractXDMzq.setPkContract(wgContractXD);
				if (wgContractXDMzq.getIsNewRecord()){
					wgContractMzqDao.insert(wgContractXDMzq);
				}else{
					wgContractMzqDao.update(wgContractXDMzq);
				}
			}else{
				wgContractMzqDao.deleteDataNewByPk(wgContractXDMzq.getPkContractMzq());
			}
		}
		// 保存 WgContract子表增长期
		for (WgContractZzq wgContractXDZzq : wgContractXD.getWgContractZzqList()){
			//更新时间戳
			wgContractXDZzq.setTs(new Date());
			if (!WgContractZzq.STATUS_DELETE.equals(wgContractXDZzq.getStatus())){
				wgContractXDZzq.setPkContract(wgContractXD);
				if (wgContractXDZzq.getIsNewRecord()){
					wgContractZzqDao.insert(wgContractXDZzq);
				}else{
					wgContractZzqDao.update(wgContractXDZzq);
				}
			}else{
				wgContractZzqDao.deleteDataNewByPk(wgContractXDZzq.getPkContractZzq());
			}
		}
		return "";
	}
	
	/**
	 * 校验数据
	 */
	public String checkData(WgContract wgContract){
		String pk=wgContract.getPkContract();
		if(pk.equals("")){
			pk="***";
		}
		Integer count1 = wgContractDao.findCountBySql("select count(*) from wg_contract where nvl(dr,0)=0 and version<>0 and vbillno='"+wgContract.getVbillno()+"' and pk_contract!='"+pk+"'");
		if(count1>0){
			return "单据号已存在，请重新输入！";
		}
		if(wgContract.getBilltype().equals(AbsEnumType.BillType_HTXD)){
			pk=wgContract.getVsrcid();
		}
		Integer count2 = wgContractDao.findCountBySql("select count(*) from wg_contract where nvl(dr,0)=0 and version=-1 and htcode=replace('"+wgContract.getHtcode()+"',' ','') and pk_contract!='"+pk+"'");
		if(count2>0){
			return "合同号已存在，请重新输入！";
		}
		return "";
	}
	
	/**
	 * 删除数据
	 * @param wgContract
	 */
	@Transactional(readOnly=false)
	public String deleteData(List<String> listpk) {
		String msg="";
		for (String pk : listpk) {
			//校验
			WgContract newct=this.get(pk);
			if(!newct.getVbillstatus().equals(AbsEnumType.BillStatus_ZY)){
				msg+="合同号"+newct.getHtcode()+"不是自由态，无法删除！</br>";
			}else{
				//回写房源状态空置
				for (WgContractHouse house : newct.getWgContractHouseList()) {
					commonBaseDao.updateSql("update zl_housesource set housestate="+AbsEnumType.HOUSE_KZ+" where pk_housesource='"+house.getPkHouse()+"'");
				}
				//删除自身
				wgContractDao.deleteDataNewByPk(pk);
				wgContractCustDao.deleteDataNewByFk(pk);
				wgContractHouseDao.deleteDataNewByFk(pk);
				wgContractWyfDao.deleteDataNewByFk(pk);
				wgContractZqfyDao.deleteDataNewByFk(pk);
				wgContractYjDao.deleteDataNewByFk(pk);
				wgContractZltypeDao.deleteDataNewByFk(pk);
				wgContractWytypeDao.deleteDataNewByFk(pk);
				wgContractMzqDao.deleteDataNewByFk(pk);
				wgContractZzqDao.deleteDataNewByFk(pk);
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
		String msg="";
		for(String pk:listpk){
			WgContract newct=this.get(pk);
			if(newct.getVbillstatus().equals(AbsEnumType.BillStatus_SPTG)){
				continue;
			}
			if(newct.getBilltype().equals(AbsEnumType.BillType_HT)){//合同管理
				WgContract newctclone=(WgContract) newct.clone();//复制一份0号版本
				newctclone.setPkContract(null);//清空主键
				newctclone.setId(null);
				newctclone.setVersion(0);//版本号设为0
				newctclone.setVsrcid(newct.getPkContract());
				wgContractDao.insert(newctclone);
				for (WgContractCust wgContractCust : newctclone.getWgContractCustList()){
					wgContractCust.setPkContract(newctclone);
					wgContractCust.setPkContractCust(null);
					wgContractCust.setId(null);
					wgContractCustDao.insert(wgContractCust);
				}
				for (WgContractHouse wgContractHouse : newctclone.getWgContractHouseList()){
					wgContractHouse.setPkContract(newctclone);
					wgContractHouse.setPkContractHouse(null);
					wgContractHouse.setId(null);
					wgContractHouseDao.insert(wgContractHouse);
				}
				for (WgContractWyf wgContractWyf : newctclone.getWgContractWyfList()){
					wgContractWyf.setPkContract(newctclone);
					wgContractWyf.setPkContractWyf(null);
					wgContractWyf.setId(null);
					wgContractWyfDao.insert(wgContractWyf);
				}
				for (WgContractZqfy wgContractZqfy : newctclone.getWgContractZqfyList()){
					wgContractZqfy.setPkContract(newctclone);
					wgContractZqfy.setPkContractZqfy(null);
					wgContractZqfy.setId(null);
					wgContractZqfyDao.insert(wgContractZqfy);
				}
				for (WgContractYj wgContractYj : newctclone.getWgContractYjList()){
					wgContractYj.setPkContract(newctclone);
					wgContractYj.setPkContractYj(null);
					wgContractYj.setId(null);
					wgContractYjDao.insert(wgContractYj);
				}
				for (WgContractZltype wgContractZltype : newctclone.getWgContractZltypeList()){
					wgContractZltype.setPkContract(newctclone);
					wgContractZltype.setPkContractZltype(null);
					wgContractZltype.setId(null);
					wgContractZltypeDao.insert(wgContractZltype);
				}
				for (WgContractWytype wgContractWytype : newctclone.getWgContractWytypeList()){
					wgContractWytype.setPkContract(newctclone);
					wgContractWytype.setPkContractWytype(null);
					wgContractWytype.setId(null);
					wgContractWytypeDao.insert(wgContractWytype);
				}
				
				for (WgContractMzq wgContractMzq : newctclone.getWgContractMzqList()){
					wgContractMzq.setPkContract(newctclone);
					wgContractMzq.setPkContractMzq(null);
					wgContractMzq.setId(null);
					wgContractMzqDao.insert(wgContractMzq);
				}
				for (WgContractZzq wgContractZzq : newctclone.getWgContractZzqList()){
					wgContractZzq.setPkContract(newctclone);
					wgContractZzq.setPkContractZzq(null);
					wgContractZzq.setId(null);
					wgContractZzqDao.insert(wgContractZzq);
				}
				
				//更新自身
				newct.setTs(new Date());
				newct.setVbillstatus(AbsEnumType.BillStatus_SPTG);
				newct.setHtstatus(AbsEnumType.HtStatus_QZ);
				newct.setApprover(UserUtils.getUser());
				newct.setApprovedtime(new Date());
				wgContractDao.update(newct);
				
				WgContract newentity=CalculateRentUtils.recalRent2(this.get(newct.getPkContract()));
				if(!newct.getIswyht().equals(Global.YES)){
					//生成年租金、拆分数据
					for (WgContractRentprice wgContractRentprice : newentity.getWgContractRentpriceList()) {
						wgContractRentprice.setPkContract(newct);
						wgContractRentprice.setTs(new Date());
						wgContractRentpriceDao.insert(wgContractRentprice);
					}
					CalculateRentUtils.recalYwcf2(newentity);
					for (WgContractYwcf wgContractYwcf : newentity.getWgContractYwcfList()) {
						wgContractYwcf.setPkContract(newct);
						wgContractYwcf.setTs(new Date());
						wgContractYwcfDao.insert(wgContractYwcf);
					}
				}
				CalculateRentUtils.recalWyfcf2(newentity);
				for (WgContractWyfcf wgContractWyfcf : newentity.getWgContractWyfcfList()) {
					wgContractWyfcf.setPkContract(newct);
					wgContractWyfcf.setTs(new Date());
					wgContractWyfcfDao.insert(wgContractWyfcf);
				}
				CalculateRentUtils.recalZqfycf(newentity);
				for (WgContractZqfycf wgContractZqfycf : newentity.getWgContractZqfycfList()) {
					wgContractZqfycf.setPkContract(newct);
					wgContractZqfycf.setTs(new Date());
					wgContractZqfycfDao.insert(wgContractZqfycf);
				}
				CalculateRentUtils.recalSrcf(newentity);
				for (WgContractSrcf wgContractSrcf : newentity.getWgContractSrcfList()) {
					wgContractSrcf.setPkContract(newct);
					wgContractSrcf.setTs(new Date());
					wgContractSrcfDao.insert(wgContractSrcf);
				}
				
				//推租约待开账
				sendToCtRentdkz(newentity);
				
			}else if(newct.getBilltype().equals(AbsEnumType.BillType_HTXD)){//合同修订
				//回写合同
				
				//更新自身
				newct.setTs(new Date());
				newct.setVbillstatus(AbsEnumType.BillStatus_SPTG);
				newct.setApprover(UserUtils.getUser());
				newct.setApprovedtime(new Date());
				wgContractDao.update(newct);
			}
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
			WgContract newct=this.get(pk);
			if(newct.getVbillstatus().equals(AbsEnumType.BillStatus_ZY)){
				continue;
			}
			if(newct.getBilltype().equals(AbsEnumType.BillType_HT)){//合同管理
				//判断单据是否被下游参照（修订、退租）
				if(newct.getHtstatus().equals(AbsEnumType.HtStatus_TZ)){
					msg+="单据号"+newct.getVbillno()+"合同状态为退租，无法取消审批！</br>";
					continue;
				}
				Integer countxy=wgContractDao.findCountBySql("select count(*) from wg_contract where version=-1 and nvl(dr,0)=0 and vdef1='"+newct.getPkContract()+"'");
				if(countxy>0){
					msg+="该合同已经续约，不可取消审批！</br>";
					continue;
				}
				Integer count0=wgContractDao.findCountBySql("select count(*) from wg_throwalease where nvl(dr,0)=0 and vsrcid='"+newct.getPkContract()+"'");
				if(count0>0){
					msg+="单据号"+newct.getVbillno()+"存在下游退租管理，无法取消审批！</br>";
					continue;
				}
				Integer count=wgContractDao.checkExistXDByVsrcid(newct.getPkContract());
				if(count>0){
					msg+="单据号"+newct.getVbillno()+"存在下游合同修订，无法取消审批！</br>";
					continue;
				}
				Integer count1=wgContractDao.findCountBySql("select count(*) from ct_rentbill r where r.vsrcid='"+newct.getPkContract()+"' and nvl(r.dr,0)=0");
				if(count1>0){
					msg+="单据号"+newct.getVbillno()+"已经生成账单，无法取消审批！</br>";
					continue;
				}
				String sqlyj="select count(*) from ct_charge_sk_b skb where nvl(skb.dr,0)=0 and skb.lyvbillno in (select ys.pk_charge_ys from ct_charge_ys ys "
						+ "where nvl(ys.dr,0)=0 and ys.vsrcid2 in (select yj.pk_contract_yj from wg_contract_yj yj where nvl(yj.dr,0)=0 and yj.pk_contract='"+newct.getPkContract()+"'))";
				Integer count2=wgContractDao.findCountBySql(sqlyj);
				if(count2>0){
					msg+="单据号"+newct.getVbillno()+"保证金已经收款，无法取消审批！</br>";
					continue;
				}
				
				//删除0号版本
				wgContractDao.deleteSql("delete from wg_contract_cust b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//客户信息
				wgContractDao.deleteSql("delete from wg_contract_house b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//房产信息
				wgContractDao.deleteSql("delete from wg_contract_wyf b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//物业费
				wgContractDao.deleteSql("delete from wg_contract_zqfy b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//其他周期费用
				wgContractDao.deleteSql("delete from wg_contract_yj b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//押金
				wgContractDao.deleteSql("delete from wg_contract_zltype b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//租赁支付方式
				wgContractDao.deleteSql("delete from wg_contract_wytype b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//物业支付方式
				wgContractDao.deleteSql("delete from wg_contract_mzq b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//免租期
				wgContractDao.deleteSql("delete from wg_contract_zzq b where b.pk_contract in "
						+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0)");//增长期
				wgContractDao.deleteSql("delete from wg_contract c where nvl(c.dr,0)=0 and c.vsrcid='"+newct.getPkContract()+"' and c.billtype='ct' and c.version=0");//表头
				
				//删除生成的年租金、拆分数据
				wgContractDao.deleteSql("delete from wg_contract_rentprice cr where cr.pk_contract='"+newct.getPkContract()+"'");//年租金
				wgContractDao.deleteSql("delete from wg_contract_ywcf yw where yw.pk_contract='"+newct.getPkContract()+"'");//业务拆分
				wgContractDao.deleteSql("delete from wg_contract_wyfcf wyf where wyf.pk_contract='"+newct.getPkContract()+"'");//物业费拆分
				wgContractDao.deleteSql("delete from wg_contract_zqfycf zqcf where zqcf.pk_contract='"+newct.getPkContract()+"'");//其他周期费用拆分
				wgContractDao.deleteSql("delete from wg_contract_srcf cs where cs.pk_contract='"+newct.getPkContract()+"'");//收入拆分
				//删除生成待开账
				wgContractDao.deleteSql("delete from ct_rentdkz where pk_contract='"+newct.getPkContract()+"'");
				//删除生成应收
				wgContractDao.deleteSql("delete from ct_charge_ys ys where nvl(ys.dr,0)=0 and ys.vsrcid2 in (select yj.pk_contract_yj from wg_contract_yj yj "
						+ "where nvl(yj.dr,0)=0 and yj.pk_contract='"+newct.getPkContract()+"')");
				//删除税率变更记录
				wgContractDao.deleteSql("delete from wg_contract_tax tax where tax.pk_contract='"+newct.getPkContract()+"'");
				
				//更新自身
				newct.setTs(new Date());
				newct.setVbillstatus(AbsEnumType.BillStatus_ZY);
				newct.setHtstatus(AbsEnumType.HtStatus_ZY);
				newct.setApprover(null);
				newct.setApprovedtime(null);
				wgContractDao.update(newct);
			}else if(newct.getBilltype().equals(AbsEnumType.BillType_HTXD)){//合同修订
				//更新自身
				newct.setTs(new Date());
				newct.setVbillstatus(AbsEnumType.BillStatus_ZY);
				newct.setHtstatus(AbsEnumType.HtStatus_ZY);
				newct.setApprover(null);
				newct.setApprovedtime(null);
				wgContractDao.update(newct);
			}
		}
		return msg;
	}
	
	/**
	 * 参照转换
	 * @param pk
	 * @return
	 */
	public WgContract refDataRender(String pk){
		WgContract newct=this.get(pk);
		newct.setPkContract(null);//清空主键
		newct.setId(null);
		newct.setVbillstatus(AbsEnumType.BillStatus_ZY);
		newct.setBilltype(AbsEnumType.BillType_HTXD);
		newct.setVersion(wgContractDao.findMaxVersionByVsrcid(pk));//设置最大版本号
		newct.setVsrcid(pk);
		newct.setVsrctype(AbsEnumType.BillType_HT);
		newct.setCreator(null);
		newct.setCreatedtime(null);
		newct.setModifier(null);
		newct.setModifiedtime(null);
		newct.setApprover(null);
		newct.setApprovedtime(null);
		for (WgContractCust wgContractCust : newct.getWgContractCustList()){
			wgContractCust.setPkContract(null);
			wgContractCust.setPkContractCust(null);
			wgContractCust.setId(null);
		}
		for (WgContractHouse wgContractHouse : newct.getWgContractHouseList()){
			wgContractHouse.setPkContract(null);
			wgContractHouse.setPkContractHouse(null);
			wgContractHouse.setId(null);
		}
		for (WgContractWyf wgContractWyf : newct.getWgContractWyfList()){
			wgContractWyf.setPkContract(null);
			wgContractWyf.setPkContractWyf(null);
			wgContractWyf.setId(null);
		}
		for (WgContractZqfy wgContractZqfy : newct.getWgContractZqfyList()){
			wgContractZqfy.setPkContract(null);
			wgContractZqfy.setPkContractZqfy(null);
			wgContractZqfy.setId(null);
		}
		for (WgContractYj wgContractYj : newct.getWgContractYjList()){
			wgContractYj.setPkContract(null);
			wgContractYj.setPkContractYj(null);
			wgContractYj.setId(null);
		}
		for (WgContractZltype wgContractZltype : newct.getWgContractZltypeList()){
			wgContractZltype.setPkContract(null);
			wgContractZltype.setPkContractZltype(null);
			wgContractZltype.setId(null);
		}
		for (WgContractWytype wgContractWytype : newct.getWgContractWytypeList()){
			wgContractWytype.setPkContract(null);
			wgContractWytype.setPkContractWytype(null);
			wgContractWytype.setId(null);
		}
		
		for (WgContractMzq wgContractMzq : newct.getWgContractMzqList()){
			wgContractMzq.setPkContract(null);
			wgContractMzq.setPkContractMzq(null);
			wgContractMzq.setId(null);
		}
		for (WgContractZzq wgContractZzq : newct.getWgContractZzqList()){
			wgContractZzq.setPkContract(null);
			wgContractZzq.setPkContractZzq(null);
			wgContractZzq.setId(null);
		}
		return newct;
	}
	
	public String filterHouse(String pkHouse, String pkContract){
		String msg="";
		if(pkContract==null||pkContract.equals("")){
			pkContract="null";
		}
		String sql="select count(*) from wg_contract_house h where h.pk_house='"+pkHouse+"' and h.pk_contract in "
				+ "(select c.pk_contract from wg_contract c where nvl(c.dr,0)=0 and c.version=-1) "
				+ "and h.pk_contract!='"+pkContract+"' and nvl(h.dr,0)=0";
		Integer count=wgContractDao.findCountBySql(sql);
		if(count>0){
			msg="该房产已经被其他合同选用，请重新选择！";
		}
		return msg;
	}
	
	public void sendToCtRentdkz(WgContract wgContract){
		CtRentdkz dkz=new CtRentdkz();
		List<WgContractHouse> houses = wgContract.getWgContractHouseList();
		String pkBuild="";
		String pkHouse="";
		if(houses.size()>0){
			for (int i=0;i<houses.size();i++) {
				pkBuild+=houses.get(i).getPkBuild().getPkBuildingfile();
				pkHouse+=houses.get(i).getPkHouse().getPkHousesource();
				if(i<houses.size()-1){
					pkBuild+=",";
					pkHouse+=",";
				}
			}
		}
		dkz.setPkOrg(wgContract.getPkOrg());
		dkz.setPkProject(wgContract.getPkProject());
		dkz.setPkCustomer(wgContract.getWgContractCustList().get(0).getPkCustomer());
		dkz.setHtcode(wgContract.getHtcode());
		ZlBuildingfile build=new ZlBuildingfile();
		build.setPkBuildingfile(pkBuild);
		dkz.setPkBuilding(build);
		ZlHousesource house=new ZlHousesource();
		house.setPkHousesource(pkHouse);
		dkz.setPkHouse(house);
		dkz.setPkDept(wgContract.getPkDept());
		dkz.setNprice(wgContract.getNprice());
		dkz.setRenttype(wgContract.getRenttype());
		dkz.setDstartdate(wgContract.getDstartdate());
		dkz.setDenddate(wgContract.getDenddate());
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String sdate=df.format(new Date()).replaceAll("-", "");
		dkz.setVbillno(CodeFactoryUtils.createBillCodeByDr("DKZ"+sdate, "ct_rentdkz", "vbillno", 5));
		dkz.setPkContract(wgContract.getPkContract());
		dkz.setTs(new Date());
		dkz.setDr(0);
		ctRentdkzDao.insert(dkz);
	}
	
	public String getStgObj(Object obj){
		return obj==null?"":obj.toString();
	}
	
	public List<WgContractYwcf> getJhmnyDdetail(String pkContract){
		String sql="select * from "
				+ "(select f.PK_CONTRACT_YWCF,f.pk_contract as \"pkContract.pkContract\",f.pk_customer AS \"pkCustomer.pkCustomer\", f.pk_build AS \"pkBuild.pkBuildingfile\","
				+ " f.pk_house AS \"pkHouse.pkHousesource\",f.pk_costproject as \"pkCostproject.pkProject\", "
				+ "f.DSTARTDATE,f.DENDDATE,f.PK_COSTPROJECT PK_COSTPROJECT2,"
				+ "f.NRECMNY,f.NREALMNY,f.NTAXRATE,f.NTAXMNY,f.NNOTAXMNY,c1.code AS \"pkCustomer.code\", "
				+ "c1.name AS \"pkCustomer.name\", b1.code AS \"pkBuild.code\", b1.name AS \"pkBuild.name\","
				+ " h1.estatecode AS \"pkHouse.estatecode\", h1.estatename AS \"pkHouse.estatename\","
				+ "p.code as \"pkCostproject.code\",p.name as \"pkCostproject.name\",f.vdef1 as \"vdef1\",'ywcf' as \"vdef5\" from WG_CONTRACT_YWCF f "
				+ "LEFT JOIN bd_customer c1 ON c1.pk_customer = f.pk_customer LEFT JOIN zl_buildingfile b1 ON "
				+ "b1.pk_buildingfile = f.pk_build LEFT JOIN zl_housesource h1 ON h1.pk_housesource = f.pk_house "
				+ "left join BD_PROJECT p on p.PK_PROJECT=f.PK_COSTPROJECT where nvl(f.dr,0)=0 and f.PK_CONTRACT='"+pkContract+"'"
				+ " union all "
				+ "select w.PK_CONTRACT_WYFCF,w.pk_contract as \"pkContract.pkContract\",w.PK_CUSTOMER,w.PK_BUILD,w.PK_HOUSE,w.PK_COSTPROJECT,w.DSTARTDATE,w.DENDDATE,w.PK_COSTPROJECT,"
				+ "w.NRECMNY,w.NREALMNY,w.NTAXRATE,w.NTAXMNY,w.NNOTAXMNY,c1.code,c1.name,b1.code,b1.name,h1.estatecode,h1.estatename,p.code,p.name,w.vdef1 as \"vdef1\",'wyfcf' as \"vdef5\" "
				+ "from WG_CONTRACT_WYFCF w LEFT JOIN bd_customer c1 ON c1.pk_customer = w.pk_customer LEFT JOIN zl_buildingfile b1 "
				+ "ON b1.pk_buildingfile = w.pk_build LEFT JOIN zl_housesource h1 ON h1.pk_housesource = w.pk_house left join "
				+ "BD_PROJECT p on p.PK_PROJECT=w.PK_COSTPROJECT where nvl(w.dr,0)=0 and w.PK_CONTRACT='"+pkContract+"' "
				+ " union all "
				+ "select w.PK_CONTRACT_ZQFYCF,w.pk_contract as \"pkContract.pkContract\",w.PK_CUSTOMER,w.PK_BUILD,w.PK_HOUSE,w.PK_COSTPROJECT,w.DSTARTDATE,w.DENDDATE,w.PK_COSTPROJECT,"
				+ "w.NRECMNY,w.NREALMNY,w.NTAXRATE,w.NTAXMNY,w.NNOTAXMNY,c1.code,c1.name,b1.code,b1.name,h1.estatecode,h1.estatename,p.code,p.name,w.vdef1 as \"vdef1\",'zqfycf' as \"vdef5\" "
				+ "from WG_CONTRACT_ZQFYCF w LEFT JOIN bd_customer c1 ON c1.pk_customer = w.pk_customer LEFT JOIN zl_buildingfile b1 "
				+ "ON b1.pk_buildingfile = w.pk_build LEFT JOIN zl_housesource h1 ON h1.pk_housesource = w.pk_house left join "
				+ "BD_PROJECT p on p.PK_PROJECT=w.PK_COSTPROJECT where nvl(w.dr,0)=0 and w.PK_CONTRACT='"+pkContract+"') "
				+ " order by pk_costproject2,DSTARTDATE";
		return wgContractYwcfDao.getNjhmnyDetail(sql);
	}
	public void savePlanMny(WgContract wgContract){
		List<WgContractYwcf> list=wgContract.getWgContractYwcfList();
		List<WgContractYwcf> listyw=new ArrayList<WgContractYwcf>();
		List<WgContractWyfcf> listwy=new ArrayList<WgContractWyfcf>();
		for (WgContractYwcf yw : list) {
			if(yw.getVdef5().equals("ywcf")){
				listyw.add(yw);
			}else if(yw.getVdef5().equals("wyfcf")){
				WgContractWyfcf wyf=new WgContractWyfcf();
				wyf.setPkContract(yw.getPkContract());
				wyf.setPkContractWyfcf(yw.getPkContractYwcf());
				wyf.setNrecmny(yw.getNrecmny());
				wyf.setNnotaxmny(yw.getNnotaxmny());
				wyf.setNtaxmny(yw.getNtaxmny());
				wyf.setVdef1(yw.getVdef1());
				wyf.setVdef5(yw.getVdef5());
				listwy.add(wyf);
			}
		}
		
		if(listyw!=null&&listyw.size()>0){
			List<WgContractYwcf> listyw3=new ArrayList<WgContractYwcf>();
			Double allmny2=new Double(0);//修改前租金总金额
			Double allmny=new Double(0);//修改后租金总金额
			WgContract oldht=this.get(listyw.get(0).getPkContract());
			WgContractYwcf yw2=new WgContractYwcf();
			yw2.setPkContract(listyw.get(0).getPkContract());
			List<WgContractYwcf> listyw2=wgContractYwcfDao.findList(yw2);//获取数据库业务拆分数据
			oldht.setWgContractYwcfList(listyw2);
			for (WgContractYwcf ywcf2 : listyw2) {
				allmny2+=ywcf2.getNrecmny();
				for (int i=0;i<listyw.size();i++) {
					WgContractYwcf ywcf=listyw.get(i);
					if(ywcf.getPkContractYwcf().equals(ywcf2.getPkContractYwcf())){//主键相同
						if(ywcf.getNrecmny().compareTo(ywcf2.getNrecmny())!=0){//应收金额不同，记录
							ywcf2.setNrecmny(ywcf.getNrecmny());
							ywcf2.setNnotaxmny(ywcf.getNnotaxmny());
							ywcf2.setNtaxmny(ywcf.getNtaxmny());
							listyw3.add(ywcf2);
						}
						allmny+=ywcf.getNrecmny();
						listyw.remove(i);
						break;
					}
				}
			}
			if(listyw3!=null&&listyw3.size()>0){//更新业务拆分
				for (WgContractYwcf ywcf : listyw3) {
					ywcf.setTs(new Date());
					wgContractYwcfDao.update(ywcf);
				}
			}
			if(allmny.compareTo(allmny2)!=0){//修改前后租金总金额不等
				//重算摊销金额
				WgContractSrcf srcf2=new WgContractSrcf();
				srcf2.setPkContract(oldht);
				List<WgContractSrcf> srlist2=wgContractSrcfDao.findList(srcf2);//更新前摊销
				for (int i=0;i<srlist2.size();i++) {
					if(!srlist2.get(i).getVsrctype().equals("房租")){
						srlist2.remove(i);
						i--;
					}
				}
				CalculateRentUtils.recalSrcf(oldht);
				List<WgContractSrcf> srlist=oldht.getWgContractSrcfList();//重新计算后的摊销
				for (WgContractSrcf sr2 : srlist2) {
					for (int i=0;i<srlist.size();i++) {
						WgContractSrcf sr=srlist.get(i);
						if(sr.getDstartdate().compareTo(sr2.getDstartdate())==0
								&&sr.getPkHouse().getPkHousesource().equals(sr2.getPkHouse().getPkHousesource())){
							//同时间段数据值替换
							sr2.setNrecmny(sr.getNrecmny());
							sr2.setNnotaxmny(sr.getNnotaxmny());
							sr2.setNtaxmny(sr.getNtaxmny());
							srlist.remove(i);
							break;
						}
					}
					sr2.setTs(new Date());
					wgContractSrcfDao.update(sr2);
				}
			}
		}
		
		if(listwy!=null&&listwy.size()>0){
			List<WgContractWyfcf> listwy3=new ArrayList<WgContractWyfcf>();
			WgContract oldht=this.get(listwy.get(0).getPkContract());
			List<WgContractWyfcf> listwy2=oldht.getWgContractWyfcfList();
			for (WgContractWyfcf wyfcf2 : listwy2) {
				for (int i=0;i<listwy.size();i++) {
					WgContractWyfcf wyfcf=listwy.get(i);
					if(wyfcf.getPkContractWyfcf().equals(wyfcf2.getPkContractWyfcf())){//主键相同
						if(wyfcf.getNrecmny().compareTo(wyfcf2.getNrecmny())!=0){//应收金额不同，记录
							wyfcf2.setNrecmny(wyfcf.getNrecmny());
							wyfcf2.setNnotaxmny(wyfcf.getNnotaxmny());
							wyfcf2.setNtaxmny(wyfcf.getNtaxmny());
							listwy3.add(wyfcf2);
						}
						listwy.remove(i);
						break;
					}
				}
			}
			if(listwy3!=null&&listwy3.size()>0){//更新物业费拆分
				for (WgContractWyfcf wyfcf : listwy3) {
					wyfcf.setTs(new Date());
					wgContractWyfcfDao.update(wyfcf);
				}
				//重算摊销金额
				WgContractSrcf srcf2=new WgContractSrcf();
				srcf2.setPkContract(oldht);
				List<WgContractSrcf> srlist2=wgContractSrcfDao.findList(srcf2);//更新前摊销
				for (int i=0;i<srlist2.size();i++) {
					if(!srlist2.get(i).getVsrctype().equals("物业")){
						srlist2.remove(i);
						i--;
					}
				}
				CalculateRentUtils.recalSrcf(oldht);
				List<WgContractSrcf> srlist=oldht.getWgContractSrcfList();//重新计算后的摊销
				for (WgContractSrcf sr2 : srlist2) {
					for (int i=0;i<srlist.size();i++) {
						WgContractSrcf sr=srlist.get(i);
						if(sr.getDstartdate().compareTo(sr2.getDstartdate())==0
								&&sr.getPkHouse().getPkHousesource().equals(sr2.getPkHouse().getPkHousesource())){
							//同时间段数据值替换
							sr2.setNrecmny(sr.getNrecmny());
							sr2.setNnotaxmny(sr.getNnotaxmny());
							sr2.setNtaxmny(sr.getNtaxmny());
							srlist.remove(i);
							break;
						}
					}
					sr2.setTs(new Date());
					wgContractSrcfDao.update(sr2);
				}
			}
		}
	}
	
	public List<WgContractSrcf> getSrmnyDdetail(String pkContract){
		WgContractSrcf wf=new WgContractSrcf();
		wf.getSqlMap().getDataScope().addFilter("dsf", " a.pk_contract='"+pkContract+"' and a.vsrctype='房租'");
		return wgContractSrcfDao.findList(wf);
	}
	public void saveSrMny(WgContract wgContract){
		List<WgContractSrcf> list=wgContract.getWgContractSrcfList();//变更后收入拆分数据
		WgContractSrcf srcf=new WgContractSrcf();
		srcf.setPkContract(list.get(0).getPkContract());
		List<WgContractSrcf> list2=wgContractSrcfDao.findList(srcf);//数据库存储收入拆分数据
		List<WgContractSrcf> listsr=new ArrayList<WgContractSrcf>();
		for (WgContractSrcf sr2 : list2) {
			for(int i=0;i<list.size();i++){
				WgContractSrcf sr=list.get(i);
				if(sr.getPkContractSrcf().equals(sr2.getPkContractSrcf())){//主键相同
					if(sr.getNnotaxmny().compareTo(sr2.getNnotaxmny())!=0){//无税金额不一致，记录
						sr2.setNnotaxmny(sr.getNnotaxmny());
						sr2.setNrecmny(sr.getNrecmny());
						listsr.add(sr2);
					}
					list.remove(i);
				}
			}
		}
		if(listsr!=null&&listsr.size()>0){//更新业务拆分
			for (WgContractSrcf sr : listsr) {
				sr.setTs(new Date());
				wgContractSrcfDao.update(sr);
			}
		}
	}
	
	public WgContract getXyCont(WgContract wgContract){
		wgContract.setVdef1(wgContract.getPkContract());//vdef1存放被续约合同主键
		wgContract.setPkContract(null);
		wgContract.setHtcode(null);
		wgContract.setDstartdate(CalendarUtls.getAfterFirstDay(wgContract.getDenddate()));
		wgContract.setDenddate(null);
		wgContract.setDfirstfkdate(CalendarUtls.getBeforeNDay(wgContract.getDstartdate(), Global.getConfigToInteger("wght_prehtfirstfkdays", "10")));
		wgContract.setNtaxrate(null);
		wgContract.setExremarks(null);
		wgContract.setNprice(null);
		wgContract.setNdayprice(null);
		wgContract.setNmonthprice(null);
		wgContract.setNyearprice(null);
		wgContract.setPkDept(null);
		wgContract.setVersion(-1);
		wgContract.setVbillstatus(-1);
		wgContract.setHtstatus(-1);
		wgContract.setIsNewRecord(true);
		for (WgContractCust cust : wgContract.getWgContractCustList()) {
			cust.setPkContractCust(null);
			cust.setPkContract(null);
			cust.setIsNewRecord(true);
		}
		for (WgContractHouse house : wgContract.getWgContractHouseList()) {
			house.setPkContractHouse(null);
			house.setPkContract(null);
			house.setNprice(null);
			house.setNdayprice(null);
			house.setNmonthprice(null);
			house.setNmonthtzprice(null);
			house.setNmonthtzhprice(null);
			house.setNyearprice(null);
			house.setNyeartzprice(null);
			house.setNyeartzhprice(null);
			house.setIswy("N");
			house.setIsNewRecord(true);
		}
		wgContract.setWgContractWyfList(new ArrayList<WgContractWyf>());
		wgContract.setWgContractZqfyList(new ArrayList<WgContractZqfy>());
		wgContract.setWgContractYjList(new ArrayList<WgContractYj>());
		wgContract.setWgContractZltypeList(new ArrayList<WgContractZltype>());		// 子表租赁支付方式
		wgContract.setWgContractWytypeList(new ArrayList<WgContractWytype>());		// 子表物业支付方式
		wgContract.setWgContractMzqList(new ArrayList<WgContractMzq>());		// 子表免租期
		wgContract.setWgContractZzqList(new ArrayList<WgContractZzq>());		// 子表增长期
		wgContract.setWgContractRentpriceList(new ArrayList<WgContractRentprice>());		// 子表年租金
		wgContract.setWgContractYwcfList(new ArrayList<WgContractYwcf>());		// 子表业务拆分
		wgContract.setWgContractWyfcfList(new ArrayList<WgContractWyfcf>());		// 子表物业费拆分
		wgContract.setWgContractZqfycfList(new ArrayList<WgContractZqfycf>());		// 子表其他周期费用拆分
		wgContract.setWgContractSrcfList(new ArrayList<WgContractSrcf>());		// 子表收入拆分
		return wgContract;
	}

	public Map<String, String> getMaxTaxChangeDate(String pkContract) {
		Map<String, String> map=new HashMap<String, String>();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM");
		Map<String, Object> map2=commonBaseDao.selectMapResult("select a.dstartdate,a.ntaxrate from (select * from wg_contract_tax where pk_contract='"+pkContract+"' order by dstartdate desc) a where rownum=1");
		WgContract wght=this.get(pkContract);
		String dmin=df.format(wght.getDstartdate());
		String tax=wght.getNtaxrate().toString();
		if(map2!=null){
			Object dmin2=map2.get("DSTARTDATE");
			Object tax2=map2.get("NTAXRATE");
			if(dmin2!=null){
				dmin=df.format(dmin2);
			}
			if(tax2!=null){
				tax=tax2.toString();
			}
		}
		map.put("dmin", dmin.toString());
		map.put("dmax", df.format(this.get(pkContract).getDenddate()));
		map.put("tax", tax.toString());
		return map;
	}
	public String saveTaxChange(String pkContract,Date dstart, Integer taxrate){
		SimpleDateFormat df=new SimpleDateFormat("yyy-MM-dd");
		//校验是否可以变更
		Integer count1=commonBaseDao.selectCount("select count(*) from ct_charge_yqrsr yqr left join wg_contract_srcf srcf on srcf.pk_contract_srcf=yqr.vsrcid2 where nvl(yqr.dr,0)=0 "
				+ "and srcf.pk_contract='"+pkContract+"' and exists (select 1 from ct_charge_srqr_b srb where nvl(srb.dr,0)=0 and srb.lyvbillno=yqr.pk_charge_yqrsr "
				+ "and to_char(srb.fyksdate,'YYYY-MM-DD')>='"+df.format(dstart)+"') and srcf.vsrctype='房租'");
		if(count1>0){
			return "该变更日期之后含有确认收入单，无法以此时间节点开始变更，请重新选择！";
		}
		Integer count2=commonBaseDao.selectCount("select count(*) from ct_charge_yqrsr yqr left join wg_contract_srcf srcf on srcf.pk_contract_srcf=yqr.vsrcid2 where nvl(yqr.dr,0)=0 "
				+ "and srcf.pk_contract='"+pkContract+"' and exists (select 1 from ct_charge_srqr_b srb left join ct_charge_srqr sr on sr.pk_charge_srqr=srb.pk_charge_srqr "
				+ "where nvl(srb.dr,0)=0 and nvl(sr.dr,0)=0 and srb.lyvbillno=yqr.pk_charge_yqrsr and sr.vbillstatus!=1) and srcf.vsrctype='房租'");
		if(count2>0){
			return "该合同含有未审批确认收入单，请先审批！";
		}
		WgContract wght=this.get(pkContract);
		//获取已经开票金额
		Object nallkpmny=commonBaseDao.selectObject("select nvl(sum(nvl(isk.nsqkpmny,0)),0) nallkpmny from ct_invoiceapply_skmx isk left join ct_invoiceapply i on i.pk_invoiceapply=isk.pk_invoiceapply "
				+ "where nvl(isk.dr, 0) = 0 and nvl(i.dr,0)=0 and i.vbillstatus=1 and isk.vsrcid in (select ys.pk_charge_ys from ct_charge_ys ys left join wg_contract_ywcf yw on yw.pk_contract_ywcf = ys.vsrcid2 "
				+ "where nvl(ys.dr, 0) = 0 and nvl(yw.dr, 0) = 0 and yw.pk_contract = '"+pkContract+"')");
		//获取税率变更记录
		List<WgContractTax> listtax=wgContractTaxDao.fingListBySql("select * from wg_contract_tax where pk_contract='"+pkContract+"' order by dstartdate desc");
		Double nprekp=new Double(0);//之前已开票金额
		Double nkpnotaxmny=new Double(0);//之前已开票无税金额
		if(listtax!=null&&listtax.size()>0){
			for (WgContractTax tax : listtax) {
				nprekp+=tax.getNkpmny();
				if(tax.getNkpmny()!=null&&tax.getNkpmny().compareTo(new Double(0))!=0){ 
					nkpnotaxmny+=tax.getNkpmny()/(new Double(1)+new Double(tax.getNtaxrate())/new Double(100));
				}
			}
		}
		Double nsckpmny=getDbObj(nallkpmny)-nprekp;//上一个税率可开票金额
		Double nallkpnotax=nkpnotaxmny+nsckpmny/(new Double(1)+new Double(wght.getNtaxrate())/new Double(100));//所有已开票无税金额
		//合同金额
		Object nallhtmny=commonBaseDao.selectObject("select sum(nrecmny) from wg_contract_ywcf where pk_contract='"+pkContract+"' and nvl(dr,0)=0");
		Double nwkpnotax=(getDbObj(nallhtmny)-getDbObj(nallkpmny))/(new Double(1)+new Double(taxrate)/new Double(100));//未开票无税金额
		//时间节点前应确认无税金额
		Object nyqrmny=commonBaseDao.selectObject("select sum(nvl(sr.nnotaxmny,0)) qrsr from wg_contract_srcf sr where nvl(sr.dr, 0) = 0 and sr.pk_contract = '"+pkContract+"' "
				+ "and sr.vsrctype = '房租' and to_char(sr.dstartdate, 'YYYY-MM-DD') < '"+df.format(dstart)+"'");
		
		Double nsyallnotax=getDbobjBy2w(nallkpnotax+nwkpnotax-getDbObj(nyqrmny));//剩余总摊销金额
		Double nysmny=getDbobjBy2w(nsyallnotax*(new Double(taxrate)/new Double(100)+new Double(1)));//剩余应收
		Double ntaxmny=nysmny-nsyallnotax;
		//获取每天摊销金额 ((已经开票金额/之前税率+未开票金额/变更税率)-已经确认金额)/开始变更日期之后天数
		Double ndaynotax=getDbobjBy2w(nsyallnotax/CalendarUtls.getBetweenTwoDays(dstart, wght.getDenddate()));
		Double ysmny = new Double(0);
		Double notaxmny=new Double(0);
		Double taxmny=new Double(0);
		WgContractSrcf srcf2=new WgContractSrcf();
		srcf2.setPkContract(wght);
		srcf2.setVsrctype("房租");
		List<WgContractSrcf> srlist2=wgContractSrcfDao.findList(srcf2);
		for(WgContractSrcf srcf : srlist2){
			if(srcf.getDstartdate().compareTo(dstart)<0){
				continue;
			}
			//租金分摊，并且时间在变更时间之后的金额重新计算
			Integer days=CalendarUtls.getBetweenTwoDays(srcf.getDstartdate(), srcf.getDenddate());
			Double yqrnotaxmny=ndaynotax*days;//无税金额
			Double mny1=getDbobjBy2w(yqrnotaxmny*(new Double(taxrate)/new Double(100)+new Double(1)));//应确认
			Double mny2=mny1-yqrnotaxmny;//税额
			
			if (srcf.getDenddate().compareTo(wght.getDenddate())==0) {//拆分最后一笔，倒减
				srcf.setNrecmny(nysmny-ysmny);
				srcf.setNnotaxmny(nsyallnotax-notaxmny);
				srcf.setNtaxmny(ntaxmny-taxmny);
			} else {
				srcf.setNrecmny(mny1);
				srcf.setNnotaxmny(yqrnotaxmny);
				srcf.setNtaxmny(mny2);
			}
			srcf.setNtaxrate(new Double(taxrate));
			srcf.setTs(new Date());
			wgContractSrcfDao.update(srcf);
			ysmny += mny1;
			notaxmny += yqrnotaxmny;
			taxmny += mny2;
		}
		
		//更新账单
		commonBaseDao.updateSql("update ct_rentbill_srft rsr set rsr.nyqrmny=(select srcf1.nrecmny from wg_contract_srcf srcf1 where srcf1.pk_contract_srcf=rsr.vsrcid),"
				+ "rsr.taxrate=(select srcf2.ntaxrate from wg_contract_srcf srcf2 where srcf2.pk_contract_srcf=rsr.vsrcid),"
				+ "rsr.ntaxmny=(select srcf3.ntaxmny from wg_contract_srcf srcf3 where srcf3.pk_contract_srcf=rsr.vsrcid),"
				+ "rsr.nnotaxmny=(select srcf4.nnotaxmny from wg_contract_srcf srcf4 where srcf4.pk_contract_srcf=rsr.vsrcid) "
				+ "where rsr.vsrcid in (select srcf.pk_contract_srcf from wg_contract_srcf srcf where srcf.vsrctype='房租' and nvl(srcf.dr,0)=0 "
				+ "and srcf.pk_contract='"+pkContract+"' and to_char(srcf.dstartdate,'YYYY-MM-DD')>='"+df.format(dstart)+"')");
		//更新应确认收入
		commonBaseDao.updateSql("update ct_charge_yqrsr yqr set yqr.nyqrsrmny=(select srcf1.nrecmny from wg_contract_srcf srcf1 where srcf1.pk_contract_srcf=yqr.vsrcid2),"
				+ "yqr.tax_rate=(select srcf2.ntaxrate from wg_contract_srcf srcf2 where srcf2.pk_contract_srcf=yqr.vsrcid2),"
				+ "yqr.tax_amount=(select srcf3.ntaxmny from wg_contract_srcf srcf3 where srcf3.pk_contract_srcf=yqr.vsrcid2),"
				+ "yqr.no_tax_amount=(select srcf4.nnotaxmny from wg_contract_srcf srcf4 where srcf4.pk_contract_srcf=yqr.vsrcid2) "
				+ "where yqr.vsrcid2 in (select srcf.pk_contract_srcf from wg_contract_srcf srcf where srcf.vsrctype='房租' and nvl(srcf.dr,0)=0 "
				+ "and srcf.pk_contract='"+pkContract+"' and to_char(srcf.dstartdate,'YYYY-MM-DD')>='"+df.format(dstart)+"')");
		
		if(listtax!=null&&listtax.size()>0){
			listtax.get(0).setNkpmny(nsckpmny);
			//更新上次开票金额
			wgContractTaxDao.update(listtax.get(0));
		}else{
			//没有变更记录时增加初始值
			WgContractTax newtax=new WgContractTax();
			newtax.setPkContract(pkContract);
			newtax.setHtcode(wght.getHtcode());
			newtax.setDstartdate(wght.getDstartdate());
			newtax.setNtaxrate(wght.getNtaxrate());
			newtax.setChanger(wght.getCreator());
			newtax.setDchangedate(wght.getCreatedtime());
			newtax.setNkpmny(nsckpmny);
			wgContractTaxDao.insert(newtax);
		}
		//插入最新一次税率变更
		WgContractTax newtax=new WgContractTax();
		newtax.setPkContract(pkContract);
		newtax.setHtcode(wght.getHtcode());
		newtax.setDstartdate(dstart);
		newtax.setNtaxrate(taxrate);
		newtax.setChanger(UserUtils.getUser());
		newtax.setDchangedate(new Date());
		newtax.setNkpmny(new Double(0));
		wgContractTaxDao.insert(newtax);
		//更新合同表头税率
		wght.setNtaxrate(taxrate);
		wgContractDao.update(wght);
		return "";
	}
	
	public Map<String, Object> getPrintModel(String pkContract, String templetname){
		String sql="select * from v_wgprint_"+templetname+" where pk_contract='"+pkContract+"'";
		Map<String, Object> map=commonBaseDao.selectMapResult(sql);
		return map;
	}
	
	public Double getDbObj(Object obj){
		return obj==null?new Double(0):new Double(obj.toString());
	}
	
	private Double getDbobjBy2w(Double ud){
		return new BigDecimal(ud).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public List<Map<String, Object>> getHtfkDetail(String pkContract) {
		WgContract wgContract=this.get(pkContract);
		List<WgContractYj> listyj=wgContract.getWgContractYjList();
		List<WgContractYwcf> listyw=wgContractYwcfDao.getNjhmnyDetail("select * from wg_contract_ywcf where pk_contract='"+pkContract+"' and nvl(dr,0)=0 order by dstartdate");
		List<Map<String, Object>> fkDetail=new ArrayList<Map<String,Object>>();
		for (int i = 0; i < listyw.size(); i++) {
			Map<String, Object> map=new HashMap<String, Object>();
			if(i==0){//第一行
				for (WgContractYj yj : listyj) {
					String pkcost=yj.getPkYsproject().getPkProject();
					if(pkcost.equals("1198817182191144960")){//履约保证金
						map.put("htyj", yj.getNrecmny());
					}
					if(pkcost.equals("1198817297039577088")){//设备押金
						map.put("sbyj", yj.getNrecmny());
					}
				}
			}
			Integer tqfkdays=CalendarUtls.getBetweenTwoDays2(wgContract.getDfirstfkdate(), wgContract.getDstartdate());
			Date ywstart=listyw.get(i).getDstartdate();//业务开始
			Date ywend=listyw.get(i).getDenddate();//业务结束
			Integer months=CalendarUtls.getBetweenMonths(ywstart, ywend);
			Integer days=CalendarUtls.getLeftDays(ywstart, ywend);
			Integer years=(int) Math.floor(months/12);
			months=months-years*12;
			String fzfkqj="";//房租期间
			if(years>0){
				fzfkqj+=years+"年";
			}
			if(months>0){
				fzfkqj+=months+"个月";
			}
			if(days>0){
				fzfkqj+=days+"天";
			}
			SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd");
			Date fkdate=CalendarUtls.getBeforeNDay(ywstart, tqfkdays);
			map.put("fkdate", df.format(fkdate));
			map.put("fzfkqj", fzfkqj);
			map.put("nmny", listyw.get(i).getNrecmny());
			map.put("zlqj", df.format(ywstart)+"-"+df.format(ywend));
			fkDetail.add(map);
		}
		return fkDetail;
	}
	
	public List<Map<String, Object>> getWyfkDetail(String pkContract) {
		WgContract wgContract=this.get(pkContract);
		List<WgContractWyfcf> listwy=wgContractWyfcfDao.getNjhmnyDetail("select * from wg_contract_wyfcf where pk_contract='"+pkContract+"' and nvl(dr,0)=0 order by dstartdate");
		List<Map<String, Object>> fkDetail=new ArrayList<Map<String,Object>>();
		for (int i = 0; i < listwy.size(); i++) {
			Map<String, Object> map=new HashMap<String, Object>();
			Integer tqfkdays=CalendarUtls.getBetweenTwoDays2(wgContract.getDfirstfkdate(), wgContract.getDstartdate());
			Date wystart=listwy.get(i).getDstartdate();//物业开始
			Date wyend=listwy.get(i).getDenddate();//物业结束
			Integer months=CalendarUtls.getBetweenMonths(wystart, wyend);
			Integer days=CalendarUtls.getLeftDays(wystart, wyend);
			Integer years=(int) Math.floor(months/12);
			months=months-years*12;
			String fzfkqj="";//房租期间
			if(years>0){
				fzfkqj+=years+"年";
			}
			if(months>0){
				fzfkqj+=months+"个月";
			}
			if(days>0){
				fzfkqj+=days+"天";
			}
			SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd");
			Date fkdate=CalendarUtls.getBeforeNDay(wystart, tqfkdays);
			map.put("fkdate", df.format(fkdate));
			map.put("fzfkqj", fzfkqj);
			map.put("nmny", listwy.get(i).getNrecmny());
			map.put("zlqj", df.format(wystart)+"-"+df.format(wyend));
			fkDetail.add(map);
		}
		return fkDetail;
	}
	
}