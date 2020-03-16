/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * bd_deptEntity
 * @author gj
 * @version 2019-04-24
 */
@Table(name="bd_dept", alias="a", columns={
		@Column(name="dept_code", attrName="deptCode", label="部门编码"),
		@Column(name="pk_dept", attrName="pkDept", label="部门主键",isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(includeEntity=DataEntity.class),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="tree_name", attrName="treeName", label="节点名称"),//未用
		@Column(name="dept_name", attrName="deptName", label="部门名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(name="dept_admin", attrName="deptAdmin.userCode", label="部门管理员"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="d1",
		on="d1.office_code = a.pk_org", columns={
			@Column(name="office_code", label="机构编码", isPK=true),
			@Column(name="office_name", label="机构名称", isQuery=false),
		}),
	
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="deptAdmin", alias="u3",
		on="u3.user_code = a.dept_admin", columns={
			@Column(name="user_code", label="用户编码", isPK=true),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
},extWhereKeys="dsf", orderBy="a.dept_code"
)
public class BdDept extends TreeEntity<BdDept> {
	
	private static final long serialVersionUID = 1L;
	private String pkDept;		// 部门主键
	private Office pkOrg;		// 组织编码
	private String treeName;		// 节点名称
	private String deptCode;		// 部门编码
	private String deptName;		// 部门名称
	private User deptAdmin;       //部门管理员
	
	public BdDept() {
		this(null);
	}

	public BdDept(String id){
		super(id);
	}
	
	@Override
	public BdDept getParent() {
		return parent;
	}

	@Override
	public void setParent(BdDept parent) {
		this.parent = parent;
	}

	public User getDeptAdmin() {
		return deptAdmin;
	}

	public void setDeptAdmin(User deptAdmin) {
		this.deptAdmin = deptAdmin;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}

	@Length(min=0, max=100, message="节点名称长度不能超过 100 个字符")
	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Length(min=0, max=50, message="部门名称长度不能超过 50 个字符")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}