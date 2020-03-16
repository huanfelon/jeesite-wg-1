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
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.zl.entity.ZlHousesource;

/**
 * 退租管理Entity
 * @author LY
 * @version 2019-12-19
 */
@Table(name="wg_throwalease_khfc", alias="a", columns={
		@Column(name="pk_throwalease_khfc", attrName="pkThrowaleaseKhfc", label="主键", isPK=true),
		@Column(name="pk_throwalease", attrName="pkThrowalease.pkThrowalease", label="表头主键"),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="pk_house", attrName="pkHouse.pkHousesource", label="房产名称"),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlHousesource.class, attrName="pkHouse", alias="h1",
		on="h1.pk_housesource = a.pk_house", columns={
			@Column(name="pk_housesource", label="主键", isPK=true, isQuery=false),
			@Column(name="estatecode", label="房产编号", isQuery=false),
			@Column(name="estatename", label="房产名称", isQuery=false),
		}),
}, orderBy="a.pk_throwalease_khfc ASC"
)
public class WgThrowaleaseKhfc extends DataEntity<WgThrowaleaseKhfc> {
	
	private static final long serialVersionUID = 1L;
	private String pkThrowaleaseKhfc;		// 主键
	private WgThrowalease pkThrowalease;		// 表头主键 父类
	private BdCustomer pkCustomer;		// 客户名称
	private ZlHousesource pkHouse;		// 房产名称
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		// 删除标识
	private Date ts;		// 时间戳
	
	public WgThrowaleaseKhfc() {
		this(null);
	}

	public WgThrowaleaseKhfc(WgThrowalease pkThrowalease){
		this.pkThrowalease = pkThrowalease;
	}
	
	public String getPkThrowaleaseKhfc() {
		return pkThrowaleaseKhfc;
	}

	public void setPkThrowaleaseKhfc(String pkThrowaleaseKhfc) {
		this.pkThrowaleaseKhfc = pkThrowaleaseKhfc;
	}
	
	public WgThrowalease getPkThrowalease() {
		return pkThrowalease;
	}

	public void setPkThrowalease(WgThrowalease pkThrowalease) {
		this.pkThrowalease = pkThrowalease;
	}
	
	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}
	
	public ZlHousesource getPkHouse() {
		return pkHouse;
	}

	public void setPkHouse(ZlHousesource pkHouse) {
		this.pkHouse = pkHouse;
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