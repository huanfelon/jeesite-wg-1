/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.web;

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
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.ct.entity.CtRentbill;
import com.jeesite.modules.ct.service.CtRentbillService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 租约账单Controller
 * @author tcl
 * @version 2019-11-08
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctRentbill")
public class CtRentbillController extends BaseController {

	@Autowired
	private CtRentbillService ctRentbillService;
	
	@Autowired
	private CommonBaseService commonService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtRentbill get(String pkRentbill, boolean isNewRecord) {
		return ctRentbillService.get(pkRentbill, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctRentbill:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtRentbill ctRentbill, Model model) {
		model.addAttribute("ctRentbill", ctRentbill);
		return "modules/ct/ctRentbillList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctRentbill:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtRentbill> listData(CtRentbill ctRentbill, HttpServletRequest request, HttpServletResponse response) {
		ctRentbill.setPage(new Page<>(request, response));
		Page<CtRentbill> page = ctRentbillService.findPage(ctRentbill);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctRentbill:view")
	@RequestMapping(value = "form")
	public String form(CtRentbill ctRentbill,String noEdit, Model model) {
		if(ctRentbill.getVbillstatus().equals(AbsEnumType.BillStatus_SPTG.toString())){
			noEdit="true";
		}
		boolean edit=Boolean.valueOf(noEdit);
		model.addAttribute("noEdit",edit);
		model.addAttribute("ctRentbill", ctRentbill);
		return "modules/ct/ctRentbillForm";
	}

	/**
	 * 保存租约账单
	 */
	@RequiresPermissions("ct:ctRentbill:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtRentbill ctRentbill) {
		ctRentbillService.save(ctRentbill);
		return renderResult(Global.TRUE, text("保存租约账单成功！"));
	}
	
	/**
	 * 删除租约账单
	 */
	@RequiresPermissions("ct:ctRentbill:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CtRentbill ctRentbill) {
		
		String spstatus=ctRentbill.getVbillstatus();
		if(AbsEnumType.BillStatus_SPTG.toString().equals(spstatus)){
			return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
		}
		ctRentbillService.delete(ctRentbill);
		return renderResult(Global.TRUE, text("删除租约账单成功！"));
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String deleteAllData(@RequestBody List<String> list) {
		for(String pk:list){
			//从列表转换的数据，需要重新获取
			CtRentbill ctRentbill=ctRentbillService.get(pk);
			String spstatus=ctRentbill.getVbillstatus();
			if(AbsEnumType.BillStatus_SPTG.toString().equals(spstatus)){
				return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
			}
			ctRentbillService.delete(ctRentbill);
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
			CtRentbill ctRentbill=ctRentbillService.get(pk);
			ctRentbill.setVbillstatus(AbsEnumType.BillStatus_SPTG.toString());
			ctRentbill.setApprover(UserUtils.getUser());
			ctRentbill.setApprovetime(new Date());
			ctRentbillService.update(ctRentbill);
			//审批之后回写合同计划金额
			ctRentbillService.approveUpdateHtPlayMny(ctRentbill);
			//审批之后生成单据
			ctRentbillService.sendToYs(ctRentbill);
			ctRentbillService.sendToDqr(ctRentbill);
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
			CtRentbill ctRentbill=ctRentbillService.get(pk);
			//校验是否有下游应收或者待收入确认
			String pk2=ctRentbill.getPkRentbill();
			String sql="select count(1) from ct_charge_ys where nvl(dr,0)=0 and vpid='"+pk2+"'";
			Integer it=commonService.selectCount(sql);
			if(it>0){
				return renderResult(Global.FALSE, text("单据号"+ctRentbill.getVbillno()+"的单据已有下游应收单，不可取消审批！"));
			}
			//校验是否有下游应收或者待收入确认
			String sql2="select count(1) from ct_charge_yqrsr where nvl(dr,0)=0 and vpid='"+pk2+"'";
			Integer it2=commonService.selectCount(sql2);
			if(it2>0){
				return renderResult(Global.FALSE, text("单据号"+ctRentbill.getVbillno()+"的单据已有下游应确认收入，不可取消审批！"));
			}
			ctRentbill.setVbillstatus(AbsEnumType.BillStatus_ZY.toString());
			ctRentbill.setApprover(null);
			ctRentbill.setApprovetime(null);
			ctRentbillService.update(ctRentbill);
			//取消审批之后回写合同计划金额
			//ctRentbillService.unapproveUpdateHtPlayMny(ctRentbill);
		}
		return renderResult(Global.TRUE, text("取消审批成功！"));
	}
	
}