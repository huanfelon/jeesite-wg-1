/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.web;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.zl.entity.ZlFamilyfile;
import com.jeesite.modules.zl.entity.ZlProject;
import com.jeesite.modules.zl.service.ZlFamilyfileService;
import com.jeesite.modules.zl.service.ZlHousesourceService;

/**
 * zl_familyfileController
 * @author GuoJ
 * @version 2019-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/zl/zlFamilyfile")
public class ZlFamilyfileController extends BaseController {

	@Autowired
	private ZlFamilyfileService zlFamilyfileService;
	@Autowired
	private ZlHousesourceService zlHousesourceService;
	@Autowired
	private CommonBaseService commonService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZlFamilyfile get(String pkFamilyfile, boolean isNewRecord) {
		return zlFamilyfileService.get(pkFamilyfile, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("zl:zlFamilyfile:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZlFamilyfile zlFamilyfile, Model model) {
		model.addAttribute("zlFamilyfile", zlFamilyfile);
		return "modules/zl/zlFamilyfileList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("zl:zlFamilyfile:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZlFamilyfile> listData(ZlFamilyfile zlFamilyfile, HttpServletRequest request, HttpServletResponse response) {
/*		 if(zlFamilyfile.getDbilldate_lte()!=null){
	        	Date dt=zlFamilyfile.getDbilldate_lte();
	    		SimpleDateFormat djrqsdf = new SimpleDateFormat("yyyy-MM-dd");
	    		String djrqdateFormat = djrqsdf.format(dt);
	    	    String str=djrqdateFormat +" 23:59:59";
	    	    str=str.replaceAll("-", "/");
	    	    Date dt2=new Date(str);
	    	    zlFamilyfile.setDbilldate_lte(dt2);
	        }*/
		zlFamilyfile.setPage(new Page<>(request, response));
		Page<ZlFamilyfile> page = zlFamilyfileService.findPage(zlFamilyfile);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlFamilyfile:view")
	@RequestMapping(value = "form")
	public String form(ZlFamilyfile zlFamilyfile, String isEdit,Model model) {
		if(isEdit.equals("true")&& StringUtils.isBlank(zlFamilyfile.getPkFamilyfile())){
			zlFamilyfile.setCreator(UserUtils.getUser());
			zlFamilyfile.setDbilldate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(zlFamilyfile.getPkFamilyfile())) {
			zlFamilyfile.setModifier(UserUtils.getUser());
			zlFamilyfile.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("zlFamilyfile", zlFamilyfile);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlFamilyfileForm";
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlFamilyfile:view")
	@RequestMapping(value = "xgform")
	public String xgform(ZlFamilyfile zlFamilyfile, String isEdit,Model model) {
		if(isEdit.equals("true")&& StringUtils.isBlank(zlFamilyfile.getPkFamilyfile())){
			zlFamilyfile.setCreator(UserUtils.getUser());
			zlFamilyfile.setDbilldate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(zlFamilyfile.getPkFamilyfile())) {
			zlFamilyfile.setModifier(UserUtils.getUser());
			zlFamilyfile.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("zlFamilyfile", zlFamilyfile);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlFamilyfileXgForm";
	}
	/**
	 * 保存户型信息
	 */
	@RequiresPermissions("zl:zlFamilyfile:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZlFamilyfile zlFamilyfile) {
		String code=zlFamilyfileService.save1(zlFamilyfile);
		return renderResult(Global.TRUE, text("保存户型信息成功！"),code);
	}
	
	/**
	 * 删除户型信息
	 */
	@RequiresPermissions("zl:zlFamilyfile:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZlFamilyfile zlFamilyfile) {
		zlFamilyfileService.delete(zlFamilyfile);
		return renderResult(Global.TRUE, text("删除户型信息成功！"));
	}
	/**
	 * 房源新增参照查询
	 */
	@RequiresPermissions("zl:zlFamilyfile:edit")
	@RequestMapping({"familySelect"})
	public String familySelect(ZlFamilyfile zlFamilyfile,String family, String selectData, Model model)
	{
		if(zlFamilyfile.getPkProjectid()==null&&family==null){
			ZlProject zlProject = new ZlProject();
			zlProject.setPkProject("0");
			zlFamilyfile.setPkProjectid(zlProject);
		}else if(StringUtils.isNotBlank(family)){
			ZlFamilyfile zlFamilyfile2 =zlFamilyfileService.get(family);
			ZlProject zlProject = new ZlProject();
			zlProject.setPkProject(zlFamilyfile2.getPkProjectid().getPkProject());
			zlFamilyfile.setPkProjectid(zlProject);
		}
		model.addAttribute("zlFamilyfile", zlFamilyfile);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson); 
		}
		return "modules/refPages/familySelect";
	}
	/**
	 * 房源新增参照查询cw
	 */
	@RequiresPermissions("zl:zlFamilyfile:edit")
	@RequestMapping({"familySelect2"})
	public String familySelect2(ZlFamilyfile zlFamilyfile,String family, String selectData, Model model)
	{
		if(zlFamilyfile.getPkProjectid()==null&&family==null){
			ZlProject zlProject = new ZlProject();
			zlProject.setPkProject("0");
			zlFamilyfile.setPkProjectid(zlProject);
		}else if(StringUtils.isNotBlank(family)){
			ZlFamilyfile zlFamilyfile2 =zlFamilyfileService.get(family);
			ZlProject zlProject = new ZlProject();
			zlProject.setPkProject(zlFamilyfile2.getPkProjectid().getPkProject());
			zlFamilyfile.setPkProjectid(zlProject);
		}
		model.addAttribute("zlFamilyfile", zlFamilyfile);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson); 
		}
		return "modules/refPages/familySelect2";
	}
	/**
	 * 房源列表参照查询
	 */
	@RequiresPermissions("zl:zlFamilyfile:edit")
	@RequestMapping({"familySelect1"})
	public String familySelect1(ZlFamilyfile zlFamilyfile,String family, String selectData, Model model)
	{
		model.addAttribute("zlFamilyfile", zlFamilyfile);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson); 
		}
		return "modules/refPages/familySelect";
	}
	//修改已被参照的项目
	@RequiresPermissions("zl:zlFamilyfile:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkFamilyfile,boolean isNewRecord){
		   //查询房源
		   String sql="select count(*) from zl_housesource z where z.dr=0 and z.pk_familyfile='"+pkFamilyfile+"'";
		   Integer count= commonService.selectCount(sql);
	    	if (count>0) {
			    return "isXj";
		    }
		 return pkFamilyfile;
    }
	//选择户型带出业态、租赁、产证
	@RequiresPermissions("zl:zlFamilyfile:edit")
	@RequestMapping(value = "out")
	@ResponseBody
	public ZlFamilyfile out(String pkFamilyfileCode){
		ZlFamilyfile zlFamilyfile = zlFamilyfileService.get(pkFamilyfileCode);
	
		return zlFamilyfile;
	}
	/**
	 * 房源页面户型参照查询
	 */
	@RequiresPermissions("zl:zlFamilyfile:view")
	@RequestMapping(value = "listFamily")
	@ResponseBody
	public List<ZlFamilyfile> listFamily(ZlFamilyfile zlFamilyfile, HttpServletRequest request, HttpServletResponse response) {
	
		List<ZlFamilyfile> page = zlFamilyfileService.findList(zlFamilyfile);
		return page;
	}
	/**
	 * 房源页面户型参照查询
	 */
	@RequiresPermissions("zl:zlFamilyfile:view")
	@RequestMapping(value = "listFamily1")
	@ResponseBody
	public List<ZlFamilyfile> listFamily1(ZlFamilyfile zlFamilyfile, HttpServletRequest request, HttpServletResponse response) {
	
		List<ZlFamilyfile> page = new ArrayList<ZlFamilyfile>();
		return page;
	}
}