/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;
import com.jeesite.modules.zl.entity.ZlProject;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 租约待开账Entity
 * @author tcl
 * @version 2019-11-06
 */
@Table(name="ct_rentdkz", alias="a", columns={
		@Column(name="pk_rentdkz", attrName="pkRentdkz", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract", label="合同pk", isQuery=false),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="所属组织"),
		@Column(name="pk_project", attrName="pkProject.id", label="项目信息"),
		@Column(name="pk_customer", attrName="pkCustomer.id", label="客户名称"),
		@Column(name="htcode", attrName="htcode", label="合同号",queryType=QueryType.LIKE),
		@Column(name="pk_building", attrName="pkBuilding.id", label="楼栋"),
		@Column(name="pk_house", attrName="pkHouse.id", label="房产名称"),
		@Column(name="nprice", attrName="nprice", label="租金单价", isQuery=false),
		@Column(name="renttype", attrName="renttype", label="租赁方式", isQuery=false),
		@Column(name="dstartdate", attrName="dstartdate", label="合同起租日期", isQuery=false),
		@Column(name="denddate", attrName="denddate", label="合同截止日期", isQuery=false),
		@Column(name="pk_dept", attrName="pkDept.id", label="部门"),
		@Column(name="vbillno", attrName="vbillno", label="单据号",queryType=QueryType.LIKE),
		@Column(name="ts", attrName="ts", label="时间戳", isQuery=true),
		@Column(name="dr", attrName="dr", label="删除标识", isQuery=true),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1", isQuery=false),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2", isQuery=false),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3", isQuery=false),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4", isQuery=false),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5", isQuery=false),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u3",
			on="u3.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true, isQuery=false),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="u4",
			on="u4.pk_project = a.pk_project", columns={
				@Column(name="pk_project", label="", isPK=true, isQuery=false),
				@Column(name="code", label="编码", isQuery=false),
				@Column(name="name", label="名称", isQuery=false),
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=BdCustomer.class, attrName="pkCustomer", alias="u5",
			on="u5.pk_customer = a.pk_customer", columns={
				@Column(name="pk_customer", label="", isPK=true, isQuery=false),
				@Column(name="code", label="编码", isQuery=false),
				@Column(name="name", label="名称", isQuery=false),
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlBuildingfile.class, attrName="pkBuilding", alias="u7",
			on="u7.pk_buildingfile = a.pk_building", columns={
				@Column(name="pk_buildingfile", label="", isPK=true, isQuery=false),
				@Column(name="code", label="编码", isQuery=false),
				@Column(name="name", label="名称", isQuery=false),
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlHousesource.class, attrName="pkHouse", alias="u8",
			on="u8.pk_housesource = a.pk_house", columns={
				@Column(name="pk_housesource", label="", isPK=true, isQuery=false),
				@Column(name="estatecode", label="编码", isQuery=false),
				@Column(name="estatename", label="名称", isQuery=false),	
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="u13",
			on="u13.pk_dept = a.pk_dept", columns={
				@Column(name="pk_dept", label="", isPK=true, isQuery=false),
				@Column(name="dept_code", label="编码", isQuery=false),
				@Column(name="dept_name", label="名称", isQuery=false),	
		}),
		},extWhereKeys="dsf", orderBy="a.pk_org,a.pk_project,a.vbillno "
)
public class CtRentdkz extends DataEntity<CtRentdkz> {
	
	private static final long serialVersionUID = 1L;
	private String pkRentdkz;		// 主键
	private String pkContract;		// 合同pk
	private Office pkOrg;		// 所属组织
	private ZlProject pkProject;		// 项目信息
	private BdCustomer pkCustomer;		// 客户名称
	private String htcode;		// 合同号
	private ZlBuildingfile pkBuilding;		// 楼栋
	private ZlHousesource pkHouse;		// 房产名称
	private Double nprice;		// 租金单价
	private Integer renttype;		// 租赁方式
	private Date dstartdate;		// 合同起租日期
	private Date denddate;		// 合同截止日期
	private BdDept pkDept;		// 部门
	private String vbillno;		// 单据号
	private Date ts;		// 时间戳
	private Integer dr;		// 删除标识
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	
	public CtRentdkz() {
		this(null);
	}

	public CtRentdkz(String id){
		super(id);
	}
	
	public String getPkRentdkz() {
		return pkRentdkz;
	}

	public void setPkRentdkz(String pkRentdkz) {
		this.pkRentdkz = pkRentdkz;
	}
	
	@Length(min=0, max=255, message="合同pk长度不能超过 255 个字符")
	public String getPkContract() {
		return pkContract;
	}

	public void setPkContract(String pkContract) {
		this.pkContract = pkContract;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	public ZlProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(ZlProject pkProject) {
		this.pkProject = pkProject;
	}
	
	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}
	
	@Length(min=0, max=255, message="合同号长度不能超过 255 个字符")
	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
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
	
	public Double getNprice() {
		return nprice;
	}

	public void setNprice(Double nprice) {
		this.nprice = nprice;
	}
	
	public Integer getRenttype() {
		return renttype;
	}

	public void setRenttype(Integer renttype) {
		this.renttype = renttype;
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
	
	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}
	
	@Length(min=0, max=255, message="单据号长度不能超过 255 个字符")
	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
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
	
	@Length(min=0, max=255, message="自定义项1长度不能超过 255 个字符")
	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	
	@Length(min=0, max=255, message="自定义项2长度不能超过 255 个字符")
	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	
	@Length(min=0, max=255, message="自定义项3长度不能超过 255 个字符")
	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}
	
	@Length(min=0, max=255, message="自定义项4长度不能超过 255 个字符")
	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}
	
	@Length(min=0, max=255, message="自定义项5长度不能超过 255 个字符")
	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}
	
}