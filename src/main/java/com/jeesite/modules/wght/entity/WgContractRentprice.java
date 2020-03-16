/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 合同管理表体-年租金（隐藏）
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_rentprice", alias="a", columns={
		@Column(name="pk_contract_rentprice", attrName="pkContractRentprice", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="pk_house", attrName="pkHouse.pkHousesource", label="房产名称"),
		@Column(name="pk_costproject", attrName="pkCostp.pkProject", label="收费项目"),
		@Column(name="dstartdate", attrName="dstartdate", label="开始日期"),
		@Column(name="denddate", attrName="denddate", label="截止日期"),
		@Column(name="nprice", attrName="nprice", label="单价"),
		@Column(name="ndaymny", attrName="ndaymny", label="日租金"),
		@Column(name="nmonthmny", attrName="nmonthmny", label="月租金"),
		@Column(name="nyearmny", attrName="nyearmny", label="年租金"),
		@Column(name="ntaxrate", attrName="ntaxrate", label="税率"),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlHousesource.class, attrName="pkHouse", alias="h1",
		on="h1.pk_housesource = a.pk_house", columns={
			@Column(name="pk_housesource", label="主键", isPK=true, isQuery=false),
			@Column(name="estatecode", label="房产编号", isQuery=false),
			@Column(name="estatename", label="房产名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdProject.class, attrName="pkCostp", alias="cp1",
		on="cp1.pk_project = a.pk_costproject", columns={
			@Column(name="pk_project", attrName="pkProject", label="主键", isPK=true, isQuery=false),
			@Column(name="code", attrName="code", label="编码", isQuery=false),
			@Column(name="name", attrName="name", label="名称", isQuery=false),
		}),
}, orderBy="a.pk_contract_rentprice ASC"
)
public class WgContractRentprice extends DataEntity<WgContractRentprice> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractRentprice;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private ZlHousesource pkHouse;		// 房产名称
	private BdProject pkCostproject;		// 收费项目
	private Date dstartdate;		// 开始日期
	private Date denddate;		// 截止日期
	private Double nprice;		// 单价
	private Double ndaymny;		// 日租金
	private Double nmonthmny;		// 月租金
	private Double nyearmny;		// 年租金
	private Double ntaxrate;		//税率
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractRentprice() {
		this(null);
	}

	public WgContractRentprice(WgContract pkContract){
		this.pkContract = pkContract;
	}

	public String getPkContractRentprice() {
		return pkContractRentprice;
	}

	public void setPkContractRentprice(String pkContractRentprice) {
		this.pkContractRentprice = pkContractRentprice;
	}

	public WgContract getPkContract() {
		return pkContract;
	}

	public void setPkContract(WgContract pkContract) {
		this.pkContract = pkContract;
	}

	public ZlHousesource getPkHouse() {
		return pkHouse;
	}

	public void setPkHouse(ZlHousesource pkHouse) {
		this.pkHouse = pkHouse;
	}

	public BdProject getPkCostproject() {
		return pkCostproject;
	}

	public void setPkCostproject(BdProject pkCostproject) {
		this.pkCostproject = pkCostproject;
	}

	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}

	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}

	public Double getNprice() {
		return nprice;
	}

	public void setNprice(Double nprice) {
		this.nprice = nprice;
	}

	public Double getNdaymny() {
		return ndaymny;
	}

	public void setNdaymny(Double ndaymny) {
		this.ndaymny = ndaymny;
	}

	public Double getNmonthmny() {
		return nmonthmny;
	}

	public void setNmonthmny(Double nmonthmny) {
		this.nmonthmny = nmonthmny;
	}

	public Double getNyearmny() {
		return nyearmny;
	}

	public void setNyearmny(Double nyearmny) {
		this.nyearmny = nyearmny;
	}

	public Double getNtaxrate() {
		return ntaxrate;
	}

	public void setNtaxrate(Double ntaxrate) {
		this.ntaxrate = ntaxrate;
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