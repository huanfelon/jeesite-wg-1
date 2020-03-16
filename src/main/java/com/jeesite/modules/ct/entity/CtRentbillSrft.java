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
import com.jeesite.modules.bd.entity.BdProject;

/**
 * 租约账单Entity
 * @author tcl
 * @version 2019-11-08
 */
@Table(name="ct_rentbill_srft", alias="a", columns={
		@Column(name="pk_rentbill_srft", attrName="pkRentbillSrft", label="主键", isPK=true),
		@Column(name="pk_rentbill", attrName="pkRentbill.pkRentbill", label="父主键", isQuery=true),
		@Column(name="is_qc", attrName="isQc", label="是否期初"),
		@Column(name="pk_ysproject", attrName="pkYsproject.id", label="预算项目"),
		@Column(name="dstartdate", attrName="dstartdate", label="费用开始日期"),
		@Column(name="denddate", attrName="denddate", label="费用截止日期"),
		@Column(name="kjqj", attrName="kjqj", label="会计年月"),
		@Column(name="nplanmny", attrName="nplanmny", label="计划确认金额"),
		@Column(name="nyhmny", attrName="nyhmny", label="优惠金额"),
		@Column(name="nyqrmny", attrName="nyqrmny", label="应确认收入金额"),
		@Column(name="taxrate", attrName="taxrate", label="税率"),
		@Column(name="ntaxmny", attrName="ntaxmny", label="税额"),
		@Column(name="nnotaxmny", attrName="nnotaxmny", label="应确认收入无税金额"),
		@Column(name="vmemo", attrName="vmemo", label="备注"),
		@Column(name="vsrcid", attrName="vsrcid", label="来源单据id", isQuery=false),
		@Column(name="vywcfid", attrName="vywcfid", label="来源单据id", isQuery=false),
		@Column(name="vsrctbname", attrName="vsrctbname", label="来源合同业务表名", isQuery=false),
		@Column(name="ts", attrName="ts", label="时间戳", isQuery=true),
		@Column(name="dr", attrName="dr", label="删除标识", isQuery=true),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=BdProject.class, attrName="pkYsproject", alias="u4",
			on="u4.pk_project = a.pk_ysproject", columns={
			@Column(name="pk_project", label="", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		}, orderBy="a.dstartdate "
)
public class CtRentbillSrft extends DataEntity<CtRentbillSrft> {
	
	private static final long serialVersionUID = 1L;
	private String pkRentbillSrft;		// 主键
	private CtRentbill pkRentbill;		// 父主键 父类
	private String isQc;		// 是否期初
	private BdProject pkYsproject;		// 预算项目
	private Date dstartdate;		// 费用开始日期
	private Date denddate;		// 费用截止日期
	private String kjqj;		// 会计年月
	private Double nyqrmny;		// 应确认收入金额
	private Double nplanmny;		// 计划金额
	private Double nyhmny;		// 优惠金额
	private Double taxrate;		// 税率
	private Double ntaxmny;		// 税额
	private Double nnotaxmny;		// 应确认收入无税金额
	private String vmemo;		// 备注
	private String vsrcid;		// 来源单据id及合同收入页签id
	private String vywcfid;		// 来源单据id及合同业务页签id
	private String vsrctbname;		// 来源合同页签表名
	private Date ts;		// 时间戳
	private Integer dr;		// 删除标识
	
	public CtRentbillSrft() {
		this(null);
	}


	public CtRentbillSrft(CtRentbill pkRentbill){
		this.pkRentbill = pkRentbill;
	}
	
	public String getPkRentbillSrft() {
		return pkRentbillSrft;
	}

	public void setPkRentbillSrft(String pkRentbillSrft) {
		this.pkRentbillSrft = pkRentbillSrft;
	}
	
	@Length(min=0, max=255, message="父主键长度不能超过 255 个字符")
	public CtRentbill getPkRentbill() {
		return pkRentbill;
	}

	public void setPkRentbill(CtRentbill pkRentbill) {
		this.pkRentbill = pkRentbill;
	}
	
	@Length(min=0, max=1, message="是否期初长度不能超过 1 个字符")
	public String getIsQc() {
		return isQc;
	}

	public void setIsQc(String isQc) {
		this.isQc = isQc;
	}
	
	public BdProject getPkYsproject() {
		return pkYsproject;
	}

	public void setPkYsproject(BdProject pkYsproject) {
		this.pkYsproject = pkYsproject;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}
	
	@Length(min=0, max=255, message="会计年月长度不能超过 255 个字符")
	public String getKjqj() {
		return kjqj;
	}

	public void setKjqj(String kjqj) {
		this.kjqj = kjqj;
	}
	
	public Double getNyqrmny() {
		return nyqrmny;
	}

	public void setNyqrmny(Double nyqrmny) {
		this.nyqrmny = nyqrmny;
	}
	
	public Double getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}
	
	public Double getNtaxmny() {
		return ntaxmny;
	}

	public void setNtaxmny(Double ntaxmny) {
		this.ntaxmny = ntaxmny;
	}
	
	public Double getNnotaxmny() {
		return nnotaxmny;
	}

	public void setNnotaxmny(Double nnotaxmny) {
		this.nnotaxmny = nnotaxmny;
	}
	
	@Length(min=0, max=255, message="备注长度不能超过 255 个字符")
	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	
	@Length(min=0, max=255, message="来源单据id长度不能超过 255 个字符")
	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
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


	public Double getNplanmny() {
		return nplanmny;
	}


	public void setNplanmny(Double nplanmny) {
		this.nplanmny = nplanmny;
	}


	public Double getNyhmny() {
		return nyhmny;
	}


	public void setNyhmny(Double nyhmny) {
		this.nyhmny = nyhmny;
	}


	public String getVywcfid() {
		return vywcfid;
	}


	public void setVywcfid(String vywcfid) {
		this.vywcfid = vywcfid;
	}


	public String getVsrctbname() {
		return vsrctbname;
	}


	public void setVsrctbname(String vsrctbname) {
		this.vsrctbname = vsrctbname;
	}
	
}