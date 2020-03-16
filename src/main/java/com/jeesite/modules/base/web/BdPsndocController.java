/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.web;

import java.util.HashMap;
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
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.base.validator.CodeValidatorUtils;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.base.entity.BdPsndoc;
import com.jeesite.modules.base.entity.BdPsndocJob;
import com.jeesite.modules.base.service.BdCustomerService;
import com.jeesite.modules.base.service.BdPsndocService;

/**
 * 人员基本信息Controller
 * @author LY
 * @version 2019-09-26
 */
@Controller
@RequestMapping(value = "${adminPath}/base/bdPsndoc")
public class BdPsndocController extends BaseController {

	@Autowired
	private BdPsndocService bdPsndocService;
	@Autowired
	private BdCustomerService custService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdPsndoc get(String pkPsndoc, boolean isNewRecord) {
		return bdPsndocService.get(pkPsndoc, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(BdPsndoc bdPsndoc, Model model) {
		model.addAttribute("bdPsndoc", bdPsndoc);
		return "modules/base/bdPsndocList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BdPsndoc> listData(BdPsndoc bdPsndoc, HttpServletRequest request, HttpServletResponse response) {
		bdPsndoc.setPage(new Page<>(request, response));
		Page<BdPsndoc> page = bdPsndocService.findPage(bdPsndoc);
		
		//带出用户名称
		List<BdPsndoc> list=page.getList();
		for(BdPsndoc bd:list){
			bdPsndocService.setDeptNameByPks(bd);
		}
		return page;
	}
	
	/**
	 * 参照列表数据查询
	 */
	@RequestMapping({"bdPsndocSelect"})
	public String refListSelect(BdPsndoc bdPsndoc, String selectData, Model model)
	{
		model.addAttribute("bdPsndoc", bdPsndoc);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson);
		}
		return "modules/refPages/bdPsndocSelect";
	}
	
	/**
	 * 修改人员基本信息
	 */
	@RequiresPermissions("base:bdPsndoc:edit")
	@RequestMapping(value = "edit")
	@ResponseBody
	public String edit(BdPsndoc bdPsndoc) {
		Integer count=bdPsndocService.getCustCountByPsndoc(bdPsndoc.getPkPsndoc(), "");
		if(count>0){
			return renderResult(Global.FALSE, text("该人员已被用户关联身份，不可修改！"));
		}
		return "";
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("base:bdPsndoc:view")
	@RequestMapping(value = "form")
	public String form(BdPsndoc bdPsndoc,String noEdit, Model model) {
		//设置表头部门权限
		bdPsndocService.setDeptNameByPks(bdPsndoc);
		//设置表体部门权限
		List<BdPsndocJob> blist=bdPsndoc.getBdPsndocJobList();
		if(blist!=null&&blist.size()>0){
			for(BdPsndocJob psnjob:blist){
				bdPsndocService.setBodyDeptNameByPks(psnjob);
			}
		}
		boolean edit=Boolean.valueOf(noEdit);
		model.addAttribute("noEdit",edit);
		model.addAttribute("bdPsndoc", bdPsndoc);
		return "modules/base/bdPsndocForm";
	}

	/**
	 * 保存人员基本信息
	 */
	@RequiresPermissions("base:bdPsndoc:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdPsndoc bdPsndoc) {
		//校验任职单元是否重复
		Map<String, String> map=new HashMap<String, String>();
		map.put(bdPsndoc.getPkOrg().getId(), bdPsndoc.getPkOrg().getId());
		for(BdPsndocJob bdPsndocJob : bdPsndoc.getBdPsndocJobList()){
			if(map.containsKey(bdPsndocJob.getPkOrg().getId())){
				return renderResult(Global.FALSE, text("人员任职业务单元不可重复！"));
			}else{
				map.put(bdPsndocJob.getPkOrg().getId(), bdPsndocJob.getPkOrg().getId());
			}
		}
		//校验编码重复
		CodeValidatorUtils.validatorCode(new BdPsndoc(), bdPsndoc,"code",bdPsndoc.getCode(), bdPsndocService);
		bdPsndocService.save(bdPsndoc);
		return renderResult(Global.TRUE, text("保存人员基本信息成功！"));
	}
	
	/**
	 * 停用人员基本信息
	 */
	@RequiresPermissions("base:bdPsndoc:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdPsndoc bdPsndoc) {
		Integer count=bdPsndocService.getCustCountByPsndoc(bdPsndoc.getPkPsndoc(), "");
		if(count>0){
			return renderResult(Global.FALSE, text("该人员已被用户关联身份，不可停用！"));
		}
		
		//判断人员是否被客户引用 -预留
		BdCustomer cust=new BdCustomer();
		cust.setPkPsndoc(bdPsndoc);
		List<BdCustomer> custlist=custService.findList(cust);
		if(custlist.size()>0){
			return renderResult(Global.FALSE, text("该人员已被客户关联，不可停用！"));
		}
		bdPsndoc.setStatus(BdPsndoc.STATUS_DISABLE);
		bdPsndocService.updateStatus(bdPsndoc);
		return renderResult(Global.TRUE, text("停用人员基本信息成功"));
	}
	
	/**
	 * 启用人员基本信息
	 */
	@RequiresPermissions("base:bdPsndoc:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdPsndoc bdPsndoc) {
		bdPsndoc.setStatus(BdPsndoc.STATUS_NORMAL);
		bdPsndocService.updateStatus(bdPsndoc);
		return renderResult(Global.TRUE, text("启用人员基本信息成功"));
	}
	
	/**
	 * 删除人员基本信息
	 */
	@RequiresPermissions("base:bdPsndoc:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdPsndoc bdPsndoc) {
		Integer count=bdPsndocService.getCustCountByPsndoc(bdPsndoc.getPkPsndoc(), "");
		if(count>0){
			return renderResult(Global.FALSE, text("该人员已被用户关联身份，不可删除！"));
		}
		//判断人员是否被客户引用 -预留
		BdCustomer cust=new BdCustomer();
		cust.setPkPsndoc(bdPsndoc);
		List<BdCustomer> custlist=custService.findList(cust);
		if(custlist.size()>0){
			return renderResult(Global.FALSE, text("该人员已被客户关联，不可删除！"));
		}
		
		//判断人员是否被业务单据引用 -预留
		bdPsndocService.delete(bdPsndoc);
		return renderResult(Global.TRUE, text("删除人员基本信息成功！"));
	}
	
	/**
	 * 根据登录用户获取关联的人员
	 */
	@RequestMapping({"getUserPsndoc"})
	public Map<String,Object> getUserPsndoc(String userCode){
		return bdPsndocService.getUserPsndoc(userCode);
	}
	
	/**
	 * 判断当前人员是否已被其他用户绑定
	 */
	@RequestMapping({"getCustCountByPsndoc"})
	@ResponseBody
	public String getCustCountByPsndoc(String pkPsndoc, String userCode){
		Integer count=bdPsndocService.getCustCountByPsndoc(pkPsndoc,userCode);
		String msg="";
		if(count>0){
			msg="该人员已被其他用户绑定身份，请重新选择！";
		}
		return renderResult(Global.TRUE, text(msg));
	}
	
	/**
	 * 导出数据
	 * @param empUser
	 * @param isAll
	 * @param ctrlPermi
	 * @param response
	 */
	  @RequestMapping({"exportData"})
	  public void exportData(BdPsndoc bdPsndoc, Boolean isAll, String ctrlPermi, HttpServletResponse response)
	  {
		/* if ((isAll == null) || (!isAll.booleanValue())) {
	      this.bdCarService.addDataScopeFilter(bdCar, ctrlPermi);
	    }*/
	    List<BdPsndoc> list = this.bdPsndocService.findList(bdPsndoc);
	    String fileName = "人员数据" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
	    ExcelExport ee = new ExcelExport("人员数据", BdPsndoc.class);
	    Throwable localThrowable3 = null;
	    try { 
	    	ee.setDataList(list).write(response, fileName);
	    }
	    catch (Throwable localThrowable1)
	    {
	      throw localThrowable1;
	    } finally {
	      if (ee != null) if (localThrowable3 != null) 
	    	  try { ee.close(); } 
	      catch (Throwable localThrowable2) {
	    	  localThrowable3.addSuppressed(localThrowable2); } 
	      else ee.close(); 
	    }
	  }
	  
	  @RequestMapping({"importTemplate"})
	  public void importTemplate(HttpServletResponse response)
	  {
		BdPsndoc bdPsndoc = new BdPsndoc();
	   
	    List<BdPsndoc> list = ListUtils.newArrayList(new BdPsndoc[] { bdPsndoc });
	    String fileName = "人员信息数据模板.xlsx";
	    ExcelExport ee = new ExcelExport("人员数据", BdPsndoc.class, Type.IMPORT, new String[0]); 
	    Throwable localThrowable3 = null;
	    try { ee.setDataList(list).write(response, fileName);
	    }
	    catch (Throwable localThrowable1)
	    {
	      localThrowable3 = localThrowable1; throw localThrowable1;
	    } finally {
	      if (ee != null) if (localThrowable3 != null) 
	    	  try { ee.close(); } catch (Throwable localThrowable2) {
	    		  localThrowable3.addSuppressed(localThrowable2); }
	      else ee.close();  
	    }
	  }

	  @ResponseBody
	  @PostMapping({"importData"})
	  public String importData(MultipartFile file, String updateSupport)
	  {
	    try {
	      boolean isUpdateSupport = "1".equals(updateSupport);
	      String message = this.bdPsndocService.importData(file, Boolean.valueOf(isUpdateSupport));
	      return renderResult("true", "posfull:" + message); } 
	    catch (Exception ex) {
	    	
	    	 return renderResult("false", "posfull:" + ex.getMessage());
	    }
	   
	  }
	
}