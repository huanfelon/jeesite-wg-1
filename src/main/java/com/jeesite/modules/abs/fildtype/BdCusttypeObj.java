package com.jeesite.modules.abs.fildtype;
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */

import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.bd.entity.BdCusttype;
import com.jeesite.modules.bd.service.BdCusttypeService;
public class BdCusttypeObj {

	private static BdCusttypeService carService = SpringUtils.getBean(BdCusttypeService.class);
	
	private static ThreadLocal<List<BdCusttype>> cache = new NamedThreadLocal<>("BdCusttypeObj");
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<BdCusttype> cacheList = cache.get();
		if (cacheList == null){
			cacheList = carService.findList(new BdCusttype());
			cache.set(cacheList);
		}
		for (BdCusttype e : cacheList){
			if (StringUtils.trimToEmpty(val).equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((BdCusttype)val).getName() != null){
			return ((BdCusttype)val).getName();
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
