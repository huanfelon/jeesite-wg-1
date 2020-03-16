/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.abs.enums.AbsEnumType;
import com.jeesite.modules.base.service.CommonBaseService;
import com.jeesite.modules.bd.service.BdFormattypeService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.zl.entity.CurrectMergeHouseVO;
import com.jeesite.modules.zl.entity.SplitHouseVO;
import com.jeesite.modules.zl.entity.ZlHousesource;
import com.jeesite.modules.zl.service.ZlBuildingfileService;
import com.jeesite.modules.zl.service.ZlFamilyfileService;
import com.jeesite.modules.zl.service.ZlHousesourceService;
import com.jeesite.modules.zl.service.ZlProjectService;

/**
 * zl_housesourceController
 * @author GuoJ
 * @version 2019-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/zl/zlHousesource")
public class ZlHousesourceController extends BaseController {

	@Autowired
	private ZlHousesourceService zlHousesourceService;
	@Autowired
	private CommonBaseService commonService;
	@Autowired
    private SolrClient solrClient;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ZlProjectService zlProjectService;
	@Autowired
	private ZlBuildingfileService  zlBuildingfileService;
	@Autowired
	private ZlFamilyfileService zlFamilyfileService;
	@Autowired
	private BdFormattypeService  bdFormattypeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZlHousesource get(String pkHousesource, boolean isNewRecord) {
		return zlHousesourceService.get(pkHousesource, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("zl:zlHousesource:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZlHousesource zlHousesource, Model model) {
		model.addAttribute("zlHousesource", zlHousesource);
		return "modules/zl/zlHousesourceList";
	}
	/**
	 * 房源版本记录查询列表
	 */
	@RequiresPermissions("zl:zlHousesource:view")
	@RequestMapping(value = {"houseBodyList", ""})
	public String houseBodyList(ZlHousesource zlHousesource, Model model) {
		List<ZlHousesource> list =new ArrayList<ZlHousesource>();
		ZlHousesource zlHousesource3=zlHousesourceService.get(zlHousesource.getPkHousesource());
		//根据主实体pk查询相关房源
		if (StringUtils.isNotBlank(zlHousesource.getZstpk())) {
			String string[] = zlHousesource.getZstpk().split("-");
			for(String pk:string){
				ZlHousesource z=zlHousesourceService.getZstPkByzl(pk);
				z.setPkOrg(zlHousesource3.getPkOrg());
				z.setProjectname(zlHousesource3.getProjectname());
				z.setBuildname(zlHousesource3.getBuildname());
				z.setPkFamilyfile(zlHousesource3.getPkFamilyfile());
				z.setPkFormattype(zlHousesource3.getPkFormattype());
				list.add(z);
			}
		}
		
		//获取当前数据
		list.add(zlHousesource3);
		model.addAttribute("list", list);
		return "modules/refPages/houseBodyList";
	}
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("zl:zlHousesource:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZlHousesource> listData(ZlHousesource zlHousesource,HttpServletRequest request, HttpServletResponse response) {

		zlHousesource.setPage(new Page<>(request, response));
		zlHousesourceService.queryFilter(zlHousesource);
		Page<ZlHousesource> page = zlHousesourceService.findPage(zlHousesource);
/*		List<ZlHousesource> list=page.getList();
		//加入中文分词器
		if(!StringUtils.isEmpty(zlHousesource.getEstatecode())){//房产编码
			
			String keywords=zlHousesource.getEstatecode();
			SolrQuery solrQuery = new SolrQuery();
			
			solrQuery.setQuery("code:"+keywords); // 设置查询关键字
	        solrQuery.setHighlight(true); // 开启高亮
	        solrQuery.addHighlightField("code,name"); // 高亮字段
	        //solrQuery.addHighlightField("name"); // 高亮字段
	        solrQuery.setHighlightSimplePre("<font color='red'>"); // 高亮单词的前缀
	        solrQuery.setHighlightSimplePost("</font>"); // 高亮单词的后缀
	        solrQuery.setHighlightFragsize(100);
	        solrQuery.setRows(page.getPageSize());
	        try {
	        	solrClient.deleteByQuery("*:*");//删除所有
	        	solrClient.commit();
	        	List<SolrInputDocument> hlist=new ArrayList<SolrInputDocument>();
	        	//根据房源信息设置
	        	for(ZlHousesource zl:list){
	        		 SolrInputDocument document = new SolrInputDocument();
		             //向文档中添加域
		        	 document.addField("id", zl.getPkHousesource());
		             document.addField("code", zl.getEstatecode());
		             document.addField("name", zl.getEstatename());
		             hlist.add(document);
	        	}
	        	if(hlist.size()>0){
	        		solrClient.add(hlist);
		        	solrClient.commit();
					QueryResponse query = solrClient.query(solrQuery);
					//System.err.println("-------------------高亮效果部分展示-------------------------");
					Map<String, Map<String, List<String>>> maplist = query.getHighlighting();
					for (int i = 0; i < list.size(); ++i) {
			            String id = list.get(i).getId();
			            if (maplist.get(id) != null && maplist.get(id).get("code") != null) {
			            	list.get(i).setEstatecode(maplist.get(id).get("code").get(0));
			            }
			            if (maplist.get(id) != null && maplist.get(id).get("name") != null) {
			            	list.get(i).setEstatename(maplist.get(id).get("name").get(0));
			            }
			        }
	        	}
	        	
				
			} catch (SolrServerException | IOException e) {
				System.out.println("solr服务异常！");
			}
		}
		*/
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlHousesource:view")
	@RequestMapping(value = "form")
	public String form(ZlHousesource zlHousesource, String isEdit, Model model) {
		if(isEdit.equals("true")&& StringUtils.isBlank(zlHousesource.getPkHousesource())){
			zlHousesource.setCreator(UserUtils.getUser());
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(zlHousesource.getPkHousesource())) {
			zlHousesource.setModifier(UserUtils.getUser());
			zlHousesource.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("zlHousesource", zlHousesource);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlHousesourceForm";
	}
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("zl:zlHousesource:view")
	@RequestMapping(value = "xgform")
	public String xgform(ZlHousesource zlHousesource, String isEdit, Model model) {
		if(isEdit.equals("true")&& StringUtils.isBlank(zlHousesource.getPkHousesource())){
			zlHousesource.setCreator(UserUtils.getUser());
			zlHousesource.setDbilldate(new Timestamp((new Date()).getTime()));
		}else if (isEdit.equals("true")&& StringUtils.isNotBlank(zlHousesource.getPkHousesource())) {
			zlHousesource.setModifier(UserUtils.getUser());
			zlHousesource.setModifiedtime(new Timestamp((new Date()).getTime()));
		}
		model.addAttribute("zlHousesource", zlHousesource);
		model.addAttribute("isEdit", isEdit);
		return "modules/zl/zlHousesourceXgForm";
	}
	/**
	 * 保存房源信息
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZlHousesource zlHousesource) {
		String code =zlHousesourceService.save1(zlHousesource);
	       if (code.lastIndexOf("!")!=-1) {
	   		return renderResult(Global.FALSE, text(code));
		}
		return renderResult(Global.TRUE, text("保存房源信息成功！"),code);
	}
	
	/**
	 * 删除房源信息
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZlHousesource zlHousesource) {
		zlHousesourceService.delete(zlHousesource);
		return renderResult(Global.TRUE, text("删除房源信息成功！"));
	}
	/**
	 * 批量删除房源信息
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@PostMapping(value = "deleteData")
	@ResponseBody
	public String deleteData(String pks) {
		String [] strings =pks.split("-");
		for(int i=0;i<strings.length;i++){
			ZlHousesource zlHousesource = zlHousesourceService.get(strings[i]);
			zlHousesourceService.delete(zlHousesource);
		}
		//zlHousesourceService.delete(zlHousesource);
		return renderResult(Global.TRUE, text("删除房源信息成功！"));
	}

	/**
	 * 参照查询
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@RequestMapping({"housesourceSelect"})
	public String housesourceSelect(ZlHousesource zlHousesource, String selectData, Model model)
	{
		model.addAttribute("zlHousesource", zlHousesource);
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {
			model.addAttribute("selectData", selectDataJson);
		}
		return "modules/refPages/housesourceSelect";
	}
	//下载模板
	@RequiresPermissions({"zl:zlHousesource:edit"})
	@RequestMapping({"importTemplate"})
	public void importTemplate(ZlHousesource zlHousesource, HttpServletRequest request,HttpServletResponse response){
		//Page<ZlHousesource> listData=this.listData(zlHousesource,request,response);
		List<ZlHousesource> list = new ArrayList<ZlHousesource>();
	/*	if(listData.getList()!=null&&listData.getList().size()>0){
			for(ZlHousesource zl :listData.getList()){
			//	list = ListUtils.newArrayList(new ZlHousesource[] { zl});
				list.add(zl);
			}
			
		}*/
		String fileName = "房源数据模板.xlsx";
		ExcelExport ee = new ExcelExport("房源管理中心", ZlHousesource.class, ExcelField.Type.IMPORT, new String[0]);
		Throwable localThrowable3 = null;
		try {//添加数据
			ee.setDataList(list).write(response, fileName);
		} catch (Throwable localThrowable1){
			localThrowable3 = localThrowable1;
			throw localThrowable1;
		} finally {
			if (ee != null){
				if (localThrowable3 != null){
					try { 
						ee.close(); 
					} catch (Throwable localThrowable2) { 
						localThrowable3.addSuppressed(localThrowable2); 
					}
				}else {
					ee.close();
				}
			}
		}
	}
	//导入
	@ResponseBody
	@RequiresPermissions({"zl:zlHousesource:edit"})
	@PostMapping({"importData"})
	public String importData(MultipartFile file, String updateSupport)
	{
	  try {
	    boolean isUpdateSupport = "1".equals(updateSupport);
	    String message = this.zlHousesourceService.importData(file, Boolean.valueOf(isUpdateSupport));
	    return renderResult("true", "posfull:" + message);
	  } catch (Exception ex) {
	    return renderResult("false", "posfull:" + ex.getMessage());
	  }
	}
	//导出
	@RequiresPermissions({"zl:zlHousesource:edit"})
	@RequestMapping({"exportTemplate"})
	public void exportTemplate(ZlHousesource zlHousesource, HttpServletRequest request,HttpServletResponse response){
		List<ZlHousesource> list = new ArrayList<ZlHousesource>();
		List<ZlHousesource> list1=zlHousesourceService.findList(new ZlHousesource());
		if(list1.size()>0){
			for(ZlHousesource zl :list1){
			//	list = ListUtils.newArrayList(new ZlHousesource[] { zl});
				list.add(zl);
			}
			
		}
		String fileName = "房源数据模板.xlsx";
		ExcelExport ee = new ExcelExport("房源管理中心", ZlHousesource.class, ExcelField.Type.EXPORT, new String[0]);
		Throwable localThrowable3 = null;
		try {
			ee.setDataList(list).write(response, fileName);
		} catch (Throwable localThrowable1){
			localThrowable3 = localThrowable1;
			throw localThrowable1;
		} finally {
			if (ee != null){
				if (localThrowable3 != null){
					try { 
						ee.close(); 
					} catch (Throwable localThrowable2) { 
						localThrowable3.addSuppressed(localThrowable2); 
					}
				}else {
					ee.close();
				}
			}
		}
	}
	/**
	 * 参照查询
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@RequestMapping({"currectHouseSelect"})
	public String currectHouseSelect(ZlHousesource zlHousesource, String selectData,String projectname,String pk, Model model)
	{
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {

			model.addAttribute("selectData", selectDataJson);
		}
		 model.addAttribute("projectname", projectname);
		 model.addAttribute("pk", pk);
		return "modules/refPages/currectHouseSelect";
	}
	//批改
	@PostMapping(value="currect")
	
	 public @ResponseBody String currect(CurrectMergeHouseVO vo){
		zlHousesourceService.houseCurrect(vo);

		return renderResult(Global.TRUE, text("保存成功!"));
		
	}
	//修改已被参照的树结构项目
	@RequiresPermissions("zl:zlHousesource:edit")
	@RequestMapping(value = "bianji")
	@ResponseBody
    public String bianji(String pkHousesource,boolean isNewRecord){
	    	  //查询合同管理房产页签
			   String sql1="select count(*) from wg_contract_house z  where z.dr=0 and z.pk_house='"+pkHousesource+"'";
			   Integer count1= commonService.selectCount(sql1);
		    	if (count1>0) {
		    		 return "isXj";
			    }
		 return pkHousesource;
    }
	//拆分参照
	@RequiresPermissions("zl:zlHousesource:edit")
	@RequestMapping({"splitHouseSelect"})
	public String splitHouseSelect(ZlHousesource zlHousesource, String selectData,String pk, Model model)
	{
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		
		ZlHousesource zlhouse = zlHousesourceService.get(pk);
		zlhouse.setPkHousesource(null);
		zlhouse.setParentroom(zlhouse.getRoomnumber());
		zlhouse.setRoomnumber(null);
		List<ZlHousesource> page =  new ArrayList<ZlHousesource>();
		if (StringUtils.isNotBlank(zlhouse.getZstpk())) {
			String string[]=zlhouse.getZstpk().split("-");
			for(String string2:string){
				ZlHousesource zs = zlHousesourceService.get(string2);
				if (StringUtils.isBlank(zs.getZstpk())) {
					zs.setPkHousesource(null);
					zs.setRoomnumber(null);
					zs.setParentroom(zlhouse.getParentroom());
					zs.setHbcfstatus(AbsEnumType.HbcfStatus_0);
					page.add(zs);
				}
			}
			model.addAttribute("page", page);
			model.addAttribute("isHb", true);
		}else {
			for(int i=0;i<2;i++){
				if (i==1) {
					ZlHousesource zlhouse1 = zlHousesourceService.get(pk);
					zlhouse1.setPkHousesource(null);
					zlhouse1.setBuildarea(new Double(0));
					zlhouse1.setInnerarea(new Double(0));
					zlhouse1.setRoomnumber(null);
					zlhouse1.setParentroom(zlhouse.getParentroom());
					page.add(zlhouse1);
					continue;
				}
				page.add(zlhouse);
			}
			model.addAttribute("page", page);
			model.addAttribute("isHb", false);
		}
	
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {

			model.addAttribute("selectData", selectDataJson);
		}
		 model.addAttribute("pk", pk);
		return "modules/refPages/splitHouseSelect";
	}
	//拆分查询
/*	*//**
	 * 查询列表数据
	 *//*
	@RequiresPermissions("zl:zlHousesource:view")
	@RequestMapping(value = "listSplit")
	@ResponseBody
	public List<ZlHousesource> listSplit(ZlHousesource zlHousesource,String pk,HttpServletRequest request, HttpServletResponse response) {
	
			ZlHousesource zlhouse = zlHousesourceService.get(pk);
			zlhouse.setPkHousesource(null);
			zlhouse.setRoomnumber(null);
			List<ZlHousesource> page =  new ArrayList<ZlHousesource>();
			for(int i=0;i<2;i++){
				if (i==1) {
					ZlHousesource zlhouse1 = zlHousesourceService.get(pk);
					zlhouse1.setPkHousesource(null);
					zlhouse1.setBuildarea(new Double(0));
					zlhouse1.setInnerarea(new Double(0));
					zlhouse1.setRoomnumber(null);
					page.add(zlhouse1);
					continue;
				}

				page.add(zlhouse);
			}

			return page;
	}*/
	//拆分
	@PostMapping(value="split")
	
	 public @ResponseBody String split(SplitHouseVO vo){
	
		String msg= zlHousesourceService.houseSplit(vo);
		if (!msg.equals("")) {
			return renderResult(Global.FALSE, text(msg));
		}
		return renderResult(Global.TRUE, text("拆分成功!"));
	}
	//合并
	@RequiresPermissions("zl:zlHousesource:edit")
	@RequestMapping({"mergeHouseSelect"})
	public String mergeHouseSelect(ZlHousesource zlHousesource, String selectData,String pk,String bsum,String isum, String zst,Model model)
	{
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null) {

			model.addAttribute("selectData", selectDataJson);
		}
		 model.addAttribute("pk", pk);
		 model.addAttribute("bsum", bsum);
		 model.addAttribute("isum", isum);
		 model.addAttribute("zst", zst);
		return "modules/refPages/mergeHouseSelect";
	}
	/**
	 * 合并查询列表数据
	 */
	@RequiresPermissions("zl:zlHousesource:view")
	@RequestMapping(value = "listMerge")
	@ResponseBody
	public List<ZlHousesource> listMerge(ZlHousesource zlHousesource,String pk,String bsum,String isum,String zst,HttpServletRequest request, HttpServletResponse response) {
	
			ZlHousesource zlhouse = zlHousesourceService.get(pk);
			zlhouse.setPkHousesource(null);
			zlhouse.setRoomnumber(null);
			zlhouse.setBuildarea(new Double(bsum));
			zlhouse.setInnerarea(new Double(isum));
			zlhouse.setHbcfstatus(AbsEnumType.HbcfStatus_0);
			String string[]=zst.split("-");
			zlhouse.setZstroom(string[0]);
			String string2 = zst.replace("-", " | ");
			zlhouse.setParentroom(string2);
			List<ZlHousesource> page =  new ArrayList<ZlHousesource>();
			page.add(zlhouse);
	        return page;
	}
	//合并
	@PostMapping(value="merge")
	
	 public @ResponseBody String merge(CurrectMergeHouseVO vo){
	
		boolean aa= zlHousesourceService.houseMerge(vo);
        if (!aa) {
    		return renderResult(Global.FALSE, text("房源状态异常，请检查!"));
		}
		return renderResult(Global.TRUE, text("合并成功!"));
	}
	
	/**
	 * 建房cwf_house
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@RequestMapping(value = "build",method=RequestMethod.POST)
	@ResponseBody
	public String build(@RequestBody List<Object> pk_cwf) {
		zlHousesourceService.build(pk_cwf);
		return renderResult(Global.TRUE, text("建房成功！"));
	}
	/**
	 * 合并时校验房源有没有被合同引用
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@PostMapping(value = "mergeData")
	@ResponseBody
	public String mergeData(String pks) {
		String [] strings =pks.split("-");
		for(int i=0;i<strings.length;i++){
			//查询合同管理房产页签
			   String sql1="select count(*) from wg_contract_house z  where z.dr=0 and z.pk_house='"+strings[i]+"'";
			   Integer count1= commonService.selectCount(sql1);
		    	if (count1>0) {
		    		 return renderResult(Global.FALSE, text("已被合同管理房产页签引用，不可合并!请检查"),strings[i]);
			    }
		}
		    return "merge";
	}
	/**
	 * 拆分时校验房源有没有被合同管理房产页签
	 */
	@RequiresPermissions("zl:zlHousesource:edit")
	@PostMapping(value = "splitData")
	@ResponseBody
	public String splitData(String pk) {
			//查询合同管理房产页签
			   String sql1="select count(*) from wg_contract_house z  where z.dr=0 and z.pk_house='"+pk+"'";
			   Integer count1= commonService.selectCount(sql1);
		    	if (count1>0) {
		    		 return renderResult(Global.FALSE, text("已被合同管理房产页签引用，不可拆分!请检查"),pk);
			    }
		
		    return "split";
	}
}