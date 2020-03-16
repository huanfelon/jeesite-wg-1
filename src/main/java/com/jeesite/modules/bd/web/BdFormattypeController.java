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
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.entity.BdFormattype;
import com.jeesite.modules.bd.service.BdCategoryService;
import com.jeesite.modules.bd.service.BdFormattypeService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.zl.service.ZlFamilyfileService;

/**
 * 业态类型Controller
 * @author GJ
 * @version 2019-10-30
 */
@Controller
@RequestMapping(value = "${adminPath}/bd/bdFormattype")
public class BdFormattypeController extends BaseController {

	@Autowired
	private BdFormattypeService bdFormattypeService;
	@Autowired
	private BdCategoryService bdCategoryService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ZlFamilyfileService zlFamilyfileService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdFormattype get(String pkFormattype, boolean isNewRecord) {
		return bdFormattypeService.get(pkFormattype, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("bd:bdFormattype:view")
	@RequestMapping(value = {"list", ""})
	public String list(BdFormattype bdFormattype, Model model) {
		model.addAttribute("bdFormattype", bdFormattype);
		return "modules/bd/bdFormattypeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdFormattype:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<BdFormattype> listData(BdFormattype bdFormattype) {
		if (StringUtils.isNotBlank(bdFormattype.getCode())){
			bdFormattype.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdFormattype.getName())){
			bdFormattype.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdFormattype.getRemarks())){
			bdFormattype.setParentCode(null);
		}
		//查询顺序错乱，带入组织查询时先查询最上一级，点击展开再查询下级
		if (bdFormattype.getParent()==null) {
			bdFormattype.setParentCode("0");
		}
		bdFormattype.getSqlMap().getWhere().and("pk_org", QueryType.EQ,AbsEnumType.PK_GROUP);
		List<BdFormattype> list = bdFormattypeService.findList(bdFormattype);
		return list;
			
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("bd:bdFormattype:view")
	@RequestMapping(value = "form")
	public String form(BdFormattype bdFormattype, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdFormattype.getPkFormattype())){
			bdFormattype.setCreator(UserUtils.getUser());
			bdFormattype.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdFormattype.getPkFormattype())) {
			bdFormattype.setModifier(UserUtils.getUser());
			bdFormattype.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		//初始化集团值
		Office office=new Office();
		office.setOfficeCode(AbsEnumType.PK_GROUP);
		office=officeService.get(office);
		// 创建并初始化下一个节点信息
		bdFormattype.setPkOrg(office);
		bdFormattype = createNextNode(bdFormattype);
		model.addAttribute("bdFormattype", bdFormattype);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdFormattypeForm";
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("bd:bdFormattype:view")
	@RequestMapping(value = "xgform")
	public String xgform(BdFormattype bdFormattype, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdFormattype.getPkFormattype())){
			bdFormattype.setCreator(UserUtils.getUser());
			bdFormattype.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdFormattype.getPkFormattype())) {
			bdFormattype.setModifier(UserUtils.getUser());
			bdFormattype.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdFormattype = createNextNode(bdFormattype);
		model.addAttribute("bdFormattype", bdFormattype);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdFormattypeXgForm";
	}
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("bd:bdFormattype:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public BdFormattype createNextNode(BdFormattype bdFormattype) {
		if (StringUtils.isNotBlank(bdFormattype.getParentCode())){
			bdFormattype.setParent(bdFormattypeService.get(bdFormattype.getParentCode()));
		}
		if (bdFormattype.getIsNewRecord()) {
			if(StringUtils.isNotBlank(bdFormattype.getParentCode())){
				bdFormattype.setCode(bdFormattype.getParent().getCode());
			}
			BdFormattype where = new BdFormattype();
			where.setParentCode(bdFormattype.getParentCode());
			BdFormattype last = bdFormattypeService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				bdFormattype.setTreeSort(last.getTreeSort() + 30);
			}
		}
		// 以下设置表单默认数据
		if (bdFormattype.getTreeSort() == null){
			bdFormattype.setTreeSort(BdFormattype.DEFAULT_TREE_SORT);
		}
		return bdFormattype;
	}

	/**
	 * 保存业态类型
	 */
	@RequiresPermissions("bd:bdFormattype:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdFormattype bdFormattype) {
		bdFormattypeService.save(bdFormattype);
		return renderResult(Global.TRUE, text("保存业态类型成功！"));
	}
	
	/**
	 * 停用业态类型
	 */
	@RequiresPermissions("bd:bdFormattype:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdFormattype bdFormattype) {
		BdFormattype where = new BdFormattype();
		where.setStatus(BdFormattype.STATUS_NORMAL);
		where.setParentCodes("," + bdFormattype.getId() + ",");
		long count = bdFormattypeService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该业态类型包含未停用的子业态类型！"));
		}
		bdFormattype.setStatus(BdFormattype.STATUS_DISABLE);
		bdFormattypeService.updateStatus(bdFormattype);
		return renderResult(Global.TRUE, text("停用业态类型成功"));
	}
	
	/**
	 * 启用业态类型
	 */
	@RequiresPermissions("bd:bdFormattype:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdFormattype bdFormattype) {
		bdFormattype.setStatus(BdFormattype.STATUS_NORMAL);
		bdFormattypeService.updateStatus(bdFormattype);
		return renderResult(Global.TRUE, text("启用业态类型成功"));
	}
	
	/**
	 * 删除业态类型
	 */
	@RequiresPermissions("bd:bdFormattype:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdFormattype bdFormattype) {
		bdFormattypeService.delete(bdFormattype);
		return renderResult(Global.TRUE, text("删除业态类型成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("bd:bdFormattype:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<BdFormattype> list = bdFormattypeService.findList(new BdFormattype());
		for (int i=0; i<list.size(); i++){
			BdFormattype e = list.get(i);
			// 过滤非正常的数据
			if (!BdFormattype.STATUS_NORMAL.equals(e.getStatus())){
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
	@RequiresPermissions("bd:bdFormattype:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(BdFormattype bdFormattype){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		bdFormattypeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	//修改已被参照的树结构项目
		@RequiresPermissions("bd:bdFormattype:edit")
		@RequestMapping(value = "bianji")
		@ResponseBody
	    public String bianji(String pkFormattype,boolean isNewRecord){
			BdFormattype  bdHttype = bdFormattypeService.get(pkFormattype);
			//判断修改时有没有下级
			BdFormattype bdHttype3= new BdFormattype();
			bdHttype3.setParentCode(bdHttype.getPkFormattype());//判断当前预算类别有无下级
		  //bdCategory3.setParentCode(bdCategory.getParentCode());//判断父节点的子节点即与当前同级别的预算类别
			BdFormattype bdCategory2 = bdFormattypeService.getLastByParentCode(bdHttype3);
			if (bdCategory2!=null) {
				 return "isXj";
			}else{
				//查询户型
				 String sql="select count(*) from zl_familyfile z where z.dr=0 and z.pk_formattypeid='"+bdHttype.getPkFormattype()+"'";
				  Integer count= commonService.selectCount(sql);
				if (count>0) {
					return "isXj";
				}
			}
			 return pkFormattype;
	    }
}