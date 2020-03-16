package com.jeesite.modules.abs.fildtype;
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */

import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.base.service.BdDeptService;
public class BdDeptObj {

	private static BdDeptService carService = SpringUtils.getBean(BdDeptService.class);
	
	private static ThreadLocal<List<BdDept>> cache = new NamedThreadLocal<>("BdDeptObj");
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<BdDept> cacheList = cache.get();
		if (cacheList == null){
			cacheList = carService.findList(new BdDept());
			cache.set(cacheList);
		}
		for (BdDept e : cacheList){
			if (StringUtils.trimToEmpty(val).equals(e.getDeptName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((BdDept)val).getDeptName() != null){
			return ((BdDept)val).getDeptName();
		}
		return "";
	}
	
	/**
	 * 清理缓存
	 */
	public static void clearCache(){
		cache.remove();
	}
}
