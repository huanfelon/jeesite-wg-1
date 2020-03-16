/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * bd_categoryEntity
 * @author GJ
 * @version 2019-10-16
 */
@Table(name="bd_category", alias="a", columns={
		@Column(name="pk_category", attrName="pkCategory", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织",isQuery=false),
		@Column(name="code", attrName="code", label="编码", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(name="create_by", attrName="creator.userCode", label="创建人"),
		@Column(name="update_by", attrName="modifier.userCode", label="修改人"),
		@Column(name="status", attrName="status", label="状态"),
		@Column(name="create_date", attrName="createDate", label="创建时间"),
		@Column(name="update_date", attrName="updateDate", label="修改时间"),
		@Column(includeEntity=TreeEntity.class),
	}, joinTable={
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
	}, orderBy="a.tree_sorts, a.pk_category"
)
public class BdCategory extends TreeEntity<BdCategory> {
	
	private static final long serialVersionUID = 1L;
	private String pkCategory;		// 主键
	private Office pkOrg;		// 组织
	private String code;		// 编码
	private String name;		// 名称
	private User  creator; //创建人
	private User modifier; //修改人
	private String status;//状态
	private Date createDate;//创建时间
	private Date updateDate;//修改时间
	
	
	public BdCategory() {
		this(null);
	}

	public BdCategory(String id){
		super(id);
	}
	
	@Override
	public BdCategory getParent() {
		return parent;
	}

	@Override
	public void setParent(BdCategory parent) {
		this.parent = parent;
	}
	
	public String getPkCategory() {
		return pkCategory;
	}

	public void setPkCategory(String pkCategory) {
		this.pkCategory = pkCategory;
	}
	

	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}

	@Length(min=0, max=50, message="编码长度不能超过 50 个字符")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=50, message="名称长度不能超过 50 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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