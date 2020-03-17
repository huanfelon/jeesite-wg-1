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
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.bd.entity.BdProject;

/**
 * 合同管理表体-押金页签Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_yj", alias="a", columns={
		@Column(name="pk_contract_yj", attrName="pkContractYj", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="pk_ysproject", attrName="pkYsproject.pkProject", label="预算项目"),
		@Column(name="drecdate", attrName="drecdate", label="应交日期"),
		@Column(name="nrecmny", attrName="nrecmny", label="应交金额"),
		@Column(name="nrealmny", attrName="nrealmny", label="已收金额"),
		@Column(name="nqsmny", attrName="nqsmny", label="欠收金额"),
		@Column(name="dygtdate", attrName="dygtdate", label="应退日期"),
		@Column(name="nygtmny", attrName="nygtmny", label="应退金额"),
		@Column(name="nyjtmny", attrName="nyjtmny", label="已退金额"),
		@Column(name="vmemo", attrName="vmemo", label="备注"),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=BdProject.class, attrName="pkYsproject", alias="cp1",
		on="cp1.pk_project = a.pk_ysproject", columns={
			@Column(name="pk_project", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
},extWhereKeys="dsf", orderBy="a.pk_contract_yj ASC"
)
public class WgContractYj extends DataEntity<WgContractYj> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractYj;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private BdCustomer pkCustomer;		// 客户名称
	private BdProject pkYsproject;		// 预算项目
	private Date drecdate;		// 应交日期
	private Double nrecmny;		// 应交金额
	private Double nrealmny;		// 已收金额
	private Double nqsmny;		// 欠收金额
	private Date dygtdate;		// 应退日期
	private Double nygtmny;		// 应退金额
	private Double nyjtmny;		// 已退金额
	private String vmemo;		// 备注
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractYj() {
		this(null);
	}


	public WgContractYj(WgContract pkContract){
		this.pkContract = pkContract;
	}
	
	public String getPkContractYj() {
		return pkContractYj;
	}

	public void setPkContractYj(String pkContractYj) {
		this.pkContractYj = pkContractYj;
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
	
	public BdProject getPkYsproject() {
		return pkYsproject;
	}

	public void setPkYsproject(BdProject pkYsproject) {
		this.pkYsproject = pkYsproject;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDrecdate() {
		return drecdate;
	}

	public void setDrecdate(Date drecdate) {
		this.drecdate = drecdate;
	}
	
	public Double getNrecmny() {
		return nrecmny;
	}

	public void setNrecmny(Double nrecmny) {
		this.nrecmny = nrecmny;
	}
	
	public Double getNrealmny() {
		return nrealmny;
	}

	public void setNrealmny(Double nrealmny) {
		this.nrealmny = nrealmny;
	}
	
	public Double getNqsmny() {
		return nqsmny;
	}

	public void setNqsmny(Double nqsmny) {
		this.nqsmny = nqsmny;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDygtdate() {
		return dygtdate;
	}

	public void setDygtdate(Date dygtdate) {
		this.dygtdate = dygtdate;
	}
	
	public Double getNygtmny() {
		return nygtmny;
	}

	public void setNygtmny(Double nygtmny) {
		this.nygtmny = nygtmny;
	}
	
	public Double getNyjtmny() {
		return nyjtmny;
	}

	public void setNyjtmny(Double nyjtmny) {
		this.nyjtmny = nyjtmny;
	}
	
	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
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