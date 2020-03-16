/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 收入确认Entity
 * @author GJ
 * @version 2019-11-07
 */
@Table(name="ct_charge_srqr_b", alias="a", columns={
		@Column(name="pk_charge_srqr_b", attrName="pkChargeSrqrB", label="主键", isPK=true),
		@Column(name="pk_charge_srqr", attrName="pkChargeSrqr.pkChargeSrqr", label="父主键"),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="pk_buildingfile", attrName="pkBuild.pkBuildingfile", label="楼栋"),
		@Column(name="pk_housesource", attrName="pkHouse.pkHousesource", label="房产名称"),
		@Column(name="pk_sf_project", attrName="pkSfProject.pkProject", label="收费项目"),
		@Column(name="fyksdate", attrName="fyksdate", label="费用开始日期"),
		@Column(name="fyjzdate", attrName="fyjzdate", label="费用截止日期"),
		@Column(name="kjyears", attrName="kjyears", label="会计年月"),
		@Column(name="nyqrsrmny", attrName="nyqrsrmny", label="应确认收入金额"),
		@Column(name="nbcsrqrmny", attrName="nbcsrqrmny", label="本次收入确认金额"),
		@Column(name="tax_rate", attrName="taxRate", label="税率"),
		@Column(name="no_tax_amount", attrName="noTaxAmount", label="本次确认收入无税金额"),
		@Column(name="tax_amount", attrName="taxAmount", label="本次确认收入税额"),
		@Column(name="nqsmny", attrName="nqsmny", label="欠收金额"),
		@Column(name="dr", attrName="dr", label="dr"),
		@Column(name="ts", attrName="ts", label="ts"),
		@Column(name="remarks", attrName="remarks", label="备注", queryType=QueryType.LIKE),
		@Column(name="lyvbillno", attrName="lyvbillno", label="来源单据号"),
		@Column(name="vsrcid2", attrName="vsrcid2", label="来源账单单据表头id"),
		@Column(name="vsrcid2name", attrName="vsrcid2name", label="来源合同页签名称"),
	},joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlBuildingfile.class, attrName="pkBuild", alias="u14",
					on="u14.pk_buildingfile = a.pk_buildingfile", columns={
						@Column(name="pk_buildingfile", label="机构编码", isPK=true),
						@Column(name="code", attrName="code", label="编码"),
						@Column(name="name", label="机构名称", isQuery=false),
					}),
					@JoinTable(type=Type.LEFT_JOIN, entity=BdProject.class, attrName="pkSfProject", alias="u15",
					on="u15.pk_project = a.pk_sf_project", columns={
						@Column(name="pk_project", label="收费项目主键", isPK=true),
						@Column(name="code", attrName="code", label="收费项目编码"),
						@Column(name="name", label="机构名称", isQuery=false),
					}),
					@JoinTable(type=Type.LEFT_JOIN, entity=ZlHousesource.class, attrName="pkHouse", alias="u16",
					on="u16.pk_housesource = a.pk_housesource", columns={
						@Column(name="pk_housesource", label="房源主键", isPK=true),
						@Column(name="estatecode", attrName="code", label="房源编码"),
						@Column(name="estatename", label="房源名称", isQuery=false),
					}),
					@JoinTable(type=Type.LEFT_JOIN, entity=BdCustomer.class, attrName="pkCustomer", alias="u17",
					on="u17.pk_customer = a.pk_customer", columns={
						@Column(name="pk_customer", label="客户名称主键", isPK=true),
						@Column(name="code", attrName="code", label="客户名称编码"),
						@Column(name="name", label="客户名称", isQuery=false),
					}),
				},  orderBy="a.pk_charge_srqr_b ASC"
)
public class CtChargeSrqrB extends DataEntity<CtChargeSrqrB> {
	
	private static final long serialVersionUID = 1L;
	private String pkChargeSrqrB;		// 主键
	private CtChargeSrqr pkChargeSrqr;		// 父主键 父类
	private BdCustomer pkCustomer;		// 客户名称
	private ZlBuildingfile pkBuild;		// 楼栋
	private ZlHousesource pkHouse;		// 房产名称
	private BdProject pkSfProject;		// 收费项目
	private Date fyksdate;		// 费用开始日期
	private Date fyjzdate;		// 费用截止日期
	private Date kjyears;		// 会计年月
	private Double nyqrsrmny;		// 应确认收入金额
	private Double nbcsrqrmny;		// 本次收入确认金额
	private Double taxRate;		// 税率
	private Double noTaxAmount;		// 本次确认收入无税金额
	private Double taxAmount;		// 本次确认收入税额
	private Double nqsmny;		// 欠收金额
	private Integer dr;		// dr
	private Date ts;		// ts
	private String lyvbillno;		// 来源单据号
	private String vsrcid2;
	private String vsrcid2name;
	
	public CtChargeSrqrB() {
		this(null);
	}


	public CtChargeSrqrB(CtChargeSrqr pkChargeSrqr){
		this.pkChargeSrqr = pkChargeSrqr;
	}
	
	public String getPkChargeSrqrB() {
		return pkChargeSrqrB;
	}

	public void setPkChargeSrqrB(String pkChargeSrqrB) {
		this.pkChargeSrqrB = pkChargeSrqrB;
	}
	
	public CtChargeSrqr getPkChargeSrqr() {
		return pkChargeSrqr;
	}

	public void setPkChargeSrqr(CtChargeSrqr pkChargeSrqr) {
		this.pkChargeSrqr = pkChargeSrqr;
	}
	

	
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getFyksdate() {
		return fyksdate;
	}

	public void setFyksdate(Date fyksdate) {
		this.fyksdate = fyksdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getFyjzdate() {
		return fyjzdate;
	}

	public void setFyjzdate(Date fyjzdate) {
		this.fyjzdate = fyjzdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM")
	public Date getKjyears() {
		return kjyears;
	}

	public void setKjyears(Date kjyears) {
		this.kjyears = kjyears;
	}
	
	public Double getNyqrsrmny() {
		return nyqrsrmny;
	}

	public void setNyqrsrmny(Double nyqrsrmny) {
		this.nyqrsrmny = nyqrsrmny;
	}
	
	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	
	public Double getNoTaxAmount() {
		return noTaxAmount;
	}

	public void setNoTaxAmount(Double noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}
	
	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	public Integer getDr() {
		sqlMap.getWhere().and("dr", QueryType.EQ, 0);
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}


	public String getLyvbillno() {
		return lyvbillno;
	}


	public void setLyvbillno(String lyvbillno) {
		this.lyvbillno = lyvbillno;
	}


	public ZlBuildingfile getPkBuild() {
		return pkBuild;
	}


	public void setPkBuild(ZlBuildingfile pkBuild) {
		this.pkBuild = pkBuild;
	}


	public ZlHousesource getPkHouse() {
		return pkHouse;
	}


	public void setPkHouse(ZlHousesource pkHouse) {
		this.pkHouse = pkHouse;
	}


	public BdProject getPkSfProject() {
		return pkSfProject;
	}


	public void setPkSfProject(BdProject pkSfProject) {
		this.pkSfProject = pkSfProject;
	}


	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}


	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}


	public Double getNqsmny() {
		return nqsmny;
	}


	public void setNqsmny(Double nqsmny) {
		this.nqsmny = nqsmny;
	}


	public Double getNbcsrqrmny() {
		return nbcsrqrmny;
	}


	public void setNbcsrqrmny(Double nbcsrqrmny) {
		this.nbcsrqrmny = nbcsrqrmny;
	}


	public String getVsrcid2() {
		return vsrcid2;
	}


	public void setVsrcid2(String vsrcid2) {
		this.vsrcid2 = vsrcid2;
	}


	public String getVsrcid2name() {
		return vsrcid2name;
	}


	public void setVsrcid2name(String vsrcid2name) {
		this.vsrcid2name = vsrcid2name;
	}



	
}