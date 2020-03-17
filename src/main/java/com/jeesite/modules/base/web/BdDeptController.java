/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.web;

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
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.base.service.BdDeptService;
import com.jeesite.modules.base.validator.CodeValidatorUtils;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;


/**
 * bd_deptController
 * @author gj
 * @version 2019-04-24
 */
@Controller
@RequestMapping(value = "${adminPath}/base/bdDept")
public class BdDeptController extends BaseController {

	@Autowired
	private BdDeptService bdDeptService;
	@Autowired
	private OfficeService officeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdDept get(String pkDept, boolean isNewRecord) {
		return bdDeptService.get(pkDept, isNewRecord);
	}
	
	 @RequiresPermissions({"base:bdDept:view"})
	  @RequestMapping({"index"})
	   public String index(BdDept bdDept, Model model) {
	   return "modules/base/bdDeptIndex";
	  }
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(BdDept bdDept, Model model) {
		model.addAttribute("bdDept", bdDept);
		return "modules/base/bdDeptList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<BdDept> listData(BdDept bdDept) {
		if (StringUtils.isBlank(bdDept.getParentCode())) {
			bdDept.setParentCode("0");
		}
	
		if((StringUtils.isNotBlank(bdDept.getDeptCode()))||
		  (StringUtils.isNotBlank(bdDept.getDeptName()))){
			bdDept.setParentCode(null);
		}
		bdDeptService.addDataScopeFilter(bdDept);
		List<BdDept> list = bdDeptService.findList(bdDept);
	
		for(int i=0;i<list.size();i++){
			BdDept d=list.get(i);
			if(d.getDeptAdmin()!=null){
				bdDeptService.selectNameByCode(d);
			}
			
		}
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("base:bdDept:view")
	@RequestMapping(value = "form")
	public String form(BdDept bdDept, Model model,String pkOffice) {
		// 创建并初始化下一个节点信息
		bdDept = createNextNode(bdDept);
		if(bdDept.getDeptAdmin()!=null){
			bdDeptService.selectNameByCode(bdDept);
		}
		
		//根据左边选择的组织自动带出
		if(pkOffice!=null&&!StringUtils.isEmpty(pkOffice)){
			Office office=officeService.get(new Office(pkOffice));
			bdDept.setPkOrg(office);
		}
		model.addAttribute("bdDept", bdDept);
		return "modules/base/bdDeptForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("base:bdDept:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public BdDept createNextNode(BdDept bdDept) {
		if (StringUtils.isNotBlank(bdDept.getParentCode())){
			bdDept.setParent(bdDeptService.get(bdDept.getParentCode()));
		}
		if (bdDept.getIsNewRecord()) {
			BdDept where = new BdDept();
			where.setParentCode(bdDept.getParentCode());
			BdDept last = bdDeptService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				bdDept.setTreeSort(last.getTreeSort() + 30);
				bdDept.setDeptCode(IdGen.nextCode(last.getDeptCode()));
			}else if (bdDept.getParent() != null){
				bdDept.setDeptCode(bdDept.getParent().getDeptCode() + "001");
			}
			
		}
		// 以下设置表单默认数据
		if (bdDept.getTreeSort() == null){
			bdDept.setTreeSort(BdDept.DEFAULT_TREE_SORT);
		}
		return bdDept;
	}

	/**
	 * 保存bd_dept
	 */
	@RequiresPermissions("base:bdDept:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdDept bdDept) {
		//校验编码重复
		CodeValidatorUtils.validatorCode(new BdDept(), bdDept,"dept_code",bdDept.getDeptCode(), bdDeptService);
		bdDeptService.save(bdDept);
		return renderResult(Global.TRUE, text("保存数据成功！"));
	}
	/**
	 * 停用部门
	 */
	@RequiresPermissions("base:bdDept:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdDept bdDept) {
		BdDept where = new BdDept();
		where.setStatus(BdDept.STATUS_NORMAL);
		where.setParentCodes("," + bdDept.getId() + ",");
		long count = bdDeptService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该部门包含未停用的子部门！"));
		}
		bdDept.setStatus(BdDept.STATUS_DISABLE);
		bdDeptService.updateStatus(bdDept);
		return renderResult(Global.TRUE, text("停用部门成功"));
	}
	
	/**
	 * 启用部门
	 */
	@RequiresPermissions("base:bdDept:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdDept bdDept) {
		bdDept.setStatus(BdDept.STATUS_NORMAL);
		bdDeptService.updateStatus(bdDept);
		return renderResult(Global.TRUE, text("启用部门成功"));
	}
	
	/**
	 * 删除bd_dept
	 */
	@RequiresPermissions("base:bdDept:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdDept bdDept) {
		bdDeptService.delete(bdDept);
		return renderResult(Global.TRUE, text("删除数据成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode,BdDept bdDept) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		
		List<BdDept> list = bdDeptService.findList(bdDept);
		//默认查询所有部门  并返回list集合
		//List<BdDept> list = bdDeptService.findList(new BdDept());
		
		for (int i=0; i<list.size(); i++){
			BdDept e = list.get(i);
			// 过滤非正常的数据
			/*if (!BdDept.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}*/
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
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getDeptCode(), e.getDeptName()));
			mapList.add(map);
		}
		//bdDeptService.addDataScopeFilter(bdDept);
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("base:bdDept:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(BdDept bdDept){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		bdDeptService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	/**
	 * 查询组织
	 */
	@RequestMapping(value = "getOrg")
	@ResponseBody
	public List<Map<String,String>> getOrg(String pk_url) {
		List<Map<String,String>>  dept= bdDeptService.selectOrgBycode(pk_url);
		return dept;
	}
	/**
	 * 查询岗位
	 */
	@RequiresPermissions("base:bdDept:view")
	@RequestMapping(value = "getPost")
	@ResponseBody
	public List<Map<String,String>> getPost(String bdept) {
    
        
		List<Map<String,String>>  post= bdDeptService.selectPostBycode(bdept);
		
		return post;
	}


}