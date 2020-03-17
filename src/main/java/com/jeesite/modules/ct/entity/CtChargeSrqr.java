/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 收入确认Entity
 * @author GJ
 * @version 2019-11-07
 */
@Table(name="ct_charge_srqr", alias="a", columns={
		@Column(name="pk_charge_srqr", attrName="pkChargeSrqr", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="部门"),
		@Column(name="pk_project", attrName="pkProject.pkProject", label="项目信息"),
		@Column(name="nyqrsrmny", attrName="nyqrsrmny", label="应确认收入金额"),
		@Column(name="nbcsrqrmny", attrName="nbcsrqrmny", label="本次收入确认金额"),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u9",
			on="u9.user_code = a.creator", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u11",
			on="u11.user_code = a.approver", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u13",
			on="u13.user_code = a.modifier", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="u14",
		on="u14.pk_project = a.pk_project", columns={
			@Column(name="pk_project", label="用户编码", isPK=true),
			@Column(name="code", attrName="code", label="编码"),
			@Column(name="name", label="用户名称", isQuery=false),
	}),
	}, orderBy="a.creationtime "
)
public class CtChargeSrqr extends DataEntity<CtChargeSrqr> {
	
	private static final long serialVersionUID = 1L;
	private String pkChargeSrqr;		// 主键
	private Office pkOrg;		// 组织
	private BdDept pkDept;		// 部门
	private ZlProject pkProject;		// 项目信息
	private Double nyqrsrmny;		// 应确认收入金额
	private Double nbcsrqrmny;		// 本次收入确认金额
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
	private String iszz; //是否自制
	private List<CtChargeSrqrB> ctChargeSrqrBList = ListUtils.newArrayList();		// 子表列表
	
	public CtChargeSrqr() {
		this(null);
	}

	public CtChargeSrqr(String id){
		super(id);
	}
	
	public String getPkChargeSrqr() {
		return pkChargeSrqr;
	}

	public void setPkChargeSrqr(String pkChargeSrqr) {
		this.pkChargeSrqr = pkChargeSrqr;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}

	
	
	
	public Double getNyqrsrmny() {
		return nyqrsrmny;
	}

	public void setNyqrsrmny(Double nyqrsrmny) {
		this.nyqrsrmny = nyqrsrmny;
	}
	
	public Double getNbcsrqrmny() {
		return nbcsrqrmny;
	}

	public void setNbcsrqrmny(Double nbcsrqrmny) {
		this.nbcsrqrmny = nbcsrqrmny;
	}
	
	@Length(min=0, max=64, message="来源单据类型长度不能超过 64 个字符")
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
	
	public List<CtChargeSrqrB> getCtChargeSrqrBList() {
		return ctChargeSrqrBList;
	}

	public void setCtChargeSrqrBList(List<CtChargeSrqrB> ctChargeSrqrBList) {
		this.ctChargeSrqrBList = ctChargeSrqrBList;
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

	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}

	public String getIszz() {
		return iszz;
	}

	public void setIszz(String iszz) {
		this.iszz = iszz;
	}
	
}