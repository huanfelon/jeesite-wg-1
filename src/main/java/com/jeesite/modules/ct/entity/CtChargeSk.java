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
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 收款单Entity
 * @author GJ
 * @version 2019-11-05
 */
@Table(name="ct_charge_sk", alias="a", columns={
		@Column(name="pk_charge_sk", attrName="pkChargeSk", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="部门"),
		@Column(name="pk_project", attrName="pkProject.pkProject", label="项目信息"),
		@Column(name="pk_customer", attrName="pkCustomer.pkCustomer", label="客户名称"),
		@Column(name="skdate", attrName="skdate", label="收款日期"),
		@Column(name="bc_total_amount", attrName="bcTotalAmount", label="本次收款总额"),
		@Column(name="payment_method", attrName="paymentMethod", label="付款方式"),
		@Column(name="bank_account", attrName="bankAccount", label="银行账号"),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="u3",
		on="u3.pk_dept = a.pk_dept", columns={
			@Column(name="pk_dept", label="部门编码", isPK=true),
			@Column(name="dept_code", label="编码"),
			@Column(name="dept_name", label="部门名称", isQuery=false),
	}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u12",
			on="u12.user_code = a.creator", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="u13",
		on="u13.pk_project = a.pk_project", columns={
			@Column(name="pk_project", label="用户编码", isPK=true),
			@Column(name="code", attrName="code", label="编码"),
			@Column(name="name", label="用户名称", isQuery=false),
	}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u14",
			on="u14.user_code = a.approver", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u16",
			on="u16.user_code = a.modifier", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdCustomer.class, attrName="pkCustomer", alias="u17",
		on="u17.pk_customer = a.pk_customer", columns={
			@Column(name="pk_customer", label="客户名称主键", isPK=true),
			@Column(name="code", attrName="code", label="客户名称编码"),
			@Column(name="name", label="客户名称", isQuery=false),
		}),
	}, orderBy="a.skdate "
)
public class CtChargeSk extends DataEntity<CtChargeSk> {
	
	private static final long serialVersionUID = 1L;
	private String pkChargeSk;		// 主键
	private Office pkOrg;		// 组织
	private BdDept pkDept;		// 部门
	private ZlProject pkProject;		// 项目信息
	private BdCustomer pkCustomer;		// 客户名称
	private Date skdate;		// 收款日期
	private Double bcTotalAmount;		// 本次收款总额
	private String paymentMethod;		// 付款方式
	private String bankAccount;		// 银行账号
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
	private String  iszz;   //是否自制
	private List<CtChargeSkB> ctChargeSkBList = ListUtils.newArrayList();		// 子表列表
	
	
	public CtChargeSk() {
		this(null);
	}

	public CtChargeSk(String id){
		super(id);
	}
	
	public String getPkChargeSk() {
		return pkChargeSk;
	}

	public void setPkChargeSk(String pkChargeSk) {
		this.pkChargeSk = pkChargeSk;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getSkdate() {
		return skdate;
	}

	public void setSkdate(Date skdate) {
		this.skdate = skdate;
	}
	
	public Date getSkdate_gte() {
		return sqlMap.getWhere().getValue("skdate", QueryType.GTE);
	}

	public void setSkdate_gte(Date skdate) {
		sqlMap.getWhere().and("skdate", QueryType.GTE, skdate);
	}
	
	public Date getSkdate_lte() {
		return sqlMap.getWhere().getValue("skdate", QueryType.LTE);
	}

	public void setSkdate_lte(Date skdate) {
		sqlMap.getWhere().and("skdate", QueryType.LTE, skdate);
	}
	
	public Double getBcTotalAmount() {
		return bcTotalAmount;
	}

	public void setBcTotalAmount(Double bcTotalAmount) {
		this.bcTotalAmount = bcTotalAmount;
	}
	
	@Length(min=0, max=64, message="付款方式长度不能超过 64 个字符")
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	@Length(min=0, max=64, message="银行账号长度不能超过 64 个字符")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
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
	
	public List<CtChargeSkB> getCtChargeSkBList() {
		return ctChargeSkBList;
	}

	public void setCtChargeSkBList(List<CtChargeSkB> ctChargeSkBList) {
		this.ctChargeSkBList = ctChargeSkBList;
	}

	public ZlProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(ZlProject pkProject) {
		this.pkProject = pkProject;
	}

	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}

	public String getIszz() {
		return iszz;
	}

	public void setIszz(String iszz) {
		this.iszz = iszz;
	}


	
}