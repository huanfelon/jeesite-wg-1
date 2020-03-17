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
import com.jeesite.modules.reports.entity.InvoiceDj;
import com.jeesite.modules.reports.service.InvoiceDjService;

/**
 * 合同管理Controller
 * @author LY
 * @version 2019-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/reports/invoiceDj")
public class InvoiceDjController extends BaseController {

	@Autowired
	private InvoiceDjService invoiceDjService;
	
	/**
	 * 查询合同管理列表
	 */
	@RequiresPermissions("reports:invoiceDj:view")
	@RequestMapping(value = {"list", ""})
	public String list(InvoiceDj invoiceDj, Model model) {
		model.addAttribute("invoiceDj", invoiceDj);
		return "modules/reports/invoiceDjList";
	}
	
	/**
	 * 查询合同管理列表数据
	 */
	@RequiresPermissions("reports:invoiceDj:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<InvoiceDj> listData(InvoiceDj invoiceDj, HttpServletRequest request, HttpServletResponse response) {
		if(invoiceDj.getPkOrg()==null||invoiceDj.getPkOrg().getId()==null||invoiceDj.getPkOrg().getId().equals("")){
			return null;
		}
		invoiceDj.setPage(new Page<>(request, response));
		List<InvoiceDj> list = invoiceDjService.findList(invoiceDj);
		return list;
	}
	/**
	 * 导出
	 */
	@RequestMapping({"exportData"})
	public void exportData(InvoiceDj invoiceDj, Boolean isAll, String ctrlPermi, HttpServletResponse response)
	{
		
		List<InvoiceDj> list = invoiceDjService.findList(invoiceDj);
		String fileName = "开票登记表" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
		ExcelExport ee = new ExcelExport("开票登记表", InvoiceDj.class);
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