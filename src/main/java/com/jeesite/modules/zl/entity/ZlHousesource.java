/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
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
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.common.utils.excel.fieldtype.OfficeType;
import com.jeesite.modules.abs.fildtype.BdFormatType;
import com.jeesite.modules.abs.fildtype.UserType;
import com.jeesite.modules.abs.fildtype.ZlBuildingfileType;
import com.jeesite.modules.abs.fildtype.ZlFamilyfileType;
import com.jeesite.modules.abs.fildtype.ZlprojectType;
import com.jeesite.modules.bd.entity.BdFormattype;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * zl_housesourceEntity
 * @author GuoJ
 * @version 2019-07-19
 */
@Table(name="zl_housesource", alias="a", columns={
		@Column(name="pk_housesource", attrName="pkHousesource", label="主键", isPK=true),
		@Column(name="estatecode", attrName="estatecode", label="房产编号", queryType=QueryType.LIKE),
		@Column(name="estatename", attrName="estatename", label="房产名称", queryType=QueryType.LIKE),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织名称"),
		@Column(name="pk_org_v", attrName="pkOrgV", label="组织版本"),
		@Column(name="pk_group", attrName="pkGroup.officeCode", label="集团"),
		@Column(name="projectname", attrName="projectname.pkProject", label="项目名称"),
		@Column(name="buildname", attrName="buildname.pkBuildingfile", label="楼栋"),
		@Column(name="unit", attrName="unit", label="单元"),
		@Column(name="floorn", attrName="floorn", label="楼层"),
		@Column(name="roomnumber", attrName="roomnumber", label="房号"),
		@Column(name="buildarea", attrName="buildarea", label="建筑面积"),
		@Column(name="innerarea", attrName="innerarea", label="套内面积"),
		@Column(name="housestate", attrName="housestate", label="房源状态"),
		@Column(name="pk_familyfile", attrName="pkFamilyfile.pkFamilyfile", label="户型信息"),
		@Column(name="isentity", attrName="isentity", label="是否实体"),
		@Column(name="entitypk", attrName="entitypk", label="主实体主键"),
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
		@Column(name="pk_formattype", attrName="pkFormattype.pkFormattype", label="业态"),
		@Column(name="hbcfstatus", attrName="hbcfstatus", label="合并拆分状态"),
		@Column(name="parentroom", attrName="parentroom", label="上级房号"),
		@Column(name="zstroom", attrName="zstroom", label="主实体房号"),
		@Column(name="zstpk", attrName="zstpk", label="主实体主键"),
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
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="projectname", alias="u13",
			on="u13.pk_project = a.projectname", columns={
				@Column(name="pk_project", label="用户编码", isPK=true),
				@Column(name="code", attrName="code", label="编码"),
				@Column(name="name", label="用户名称", isQuery=false),
		}),
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlBuildingfile.class, attrName="buildname", alias="u14",
				on="u14.pk_buildingfile = a.buildname", columns={
					@Column(name="pk_buildingfile", label="机构编码", isPK=true),
					@Column(name="code", attrName="code", label="编码"),
					@Column(name="name", label="机构名称", isQuery=false),
			}),
			@JoinTable(type=Type.LEFT_JOIN, entity=ZlFamilyfile.class, attrName="pkFamilyfile", alias="u15",
			on="u15.pk_familyfile = a.pk_familyfile", columns={
				@Column(name="pk_familyfile", label="主键", isPK=true),
				@Column(name="code", attrName="code", label="编码"),
				@Column(name="name", label="名称", isQuery=false),
				@Column(name="is_cw", label="是否车位", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdFormattype.class, attrName="pkFormattype", alias="u16",
		on="u16.pk_formattype = a.pk_formattype", columns={
			@Column(name="pk_formattype", label="机构编码", isPK=true),
			@Column(name="code", attrName="code", label="编码"),
			@Column(name="name", label="机构名称", isQuery=false),
	}),
}, extWhereKeys="extquery,wghtqueryhouse", orderBy="a.projectname ASC,a.buildname ASC,a.floorn DESC,a.unit ASC,a.roomnumber ASC"
)
public class ZlHousesource extends DataEntity<ZlHousesource> {
	
	private static final long serialVersionUID = 1L;
	private String pkHousesource;		// 主键
	private String estatecode;		// 房产编号
	private String estatename;		// 房产名称
	private Office pkOrg;		// 组织名称
	private String pkOrgV;		// 组织版本
	private Office pkGroup;		// 集团
	private ZlProject projectname;		// 项目名称
	private ZlBuildingfile buildname;		// 楼栋
	private String unit;		// 单元
	private String floorn;		// 楼层
	private String roomnumber;		// 房号
	private Double buildarea;		// 租赁面积
	private Double innerarea;		// 产证面积
	private Integer housestate;		// 房源状态
	private ZlFamilyfile pkFamilyfile;		// 户型信息
	private String isentity;		// 是否实体
	private String entitypk;		// 主实体主键
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
	private BdFormattype pkFormattype;		// 业态
	private Integer hbcfstatus;		// 合并拆分状态
	private String parentroom; //上级房号
	private String zstroom;   //主实体房号
	private String zstpk;   //主实体主键
	
	public ZlHousesource() {
		this(null);
	}

	public ZlHousesource(String id){
		super(id);
	}
	@ExcelFields({
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="房产编号", attrName="estatecode", align=Align.CENTER, sort=10, type=com.jeesite.common.utils.excel.annotation.ExcelField.Type.EXPORT),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="房产名称", attrName="estatename", align=Align.CENTER, sort=20, type=com.jeesite.common.utils.excel.annotation.ExcelField.Type.EXPORT),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="组织名称", attrName="pkOrg", align=Align.CENTER, sort=30, fieldType=OfficeType.class),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="项目名称", attrName="projectname", align=Align.CENTER, sort=40, fieldType=ZlprojectType.class),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="户型信息", attrName="pkFamilyfile", align=Align.CENTER, sort=50, fieldType=ZlFamilyfileType.class),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="业态", attrName="pkFormattype", align=Align.CENTER, sort=60, fieldType=BdFormatType.class, type=com.jeesite.common.utils.excel.annotation.ExcelField.Type.EXPORT),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="楼栋", attrName="buildname", align=Align.CENTER, sort=70, fieldType=ZlBuildingfileType.class),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="单元", attrName="unit", align=Align.RIGHT, sort=80),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="楼层", attrName="floorn", align=Align.RIGHT, sort=90),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="房号", attrName="roomnumber", align=Align.CENTER, sort=100),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="面积", attrName="buildarea", align=Align.RIGHT, sort=110),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="房源状态", attrName="housestate", align=Align.RIGHT, sort=120,dictType="zl_housestatus"),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="制单人", attrName="creator", align=Align.CENTER, sort=140, fieldType=UserType.class, type=com.jeesite.common.utils.excel.annotation.ExcelField.Type.EXPORT),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="制单日期", attrName="dbilldate", align=Align.CENTER, sort=150, dataFormat="yyyy-MM-dd", type=com.jeesite.common.utils.excel.annotation.ExcelField.Type.EXPORT),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="最后修改人", attrName="modifier", align=Align.CENTER, sort=170, fieldType=UserType.class, type=com.jeesite.common.utils.excel.annotation.ExcelField.Type.EXPORT),
		@com.jeesite.common.utils.excel.annotation.ExcelField(title="最后修改时间", attrName="modifiedtime", align=Align.CENTER, sort=180, dataFormat="yyyy-MM-dd HH:mm", type=com.jeesite.common.utils.excel.annotation.ExcelField.Type.EXPORT),
	})
	public String getPkHousesource() {
		return pkHousesource;
	}

	public void setPkHousesource(String pkHousesource) {
		this.pkHousesource = pkHousesource;
	}
	
	@Length(min=0, max=50, message="房产编号长度不能超过 50 个字符")
	public String getEstatecode() {
		return estatecode;
	}

	public void setEstatecode(String estatecode) {
		this.estatecode = estatecode;
	}
	
	@Length(min=0, max=50, message="房产名称长度不能超过 50 个字符")
	public String getEstatename() {
		return estatename;
	}

	public void setEstatename(String estatename) {
		this.estatename = estatename;
	}
	

	@Length(min=0, max=20, message="组织版本长度不能超过 20 个字符")
	public String getPkOrgV() {
		return pkOrgV;
	}

	public void setPkOrgV(String pkOrgV) {
		this.pkOrgV = pkOrgV;
	}

	

	@Length(min=0, max=50, message="单元长度不能超过 50 个字符")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Length(min=0, max=50, message="楼层长度不能超过 50 个字符")
	public String getFloorn() {
		return floorn;
	}

	public void setFloorn(String floorn) {
		this.floorn = floorn;
	}
	
	@Length(min=0, max=50, message="房号长度不能超过 50 个字符")
	public String getRoomnumber() {
		return roomnumber;
	}

	public void setRoomnumber(String roomnumber) {
		this.roomnumber = roomnumber;
	}
	
	public Double getBuildarea() {
		return buildarea;
	}

	public void setBuildarea(Double buildarea) {
		this.buildarea = buildarea;
	}
	
	public Double getInnerarea() {
		return innerarea;
	}

	public void setInnerarea(Double innerarea) {
		this.innerarea = innerarea;
	}
	

	

	
	@Length(min=0, max=50, message="是否实体长度不能超过 50 个字符")
	public String getIsentity() {
		return isentity;
	}

	public void setIsentity(String isentity) {
		this.isentity = isentity;
	}
	
	@Length(min=0, max=50, message="主实体主键长度不能超过 50 个字符")
	public String getEntitypk() {
		return entitypk;
	}

	public void setEntitypk(String entitypk) {
		this.entitypk = entitypk;
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

	public ZlProject getProjectname() {
		return projectname;
	}

	public void setProjectname(ZlProject projectname) {
		this.projectname = projectname;
	}

	public ZlBuildingfile getBuildname() {
		return buildname;
	}

	public void setBuildname(ZlBuildingfile buildname) {
		this.buildname = buildname;
	}

	public ZlFamilyfile getPkFamilyfile() {
		return pkFamilyfile;
	}

	public void setPkFamilyfile(ZlFamilyfile pkFamilyfile) {
		this.pkFamilyfile = pkFamilyfile;
	}

//	public ZlFormattype getPkFormattype() {
//		return pkFormattype;
//	}
//
//	public void setPkFormattype(ZlFormattype pkFormattype) {
//		this.pkFormattype = pkFormattype;
//	}

	public Integer getDr() {
		sqlMap.getWhere().and("dr", QueryType.EQ, 0);
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public BdFormattype getPkFormattype() {
		return pkFormattype;
	}

	public void setPkFormattype(BdFormattype pkFormattype) {
		this.pkFormattype = pkFormattype;
	}

	public Integer getHousestate() {
		return housestate;
	}

	public void setHousestate(Integer housestate) {
		this.housestate = housestate;
	}

	public Integer getHbcfstatus() {
		return hbcfstatus;
	}

	public void setHbcfstatus(Integer hbcfstatus) {
		this.hbcfstatus = hbcfstatus;
	}



	public String getZstroom() {
		return zstroom;
	}

	public void setZstroom(String zstroom) {
		this.zstroom = zstroom;
	}

	public String getParentroom() {
		return parentroom;
	}

	public void setParentroom(String parentroom) {
		this.parentroom = parentroom;
	}

	public String getZstpk() {
		return zstpk;
	}

	public void setZstpk(String zstpk) {
		this.zstpk = zstpk;
	}

	
}