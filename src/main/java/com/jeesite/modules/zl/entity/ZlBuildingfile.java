/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * zl_buildingfileEntity
 * @author GuoJ
 * @version 2019-07-19
 */
@Table(name="zl_buildingfile", alias="a", columns={
		@Column(name="pk_buildingfile", attrName="pkBuildingfile", label="主键", isPK=true),
		@Column(name="code", attrName="code", label="编码", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="名称", queryType=QueryType.LIKE),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(name="pk_org_v", attrName="pkOrgV", label="组织版本"),
		@Column(name="pk_group", attrName="pkGroup.officeCode", label="集团"),
		@Column(name="pk_projectid", attrName="pkProjectid.pkProject", label="项目信息"),
		@Column(name="nunit", attrName="nunit", label="单元数量"),
		@Column(name="nbuilding", attrName="nbuilding", label="楼栋层数"),
		@Column(name="nproperty", attrName="nproperty", label="房产数量"),
		@Column(name="builtuparea", attrName="builtuparea", label="建筑面积"),
		@Column(name="innerarea", attrName="innerarea", label="套内面积"),
		@Column(name="zlarea", attrName="zlarea", label="租赁面积"),
		@Column(name="isbuild", attrName="isbuild", label="是否建房"),
		@Column(name="dbilldate", attrName="dbilldate", label="制单日期"),
		@Column(name="creator", attrName="creator.userCode", label="创建人"),
		@Column(name="creationtime", attrName="creationtime", label="创建时间"),
		@Column(name="modifier", attrName="modifier.userCode", label="修改人"),
		@Column(name="modifiedtime", attrName="modifiedtime", label="修改时间"),
		@Column(name="vdef1", attrName="vdef1", label="vdef1"),
		@Column(name="vdef2", attrName="vdef2", label="vdef2"),
		@Column(name="vdef3", attrName="vdef3", label="vdef3"),
		@Column(name="vdef4", attrName="vdef4", label="vdef4"),
		@Column(name="vdef5", attrName="vdef5", label="vdef5"),
		@Column(name="personalarea", attrName="personalarea", label="自用面积"),
		@Column(name="ts", attrName="ts", label="ts"),
		@Column(name="dr", attrName="dr", label="dr"),
	},joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u10",
					on="u10.user_code = a.creator", columns={
						@Column(name="user_code", label="用户编码", isPK=true),
						@Column(name="user_name", label="用户名称", isQuery=false),
				}),
				@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u9",
				on="u9.user_code = a.modifier", columns={
					@Column(name="user_code", label="用户编码", isPK=true),
					@Column(name="user_name", label="用户名称", isQuery=false),
			}),
				@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u11",
					on="u11.office_code = a.pk_org", columns={
						@Column(name="office_code", label="机构编码", isPK=true),
						@Column(name="office_name", label="机构名称", isQuery=false),
				}),
				@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkGroup", alias="u12",
				on="u12.office_code = a.pk_group", columns={
					@Column(name="office_code", label="机构编码", isPK=true),
					@Column(name="office_name", label="机构名称", isQuery=false),
			}),
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProjectid", alias="u13",
			on="u13.pk_project = a.pk_projectid", columns={
				@Column(name="pk_project", label="机构编码", isPK=true),
				@Column(name="code", attrName="code", label="编码"),
				@Column(name="name", label="机构名称", isQuery=false),
		}),
}, extWhereKeys="extquery", orderBy="a.pk_projectid  ASC,a.code ASC"
)
public class ZlBuildingfile extends DataEntity<ZlBuildingfile> {
	
	private static final long serialVersionUID = 1L;
	private String pkBuildingfile;		// 主键
	private String code;		// 编码
	private String name;		// 名称
	private Office pkOrg;		// 组织
	private String pkOrgV;		// 组织版本
	private Office pkGroup;		// 集团
	private ZlProject pkProjectid;		// 项目信息
	private Integer nunit;		// 单元数量
	private Integer nbuilding;		// 楼栋层数
	private Integer nproperty;		// 房产数量
	private Double builtuparea;		// 建筑面积
	private Double innerarea;		// 套内面积
	private Double zlarea;		// 租赁面积
	private String isbuild;		// 是否建房
	private Date dbilldate;		// 制单日期
	private User creator;		// 创建人
	private Date creationtime;		// 创建时间
	private User modifier;		// 修改人
	private Date modifiedtime;		// 修改时间
	private String vdef1;		// vdef1
	private String vdef2;		// vdef2
	private String vdef3;		// vdef3
	private String vdef4;		// vdef4
	private String vdef5;		// vdef5
	private Double personalarea;		// 自用面积
	private Date ts;		// ts
	private Integer dr;		// dr
	
	public ZlBuildingfile() {
		this(null);
	}

	public ZlBuildingfile(String id){
		super(id);
	}
	
	public String getPkBuildingfile() {
		return pkBuildingfile;
	}

	public void setPkBuildingfile(String pkBuildingfile) {
		this.pkBuildingfile = pkBuildingfile;
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
	

	
	@Length(min=0, max=20, message="组织版本长度不能超过 20 个字符")
	public String getPkOrgV() {
		return pkOrgV;
	}

	public void setPkOrgV(String pkOrgV) {
		this.pkOrgV = pkOrgV;
	}
	

	

	

	
	public Double getBuiltuparea() {
		return builtuparea;
	}

	public void setBuiltuparea(Double builtuparea) {
		this.builtuparea = builtuparea;
	}
	
	public Double getInnerarea() {
		return innerarea;
	}

	public void setInnerarea(Double innerarea) {
		this.innerarea = innerarea;
	}
	
	public Double getZlarea() {
		return zlarea;
	}

	public void setZlarea(Double zlarea) {
		this.zlarea = zlarea;
	}
	
	@Length(min=0, max=1, message="是否建房长度不能超过 1 个字符")
	public String getIsbuild() {
		return isbuild;
	}

	public void setIsbuild(String isbuild) {
		this.isbuild = isbuild;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(Date dbilldate) {
		this.dbilldate = dbilldate;
	}
	
/*	public Date getDbilldate_gte() {
		return sqlMap.getWhere().getValue("dbilldate", QueryType.GTE);
	}

	public void setDbilldate_gte(Date dbilldate) {
		sqlMap.getWhere().and("dbilldate", QueryType.GTE, dbilldate);
	}
	
	public Date getDbilldate_lte() {
		return sqlMap.getWhere().getValue("dbilldate", QueryType.LTE);
	}

	public void setDbilldate_lte(Date dbilldate) {
		sqlMap.getWhere().and("dbilldate", QueryType.LTE, dbilldate);
	}*/
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(Date creationtime) {
		this.creationtime = creationtime;
	}
	

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(Date modifiedtime) {
		this.modifiedtime = modifiedtime;
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
	
	public Double getPersonalarea() {
		return personalarea;
	}

	public void setPersonalarea(Double personalarea) {
		this.personalarea = personalarea;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	


	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}

	public Office getPkGroup() {
		return pkGroup;
	}

	public void setPkGroup(Office pkGroup) {
		this.pkGroup = pkGroup;
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

	public ZlProject getPkProjectid() {
		return pkProjectid;
	}

	public void setPkProjectid(ZlProject pkProjectid) {
		this.pkProjectid = pkProjectid;
	}

	public Integer getNunit() {
		return nunit;
	}

	public void setNunit(Integer nunit) {
		this.nunit = nunit;
	}

	public Integer getNbuilding() {
		return nbuilding;
	}

	public void setNbuilding(Integer nbuilding) {
		this.nbuilding = nbuilding;
	}

	public Integer getNproperty() {
		return nproperty;
	}

	public void setNproperty(Integer nproperty) {
		this.nproperty = nproperty;
	}

	public Integer getDr() {
		sqlMap.getWhere().and("dr", QueryType.EQ, 0);
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
}