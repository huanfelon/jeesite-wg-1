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
import com.jeesite.modules.reports.dao.FzWyhttzDao;
import com.jeesite.modules.reports.entity.FzWyhttz;

/**
 * 房租物业合同台账
 * @author LY
 * @version 2019-10-31
 */
@Service
@Transactional(readOnly=true)
public class FzWyhttzService extends CrudService<FzWyhttzDao, FzWyhttz> {
	
	@Autowired
	private FzWyhttzDao fzWyhttzDao;
	
	/**
	 * 查询数据
	 */
	@Override
	public List<FzWyhttz> findList(FzWyhttz fzWyhttz) {
		String wpkorg="";
		if(fzWyhttz.getPkOrg()!=null&&StringUtils.isNotBlank(fzWyhttz.getPkOrg().getOfficeCode())){
			String[] pkarr=fzWyhttz.getPkOrg().getOfficeCode().split(",");
			String pks="";
			for (int i=0;i<pkarr.length;i++) {
				pks+="'"+pkarr[i]+"'";
				if(i<pkarr.length-1){
					pks+=",";
				}
			}
			wpkorg=" and a.pk_org in ("+pks+")";
		}
		String wpkpro="";
		if(fzWyhttz.getPkProject()!=null&&StringUtils.isNotBlank(fzWyhttz.getPkProject().getPkProject())){
			String[] pkarr=fzWyhttz.getPkProject().getPkProject().split(",");
			String pks="";
			for (int i=0;i<pkarr.length;i++) {
				pks+="'"+pkarr[i]+"'";
				if(i<pkarr.length-1){
					pks+=",";
				}
			}
			wpkpro=" and a.pk_project in ("+pks+")";
		}
		String whtcode="";
		if(StringUtils.isNotBlank(fzWyhttz.getHtcode())){
			whtcode=" and a.htcode like '%"+fzWyhttz.getHtcode()+"%'";
		}
		String whcust="";
		if(StringUtils.isNotBlank(fzWyhttz.getCustname())){
			whcust=" and a.custname like '%"+fzWyhttz.getCustname()+"%'";
		}
		String order="";
		if(fzWyhttz.getPage()!=null&&StringUtils.isNotBlank(fzWyhttz.getPage().getOrderBy())){
			order=" order by a."+fzWyhttz.getPage().getOrderBy();
		}
		String sql="select * from ((select * from (select * from v_wg_contract a2 where a2.lc not like '负%' order by a2.pk_org, a2.pk_project, a2.build, a2.lc asc, a2.room)) "
				+ "union all "
				+ "(select * from (select * from v_wg_contract a1 where a1.lc like '负%' order by a1.pk_org, a1.pk_project, a1.build, a1.lc desc, a1.room))) a where 1=1 "+wpkorg+wpkpro+whtcode+whcust+order;
		return fzWyhttzDao.queryFzWyhttz(sql);
	}
	
}