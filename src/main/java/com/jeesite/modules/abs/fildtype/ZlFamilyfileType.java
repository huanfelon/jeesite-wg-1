package com.jeesite.modules.abs.fildtype;
/*    */ 
/*    */ /*    */ import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.zl.entity.ZlFamilyfile;
import com.jeesite.modules.zl.service.ZlFamilyfileService;
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
/*    */ 
/*    */ 
/*    */ public class ZlFamilyfileType
/*    */ {

/* 22 */   private static ThreadLocal<List<ZlFamilyfile>> cache = new NamedThreadLocal("ZlFamilyfileType");
            private static ZlFamilyfileService zlFamilyfileService = SpringUtils.getBean(ZlFamilyfileService.class);
/*    */   
/*    */   public ZlFamilyfileType() {}
/*    */   
/*    */   public static Object getValue(String val)
/*    */   {
/* 28 */     List<ZlFamilyfile> cacheList = (List)cache.get();
/* 29 */     if (cacheList == null) {
            	ZlFamilyfile where =new ZlFamilyfile();
    			where.setStatus(ZlFamilyfile.STATUS_NORMAL);
/* 30 */       cacheList = zlFamilyfileService.findList(where);
/* 31 */       cache.set(cacheList);
/*    */     }
/* 33 */     for (ZlFamilyfile e : cacheList) {
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
/* 45 */     if ((val != null) && (((ZlFamilyfile)val).getName() != null)) {
/* 46 */       return ((ZlFamilyfile)val).getName();
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