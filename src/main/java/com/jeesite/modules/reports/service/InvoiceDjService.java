/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.reports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.reports.dao.InvoiceDjDao;
import com.jeesite.modules.reports.entity.InvoiceDj;

/**
 * 合同管理Service
 * @author LY
 * @version 2019-10-31
 */
@Service
@Transactional(readOnly=true)
public class InvoiceDjService extends CrudService<InvoiceDjDao, InvoiceDj> {
	
	@Autowired
	private InvoiceDjDao invoiceDjDao;
	
	/**
	 * 查询分页数据
	 * @param wgContract 查询条件
	 * @param wgContract.page 分页对象
	 * @return
	 */
	@Override
	public List<InvoiceDj> findList(InvoiceDj entity) {
		String wpkorg="";
		if(entity.getPkOrg()!=null&&StringUtils.isNotBlank(entity.getPkOrg().getOfficeCode())){
			String[] pkarr=entity.getPkOrg().getOfficeCode().split(",");
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
		if(entity.getPkProject()!=null&&StringUtils.isNotBlank(entity.getPkProject().getPkProject())){
			String[] pkarr=entity.getPkProject().getPkProject().split(",");
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
		if(StringUtils.isNotBlank(entity.getHtcode())){
			whtcode=" and htcode like '%"+entity.getHtcode()+"%' ";
		}
		String whcust="";
		if(StringUtils.isNotBlank(entity.getKpname())){
			whcust=" and kpname like '%"+entity.getKpname()+"%' ";
		}
		String order="";
		if(entity.getPage()!=null&&StringUtils.isNotBlank(entity.getPage().getOrderBy())){
			order=" order by "+entity.getPage().getOrderBy();
		}
		String sql="select kpname,htcode,house,to_char(dskstart,'YYYY.MM.DD')||'-'||to_char(dskend,'YYYY.MM.DD') dskqj,fkman,dsqdate,"
				+ "fptype,fpcode,(select bp.name from bd_project bp where bp.pk_project=pkcost) pkcost,(select bp.name from bd_project bp where bp.pk_project=kpcostp) kpcostp,"
				+ "taxrate,nkpmny,nnotaxmny,ntaxmny,to_char(dkpstart,'YYYY.MM.DD')||'-'||to_char(dkpend,'YYYY.MM.DD') dkpqj from v_ct_kpdj where 1=1 "+wpkorg+wpkpro+whtcode+whcust+order;
		List<InvoiceDj> list =invoiceDjDao.queryInvoiceDj(sql);
		if(list!=null&&list.size()>0){
			Double allkp=new Double(0);//开票合计
			Double allnnotax=new Double(0);//无税合计
			Double alltax=new Double(0);//税额合计
			for (int i = 0; i < list.size(); i++) {
				allkp+=list.get(i).getNkpmny();
				allnnotax+=list.get(i).getNnotaxmny();
				alltax+=list.get(i).getNtaxmny();
			}
			InvoiceDj ys=new InvoiceDj();
			ys.setHtcode("合计");
			ys.setNkpmny(allkp);
			ys.setNnotaxmny(allnnotax);
			ys.setNtaxmny(alltax);
			list.add(ys);
		}
		return list;
	}


}