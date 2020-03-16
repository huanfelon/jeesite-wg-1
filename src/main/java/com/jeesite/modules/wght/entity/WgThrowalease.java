/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 退租管理Entity
 * @author LY
 * @version 2019-12-19
 */
@Table(name="wg_throwalease", alias="a", columns={
		@Column(name="pk_throwalease", attrName="pkThrowalease", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="所属组织"),
		@Column(name="pk_project", attrName="pkProject.pkProject", label="项目信息"),
		@Column(name="htcode", attrName="htcode", label="合同编号"),
		@Column(name="dstartdate", attrName="dstartdate", label="合同起始日"),
		@Column(name="denddate", attrName="denddate", label="合同终止日"),
		@Column(name="dtzdate", attrName="dtzdate", label="退租日期"),
		@Column(name="tztype", attrName="tztype", label="退租类型"),
		@Column(name="vbillstatus", attrName="vbillstatus", label="单据状态", comment="单据状态（0、自由,1、审批通过）"),
		@Column(name="billtype", attrName="billtype", label="单据类型"),
		@Column(name="creator", attrName="creator.userCode", label="制单人"),
		@Column(name="createdtime", attrName="createdtime", label="制单时间"),
		@Column(name="modifier", attrName="modifier.userCode", label="最后修改人"),
		@Column(name="modifiedtime", attrName="modifiedtime", label="最后修改时间"),
		@Column(name="approver", attrName="approver.userCode", label="审核人", isUpdateForce=true),
		@Column(name="approvedtime", attrName="approvedtime", label="审核时间", isUpdateForce=true),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="vdef6", attrName="vdef6", label="自定义项6"),
		@Column(name="vdef7", attrName="vdef7", label="自定义项7"),
		@Column(name="vdef8", attrName="vdef8", label="自定义项8"),
		@Column(name="vdef9", attrName="vdef9", label="自定义项9"),
		@Column(name="vdef10", attrName="vdef10", label="自定义项10"),
		@Column(name="vsrcid", attrName="vsrcid", label="来源单据id"),
		@Column(name="vsrctype", attrName="vsrctype", label="来源单据类型"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
		@Column(name="vbillno", attrName="vbillno", label="单据号"),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="部门"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="o1",
		on="o1.office_code = a.pk_org", columns={
			@Column(name="office_code", label="组织编码", isPK=true, isQuery=false),
			@Column(name="office_name", label="组织名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="p1",
		on="p1.pk_project = a.pk_project", columns={
			@Column(name="pk_project", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u1",
		on="u1.user_code = a.creator", columns={
			@Column(name="user_code", label="用户编码", isPK=true, isQuery=false),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u2",
		on="u2.user_code = a.modifier", columns={
			@Column(name="user_code", label="用户编码", isPK=true, isQuery=false),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u3",
		on="u3.user_code = a.approver", columns={
			@Column(name="user_code", label="用户编码", isPK=true, isQuery=false),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="d1",
		on="d1.pk_dept = a.pk_dept", columns={
			@Column(name="pk_dept", label="部门主键",isPK=true, isQuery=false),
			@Column(name="dept_code", label="部门编码", isQuery=false),
			@Column(name="dept_name", label="部门名称", isQuery=false),
		}),
}, orderBy="a.pk_throwalease DESC"
)
public class WgThrowalease extends DataEntity<WgThrowalease> {
	
	private static final long serialVersionUID = 1L;
	private String pkThrowalease;		// 主键
	private Office pkOrg;		// 所属组织
	private ZlProject pkProject;		// 项目信息
	private String htcode;		// 合同编号
	private Date dstartdate;		// 合同起始日
	private Date denddate;		// 合同终止日
	private Date dtzdate;		// 退租日期
	private Integer tztype;		// 退租类型（0、正常退租，2、提前退租）
	private Integer vbillstatus;		// 单据状态（0、自由,1、审批通过）
	private String billtype;		// 单据类型
	private User creator;		// 制单人
	private Date createdtime;		// 制单时间
	private User modifier;		// 最后修改人
	private Date modifiedtime;		// 最后修改时间
	private User approver;		// 审核人
	private Date approvedtime;		// 审核时间
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private String vdef6;		// 自定义项6
	private String vdef7;		// 自定义项7
	private String vdef8;		// 自定义项8
	private String vdef9;		// 自定义项9
	private String vdef10;		// 自定义项10
	private String vsrcid;		// 来源单据id
	private String vsrctype;		// 来源单据类型
	private Integer dr;		// 删除标识
	private Date ts;		// 时间戳
	private String vbillno;		// 单据号
	private BdDept pkDept;		//部门
	private List<WgThrowaleaseFyqs> wgThrowaleaseFyqsList = ListUtils.newArrayList();		// 子表列表
	private List<WgThrowaleaseBzj> wgThrowaleaseBzjList = ListUtils.newArrayList();		// 子表列表
	private List<WgThrowaleaseKhfc> wgThrowaleaseKhfcList = ListUtils.newArrayList();		// 子表列表
	
	public WgThrowalease() {
		this(null);
	}

	public WgThrowalease(String id){
		super(id);
	}
	
	public String getPkThrowalease() {
		return pkThrowalease;
	}

	public void setPkThrowalease(String pkThrowalease) {
		this.pkThrowalease = pkThrowalease;
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

	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
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
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDtzdate() {
		return dtzdate;
	}

	public void setDtzdate(Date dtzdate) {
		this.dtzdate = dtzdate;
	}
	
	public Date getDtzdate_gte(){
		return sqlMap.getWhere().getValue("dtzdate", QueryType.GTE);
	}

	public void setDtzdate_gte(Date dtzdate){
		dtzdate = DateUtils.getOfDayFirst(dtzdate); // 将日期的时间改为0点0分0秒
		sqlMap.getWhere().and("dtzdate", QueryType.GTE, dtzdate);
	}

	public Date getDtzdate_lte(){
		return sqlMap.getWhere().getValue("dtzdate", QueryType.LTE);
	}

	public void setDtzdate_lte(Date dtzdate){
		dtzdate = DateUtils.getOfDayLast(dtzdate); // 将日期的时间改为23点59分59秒
		sqlMap.getWhere().and("dtzdate", QueryType.LTE, dtzdate);
	}
	
	public Integer getTztype() {
		return tztype;
	}

	public void setTztype(Integer tztype) {
		this.tztype = tztype;
	}
	
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}
	
	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}
	
	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
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

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getApprovedtime() {
		return approvedtime;
	}

	public void setApprovedtime(Date approvedtime) {
		this.approvedtime = approvedtime;
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
	
	public String getVdef6() {
		return vdef6;
	}

	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}
	
	public String getVdef7() {
		return vdef7;
	}

	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	
	public String getVdef8() {
		return vdef8;
	}

	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	
	public String getVdef9() {
		return vdef9;
	}

	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	
	public String getVdef10() {
		return vdef10;
	}

	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
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

	public List<WgThrowaleaseFyqs> getWgThrowaleaseFyqsList() {
		return wgThrowaleaseFyqsList;
	}

	public void setWgThrowaleaseFyqsList(List<WgThrowaleaseFyqs> wgThrowaleaseFyqsList) {
		this.wgThrowaleaseFyqsList = wgThrowaleaseFyqsList;
	}
	
	public List<WgThrowaleaseBzj> getWgThrowaleaseBzjList() {
		return wgThrowaleaseBzjList;
	}

	public void setWgThrowaleaseBzjList(List<WgThrowaleaseBzj> wgThrowaleaseBzjList) {
		this.wgThrowaleaseBzjList = wgThrowaleaseBzjList;
	}
	
	public List<WgThrowaleaseKhfc> getWgThrowaleaseKhfcList() {
		return wgThrowaleaseKhfcList;
	}

	public void setWgThrowaleaseKhfcList(List<WgThrowaleaseKhfc> wgThrowaleaseKhfcList) {
		this.wgThrowaleaseKhfcList = wgThrowaleaseKhfcList;
	}
	
}