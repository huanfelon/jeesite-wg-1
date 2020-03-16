/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdCustomer;

/**
 * 合同管理表体-客户信息页签Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_cust", alias="a", columns={
		@Column(name="pk_contract_cust", attrName="pkContractCust", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="idno", attrName="idno", label="身份证号"),
		@Column(name="shtyxydm", attrName="shtyxydm", label="社会统一信用代码"),
		@Column(name="taxnum", attrName="taxnum", label="税号"),
		@Column(name="address", attrName="address", label="地址"),
		@Column(name="tel", attrName="tel", label="电话"),
		@Column(name="openbank", attrName="openbank", label="开户行"),
		@Column(name="accountnum", attrName="accountnum", label="账户"),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=BdCustomer.class, attrName="pkCustomer", alias="c1",
		on="c1.pk_customer = a.pk_customer", columns={
			@Column(name="pk_customer", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="客户编码", isQuery=false),
			@Column(name="name", label="客户名称", isQuery=false),
		}),
}, orderBy="a.pk_contract_cust ASC"
)
public class WgContractCust extends DataEntity<WgContractCust> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractCust;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private BdCustomer pkCustomer;		// 客户名称
	private String idno;		// 身份证号
	private String shtyxydm;		// 社会统一信用代码
	private String taxnum;		// 税号
	private String address;		// 地址
	private String tel;		// 电话
	private String openbank;		// 开户行
	private String accountnum;		// 账户
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractCust() {
		this(null);
	}

	public WgContractCust(WgContract pkContract){
		this.pkContract = pkContract;
	}
	
	public String getPkContractCust() {
		return pkContractCust;
	}

	public void setPkContractCust(String pkContractCust) {
		this.pkContractCust = pkContractCust;
	}
	
	public WgContract getPkContract() {
		return pkContract;
	}

	public void setPkContract(WgContract pkContract) {
		this.pkContract = pkContract;
	}
	
	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}
	
	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}
	
	public String getShtyxydm() {
		return shtyxydm;
	}

	public void setShtyxydm(String shtyxydm) {
		this.shtyxydm = shtyxydm;
	}
	
	public String getTaxnum() {
		return taxnum;
	}

	public void setTaxnum(String taxnum) {
		this.taxnum = taxnum;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getOpenbank() {
		return openbank;
	}

	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}
	
	public String getAccountnum() {
		return accountnum;
	}

	public void setAccountnum(String accountnum) {
		this.accountnum = accountnum;
	}
	
	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	
	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	
	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}
	
	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}
	
	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}
	
	public Integer getDr() {
		sqlMap.getWhere().and("dr", QueryType.EQ, "0");
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
}