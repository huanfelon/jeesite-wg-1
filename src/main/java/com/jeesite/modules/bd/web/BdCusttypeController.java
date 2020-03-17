/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.web;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.service.BdCustomerService;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.entity.BdCusttype;
import com.jeesite.modules.bd.service.BdCategoryService;
import com.jeesite.modules.bd.service.BdCusttypeService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 客户类型Controller
 * @author GJ
 * @version 2019-10-30
 */
@Controller
@RequestMapping(value = "${adminPath}/bd/bdCusttype")
public class BdCusttypeController extends BaseController {

	@Autowired
	private BdCusttypeService bdCusttypeService;
	@Autowired
	private BdCategoryService bdCategoryService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private BdCustomerService bdCustomerService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdCusttype get(String pkCusttype, boolean isNewRecord) {
		return bdCusttypeService.get(pkCusttype, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("bd:bdCusttype:view")
	@RequestMapping(value = {"list", ""})
	public String list(BdCusttype bdCusttype, Model model) {
		model.addAttribute("bdCusttype", bdCusttype);
		return "modules/bd/bdCusttypeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdCusttype:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<BdCusttype> listData(BdCusttype bdCusttype) {
		if (StringUtils.isNotBlank(bdCusttype.getCode())){
			bdCusttype.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdCusttype.getName())){
			bdCusttype.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdCusttype.getRemarks())){
			bdCusttype.setParentCode(null);
		}
		//查询顺序错乱，带入组织查询时先查询最上一级，点击展开再查询下级
		if (bdCusttype.getParent()==null) {
			bdCusttype.setParentCode("0");
		}
		bdCusttype.getSqlMap().getWhere().and("pk_org", QueryType.EQ,AbsEnumType.PK_GROUP);
		List<BdCusttype> list = bdCusttypeService.findList(bdCusttype);
		return list;
		
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("bd:bdCusttype:view")
	@RequestMapping(value = "form")
	public String form(BdCusttype bdCusttype, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdCusttype.getPkCusttype())){
			bdCusttype.setCreator(UserUtils.getUser());
			bdCusttype.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdCusttype.getPkCusttype())) {
			bdCusttype.setModifier(UserUtils.getUser());
			bdCusttype.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		//初始化集团值
		Office office=new Office();
		office.setOfficeCode(AbsEnumType.PK_GROUP);
		office=officeService.get(office);
		// 创建并初始化下一个节点信息
		bdCusttype.setPkOrg(office);
		bdCusttype = createNextNode(bdCusttype);
		model.addAttribute("bdCusttype", bdCusttype);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdCusttypeForm";
	}
	/**
	 * 查看编辑表单修改
	 */
	@RequiresPermissions("bd:bdCusttype:view")
	@RequestMapping(value = "xgform")
	public String xgform(BdCusttype bdCusttype, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdCusttype.getPkCusttype())){
			bdCusttype.setCreator(UserUtils.getUser());
			bdCusttype.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdCusttype.getPkCusttype())) {
			bdCusttype.setModifier(UserUtils.getUser());
			bdCusttype.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdCusttype = createNextNode(bdCusttype);
		model.addAttribute("bdCusttype", bdCusttype);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdCusttypeXgForm";
	}
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("bd:bdCusttype:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public BdCusttype createNextNode(BdCusttype bdCusttype) {
		if (StringUtils.isNotBlank(bdCusttype.getParentCode())){
			bdCusttype.setParent(bdCusttypeService.get(bdCusttype.getParentCode()));
		}
		if (bdCusttype.getIsNewRecord()) {
			//新增下级默认带出父级编码
			if(StringUtils.isNotBlank(bdCusttype.getParentCode())){
				bdCusttype.setCode(bdCusttype.getParent().getCode());
			}
			BdCusttype where = new BdCusttype();
			where.setParentCode(bdCusttype.getParentCode());
			BdCusttype last = bdCusttypeService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				bdCusttype.setTreeSort(last.getTreeSort() + 30);
			}
		}
		// 以下设置表单默认数据
		if (bdCusttype.getTreeSort() == null){
			bdCusttype.setTreeSort(BdCusttype.DEFAULT_TREE_SORT);
		}
		return bdCusttype;
	}

	/**
	 * 保存客户类型
	 */
	@RequiresPermissions("bd:bdCusttype:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdCusttype bdCusttype) {
		bdCusttypeService.save(bdCusttype);
		return renderResult(Global.TRUE, text("保存客户类型成功！"));
	}
	
	/**
	 * 停用客户类型
	 */
	@RequiresPermissions("bd:bdCusttype:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdCusttype bdCusttype) {
		BdCusttype where = new BdCusttype();
		where.setStatus(BdCusttype.STATUS_NORMAL);
		where.setParentCodes("," + bdCusttype.getId() + ",");
		long count = bdCusttypeService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该客户类型包含未停用的子客户类型！"));
		}
		bdCusttype.setStatus(BdCusttype.STATUS_DISABLE);
		bdCusttypeService.updateStatus(bdCusttype);
		return renderResult(Global.TRUE, text("停用客户类型成功"));
	}
	
	/**
	 * 启用客户类型
	 */
	@RequiresPermissions("bd:bdCusttype:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdCusttype bdCusttype) {
		bdCusttype.setStatus(BdCusttype.STATUS_NORMAL);
		bdCusttypeService.updateStatus(bdCusttype);
		return renderResult(Global.TRUE, text("启用客户类型成功"));
	}
	
	/**
	 * 删除客户类型
	 */
	@RequiresPermissions("bd:bdCusttype:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdCusttype bdCusttype) {
		bdCusttypeService.delete(bdCusttype);
		return renderResult(Global.TRUE, text("删除客户类型成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("bd:bdCusttype:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<BdCusttype> list = bdCusttypeService.findList(new BdCusttype());
		for (int i=0; i<list.size(); i++){
			BdCusttype e = list.get(i);
			// 过滤非正常的数据
			if (!BdCusttype.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getCode(), e.getName()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("bd:bdCusttype:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(BdCusttype bdCusttype){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		bdCusttypeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	//修改已被参照的树结构项目
	@RequiresPermissions("bd:bdCusttype:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkCusttype,boolean isNewRecord){
		BdCusttype  bdHttype = bdCusttypeService.get(pkCusttype);
		//判断修改时有没有下级
		BdCusttype bdHttype3= new BdCusttype();
		bdHttype3.setParentCode(bdHttype.getPkCusttype());//判断当前预算类别有无下级
	  //bdCategory3.setParentCode(bdCategory.getParentCode());//判断父节点的子节点即与当前同级别的预算类别
		BdCusttype bdCategory2 = bdCusttypeService.getLastByParentCode(bdHttype3);
		if (bdCategory2!=null) {
			 return "isXj";
		}else{
			//查询客户信息
			 String sql="select count(*) from bd_customer z where z.status=0 and z.pk_custtype='"+bdHttype.getPkCusttype()+"'";
			  Integer count= commonService.selectCount(sql);
			if (count>0) {
				 return "isXj";
			}
		}
		 return pkCusttype;
    }
}