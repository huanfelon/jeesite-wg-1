package com.jeesite.modules.abs.fildtype;

import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.base.entity.BdPsndoc;
import com.jeesite.modules.base.service.BdPsndocService;
public class BdPsndocObj {

	private static BdPsndocService psndocService = SpringUtils.getBean(BdPsndocService.class);
	
	private static ThreadLocal<List<BdPsndoc>> cache = new NamedThreadLocal<>("BdPsndocObj");
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<BdPsndoc> cacheList = cache.get();
		if (cacheList == null){
			cacheList = psndocService.findList(new BdPsndoc());
			cache.set(cacheList);
		}
		for (BdPsndoc e : cacheList){
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
		if (val != null && ((BdPsndoc)val).getName() != null){
			return ((BdPsndoc)val).getName();
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
