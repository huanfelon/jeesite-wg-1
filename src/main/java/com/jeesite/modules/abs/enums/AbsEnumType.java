package com.jeesite.modules.abs.enums;

public class AbsEnumType {
	
	//税率
	/** 无税*/
	public static final Integer Taxrate0 = 0;
	/** 3%*/
	public static final Integer Taxrate3 = 3;
	/** 5%*/
	public static final Integer Taxrate5 = 5;
	/** 6%*/
	public static final Integer Taxrate6 = 6;
	/** 9%*/
	public static final Integer Taxrate9 = 9;
	/** 10%*/
	public static final Integer Taxrate10 = 10;
	/** 11%*/
	public static final Integer Taxrate11 = 11;
	/** 17%*/
	public static final Integer Taxrate17 = 17;
	
	// 租赁方式
	/** 日/平方米，1 **/
	public static final Integer RentType_rm=1;
	/** 日/套，2 **/
	public static final Integer RentType_rt=2;
	/** 月/平方米，3 **/
	public static final Integer RentType_ym=3;
	/** 月/套，4 **/
	public static final Integer RentType_yt=4;
	/** 年/平方米，5 **/
	public static final Integer RentType_nm=5;
	/** 年/套，6 **/
	public static final Integer RentType_nt=6;
	
	//收费标准-计算方式
	/** 租赁面积*/
	public static final Integer FeeScale_mj = 0;
	/** 固定金额*/
	public static final Integer FeeScale_je = 1;
	
	//收费标准-舍入方式
	/** 四舍五入取整*/
	public static final Integer FeeScale2_QZ = 0;
	/** 四舍五入保留一位*/
	public static final Integer FeeScale2_BLYW = 1;
	/** 四舍五入保留两位*/
	public static final Integer FeeScale2_BLLW = 2;
	/** 四舍五入保留三位*/
	public static final Integer FeeScale2_BLSANW = 3;
	/** 四舍五入保留四位*/
	public static final Integer FeeScale2_BLSIW = 4;
	/** 进位取整*/
	public static final Integer FeeScale2_JW = -1;
	
	//房源状态
	/** 空置*/
	public static final Integer HOUSE_KZ = 0;
	/** 自用*/
	public static final Integer HOUSE_ZY = 1;
	/** 已租*/
	public static final Integer HOUSE_YZ = 2;
	
	//集团编码
	/** 集团*/
	public static String PK_GROUP="SD";
	
	//组织集团过滤
	/** 组织*/
	public static String isZz="N";
	/** 集团*/
	public static String isJt="Y";
	//单据类型
	/** 合同管理 **/
	public static final String BillType_HT="ct";
	/** 合同修订 **/
	public static final String BillType_HTXD="ctxd";
	/** 退租管理 **/
	public static final String BillType_TZ="tz";
	/** 租约账单 **/
	public static final String BillType_ZD="zd";
	
	//单据状态
	/** 自由 **/
	public static final Integer BillStatus_ZY=0;
	/** 审批通过 **/
	public static final Integer BillStatus_SPTG=1;
	
	//合同状态
	/** 自由 **/
	public static final Integer HtStatus_ZY=0;
	/** 签租 **/
	public static final Integer HtStatus_QZ=1;
	/** 退租 **/
	public static final Integer HtStatus_TZ=2;
	
	/**
	 * 客户状态
	 */
	public static String CUST_MORMAL = "0";
	
	/** 是否自制 **/
	public static String CT_CHARGE_iSZZ = "1";
	
	public static String ISQC = "Y";
	
	/** 合并拆分状态 **/
	public static final Integer HbcfStatus_0=0;
	public static final Integer HbcfStatus_1=-1;
	public static final Integer HbcfStatus_2=-2;
}
