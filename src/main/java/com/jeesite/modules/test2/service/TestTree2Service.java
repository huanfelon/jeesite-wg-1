/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.test2.entity.TestTree2;
import com.jeesite.modules.test2.dao.TestTree2Dao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 测试树表Service
 * @author tcl
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly=true)
public class TestTree2Service extends TreeService<TestTree2Dao, TestTree2> {
	
	/**
	 * 获取单条数据
	 * @param testTree2
	 * @return
	 */
	@Override
	public TestTree2 get(TestTree2 testTree2) {
		return super.get(testTree2);
	}
	
	/**
	 * 查询列表数据
	 * @param testTree2
	 * @return
	 */
	@Override
	public List<TestTree2> findList(TestTree2 testTree2) {
		return super.findList(testTree2);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testTree2
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TestTree2 testTree2) {
		super.save(testTree2);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testTree2.getId(), "testTree2_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testTree2.getId(), "testTree2_file");
	}
	
	/**
	 * 更新状态
	 * @param testTree2
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TestTree2 testTree2) {
		super.updateStatus(testTree2);
	}
	
	/**
	 * 删除数据
	 * @param testTree2
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TestTree2 testTree2) {
		super.delete(testTree2);
	}
	
}