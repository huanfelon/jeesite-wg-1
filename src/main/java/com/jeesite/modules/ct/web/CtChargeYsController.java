/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.web;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.jeesite.modules.ct.entity.CtChargeYs;
import com.jeesite.modules.ct.service.CtChargeSkService;
import com.jeesite.modules.ct.service.CtChargeYsService;
import com.jeesite.modules.ct.service.CtInvoiceapplyService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 应收单Controller
 * @author GJ
 * @version 2019-11-04
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctChargeYs")
public class CtChargeYsController extends BaseController {

	@Autowired
	private CtChargeYsService ctChargeYsService;
	
	@Autowired
	private CtChargeSkService ctChargeSkService;
	@Autowired
	private CtInvoiceapplyService ctInvoiceapplyService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtChargeYs get(String pkChargeYs, boolean isNewRecord) {
		return ctChargeYsService.get(pkChargeYs, isNewRecord);
	}
	
	/**
	 * 付款单参照应收单查询列表
	 */
	@RequiresPermissions("ct:ctChargeYs:view")
	@RequestMapping(value = {"reflist", ""})
	public String reflist(CtChargeYs ctChargeYs, Model model) {
		model.addAttribute("ctChargeYs", ctChargeYs);
		return "modules/refPages/refChargeYsSelect";
	}
	
	/**
	 * 开票申请参照应收单 by tcl
	 */
	@RequiresPermissions("ct:ctChargeYs:view")
	@RequestMapping(value = {"refKpsqList", ""})
	public String refKpsqList(CtChargeYs ctChargeYs, Model model) {
		model.addAttribute("ctChargeYs", ctChargeYs);
		return "modules/refPages/refChargeYsForKpsqSelect";
	}
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctChargeYs:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtChargeYs ctChargeYs, Model model) {
		model.addAttribute("ctChargeYs", ctChargeYs);
		return "modules/ct/ctChargeYsList";
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctChargeYs:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtChargeYs> listData(CtChargeYs ctChargeYs, HttpServletRequest request, HttpServletResponse response) {
		ctChargeYs.setPage(new Page<>(request, response));
		Page<CtChargeYs> page = ctChargeYsService.findPage(ctChargeYs);
		return page;
	}
	/**
	 * 收款单参照应收单查询列表数据
	 */
	@RequiresPermissions("ct:ctChargeYs:view")
	@RequestMapping(value = "listDataRef")
	@ResponseBody
	public Page<CtChargeYs> listDataRef(CtChargeYs ctChargeYs, HttpServletRequest request, HttpServletResponse response) {
		ctChargeYs.setPage(new Page<>(request, response));
		ctChargeYs.getSqlMap().getDataScope().addFilter("skrefquery", " vbillstatus=1 and a.nqsmny<>0 and not exists( select  1 from ct_charge_sk_b b,"
			+ "ct_charge_sk s where b.lyvbillno=a.pk_charge_ys and nvl(b.dr,0)=0  and nvl(s.dr,0)=0 and b.pk_charge_sk=s.pk_charge_sk and s.vbillstatus<>1)");
		Page<CtChargeYs> page = ctChargeYsService.findPage(ctChargeYs);
		return page;
	}
	
	/**
	 * 开票申请参照应收单查询
	 */
	@RequiresPermissions("ct:ctChargeYs:view")
	@RequestMapping(value = "listDataRefForKpsq")
	@ResponseBody
	public Page<CtChargeYs> listDataRefForKpsq(CtChargeYs ctChargeYs, HttpServletRequest request, HttpServletResponse response) {
		
		//排除1.开票申请未审批
		String where =" a.vbillstatus=1 and abs(nvl(a.nysmny,0))<>abs(nvl(a.nkpmny,0)) "
				+ " and not exists (select 1 from ct_invoiceapply k where nvl(k.dr,0)=0 "
				+ " and k.vbillstatus<>1 and k.pk_invoiceapply in (select ks.pk_invoiceapply "
				+ "from ct_invoiceapply_skmx ks where nvl(ks.dr,0)=0 and ks.vsrcid=a.pk_charge_ys))";
			
		ctChargeYs.getSqlMap().getDataScope().addFilter("dsf", where);
		ctChargeYs.setPage(new Page<>(request, response));
		Page<CtChargeYs> page = ctChargeYsService.findPage(ctChargeYs);
		return page;
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctChargeYs:view")
	@RequestMapping(value = "form")
	public String form(CtChargeYs ctChargeYs, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(ctChargeYs.getPkChargeYs())){
			ctChargeYs.setCreator(UserUtils.getUser());
			ctChargeYs.setCreationtime(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(ctChargeYs.getPkChargeYs())) {
			ctChargeYs.setModifier(UserUtils.getUser());
			ctChargeYs.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("ctChargeYs", ctChargeYs);
		model.addAttribute("isEdit", isEdit);
		return "modules/ct/ctChargeYsForm";
	}

	/**
	 * 保存应收单
	 */
	@RequiresPermissions("ct:ctChargeYs:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtChargeYs ctChargeYs) {
		ctChargeYsService.save(ctChargeYs);
		return renderResult(Global.TRUE, text("保存应收单成功！"));
	}
	
	/**
	 * 删除应收单
	 */
	@RequiresPermissions("ct:ctChargeYs:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CtChargeYs ctChargeYs) {
		ctChargeYsService.delete(ctChargeYs);
		return renderResult(Global.TRUE, text("删除应收单成功！"));
	}
	/**
	 * 批量审批功能
	 */
	@RequestMapping(value = "approveAll")
	@ResponseBody
	public String approveAll(@RequestBody List<String> list) {
		for(String pk:list){
			CtChargeYs ctChargeYs=ctChargeYsService.get(pk);
			ctChargeYs.setVbillstatus(AbsEnumType.BillStatus_SPTG);
			ctChargeYs.setApprover(UserUtils.getUser());
			ctChargeYs.setApprovetime(new Date());
			this.save(ctChargeYs);
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
			 boolean aa =ctChargeYsService.unSubYs(pk);
			   if(!aa){
				   return renderResult(Global.FALSE, text("单据已被收款单参照,不可取消审批!"));
				   
			   }
			   boolean bb =ctChargeYsService.unSubYs1(pk);
			   if(!bb){
				   return renderResult(Global.FALSE, text("单据已被开票申请参照,不可取消审批!"));
			   }
			   ctChargeYsService.unSubData(pk);
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
			CtChargeYs ctInvoiceapply=ctChargeYsService.get(pk);
			Integer spstatus=ctInvoiceapply.getVbillstatus();
			if(AbsEnumType.BillStatus_SPTG==spstatus){
				return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
			}
			ctChargeYsService.delete(ctInvoiceapply);
		}
		return renderResult(Global.TRUE, text("批量删除成功！"));
	}
	
	/**
	 * 打印弹框
	 */
	@RequestMapping(value = {"print", ""})
	public String print(String pkChargeYs, Model model) {
		Map<String, Object> cjd=ctChargeYsService.getPrintModel(pkChargeYs);
		model.addAttribute("cjd", cjd);
		return "modules/print/ctjktzd";
	}
	
}