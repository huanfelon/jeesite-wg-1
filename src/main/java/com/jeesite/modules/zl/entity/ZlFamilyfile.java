/**
dbilldate * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.zl.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.bd.entity.BdFormattype;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * zl_familyfileEntity
 * @author GuoJ
 * @version 2019-07-19
 */
@Table(name="zl_familyfile", alias="a", columns={
		@Column(name="pk_familyfile", attrName="pkFamilyfile", label="主键", isPK=true),
		@Column(name="code", attrName="code", label="编码", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="名称", queryType=QueryType.LIKE),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(name="pk_org_v", attrName="pkOrgV", label="组织版本"),
		@Column(name="pk_group", attrName="pkGroup.officeCode", label="集团"),
		@Column(name="zlarea", attrName="zlarea", label="租赁面积"),
		@Column(name="pk_projectid", attrName="pkProjectid.pkProject", label="项目信息"),
		@Column(name="builtuparea", attrName="builtuparea", label="建筑面积"),
		@Column(name="innerarea", attrName="innerarea", label="套内面积"),
		@Column(name="pk_formattypeid", attrName="pkFormattypeid.pkFormattype", label="业态"),
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
		@Column(name="ts", attrName="ts", label="ts"),
		@Column(name="dr", attrName="dr", label="dr"),
		@Column(name="is_cw", attrName="isCw", label="是否车位"),
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
		@JoinTable(type=Type.LEFT_JOIN, entity=BdFormattype.class, attrName="pkFormattypeid", alias="u14",
		on="u14.pk_formattype = a.pk_formattypeid", columns={
			@Column(name="pk_formattype", label="机构编码", isPK=true),
			@Column(name="code", attrName="code", label="编码"),
			@Column(name="name", label="机构名称", isQuery=false),
	}),
			}, orderBy="a.code ASC"
)
public class ZlFamilyfile extends DataEntity<ZlFamilyfile> {
	
	private static final long serialVersionUID = 1L;
	private String pkFamilyfile;		// 主键
	private String code;		// 编码
	private String name;		// 名称
	private Office pkOrg;		// 组织
	private String pkOrgV;		// 组织版本
	private Office pkGroup;		// 集团
	private Double zlarea;		// 租赁面积
	private ZlProject pkProjectid;		// 项目信息
	private Double builtuparea;		// 建筑面积
	private Double innerarea;		// 套内面积
	private BdFormattype pkFormattypeid;		// 业态
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
	private Date ts;		// ts
	private Integer dr;		// dr
	private String isCw ;   //是否车位
	
	public ZlFamilyfile() {
		this(null);
	}

	public ZlFamilyfile(String id){
		super(id);
	}
	
	public String getPkFamilyfile() {
		return pkFamilyfile;
	}

	public void setPkFamilyfile(String pkFamilyfile) {
		this.pkFamilyfile = pkFamilyfile;
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
	

	public Double getZlarea() {
		return zlarea;
	}

	public void setZlarea(Double zlarea) {
		this.zlarea = zlarea;
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

//	public ZlFormattype getPkFormattypeid() {
//		return pkFormattypeid;
//	}
//
//	public void setPkFormattypeid(ZlFormattype pkFormattypeid) {
//		this.pkFormattypeid = pkFormattypeid;
//	}

	public String getIsCw() {
		return isCw;
	}

	public void setIsCw(String isCw) {
		this.isCw = isCw;
	}

	public Integer getDr() {
		sqlMap.getWhere().and("dr", QueryType.EQ, 0);
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public BdFormattype getPkFormattypeid() {
		return pkFormattypeid;
	}

	public void setPkFormattypeid(BdFormattype pkFormattypeid) {
		this.pkFormattypeid = pkFormattypeid;
	}
	
}