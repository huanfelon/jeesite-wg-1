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
import com.jeesite.modules.ct.entity.CtChargeSrqr;
import com.jeesite.modules.ct.service.CtChargeSrqrService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 收入确认Controller
 * @author GJ
 * @version 2019-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctChargeSrqr")
public class CtChargeSrqrController extends BaseController {

	@Autowired
	private CtChargeSrqrService ctChargeSrqrService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtChargeSrqr get(String pkChargeSrqr, boolean isNewRecord) {
		return ctChargeSrqrService.get(pkChargeSrqr, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctChargeSrqr:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtChargeSrqr ctChargeSrqr, Model model) {
		model.addAttribute("ctChargeSrqr", ctChargeSrqr);
		return "modules/ct/ctChargeSrqrList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctChargeSrqr:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtChargeSrqr> listData(CtChargeSrqr ctChargeSrqr, HttpServletRequest request, HttpServletResponse response) {
		ctChargeSrqr.setPage(new Page<>(request, response));
		Page<CtChargeSrqr> page = ctChargeSrqrService.findPage(ctChargeSrqr);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctChargeSrqr:view")
	@RequestMapping(value = "form")
	public String form(CtChargeSrqr ctChargeSrqr, Model model,String isEdit,String isCz) {
		if(isEdit.equals("true")&& StringUtils.isBlank(ctChargeSrqr.getPkChargeSrqr())){
			ctChargeSrqr.setCreator(UserUtils.getUser());
			ctChargeSrqr.setCreationtime(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(ctChargeSrqr.getPkChargeSrqr())) {
			ctChargeSrqr.setModifier(UserUtils.getUser());
			ctChargeSrqr.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("ctChargeSrqr", ctChargeSrqr);
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("isCz", isCz);
		return "modules/ct/ctChargeSrqrForm";
	}

	/**
	 * 保存收入确认
	 */
	@RequiresPermissions("ct:ctChargeSrqr:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtChargeSrqr ctChargeSrqr) {
		ctChargeSrqrService.save(ctChargeSrqr);
		return renderResult(Global.TRUE, text("保存收入确认成功！"));
	}
	
	/**
	 * 删除收入确认
	 */
	@RequiresPermissions("ct:ctChargeSrqr:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CtChargeSrqr ctChargeSrqr) {
		ctChargeSrqrService.delete(ctChargeSrqr);
		return renderResult(Global.TRUE, text("删除收入确认成功！"));
	}
	/**
	 * 批量审批功能
	 */
	@RequestMapping(value = "approveAll")
	@ResponseBody
	public String approveAll(@RequestBody List<String> list) {
		for(String pk:list){
			ctChargeSrqrService.subData(pk);
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
			boolean aa =ctChargeSrqrService.unSubData(pk);
			if(!aa){
				   return renderResult(Global.FALSE, text("该收入确认单不是最新一笔该收入确认单,请先取消最新一笔该收入确认单!"));
			   }
		}
		return renderResult(Global.TRUE, text("取消审批成功！"));
	}
	/**
	 * 收入确认参照应确认收入选取数据跳转编辑页面
	 */
	@RequiresPermissions("ct:ctChargeSrqr:view")
	@RequestMapping(value = "refToYqrsr")
	public String refToYqrsr(String pkChargeYqrsr, Model model){
		CtChargeSrqr ctChargeSrqr=ctChargeSrqrService.refData(pkChargeYqrsr);
		ctChargeSrqr.setCreator(UserUtils.getUser());
		ctChargeSrqr.setCreationtime(new Timestamp((new Date()).getTime()));
		model.addAttribute("ctChargeSrqr", ctChargeSrqr);
		model.addAttribute("isEdit", "true");
		model.addAttribute("isCz", "false");
		return "modules/ct/ctChargeSrqrForm";
	}
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String deleteAllData(@RequestBody List<String> list) {
		for(String pk:list){
			//从列表转换的数据，需要重新获取
			CtChargeSrqr ctInvoiceapply=ctChargeSrqrService.get(pk);
			Integer spstatus=ctInvoiceapply.getVbillstatus();
			if(AbsEnumType.BillStatus_SPTG==spstatus){
				return renderResult(Global.FALSE, text("单据已审批，不可删除！"));
			}
			ctChargeSrqrService.delete(ctInvoiceapply);
		}
		return renderResult(Global.TRUE, text("批量删除成功！"));
	}
}