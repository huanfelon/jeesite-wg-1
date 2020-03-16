/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.fieldtype.OfficeType;
import com.jeesite.modules.abs.fildtype.BdCusttypeObj;
import com.jeesite.modules.abs.fildtype.BdPsndocObj;
import com.jeesite.modules.bd.entity.BdCusttype;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * 客户信息中心Entity
 * @author tcl
 * @version 2019-11-05
 */
@Table(name="bd_customer", alias="a", columns={
		@Column(name="pk_customer", attrName="pkCustomer", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="所属组织"),
		@Column(name="code", attrName="code", label="客户编码", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="客户名称", queryType=QueryType.LIKE),
		@Column(name="pk_custtype", attrName="pkCusttype.id", label="客户类型"),
		@Column(name="pk_psndoc", attrName="pkPsndoc.pkPsndoc", label="业务员"),
		@Column(name="idno", attrName="idno", label="身份证号", isQuery=false),
		@Column(name="creditcode", attrName="creditcode", label="社会统一信用代码", isQuery=false),
		@Column(name="custstatus", attrName="custstatus", label="客户状态"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u2",
			on="u2.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true,isQuery=false),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=BdCusttype.class, attrName="pkCusttype", alias="u5",
			on="u5.pk_custtype = a.pk_custtype", columns={
				@Column(name="pk_custtype", label="主键", isPK=true),
				@Column(name="code", label="编码", isQuery=true),
				@Column(name="name", label="名称", isQuery=true),
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=BdPsndoc.class, attrName="pkPsndoc", alias="u6",
			on="u6.pk_psndoc = a.pk_psndoc", columns={
				@Column(name="pk_psndoc", label="主键", isPK=true),
				@Column(name="code", label="编码", isQuery=true),
				@Column(name="name", label="名称", isQuery=true),
		}),
	
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="createBy", alias="u11",
			on="u11.user_code = a.create_by", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="updateBy", alias="u13",
			on="u13.user_code = a.update_by", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
	}, extWhereKeys="dsf", orderBy="a.code "
)
public class BdCustomer extends DataEntity<BdCustomer> {
	
	private static final long serialVersionUID = 1L;
	private String pkCustomer;		// 主键
	@ExcelField(title="所属组织", attrName="pkOrg",align=ExcelField.Align.CENTER, sort=1,fieldType=OfficeType.class)
	private Office pkOrg;		// 所属组织
	@ExcelField(title="客户编码", attrName="code", align=ExcelField.Align.CENTER, sort=4)
	private String code;		// 客户编码
	@ExcelField(title="客户名称", attrName="name", align=ExcelField.Align.CENTER, sort=5)
	private String name;		// 客户名称
	@ExcelField(title="客户类型", attrName="pkCusttype",align=ExcelField.Align.CENTER, sort=10,fieldType=BdCusttypeObj.class)
	private BdCusttype pkCusttype;		// 客户类型
	@ExcelField(title="业务员", attrName="pkPsndoc",align=ExcelField.Align.CENTER, sort=20,fieldType=BdPsndocObj.class)
	private BdPsndoc pkPsndoc;		// 业务员
	@ExcelField(title="身份证号", attrName="idno", align=ExcelField.Align.CENTER, sort=30)
	private String idno;		// 身份证号
	@ExcelField(title="社会统一信用代码", attrName="creditcode", align=ExcelField.Align.CENTER, sort=40)
	private String creditcode;		// 社会统一信用代码
	@ExcelField(title="客户状态", attrName="custstatus",dictType="wg_custstatus", align=ExcelField.Align.CENTER, sort=50)
	private String custstatus;		// 客户状态
	private List<BdCustomerKpxx> bdCustomerKpxxList = ListUtils.newArrayList();		// 子表列表
	private List<BdCustomerProj> bdCustomerProjList = ListUtils.newArrayList();		// 子表列表
	
	public BdCustomer() {
		this(null);
	}

	public BdCustomer(String id){
		super(id);
	}
	
	public String getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(String pkCustomer) {
		this.pkCustomer = pkCustomer;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	@Length(min=0, max=255, message="客户编码长度不能超过 255 个字符")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=255, message="客户名称长度不能超过 255 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public BdCusttype getPkCusttype() {
		return pkCusttype;
	}

	public void setPkCusttype(BdCusttype pkCusttype) {
		this.pkCusttype = pkCusttype;
	}
	
	public BdPsndoc getPkPsndoc() {
		return pkPsndoc;
	}

	public void setPkPsndoc(BdPsndoc pkPsndoc) {
		this.pkPsndoc = pkPsndoc;
	}
	
	@Length(min=0, max=255, message="身份证号长度不能超过 255 个字符")
	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}
	
	@Length(min=0, max=255, message="社会统一信用代码长度不能超过 255 个字符")
	public String getCreditcode() {
		return creditcode;
	}

	public void setCreditcode(String creditcode) {
		this.creditcode = creditcode;
	}
	
	@Length(min=0, max=255, message="客户状态长度不能超过 255 个字符")
	public String getCuststatus() {
		return custstatus;
	}

	public void setCuststatus(String custstatus) {
		this.custstatus = custstatus;
	}
	
	public List<BdCustomerKpxx> getBdCustomerKpxxList() {
		return bdCustomerKpxxList;
	}

	public void setBdCustomerKpxxList(List<BdCustomerKpxx> bdCustomerKpxxList) {
		this.bdCustomerKpxxList = bdCustomerKpxxList;
	}
	
	public List<BdCustomerProj> getBdCustomerProjList() {
		return bdCustomerProjList;
	}

	public void setBdCustomerProjList(List<BdCustomerProj> bdCustomerProjList) {
		this.bdCustomerProjList = bdCustomerProjList;
	}
	
}