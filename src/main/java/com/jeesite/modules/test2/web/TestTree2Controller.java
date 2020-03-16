/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test2.web;

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

import com.jeesite.common.config.Global;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.test2.entity.TestTree2;
import com.jeesite.modules.test2.service.TestTree2Service;

/**
 * 测试树表Controller
 * @author tcl
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/test2/testTree2")
public class TestTree2Controller extends BaseController {

	@Autowired
	private TestTree2Service testTree2Service;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TestTree2 get(String treeCode, boolean isNewRecord) {
		return testTree2Service.get(treeCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("test2:testTree2:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestTree2 testTree2, Model model) {
		model.addAttribute("testTree2", testTree2);
		return "modules/test2/testTree2List";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("test2:testTree2:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<TestTree2> listData(TestTree2 testTree2) {
		if (StringUtils.isBlank(testTree2.getParentCode())) {
			testTree2.setParentCode(TestTree2.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(testTree2.getTreeName())){
			testTree2.setParentCode(null);
		}
		if (StringUtils.isNotBlank(testTree2.getRemarks())){
			testTree2.setParentCode(null);
		}
		List<TestTree2> list = testTree2Service.findList(testTree2);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("test2:testTree2:view")
	@RequestMapping(value = "form")
	public String form(TestTree2 testTree2, Model model) {
		// 创建并初始化下一个节点信息
		testTree2 = createNextNode(testTree2);
		model.addAttribute("testTree2", testTree2);
		return "modules/test2/testTree2Form";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("test2:testTree2:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public TestTree2 createNextNode(TestTree2 testTree2) {
		if (StringUtils.isNotBlank(testTree2.getParentCode())){
			testTree2.setParent(testTree2Service.get(testTree2.getParentCode()));
		}
		if (testTree2.getIsNewRecord()) {
			TestTree2 where = new TestTree2();
			where.setParentCode(testTree2.getParentCode());
			TestTree2 last = testTree2Service.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				testTree2.setTreeSort(last.getTreeSort() + 30);
				testTree2.setTreeCode(IdGen.nextCode(last.getTreeCode()));
			}else if (testTree2.getParent() != null){
				testTree2.setTreeCode(testTree2.getParent().getTreeCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (testTree2.getTreeSort() == null){
			testTree2.setTreeSort(TestTree2.DEFAULT_TREE_SORT);
		}
		return testTree2;
	}

	/**
	 * 保存测试树表
	 */
	@RequiresPermissions("test2:testTree2:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated TestTree2 testTree2) {
		testTree2Service.save(testTree2);
		return renderResult(Global.TRUE, text("保存测试树表成功！"));
	}
	
	/**
	 * 删除测试树表
	 */
	@RequiresPermissions("test2:testTree2:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(TestTree2 testTree2) {
		testTree2Service.delete(testTree2);
		return renderResult(Global.TRUE, text("删除测试树表成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("test2:testTree2:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<TestTree2> list = testTree2Service.findList(new TestTree2());
		for (int i=0; i<list.size(); i++){
			TestTree2 e = list.get(i);
			// 过滤非正常的数据
			if (!TestTree2.STATUS_NORMAL.equals(e.getStatus())){
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
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getTreeCode(), e.getTreeName()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("test2:testTree2:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(TestTree2 testTree2){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		testTree2Service.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}