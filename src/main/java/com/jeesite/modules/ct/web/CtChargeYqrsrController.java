/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.web;

import java.sql.Timestamp;
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
import com.jeesite.modules.ct.entity.CtChargeYqrsr;
import com.jeesite.modules.ct.service.CtChargeYqrsrService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 应确认收入Controller
 * @author GJ
 * @version 2019-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctChargeYqrsr")
public class CtChargeYqrsrController extends BaseController {

	@Autowired
	private CtChargeYqrsrService ctChargeYqrsrService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtChargeYqrsr get(String pkChargeYqrsr, boolean isNewRecord) {
		return ctChargeYqrsrService.get(pkChargeYqrsr, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctChargeYqrsr:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtChargeYqrsr ctChargeYqrsr, Model model) {
		model.addAttribute("ctChargeYqrsr", ctChargeYqrsr);
		return "modules/ct/ctChargeYqrsrList";
	}
	/**
	 * 收入确认参照应确认收入查询列表
	 */
	@RequiresPermissions("ct:ctChargeYqrsr:view")
	@RequestMapping(value = {"reflist", ""})
	public String reflist(CtChargeYqrsr ctChargeYqrsr, Model model) {
		model.addAttribute("ctChargeYqrsr", ctChargeYqrsr);
		return "modules/refPages/refChargeYqrsrSelect";
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctChargeYqrsr:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtChargeYqrsr> listData(CtChargeYqrsr ctChargeYqrsr, HttpServletRequest request, HttpServletResponse response) {
		ctChargeYqrsr.setPage(new Page<>(request, response));
		Page<CtChargeYqrsr> page = ctChargeYqrsrService.findPage(ctChargeYqrsr);
		return page;
	}
	/**
	 * 收款单参照应收单查询列表数据
	 */
	@RequiresPermissions("ct:ctChargeYqrsr:view")
	@RequestMapping(value = "listDataRef")
	@ResponseBody
	public Page<CtChargeYqrsr> listDataRef(CtChargeYqrsr ctChargeYqrsr, HttpServletRequest request, HttpServletResponse response) {
		ctChargeYqrsr.setPage(new Page<>(request, response));
		ctChargeYqrsr.getSqlMap().getDataScope().addFilter("extquery", " vbillstatus=1 and a.nqsmny<>0 and not exists( select  1 from ct_charge_srqr_b b,"
			+ "ct_charge_srqr s where b.lyvbillno=a.pk_charge_yqrsr and nvl(b.dr,0)=0  and nvl(s.dr,0)=0 and b.pk_charge_srqr=s.pk_charge_srqr and s.vbillstatus<>1)");
		Page<CtChargeYqrsr> page = ctChargeYqrsrService.findPage(ctChargeYqrsr);
		return page;
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctChargeYqrsr:view")
	@RequestMapping(value = "form")
	public String form(CtChargeYqrsr ctChargeYqrsr, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(ctChargeYqrsr.getPkChargeYqrsr())){
			ctChargeYqrsr.setCreator(UserUtils.getUser());
			ctChargeYqrsr.setCreationtime(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(ctChargeYqrsr.getPkChargeYqrsr())) {
			ctChargeYqrsr.setModifier(UserUtils.getUser());
			ctChargeYqrsr.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("ctChargeYqrsr", ctChargeYqrsr);
		model.addAttribute("isEdit", isEdit);
		return "modules/ct/ctChargeYqrsrForm";
	}

	/**
	 * 保存应确认收入
	 */
	@RequiresPermissions("ct:ctChargeYqrsr:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtChargeYqrsr ctChargeYqrsr) {
		ctChargeYqrsrService.save(ctChargeYqrsr);
		return renderResult(Global.TRUE, text("保存应确认收入成功！"));
	}
	
	/**
	 * 删除应确认收入
	 */
	@RequiresPermissions("ct:ctChargeYqrsr:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CtChargeYqrsr ctChargeYqrsr) {
		ctChargeYqrsrService.delete(ctChargeYqrsr);
		return renderResult(Global.TRUE, text("删除应确认收入成功！"));
	}
	/**
	 * 批量审批功能
	 */
	@RequestMapping(value = "approveAll")
	@ResponseBody
	public String approveAll(@RequestBody List<String> list) {
		for(String pk:list){
			ctChargeYqrsrService.subData(pk);
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
			  boolean aa =ctChargeYqrsrService.unSubYqrsr(pk);
			   if(!aa){
				   return renderResult(Global.FALSE, text("单据已被收入确认单参照,不可取消审批!"));
			   }
			   ctChargeYqrsrService.unSubData(pk);
		}
		return renderResult(Global.TRUE, text("取消审批成功！"));
	}
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String deleteAllData(@RequestBody List<String> list) {
		for(String pk:list){
			//从列表转换的数据，需要重新获取
			CtChargeYqrsr ctInvoiceapply=ctChargeYqrsrService.get(pk);
			Integer spstatus=ctInvoiceapply.getVbillstatus();
			if(AbsEnumType.BillStatus_SPTG==spstatus){
				return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
			}
			ctChargeYqrsrService.delete(ctInvoiceapply);
		}
		return renderResult(Global.TRUE, text("批量删除成功！"));
	}
}