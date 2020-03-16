/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 合同管理表体-收入拆分（隐藏）
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_srcf", alias="a", columns={
		@Column(name="pk_contract_srcf", attrName="pkContractSrcf", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="pk_build", attrName="pkBuild.pkBuildingfile", label="楼栋"),
		@Column(name="pk_house", attrName="pkHouse.pkHousesource", label="房产名称"),
		@Column(name="pk_costproject", attrName="pkCostproject.pkProject", label="收费项目"),
		@Column(name="dstartdate", attrName="dstartdate", label="开始日期"),
		@Column(name="denddate", attrName="denddate", label="截止日期"),
		@Column(name="ntaxrate", attrName="ntaxrate", label="税率"),
		@Column(name="nrecmny", attrName="nrecmny", label="应收金额"),
		@Column(name="ntaxmny", attrName="ntaxmny", label="税额"),
		@Column(name="nnotaxmny", attrName="nnotaxmny", label="无税金额"),
		@Column(name="nrealmny", attrName="nrealmny", label="实收金额"),
		@Column(name="vsrcid", attrName="vsrcid", label="来源ID"),
		@Column(name="vsrctype", attrName="vsrctype", label="来源类型"),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlBuildingfile.class, attrName="pkBuild", alias="b1",
		on="b1.pk_buildingfile = a.pk_build", columns={
			@Column(name="pk_buildingfile", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlHousesource.class, attrName="pkHouse", alias="h1",
		on="h1.pk_housesource = a.pk_house", columns={
			@Column(name="pk_housesource", label="主键", isPK=true, isQuery=false),
			@Column(name="estatecode", label="房产编号", isQuery=false),
			@Column(name="estatename", label="房产名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdProject.class, attrName="pkCostproject", alias="cp1",
		on="cp1.pk_project = a.pk_costproject", columns={
			@Column(name="pk_project", attrName="pkProject", label="主键", isPK=true, isQuery=false),
			@Column(name="code", attrName="code", label="编码", isQuery=false),
			@Column(name="name", attrName="name", label="名称", isQuery=false),
		}),
},extWhereKeys="dsf", orderBy="a.pk_contract,a.vsrcid,a.dstartdate,a.pk_costproject "
)
public class WgContractSrcf extends DataEntity<WgContractSrcf> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractSrcf;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private BdCustomer pkCustomer;		//客户名称
	private ZlBuildingfile pkBuild;		//楼栋
	private ZlHousesource pkHouse;		// 房产名称
	private BdProject pkCostproject;		// 收费项目
	private Date dstartdate;		// 开始日期
	private Date denddate;		// 截止日期
	private Double nrecmny;		// 单价
	private Double ntaxrate;		// 税率
	private Double ntaxmny;		// 税额
	private Double nnotaxmny;		// 无税金额
	private Double nrealmny;		//实收金额
	private String vsrcid;		//来源ID
	private String vsrctype;		//来源类型
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractSrcf() {
		this(null);
	}

	public WgContractSrcf(WgContract pkContract){
		this.pkContract = pkContract;
	}

	public String getPkContractSrcf() {
		return pkContractSrcf;
	}

	public void setPkContractSrcf(String pkContractSrcf) {
		this.pkContractSrcf = pkContractSrcf;
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

	public BdProject getPkCostproject() {
		return pkCostproject;
	}

	public void setPkCostproject(BdProject pkCostproject) {
		this.pkCostproject = pkCostproject;
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

	public Double getNrecmny() {
		return nrecmny;
	}

	public void setNrecmny(Double nrecmny) {
		this.nrecmny = nrecmny;
	}

	public Double getNtaxrate() {
		return ntaxrate;
	}

	public void setNtaxrate(Double ntaxrate) {
		this.ntaxrate = ntaxrate;
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

	public Double getNrealmny() {
		return nrealmny;
	}

	public void setNrealmny(Double nrealmny) {
		this.nrealmny = nrealmny;
	}

	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}

	public String getVsrctype() {
		return vsrctype;
	}

	public void setVsrctype(String vsrctype) {
		this.vsrctype = vsrctype;
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