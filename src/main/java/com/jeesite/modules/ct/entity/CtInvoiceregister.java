/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;

import org.hibernate.validator.constraints.Length;

import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.zl.entity.ZlProject;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 开票登记Entity
 * @author tcl
 * @version 2019-11-11
 */
@Table(name="ct_invoiceregister", alias="a", columns={
		@Column(name="pk_invoiceregister", attrName="pkInvoiceregister", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="所属组织"),
		@Column(name="vbillno", attrName="vbillno", label="单据号", queryType=QueryType.LIKE),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="部门"),
		@Column(name="pk_project", attrName="pkProject.id", label="项目信息"),
		@Column(name="nkpmny", attrName="nkpmny", label="开票金额", isQuery=false),
		@Column(name="pk_kpr", attrName="pkKpr.userCode", label="开票人"),
		@Column(name="dkpdate", attrName="dkpdate", label="开票日期"),
		@Column(name="vsrcid", attrName="vsrcid", label="来源单据id", isQuery=false),
		@Column(name="vsrcno", attrName="vsrcno", label="来源单据号", isQuery=false),
		@Column(name="vsrctype", attrName="vsrctype", label="来源单据类型", isQuery=false),
		@Column(name="vbillstatus", attrName="vbillstatus", label="单据状态"),
		@Column(name="creator", attrName="creator.userCode", label="创建人", isQuery=false),
		@Column(name="creationtime", attrName="creationtime", label="创建时间", isQuery=false),
		@Column(name="modifier", attrName="modifier.userCode", label="修改人", isQuery=false),
		@Column(name="modifiedtime", attrName="modifiedtime", label="修改时间", isQuery=false),
		@Column(name="approver", attrName="approver.userCode", label="审核人", isQuery=false),
		@Column(name="approvetime", attrName="approvetime", label="审核时间", isQuery=false),
		@Column(name="ts", attrName="ts", label="时间戳", isQuery=true),
		@Column(name="dr", attrName="dr", label="删除标识", isQuery=true),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u2",
			on="u2.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="u5",
		on="u5.pk_dept = a.pk_dept", columns={
			@Column(name="pk_dept", label="", isPK=true, isQuery=false),
			@Column(name="dept_code", label="编码", isQuery=false),
			@Column(name="dept_name", label="名称", isQuery=false),	
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="u4",
		on="u4.pk_project = a.pk_project", columns={
			@Column(name="pk_project", label="", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="pkKpr", alias="u7",
			on="u7.user_code = a.pk_kpr", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u13",
			on="u13.user_code = a.creator", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u15",
			on="u15.user_code = a.modifier", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u17",
			on="u17.user_code = a.approver", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
	},extWhereKeys="dsf", orderBy="a.pk_org,a.pk_project,a.vbillno "
)
public class CtInvoiceregister extends DataEntity<CtInvoiceregister> {
	
	private static final long serialVersionUID = 1L;
	private String pkInvoiceregister;		// 主键
	private Office pkOrg;		// 所属组织
	private String vbillno;		// 单据号
	private BdDept pkDept;		// 部门
	private ZlProject pkProject;		// 项目信息
	private Double nkpmny;		// 开票金额
	private User pkKpr;		// 开票人
	private Date dkpdate;		// 开票日期
	private String vsrcid;		// 来源单据id
	private String vsrcno;		// 来源单据号
	private String vsrctype;		// 来源单据类型
	private Integer vbillstatus;		// 单据状态
	private User creator;		// 创建人
	private Date creationtime;		// 创建时间
	private User modifier;		// 修改人
	private Date modifiedtime;		// 修改时间
	private User approver;		// 审核人
	private Date approvetime;		// 审核时间
	private Date ts;		// 时间戳
	private Integer dr;		// 删除标识
	private List<CtInvoiceregisterKpxx> ctInvoiceregisterKpxxList = ListUtils.newArrayList();		// 子表列表
	private List<CtInvoiceregisterSkmx> ctInvoiceregisterSkmxList = ListUtils.newArrayList();		// 子表列表
	
	public CtInvoiceregister() {
		this(null);
	}

	public CtInvoiceregister(String id){
		super(id);
	}
	
	public String getPkInvoiceregister() {
		return pkInvoiceregister;
	}

	public void setPkInvoiceregister(String pkInvoiceregister) {
		this.pkInvoiceregister = pkInvoiceregister;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	@Length(min=0, max=255, message="单据号长度不能超过 255 个字符")
	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}
	
	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}
	
	public ZlProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(ZlProject pkProject) {
		this.pkProject = pkProject;
	}
	
	public Double getNkpmny() {
		return nkpmny;
	}

	public void setNkpmny(Double nkpmny) {
		this.nkpmny = nkpmny;
	}
	
	public User getPkKpr() {
		return pkKpr;
	}

	public void setPkKpr(User pkKpr) {
		this.pkKpr = pkKpr;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDkpdate() {
		return dkpdate;
	}

	public void setDkpdate(Date dkpdate) {
		this.dkpdate = dkpdate;
	}
	
	@Length(min=0, max=255, message="来源单据id长度不能超过 255 个字符")
	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}
	
	@Length(min=0, max=255, message="来源单据号长度不能超过 255 个字符")
	public String getVsrcno() {
		return vsrcno;
	}

	public void setVsrcno(String vsrcno) {
		this.vsrcno = vsrcno;
	}
	
	@Length(min=0, max=255, message="来源单据类型长度不能超过 255 个字符")
	public String getVsrctype() {
		return vsrctype;
	}

	public void setVsrctype(String vsrctype) {
		this.vsrctype = vsrctype;
	}
	
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
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
	
	public Date getDkpdate_gte() {
		return sqlMap.getWhere().getValue("dkpdate", QueryType.GTE);
	}

	public void setDkpdate_gte(Date dkpdate) {
		sqlMap.getWhere().and("dkpdate", QueryType.GTE, dkpdate);
	}
	
	public Date getDkpdate_lte() {
		return sqlMap.getWhere().getValue("dkpdate", QueryType.LTE);
	}

	public void setDkpdate_lte(Date dkpdate) {
		sqlMap.getWhere().and("dkpdate", QueryType.LTE, dkpdate);
	}
	
	public List<CtInvoiceregisterKpxx> getCtInvoiceregisterKpxxList() {
		return ctInvoiceregisterKpxxList;
	}

	public void setCtInvoiceregisterKpxxList(List<CtInvoiceregisterKpxx> ctInvoiceregisterKpxxList) {
		this.ctInvoiceregisterKpxxList = ctInvoiceregisterKpxxList;
	}
	
	public List<CtInvoiceregisterSkmx> getCtInvoiceregisterSkmxList() {
		return ctInvoiceregisterSkmxList;
	}

	public void setCtInvoiceregisterSkmxList(List<CtInvoiceregisterSkmx> ctInvoiceregisterSkmxList) {
		this.ctInvoiceregisterSkmxList = ctInvoiceregisterSkmxList;
	}
	
}