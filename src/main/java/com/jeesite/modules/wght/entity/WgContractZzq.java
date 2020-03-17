/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 合同管理表体-增长期页签Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_zzq", alias="a", columns={
		@Column(name="pk_contract_zzq", attrName="pkContractZzq", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="dstartdate", attrName="dstartdate", label="起始日期"),
		@Column(name="denddate", attrName="denddate", label="截止日期"),
		@Column(name="zzrate", attrName="zzrate", label="增长比例", comment="增长比例（%）"),
		@Column(name="nzzmny", attrName="nzzmny", label="增长金额"),
		@Column(name="nzzprice", attrName="nzzprice", label="增长后单价"),
		@Column(name="nzzdayp", attrName="nzzdayp", label="增长后日租金"),
		@Column(name="nzzmonthp", attrName="nzzmonthp", label="增长后月租金"),
		@Column(name="nzzmonthptz", attrName="nzzmonthptz", label="增长后月租金调整额", isUpdateForce=true),
		@Column(name="nzzmonthptzh", attrName="nzzmonthptzh", label="增长后月租金调整后金额"),
		@Column(name="nzzyearp", attrName="nzzyearp", label="增长后年租金"),
		@Column(name="nzzyearptz", attrName="nzzyearptz", label="增长后年租金调整额", isUpdateForce=true),
		@Column(name="nzzyearptzh", attrName="nzzyearptzh", label="增长后年租金调整后金额"),
		@Column(name="vmemo", attrName="vmemo", label="备注"),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
	}, orderBy="a.pk_contract_zzq ASC"
)
public class WgContractZzq extends DataEntity<WgContractZzq> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractZzq;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private Date dstartdate;		// 起始日期
	private Date denddate;		// 截止日期
	private Double zzrate;		// 增长比例（%）
	private Double nzzmny;		// 增长金额
	private Double nzzprice;		//增长后单价
	private Double nzzdayp;		// 增长后日租金
	private Double nzzmonthp;		// 增长后月租金
	private Double nzzmonthptz;		// 增长后月租金调整额
	private Double nzzmonthptzh;		// 增长后月租金调整后金额
	private Double nzzyearp;		// 增长后年租金
	private Double nzzyearptz;		// 增长后年租金调整额
	private Double nzzyearptzh;		// 增长后年租金调整后金额
	private String vmemo;		// 备注
	private Double vdef1;		// 自定义项1
	private Double vdef2;		// 自定义项2
	private Double vdef3;		// 自定义项3
	private Double vdef4;		// 自定义项4
	private Double vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractZzq() {
		this(null);
	}

	public WgContractZzq(WgContract pkContract){
		this.pkContract = pkContract;
	}
	
	public String getPkContractZzq() {
		return pkContractZzq;
	}

	public void setPkContractZzq(String pkContractZzq) {
		this.pkContractZzq = pkContractZzq;
	}
	
	public WgContract getPkContract() {
		return pkContract;
	}

	public void setPkContract(WgContract pkContract) {
		this.pkContract = pkContract;
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
	
	public Double getZzrate() {
		return zzrate;
	}

	public void setZzrate(Double zzrate) {
		this.zzrate = zzrate;
	}
	
	public Double getNzzmny() {
		return nzzmny;
	}

	public void setNzzmny(Double nzzmny) {
		this.nzzmny = nzzmny;
	}
	
	public Double getNzzprice() {
		return nzzprice;
	}

	public void setNzzprice(Double nzzprice) {
		this.nzzprice = nzzprice;
	}

	public Double getNzzdayp() {
		return nzzdayp;
	}

	public void setNzzdayp(Double nzzdayp) {
		this.nzzdayp = nzzdayp;
	}
	
	public Double getNzzmonthp() {
		return nzzmonthp;
	}

	public void setNzzmonthp(Double nzzmonthp) {
		this.nzzmonthp = nzzmonthp;
	}
	
	public Double getNzzmonthptz() {
		return nzzmonthptz;
	}

	public void setNzzmonthptz(Double nzzmonthptz) {
		this.nzzmonthptz = nzzmonthptz;
	}

	public Double getNzzmonthptzh() {
		return nzzmonthptzh;
	}

	public void setNzzmonthptzh(Double nzzmonthptzh) {
		this.nzzmonthptzh = nzzmonthptzh;
	}

	public Double getNzzyearp() {
		return nzzyearp;
	}

	public void setNzzyearp(Double nzzyearp) {
		this.nzzyearp = nzzyearp;
	}
	
	public Double getNzzyearptz() {
		return nzzyearptz;
	}

	public void setNzzyearptz(Double nzzyearptz) {
		this.nzzyearptz = nzzyearptz;
	}

	public Double getNzzyearptzh() {
		return nzzyearptzh;
	}

	public void setNzzyearptzh(Double nzzyearptzh) {
		this.nzzyearptzh = nzzyearptzh;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	
	public Double getVdef1() {
		return vdef1;
	}

	public void setVdef1(Double vdef1) {
		this.vdef1 = vdef1;
	}
	
	public Double getVdef2() {
		return vdef2;
	}

	public void setVdef2(Double vdef2) {
		this.vdef2 = vdef2;
	}
	
	public Double getVdef3() {
		return vdef3;
	}

	public void setVdef3(Double vdef3) {
		this.vdef3 = vdef3;
	}
	
	public Double getVdef4() {
		return vdef4;
	}

	public void setVdef4(Double vdef4) {
		this.vdef4 = vdef4;
	}
	
	public Double getVdef5() {
		return vdef5;
	}

	public void setVdef5(Double vdef5) {
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