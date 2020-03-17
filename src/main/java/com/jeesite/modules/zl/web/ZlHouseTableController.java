/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.zl.entity.ZlHousesource;
import com.jeesite.modules.zl.service.ZlHouseTableService;

/**
 * zl_houseTableController
 * @author LY
 * @version 2019-08-26
 */
@Controller
@RequestMapping(value = "${adminPath}/zl/zlHouseTable")
public class ZlHouseTableController extends BaseController {

	@Autowired
	private ZlHouseTableService zlHouseTableService;
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("zl:zlHouseTable:view")
	@RequestMapping(value = {"show", ""})
	public String list(ZlHousesource zlHousesource, Model model) {
		model.addAttribute("zlHousesource", zlHousesource);
		return "modules/zl/zlHouseTableShow";
	}
	
	/**
	 * 查询房源表数据
	 */
	@RequiresPermissions("zl:zlHouseTable:view")
	@RequestMapping(value = "dataShow")
	@ResponseBody
	public List<Map<String,Object>> listData(String pkOrg,String pkPro,String pkBuild,String busidate,HttpServletRequest request, HttpServletResponse response) {
		return zlHouseTableService.findTableData(pkOrg,pkPro,pkBuild,busidate);
	}
	
}