/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.base.dao.BdCustomerDao;
import com.jeesite.modules.base.dao.BdCustomerKpxxDao;
import com.jeesite.modules.base.dao.BdCustomerProjDao;
import com.jeesite.modules.base.dao.CommonBaseDao;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.base.entity.BdCustomerKpxx;
import com.jeesite.modules.base.entity.BdCustomerProj;

/**
 * 客户信息中心Service
 * @author tcl
 * @version 2019-11-05
 */
@Service
@Transactional(readOnly=true)
public class BdCustomerService extends CrudService<BdCustomerDao, BdCustomer> {
	
	@Autowired
	private BdCustomerDao bdCustomerxDao;
	
	@Autowired
	private BdCustomerKpxxDao bdCustomerKpxxDao;
	
	@Autowired
	private BdCustomerProjDao bdCustomerProjDao;
	@Autowired
	private CommonBaseDao baseDao;
	
	/**
	 * 获取单条数据
	 * @param bdCustomer
	 * @return
	 */
	@Override
	public BdCustomer get(BdCustomer bdCustomer) {
		BdCustomer entity = super.get(bdCustomer);
		if (entity != null){
			BdCustomerKpxx bdCustomerKpxx = new BdCustomerKpxx(entity);
			bdCustomerKpxx.setStatus(BdCustomerKpxx.STATUS_NORMAL);
			entity.setBdCustomerKpxxList(bdCustomerKpxxDao.findList(bdCustomerKpxx));
			BdCustomerProj bdCustomerProj = new BdCustomerProj(entity);
			bdCustomerProj.setStatus(BdCustomerProj.STATUS_NORMAL);
			entity.setBdCustomerProjList(bdCustomerProjDao.findList(bdCustomerProj));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param bdCustomer 查询条件
	 * @param bdCustomer.page 分页对象
	 * @return
	 */
	@Override
	public Page<BdCustomer> findPage(BdCustomer bdCustomer) {
		return super.findPage(bdCustomer);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bdCustomer
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BdCustomer bdCustomer) {
		super.save(bdCustomer);
		// 保存 BdCustomer子表
		for (BdCustomerKpxx bdCustomerKpxx : bdCustomer.getBdCustomerKpxxList()){
			if (!BdCustomerKpxx.STATUS_DELETE.equals(bdCustomerKpxx.getStatus())){
				bdCustomerKpxx.setPkCustomer(bdCustomer);
				if (bdCustomerKpxx.getIsNewRecord()){
					bdCustomerKpxxDao.insert(bdCustomerKpxx);
				}else{
					bdCustomerKpxxDao.update(bdCustomerKpxx);
				}
			}else{
				bdCustomerKpxxDao.delete(bdCustomerKpxx);
			}
		}
		// 保存 BdCustomer子表
		for (BdCustomerProj bdCustomerProj : bdCustomer.getBdCustomerProjList()){
			if (!BdCustomerProj.STATUS_DELETE.equals(bdCustomerProj.getStatus())){
				bdCustomerProj.setPkCustomer(bdCustomer);
				if (bdCustomerProj.getIsNewRecord()){
					bdCustomerProjDao.insert(bdCustomerProj);
				}else{
					bdCustomerProjDao.update(bdCustomerProj);
				}
			}else{
				bdCustomerProjDao.delete(bdCustomerProj);
			}
		}
		//导入直接挂项目By LY 2019-11-26
		/*BdCustomerProj bdCustomerProj=new BdCustomerProj();
		bdCustomerProj.setPkCustomer(bdCustomer);
		bdCustomerProj.setPkProject(new ZlProject("1198808968796000256"));
		bdCustomerProj.setCode("06");
		bdCustomerProjDao.insert(bdCustomerProj);*/
	}
	
	/**
	 * 更新状态
	 * @param bdCustomer
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BdCustomer bdCustomer) {
		super.updateStatus(bdCustomer);
	}
	
	/**
	 * 删除数据
	 * @param bdCustomer
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BdCustomer bdCustomer) {
		super.delete(bdCustomer);
		BdCustomerKpxx bdCustomerKpxx = new BdCustomerKpxx();
		bdCustomerKpxx.setPkCustomer(bdCustomer);
		bdCustomerKpxxDao.deleteByEntity(bdCustomerKpxx);
		BdCustomerProj bdCustomerProj = new BdCustomerProj();
		bdCustomerProj.setPkCustomer(bdCustomer);
		
		bdCustomerProjDao.deleteByEntity(bdCustomerProj);
	}
	
	/**
	 * 判断项目是否被参照
	 */
	public Integer getXmIsRefByProjectid(String projectid){
		return bdCustomerxDao.getXmIsRefByProjectid(projectid);
	}
	
	/**
	 * 数据导入
	 * @param file
	 * @param isUpdateSupport
	 * @return
	 */
	public String importData(MultipartFile file, Boolean isUpdateSupport)
	{
		if (file == null) {
			throw new ServiceException("请选择导入的数据文件！");
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try { 
			ExcelImport ei = new ExcelImport(file, 2, Integer.valueOf(0)); 
			Throwable localThrowable3 = null;
			try { 
				List<BdCustomer> list = ei.getDataList(BdCustomer.class, new String[0]);
				for (BdCustomer user : list)
					try
				{
						ValidatorUtils.validateWithException(user, new Class[0]);
						
						Integer count = baseDao.selectCount("select count(*) from bd_customer where status<>1 and code='"+user.getCode()+"'");
						if (count<=0) {
							save(user);
							successNum++;
							successMsg.append(new StringBuilder().append("<br/>").append(successNum).append("、客户 ").append(user.getCode()).append(" 导入成功").toString());
						} else if (isUpdateSupport.booleanValue()) {
							BdCustomer car2=new BdCustomer();
							car2.setCode(user.getCode());
							List<BdCustomer> list2 = this.findList(car2);
							user.setPkCustomer(list2.get(0).getPkCustomer());
							user.setCode(list2.get(0).getCode());
							save(user);
							successNum++;
							successMsg.append(new StringBuilder().append("<br/>").append(successNum).append("、客户 ").append(user.getCode()).append(" 更新成功").toString());
						} else {
							failureNum++;
							failureMsg.append(new StringBuilder().append("<br/>").append(failureNum).append("、客户 ").append(user.getCode()).append(" 已存在").toString());
						}
				} catch (Exception e) {
					failureNum++;
					String msg = new StringBuilder().append("<br/>").append(failureNum).append("、客户 ").append(user.getCode()).append(" 导入失败：").toString();
					if ((e instanceof ConstraintViolationException)) {
						List<String> messageList = ValidatorUtils.extractPropertyAndMessageAsList((ConstraintViolationException)e, ": ");
						for (String message : messageList)
							msg = new StringBuilder().append(msg).append(message).append("; ").toString();
					}
					else {
						msg = new StringBuilder().append(msg).append(e.getMessage()).toString();
					}
					failureMsg.append(msg);
					this.logger.error(msg, e);
				}
			}
			catch (Throwable localThrowable1)
			{
				localThrowable3 = localThrowable1; throw localThrowable1;
			}
			finally
			{
				if (ei != null) if (localThrowable3 != null) try { ei.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else ei.close();  
			}
		} catch (Exception e) {
			failureMsg.append(e.getMessage());
			this.logger.error(e.getMessage(), e);
		}
		if (failureNum > 0) {
			failureMsg.insert(0, new StringBuilder().append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：").toString());
			throw new ServiceException(failureMsg.toString());
		}
		successMsg.insert(0, new StringBuilder().append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条，数据如下：").toString());
		
		return successMsg.toString();
	}
	
}