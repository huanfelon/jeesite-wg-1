/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.Office;

/**
 * 开票登记Entity
 * @author tcl
 * @version 2019-11-11
 */
@Table(name="ct_invoiceregister_kpxx", alias="a", columns={
		@Column(name="pk_invoiceregister_kpxx", attrName="pkInvoiceregisterKpxx", label="主键", isPK=true),
		@Column(name="pk_invoiceregister", attrName="pkInvoiceregister.id", label="父主键", isQuery=true),
		@Column(name="pk_org", attrName="pkOrg", label="公司名称"),
		@Column(name="taxno", attrName="taxno", label="税号"),
		@Column(name="addr", attrName="addr", label="地址"),
		@Column(name="phone", attrName="phone", label="电话"),
		@Column(name="bankname", attrName="bankname", label="开户行"),
		@Column(name="accnum", attrName="accnum", label="账户"),
		@Column(name="ts", attrName="ts", label="时间戳"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="vsrcid", attrName="vsrcid", label="来源单据id"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u3",
			on="u3.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
		}, orderBy="a.pk_invoiceregister_kpxx ASC"
)
public class CtInvoiceregisterKpxx extends DataEntity<CtInvoiceregisterKpxx> {
	
	private static final long serialVersionUID = 1L;
	private String pkInvoiceregisterKpxx;		// 主键
	private CtInvoiceregister pkInvoiceregister;		// 父主键 父类
	private String pkOrg;		// 公司名称
	private String taxno;		// 税号
	private String addr;		// 地址
	private String phone;		// 电话
	private String bankname;		// 开户行
	private String accnum;		// 账户
	private Date ts;		// 时间戳
	private Integer dr;		// 删除标识
	private String vsrcid;		// 来源单据id
	
	public CtInvoiceregisterKpxx() {
		this(null);
	}


	public CtInvoiceregisterKpxx(CtInvoiceregister pkInvoiceregister){
		this.pkInvoiceregister = pkInvoiceregister;
	}
	
	public String getPkInvoiceregisterKpxx() {
		return pkInvoiceregisterKpxx;
	}

	public void setPkInvoiceregisterKpxx(String pkInvoiceregisterKpxx) {
		this.pkInvoiceregisterKpxx = pkInvoiceregisterKpxx;
	}
	
	@Length(min=0, max=255, message="父主键长度不能超过 255 个字符")
	public CtInvoiceregister getPkInvoiceregister() {
		return pkInvoiceregister;
	}

	public void setPkInvoiceregister(CtInvoiceregister pkInvoiceregister) {
		this.pkInvoiceregister = pkInvoiceregister;
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	public Integer getDr() {
		sqlMap.getWhere().and("dr", QueryType.EQ, 0);
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	
	@Length(min=0, max=255, message="来源单据id长度不能超过 255 个字符")
	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}
	
}