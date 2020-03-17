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
import com.jeesite.modules.bd.entity.BdCategory;
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.bd.service.BdCategoryService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * bd_categoryController
 * @author GJ
 * @version 2019-10-16
 */
@Controller
@RequestMapping(value = "${adminPath}/bd/bdCategory")
public class BdCategoryController extends BaseController {

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
	public BdCategory get(String pkCategory, boolean isNewRecord) {
		return bdCategoryService.get(pkCategory, isNewRecord);
	}
	
	/**
	 * 查询列表组织级
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = {"list", ""})
	public String list(BdCategory bdCategory, Model model) {
		model.addAttribute("bdCategory", bdCategory);
		return "modules/bd/bdCategoryList";
	}
	/**
	 * 查询列表集团级
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = {"list1", ""})
	public String list1(BdCategory bdCategory, Model model) {
		model.addAttribute("bdCategory", bdCategory);
		return "modules/bd/bdCategoryList1";
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<BdCategory> listData(BdCategory bdCategory) {
		if (StringUtils.isNotBlank(bdCategory.getCode())){
			bdCategory.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdCategory.getName())){
			bdCategory.setParentCode(null);
		}
		String [] pks=null; //IN方法只接受Object[]和list集合
		//查询条件组织不为空则拼上集团,为空则只展示集团
		if(bdCategory.getPkOrg()!=null&&!StringUtils.isEmpty(bdCategory.getPkOrg().getOfficeCode())){
			String newpks=bdCategory.getPkOrg().getOfficeCode()+","+AbsEnumType.PK_GROUP;
			bdCategory.getPkOrg().setOfficeCode(newpks);
			//查询顺序错乱，带入组织查询时先查询最上一级，点击展开再查询下级
			if (bdCategory.getParent()==null) {
				bdCategory.setParentCode("0");
			}
		}else{
			bdCategory.getPkOrg().setOfficeCode(AbsEnumType.PK_GROUP);
		}
		pks=bdCategory.getPkOrg().getOfficeCode().split(",");
		bdCategory.getSqlMap().getWhere().and("pk_org",QueryType.IN,pks);
		List<BdCategory> list = bdCategoryService.findList(bdCategory);
		return list;
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = "listData1")
	@ResponseBody
	public List<BdCategory> listData1(BdCategory bdCategory) {
		if (StringUtils.isBlank(bdCategory.getParentCode())) {
			bdCategory.setParentCode(BdCategory.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(bdCategory.getCode())){
			bdCategory.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdCategory.getName())){
			bdCategory.setParentCode(null);
		}
		//查询顺序错乱，带入组织查询时先查询最上一级，点击展开再查询下级
		if (bdCategory.getParent()==null) {
			bdCategory.setParentCode("0");
		}
		bdCategory.getSqlMap().getWhere().and("pk_org", QueryType.EQ,AbsEnumType.PK_GROUP);
		List<BdCategory> list = bdCategoryService.findList(bdCategory);
		return list;
	
	}
	/**
	 * 查看编辑表单组织(有下级不可以修改编码)
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = "form")
	public String form(BdCategory bdCategory, Model model,String isEdit) {
		//新增
		if(isEdit.equals("true")&& StringUtils.isBlank(bdCategory.getPkCategory())){
			if(bdCategory.getParent()!=null&&StringUtils.isNotBlank(bdCategory.getParent().getPkCategory())){
				BdCategory zlProject2 =bdCategoryService.get(bdCategory.getParent());
					if(!zlProject2.getPkOrg().getOfficeCode().equals(AbsEnumType.PK_GROUP)){
						bdCategory.setPkOrg(zlProject2.getPkOrg());
				}
			}
			bdCategory.setCreator(UserUtils.getUser());
			bdCategory.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdCategory.getPkCategory())) {
			bdCategory.setModifier(UserUtils.getUser());
			bdCategory.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdCategory = createNextNode(bdCategory);
		model.addAttribute("bdCategory", bdCategory);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdCategoryForm";
	}
	/**
	 * 查看编辑表单组织(没有下级可以修改编码)
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = "xgform")
	public String xgform(BdCategory bdCategory, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdCategory.getPkCategory())){
			bdCategory.setCreator(UserUtils.getUser());
			bdCategory.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdCategory.getPkCategory())) {
			bdCategory.setModifier(UserUtils.getUser());
			bdCategory.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdCategory = createNextNode(bdCategory);
		model.addAttribute("bdCategory", bdCategory);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdCategoryXgForm";
	}
	/**
	 * 查看编辑表单集团(有下级不可以修改编码)
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = "form1")
	public String form1(BdCategory bdCategory, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdCategory.getPkCategory())){
			bdCategory.setCreator(UserUtils.getUser());
			bdCategory.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdCategory.getPkCategory())) {
			bdCategory.setModifier(UserUtils.getUser());
			bdCategory.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		//初始化集团值
		Office office=new Office();
		office.setOfficeCode(AbsEnumType.PK_GROUP);
		office=officeService.get(office);
		// 创建并初始化下一个节点信息
		bdCategory.setPkOrg(office);
		bdCategory = createNextNode(bdCategory);
		model.addAttribute("bdCategory", bdCategory);
		model.addAttribute("isEdit", isEdit);
		
		return "modules/bd/bdCategoryForm1";
	}
	
	/**
	 * 查看编辑表单集团(没有下级可以修改编码)
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = "xgform1")
	public String xgform1(BdCategory bdCategory, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdCategory.getPkCategory())){
			if(bdCategory.getParent()!=null&&StringUtils.isNotBlank(bdCategory.getParent().getPkCategory())){
				BdCategory zlProject2 =   bdCategoryService.get(bdCategory.getParent());
				bdCategory.setPkOrg(zlProject2.getPkOrg());
			}
			bdCategory.setCreator(UserUtils.getUser());
			bdCategory.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdCategory.getPkCategory())) {
			bdCategory.setModifier(UserUtils.getUser());
			bdCategory.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdCategory = createNextNode(bdCategory);
		model.addAttribute("bdCategory", bdCategory);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdCategoryXgForm1";
	}
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("bd:bdCategory:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public BdCategory createNextNode(BdCategory bdCategory) {
		if (StringUtils.isNotBlank(bdCategory.getParentCode())){
			bdCategory.setParent(bdCategoryService.get(bdCategory.getParentCode()));
		}
		if (bdCategory.getIsNewRecord()) {
			//新增下级默认带出父级编码
			if(StringUtils.isNotBlank(bdCategory.getParentCode())){
				bdCategory.setCode(bdCategory.getParent().getCode());
			}
			BdCategory where = new BdCategory();
			where.setParentCode(bdCategory.getParentCode());
			BdCategory last = bdCategoryService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				bdCategory.setTreeSort(last.getTreeSort() + 30);
			//	bdCategory.setCode(IdGen.nextCode(last.getCode()));
			}/*else if (bdCategory.getParent() != null){
				bdCategory.setCode(bdCategory.getParent().getCode() + "01");
			}*/
		}
		// 以下设置表单默认数据
		if (bdCategory.getTreeSort() == null){
			bdCategory.setTreeSort(BdCategory.DEFAULT_TREE_SORT);
		}
		return bdCategory;
	}

	/**
	 * 保存预算类别
	 */
	@RequiresPermissions("bd:bdCategory:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdCategory bdCategory) {
		bdCategoryService.save(bdCategory);
		return renderResult(Global.TRUE, text("保存预算类别成功！"));
	}

	/**
	 * 停用预算类别
	 */
	@RequiresPermissions("bd:bdCategory:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdCategory bdCategory) {
		BdCategory where = new BdCategory();
		where.setStatus(BdProject.STATUS_NORMAL);
		where.setParentCodes("," + bdCategory.getId() + ",");
		long count = bdCategoryService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该预算类别包含未停用的子预算类别！"));
		}
		bdCategory.setStatus(BdProject.STATUS_DISABLE);
		bdCategoryService.updateStatus(bdCategory);
		return renderResult(Global.TRUE, text("停用预算类别成功"));
	}
	
	/**
	 * 启用预算类别
	 */
	@RequiresPermissions("bd:bdCategory:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdCategory bdCategory) {
		bdCategory.setStatus(BdCategory.STATUS_NORMAL);
		bdCategoryService.updateStatus(bdCategory);
		return renderResult(Global.TRUE, text("启用预算类别成功"));
	}
	
	/**
	 * 删除预算类别
	 */
	@RequiresPermissions("bd:bdCategory:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdCategory bdCategory) {
		bdCategoryService.delete(bdCategory);
		return renderResult(Global.TRUE, text("删除预算类别成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("bd:bdCategory:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<BdCategory> list = bdCategoryService.findList(new BdCategory());
		for (int i=0; i<list.size(); i++){
			BdCategory e = list.get(i);
			// 过滤非正常的数据
			if (!BdCategory.STATUS_NORMAL.equals(e.getStatus())){
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
	@RequiresPermissions("bd:bdCategory:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(BdCategory bdCategory){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		bdCategoryService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	//修改已被参照的树结构项目
	@RequiresPermissions("bd:bdCategory:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkCategory,boolean isNewRecord){
		BdCategory  bdCategory = bdCategoryService.get(pkCategory);
		//判断修改时有没有下级
		BdCategory bdCategory3= new BdCategory();
		bdCategory3.setParentCode(bdCategory.getPkCategory());//判断当前预算类别有无下级
	  //bdCategory3.setParentCode(bdCategory.getParentCode());//判断父节点的子节点即与当前同级别的预算类别
		BdCategory bdCategory2 = bdCategoryService.getLastByParentCode(bdCategory3);
		if (bdCategory2!=null) {
			 return "isXj";
		}else{
         /*    List<String> list = ctContractService.listCtContracts();
             for(String string :list){
            	 CtContract contract = ctContractService.get(string);
            	 if (contract.getPkHttype().getPkHttype().equals(pkHttype)) {
					 return renderResult(Global.FALSE, "当前合同类型已被'"+contract.getHtname()+"'引用，不可修改!请检查");

				}
             }*/
			
		}
		 return pkCategory;
    }
	
	//在组织节点中 集团下新增组织级判断
	@RequiresPermissions("bd:bdCategory:edit")
	@RequestMapping(value = "xinzen")
	@ResponseBody
    public String xinzen(String pk,boolean isNewRecord,String parentCode){
		//顶部新增下级按钮，判断选中的是集团还是组织 ，是集团则校验集团下是否有集团级档案，不是集团则直接新增下级
		if(StringUtils.isNotBlank(pk)&&StringUtils.isBlank(parentCode)){
			//在组织节点新增
			String sql="select count(*) from bd_category c where nvl(c.status,0)=0 and c.pk_org='"+AbsEnumType.PK_GROUP+"'and exists "
					+ "(select 1 from bd_category b where nvl(b.status,0)=0 and b.pk_category=c.parent_code and b.pk_category='"+pk+"')";
		  Integer count=commonService.selectCount(sql);
		  if (count>0) {
			   return renderResult(Global.FALSE, text("下级存在集团级项目,不可以新增组织级!"));
		    }
		} else if (StringUtils.isBlank(pk)&&StringUtils.isNotBlank(parentCode)) {
			return "isXz";
		}
		 return "isXz";
    }
}