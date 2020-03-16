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
import com.jeesite.modules.reports.entity.CtYsSrCheck;
import com.jeesite.modules.reports.service.CtYsSrCheckService;

/**
 * 检验表Controller
 * @author LY
 * @version 2019-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/reports/ctYsSrCheck")
public class CtYsSrCheckController extends BaseController {

	@Autowired
	private CtYsSrCheckService ctYsSrCheckService;
	
	/**
	 * 查询合同管理列表
	 */
	@RequiresPermissions("reports:ctYsSrCheck:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtYsSrCheck ctYsSrCheck, Model model) {
		model.addAttribute("ctYsSrCheck", ctYsSrCheck);
		return "modules/reports/ctYsSrCheckList";
	}
	
	/**
	 * 查询合同管理列表数据
	 */
	@RequiresPermissions("reports:ctYsSrCheck:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<CtYsSrCheck> listData(CtYsSrCheck ctYsSrCheck, HttpServletRequest request, HttpServletResponse response) {
		if(ctYsSrCheck.getPkOrg()==null||ctYsSrCheck.getPkOrg().getId()==null||ctYsSrCheck.getPkOrg().getId().equals("")){
			return null;
		}
		ctYsSrCheck.setPage(new Page<>(request, response));
		List<CtYsSrCheck> list = ctYsSrCheckService.findList(ctYsSrCheck);
		return list;
	}
	/**
	 * 导出
	 */
	@RequestMapping({"exportData"})
	public void exportData(CtYsSrCheck ctYsSrCheck, Boolean isAll, String ctrlPermi, HttpServletResponse response)
	{
		
		List<CtYsSrCheck> list = ctYsSrCheckService.findList(ctYsSrCheck);
		String fileName = "检验表" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
		ExcelExport ee = new ExcelExport("检验表", CtYsSrCheck.class);
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