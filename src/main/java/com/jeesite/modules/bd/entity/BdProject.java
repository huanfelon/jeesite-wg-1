/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.entity;

import java.util.Date;

import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * 预算项目Entity
 * @author GJ
 * @version 2019-10-25
 */
@Table(name="bd_project", alias="a", columns={
		@Column(name="pk_project", attrName="pkProject", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织",isQuery=false),
		@Column(name="code", attrName="code", label="编码", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(name="round", attrName="round", label="舍入方式"),
		@Column(name="pk_yslb", attrName="pkYslb.pkCategory", label="预算类别"),
		@Column(name="create_by", attrName="creator.userCode", label="创建人"),
		@Column(name="update_by", attrName="modifier.userCode", label="修改人"),
		@Column(name="status", attrName="status", label="状态"),
		@Column(name="create_date", attrName="createDate", label="创建时间"),
		@Column(name="update_date", attrName="updateDate", label="修改时间"),
		@Column(includeEntity=TreeEntity.class),
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=BdCategory.class, attrName="pkYslb", alias="y1",
				on="y1.pk_category = a.pk_yslb", columns={
				@Column(name="pk_category", label="主键", isPK=true, isQuery=false),
				@Column(name="code", label="编码", isQuery=false),
				@Column(name="name", label="名称", isQuery=false),
			}),
			@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u12",
				on="u12.user_code = a.create_by", columns={
					@Column(name="user_code", label="用户编码", isPK=true),
					@Column(name="user_name", label="用户名称", isQuery=false),
			}),

			@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u14",
				on="u14.user_code = a.update_by", columns={
					@Column(name="user_code", label="用户编码", isPK=true),
					@Column(name="user_name", label="用户名称", isQuery=false),
			}),
			@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u11",
			on="u11.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true,isQuery=false),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
		},orderBy="a.tree_sorts, a.pk_project"
)
public class BdProject extends TreeEntity<BdProject> {
	
	private static final long serialVersionUID = 1L;
	private String pkProject;		// 主键
	private Office pkOrg;		// 组织
	private String code;		// 编码
	private String name;		// 名称
	private BdCategory pkYslb;		//预算类别
	private String round;		// 舍入方式
	private User  creator; //创建人
	private User modifier; //修改人
	private String status;//状态
	private Date createDate;//创建时间
	private Date updateDate;//修改时间
	
	public BdProject() {
		this(null);
	}

	public BdProject(String id){
		super(id);
	}
	
	@Override
	public BdProject getParent() {
		return parent;
	}

	@Override
	public void setParent(BdProject parent) {
		this.parent = parent;
	}
	
	public String getPkProject() {
		return pkProject;
	}

	public void setPkProject(String pkProject) {
		this.pkProject = pkProject;
	}
	

	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public BdCategory getPkYslb() {
		return pkYslb;
	}

	public void setPkYslb(BdCategory pkYslb) {
		this.pkYslb = pkYslb;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}