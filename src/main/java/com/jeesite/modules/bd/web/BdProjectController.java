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
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.bd.service.BdProjectService;
import com.jeesite.modules.ct.dao.CtChargeSkBDao;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.wght.dao.WgContractWytypeDao;
import com.jeesite.modules.wght.dao.WgContractYjDao;
import com.jeesite.modules.wght.dao.WgContractZltypeDao;
import com.jeesite.modules.wght.dao.WgContractZqfyDao;

/**
 * 预算项目Controller
 * @author GJ
 * @version 2019-10-25
 */
@Controller
@RequestMapping(value = "${adminPath}/bd/bdProject")
public class BdProjectController extends BaseController {

	@Autowired
	private BdProjectService bdProjectService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private WgContractZqfyDao wgContractZqfyDao;
	@Autowired
	private WgContractYjDao wgContractYjDao;
	@Autowired
	private WgContractZltypeDao wgContractZltypeDao;
	@Autowired
	private WgContractWytypeDao wgContractWytypeDao;
	@Autowired 
	private CtChargeSkBDao ctChargeSkBDao;
	@Autowired
	private CommonBaseService commonService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BdProject get(String pkProject, boolean isNewRecord) {
		return bdProjectService.get(pkProject, isNewRecord);
	}
	
	/**
	 * 查询列表-组织
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = {"list", ""})
	public String list(BdProject bdProject, Model model) {
		model.addAttribute("bdProject", bdProject);
		return "modules/bd/bdProjectList";
	}
	/**
	 * 查询列表--集团
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = {"list1", ""})
	public String list1(BdProject bdProject, Model model) {
		model.addAttribute("bdProject", bdProject);
		return "modules/bd/bdProjectList1";
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<BdProject> listData(BdProject bdProject) {
	
		if (StringUtils.isNotBlank(bdProject.getCode())){
			bdProject.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdProject.getName())){
			bdProject.setParentCode(null);
		}
		String [] pks=null; //IN方法只接受Object[]和list集合
		//查询条件组织不为空则拼上集团,为空则只展示集团
		if(bdProject.getPkOrg()!=null&&!StringUtils.isEmpty(bdProject.getPkOrg().getOfficeCode())){
			String newpks=bdProject.getPkOrg().getOfficeCode()+","+AbsEnumType.PK_GROUP;
			bdProject.getPkOrg().setOfficeCode(newpks);
			//查询顺序错乱，带入组织查询时先查询最上一级，点击展开再查询下级
			if (bdProject.getParent()==null) {
				bdProject.setParentCode("0");
			}
		}else{
			bdProject.getPkOrg().setOfficeCode(AbsEnumType.PK_GROUP);
		}
		pks=bdProject.getPkOrg().getOfficeCode().split(",");
		bdProject.getSqlMap().getWhere().and("pk_org",QueryType.IN,pks);
		List<BdProject> list = bdProjectService.findList(bdProject);
		return list;
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = "listData1")
	@ResponseBody
	public List<BdProject> listData1(BdProject bdProject) {
	
		if (StringUtils.isNotBlank(bdProject.getCode())){
			bdProject.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bdProject.getName())){
			bdProject.setParentCode(null);
		}
		//查询顺序错乱，带入组织查询时先查询最上一级，点击展开再查询下级
		if (bdProject.getParent()==null) {
			bdProject.setParentCode("0");
		}
		bdProject.getSqlMap().getWhere().and("pk_org", QueryType.EQ,AbsEnumType.PK_GROUP);
		List<BdProject> list = bdProjectService.findList(bdProject);
		return list;
	}
	/**
	 * 查看编辑表单-组织
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = "form")
	public String form(BdProject bdProject, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdProject.getPkProject())){
			if(bdProject.getParent()!=null&&StringUtils.isNotBlank(bdProject.getParent().getPkProject())){
				BdProject zlProject2 =bdProjectService.get(bdProject.getParent());
			    if(!zlProject2.getPkOrg().getOfficeCode().equals(AbsEnumType.PK_GROUP)){
					bdProject.setPkOrg(zlProject2.getPkOrg());
				}
			}
			bdProject.setCreator(UserUtils.getUser());
			bdProject.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdProject.getPkProject())) {
			bdProject.setModifier(UserUtils.getUser());
			bdProject.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdProject = createNextNode(bdProject);
		model.addAttribute("bdProject", bdProject);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdProjectForm";
	}/**
	 * 查看编辑表单-组织
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = "xgform")
	public String xgform(BdProject bdProject, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdProject.getPkProject())){
			bdProject.setCreator(UserUtils.getUser());
			bdProject.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdProject.getPkProject())) {
			bdProject.setModifier(UserUtils.getUser());
			bdProject.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdProject = createNextNode(bdProject);
		model.addAttribute("bdProject", bdProject);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdProjectXgForm";
	}
	/**
	 * 查看编辑表单-集团
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = "form1")
	public String form1(BdProject bdProject, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdProject.getPkProject())){
			bdProject.setCreator(UserUtils.getUser());
			bdProject.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdProject.getPkProject())) {
			bdProject.setModifier(UserUtils.getUser());
			bdProject.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		//初始化集团值
		Office office=new Office();
		office.setOfficeCode(AbsEnumType.PK_GROUP);
		office=officeService.get(office);
		// 创建并初始化下一个节点信息
		bdProject.setPkOrg(office);
		bdProject = createNextNode(bdProject);
		model.addAttribute("bdProject", bdProject);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdProjectForm1";
	}
	/**
	 * 查看编辑表单-集团
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = "xgform1")
	public String xgform1(BdProject bdProject, Model model,String isEdit) {
		if(isEdit.equals("true")&& StringUtils.isBlank(bdProject.getPkProject())){
			bdProject.setCreator(UserUtils.getUser());
			bdProject.setCreateDate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(bdProject.getPkProject())) {
			bdProject.setModifier(UserUtils.getUser());
			bdProject.setUpdateDate(new Timestamp((new Date()).getTime()));
		}
		// 创建并初始化下一个节点信息
		bdProject = createNextNode(bdProject);
		model.addAttribute("bdProject", bdProject);
		model.addAttribute("isEdit", isEdit);
		return "modules/bd/bdProjectXgForm1";
	}
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("bd:bdProject:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public BdProject createNextNode(BdProject bdProject) {
		if (StringUtils.isNotBlank(bdProject.getParentCode())){
			bdProject.setParent(bdProjectService.get(bdProject.getParentCode()));
		}
		if (bdProject.getIsNewRecord()) {
			//新增下级默认带出父级编码
			if(StringUtils.isNotBlank(bdProject.getParentCode())){
				bdProject.setCode(bdProject.getParent().getCode());
			}
			BdProject where = new BdProject();
			where.setParentCode(bdProject.getParentCode());
			BdProject last = bdProjectService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				bdProject.setTreeSort(last.getTreeSort() + 30);
			}
		}
		// 以下设置表单默认数据
		if (bdProject.getTreeSort() == null){
			bdProject.setTreeSort(BdProject.DEFAULT_TREE_SORT);
		}
		return bdProject;
	}

	/**
	 * 保存预算项目
	 */
	@RequiresPermissions("bd:bdProject:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BdProject bdProject) {
		bdProjectService.save(bdProject);
		return renderResult(Global.TRUE, text("保存预算项目成功！"));
	}
	
	/**
	 * 停用预算项目
	 */
	@RequiresPermissions("bd:bdProject:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BdProject bdProject) {
		BdProject where = new BdProject();
		where.setStatus(BdProject.STATUS_NORMAL);
		where.setParentCodes("," + bdProject.getId() + ",");
		long count = bdProjectService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该预算项目包含未停用的子预算项目！"));
		}
		bdProject.setStatus(BdProject.STATUS_DISABLE);
		bdProjectService.updateStatus(bdProject);
		return renderResult(Global.TRUE, text("停用预算项目成功"));
	}
	
	/**
	 * 启用预算项目
	 */
	@RequiresPermissions("bd:bdProject:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BdProject bdProject) {
		bdProject.setStatus(BdProject.STATUS_NORMAL);
		bdProjectService.updateStatus(bdProject);
		return renderResult(Global.TRUE, text("启用预算项目成功"));
	}
	
	/**
	 * 删除预算项目
	 */
	@RequiresPermissions("bd:bdProject:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BdProject bdProject) {
		bdProjectService.delete(bdProject);
		return renderResult(Global.TRUE, text("删除预算项目成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("bd:bdProject:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(BdProject bdProject,String excludeCode, String isShowCode) {
		//by tcl 树参照
		String [] pks=null; //IN方法只接受Object[]和list集合
		//查询条件组织不为空则拼上集团,为空则只展示集团
		if(bdProject.getPkOrg()!=null&&!StringUtils.isEmpty(bdProject.getPkOrg().getOfficeCode())){
			String newpks=bdProject.getPkOrg().getOfficeCode()+","+AbsEnumType.PK_GROUP;
			bdProject.getPkOrg().setOfficeCode(newpks);
		}else{
			Office org=new Office();
			org.setOfficeCode(AbsEnumType.PK_GROUP);
			bdProject.setPkOrg(org);
		}
		pks=bdProject.getPkOrg().getOfficeCode().split(",");
		bdProject.getSqlMap().getWhere().and("pk_org",QueryType.IN,pks);
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<BdProject> list = bdProjectService.findList(bdProject);
		
		for (int i=0; i<list.size(); i++){
			BdProject e = list.get(i);
			// 过滤非正常的数据
			if (!BdProject.STATUS_NORMAL.equals(e.getStatus())){
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
	@RequiresPermissions("bd:bdProject:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(BdProject bdProject){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		bdProjectService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	//修改已被参照的树结构项目
	@RequiresPermissions("bd:bdProject:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkProject,boolean isNewRecord){
		BdProject  bdProject = bdProjectService.get(pkProject);
		//判断修改时有没有下级
		BdProject bdCategory3= new BdProject();
		bdCategory3.setParentCode(bdProject.getPkProject());//判断当前预算类别有无下级
	  //bdCategory3.setParentCode(bdCategory.getParentCode());//判断父节点的子节点即与当前同级别的预算类别
		BdProject bdCategory2 = bdProjectService.getLastByParentCode(bdCategory3);
		if (bdCategory2!=null) {
			 return "isXj";
		}else{
			//查询其他周期费用
			 String sql="select count(*) from wg_contract_zqfy z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
			  Integer count= commonService.selectCount(sql);
			if (count>0) {
				 return "isXj";			
				 }
			//查询合同管理押金页签
			 String sql1="select count(*) from wg_contract_yj z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
			  Integer count1= commonService.selectCount(sql1);
			if (count1>0) {
				 return "isXj";
				 }
			//查询租赁支付方式页签
			 String sql2="select count(*) from wg_contract_zltype z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
			  Integer count2= commonService.selectCount(sql2);
			if (count2>0) {
				 return "isXj";
				 }
			//查询物业支付方式页签
			 String sql3="select count(*) from wg_contract_wytype z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
			  Integer count3= commonService.selectCount(sql3);
			if (count3>0) {
				 return "isXj";
				 }
			//查询收款单收款明细页签
			 String sql4="select count(*) from ct_charge_sk_b z where z.dr=0 and z.pk_sf_project='"+bdProject.getPkProject()+"'";
			  Integer count4= commonService.selectCount(sql4);
			if (count4>0) {
				 return "isXj";
				 }
			//查询收款单收款明细页签
			 String sql5="select count(*) from ct_rentbill_zjmx z where z.dr=0 and z.pk_ysproject='"+bdProject.getPkProject()+"'";
			  Integer count5= commonService.selectCount(sql5);
			if (count5>0) {
				 return "isXj";
				 }
		}
		 return pkProject;
    }
	//在组织节点中 集团下新增组织级判断
	@RequiresPermissions("bd:bdProject:edit")
	@RequestMapping(value = "xinzen")
	@ResponseBody
    public String xinzen(String pk,boolean isNewRecord,String parentCode){
		//顶部新增下级按钮，判断选中的是集团还是组织 ，是集团则校验集团下是否有集团级档案，不是集团则直接新增下级
		if(StringUtils.isNotBlank(pk)&&StringUtils.isBlank(parentCode)){
			//在组织节点新增
			String sql="select count(*) from bd_project c where nvl(c.status,0)=0 and c.pk_org='"+AbsEnumType.PK_GROUP+"'and exists "
					+ "(select 1 from bd_project b where nvl(b.status,0)=0 and b.pk_project=c.parent_code and b.pk_project='"+pk+"')";
		  Integer count=commonService.selectCount(sql);
		  if (count>0) {
			   return renderResult(Global.FALSE, text("下级存在集团级项目,不可以新增组织级!"));
		    }
		}else if (StringUtils.isBlank(pk)&&StringUtils.isNotBlank(parentCode)) {
			return "isXz";
		}
		return "isXz";
    }
}