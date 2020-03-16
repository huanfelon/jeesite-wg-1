/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.ct.entity;

import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;

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
 * 开票申请Entity
 * @author tcl
 * @version 2019-11-11
 */
@Table(name="ct_invoiceapply", alias="a", columns={
		@Column(name="pk_invoiceapply", attrName="pkInvoiceapply", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="所属组织"),
		@Column(name="vbillno", attrName="vbillno", label="单据号", queryType=QueryType.LIKE),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="部门"),
		@Column(name="pk_project", attrName="pkProject.id", label="项目信息"),
		@Column(name="nsqmny", attrName="nsqmny", label="申请开票总金额", isQuery=false),
		@Column(name="pk_sqr", attrName="pkSqr.userCode", label="申请开票人"),
		@Column(name="dsqdate", attrName="dsqdate", label="申请开票日期"),
		@Column(name="fptype", attrName="fptype", label="发票种类"),
		@Column(name="fpcode", attrName="fpcode", label="发票号码"),
		@Column(name="kpyq", attrName="kpyq", label="开票要求", isQuery=false),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="u13",
		on="u13.pk_dept = a.pk_dept", columns={
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
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="pkSqr", alias="u7",
			on="u7.user_code = a.pk_sqr", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u14",
			on="u14.user_code = a.creator", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u16",
			on="u16.user_code = a.modifier", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u18",
			on="u18.user_code = a.approver", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
	},extWhereKeys="dsf", orderBy="a.pk_org,a.pk_project,a.vbillno "
)
public class CtInvoiceapply extends DataEntity<CtInvoiceapply> {
	
	private static final long serialVersionUID = 1L;
	private String pkInvoiceapply;		// 主键
	private Office pkOrg;		// 所属组织
	private String vbillno;		// 单据号
	private BdDept pkDept;		// 部门
	private ZlProject pkProject;		// 项目信息
	private Double nsqmny;		// 申请开票总金额
	private User pkSqr;		// 申请开票人
	private Date dsqdate;		// 申请开票日期
	private String fptype;		//发票种类
	private String fpcode;		//发票号码
	private String kpyq;		// 开票要求
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
	private List<CtInvoiceapplyKpxx> ctInvoiceapplyKpxxList = ListUtils.newArrayList();		// 子表列表
	private List<CtInvoiceapplySkmx> ctInvoiceapplySkmxList = ListUtils.newArrayList();		// 子表列表
	
	public CtInvoiceapply() {
		this(null);
	}

	public CtInvoiceapply(String id){
		super(id);
	}
	
	public String getPkInvoiceapply() {
		return pkInvoiceapply;
	}

	public void setPkInvoiceapply(String pkInvoiceapply) {
		this.pkInvoiceapply = pkInvoiceapply;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
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
	
	public Double getNsqmny() {
		return nsqmny;
	}

	public void setNsqmny(Double nsqmny) {
		this.nsqmny = nsqmny;
	}
	
	public User getPkSqr() {
		return pkSqr;
	}

	public void setPkSqr(User pkSqr) {
		this.pkSqr = pkSqr;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDsqdate() {
		return dsqdate;
	}

	public void setDsqdate(Date dsqdate) {
		this.dsqdate = dsqdate;
	}
	
	public String getFptype() {
		return fptype;
	}

	public void setFptype(String fptype) {
		this.fptype = fptype;
	}

	public String getFpcode() {
		return fpcode;
	}

	public void setFpcode(String fpcode) {
		this.fpcode = fpcode;
	}

	public String getKpyq() {
		return kpyq;
	}

	public void setKpyq(String kpyq) {
		this.kpyq = kpyq;
	}
	
	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}
	
	public String getVsrcno() {
		return vsrcno;
	}

	public void setVsrcno(String vsrcno) {
		this.vsrcno = vsrcno;
	}
	
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
	
	public Date getDsqdate_gte() {
		return sqlMap.getWhere().getValue("dsqdate", QueryType.GTE);
	}

	public void setDsqdate_gte(Date dsqdate) {
		dsqdate = DateUtils.getOfDayFirst(dsqdate); // 将日期的时间改为0点0分0秒
		sqlMap.getWhere().and("dsqdate", QueryType.GTE, dsqdate);
	}
	
	public Date getDsqdate_lte() {
		return sqlMap.getWhere().getValue("dsqdate", QueryType.LTE);
	}

	public void setDsqdate_lte(Date dsqdate) {
		dsqdate = DateUtils.getOfDayLast(dsqdate); // 将日期的时间改为23点59分59秒
		sqlMap.getWhere().and("dsqdate", QueryType.LTE, dsqdate);
	}
	
	public List<CtInvoiceapplyKpxx> getCtInvoiceapplyKpxxList() {
		return ctInvoiceapplyKpxxList;
	}

	public void setCtInvoiceapplyKpxxList(List<CtInvoiceapplyKpxx> ctInvoiceapplyKpxxList) {
		this.ctInvoiceapplyKpxxList = ctInvoiceapplyKpxxList;
	}
	
	public List<CtInvoiceapplySkmx> getCtInvoiceapplySkmxList() {
		return ctInvoiceapplySkmxList;
	}

	public void setCtInvoiceapplySkmxList(List<CtInvoiceapplySkmx> ctInvoiceapplySkmxList) {
		this.ctInvoiceapplySkmxList = ctInvoiceapplySkmxList;
	}
	
}