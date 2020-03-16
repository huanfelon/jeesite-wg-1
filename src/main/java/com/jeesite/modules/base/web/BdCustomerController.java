/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.web;

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
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.base.entity.BdCustomerProj;
import com.jeesite.modules.base.service.BdCustomerService;
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.base.validator.CodeValidatorUtils;
import com.jeesite.modules.wght.service.WgContractService;

/**
 * 客户信息中心Controller
 * @author tcl
 * @version 2019-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/base/bdCustomer")
public class BdCustomerController extends BaseController {

	@Autowired
	private BdCustomerService bdCustomerService;
	
	@Autowired
	private WgContractService contractService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdCustomer get(String pkCustomer, boolean isNewRecord) {
		return bdCustomerService.get(pkCustomer, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("base:bdCustomer:view")
	@RequestMapping(value = {"list", ""})
	public String list(BdCustomer bdCustomer, Model model) {
		model.addAttribute("bdCustomer", bdCustomer);
		return "modules/base/bdCustomerList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("base:bdCustomer:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BdCustomer> listData(BdCustomer bdCustomer,String pk_project, HttpServletRequest request, HttpServletResponse response) {
		bdCustomer.setPage(new Page<>(request, response));
		//根据项目查询客户
		if(!StringUtils.isEmpty(pk_project)){
			String[] pks=pk_project.split(",");
			String str="";
			for(String pk:pks){
				str+="'"+pk+"',";
			}
			str=str.substring(0, str.lastIndexOf(","));
			
			bdCustomer.getSqlMap().getDataScope().addFilter("dsf", " exists(select 1 from bd_customer_proj p "
					+ "where p.pk_customer=a.pk_customer and p.pk_project in("+str+"))");
		} 
		Page<BdCustomer> page = bdCustomerService.findPage(bdCustomer);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("base:bdCustomer:view")
	@RequestMapping(value = "form")
	public String form(BdCustomer bdCustomer,String noEdit, Model model) {
		boolean edit=Boolean.valueOf(noEdit);
		model.addAttribute("noEdit",edit);
		model.addAttribute("bdCustomer", bdCustomer);
		return "modules/base/bdCustomerForm";
	}

	/**
	 * 保存客户信息中心
	 */
	@RequiresPermissions("base:bdCustomer:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdCustomer bdCustomer) {
		//校验编码重复
		CodeValidatorUtils.validatorCode(new BdCustomer(), bdCustomer,"code",bdCustomer.getCode(), bdCustomerService);
		if(StringUtils.isEmpty(bdCustomer.getCuststatus())){
			bdCustomer.setCuststatus(AbsEnumType.CUST_MORMAL);
		}
		bdCustomerService.save(bdCustomer);
		return renderResult(Global.TRUE, text("保存客户信息中心成功！"));
	}
	
	/**
	 * 删除客户信息中心
	 */
	@RequiresPermissions("base:bdCustomer:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdCustomer bdCustomer) {
		bdCustomerService.delete(bdCustomer);
		return renderResult(Global.TRUE, text("删除客户信息中心成功！"));
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String deleteAllData(@RequestBody List<String> list) {
		for(String pk:list){
			//从列表转换的数据，需要重新获取
			BdCustomer bd=bdCustomerService.get(pk);
			//校验表体项目是否被引用
			List<BdCustomerProj> listchild=bd.getBdCustomerProjList();
			for(BdCustomerProj c:listchild){
				String pk_proj=c.getPkProject().getPkProject();
				String code=c.getPkProject().getCode();
				Integer it=bdCustomerService.getXmIsRefByProjectid(pk_proj);
				if(it>0){
					String custcode=bd.getCode();
					return renderResult(Global.FALSE, "客户编号"+custcode+"的表体项目"+code+"已被业务引用，不允许删除！");
				}
			}
			//检查数据是否引用-==预留
			
			bdCustomerService.delete(bd);
		}
		return renderResult(Global.TRUE, text("批量删除成功！"));
	}
	
	/**
	 * 参照列表数据查询
	 */
	@RequestMapping({"bdCustomerSelect"})
	public String refListSelect(BdCustomer bdCustomer,String pk_project, String selectData, Model model)
	{
		model.addAttribute("bdCustomer", bdCustomer);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson);
		}
		model.addAttribute("pkProject", pk_project);
		return "modules/refPages/bdCustomerSelect";
	}
	
	/**
	 * 根据组织查询最大单据号编码
	 */
	@RequestMapping(value = "getMaxBillno")
	@ResponseBody
	public String getMaxBillno(@RequestBody String orgCode){
		
		String str="";
		if(StringUtils.isEmpty(orgCode)){
			return str;
		}
		str=CodeFactoryUtils.createBillCode("ZL"+orgCode, "bd_customer", "code", 6);
		return renderResult(Global.TRUE, str);
	}
	
	/**
	 * 查询项目是否被业务引用
	 */
	@RequestMapping(value = "getXmIsRef")
	@ResponseBody
	public String getXmIsRef(@RequestBody String projectid){
		
		//查询项目是否被合同引用
		Integer count=bdCustomerService.getXmIsRefByProjectid(projectid);
		if(count>0){
			return renderResult(Global.FALSE,"项目已被参照，请重新选择！");
		}
		return renderResult(Global.TRUE, "未被参照！");
	}
	
	/**
	 * 查询开票信息
	 * @return
	 */
	@RequestMapping({"selectKpxxByCustomer"})
	@ResponseBody
	public BdCustomer selectKpxxByCustomer(String pkCustomer){
	    return bdCustomerService.get(pkCustomer);
	 }
	
	/**
	 * 批该业务员
	 */
	@RequestMapping(value = "batchPGPsndoc")
	@ResponseBody
	public String batchPGPsndoc(String[] pks,String pk_psndoc) {
		
		//批改业务员逻辑
		for(String pk : pks){
			BdCustomer customer=bdCustomerService.get(pk);
			customer.getPkPsndoc().setId(pk_psndoc);
			bdCustomerService.update(customer);
		}
		//预留更新业务单据的业务员处理操作
		
		//批改业务逻辑
		return renderResult(Global.TRUE, text("批量更改成功！"));
	}
	
	/**
	 * 导出数据
	 * @param empUser
	 * @param isAll
	 * @param ctrlPermi
	 * @param response
	 */
	  @RequestMapping({"exportData"})
	  public void exportData(BdCustomer bdCustomer, Boolean isAll, String ctrlPermi, HttpServletResponse response)
	  {
		/* if ((isAll == null) || (!isAll.booleanValue())) {
	      this.bdCarService.addDataScopeFilter(bdCar, ctrlPermi);
	    }*/
	    List<BdCustomer> list = this.bdCustomerService.findList(bdCustomer);
	    String fileName = "客户数据" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
	    ExcelExport ee = new ExcelExport("客户数据", BdCustomer.class);
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
		BdCustomer bdCustomer = new BdCustomer();
	   
	    List<BdCustomer> list = ListUtils.newArrayList(new BdCustomer[] { bdCustomer });
	    String fileName = "客户信息数据模板.xlsx";
	    ExcelExport ee = new ExcelExport("客户基本信息", BdCustomer.class, Type.IMPORT, new String[0]); 
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
	      String message = this.bdCustomerService.importData(file, Boolean.valueOf(isUpdateSupport));
	      return renderResult("true", "posfull:" + message); } 
	    catch (Exception ex) {
	    	
	    	 return renderResult("false", "posfull:" + ex.getMessage());
	    }
	   
	  }
}