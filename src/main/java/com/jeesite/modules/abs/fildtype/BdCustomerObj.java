package com.jeesite.modules.abs.fildtype;

import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.base.service.BdCustomerService;
public class BdCustomerObj {

	private static BdCustomerService customerService = SpringUtils.getBean(BdCustomerService.class);
	
	private static ThreadLocal<List<BdCustomer>> cache = new NamedThreadLocal<>("BdCustomerObj");
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<BdCustomer> cacheList = cache.get();
		if (cacheList == null){
			cacheList = customerService.findList(new BdCustomer());
			cache.set(cacheList);
		}
		for (BdCustomer e : cacheList){
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
		if (val != null && ((BdCustomer)val).getName() != null){
			return ((BdCustomer)val).getName();
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
