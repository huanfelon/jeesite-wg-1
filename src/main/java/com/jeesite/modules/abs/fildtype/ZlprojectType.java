package com.jeesite.modules.abs.fildtype;
/*    */ 
/*    */ import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
/*    */ import com.jeesite.modules.sys.entity.Office;
/*    */ import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.zl.entity.ZlFamilyfile;
import com.jeesite.modules.zl.entity.ZlProject;
import com.jeesite.modules.zl.service.ZlFamilyfileService;
import com.jeesite.modules.zl.service.ZlProjectService;

/*    */ import java.util.List;

import org.springframework.core.NamedThreadLocal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZlprojectType
/*    */ {
/* 22 */   private static ThreadLocal<List<ZlProject>> cache = new NamedThreadLocal("ZlProjectType");
           private static ZlProjectService zlProjectService = SpringUtils.getBean(ZlProjectService.class);
/*    */   
/*    */   public ZlprojectType() {}
/*    */   
/*    */   public static Object getValue(String val)
/*    */   {
/* 28 */     List<ZlProject> cacheList = (List)cache.get();
/* 29 */     if (cacheList == null) {
	           ZlProject where =new ZlProject();
	           where.setStatus(ZlProject.STATUS_NORMAL);
/* 30 */       cacheList = zlProjectService.findList(where);
/* 31 */       cache.set(cacheList);
/*    */     }
/* 33 */     for (ZlProject e : cacheList) {
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
/* 45 */     if ((val != null) && (((ZlProject)val).getName() != null)) {
/* 46 */       return ((ZlProject)val).getName();
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