package com.jeesite.modules.abs.fildtype;
import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.service.ZlBuildingfileService;
	public class ZlBuildingfileType
 {

		private static ThreadLocal<List<ZlBuildingfile>> cache = new NamedThreadLocal("ZlBuildingfileType");
		private static ZlBuildingfileService zlBuildingfileService = SpringUtils.getBean(ZlBuildingfileService.class);
   
		public ZlBuildingfileType() {}
   
		public static Object getValue(String val)
			{
			List<ZlBuildingfile> cacheList = (List)cache.get();
			if (cacheList == null) {
				ZlBuildingfile where =new ZlBuildingfile();
    			where.setStatus(ZlBuildingfile.STATUS_NORMAL);
    			cacheList = zlBuildingfileService.findList(where);
/* 31 */       cache.set(cacheList);
/*    */     }
/* 33 */     for (ZlBuildingfile e : cacheList) {
/* 34 */       if (StringUtils.trimToEmpty(val).equals(e.getName())) {
/* 35 */         return e;
/*    */       }
/*    */     }
/* 38 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static String setValue(Object val)
/*    */   {
/* 45 */     if ((val != null) && (((ZlBuildingfile)val).getName() != null)) {
/* 46 */       return ((ZlBuildingfile)val).getName();
/*    */     }
/* 48 */     return "";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void clearCache()
/*    */   {
/* 55 */     cache.remove();
/*    */   }
/*    */ }

/* Location:           C:\Users\11592\.m2\repository\com\jeesite\jeesite-module-core\4.1.2-SNAPSHOT\jeesite-module-core-4.1.2-SNAPSHOT.jar
 * Qualified Name:     com.jeesite.common.utils.excel.fieldtype.OfficeType
 * Java Class Version: 8 (52.0)
 * JD-Core Version:    0.7.0.1
 */