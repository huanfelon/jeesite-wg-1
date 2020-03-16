/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.dao.CommonBaseDao;
import com.jeesite.modules.ct.dao.CtChargeSkBDao;
import com.jeesite.modules.ct.dao.CtChargeYsDao;
import com.jeesite.modules.ct.dao.CtInvoiceapplySkmxDao;
import com.jeesite.modules.ct.entity.CtChargeSkB;
import com.jeesite.modules.ct.entity.CtChargeYs;
import com.jeesite.modules.ct.entity.CtInvoiceapplySkmx;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 应收单Service
 * @author GJ
 * @version 2019-11-04
 */
@Service
@Transactional(readOnly=true)
public class CtChargeYsService extends CrudService<CtChargeYsDao, CtChargeYs> {
	
	@Autowired
	private CtChargeSkBDao ctChargeSkBDao;
	
	@Autowired
	private CtChargeYsDao ctChargeYsDao;
	@Autowired
	private CtInvoiceapplySkmxDao ctInvoiceapplySkmxDao;
	
	@Autowired
	private CommonBaseDao commonBaseDao;
	/**
	 * 获取单条数据
	 * @param ctChargeYs
	 * @return
	 */
	@Override
	public CtChargeYs get(CtChargeYs ctChargeYs) {
		return super.get(ctChargeYs);
	}
	
	/**
	 * 查询分页数据
	 * @param ctChargeYs 查询条件
	 * @param ctChargeYs.page 分页对象
	 * @return
	 */
	@Override
	public Page<CtChargeYs> findPage(CtChargeYs ctChargeYs) {
		return super.findPage(ctChargeYs);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ctChargeYs
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CtChargeYs ctChargeYs) {
		super.save(ctChargeYs);
	}
	
	/**
	 * 更新状态
	 * @param ctChargeYs
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CtChargeYs ctChargeYs) {
		super.updateStatus(ctChargeYs);
	}
	
	/**
	 * 删除数据
	 * @param ctChargeYs
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CtChargeYs ctChargeYs) {
		ctChargeYsDao.deleteData(ctChargeYs.getPkChargeYs());
		//更新合同押金页签
		/*String htsql="update wg_contract_yj set vdef1='N' where pk_contract_yj ='"+ctChargeYs.getVsrcid2()+"'";
		commonService.updateSql(htsql);*/
	}
	//审批应收单
	public void subData(String pks){
    CtChargeYs chargeYs = this.get(pks);
    chargeYs.setVbillstatus(AbsEnumType.BillStatus_SPTG);
    chargeYs.setApprover(UserUtils.getUser());
    chargeYs.setApprovetime(new Date());
    this.save(chargeYs);
		
	}
	//校验有没有被下游收款单参照，被参照则不可取消审批
	 public boolean  unSubYs(String pks){
		   List<CtChargeSkB> chargeSks = ctChargeSkBDao.getCtChargeSkBByCode(pks);
		   if(chargeSks.size()>0){
				return false;
			}
			return true;
	 }
	//校验有没有被下游开票申请参照，被参照则不可取消审批
		 public boolean  unSubYs1(String pks){
			   List<CtInvoiceapplySkmx> chargeSks = ctInvoiceapplySkmxDao.getCtInvoiceapplySkmxByCode(pks);
			   if(chargeSks.size()>0){
					return false;
				}
				return true;
		 }
	//取消审批
	public void unSubData(String pks){
		 CtChargeYs chargeYs = this.get(pks);
		    chargeYs.setVbillstatus(AbsEnumType.BillStatus_ZY);
		    chargeYs.setApprover(null);
		    chargeYs.setApprovetime(null);
		    this.save(chargeYs);
	}
	//收款单参照应收单,已参照但未审批的收款单不可再次被参照
	public List<CtChargeYs> refListData(){
		List<CtChargeYs> list= new ArrayList<CtChargeYs>();
		List<String> pklist = ctChargeYsDao.getCtChargeYsByCode2();
		if (pklist.size()<=0) {
			return null;
		}
		for(String s:pklist){
			list.add(this.get(s));
		}
		return list;
	}

	public Map<String, Object> getPrintModel(String pkChargeYs) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		CtChargeYs ys=this.get(pkChargeYs);
		String sql="select b1.orgname,b1.bankno,b1.bankname,b1.custname,b1.yfyear,b1.yfmonth,b1.yfday,b1.proname,b1.buildname,b1.room,nvl(b1.nysmny,0)+nvl(b2.nysmny,0)+nvl(b3.zjmny,0) allmny,"
				+ "b1.nysmny fzmny,b1.fzqj,b2.nysmny wymny,b2.wyqj,b3.zjmny,b3.zj "
				+ "from (select a.orgname,a.bankno,a.bankname,a.custname,a.yfyear,a.yfmonth,a.yfday,a.proname,a.buildname,a.room,a.nysmny,"
				+ "to_char(a.dstart,'YYYY\"年\"MM\"月\"DD\"日\"')||'~'||to_char(a.dend,'YYYY\"年\"MM\"月\"DD\"日\"') fzqj from v_ct_cjd a "
				+ "where a.htcode='"+ys.getHtcode()+"' and a.dstart1>='"+df.format(ys.getFyksdate())+"' and a.dstart1<='"+df.format(ys.getFyjzdate())+"' and a.vsrcid2name='pk_contract_ywcf') b1 "
				+ "left join "
				+ "(select a.orgname,a.nysmny,to_char(a.dstart,'YYYY\"年\"MM\"月\"DD\"日\"')||'~'||to_char(a.dend,'YYYY\"年\"MM\"月\"DD\"日\"') wyqj from v_ct_cjd a "
				+ "where a.htcode='"+ys.getHtcode()+"' and a.dstart1>='"+df.format(ys.getFyksdate())+"' and a.dstart1<='"+df.format(ys.getFyjzdate())+"' and a.vsrcid2name='pk_contract_wyfcf') b2 "
				+ "on b2.orgname=b1.orgname "
				+ "left join (select a.orgname,sum(nvl(a.nysmny,0)) zjmny,replace(wm_concat(a.costname||'：'||a.nysmny||'元；</br>'),',','') zj from v_ct_cjd a "
				+ "where a.htcode='"+ys.getHtcode()+"' and a.dstart1>='"+df.format(ys.getFyksdate())+"' and a.dstart1<='"+df.format(ys.getFyjzdate())+"' and a.vsrcid2name='pk_contract_yj' group by a.orgname) b3 "
				+ "on b3.orgname=b2.orgname";
		Map<String, Object> map=commonBaseDao.selectMapResult(sql);
		return map;
	}
}