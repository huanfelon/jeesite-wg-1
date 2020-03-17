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
import com.jeesite.modules.ct.entity.CtChargeSk;
import com.jeesite.modules.ct.service.CtChargeSkService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 收款单Controller
 * @author GJ
 * @version 2019-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctChargeSk")
public class CtChargeSkController extends BaseController {

	@Autowired
	private CtChargeSkService ctChargeSkService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtChargeSk get(String pkChargeSk, boolean isNewRecord) {
		return ctChargeSkService.get(pkChargeSk, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctChargeSk:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtChargeSk ctChargeSk, Model model) {
		model.addAttribute("ctChargeSk", ctChargeSk);
		return "modules/ct/ctChargeSkList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctChargeSk:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtChargeSk> listData(CtChargeSk ctChargeSk, HttpServletRequest request, HttpServletResponse response) {
		ctChargeSk.setPage(new Page<>(request, response));
		Page<CtChargeSk> page = ctChargeSkService.findPage(ctChargeSk);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctChargeSk:view")
	@RequestMapping(value = "form")
	public String form(CtChargeSk ctChargeSk, Model model,String isEdit,String isCz) {
		if(isEdit.equals("true")&& StringUtils.isBlank(ctChargeSk.getPkChargeSk())){
			ctChargeSk.setCreator(UserUtils.getUser());
			ctChargeSk.setCreationtime(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(ctChargeSk.getPkChargeSk())) {
			ctChargeSk.setModifier(UserUtils.getUser());
			ctChargeSk.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("ctChargeSk", ctChargeSk);
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("isCz", isCz);
		return "modules/ct/ctChargeSkForm";
	}

	/**
	 * 保存收款单
	 */
	@RequiresPermissions("ct:ctChargeSk:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtChargeSk ctChargeSk) {
		ctChargeSkService.save(ctChargeSk);
		return renderResult(Global.TRUE, text("保存收款单成功！"));
	}
	
	/**
	 * 删除收款单
	 */
	@RequiresPermissions("ct:ctChargeSk:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CtChargeSk ctChargeSk) {
		ctChargeSkService.delete(ctChargeSk);
		return renderResult(Global.TRUE, text("删除收款单成功！"));
	}
	/**
	 * 应收单穿透制首款单选取数据跳转编辑页面
	 */
	@RequiresPermissions("ct:ctChargeSk:view")
	@RequestMapping(value = "refToYs")
	public String refToYs(String pkChargeYs, Model model){
		CtChargeSk ctChargeSk=ctChargeSkService.refData(pkChargeYs);
		ctChargeSk.setCreator(UserUtils.getUser());
		ctChargeSk.setCreationtime(new Timestamp((new Date()).getTime()));
		model.addAttribute("ctChargeSk", ctChargeSk);
		model.addAttribute("isEdit", "true");
		model.addAttribute("isCz", "false");
		return "modules/ct/ctChargeSkForm";
	}
	//审批
	@PostMapping(value = "subSk")
	@ResponseBody
	public String subSk(String pks){
	
		ctChargeSkService.subData(pks);
		return renderResult(Global.TRUE, text("单据审批成功"));
	}
	/**
	 * 批量审批功能
	 */
	@RequestMapping(value = "approveAll")
	@ResponseBody
	public String approveAll(@RequestBody List<String> list) {
		for(String pk:list){
			ctChargeSkService.subData(pk);
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
			boolean aa =ctChargeSkService.unSubData(pk);
			 if(!aa){
				   return renderResult(Global.FALSE, text("该收款单不是最新一笔收款单,请先删除最新一笔收款单!"));
			   }
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
			CtChargeSk ctInvoiceapply=ctChargeSkService.get(pk);
			Integer spstatus=ctInvoiceapply.getVbillstatus();
			if(AbsEnumType.BillStatus_SPTG==spstatus){
				return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
			}
			ctChargeSkService.delete(ctInvoiceapply);
		}
		return renderResult(Global.TRUE, text("批量删除成功！"));
	}
}