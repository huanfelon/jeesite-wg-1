/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.web;

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
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.wght.entity.WgThrowalease;
import com.jeesite.modules.wght.service.WgThrowaleaseService;

/**
 * 退租管理Controller
 * @author LY
 * @version 2019-12-19
 */
@Controller
@RequestMapping(value = "${adminPath}/wght/wgThrowalease")
public class WgThrowaleaseController extends BaseController {

	@Autowired
	private WgThrowaleaseService wgThrowaleaseService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WgThrowalease get(String pkThrowalease, boolean isNewRecord) {
		return wgThrowaleaseService.get(pkThrowalease, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("wght:wgThrowalease:view")
	@RequestMapping(value = {"list", ""})
	public String list(WgThrowalease wgThrowalease, Model model) {
		model.addAttribute("wgThrowalease", wgThrowalease);
		return "modules/wght/wgThrowaleaseList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("wght:wgThrowalease:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<WgThrowalease> listData(WgThrowalease wgThrowalease, HttpServletRequest request, HttpServletResponse response) {
		wgThrowalease.setPage(new Page<>(request, response));
		Page<WgThrowalease> page = wgThrowaleaseService.findPage(wgThrowalease);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("wght:wgThrowalease:view")
	@RequestMapping(value = "form")
	public String form(WgThrowalease wgThrowalease, Boolean isEdit, Model model) {
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("wgThrowalease", wgThrowalease);
		return "modules/wght/wgThrowaleaseForm";
	}
	
	/**
	 * 退租参照选取数据跳转编辑页面
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "tzRefToAdd")
	public String tzRefToAdd(String pk, Model model){
		WgThrowalease wgThrowalease=wgThrowaleaseService.refDataRender(pk);
		wgThrowalease.setVbillno(CodeFactoryUtils.createBillCode("TZGL", "wg_throwalease", "vbillno", "billtype", AbsEnumType.BillType_TZ));//单据号自动生成
		model.addAttribute("wgThrowalease", wgThrowalease);
		model.addAttribute("isEdit", true);
		return "modules/wght/wgThrowaleaseForm";
	}

	/**
	 * 保存退租管理
	 */
	@RequiresPermissions("wght:wgThrowalease:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated WgThrowalease wgThrowalease) {
		String msg=wgThrowaleaseService.saveData(wgThrowalease);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("保存退租管理成功！"));
	}
	
	/**
	 * 删除退租管理
	 */
	@RequiresPermissions("wght:wgThrowalease:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(WgThrowalease wgThrowalease) {
		wgThrowaleaseService.delete(wgThrowalease);
		return renderResult(Global.TRUE, text("删除退租管理成功！"));
	}
	
	/**
	 * 删除退租管理
	 */
	@RequiresPermissions("wght:wgThrowalease:edit")
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String delete(@RequestBody List<String> listpk) {
		String msg=wgThrowaleaseService.deleteData(listpk);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("删除退租管理成功！"));
	}
	
	/**
	 * 修改退租管理
	 */
	@RequiresPermissions("wght:wgThrowalease:edit")
	@RequestMapping(value = "edit")
	@ResponseBody
	public String edit(WgThrowalease wgThrowalease) {
		WgThrowalease entity=wgThrowaleaseService.get(wgThrowalease);
		if(!entity.getVbillstatus().equals(AbsEnumType.BillStatus_ZY)){
			return renderResult(Global.FALSE, text("单据状态不是自由态，不可修改！"));
		}
		return "";
	}
	
	/**
	 * 批量审批功能
	 */
	@RequiresPermissions("wght:wgThrowalease:edit")
	@RequestMapping(value = "approveAll")
	@ResponseBody
	public String approveAll(@RequestBody List<String> listpk) {
		String msg=wgThrowaleaseService.approveAllData(listpk);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("审批成功！"));
	}
	
	/**
	 * 批量弃审功能
	 */
	@RequiresPermissions("wght:wgThrowalease:edit")
	@RequestMapping(value = "unapproveAll")
	@ResponseBody
	public String unapproveAll(@RequestBody List<String> listpk) {
		String msg=wgThrowaleaseService.unApproveAllData(listpk);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("取消审批成功！"));
	}
	
}