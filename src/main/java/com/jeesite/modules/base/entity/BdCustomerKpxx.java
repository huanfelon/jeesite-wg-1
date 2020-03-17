/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 客户信息中心Entity
 * @author tcl
 * @version 2019-11-05
 */
@Table(name="bd_customer_kpxx", alias="a", columns={
		@Column(name="pk_customer_kpxx", attrName="pkCustomerKpxx", label="主键", isPK=true),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="父主键", isQuery=true),
		@Column(name="pk_org", attrName="pkOrg", label="公司名称"),
		@Column(name="taxno", attrName="taxno", label="税号", isQuery=false),
		@Column(name="addr", attrName="addr", label="地址", isQuery=false),
		@Column(name="phone", attrName="phone", label="电话", isQuery=false),
		@Column(name="bankname", attrName="bankname", label="开户行", isQuery=false),
		@Column(name="accnum", attrName="accnum", label="账户", isQuery=false),
	}, orderBy="a.pk_customer_kpxx ASC"
)
public class BdCustomerKpxx extends DataEntity<BdCustomerKpxx> {
	
	private static final long serialVersionUID = 1L;
	private String pkCustomerKpxx;		// 主键
	private BdCustomer pkCustomer;		// 父主键 父类
	private String pkOrg;		// 公司名称
	private String taxno;		// 税号
	private String addr;		// 地址
	private String phone;		// 电话
	private String bankname;		// 开户行
	private String accnum;		// 账户
	
	public BdCustomerKpxx() {
		this(null);
	}


	public BdCustomerKpxx(BdCustomer pkCustomer){
		this.pkCustomer = pkCustomer;
	}
	
	public String getPkCustomerKpxx() {
		return pkCustomerKpxx;
	}

	public void setPkCustomerKpxx(String pkCustomerKpxx) {
		this.pkCustomerKpxx = pkCustomerKpxx;
	}
	
	@Length(min=0, max=255, message="父主键长度不能超过 255 个字符")
	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}
	
	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	@Length(min=0, max=255, message="税号长度不能超过 255 个字符")
	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}
	
	@Length(min=0, max=255, message="地址长度不能超过 255 个字符")
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@Length(min=0, max=255, message="电话长度不能超过 255 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=255, message="开户行长度不能超过 255 个字符")
	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	
	@Length(min=0, max=255, message="账户长度不能超过 255 个字符")
	public String getAccnum() {
		return accnum;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	
}