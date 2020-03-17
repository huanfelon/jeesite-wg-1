/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.service.CrudService;
import com.jeesite.modules.zl.entity.ZlHousesource;
import com.jeesite.modules.zl.dao.ZlHouseTableDao;

/**
 * zl_houseTableService
 * @author LY
 * @version 2019-07-19
 */
@Service
@Transactional(readOnly=true)
public class ZlHouseTableService extends CrudService<ZlHouseTableDao, ZlHousesource> {

	@Autowired
	private ZlHouseTableDao zlHouseTableeDao;

	/**
	 * 获取房源表数据
	 * @param zlHousesource
	 * @return
	 */
	public List<Map<String,Object>> findTableData(String pkOrg,String pkPro,String pkBuild,String busidate) {
		String date="";
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			//获取业务日期
			Date sdate=df.parse(busidate);
			date=sdate.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(date==null||date.equals("")){
			date=(new Date()).toString();
		}
		return zlHouseTableeDao.findTableData(pkOrg,pkPro,pkBuild,busidate);
	}
	
}