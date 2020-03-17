/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.web;

import java.sql.Timestamp;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.entity.BdFeescale;
import com.jeesite.modules.bd.service.BdFeescaleService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.wght.dao.WgContractWyfDao;
import com.jeesite.modules.wght.dao.WgContractZqfyDao;

/**
 * 收费标准Controller
 * @author GJ
 * @version 2019-10-25
 */
@Controller
@RequestMapping(value = "${adminPath}/bd/bdFeescale")
public class BdFeescaleController extends BaseController {

	@Autowired
	private BdFeescaleService bdFeescaleService;
	@Autowired
	private WgContractWyfDao wgContractWyfDao;
	@Autowired
	private WgContractZqfyDao wgContractZqfyDao;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdFeescale get(String pkFeescale, boolean isNewRecord) {
		return bdFeescaleService.get(pkFeescale, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("bd:bdFeescale:view")
	@RequestMapping(value = {"list", ""})
	public String list(BdFeescale bdFeescale, Model model) {
		model.addAttribute("bdFeescale", bdFeescale);
		return "modules/bd/bdFeescaleList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdFeescale:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BdFeescale> listData(BdFeescale bdFeescale, HttpServletRequest request, HttpServletResponse response) {
		bdFeescale.setPage(new Page<>(request, response));
		Page<BdFeescale> page = bdFeescaleService.findPage(bdFeescale);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("bd:bdFeescale:view")
	@RequestMapping(value = "form")
	public String form(BdFeescale bdFeescale, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdFeescale.getPkFeescale())){
			bdFeescale.setCreator(UserUtils.getUser());
			bdFeescale.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdFeescale.getPkFeescale())) {
			bdFeescale.setModifier(UserUtils.getUser());
			bdFeescale.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("bdFeescale", bdFeescale);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdFeescaleForm";
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("bd:bdFeescale:view")
	@RequestMapping(value = "xgform")
	public String xgform(BdFeescale bdFeescale, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdFeescale.getPkFeescale())){
			bdFeescale.setCreator(UserUtils.getUser());
			bdFeescale.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdFeescale.getPkFeescale())) {
			bdFeescale.setModifier(UserUtils.getUser());
			bdFeescale.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("bdFeescale", bdFeescale);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdFeescaleXgForm";
	}
	/**
	 * 保存收费标准
	 */
	@RequiresPermissions("bd:bdFeescale:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdFeescale bdFeescale) {
		bdFeescaleService.save(bdFeescale);
		return renderResult(Global.TRUE, text("保存收费标准成功！"));
	}
	
	/**
	 * 停用收费标准
	 */
	@RequiresPermissions("bd:bdFeescale:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdFeescale bdFeescale) {
		bdFeescale.setStatus(BdFeescale.STATUS_DISABLE);
		bdFeescaleService.updateStatus(bdFeescale);
		return renderResult(Global.TRUE, text("停用收费标准成功"));
	}
	
	/**
	 * 启用收费标准
	 */
	@RequiresPermissions("bd:bdFeescale:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdFeescale bdFeescale) {
		bdFeescale.setStatus(BdFeescale.STATUS_NORMAL);
		bdFeescaleService.updateStatus(bdFeescale);
		return renderResult(Global.TRUE, text("启用收费标准成功"));
	}
	
	/**
	 * 删除收费标准
	 */
	@RequiresPermissions("bd:bdFeescale:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdFeescale bdFeescale) {
		bdFeescaleService.delete(bdFeescale);
		return renderResult(Global.TRUE, text("删除收费标准成功！"));
	}
	/**
	 * 参照查询
	 */
	@RequiresPermissions("bd:bdFeescale:edit")
	@RequestMapping({"feescaleSelect"})
	public String feescaleSelect(BdFeescale bdFeescale, String selectData, Model model)
	{
		model.addAttribute("bdFeescale", bdFeescale);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson);
		}
		return "modules/refPages/bdFeescaleSelect";
	}
	//修改已被参照的项目
	@RequiresPermissions("bd:bdFeescale:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkFeescale,boolean isNewRecord){
		//查询物业费页签
		 String sql="select count(*) from wg_contract_wyf w where w.dr=0 and w.pk_feescale='"+pkFeescale+"'";
		  Integer count= commonService.selectCount(sql);
		if (count>0) {
				return "isXj";
		}
		//查询其他周期费用
		 String sql1="select count(*) from wg_contract_zqfy z where z.dr=0 and z.pk_feescale='"+pkFeescale+"'";
		  Integer count1= commonService.selectCount(sql1);
		if (count1>0) {
			return "isXj";
		}
		 return pkFeescale;
    }
}