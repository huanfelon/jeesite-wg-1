/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 应确认收入Entity
 * @author GJ
 * @version 2019-11-07
 */
@Table(name="ct_charge_yqrsr", alias="a", columns={
		@Column(name="pk_charge_yqrsr", attrName="pkChargeYqrsr", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="部门"),
		@Column(name="pk_project", attrName="pkProject.pkProject", label="项目信息"),
		@Column(name="htcode", attrName="htcode", label="合同号", queryType=QueryType.LIKE),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="pk_buildingfile", attrName="pkBuild.pkBuildingfile", label="楼栋"),
		@Column(name="pk_housesource", attrName="pkHouse.pkHousesource", label="房产名称"),
		@Column(name="pk_sf_project", attrName="pkSfProject.pkProject", label="收费项目"),
		@Column(name="fyksdate", attrName="fyksdate", label="费用开始日期"),
		@Column(name="fyjzdate", attrName="fyjzdate", label="费用截止日期"),
		@Column(name="kjyears", attrName="kjyears", label="会计年月"),
		@Column(name="nyqrsrmny", attrName="nyqrsrmny", label="应确认收入金额"),
		@Column(name="tax_rate", attrName="taxRate", label="税率"),
		@Column(name="no_tax_amount", attrName="noTaxAmount", label="应确认收入无税金额"),
		@Column(name="tax_amount", attrName="taxAmount", label="税额"),
		@Column(name="nsrqrmny", attrName="nsrqrmny", label="收入确认金额"),
		@Column(name="nqsmny", attrName="nqsmny", label="欠收金额"),
		@Column(name="lyvbilltype", attrName="lyvbilltype", label="来源单据类型"),
		@Column(name="vbillstatus", attrName="vbillstatus", label="单据状态"),
		@Column(name="creator", attrName="creator.userCode", label="制单人"),
		@Column(name="creationtime", attrName="creationtime", label="制单时间"),
		@Column(name="approver", attrName="approver.userCode", label="审核人"),
		@Column(name="approvetime", attrName="approvetime", label="审核时间"),
		@Column(name="modifier", attrName="modifier.userCode", label="修改人"),
		@Column(name="modifiedtime", attrName="modifiedtime", label="修改时间"),
		@Column(name="dr", attrName="dr", label="dr"),
		@Column(name="ts", attrName="ts", label="ts"),
		@Column(name="vsrcid", attrName="vsrcid", label="来源账单单据id"),
		@Column(name="vsrcid2", attrName="vsrcid2", label="来源合同业务id"),
		@Column(name="vpid", attrName="vpid", label="来源账单单据表头id"),
		@Column(name="vsrcid2name", attrName="vsrcid2name", label="来源合同页签名称"),
		@Column(name="iszz", attrName="iszz", label="是否自制"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u2",
			on="u2.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="u12",
		on="u12.pk_dept = a.pk_dept", columns={
			@Column(name="pk_dept", label="部门编码", isPK=true),
			@Column(name="dept_code", label="编码"),
			@Column(name="dept_name", label="部门名称", isQuery=false),
	}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="u13",
		on="u13.pk_project = a.pk_project", columns={
			@Column(name="pk_project", label="用户编码", isPK=true),
			@Column(name="code", attrName="code", label="编码"),
			@Column(name="name", label="用户名称", isQuery=false),
	}),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u20",
			on="u20.user_code = a.creator", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u22",
			on="u22.user_code = a.approver", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u24",
			on="u24.user_code = a.modifier", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		
	},extWhereKeys="extquery", orderBy="a.fyksdate "
)
public class CtChargeYqrsr extends DataEntity<CtChargeYqrsr> {
	
	private static final long serialVersionUID = 1L;
	private String pkChargeYqrsr;		// 主键
	private Office pkOrg;		// 组织
	private BdDept pkDept;		// 部门
	private ZlProject pkProject;		// 项目信息
	private String htcode;		// 合同号
	private BdCustomer pkCustomer;		// 客户名称
	private ZlBuildingfile pkBuild;		// 楼栋
	private ZlHousesource pkHouse;		// 房产名称
	private BdProject pkSfProject;		// 收费项目
	private Date fyksdate;		// 费用开始日期
	private Date fyjzdate;		// 费用截止日期
	private Date kjyears;		// 会计年月
	private Double nyqrsrmny;		// 应确认收入金额
	private Double taxRate;		// 税率
	private Double noTaxAmount;		// 应确认收入无税金额
	private Double taxAmount;		// 税额
	private Double nsrqrmny;		// 收入确认金额
	private Double nqsmny;		// 欠收金额
	private String lyvbilltype;		// 来源单据类型
	private Integer vbillstatus;		// 单据状态
	private User creator;		// 制单人
	private Date creationtime;		// 制单时间
	private User approver;		// 审核人
	private Date approvetime;		// 审核时间
	private User modifier;		// 修改人
	private Date modifiedtime;		// 修改时间
	private Integer dr;		// dr
	private Date ts;		// ts
	private String vsrcid;
	private String vsrcid2;
	private String vsrcid2name;
	private String vpid;
	private String iszz;   //是否自制
	
	public CtChargeYqrsr() {
		this(null);
	}

	public CtChargeYqrsr(String id){
		super(id);
	}
	
	public String getPkChargeYqrsr() {
		return pkChargeYqrsr;
	}

	public void setPkChargeYqrsr(String pkChargeYqrsr) {
		this.pkChargeYqrsr = pkChargeYqrsr;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	

	
	@Length(min=0, max=200, message="合同号长度不能超过 200 个字符")
	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getFyksdate() {
		return fyksdate;
	}

	public void setFyksdate(Date fyksdate) {
		this.fyksdate = fyksdate;
	}
	
	public Date getFyksdate_gte(){
		return sqlMap.getWhere().getValue("fyksdate", QueryType.GTE);
	}

	public void setFyksdate_gte(Date fyksdate){
		fyksdate = DateUtils.getOfDayFirst(fyksdate); // 将日期的时间改为0点0分0秒
		sqlMap.getWhere().and("fyksdate", QueryType.GTE, fyksdate);
	}

	public Date getFyksdate_lte(){
		return sqlMap.getWhere().getValue("fyksdate", QueryType.LTE);
	}

	public void setFyksdate_lte(Date fyksdate){
		fyksdate = DateUtils.getOfDayLast(fyksdate); // 将日期的时间改为23点59分59秒
		sqlMap.getWhere().and("fyksdate", QueryType.LTE, fyksdate);
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
	
	public Double getNsrqrmny() {
		return nsrqrmny;
	}

	public void setNsrqrmny(Double nsrqrmny) {
		this.nsrqrmny = nsrqrmny;
	}
	
	public String getLyvbilltype() {
		return lyvbilltype;
	}

	public void setLyvbilltype(String lyvbilltype) {
		this.lyvbilltype = lyvbilltype;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(Date creationtime) {
		this.creationtime = creationtime;
	}
	
	public User getApprover() {
		return approver;
	}

	public void setApprover(User approver) {
		this.approver = approver;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApprovetime() {
		return approvetime;
	}

	public void setApprovetime(Date approvetime) {
		this.approvetime = approvetime;
	}
	
	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(Date modifiedtime) {
		this.modifiedtime = modifiedtime;
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

	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	public ZlProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(ZlProject pkProject) {
		this.pkProject = pkProject;
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

	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}
	
	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}

	public String getVsrcid2() {
		return vsrcid2;
	}

	public void setVsrcid2(String vsrcid2) {
		this.vsrcid2 = vsrcid2;
	}

	public String getVpid() {
		return vpid;
	}

	public void setVpid(String vpid) {
		this.vpid = vpid;
	}

	public String getIszz() {
		return iszz;
	}

	public void setIszz(String iszz) {
		this.iszz = iszz;
	}

	public String getVsrcid2name() {
		return vsrcid2name;
	}

	public void setVsrcid2name(String vsrcid2name) {
		this.vsrcid2name = vsrcid2name;
	}
	
}