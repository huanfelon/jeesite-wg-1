/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.reports.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.reports.dao.ChargeYsZkDao;
import com.jeesite.modules.reports.entity.ChargeYsZk;

/**
 * 合同管理Service
 * @author LY
 * @version 2019-10-31
 */
@Service
@Transactional(readOnly=true)
public class ChargeYsZkService extends CrudService<ChargeYsZkDao, ChargeYsZk> {
	
	@Autowired
	private ChargeYsZkDao chargeYsZkDao;
	
	/**
	 * 查询分页数据
	 * @param wgContract 查询条件
	 * @param wgContract.page 分页对象
	 * @return
	 */
	@Override
	public List<ChargeYsZk> findList(ChargeYsZk chargeYsZk) {
		String wpkorg="";
		if(chargeYsZk.getPkOrg()!=null&&StringUtils.isNotBlank(chargeYsZk.getPkOrg().getOfficeCode())){
			String[] pkarr=chargeYsZk.getPkOrg().getOfficeCode().split(",");
			String pks="";
			for (int i=0;i<pkarr.length;i++) {
				pks+="'"+pkarr[i]+"'";
				if(i<pkarr.length-1){
					pks+=",";
				}
			}
			wpkorg=" and pk_org in ("+pks+") ";
		}
		String wpkpro="";
		if(chargeYsZk.getPkProject()!=null&&StringUtils.isNotBlank(chargeYsZk.getPkProject().getPkProject())){
			String[] pkarr=chargeYsZk.getPkProject().getPkProject().split(",");
			String pks="";
			for (int i=0;i<pkarr.length;i++) {
				pks+="'"+pkarr[i]+"'";
				if(i<pkarr.length-1){
					pks+=",";
				}
			}
			wpkpro=" and pk_project in ("+pks+") ";
		}
		String whtcode="";
		if(StringUtils.isNotBlank(chargeYsZk.getHtcode())){
			whtcode=" and htcode like '%"+chargeYsZk.getHtcode()+"%' ";
		}
		String whcust="";
		if(StringUtils.isNotBlank(chargeYsZk.getCustname())){
			whcust=" and custname like '%"+chargeYsZk.getCustname()+"%' ";
		}
		String yfdate="";
		if(chargeYsZk.getYfdate()!=null){
			SimpleDateFormat df=new SimpleDateFormat("YYYY-MM");
			String yf=df.format(chargeYsZk.getYfdate());
			yfdate=" and to_char(yfdate,'YYYY-MM')='"+yf+"' ";
		}
		String order="";
		if(chargeYsZk.getPage()!=null&&StringUtils.isNotBlank(chargeYsZk.getPage().getOrderBy())){
			order=" order by "+chargeYsZk.getPage().getOrderBy();
		}
		String sql="select * from v_ct_charge_ys where 1=1 "+wpkorg+wpkpro+whtcode+whcust+yfdate+order;
		List<ChargeYsZk> list =chargeYsZkDao.queryChargeYsZk(sql);
		if(list!=null&&list.size()>0){
			Double allyszl=new Double(0);//租赁费用合计
			Double allyswy=new Double(0);//物业费用合计
			Double allysqt=new Double(0);//其他费用合计
			Double allyshj=new Double(0);//总费用合计
			Double allsszl=new Double(0);//已到租赁费用合计
			Double allsswy=new Double(0);//已到物业费用合计
			Double allssqt=new Double(0);//移到其他费用合计
			Double allsshj=new Double(0);//累计收款费用合计
			Double allwsmny=new Double(0);//未到金额费用合计
			for (int i = 0; i < list.size(); i++) {
				allyszl+=list.get(i).getYszl();
				allyswy+=list.get(i).getYswy();
				allysqt+=list.get(i).getYsqt();
				allyshj+=list.get(i).getYshj();
				allsszl+=list.get(i).getSszl();
				allsswy+=list.get(i).getSswy();
				allssqt+=list.get(i).getSsqt();
				allsshj+=list.get(i).getSshj();
				allwsmny+=list.get(i).getWsmny();
			}
			ChargeYsZk ys=new ChargeYsZk();
			ys.setHtcode("合计");
			ys.setYszl(allyszl);
			ys.setYswy(allyswy);
			ys.setYsqt(allysqt);
			ys.setYshj(allyshj);
			ys.setSszl(allsszl);
			ys.setSswy(allsswy);
			ys.setSsqt(allssqt);
			ys.setSshj(allsshj);
			ys.setWsmny(allwsmny);
			list.add(ys);
		}
		return list;
	}

}