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
import com.jeesite.modules.reports.dao.CtYsSrCheckDao;
import com.jeesite.modules.reports.entity.CtYsSrCheck;

/**
 * 检验表Service
 * @author LY
 * @version 2019-10-31
 */
@Service
@Transactional(readOnly=true)
public class CtYsSrCheckService extends CrudService<CtYsSrCheckDao, CtYsSrCheck> {
	
	@Autowired
	private CtYsSrCheckDao ctYsSrCheckDao;
	
	/**
	 * 查询分页数据
	 * @param wgContract 查询条件
	 * @param wgContract.page 分页对象
	 * @return
	 */
	@Override
	public List<CtYsSrCheck> findList(CtYsSrCheck entity) {
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
		String order="";
		if(entity.getPage()!=null&&StringUtils.isNotBlank(entity.getPage().getOrderBy())){
			order=" order by "+entity.getPage().getOrderBy();
		}
		String sql="select * from v_ct_check where 1=1 "+wpkorg+wpkpro+whtcode+order;
		List<CtYsSrCheck> list =ctYsSrCheckDao.queryCtYsSrCheck(sql);
		return list;
	}


}