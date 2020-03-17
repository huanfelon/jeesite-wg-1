/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 开票申请Entity
 * @author tcl
 * @version 2019-11-11
 */
@Table(name="ct_invoiceapply_kpxx", alias="a", columns={
		@Column(name="pk_invoiceapply_kpxx", attrName="pkInvoiceapplyKpxx", label="主键", isPK=true),
		@Column(name="pk_invoiceapply", attrName="pkInvoiceapply.pkInvoiceapply", label="父主键", isQuery=true),
		@Column(name="pk_org", attrName="pkOrg", label="公司名称"),
		@Column(name="taxno", attrName="taxno", label="税号"),
		@Column(name="addr", attrName="addr", label="地址"),
		@Column(name="phone", attrName="phone", label="电话"),
		@Column(name="bankname", attrName="bankname", label="开户行"),
		@Column(name="accnum", attrName="accnum", label="账户"),
		@Column(name="ts", attrName="ts", label="时间戳", isQuery=true),
		@Column(name="dr", attrName="dr", label="删除标识", isQuery=true),
		@Column(name="vsrcid", attrName="vsrcid", label="来源单据id", isQuery=false),
	}, orderBy="a.pk_invoiceapply_kpxx ASC"
)
public class CtInvoiceapplyKpxx extends DataEntity<CtInvoiceapplyKpxx> {
	
	private static final long serialVersionUID = 1L;
	private String pkInvoiceapplyKpxx;		// 主键
	private CtInvoiceapply pkInvoiceapply;		// 父主键 父类
	private String pkOrg;		// 公司名称
	private String taxno;		// 税号
	private String addr;		// 地址
	private String phone;		// 电话
	private String bankname;		// 开户行
	private String accnum;		// 账户
	private Date ts;		// 时间戳
	private Integer dr;		// 删除标识
	private String vsrcid;		// 来源单据id
	
	public CtInvoiceapplyKpxx() {
		this(null);
	}


	public CtInvoiceapplyKpxx(CtInvoiceapply pkInvoiceapply){
		this.pkInvoiceapply = pkInvoiceapply;
	}
	
	public String getPkInvoiceapplyKpxx() {
		return pkInvoiceapplyKpxx;
	}

	public void setPkInvoiceapplyKpxx(String pkInvoiceapplyKpxx) {
		this.pkInvoiceapplyKpxx = pkInvoiceapplyKpxx;
	}
	
	@Length(min=0, max=255, message="父主键长度不能超过 255 个字符")
	public CtInvoiceapply getPkInvoiceapply() {
		return pkInvoiceapply;
	}

	public void setPkInvoiceapply(CtInvoiceapply pkInvoiceapply) {
		this.pkInvoiceapply = pkInvoiceapply;
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