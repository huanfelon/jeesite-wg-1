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
 * 开票申请Entity
 * 
 * @author tcl
 * @version 2019-11-11
 */
@Table(name = "ct_invoiceapply_skmx", alias = "a", columns = {
		@Column(name = "pk_invoiceapply_skmx", attrName = "pkInvoiceapplySkmx", label = "主键", isPK = true),
		@Column(name = "pk_invoiceapply", attrName = "pkInvoiceapply.pkInvoiceapply", label = "父主键", isQuery = true),
		@Column(name = "pk_customer", attrName = "pkCustomer.id", label = "客户名称"),
		@Column(name = "pk_building", attrName = "pkBuilding.id", label = "楼栋"),
		@Column(name = "pk_house", attrName = "pkHouse.id", label = "房产信息"),
		@Column(name = "pk_ysproject", attrName = "pkYsproject.id", label = "收费项目"),
		@Column(name = "kpcostp", attrName = "kpcostp.id", label = "开票项目"),
		@Column(name = "dstartdate", attrName = "dstartdate", label = "费用开始日期"),
		@Column(name = "denddate", attrName = "denddate", label = "费用截止日期"),
		@Column(name = "kjqj", attrName = "kjqj", label = "会计年月"),
		@Column(name = "nsykpmny", attrName = "nsykpmny", label = "剩余开票金额"),
		@Column(name = "nsqkpmny", attrName = "nsqkpmny", label = "申请开票金额"),
		@Column(name = "taxrate", attrName = "taxrate", label = "税率"),
		@Column(name = "ntaxmny", attrName = "ntaxmny", label = "税额"),
		@Column(name = "nnotaxmny", attrName = "nnotaxmny", label = "无税金额"),
		@Column(name = "nkpdjmny", attrName = "nkpdjmny", label = "开票登记金额"),
		@Column(name = "vmemo", attrName = "vmemo", label = "备注"),
		@Column(name = "ts", attrName = "ts", label = "时间戳", isQuery = true),
		@Column(name = "dr", attrName = "dr", label = "删除标识", isQuery = true),
		@Column(name = "vsrcid", attrName = "vsrcid", label = "来源单据id", isQuery = true), 
	}, joinTable = {
		@JoinTable(type = Type.LEFT_JOIN, entity = BdCustomer.class, attrName = "pkCustomer", alias = "u5",
		on = "u5.pk_customer = a.pk_customer", columns = {
				@Column(name = "pk_customer", label = "", isPK = true, isQuery = false),
				@Column(name = "code", label = "编码", isQuery = false),
				@Column(name = "name", label = "名称", isQuery = false), }),
		@JoinTable(type = Type.LEFT_JOIN, entity = ZlBuildingfile.class, attrName = "pkBuilding", alias = "u7",
		on = "u7.pk_buildingfile = a.pk_building", columns = {
				@Column(name = "pk_buildingfile", label = "", isPK = true, isQuery = false),
				@Column(name = "code", label = "编码", isQuery = false),
				@Column(name = "name", label = "名称", isQuery = false), }),
		@JoinTable(type = Type.LEFT_JOIN, entity = ZlHousesource.class, attrName = "pkHouse", alias = "u8",
		on = "u8.pk_housesource = a.pk_house", columns = {
				@Column(name = "pk_housesource", label = "", isPK = true, isQuery = false),
				@Column(name = "estatecode", label = "编码", isQuery = false),
				@Column(name = "estatename", label = "名称", isQuery = false), }),
		@JoinTable(type = Type.LEFT_JOIN, entity = BdProject.class, attrName = "pkYsproject", alias = "u4",
		on = "u4.pk_project = a.pk_ysproject", columns = {
				@Column(name = "pk_project", label = "", isPK = true, isQuery = false),
				@Column(name = "code", label = "编码", isQuery = false),
				@Column(name = "name", label = "名称", isQuery = false), }),
		@JoinTable(type = Type.LEFT_JOIN, entity = BdProject.class, attrName = "kpcostp", alias = "kpc",
		on = "kpc.pk_project = a.kpcostp", columns = {
				@Column(name = "pk_project", label = "", isPK = true, isQuery = false),
				@Column(name = "code", label = "编码", isQuery = false),
				@Column(name = "name", label = "名称", isQuery = false), }),
	}, orderBy = "a.pk_invoiceapply_skmx ASC")

public class CtInvoiceapplySkmx extends DataEntity<CtInvoiceapplySkmx> {

	private static final long serialVersionUID = 1L;
	private String pkInvoiceapplySkmx; // 主键
	private CtInvoiceapply pkInvoiceapply; // 父主键 父类
	private BdCustomer pkCustomer; // 客户名称
	private ZlBuildingfile pkBuilding; // 楼栋
	private ZlHousesource pkHouse; // 房产信息
	private BdProject pkYsproject; // 收费项目
	private BdProject kpcostp;		//开票项目
	private Date dstartdate; // 费用开始日期
	private Date denddate; // 费用截止日期
	private String kjqj; // 会计年月
	private Double nsqkpmny; // 申请开票金额
	private Double taxrate; // 税率
	private Double ntaxmny; // 税额
	private Double nnotaxmny; // 无税金额
	private String vmemo; // 备注
	private Date ts; // 时间戳
	private Integer dr; // 删除标识
	private String vsrcid; // 来源单据id
	private Double nsykpmny;
	private Double nkpdjmny;

	public CtInvoiceapplySkmx() {
		this(null);
	}

	public CtInvoiceapplySkmx(CtInvoiceapply pkInvoiceapply) {
		this.pkInvoiceapply = pkInvoiceapply;
	}

	public String getPkInvoiceapplySkmx() {
		return pkInvoiceapplySkmx;
	}

	public void setPkInvoiceapplySkmx(String pkInvoiceapplySkmx) {
		this.pkInvoiceapplySkmx = pkInvoiceapplySkmx;
	}

	public CtInvoiceapply getPkInvoiceapply() {
		return pkInvoiceapply;
	}

	public void setPkInvoiceapply(CtInvoiceapply pkInvoiceapply) {
		this.pkInvoiceapply = pkInvoiceapply;
	}

	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}

	public ZlBuildingfile getPkBuilding() {
		return pkBuilding;
	}

	public void setPkBuilding(ZlBuildingfile pkBuilding) {
		this.pkBuilding = pkBuilding;
	}

	public ZlHousesource getPkHouse() {
		return pkHouse;
	}

	public void setPkHouse(ZlHousesource pkHouse) {
		this.pkHouse = pkHouse;
	}

	public BdProject getPkYsproject() {
		return pkYsproject;
	}

	public void setPkYsproject(BdProject pkYsproject) {
		this.pkYsproject = pkYsproject;
	}

	public BdProject getKpcostp() {
		return kpcostp;
	}

	public void setKpcostp(BdProject kpcostp) {
		this.kpcostp = kpcostp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}

	public String getKjqj() {
		return kjqj;
	}

	public void setKjqj(String kjqj) {
		this.kjqj = kjqj;
	}

	public Double getNsqkpmny() {
		return nsqkpmny;
	}

	public void setNsqkpmny(Double nsqkpmny) {
		this.nsqkpmny = nsqkpmny;
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

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
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

	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}

	public Double getNsykpmny() {
		return nsykpmny;
	}

	public void setNsykpmny(Double nsykpmny) {
		this.nsykpmny = nsykpmny;
	}

	public Double getNkpdjmny() {
		return nkpdjmny;
	}

	public void setNkpdjmny(Double nkpdjmny) {
		this.nkpdjmny = nkpdjmny;
	}

}