package com.jeesite.modules.base.validator;

import java.util.List;

import javax.validation.ValidationException;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;

public class CodeValidatorUtils {
	
	/**
	 * @param entity 实体
	 * @param dbfilename  数据库编号字段
	 * @param codeValue 值
	 * @param service 服务类
	 * @throws ValidationException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void validatorCode(DataEntity newentity,DataEntity entity,String dbfilename,String codeValue,CrudService service)
			throws ValidationException{
		
		newentity.getSqlMap().getWhere().and(dbfilename, QueryType.EQ, codeValue);
		if(!entity.getIsNewRecord()){
			newentity.getSqlMap().getWhere().and(newentity.getIdColumnName(), QueryType.NE, entity.getId());
		}
		List<DataEntity> dlist=service.findList(newentity);
		if(dlist.size()<=0){
			return ;
		}
		throw new ValidationException("编码重复，请检查！");
	}
	
	/**
	 * @param entity 实体
	 * @param dbfilename  数据库编号字段
	 * @param codeValue 值
	 * @param service 服务类
	 * @throws ValidationException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String validatorHtCode(DataEntity newentity,DataEntity entity,String dbfilename,String codeValue,CrudService service)
			throws ValidationException{
		String msg="";
		newentity.getSqlMap().getWhere().and(dbfilename, QueryType.EQ, codeValue);
		List<DataEntity> dlist=service.findList(newentity);
		if(dlist.size()>0){
			msg="合同号重复，请检查！";
		}
		return msg;
	}

}
