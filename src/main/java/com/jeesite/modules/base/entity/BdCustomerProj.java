/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 客户信息中心Entity
 * @author tcl
 * @version 2019-11-05
 */
@Table(name="bd_customer_proj", alias="a", columns={
		@Column(name="pk_customer_proj", attrName="pkCustomerProj", label="主键", isPK=true),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="父主键", isQuery=true),
		@Column(name="pk_project", attrName="pkProject.id", label="项目名称"),
		@Column(name="code", attrName="code", label="项目编码", isQuery=false),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="u3",
			on="u3.pk_project = a.pk_project", columns={
				@Column(name="pk_project", label="主键", isPK=true),
				@Column(name="code", label="编码", isQuery=true),
				@Column(name="name", label="名称", isQuery=true),
		}),
		}, orderBy="a.pk_customer_proj ASC"
)
public class BdCustomerProj extends DataEntity<BdCustomerProj> {
	
	private static final long serialVersionUID = 1L;
	private String pkCustomerProj;		// 主键
	private BdCustomer pkCustomer;		// 父主键 父类
	private ZlProject pkProject;		// 项目名称
	private String code;		// 项目编码
	
	public BdCustomerProj() {
		this(null);
	}


	public BdCustomerProj(BdCustomer pkCustomer){
		this.pkCustomer = pkCustomer;
	}
	
	public String getPkCustomerProj() {
		return pkCustomerProj;
	}

	public void setPkCustomerProj(String pkCustomerProj) {
		this.pkCustomerProj = pkCustomerProj;
	}
	
	@Length(min=0, max=255, message="父主键长度不能超过 255 个字符")
	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}
	
	public ZlProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(ZlProject pkProject) {
		this.pkProject = pkProject;
	}
	
	@Length(min=0, max=255, message="项目编码长度不能超过 255 个字符")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}