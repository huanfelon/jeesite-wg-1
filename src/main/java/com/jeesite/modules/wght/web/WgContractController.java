/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.web;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.entity.BdCustomerKpxx;
import com.jeesite.modules.base.service.BdCustomerService;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.base.validator.CodeFactoryUtils;
import com.jeesite.modules.wght.dao.WgContractTaxDao;
import com.jeesite.modules.wght.entity.WgContract;
import com.jeesite.modules.wght.entity.WgContractSrcf;
import com.jeesite.modules.wght.entity.WgContractTax;
import com.jeesite.modules.wght.entity.WgContractYwcf;
import com.jeesite.modules.wght.service.WgContractService;

/**
 * 合同管理Controller
 * @author LY
 * @version 2019-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/wght/wgContract")
public class WgContractController extends BaseController {

	@Autowired
	private WgContractService wgContractService;
	@Autowired
	private BdCustomerService bdCustomerService;
	@Autowired
	private CommonBaseService commonBaseService;
	@Autowired
	private WgContractTaxDao wgContractTaxDao;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WgContract get(String pkContract, boolean isNewRecord) {
		return wgContractService.get(pkContract, isNewRecord);
	}
	
	/**
	 * 查询合同管理列表
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = {"list", ""})
	public String list(WgContract wgContract, Model model) {
		model.addAttribute("wgContract", wgContract);
		return "modules/wght/wgContractList";
	}
	
	/**
	 * 查询合同修订列表
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = {"listXD", ""})
	public String listXD(WgContract wgContractXD, Model model) {
		model.addAttribute("wgContractXD", wgContractXD);
		return "modules/wght/wgContractXDList";
	}
	
	/**
	 * 查询合同管理列表数据
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<WgContract> listData(WgContract wgContract, HttpServletRequest request, HttpServletResponse response) {
		wgContract.setPage(new Page<>(request, response));
		wgContract.getSqlMap().getWhere().and("billtype", QueryType.EQ, AbsEnumType.BillType_HT);
		wgContract.getSqlMap().getWhere().and("version", QueryType.EQ, -1);
		Page<WgContract> page = wgContractService.findPage(wgContract);
		return page;
	}
	
	/**
	 * 查询合同修订列表数据
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "listDataXD")
	@ResponseBody
	public Page<WgContract> listDataXD(WgContract wgContract, HttpServletRequest request, HttpServletResponse response) {
		wgContract.setPage(new Page<>(request, response));
		wgContract.getSqlMap().getWhere().and("billtype", QueryType.EQ, AbsEnumType.BillType_HTXD);
		Page<WgContract> page = wgContractService.findPage(wgContract);
		return page;
	}

	/**
	 * 查看合同管理编辑表单
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "form")
	public String form(WgContract wgContract, Boolean isEdit, Model model) {
		if(wgContract.getIsNewRecord()){
			wgContract.setVbillno(CodeFactoryUtils.createBillCode("HTGL", "wg_contract", "vbillno", "billtype", AbsEnumType.BillType_HT));//单据号自动生成
		}
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("wgContract", wgContract);
		return "modules/wght/wgContractForm";
	}
	
	/**
	 * 查看合同修订编辑表单
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "formXD")
	public String formXD(WgContract wgContract, Boolean isEdit, Model model) {
		if(wgContract.getIsNewRecord()){
			wgContract.setVbillno(CodeFactoryUtils.createBillCode("HTXD", "wg_contract", "vbillno", "billtype", AbsEnumType.BillType_HTXD));//单据号自动生成
		}
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("wgContract", wgContract);
		return "modules/wght/wgContractXDForm";
	}

	/**
	 * 保存合同管理
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated WgContract wgContract) {
		String msg="";
		if(wgContract.getBilltype().equals(AbsEnumType.BillType_HTXD)){//合同修订
			msg=wgContractService.saveDataXD(wgContract);
		}else if(wgContract.getBilltype().equals(AbsEnumType.BillType_HT)){//合同管理
			msg=wgContractService.saveData(wgContract);
		}
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		if(wgContract.getBilltype().equals(AbsEnumType.BillType_HTXD)){
			return renderResult(Global.TRUE, text("保存合同修订成功！"));
		}
		return renderResult(Global.TRUE, text("保存合同管理成功！"));
	}
	
	/**
	 * 删除合同管理
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String delete(@RequestBody List<String> listpk) {
		String msg=wgContractService.deleteData(listpk);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		WgContract entity=wgContractService.get(listpk.get(0));
		if(entity.getBilltype().equals(AbsEnumType.BillType_HTXD)){
			return renderResult(Global.TRUE, text("删除合同修订成功！"));
		}
		return renderResult(Global.TRUE, text("删除合同管理成功！"));
	}
	
	/**
	 * 修改合同管理
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "edit")
	@ResponseBody
	public String edit(WgContract wgContract) {
		WgContract entity=wgContractService.get(wgContract);
		if(!entity.getVbillstatus().equals(AbsEnumType.BillStatus_ZY)){
			return renderResult(Global.FALSE, text("单据状态不是自由态，不可修改！"));
		}
		return "";
	}
	
	/**
	 * 批量审批功能
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "approveAll")
	@ResponseBody
	public String approveAll(@RequestBody List<String> listpk) {
		String msg=wgContractService.approveAllData(listpk);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("审批成功！"));
	}
	
	/**
	 * 批量弃审功能
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "unapproveAll")
	@ResponseBody
	public String unapproveAll(@RequestBody List<String> listpk) {
		String msg=wgContractService.unApproveAllData(listpk);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("取消审批成功！"));
	}
	
	/**
	 * 查询参照列表
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = {"reflist", ""})
	public String reflist(WgContract wgContract, Model model) {
		model.addAttribute("wgContract", wgContract);
		return "modules/refPages/wgContractSelect";
	}
	/**
	 * 查询合同管理参照列表数据
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "reflistData")
	@ResponseBody
	public Page<WgContract> reflistData(WgContract wgContract, HttpServletRequest request, HttpServletResponse response) {
		wgContract.setPage(new Page<>(request, response));
		wgContract.getSqlMap().getWhere().and("billtype", QueryType.EQ, AbsEnumType.BillType_HT);
		wgContract.getSqlMap().getWhere().and("version", QueryType.EQ, -1);
		wgContract.getSqlMap().getWhere().and("htstatus", QueryType.EQ, AbsEnumType.HtStatus_QZ);
		wgContract.getSqlMap().getDataScope().addFilter("refquery", " not exists (select 1 from wg_contract cc1 where nvl(cc1.dr,0)=0 and cc1.vbillstatus<>1 and cc1.billtype='ctxd' and cc1.vsrcid=a.pk_contract)");
		Page<WgContract> page = wgContractService.findPage(wgContract);
		return page;
	}
	/**
	 * 参照选取数据跳转编辑页面
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "refToAdd")
	public String refToAdd(String pk, Model model){
		WgContract wgContract=wgContractService.refDataRender(pk);
		wgContract.setVbillno(CodeFactoryUtils.createBillCode("HTXD", "wg_contract", "vbillno", "billtype", AbsEnumType.BillType_HTXD));//单据号自动生成
		model.addAttribute("wgContract", wgContract);
		model.addAttribute("isEdit", true);
		return "modules/wght/wgContractXDForm";
	}

	/**
	 * 退租查询参照列表
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = {"tzReflist", ""})
	public String tzReflist(WgContract wgContract, Model model) {
		model.addAttribute("wgContract", wgContract);
		return "modules/refPages/tzRefHtSelect";
	}
	/**
	 * 退租参照列表数据
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "tzReflistData")
	@ResponseBody
	public Page<WgContract> tzReflistData(WgContract wgContract, HttpServletRequest request, HttpServletResponse response) {
		wgContract.setPage(new Page<>(request, response));
		wgContract.getSqlMap().getWhere().and("billtype", QueryType.EQ, AbsEnumType.BillType_HT);
		wgContract.getSqlMap().getWhere().and("version", QueryType.EQ, -1);
		wgContract.getSqlMap().getWhere().and("htstatus", QueryType.EQ, AbsEnumType.HtStatus_QZ);
		wgContract.getSqlMap().getWhere().and("vbillstatus", QueryType.EQ, AbsEnumType.BillStatus_SPTG);
		wgContract.getSqlMap().getDataScope().addFilter("refquery", " not exists (select 1 from wg_throwalease cc1 where nvl(cc1.dr,0)=0 and cc1.vbillstatus<>1 and cc1.vsrcid=a.pk_contract)");
		Page<WgContract> page = wgContractService.findPage(wgContract);
		return page;
	}
	
	/**
	 * 获取客户对应开票信息
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "custkp")
	public List<BdCustomerKpxx> custkp(String pkCustomer){
		return bdCustomerService.get(pkCustomer).getBdCustomerKpxxList();
	}
	
	/**
	 * 过滤已经被其他合同选择过的房产
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "filterHouse")
	@ResponseBody
	public String filterHouse(String pkHouse, String pkContract){
		String msg=wgContractService.filterHouse(pkHouse, pkContract);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text(""));
	}
	
	/**
	 * 计划金额弹出框
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "planMny")
	public String planMny(String pk, Model model){
		List<WgContractYwcf> list = wgContractService.getJhmnyDdetail(pk);
		Boolean isEdit=true;
		for (WgContractYwcf ywcf : list) {
			if(ywcf.getVdef1()!=null&&ywcf.getVdef1().equals("Y")){
				isEdit=false;
				break;
			}
		}
		WgContract wgContract=new WgContract();
		wgContract.setWgContractYwcfList(list);
		model.addAttribute("wgContract", wgContract);
		model.addAttribute("isEdit", isEdit);
		return "modules/refPages/wgContractPlanMny";
	}
	/**
	 * 保存计划金额
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@PostMapping(value = "savePlanMny")
	@ResponseBody
	public String savePlanMny(WgContract wgContract) {
		wgContractService.savePlanMny(wgContract);
		return renderResult(Global.TRUE, text("保存计划金额成功！"));
	}
	
	/**
	 * 收入金额弹出框
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "srMny")
	public String srMny(String pk, Model model){
		List<WgContractSrcf> list = wgContractService.getSrmnyDdetail(pk);
		Boolean isEdit=true;
		for (WgContractSrcf cwcf : list) {
			if(cwcf.getVdef1()!=null&&cwcf.getVdef1().equals("Y")){
				isEdit=false;
				break;
			}
		}
		WgContract wgContract=new WgContract();
		wgContract.setWgContractSrcfList(list);
		model.addAttribute("wgContract", wgContract);
		model.addAttribute("isEdit", isEdit);
		return "modules/refPages/wgContractSrMny";
	}
	/**
	 * 保存收入金额
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@PostMapping(value = "saveSrMny")
	@ResponseBody
	public String saveSrMny(WgContract wgContract) {
		wgContractService.saveSrMny(wgContract);
		return renderResult(Global.TRUE, text("保存收入金额成功！"));
	}
	
	/**
	 * 打印弹框
	 */
	@RequestMapping(value = {"print", ""})
	public String print(String pkContract, String templetname, Model model) {
		Map<String, Object> htPrint=wgContractService.getPrintModel(pkContract, templetname);
		model.addAttribute("htPrint", htPrint);
		if(templetname.equals("fwzlhtPrint")){//房屋租赁合同
			List<Map<String, Object>> fkDetail=wgContractService.getHtfkDetail(pkContract);
			model.addAttribute("fkDetail", fkDetail);
		}else if(templetname.equals("wyfwxyPrint")){//物业费合同
			List<Map<String, Object>> fkDetail=wgContractService.getWyfkDetail(pkContract);
			model.addAttribute("fkDetail", fkDetail);
		}
		return "modules/print/"+templetname;
	}
	
	/**
	 * 续约
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "resetHt")
	@ResponseBody
	public String resetHt(WgContract wgContract) {
		WgContract entity=wgContractService.get(wgContract);
		if(!entity.getVbillstatus().equals(AbsEnumType.BillStatus_SPTG)){
			return renderResult(Global.FALSE, text("单据未审批，不可续约！"));
		}
		if(entity.getHtstatus().equals(AbsEnumType.HtStatus_TZ)){
			return renderResult(Global.FALSE, text("合同已经退租，不可续约！"));
		}
		Integer count=commonBaseService.selectCount("select count(*) from wg_contract where version=-1 and nvl(dr,0)=0 and vdef1='"+entity.getPkContract()+"'");
		if(count>0){
			return renderResult(Global.FALSE, text("该合同已经有续约，不可再次续约！"));
		}
		return "";
	}
	/**
	 * 续约编辑表单
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "formXy")
	public String formXy(WgContract wgContract, Model model) {
		wgContract.setVbillno(CodeFactoryUtils.createBillCode("HTGL", "wg_contract", "vbillno", "billtype", AbsEnumType.BillType_HT));//单据号自动生成
		WgContract xycont=wgContractService.getXyCont(wgContract);
		model.addAttribute("wgContract", xycont);
		return "modules/wght/wgContractXyForm";
	}
	
	/**
	 * 获取可变更最大月份
	 */
	@RequestMapping(value = "getMaxTaxChangeDate")
	@ResponseBody
	public Map<String, String> getMaxTaxChangeDate(String pkContract) {
		return wgContractService.getMaxTaxChangeDate(pkContract);
	}
	/**
	 * 保存税率变更
	 */
	@RequiresPermissions("wght:wgContract:edit")
	@RequestMapping(value = "saveTaxChange")
	@ResponseBody
	public String saveTaxChange(String pkContract,Date dstart, Integer taxrate) {
		String msg=wgContractService.saveTaxChange(pkContract, dstart, taxrate);
		if(!msg.equals("")){
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("税率变更成功！"));
	}
	/**
	 * 税率变更记录
	 */
	@RequestMapping(value = {"taxRecord", ""})
	public String taxRecord(String pkContract, Model model) {
		model.addAttribute("pkContract", pkContract);
		return "modules/refpages/wgContractTaxRecord";
	}
	/**
	 * 税率变更记录查询数据
	 */
	@RequiresPermissions("wght:wgContract:view")
	@RequestMapping(value = "taxRecordData")
	@ResponseBody
	public List<WgContractTax> taxRecordData(String pkContract) {
		WgContractTax tax=new WgContractTax();
		tax.setPkContract(pkContract);
		return wgContractTaxDao.findList(tax);
	}
	
	
}