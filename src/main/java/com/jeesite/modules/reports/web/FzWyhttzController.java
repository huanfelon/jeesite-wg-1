/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.reports.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

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
import com.jeesite.modules.reports.entity.FzWyhttz;
import com.jeesite.modules.reports.service.FzWyhttzService;
import com.jeesite.modules.wght.entity.WgContract;
import com.jeesite.modules.wght.entity.WgContractYwcf;
import com.jeesite.modules.wght.service.WgContractService;

/**
 * 房租物业合同台账
 * @author LY
 * @version 2019-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/reports/fzWyhttz")
public class FzWyhttzController extends BaseController {

	@Autowired
	private FzWyhttzService fzWyhttzService;
	@Autowired
	private WgContractService wgContractService;
	
	/**
	 * 查询合同管理列表
	 */
	@RequiresPermissions("reports:fzWyhttz:view")
	@RequestMapping(value = {"list", ""})
	public String list(FzWyhttz fzWyhttz, Model model) {
		model.addAttribute("fzWyhttz", fzWyhttz);
		return "modules/reports/fzWyhttzReport";
	}
	
	/**
	 * 查询合同管理列表数据
	 */
	@RequiresPermissions("reports:fzWyhttz:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<FzWyhttz> listData(FzWyhttz fzWyhttz, HttpServletRequest request, HttpServletResponse response) {
		fzWyhttz.setPage(new Page<>(request, response));
		return fzWyhttzService.findList(fzWyhttz);
	}
	
	/**
	 * 计划金额弹出框
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "planMny")
	public String planMny(String pk, Model model){
		List<WgContractYwcf> list = wgContractService.getJhmnyDdetail(pk);
		WgContract wgContract=new WgContract();
		wgContract.setWgContractYwcfList(list);
		model.addAttribute("wgContract", wgContract);
		model.addAttribute("isEdit", false);
		return "modules/refPages/wgContractPlanMny";
	}
	
	/**
	 * 导出
	 */
	@RequestMapping({"exportData"})
	public void exportData(FzWyhttz fzWyhttz, Boolean isAll, String ctrlPermi, HttpServletResponse response){
		
		List<FzWyhttz> list = fzWyhttzService.findList(fzWyhttz);
		if(list==null||list.size()==0){
			throw new ValidationException("无数据无法导出！");
		}
		String fileName = "房租物业合同台账" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
		ExcelExport ee = new ExcelExport("房租物业合同台账", FzWyhttz.class);
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