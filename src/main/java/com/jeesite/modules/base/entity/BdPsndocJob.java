/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.entity;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.modules.sys.entity.Office;

/**
 * bd_psndocEntity
 * @author tcl
 * @version 2019-11-04
 */
@Table(name="bd_psndoc_job", alias="a", columns={
		@Column(name="pk_psndoc_job", attrName="pkPsndocJob", label="主键", isPK=true),
		@Column(name="pk_psndoc", attrName="pkPsndoc.pkPsndoc", label="父主键"),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="任职业务单元",isUpdateForce=true),
		@Column(name="pk_deptc", attrName="pkDeptc.pkDept", label="任职部门",isUpdateForce=true),
		@Column(name="pk_deptcall", attrName="pkDeptcall.pkDept", label="权限部门",isUpdateForce=true),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u3",
			on="u3.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDeptc", alias="d1",
			on="d1.pk_dept = a.pk_deptc", columns={
			@Column(name="pk_dept", attrName="pkDept", label="部门主键",isPK=true),
			@Column(name="dept_code", attrName="deptCode", label="部门编码", isQuery=false),
			@Column(name="dept_name", attrName="deptName", label="部门名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDeptcall", alias="d11",
			on="d11.pk_dept = a.pk_deptcall", columns={
			@Column(name="pk_dept", attrName="pkDept", label="部门主键",isPK=true),
			@Column(name="dept_code", attrName="deptCode", label="部门编码", isQuery=false),
			@Column(name="dept_name", attrName="deptName", label="部门名称", isQuery=false),
		}),
		}, orderBy="a.pk_psndoc_job ASC"
)
public class BdPsndocJob extends DataEntity<BdPsndocJob> {
	
	private static final long serialVersionUID = 1L;
	private String pkPsndocJob;		// 主键
	private BdPsndoc pkPsndoc;		// 父主键 父类
	private Office pkOrg;		// 任职业务单元
	private BdDept pkDeptc;		// 任职部门
	private BdDept pkDeptcall;		// 权限部门
	
	public BdPsndocJob() {
		this(null);
	}


	public BdPsndocJob(BdPsndoc pkPsndoc){
		this.pkPsndoc = pkPsndoc;
	}
	
	public String getPkPsndocJob() {
		return pkPsndocJob;
	}

	public void setPkPsndocJob(String pkPsndocJob) {
		this.pkPsndocJob = pkPsndocJob;
	}
	
	@NotBlank(message="父主键不能为空")
	@Length(min=0, max=64, message="父主键长度不能超过 64 个字符")
	public BdPsndoc getPkPsndoc() {
		return pkPsndoc;
	}

	public void setPkPsndoc(BdPsndoc pkPsndoc) {
		this.pkPsndoc = pkPsndoc;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}


	public BdDept getPkDeptc() {
		return pkDeptc;
	}


	public void setPkDeptc(BdDept pkDeptc) {
		this.pkDeptc = pkDeptc;
	}


	public BdDept getPkDeptcall() {
		return pkDeptcall;
	}


	public void setPkDeptcall(BdDept pkDeptcall) {
		this.pkDeptcall = pkDeptcall;
	}
	

}