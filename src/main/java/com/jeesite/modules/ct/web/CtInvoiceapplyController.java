/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

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
import com.jeesite.modules.ct.entity.CtInvoiceapply;
import com.jeesite.modules.ct.service.CtInvoiceapplyService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 开票申请Controller
 * @author tcl
 * @version 2019-11-11
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctInvoiceapply")
public class CtInvoiceapplyController extends BaseController {

	@Autowired
	private CtInvoiceapplyService ctInvoiceapplyService;
	
	@Autowired
	private CommonBaseService commonService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtInvoiceapply get(String pkInvoiceapply, boolean isNewRecord) {
		return ctInvoiceapplyService.get(pkInvoiceapply, isNewRecord);
	}
	
	/**
	 * 开票登记参照开票申请 by tcl
	 */
	@RequiresPermissions("ct:ctInvoiceapply:view")
	@RequestMapping(value = {"refKpsqForKpdjList", ""})
	public String refKpsqForKpdjList(CtInvoiceapply ctInvoiceapply, Model model) {
		model.addAttribute("ctInvoiceapply", ctInvoiceapply);
		return "modules/refpages/refKpsqForKpdjSelect";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctInvoiceapply:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtInvoiceapply ctInvoiceapply, Model model) {
		model.addAttribute("ctInvoiceapply", ctInvoiceapply);
		return "modules/ct/ctInvoiceapplyList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctInvoiceapply:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtInvoiceapply> listData(CtInvoiceapply ctInvoiceapply, HttpServletRequest request, HttpServletResponse response) {
		ctInvoiceapply.setPage(new Page<>(request, response));
		Page<CtInvoiceapply> page = ctInvoiceapplyService.findPage(ctInvoiceapply);
		return page;
	}
	
	/**
	 * 开票登记参照开票申请
	 */
	@RequiresPermissions("ct:ctInvoiceapply:view")
	@RequestMapping(value = "listDataRefForKpdj")
	@ResponseBody
	public Page<CtInvoiceapply> listDataRefForKpdj(CtInvoiceapply ctInvoiceapply, HttpServletRequest request, HttpServletResponse response) {
		
		//排除1.开票登记未审批,2金额未开票完成
		String where =" a.vbillstatus=1 and  "
				+ "(select sum(nvl(x.nsqkpmny,0)) from ct_invoiceapply_skmx x where nvl(x.dr,0)=0 and x.pk_invoiceapply=a.pk_invoiceapply) "
				+ ">(select sum(nvl(x.nkpdjmny,0)) from ct_invoiceapply_skmx x where nvl(x.dr,0)=0 and x.pk_invoiceapply=a.pk_invoiceapply) "
				+ "and not exists(select 1 from ct_invoiceregister r where r.vsrcid=a.pk_invoiceapply and nvl(r.dr,0)=0 and r.vbillstatus<>1)";
		ctInvoiceapply.getSqlMap().getDataScope().addFilter("dsf", where);
		ctInvoiceapply.setPage(new Page<>(request, response));
		Page<CtInvoiceapply> page = ctInvoiceapplyService.findPage(ctInvoiceapply);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctInvoiceapply:view")
	@RequestMapping(value = "form")
	public String form(CtInvoiceapply ctInvoiceapply,String noEdit, Model model) {
		if(ctInvoiceapply.getVbillstatus()==AbsEnumType.BillStatus_SPTG){
			noEdit="true";
		}
		if(StringUtils.isEmpty(ctInvoiceapply.getVsrctype())){
			model.addAttribute("isRef",false);
		}else{
			model.addAttribute("isRef",true);
		}
		if(StringUtils.isEmpty(ctInvoiceapply.getVsrctype())&&ctInvoiceapply.getIsNewRecord()){//新增自制
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String sdate=df.format(new Date()).replaceAll("-", "");
			String vbillno=CodeFactoryUtils.createBillCodeByDr("KPSQ"+sdate, "ct_invoiceapply", "vbillno", 5);
			ctInvoiceapply.setVbillno(vbillno);
			ctInvoiceapply.setDsqdate(new Date());
			ctInvoiceapply.setVbillstatus(AbsEnumType.BillStatus_ZY);
			ctInvoiceapply.setPkSqr(UserUtils.getUser());
		}
		boolean edit=Boolean.valueOf(noEdit);
		model.addAttribute("noEdit",edit);
		model.addAttribute("ctInvoiceapply", ctInvoiceapply);
		return "modules/ct/ctInvoiceapplyForm";
	}
	
	/**
	 * 开票申请参照带出数据
	 */
	@RequiresPermissions("ct:ctInvoiceapply:view")
	@RequestMapping(value = "refAddFromYS")
	public String refAddFromYS(String pks, Model model){
		CtInvoiceapply ctInvoiceapply=ctInvoiceapplyService.refAddFromYS(pks);
		model.addAttribute("ctInvoiceapply", ctInvoiceapply);
		model.addAttribute("isRef",true);
		model.addAttribute("noEdit",false);
		return "modules/ct/ctInvoiceapplyForm";
	}
	
	/**
	 * 修改退租管理
	 */
	@RequiresPermissions("ct:ctInvoiceapply:edit")
	@RequestMapping(value = "edit")
	@ResponseBody
	public String edit(CtInvoiceapply ctInvoiceapply) {
		CtInvoiceapply entity=ctInvoiceapplyService.get(ctInvoiceapply);
		if(!entity.getVbillstatus().equals(AbsEnumType.BillStatus_ZY)){
			return renderResult(Global.FALSE, text("单据状态不是自由态，不可修改！"));
		}
		return "";
	}

	/**
	 * 保存开票申请
	 */
	@RequiresPermissions("ct:ctInvoiceapply:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtInvoiceapply ctInvoiceapply) {
		String pk=ctInvoiceapply.getPkInvoiceapply();
		if(StringUtils.isBlank(pk)){
			pk="***";
		}
		Integer count=commonService.selectCount("select count(*) from ct_invoiceapply where fpcode='"+ctInvoiceapply.getFpcode()+"' and pk_invoiceapply!='"+pk+"'");
		if(count>0){
			throw new ValidationException("发票号已存在，请检查！");
		}
		ctInvoiceapplyService.save(ctInvoiceapply);
		List<String> list=new ArrayList<String>();
		list.add(ctInvoiceapply.getPkInvoiceapply());
		approveAll(list);
		return renderResult(Global.TRUE, text("保存开票申请成功！"));
	}
	
	/**
	 * 删除开票申请
	 */
	@RequiresPermissions("ct:ctInvoiceapply:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CtInvoiceapply ctInvoiceapply) {
		Integer spstatus=ctInvoiceapply.getVbillstatus();
		if(AbsEnumType.BillStatus_SPTG==spstatus){
			return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
		}
		ctInvoiceapplyService.delete(ctInvoiceapply);
		return renderResult(Global.TRUE, text("删除开票申请成功！"));
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String deleteAllData(@RequestBody List<String> list) {
		for(String pk:list){
			//从列表转换的数据，需要重新获取
			CtInvoiceapply ctInvoiceapply=ctInvoiceapplyService.get(pk);
			Integer spstatus=ctInvoiceapply.getVbillstatus();
			if(AbsEnumType.BillStatus_SPTG==spstatus){
				return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
			}
			ctInvoiceapplyService.delete(ctInvoiceapply);
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
			CtInvoiceapply ctInvoiceapply=ctInvoiceapplyService.get(pk);
			ctInvoiceapply.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			ctInvoiceapply.setApprover(UserUtils.getUser());
			ctInvoiceapply.setApprovetime(new Date());
			ctInvoiceapplyService.update(ctInvoiceapply);
			if(StringUtils.isNotEmpty(ctInvoiceapply.getVsrctype())){
				//审批之后回写金额
				ctInvoiceapplyService.approveUpdate(ctInvoiceapply);
			}
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
			CtInvoiceapply ctInvoiceapply=ctInvoiceapplyService.get(pk);
			ctInvoiceapply.setVbillstatus(AbsEnumType.BillStatus_ZY);
			ctInvoiceapply.setApprover(null);
			ctInvoiceapply.setApprovetime(null);
			ctInvoiceapplyService.update(ctInvoiceapply);
			if(StringUtils.isNotEmpty(ctInvoiceapply.getVsrctype())){
				//弃审之后回写金额
				ctInvoiceapplyService.approveUpdate(ctInvoiceapply);
			}
		}
		return renderResult(Global.TRUE, text("取消审批成功！"));
	}
	
}