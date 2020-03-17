/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.bd.entity.BdFeescale;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 合同管理表体-物业费页签Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_wyf", alias="a", columns={
		@Column(name="pk_contract_wyf", attrName="pkContractWyf", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="pk_build", attrName="pkBuild.pkBuildingfile", label="楼栋"),
		@Column(name="pk_house", attrName="pkHouse.pkHousesource", label="房产名称"),
		@Column(name="pk_feescale", attrName="pkFeescale.pkFeescale", label="收费标准"),
		@Column(name="nfeemny", attrName="nfeemny", label="收费金额"),
		@Column(name="ntaxrate", attrName="ntaxrate", label="税率"),
		@Column(name="vmemo", attrName="vmemo", label="备注"),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
		@Column(name="dstartdate", attrName="dstartdate", label="费用开始日期"),
	}, joinTable={
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
		@JoinTable(type=Type.LEFT_JOIN, entity=BdFeescale.class, attrName="pkFeescale", alias="fee1",
		on="fee1.pk_feescale = a.pk_feescale", columns={
			@Column(name="pk_feescale", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
}, orderBy="a.pk_contract_wyf ASC"
)
public class WgContractWyf extends DataEntity<WgContractWyf> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractWyf;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private ZlBuildingfile pkBuild;		// 楼栋
	private ZlHousesource pkHouse;		// 房产名称
	private BdFeescale pkFeescale;		// 收费标准
	private Double nfeemny;		// 收费金额
	private Integer ntaxrate;		// 税率
	private String vmemo;		// 备注
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	private Date dstartdate;		//费用开始日期
	
	public WgContractWyf() {
		this(null);
	}

	public WgContractWyf(WgContract pkContract){
		this.pkContract = pkContract;
	}
	
	public String getPkContractWyf() {
		return pkContractWyf;
	}

	public void setPkContractWyf(String pkContractWyf) {
		this.pkContractWyf = pkContractWyf;
	}
	
	public WgContract getPkContract() {
		return pkContract;
	}

	public void setPkContract(WgContract pkContract) {
		this.pkContract = pkContract;
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

	public BdFeescale getPkFeescale() {
		return pkFeescale;
	}

	public void setPkFeescale(BdFeescale pkFeescale) {
		this.pkFeescale = pkFeescale;
	}

	public Double getNfeemny() {
		return nfeemny;
	}

	public void setNfeemny(Double nfeemny) {
		this.nfeemny = nfeemny;
	}
	
	public Integer getNtaxrate() {
		return ntaxrate;
	}

	public void setNtaxrate(Integer ntaxrate) {
		this.ntaxrate = ntaxrate;
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

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}
	
}