/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.reports.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.reports.entity.ChargeYsZk;
import com.jeesite.modules.reports.service.ChargeYsZkService;

/**
 * 合同管理Controller
 * @author LY
 * @version 2019-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/reports/chargeYsZk")
public class ChargeYsZkController extends BaseController {

	@Autowired
	private ChargeYsZkService chargeYsZkService;
	
	/**
	 * 查询合同管理列表
	 */
	@RequiresPermissions("reports:chargeYsZk:view")
	@RequestMapping(value = {"list", ""})
	public String list(ChargeYsZk chargeYsZk, Model model) {
		model.addAttribute("chargeYsZk", chargeYsZk);
		return "modules/reports/chargeYsZkList";
	}
	
	/**
	 * 查询合同管理列表数据
	 */
	@RequiresPermissions("reports:chargeYsZk:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<ChargeYsZk> listData(ChargeYsZk chargeYsZk, HttpServletRequest request, HttpServletResponse response) {
		if(chargeYsZk.getPkOrg()==null||chargeYsZk.getPkOrg().getId()==null||chargeYsZk.getPkOrg().getId().equals("")){
			return null;
		}
		chargeYsZk.setPage(new Page<>(request, response));
		List<ChargeYsZk> list = chargeYsZkService.findList(chargeYsZk);
		return list;
	}
	/**
	 * 导出
	 */
	@RequestMapping({"exportData"})
	public void exportData(ChargeYsZk chargeYsZk, Boolean isAll, String ctrlPermi, HttpServletResponse response)
	{
		
		List<ChargeYsZk> list = chargeYsZkService.findList(chargeYsZk);
		String fileName = "应收账款表" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
		ExcelExport ee = new ExcelExport("应收账款表", ChargeYsZk.class);
		Throwable localThrowable3 = null;
		try { 
			ee.setDataList(list).write(response, fileName);
		}
		catch (Throwable localThrowable1)
		{
			throw localThrowable1;
		} finally {
			if (ee != null) if (localThrowable3 != null) 
				try { ee.close(); } 
			catch (Throwable localThrowable2) {
				localThrowable3.addSuppressed(localThrowable2); } 
			else ee.close(); 
		}
	}
}