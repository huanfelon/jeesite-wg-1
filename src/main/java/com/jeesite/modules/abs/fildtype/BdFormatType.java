package com.jeesite.modules.abs.fildtype;
/*    */ 
/*    */ import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.bd.entity.BdFormattype;
import com.jeesite.modules.bd.service.BdFormattypeService;
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
/*    */ public class BdFormatType
/*    */ {
/* 22 */   private static ThreadLocal<List<BdFormattype>> cache = new NamedThreadLocal("BdFormatType");
           private static BdFormattypeService bdFormattypeService = SpringUtils.getBean(BdFormattypeService.class);
/*    */   
/*    */   public BdFormatType() {}
/*    */   
/*    */   public static Object getValue(String val)
/*    */   {
/* 28 */     List<BdFormattype> cacheList = (List)cache.get();
/* 29 */     if (cacheList == null) {
	           BdFormattype where =new BdFormattype();
	           where.setStatus(BdFormattype.STATUS_NORMAL);
/* 30 */       cacheList = bdFormattypeService.findList(where);
/* 31 */       cache.set(cacheList);
/*    */     }
/* 33 */     for (BdFormattype e : cacheList) {
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
/* 45 */     if ((val != null) && (((BdFormattype)val).getName() != null)) {
/* 46 */       return ((BdFormattype)val).getName();
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