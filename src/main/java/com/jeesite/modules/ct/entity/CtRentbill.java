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
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 租约账单Entity
 * @author tcl
 * @version 2019-11-08
 */
@Table(name="ct_rentbill", alias="a", columns={
		@Column(name="pk_rentbill", attrName="pkRentbill", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="所属组织"),
		@Column(name="vbillno", attrName="vbillno", label="单据号", queryType=QueryType.LIKE),
		@Column(name="pk_project", attrName="pkProject.id", label="项目信息"),
		@Column(name="pk_customer", attrName="pkCustomer.id", label="客户名称"),
		@Column(name="htcode", attrName="htcode", label="合同号", queryType=QueryType.LIKE),
		@Column(name="pk_building", attrName="pkBuilding.id", label="楼栋"),
		@Column(name="pk_house", attrName="pkHouse.id", label="房产名称"),
		@Column(name="pk_dept", attrName="pkDept.id", label="部门"),
		@Column(name="nysmny", attrName="nysmny", label="应收金额", isQuery=false),
		@Column(name="ntaxmny", attrName="ntaxmny", label="税额", isQuery=false),
		@Column(name="nnotaxmny", attrName="nnotaxmny", label="无税金额", isQuery=false),
		@Column(name="vsrcid", attrName="vsrcid", label="来源单据id", isQuery=false),
		@Column(name="vsrcno", attrName="vsrcno", label="来源单据号", isQuery=false),
		@Column(name="vbillstatus", attrName="vbillstatus", label="单据状态"),
		@Column(name="creator", attrName="creator.userCode", label="制单人", isQuery=false),
		@Column(name="creationtime", attrName="creationtime", label="制单时间", isQuery=false),
		@Column(name="modifier", attrName="modifier.userCode", label="修改人", isQuery=false),
		@Column(name="modifiedtime", attrName="modifiedtime", label="修改时间", isQuery=false),
		@Column(name="approver", attrName="approver.userCode", label="审核人", isQuery=false),
		@Column(name="approvetime", attrName="approvetime", label="审核时间", isQuery=false),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
		@Column(name="dbilldate", attrName="dbilldate", label="开账日期"),
		@Column(name="vdef1", attrName="vdef1", label="vdef1", isQuery=false),
		@Column(name="vdef2", attrName="vdef2", label="vdef2", isQuery=false),
		@Column(name="vdef3", attrName="vdef3", label="vdef3", isQuery=false),
		@Column(name="vdef4", attrName="vdef4", label="vdef4", isQuery=false),
		@Column(name="vdef5", attrName="vdef5", label="vdef5", isQuery=false),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="u2",
			on="u2.office_code = a.pk_org", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="u4",
			on="u4.pk_project = a.pk_project", columns={
				@Column(name="pk_project", label="", isPK=true, isQuery=false),
				@Column(name="code", label="编码", isQuery=false),
				@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdCustomer.class, attrName="pkCustomer", alias="u5",
		on="u5.pk_customer = a.pk_customer", columns={
			@Column(name="pk_customer", label="", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlBuildingfile.class, attrName="pkBuilding", alias="u7",
		on="u7.pk_buildingfile = a.pk_building", columns={
			@Column(name="pk_buildingfile", label="", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlHousesource.class, attrName="pkHouse", alias="u8",
		on="u8.pk_housesource = a.pk_house", columns={
			@Column(name="pk_housesource", label="", isPK=true, isQuery=false),
			@Column(name="estatecode", label="编码", isQuery=false),
			@Column(name="estatename", label="名称", isQuery=false),	
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="u13",
		on="u13.pk_dept = a.pk_dept", columns={
			@Column(name="pk_dept", label="", isPK=true, isQuery=false),
			@Column(name="dept_code", label="编码", isQuery=false),
			@Column(name="dept_name", label="名称", isQuery=false),	
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u16",
			on="u16.user_code = a.creator", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u18",
			on="u18.user_code = a.modifier", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),

		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u20",
			on="u20.user_code = a.approver", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
	},extWhereKeys="dsf", orderBy="a.pk_org,a.pk_project,a.vbillno "
)
public class CtRentbill extends DataEntity<CtRentbill> {
	
	private static final long serialVersionUID = 1L;
	private String pkRentbill;		// 主键
	private Office pkOrg;		// 所属组织
	private String vbillno;		// 单据号
	private ZlProject pkProject;		// 项目信息
	private BdCustomer pkCustomer;		// 客户名称
	private String htcode;		// 合同号
	private ZlBuildingfile pkBuilding;		// 楼栋
	private ZlHousesource pkHouse;		// 房产名称
	private BdDept pkDept;		// 部门
	private Double nysmny;		// 应收金额
	private Double ntaxmny;		// 税额
	private Double nnotaxmny;		// 无税金额
	private String vsrcid;		// 来源单据id
	private String vsrcno;		// 来源单据号
	private String vbillstatus;		// 单据状态
	private User creator;		// 制单人
	private Date creationtime;		// 制单时间
	private User modifier;		// 修改人
	private Date modifiedtime;		// 修改时间
	private User approver;		// 审核人
	private Date approvetime;		// 审核时间
	private Integer dr;		// 删除标识
	private Date ts;		// 时间戳
	private Date dbilldate;		// 开账日期
	private String vdef1;		// vdef1
	private String vdef2;		// vdef2
	private String vdef3;		// vdef3
	private String vdef4;		// vdef4
	private String vdef5;		// vdef5
	private List<CtRentbillSrft> ctRentbillSrftList = ListUtils.newArrayList();		// 子表列表
	private List<CtRentbillZjmx> ctRentbillZjmxList = ListUtils.newArrayList();		// 子表列表
	
	public CtRentbill() {
		this(null);
	}

	public CtRentbill(String id){
		super(id);
	}
	
	public String getPkRentbill() {
		return pkRentbill;
	}

	public void setPkRentbill(String pkRentbill) {
		this.pkRentbill = pkRentbill;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	@Length(min=0, max=255, message="单据号长度不能超过 255 个字符")
	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}
	
	@Length(min=0, max=255, message="合同号长度不能超过 255 个字符")
	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
	}
	
	public Double getNysmny() {
		return nysmny;
	}

	public void setNysmny(Double nysmny) {
		this.nysmny = nysmny;
	}
	
	public Double getNtaxmny() {
		return ntaxmny;
	}

	public void setNtaxmny(Double ntaxmny) {
		this.ntaxmny = ntaxmny;
	}
	
	public Double getNnotaxmny() {
		return nnotaxmny;
	}

	public void setNnotaxmny(Double nnotaxmny) {
		this.nnotaxmny = nnotaxmny;
	}
	
	@Length(min=0, max=255, message="来源单据id长度不能超过 255 个字符")
	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}
	
	@Length(min=0, max=255, message="来源单据号长度不能超过 255 个字符")
	public String getVsrcno() {
		return vsrcno;
	}

	public void setVsrcno(String vsrcno) {
		this.vsrcno = vsrcno;
	}
	
	@Length(min=0, max=1, message="单据状态长度不能超过 1 个字符")
	public String getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(String vbillstatus) {
		this.vbillstatus = vbillstatus;
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
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(Date dbilldate) {
		this.dbilldate = dbilldate;
	}
	
	@Length(min=0, max=255, message="vdef1长度不能超过 255 个字符")
	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	
	@Length(min=0, max=255, message="vdef2长度不能超过 255 个字符")
	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	
	@Length(min=0, max=255, message="vdef3长度不能超过 255 个字符")
	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}
	
	@Length(min=0, max=255, message="vdef4长度不能超过 255 个字符")
	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}
	
	@Length(min=0, max=255, message="vdef5长度不能超过 255 个字符")
	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}
	
	public Date getDbilldate_gte() {
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
	}
	
	public List<CtRentbillSrft> getCtRentbillSrftList() {
		return ctRentbillSrftList;
	}

	public void setCtRentbillSrftList(List<CtRentbillSrft> ctRentbillSrftList) {
		this.ctRentbillSrftList = ctRentbillSrftList;
	}
	
	public List<CtRentbillZjmx> getCtRentbillZjmxList() {
		return ctRentbillZjmxList;
	}

	public void setCtRentbillZjmxList(List<CtRentbillZjmx> ctRentbillZjmxList) {
		this.ctRentbillZjmxList = ctRentbillZjmxList;
	}

	public ZlProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(ZlProject pkProject) {
		this.pkProject = pkProject;
	}

	public BdCustomer getPkCustomer() {
		return pkCustomer;
	}

	public void setPkCustomer(BdCustomer pkCustomer) {
		this.pkCustomer = pkCustomer;
	}

	public ZlBuildingfile getPkBuilding() {
		return pkBuilding;
	}

	public void setPkBuilding(ZlBuildingfile pkBuilding) {
		this.pkBuilding = pkBuilding;
	}

	public ZlHousesource getPkHouse() {
		return pkHouse;
	}

	public void setPkHouse(ZlHousesource pkHouse) {
		this.pkHouse = pkHouse;
	}

	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}
	
}