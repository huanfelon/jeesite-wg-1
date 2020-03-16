/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.ct.entity.CtInvoiceregister;
import com.jeesite.modules.ct.service.CtInvoiceregisterService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 开票登记Controller
 * @author tcl
 * @version 2019-11-11
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctInvoiceregister")
public class CtInvoiceregisterController extends BaseController {

	@Autowired
	private CtInvoiceregisterService ctInvoiceregisterService;
	
	@Autowired
	private CommonBaseService commonService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtInvoiceregister get(String pkInvoiceregister, boolean isNewRecord) {
		return ctInvoiceregisterService.get(pkInvoiceregister, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctInvoiceregister:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtInvoiceregister ctInvoiceregister, Model model) {
		model.addAttribute("ctInvoiceregister", ctInvoiceregister);
		return "modules/ct/ctInvoiceregisterList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctInvoiceregister:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtInvoiceregister> listData(CtInvoiceregister ctInvoiceregister, HttpServletRequest request, HttpServletResponse response) {
		ctInvoiceregister.setPage(new Page<>(request, response));
		Page<CtInvoiceregister> page = ctInvoiceregisterService.findPage(ctInvoiceregister);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctInvoiceregister:view")
	@RequestMapping(value = "form")
	public String form(CtInvoiceregister ctInvoiceregister,String noEdit, Model model) {
		if(ctInvoiceregister.getVbillstatus()==AbsEnumType.BillStatus_SPTG){
			noEdit="true";
		}
		if(StringUtils.isEmpty(ctInvoiceregister.getVsrctype())){
			model.addAttribute("isRef",false);
		}else{
			model.addAttribute("isRef",true);
		}
		if(StringUtils.isEmpty(ctInvoiceregister.getVsrctype())&&ctInvoiceregister.getIsNewRecord()){//新增自制
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String sdate=df.format(new Date()).replaceAll("-", "");
			String vbillno=CodeFactoryUtils.createBillCodeByDr("KPDJ"+sdate, "ct_invoiceregister", "vbillno", 5);
			ctInvoiceregister.setVbillno(vbillno);
			ctInvoiceregister.setDkpdate(new Date());
			ctInvoiceregister.setVbillstatus(AbsEnumType.BillStatus_ZY);
			ctInvoiceregister.setPkKpr(UserUtils.getUser());
		}
		boolean edit=Boolean.valueOf(noEdit);
		model.addAttribute("noEdit",edit);
		model.addAttribute("ctInvoiceregister", ctInvoiceregister);
		return "modules/ct/ctInvoiceregisterForm";
	}
	
	/**
	 * 开票登记参照带出数据
	 */
	@RequiresPermissions("ct:ctInvoiceregister:view")
	@RequestMapping(value = "refAddFromKPSQ")
	public String refAddFromKPSQ(String pks, Model model){
		CtInvoiceregister ctInvoiceregister=ctInvoiceregisterService.refAddFromKPSQ(pks);
		model.addAttribute("ctInvoiceregister", ctInvoiceregister);
		model.addAttribute("isRef",true);
		model.addAttribute("noEdit",false);
		return "modules/ct/ctInvoiceregisterForm";
	}

	/**
	 * 保存开票登记
	 */
	@RequiresPermissions("ct:ctInvoiceregister:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtInvoiceregister ctInvoiceregister) {
		ctInvoiceregisterService.save(ctInvoiceregister);
		return renderResult(Global.TRUE, text("保存开票登记成功！"));
	}
	
	/**
	 * 删除开票登记
	 */
	@RequiresPermissions("ct:ctInvoiceregister:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CtInvoiceregister ctInvoiceregister) {
		Integer spstatus=ctInvoiceregister.getVbillstatus();
		if(AbsEnumType.BillStatus_SPTG==spstatus){
			return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
		}
		ctInvoiceregisterService.delete(ctInvoiceregister);
		return renderResult(Global.TRUE, text("删除开票登记成功！"));
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String deleteAllData(@RequestBody List<String> list) {
		for(String pk:list){
			//从列表转换的数据，需要重新获取
			CtInvoiceregister ctInvoiceregister=ctInvoiceregisterService.get(pk);
			Integer spstatus=ctInvoiceregister.getVbillstatus();
			if(AbsEnumType.BillStatus_SPTG==spstatus){
				return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
			}
			ctInvoiceregisterService.delete(ctInvoiceregister);
		}
		return renderResult(Global.TRUE, text("批量删除成功！"));
	}
	
	/**
	 * 批量审批功能
	 */
	@RequestMapping(value = "approveAll")
	@ResponseBody
	public String approveAll(@RequestBody List<String> list) {
		for(String pk:list){
			CtInvoiceregister ctInvoiceregister=ctInvoiceregisterService.get(pk);
			ctInvoiceregister.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			ctInvoiceregister.setApprover(UserUtils.getUser());
			ctInvoiceregister.setApprovetime(new Date());
			ctInvoiceregisterService.update(ctInvoiceregister);
			//审批之后回写金额
			ctInvoiceregisterService.approveUpdate(ctInvoiceregister);
		}
		return renderResult(Global.TRUE, text("审批成功！"));
	}
	
	/**
	 * 批量弃审功能
	 */
	@RequestMapping(value = "unapproveAll")
	@ResponseBody
	public String unapproveAll(@RequestBody List<String> list) {
		for(String pk:list){
			CtInvoiceregister ctInvoiceregister=ctInvoiceregisterService.get(pk);
			ctInvoiceregister.setVbillstatus(AbsEnumType.BillStatus_ZY);
			ctInvoiceregister.setApprover(null);
			ctInvoiceregister.setApprovetime(null);
			ctInvoiceregisterService.update(ctInvoiceregister);
			//审批之后回写金额
			ctInvoiceregisterService.approveUpdate(ctInvoiceregister);
		}
		return renderResult(Global.TRUE, text("取消审批成功！"));
	}
	
}