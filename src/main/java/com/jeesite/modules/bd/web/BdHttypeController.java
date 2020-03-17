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
import com.jeesite.modules.bd.entity.BdHttype;
import com.jeesite.modules.bd.service.BdCategoryService;
import com.jeesite.modules.bd.service.BdHttypeService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.wght.service.WgContractService;

/**
 * 合同类型Controller
 * @author GJ
 * @version 2019-10-28
 */
@Controller
@RequestMapping(value = "${adminPath}/bd/bdHttype")
public class BdHttypeController extends BaseController {
	@Autowired
	private WgContractService wgContractService;
	@Autowired
	private BdHttypeService bdHttypeService;
	@Autowired
	private BdCategoryService bdCategoryService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdHttype get(String pkHttype, boolean isNewRecord) {
		return bdHttypeService.get(pkHttype, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("bd:bdHttype:view")
	@RequestMapping(value = {"list", ""})
	public String list(BdHttype bdHttype, Model model) {
		model.addAttribute("bdHttype", bdHttype);
		return "modules/bd/bdHttypeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdHttype:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<BdHttype> listData(BdHttype bdHttype) {
		if (StringUtils.isNotBlank(bdHttype.getCode())){
			bdHttype.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdHttype.getName())){
			bdHttype.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdHttype.getRemarks())){
			bdHttype.setParentCode(null);
		}
		//查询顺序错乱，带入组织查询时先查询最上一级，点击展开再查询下级
		if (bdHttype.getParent()==null) {
			bdHttype.setParentCode("0");
		}
		bdHttype.getSqlMap().getWhere().and("pk_org", QueryType.EQ,AbsEnumType.PK_GROUP);
		List<BdHttype> list = bdHttypeService.findList(bdHttype);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("bd:bdHttype:view")
	@RequestMapping(value = "form")
	public String form(BdHttype bdHttype, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdHttype.getPkHttype())){
			bdHttype.setCreator(UserUtils.getUser());
			bdHttype.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdHttype.getPkHttype())) {
			bdHttype.setModifier(UserUtils.getUser());
			bdHttype.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		//初始化集团值
		Office office=new Office();
		office.setOfficeCode(AbsEnumType.PK_GROUP);
		office=officeService.get(office);
		// 创建并初始化下一个节点信息
		bdHttype.setPkOrg(office);
		bdHttype = createNextNode(bdHttype);
		model.addAttribute("bdHttype", bdHttype);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdHttypeForm";
	}
	/**
	 * 查看编辑表单修改
	 */
	@RequiresPermissions("bd:bdHttype:view")
	@RequestMapping(value = "xgform")
	public String xgform(BdHttype bdHttype, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdHttype.getPkHttype())){
			bdHttype.setCreator(UserUtils.getUser());
			bdHttype.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdHttype.getPkHttype())) {
			bdHttype.setModifier(UserUtils.getUser());
			bdHttype.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdHttype = createNextNode(bdHttype);
		model.addAttribute("bdHttype", bdHttype);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdHttypeXgForm";
	}
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("bd:bdHttype:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public BdHttype createNextNode(BdHttype bdHttype) {
		if (StringUtils.isNotBlank(bdHttype.getParentCode())){
			bdHttype.setParent(bdHttypeService.get(bdHttype.getParentCode()));
		}
		if (bdHttype.getIsNewRecord()) {
			//新增下级默认带出父级编码
			if(StringUtils.isNotBlank(bdHttype.getParentCode())){
				bdHttype.setCode(bdHttype.getParent().getCode());
			}
			BdHttype where = new BdHttype();
			where.setParentCode(bdHttype.getParentCode());
			BdHttype last = bdHttypeService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				bdHttype.setTreeSort(last.getTreeSort() + 30);
			}
		}
		// 以下设置表单默认数据
		if (bdHttype.getTreeSort() == null){
			bdHttype.setTreeSort(BdHttype.DEFAULT_TREE_SORT);
		}
		return bdHttype;
	}

	/**
	 * 保存合同类型
	 */
	@RequiresPermissions("bd:bdHttype:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdHttype bdHttype) {
		bdHttypeService.save(bdHttype);
		return renderResult(Global.TRUE, text("保存合同类型成功！"));
	}
	
	/**
	 * 停用合同类型
	 */
	@RequiresPermissions("bd:bdHttype:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdHttype bdHttype) {
		BdHttype where = new BdHttype();
		where.setStatus(BdHttype.STATUS_NORMAL);
		where.setParentCodes("," + bdHttype.getId() + ",");
		long count = bdHttypeService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该合同类型包含未停用的子合同类型！"));
		}
		bdHttype.setStatus(BdHttype.STATUS_DISABLE);
		bdHttypeService.updateStatus(bdHttype);
		return renderResult(Global.TRUE, text("停用合同类型成功"));
	}
	
	/**
	 * 启用合同类型
	 */
	@RequiresPermissions("bd:bdHttype:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdHttype bdHttype) {
		bdHttype.setStatus(BdHttype.STATUS_NORMAL);
		bdHttypeService.updateStatus(bdHttype);
		return renderResult(Global.TRUE, text("启用合同类型成功"));
	}
	
	/**
	 * 删除合同类型
	 */
	@RequiresPermissions("bd:bdHttype:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdHttype bdHttype) {
		bdHttypeService.delete(bdHttype);
		return renderResult(Global.TRUE, text("删除合同类型成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("bd:bdHttype:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<BdHttype> list = bdHttypeService.findList(new BdHttype());
		for (int i=0; i<list.size(); i++){
			BdHttype e = list.get(i);
			// 过滤非正常的数据
			if (!BdHttype.STATUS_NORMAL.equals(e.getStatus())){
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
	@RequiresPermissions("bd:bdHttype:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(BdHttype bdHttype){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		bdHttypeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	//修改已被参照的树结构项目
	@RequiresPermissions("bd:bdHttype:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkHttype,boolean isNewRecord){
		BdHttype  bdHttype = bdHttypeService.get(pkHttype);
		//判断修改时有没有下级
		BdHttype bdHttype3= new BdHttype();
		bdHttype3.setParentCode(bdHttype.getPkHttype());//判断当前预算类别有无下级
	  //bdCategory3.setParentCode(bdCategory.getParentCode());//判断父节点的子节点即与当前同级别的预算类别
		BdHttype bdCategory2 = bdHttypeService.getLastByParentCode(bdHttype3);
		if (bdCategory2!=null) {
			 return "isXj";
		}else{
			//查询合同管理
			 String sql="select count(*) from wg_contract z where z.dr=0 and z.httype='"+bdHttype.getPkHttype()+"'";
			  Integer count= commonService.selectCount(sql);
			if (count>0) {
				 return "isXj";
			}
		}
		 return pkHttype;
    }
}