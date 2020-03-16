/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.bd.entity;

import java.util.Date;

import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 收费标准Entity
 * @author GJ
 * @version 2019-10-25
 */
@Table(name="bd_feescale", alias="a", columns={
		@Column(name="pk_feescale", attrName="pkFeescale", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(name="code", attrName="code", label="编码", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="名称", queryType=QueryType.LIKE),
		@Column(name="pk_project", attrName="pkProject.pkProject", label="收费项目"),
		@Column(name="ca_method", attrName="caMethod", label="计算方式"),
		@Column(name="ca_amount", attrName="caAmount", label="计算数额"),
		@Column(name="round", attrName="round", label="舍入方式"),
		@Column(name="create_by", attrName="creator.userCode", label="创建人"),
		@Column(name="update_by", attrName="modifier.userCode", label="修改人"),
		@Column(name="status", attrName="status", label="状态"),
		@Column(name="create_date", attrName="createDate", label="创建时间"),
		@Column(name="update_date", attrName="updateDate", label="修改时间"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u2",
			on="u2.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
	
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u9",
			on="u9.user_code = a.create_by", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u11",
			on="u11.user_code = a.update_by", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdProject.class, attrName="pkProject", alias="u12",
		on="u12.pk_project = a.pk_project", columns={
			@Column(name="pk_project", label="收费项目主键", isPK=true),
			@Column(name="name", label="收费项目名称", isQuery=false),
	}),
	}, orderBy="a.update_date DESC"
)
public class BdFeescale extends DataEntity<BdFeescale> {
	
	private static final long serialVersionUID = 1L;
	private String pkFeescale;		// 主键
	private Office pkOrg;		// 组织
	private String code;		// 编码
	private String name;		// 名称
	private BdProject pkProject;		// 收费项目
	private String caMethod;		// 计算方式
	private Double caAmount;		// 计算数额
	private String round;		// 舍入方式
	private User  creator; //创建人
	private User modifier; //修改人
	private String status;//状态
	private Date createDate;//创建时间
	private Date updateDate;//修改时间
	
	public BdFeescale() {
		this(null);
	}

	public BdFeescale(String id){
		super(id);
	}
	
	public String getPkFeescale() {
		return pkFeescale;
	}

	public void setPkFeescale(String pkFeescale) {
		this.pkFeescale = pkFeescale;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	@Length(min=0, max=255, message="编码长度不能超过 255 个字符")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=255, message="名称长度不能超过 255 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	
	public BdProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(BdProject pkProject) {
		this.pkProject = pkProject;
	}

	@Length(min=0, max=1, message="计算方式长度不能超过 1 个字符")
	public String getCaMethod() {
		return caMethod;
	}

	public void setCaMethod(String caMethod) {
		this.caMethod = caMethod;
	}
	
	public Double getCaAmount() {
		return caAmount;
	}

	public void setCaAmount(Double caAmount) {
		this.caAmount = caAmount;
	}
	
	@Length(min=0, max=1, message="舍入方式长度不能超过 1 个字符")
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