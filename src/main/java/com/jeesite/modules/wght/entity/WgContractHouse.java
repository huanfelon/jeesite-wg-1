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
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 合同管理表体-房产信息页签Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_house", alias="a", columns={
		@Column(name="pk_contract_house", attrName="pkContractHouse", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="pk_build", attrName="pkBuild.pkBuildingfile", label="楼栋"),
		@Column(name="pk_house", attrName="pkHouse.pkHousesource", label="房产名称"),
		@Column(name="narea", attrName="narea", label="租赁面积"),
		@Column(name="nprice", attrName="nprice", label="租金单价"),
		@Column(name="ndayprice", attrName="ndayprice", label="日租金"),
		@Column(name="nmonthprice", attrName="nmonthprice", label="月租金"),
		@Column(name="nmonthtzprice", attrName="nmonthtzprice", label="月租金调整额", isUpdateForce=true),
		@Column(name="nmonthtzhprice", attrName="nmonthtzhprice", label="月租金调整后金额"),
		@Column(name="nyearprice", attrName="nyearprice", label="年租金"),
		@Column(name="nyeartzprice", attrName="nyeartzprice", label="年租金调整额", isUpdateForce=true),
		@Column(name="nyeartzhprice", attrName="nyeartzhprice", label="年租金调整后金额"),
		@Column(name="iswy", attrName="iswy", label="是否物业"),
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
}, orderBy="a.pk_contract_house ASC"
)
public class WgContractHouse extends DataEntity<WgContractHouse> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractHouse;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private BdCustomer pkCustomer;		// 客户名称
	private ZlBuildingfile pkBuild;		// 楼栋
	private ZlHousesource pkHouse;		// 房产名称
	private Double narea;		// 租赁面积
	private Double nprice;		// 租金单价
	private Double ndayprice;		// 日租金（日/套）
	private Double nmonthprice;		// 月租金（月/套）
	private Double nmonthtzprice;		// 月租金调整额
	private Double nmonthtzhprice;		// 月租金调整后金额
	private Double nyearprice;		// 年租金（年/套）
	private Double nyeartzprice;		// 年租金调整额
	private Double nyeartzhprice;		// 年租金调整后金额
	private String iswy;		// 是否物业
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractHouse() {
		this(null);
	}

	public WgContractHouse(WgContract pkContract){
		this.pkContract = pkContract;
	}
	
	public String getPkContractHouse() {
		return pkContractHouse;
	}

	public void setPkContractHouse(String pkContractHouse) {
		this.pkContractHouse = pkContractHouse;
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

	public Double getNarea() {
		return narea;
	}

	public void setNarea(Double narea) {
		this.narea = narea;
	}
	
	public Double getNprice() {
		return nprice;
	}

	public void setNprice(Double nprice) {
		this.nprice = nprice;
	}
	
	public Double getNdayprice() {
		return ndayprice;
	}

	public void setNdayprice(Double ndayprice) {
		this.ndayprice = ndayprice;
	}
	
	public Double getNmonthprice() {
		return nmonthprice;
	}

	public void setNmonthprice(Double nmonthprice) {
		this.nmonthprice = nmonthprice;
	}
	
	public Double getNmonthtzprice() {
		return nmonthtzprice;
	}

	public void setNmonthtzprice(Double nmonthtzprice) {
		this.nmonthtzprice = nmonthtzprice;
	}

	public Double getNmonthtzhprice() {
		return nmonthtzhprice;
	}

	public void setNmonthtzhprice(Double nmonthtzhprice) {
		this.nmonthtzhprice = nmonthtzhprice;
	}

	public Double getNyearprice() {
		return nyearprice;
	}

	public void setNyearprice(Double nyearprice) {
		this.nyearprice = nyearprice;
	}
	
	public Double getNyeartzprice() {
		return nyeartzprice;
	}

	public void setNyeartzprice(Double nyeartzprice) {
		this.nyeartzprice = nyeartzprice;
	}

	public Double getNyeartzhprice() {
		return nyeartzhprice;
	}

	public void setNyeartzhprice(Double nyeartzhprice) {
		this.nyeartzhprice = nyeartzhprice;
	}

	public String getIswy() {
		return iswy;
	}

	public void setIswy(String iswy) {
		this.iswy = iswy;
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