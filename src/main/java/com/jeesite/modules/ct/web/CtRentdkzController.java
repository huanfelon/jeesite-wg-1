/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ct.entity.CtRentdkz;
import com.jeesite.modules.ct.service.CtRentdkzService;

/**
 * 租约待开账Controller
 * @author tcl
 * @version 2019-11-06
 */
@Controller
@RequestMapping(value = "${adminPath}/ct/ctRentdkz")
public class CtRentdkzController extends BaseController {

	@Autowired
	private CtRentdkzService ctRentdkzService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CtRentdkz get(String pkRentdkz, boolean isNewRecord) {
		return ctRentdkzService.get(pkRentdkz, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ct:ctRentdkz:view")
	@RequestMapping(value = {"list", ""})
	public String list(CtRentdkz ctRentdkz, Model model) {
		model.addAttribute("ctRentdkz", ctRentdkz);
		return "modules/ct/ctRentdkzList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ct:ctRentdkz:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CtRentdkz> listData(CtRentdkz ctRentdkz,String busidate, HttpServletRequest request, HttpServletResponse response) {
		ctRentdkz.setPage(new Page<>(request, response));
		Page<CtRentdkz> page = ctRentdkzService.findPageData(ctRentdkz,busidate);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ct:ctRentdkz:view")
	@RequestMapping(value = "form")
	public String form(CtRentdkz ctRentdkz,String noEdit, Model model) {
		boolean edit=Boolean.valueOf(noEdit);
		model.addAttribute("noEdit",edit);
		model.addAttribute("ctRentdkz", ctRentdkz);
		return "modules/ct/ctRentdkzForm";
	}

	/**
	 * 保存租约待开账
	 */
	@RequiresPermissions("ct:ctRentdkz:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CtRentdkz ctRentdkz) {
		ctRentdkzService.save(ctRentdkz);
		return renderResult(Global.TRUE, text("保存租约待开账成功！"));
	}
	
	/**
	 * 批量开账
	 */
	@RequestMapping(value = "batchOpening")
	@ResponseBody
	public String batchOpening(String[] pks,String busidate) {
		String msg=ctRentdkzService.batchOpening(pks,busidate);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("批量开账成功！"));
	}
	
}