package com.jeesite.modules.base.validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.base.dao.BdPsndocDao;
import com.jeesite.modules.wght.dao.WgContractDao;

public class CodeFactoryUtils {
	
	private static WgContractDao wgContractDao=SpringUtils.getBean(WgContractDao.class);
	
	private static BdPsndocDao bdPsndocDao=SpringUtils.getBean(BdPsndocDao.class);
	
	/**
	 * 单据名称首字母大写+日期（年月日）+5位流水号，流水号按年归零
	 * @param djName	单据名称首字母大写
	 * @param tableName	表名
	 * @param itemName	单据号字段名
	 * @return
	 */
	public static String createBillCode(String djName, String tableName, String itemName) {
		String billcode="";
		Integer lshsize=5;	//流水号位数
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String today=sdf.format(new Date());
		billcode=djName+today;
		String maxcode=wgContractDao.getMaxBillCode(tableName, itemName, "1", "1");
		if(getStgObj(maxcode).equals("")){
			return billcode+"00001";
		}
		Integer length=getStgObj(maxcode).length();
		String precode=maxcode.substring(0,8);
		if(!billcode.substring(0,8).equals(precode)){
			billcode+="00001";
		}else{
			Integer codenum=Integer.parseInt(getStgObj(maxcode).substring(length-lshsize,length))+1;
			String zero="";
			if(codenum.toString().length()<5){
				for(int i=0;i<lshsize-codenum.toString().length();i++){
					zero+="0";
				}
			}
			billcode+=zero+codenum;
		}
		return billcode;
	}
	
	/**
	 * @param djName	规则编码
	 * @param tableName	表名
	 * @param itemName	单据号字段名
	 * @param lshsize	流水号长度
	 * @return
	 */
	public static String createBillCode(String djName, String tableName, String itemName,Integer lshsize) {
		String billcode=djName;
		String maxcode=bdPsndocDao.getMaxBillCode(tableName, itemName, djName);
		//构造初始数据
		String last="1";
		for(int i=1;i<lshsize;i++){
			last="0"+last;
		}
		
		if(getStgObj(maxcode).equals("")){
			return billcode+last;
		}
		Integer length=getStgObj(maxcode).length();
		String precode=maxcode.substring(0,length-lshsize);
		if(!billcode.substring(0,length-lshsize).equals(precode)){
			billcode+=last;
		}else{
			Integer codenum=Integer.parseInt(getStgObj(maxcode).substring(length-lshsize,length))+1;
			String zero="";
			if(codenum.toString().length()<lshsize){
				for(int i=0;i<lshsize-codenum.toString().length();i++){
					zero+="0";
				}
			}
			billcode+=zero+codenum;
		}
		return billcode;
	}
	
	/**
	 * @param djName	规则编码
	 * @param tableName	表名
	 * @param itemName	单据号字段名
	 * @param lshsize	流水号长度
	 * @return
	 */
	public static String createBillCodeByDr(String djName, String tableName, String itemName,Integer lshsize) {
		String billcode=djName;
		String maxcode=bdPsndocDao.getMaxBillCodeByDr(tableName, itemName, djName);
		//构造初始数据
		String last="1";
		for(int i=1;i<lshsize;i++){
			last="0"+last;
		}
		
		if(getStgObj(maxcode).equals("")){
			return billcode+last;
		}
		Integer length=getStgObj(maxcode).length();
		String precode=maxcode.substring(0,length-lshsize);
		if(!billcode.substring(0,length-lshsize).equals(precode)){
			billcode+=last;
		}else{
			Integer codenum=Integer.parseInt(getStgObj(maxcode).substring(length-lshsize,length))+1;
			String zero="";
			if(codenum.toString().length()<lshsize){
				for(int i=0;i<lshsize-codenum.toString().length();i++){
					zero+="0";
				}
			}
			billcode+=zero+codenum;
		}
		return billcode;
	}
	
	/**
	 * 单据名称首字母大写+日期（年月日）+5位流水号，流水号按年归零
	 * @param djName	单据名称首字母大写
	 * @param tableName	表名
	 * @param itemName	单据号字段名
	 * @param billtypeColumn	单据类型字段名称
	 * @param billtype	单据类型值
	 * @return
	 */
	public static String createBillCode(String djName, String tableName, String itemName,String billtypeColumn, String billtype) {
		String billcode="";
		Integer lshsize=5;	//流水号位数
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String today=sdf.format(new Date());
		billcode=djName+today;
		String maxcode=wgContractDao.getMaxBillCode(tableName, itemName, billtypeColumn, billtype);
		if(getStgObj(maxcode).equals("")){
			return billcode+"00001";
		}
		Integer length=getStgObj(maxcode).length();
		String precode=maxcode.substring(0,8);
		if(!billcode.substring(0,8).equals(precode)){
			billcode+="00001";
		}else{
			Integer codenum=Integer.parseInt(getStgObj(maxcode).substring(length-lshsize,length))+1;
			String zero="";
			if(codenum.toString().length()<5){
				for(int i=0;i<lshsize-codenum.toString().length();i++){
					zero+="0";
				}
			}
			billcode+=zero+codenum;
		}
		return billcode;
	}
	
	/**
	 * @param entity 实体
	 * @param dbfilename  数据库编号字段
	 * @param codeValue 值
	 * @param service 服务类
	 * @throws ValidationException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void validatorVbillno(DataEntity newentity,DataEntity entity,String dbfilename,String codeValue,CrudService service)
			throws ValidationException{
		
		newentity.getSqlMap().getWhere().and(dbfilename, QueryType.EQ, codeValue);
		if(!entity.getIsNewRecord()){
			newentity.getSqlMap().getWhere().and(newentity.getIdColumnName(), QueryType.NE, entity.getId());
		}
		List<DataEntity> dlist=service.findList(newentity);
		if(dlist.size()<=0){
			return ;
		}
		throw new ValidationException("单据号已存在，请重新制单！");
	}
	
	public static String getStgObj(Object obj){
		return obj==null?"":obj.toString();
	}

}
