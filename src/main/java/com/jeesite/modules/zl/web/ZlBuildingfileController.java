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
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.service.ZlBuildingfileService;
import com.jeesite.modules.zl.service.ZlHousesourceService;

/**
 * zl_buildingfileController
 * @author GuoJ
 * @version 2019-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/zl/zlBuildingfile")
public class ZlBuildingfileController extends BaseController {

	@Autowired
	private ZlBuildingfileService zlBuildingfileService;
	@Autowired
	private ZlHousesourceService zlHousesourceService;
	@Autowired
	private CommonBaseService commonService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZlBuildingfile get(String pkBuildingfile, boolean isNewRecord) {
		return zlBuildingfileService.get(pkBuildingfile, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("zl:zlBuildingfile:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZlBuildingfile zlBuildingfile, Model model) {
		model.addAttribute("zlBuildingfile", zlBuildingfile);
		return "modules/zl/zlBuildingfileList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("zl:zlBuildingfile:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZlBuildingfile> listData(ZlBuildingfile zlBuildingfile, HttpServletRequest request, HttpServletResponse response) {
		/* if(zlBuildingfile.getDbilldate_lte()!=null){
	        	Date dt=zlBuildingfile.getDbilldate_lte();
	    		SimpleDateFormat djrqsdf = new SimpleDateFormat("yyyy-MM-dd");
	    		String djrqdateFormat = djrqsdf.format(dt);
	    	    String str=djrqdateFormat +" 23:59:59";
	    	    str=str.replaceAll("-", "/");
	    	    Date dt2=new Date(str);
	    	    zlBuildingfile.setDbilldate_lte(dt2);
	        }*/
		zlBuildingfile.setPage(new Page<>(request, response));
		//zlBuildingfileService.queryFilter(zlBuildingfile);
		Page<ZlBuildingfile> page = zlBuildingfileService.findPage(zlBuildingfile);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlBuildingfile:view")
	@RequestMapping(value = "form")
	public String form(ZlBuildingfile zlBuildingfile,String isEdit, Model model) {
		if(isEdit.equals("true")&&StringUtils.isBlank(zlBuildingfile.getPkBuildingfile())){//新增时塞默认值
			zlBuildingfile.setCreator(UserUtils.getUser());
			zlBuildingfile.setDbilldate(new Timestamp((new Date()).getTime()));
			zlBuildingfile.setNproperty(0);
			zlBuildingfile.setBuiltuparea(new Double(0));
			zlBuildingfile.setInnerarea(new Double(0));
		}else if ( StringUtils.isNotBlank(zlBuildingfile.getPkBuildingfile())) {
			zlBuildingfile.setModifier(UserUtils.getUser());
			zlBuildingfile.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("zlBuildingfile", zlBuildingfile);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlBuildingfileForm";
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlBuildingfile:view")
	@RequestMapping(value = "xgform")
	public String xgform(ZlBuildingfile zlBuildingfile,String isEdit, Model model) {
		
		if(isEdit.equals("true")&&StringUtils.isBlank(zlBuildingfile.getPkBuildingfile())){//新增时塞默认值
			zlBuildingfile.setCreator(UserUtils.getUser());
			zlBuildingfile.setDbilldate(new Timestamp((new Date()).getTime()));
			zlBuildingfile.setNproperty(0);
			zlBuildingfile.setBuiltuparea(new Double(0));
			zlBuildingfile.setInnerarea(new Double(0));
		}else if ( StringUtils.isNotBlank(zlBuildingfile.getPkBuildingfile())) {
			/*zlBuildingfile.getPkOrg().setOfficeName("("+zlBuildingfile.getPkOrg().getOfficeCode()+") "+zlBuildingfile.getPkOrg().getOfficeName());
			zlBuildingfile.getPkProjectid().setName("("+zlBuildingfile.getPkProjectid().getCode()+") "+zlBuildingfile.getPkProjectid().getName());*/
			zlBuildingfile.setModifier(UserUtils.getUser());
			zlBuildingfile.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("zlBuildingfile", zlBuildingfile);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlBuildingfileXgForm";
	}

	/**
	 * 保存楼栋档案
	 */
	@RequiresPermissions("zl:zlBuildingfile:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZlBuildingfile zlBuildingfile) {
		String code=zlBuildingfileService.save2(zlBuildingfile);
		return renderResult(Global.TRUE, text("保存楼栋档案成功！"),code);
	}
	
	/**
	 * 删除楼栋档案
	 */
	@RequiresPermissions("zl:zlBuildingfile:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZlBuildingfile zlBuildingfile) {
		zlBuildingfileService.delete(zlBuildingfile);
		return renderResult(Global.TRUE, text("删除楼栋档案成功！"));
	}
	/**
	 * 参照查询
	 */
	@RequiresPermissions("zl:zlBuildingfile:edit")
	@RequestMapping({"buildingSelect"})
	public String buildingSelect(ZlBuildingfile zlBuildingfile, String selectData, Model model)
	{
		model.addAttribute("zlBuildingfile", zlBuildingfile);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson);
		}
		return "modules/refPages/buildingSelect";
	}
	//修改已被参照的树结构项目
	@RequiresPermissions("zl:zlBuildingfile:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkBuildingfile,boolean isNewRecord){
		   //查询房源
		   String sql="select count(*) from zl_housesource z where z.dr=0 and z.buildname='"+pkBuildingfile+"'";
		   Integer count= commonService.selectCount(sql);
	    	if (count>0) {
			 return "isXj";
		    }
    	   //查询合同管理房产页签
		   String sql1="select count(*) from wg_contract_house z  where z.dr=0 and z.pk_build='"+pkBuildingfile+"'";
		   Integer count1= commonService.selectCount(sql1);
	    	if (count1>0) {
	    		 return "isXj";
		    }
		 return pkBuildingfile;
    }
	/**
	 * 房源节点查询楼栋参照
	 */
	@RequiresPermissions("zl:zlBuildingfile:view")
	@RequestMapping(value = "listBuild")
	@ResponseBody
	public List<ZlBuildingfile> listBuild(ZlBuildingfile zlBuildingfile, HttpServletRequest request, HttpServletResponse response) {

		List<ZlBuildingfile> page = zlBuildingfileService.findList(zlBuildingfile);
		return page;
	}
	/**
	 * 房源节点查询楼栋参照
	 */
	@RequiresPermissions("zl:zlBuildingfile:view")
	@RequestMapping(value = "listBuild1")
	@ResponseBody
	public List<ZlBuildingfile> listBuild1(ZlBuildingfile zlBuildingfile, HttpServletRequest request, HttpServletResponse response) {

		List<ZlBuildingfile> page = new ArrayList<ZlBuildingfile>();
		return page;
	}
}