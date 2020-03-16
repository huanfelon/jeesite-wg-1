/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.web;

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
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.zl.entity.ZlProject;
import com.jeesite.modules.zl.service.ZlBuildingfileService;
import com.jeesite.modules.zl.service.ZlFamilyfileService;
import com.jeesite.modules.zl.service.ZlHousesourceService;
import com.jeesite.modules.zl.service.ZlProjectService;

/**
 * zl_projectController
 * @author GuoJ
 * @version 2019-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/zl/zlProject")
public class ZlProjectController extends BaseController {

	@Autowired
	private ZlProjectService zlProjectService;
	@Autowired
	private ZlFamilyfileService zlFamilyfileService;
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
	public ZlProject get(String pkProject, boolean isNewRecord) {
		return zlProjectService.get(pkProject, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("zl:zlProject:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZlProject zlProject, Model model) {
		model.addAttribute("zlProject", zlProject);
		return "modules/zl/zlProjectList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("zl:zlProject:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<ZlProject> listData(ZlProject zlProject) {
		if (StringUtils.isBlank(zlProject.getParentCode())) {
			zlProject.setParentCode(ZlProject.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(zlProject.getCode())){
			zlProject.setParentCode(null);
		}
		if (StringUtils.isNotBlank(zlProject.getName())){
			zlProject.setParentCode(null);
		}
	/*	 if(zlProject.getDbilldate_lte()!=null){
	        	Date dt=zlProject.getDbilldate_lte();
	    		SimpleDateFormat djrqsdf = new SimpleDateFormat("yyyy-MM-dd");
	    		String djrqdateFormat = djrqsdf.format(dt);
	    	    String str=djrqdateFormat +" 23:59:59";
	    	    str=str.replaceAll("-", "/");
	    	    Date dt2=new Date(str);
	    	    zlProject.setDbilldate_lte(dt2);
	        }*/
		List<ZlProject> list = zlProjectService.findList(zlProject);
		return list;
	}
	
	/**
	 * 查询列表数据ByPk
	 */
	@RequiresPermissions("zl:zlProject:view")
	@RequestMapping(value = "dataByPk")
	@ResponseBody
	public ZlProject getDataByPk(String pkProject) {
		return this.get(pkProject,false);
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlProject:view")
	@RequestMapping(value = "form")
	public String form(ZlProject zlProject, String isEdit,Model model) {
		if(isEdit.equals("true")&& StringUtils.isBlank(zlProject.getPkProject())){
			if(zlProject.getParent()!=null&&StringUtils.isNotBlank(zlProject.getParent().getPkProject())){
				ZlProject zlProject2 =   zlProjectService.get(zlProject.getParent());
				zlProject.setPkOrg(zlProject2.getPkOrg());
			}
			zlProject.setCreator(UserUtils.getUser());
			zlProject.setDbilldate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(zlProject.getPkProject())) {
			zlProject.setModifier(UserUtils.getUser());
			zlProject.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		zlProject = createNextNode(zlProject);
		model.addAttribute("zlProject", zlProject);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlProjectForm";
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlProject:view")
	@RequestMapping(value = "xgform")
	public String xgform(ZlProject zlProject, String isEdit,Model model) {
		if(isEdit.equals("true")&& StringUtils.isBlank(zlProject.getPkProject())){
			zlProject.setCreator(UserUtils.getUser());
			zlProject.setDbilldate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(zlProject.getPkProject())) {
			zlProject.setModifier(UserUtils.getUser());
			zlProject.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		zlProject = createNextNode(zlProject);
		model.addAttribute("zlProject", zlProject);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlProjectXgForm";
	}
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("zl:zlProject:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public ZlProject createNextNode(ZlProject zlProject) {
		if (StringUtils.isNotBlank(zlProject.getParentCode())){
			zlProject.setParent(zlProjectService.get(zlProject.getParentCode()));
		}
		if (zlProject.getIsNewRecord()&&StringUtils.isNotBlank(zlProject.getParentCode())) {
			zlProject.setCode(zlProject.getParent().getCode());
			ZlProject where = new ZlProject();
			where.setParentCode(zlProject.getParentCode());
			ZlProject last = zlProjectService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				zlProject.setTreeSort(last.getTreeSort() + 30);
			}
		}else{
			ZlProject where = new ZlProject();
			where.setParentCode(zlProject.getParentCode());
			ZlProject last = zlProjectService.getLastByParentCode(where);
			if (last != null){
				zlProject.setTreeSort(last.getTreeSort() + 30);
			}
			
		}
		// 以下设置表单默认数据
		if (zlProject.getTreeSort() == null){
			zlProject.setTreeSort(ZlProject.DEFAULT_TREE_SORT);
		}
		return zlProject;
	}

	/**
	 * 保存项目信息
	 */
	@RequiresPermissions("zl:zlProject:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZlProject zlProject) {
		zlProjectService.save(zlProject);
		return renderResult(Global.TRUE, text("保存项目信息成功！"));
	}
	
	/**
	 * 删除项目信息
	 */
	@RequiresPermissions("zl:zlProject:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZlProject zlProject) {
		zlProjectService.delete(zlProject);
		return renderResult(Global.TRUE, text("删除项目信息成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("zl:zlProject:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(ZlProject zlProject, String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<ZlProject> list = zlProjectService.findList(zlProject);
		for (int i=0; i<list.size(); i++){
			ZlProject e = list.get(i);
			// 过滤非正常的数据
			if (!ZlProject.STATUS_NORMAL.equals(e.getStatus())){
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
			map.put("code", e.getCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getCode(), e.getName()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("zl:zlProject:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(ZlProject zlProject){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		zlProjectService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	//修改已被参照的树结构项目
	@RequiresPermissions("zl:zlProject:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkProject,boolean isNewRecord){
		ZlProject zl=new ZlProject();
		zl.setParentCode(pkProject);
		ZlProject zlCostproject2 = zlProjectService.getLastByParentCode(zl);
		if (zlCostproject2 !=null) {
			 return "isXj";
		}else{
		     //查询户型
			 String sql="select count(*) from zl_familyfile z where z.dr=0 and z.pk_projectid='"+pkProject+"'";
			 Integer count= commonService.selectCount(sql);
			 if (count>0) {
				 return "isXj";			
				 }
			   //查询楼栋
			 String sql1="select count(*) from zl_buildingfile z where z.dr=0 and z.pk_projectid='"+pkProject+"'";
			 Integer count1= commonService.selectCount(sql1);
			   if(count1>0) {
				 return "isXj";
			   }
			   //查询客户信息表体
				 String sql2="select count(*) from bd_customer_proj z where z.pk_project='"+pkProject+"'";
				  Integer count2= commonService.selectCount(sql2);
				 if (count2>0) {
					 return "isXj";
				 }
		
		}
		 return pkProject;
    }
}