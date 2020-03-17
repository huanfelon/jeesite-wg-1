/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.modules.sys.entity.User;

/**
 * 合同税率变更Entity
 * @author LY
 * @version 2019-12-17
 */
@Table(name="wg_contract_tax", alias="a", columns={
		@Column(name="pk_contract_tax", attrName="pkContractTax", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract", label="合同主键"),
		@Column(name="htcode", attrName="htcode", label="合同编号"),
		@Column(name="dstartdate", attrName="dstartdate", label="开始日期"),
		@Column(name="ntaxrate", attrName="ntaxrate", label="合同税率"),
		@Column(name="nkpmny", attrName="nkpmny", label="开票金额"),
		@Column(name="changer", attrName="changer.userCode", label="操作人"),
		@Column(name="dchangedate", attrName="dchangedate", label="操作日期"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="changer", alias="u1",
		on="u1.user_code = a.changer", columns={
			@Column(name="user_code", label="用户编码", isPK=true, isQuery=false),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
}, orderBy=" a.dchangedate desc"
)
public class WgContractTax extends DataEntity<WgContractTax> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pkContractTax;		//主键
	private String pkContract;		// 合同主键
	private String htcode;		// 合同编号
	private Date dstartdate;		// 开始日期
	private Integer ntaxrate;		// 合同税率
	private Double nkpmny;		//开票金额
	private User changer;		// 制单人
	private Date dchangedate;		// 制单时间
	
	public WgContractTax() {
		this(null);
	}

	public WgContractTax(String id){
		super(id);
	}
	
	public String getPkContract() {
		return pkContract;
	}

	public void setPkContract(String pkContract) {
		this.pkContract = pkContract;
	}
	
	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
	}

	public String getPkContractTax() {
		return pkContractTax;
	}

	public void setPkContractTax(String pkContractTax) {
		this.pkContractTax = pkContractTax;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}

	public Integer getNtaxrate() {
		return ntaxrate;
	}

	public void setNtaxrate(Integer ntaxrate) {
		this.ntaxrate = ntaxrate;
	}

	public Double getNkpmny() {
		return nkpmny;
	}

	public void setNkpmny(Double nkpmny) {
		this.nkpmny = nkpmny;
	}

	public User getChanger() {
		return changer;
	}

	public void setChanger(User changer) {
		this.changer = changer;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDchangedate() {
		return dchangedate;
	}

	public void setDchangedate(Date dchangedate) {
		this.dchangedate = dchangedate;
	}
	
}