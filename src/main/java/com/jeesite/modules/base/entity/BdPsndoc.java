/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.base.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.fieldtype.OfficeType;
import com.jeesite.modules.abs.fildtype.BdDeptObj;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

/**
 * 人员基本信息Entity
 * @author LY
 * @version 2019-09-26
 */
@Table(name="bd_psndoc", alias="a", columns={
		@Column(name="pk_psndoc", attrName="pkPsndoc", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="组织"),
		@Column(name="code", attrName="code", label="人员编码", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="姓名", queryType=QueryType.LIKE),
		@Column(name="netname", attrName="netname", label="昵称"),
		@Column(name="nameused", attrName="nameused", label="曾用名"),
		@Column(name="birthdate", attrName="birthdate", label="出生日期"),
		@Column(name="sex", attrName="sex", label="性别"),
		@Column(name="idno", attrName="idno", label="身份证号"),
		@Column(name="phone", attrName="phone", label="电话"),
		@Column(name="psntype", attrName="psntype", label="人员类别"),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="所属部门"),
		@Column(name="datacontrol", attrName="datacontrol", label="数据权限"),
		@Column(name="pk_deptall", attrName="pkDeptall.pkDept", label="权限部门"),
		@Column(name="status", attrName="status", label="状态", isUpdate=false),
		@Column(name="create_by", attrName="creator.userCode", label="创建人"),
		@Column(name="create_date", attrName="createDate", label="创建时间"),
		@Column(name="update_by", attrName="modifier.userCode", label="修改人"),
		@Column(name="update_date", attrName="updateDate", label="修改时间"),
	},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="o1",
			on="o1.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="d1",
			on="d1.pk_dept = a.pk_dept", columns={
			@Column(name="pk_dept", attrName="pkDept", label="部门主键",isPK=true),
			@Column(name="dept_code", attrName="deptCode", label="部门编码", isQuery=false),
			@Column(name="dept_name", attrName="deptName", label="部门名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDeptall", alias="d11",
		on="d11.pk_dept = a.pk_deptall", columns={
		@Column(name="pk_dept", attrName="pkDept", label="部门主键",isPK=true),
		@Column(name="dept_code", attrName="deptCode", label="部门编码", isQuery=false),
		@Column(name="dept_name", attrName="deptName", label="部门名称", isQuery=false),
	}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="c1",
			on="c1.user_code = a.create_by", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="c2",
			on="c2.user_code = a.update_by", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
	}, orderBy="a.update_date DESC"
)
public class BdPsndoc extends DataEntity<BdPsndoc> {
	
	private static final long serialVersionUID = 1L;
	private String pkPsndoc;		// 主键
	@ExcelField(title="所属组织", attrName="pkOrg",align=ExcelField.Align.CENTER, sort=1,fieldType=OfficeType.class)
	private Office pkOrg;		// 组织
	@ExcelField(title="人员编码", attrName="code", align=ExcelField.Align.CENTER, sort=4)
	private String code;		// 人员编码
	@ExcelField(title="姓名", attrName="name", align=ExcelField.Align.CENTER, sort=5)
	private String name;		// 姓名
	@ExcelField(title="昵称", attrName="netname", align=ExcelField.Align.CENTER, sort=6)
	private String netname;		// 昵称
	@ExcelField(title="曾用名", attrName="nameused", align=ExcelField.Align.CENTER, sort=7)
	private String nameused;		// 曾用名
	@ExcelField(title="出生日期", attrName="birthdate", dataFormat="yyyy-MM-dd", width=6000, align=ExcelField.Align.CENTER, sort=11)
	private Date birthdate;		// 出生日期
	@ExcelField(title="性别", attrName="sex", dictType="sys_user_sex", align=ExcelField.Align.CENTER, sort=20)
	private String sex;		// 性别
	@ExcelField(title="身份证号", attrName="idno", align=ExcelField.Align.CENTER, sort=22)
	private String idno;		// 身份证号
	@ExcelField(title="电话", attrName="phone", align=ExcelField.Align.CENTER, sort=23)
	private String phone;		// 电话
	@ExcelField(title="人员类别", attrName="psntype", dictType="wg_psntype", align=ExcelField.Align.CENTER, sort=25)
	private String psntype;		//人员类别
	@ExcelField(title="所属部门", attrName="pkDept", align=ExcelField.Align.CENTER, sort=30, fieldType=BdDeptObj.class)
	private BdDept pkDept;		//所属部门
	private String datacontrol;		//数据权限
	//@ExcelField(title="权限部门", attrName="pkDeptall", align=ExcelField.Align.CENTER, sort=40, fieldType=BdDeptObj.class)
	private BdDept pkDeptall;		// 权限部门
	private String status;		//状态(推荐状态：0：正常；1：删除；2：停用；3：冻结；4：审核、待审核；5：审核驳回；9：草稿)
	private User creator;		//创建人
	private Date createDate;		//创建时间
	private User modifier;		//修改人
	private Date updateDate;		//修改时间
	
	private List<BdPsndocJob> bdPsndocJobList = ListUtils.newArrayList();		// 子表列表
	
	public BdPsndoc() {
		this(null);
	}

	public BdPsndoc(String id){
		super(id);
	}
	
	public String getPkPsndoc() {
		return pkPsndoc;
	}

	public void setPkPsndoc(String pkPsndoc) {
		this.pkPsndoc = pkPsndoc;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	@Length(min=0, max=64, message="人员编码长度不能超过 64 个字符")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=200, message="姓名长度不能超过 200 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=200, message="昵称长度不能超过 200 个字符")
	public String getNetname() {
		return netname;
	}

	public void setNetname(String netname) {
		this.netname = netname;
	}
	
	@Length(min=0, max=200, message="曾用名长度不能超过 200 个字符")
	public String getNameused() {
		return nameused;
	}

	public void setNameused(String nameused) {
		this.nameused = nameused;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public Date getBirthdate_gte() {
		return sqlMap.getWhere().getValue("birthdate", QueryType.GTE);
	}

	public void setBirthdate_gte(Date birthdate) {
		birthdate = DateUtils.getOfDayFirst(birthdate); // 将日期的时间改为0点0分0秒
		sqlMap.getWhere().and("birthdate", QueryType.GTE, birthdate);
	}
	
	public Date getBirthdate_lte() {
		return sqlMap.getWhere().getValue("birthdate", QueryType.LTE);
	}

	public void setBirthdate_lte(Date birthdate) {
		birthdate = DateUtils.getOfDayLast(birthdate); // 将日期的时间改为23点59分59秒
		sqlMap.getWhere().and("birthdate", QueryType.LTE, birthdate);
	}
	
	@Length(min=0, max=1, message="性别长度不能超过 1 个字符")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=64, message="身份证号长度不能超过 64 个字符")
	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}
	
	@Length(min=0, max=64, message="电话长度不能超过 64 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}

	public String getDatacontrol() {
		return datacontrol;
	}

	public void setDatacontrol(String datacontrol) {
		this.datacontrol = datacontrol;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getPsntype() {
		return psntype;
	}

	public void setPsntype(String psntype) {
		this.psntype = psntype;
	}

	public BdDept getPkDeptall() {
		return pkDeptall;
	}

	public void setPkDeptall(BdDept pkDeptall) {
		this.pkDeptall = pkDeptall;
	}

	public List<BdPsndocJob> getBdPsndocJobList() {
		return bdPsndocJobList;
	}

	public void setBdPsndocJobList(List<BdPsndocJob> bdPsndocJobList) {
		this.bdPsndocJobList = bdPsndocJobList;
	}
	
}