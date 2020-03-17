/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.base.entity.BdDept;
import com.jeesite.modules.bd.entity.BdHttype;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 合同管理Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract", alias="a", columns={
		@Column(name="pk_contract", attrName="pkContract", label="主键", isPK=true),
		@Column(name="pk_org", attrName="pkOrg.officeCode", label="所属组织"),
		@Column(name="pk_project", attrName="pkProject.pkProject", label="项目信息"),
		@Column(name="htcode", attrName="htcode", label="合同编号", queryType=QueryType.LIKE),
		@Column(name="httype", attrName="httype.pkHttype", label="合同类型"),
		@Column(name="dqzdate", attrName="dqzdate", label="签租日期"),
		@Column(name="dstartdate", attrName="dstartdate", label="合同起始日"),
		@Column(name="denddate", attrName="denddate", label="合同终止日"),
		@Column(name="dfirstfkdate", attrName="dfirstfkdate", label="首期付款日期"),
		@Column(name="renttype", attrName="renttype", label="租赁方式", comment="租赁方式（1、日/平方米；2、日/套；3、月/平方米；4、月/套；5、年/平方米；6、年/套）"),
		@Column(name="ntaxrate", attrName="ntaxrate", label="合同税率"),
		@Column(name="nprice", attrName="nprice", label="租金单价"),
		@Column(name="ndayprice", attrName="ndayprice", label="日租金", comment="日租金（日/套合计）"),
		@Column(name="nmonthprice", attrName="nmonthprice", label="月租金", comment="月租金（月/套合计）"),
		@Column(name="nyearprice", attrName="nyearprice", label="年租金", comment="年租金（年/套合计）"),
		@Column(name="nallarea", attrName="nallarea", label="租赁总面积"),
		@Column(name="htstatus", attrName="htstatus", label="合同状态", comment="合同状态（0、自由；1、签租；2、退租）"),
		@Column(name="exremarks", attrName="exremarks", label="附加条款", queryType=QueryType.LIKE),
		@Column(name="billtype", attrName="billtype", label="单据类型", comment="单据类型（ct、合同；ctxd、合同修订）"),
		@Column(name="vbillstatus", attrName="vbillstatus", label="单据状态", comment="单据状态（0、自由,1、审批通过）"),
		@Column(name="version", attrName="version", label="版本号", comment="版本号（-1、当前显示版本，修订后从0开始记录，依次递增）"),
		@Column(name="creator", attrName="creator.userCode", label="制单人", isUpdateForce=true),
		@Column(name="createdtime", attrName="createdtime", label="制单时间", isUpdateForce=true),
		@Column(name="modifier", attrName="modifier.userCode", label="最后修改人", isUpdateForce=true),
		@Column(name="modifiedtime", attrName="modifiedtime", label="最后修改时间", isUpdateForce=true),
		@Column(name="approver", attrName="approver.userCode", label="审核人", isUpdateForce=true),
		@Column(name="approvedtime", attrName="approvedtime", label="审核时间", isUpdateForce=true),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="vdef6", attrName="vdef6", label="自定义项6"),
		@Column(name="vdef7", attrName="vdef7", label="自定义项7"),
		@Column(name="vdef8", attrName="vdef8", label="自定义项8"),
		@Column(name="vdef9", attrName="vdef9", label="自定义项9"),
		@Column(name="vdef10", attrName="vdef10", label="自定义项10"),
		@Column(name="vsrcid", attrName="vsrcid", label="来源单据id", isUpdateForce=true),
		@Column(name="vsrctype", attrName="vsrctype", label="来源单据类型", isUpdateForce=true),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
		@Column(name="vbillno", attrName="vbillno", label="单据号", queryType=QueryType.LIKE),
		@Column(name="pk_dept", attrName="pkDept.pkDept", label="部门"),
		@Column(name="iswyht", attrName="iswyht", label="是否纯物业合同"),
		@Column(name="dtzdate", attrName="dtzdate", label="退租日期"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="pkOrg", alias="o1",
		on="o1.office_code = a.pk_org", columns={
			@Column(name="office_code", label="组织编码", isPK=true, isQuery=false),
			@Column(name="office_name", label="组织名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=ZlProject.class, attrName="pkProject", alias="p1",
		on="p1.pk_project = a.pk_project", columns={
			@Column(name="pk_project", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdHttype.class, attrName="httype", alias="ht1",
		on="ht1.pk_httype = a.httype", columns={
			@Column(name="pk_httype", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="creator", alias="u1",
		on="u1.user_code = a.creator", columns={
			@Column(name="user_code", label="用户编码", isPK=true, isQuery=false),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="modifier", alias="u2",
		on="u2.user_code = a.modifier", columns={
			@Column(name="user_code", label="用户编码", isPK=true, isQuery=false),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="approver", alias="u3",
		on="u3.user_code = a.approver", columns={
			@Column(name="user_code", label="用户编码", isPK=true, isQuery=false),
			@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BdDept.class, attrName="pkDept", alias="d1",
		on="d1.pk_dept = a.pk_dept", columns={
			@Column(name="pk_dept", label="部门主键",isPK=true, isQuery=false),
			@Column(name="dept_code", label="部门编码", isQuery=false),
			@Column(name="dept_name", label="部门名称", isQuery=false),
		}),
}, extColumnKeys=" custname", extWhereKeys=" refquery,custnamequery", orderBy=" a.htcode"
)
public class WgContract extends DataEntity<WgContract> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pkContract;		// 主键
	private Office pkOrg;		// 所属组织
	private ZlProject pkProject;		// 项目信息
	private String htcode;		// 合同编号
	private String custname;		//客户名称
	private BdHttype httype;		// 合同类型
	private Date dqzdate;		// 签租日期
	private Date dstartdate;		// 合同起始日
	private Date denddate;		// 合同终止日
	private Date dfirstfkdate;		//合同首期付款日期
	private Integer renttype;		// 租赁方式（1、日/平方米；2、日/套；3、月/平方米；4、月/套；5、年/平方米；6、年/套）
	private Integer ntaxrate;		// 合同税率
	private Double nprice;		// 租金单价
	private Double ndayprice;		// 日租金（日/套合计）
	private Double nmonthprice;		// 月租金（月/套合计）
	private Double nyearprice;		// 年租金（年/套合计）
	private Double nallarea;		//租赁总面积
	private Integer htstatus;		// 合同状态（0、自由；1、签租；2、退租）
	private String exremarks;		// 附加条款
	private String billtype;		// 单据类型（ct、合同；ctxd、合同修订）
	private Integer vbillstatus;		// 单据状态（0、自由,1、审批通过）
	private Integer version;		// 版本号（-1、当前显示版本，修订后从0开始记录，依次递增）
	private User creator;		// 制单人
	private Date createdtime;		// 制单时间
	private User modifier;		// 最后修改人
	private Date modifiedtime;		// 最后修改时间
	private User approver;		// 审核人
	private Date approvedtime;		// 审核时间
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private String vdef6;		// 自定义项6
	private String vdef7;		// 自定义项7
	private String vdef8;		// 自定义项8
	private String vdef9;		// 自定义项9
	private String vdef10;		// 自定义项10
	private String vsrcid;		// 来源单据id
	private String vsrctype;		// 来源单据类型
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	private String vbillno;		//单据号
	private BdDept pkDept;		//部门
	private String iswyht;		//是否纯物业合同
	private Date dtzdate;		//退租日期
	private List<WgContractCust> wgContractCustList = ListUtils.newArrayList();		// 子表客户信息
	private List<WgContractHouse> wgContractHouseList = ListUtils.newArrayList();		// 子表房产信息
	private List<WgContractWyf> wgContractWyfList = ListUtils.newArrayList();		// 子表物业费
	private List<WgContractZqfy> wgContractZqfyList = ListUtils.newArrayList();		// 子表其他周期费用
	private List<WgContractYj> wgContractYjList = ListUtils.newArrayList();		// 子表押金
	private List<WgContractZltype> wgContractZltypeList = ListUtils.newArrayList();		// 子表租赁支付方式
	private List<WgContractWytype> wgContractWytypeList = ListUtils.newArrayList();		// 子表物业支付方式
	private List<WgContractMzq> wgContractMzqList = ListUtils.newArrayList();		// 子表免租期
	private List<WgContractZzq> wgContractZzqList = ListUtils.newArrayList();		// 子表增长期
	private List<WgContractRentprice> wgContractRentpriceList = ListUtils.newArrayList();		// 子表年租金
	private List<WgContractYwcf> wgContractYwcfList = ListUtils.newArrayList();		// 子表业务拆分
	private List<WgContractWyfcf> wgContractWyfcfList = ListUtils.newArrayList();		// 子表物业费拆分
	private List<WgContractZqfycf> wgContractZqfycfList = ListUtils.newArrayList();		// 子表其他周期费用拆分
	private List<WgContractSrcf> wgContractSrcfList = ListUtils.newArrayList();		// 子表收入拆分
	
	public WgContract() {
		this(null);
	}

	public WgContract(String id){
		super(id);
	}
	
	public String getPkContract() {
		return pkContract;
	}

	public void setPkContract(String pkContract) {
		this.pkContract = pkContract;
	}
	
	public Office getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(Office pkOrg) {
		this.pkOrg = pkOrg;
	}

	public ZlProject getPkProject() {
		return pkProject;
	}

	public void setPkProject(ZlProject pkProject) {
		this.pkProject = pkProject;
	}

	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
	}
	
	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public BdHttype getHttype() {
		return httype;
	}

	public void setHttype(BdHttype httype) {
		this.httype = httype;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDqzdate() {
		return dqzdate;
	}

	public void setDqzdate(Date dqzdate) {
		this.dqzdate = dqzdate;
	}
	
	public Date getDqzdate_gte(){
		return sqlMap.getWhere().getValue("dqzdate", QueryType.GTE);
	}

	public void setDqzdate_gte(Date dqzdate){
		dqzdate = DateUtils.getOfDayFirst(dqzdate); // 将日期的时间改为0点0分0秒
		sqlMap.getWhere().and("dqzdate", QueryType.GTE, dqzdate);
	}

	public Date getDqzdate_lte(){
		return sqlMap.getWhere().getValue("dqzdate", QueryType.LTE);
	}

	public void setDqzdate_lte(Date dqzdate){
		dqzdate = DateUtils.getOfDayLast(dqzdate); // 将日期的时间改为23点59分59秒
		sqlMap.getWhere().and("dqzdate", QueryType.LTE, dqzdate);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}
	
	public Date getDfirstfkdate() {
		return dfirstfkdate;
	}

	public void setDfirstfkdate(Date dfirstfkdate) {
		this.dfirstfkdate = dfirstfkdate;
	}

	public Integer getRenttype() {
		return renttype;
	}

	public void setRenttype(Integer renttype) {
		this.renttype = renttype;
	}
	
	public Integer getNtaxrate() {
		return ntaxrate;
	}

	public void setNtaxrate(Integer ntaxrate) {
		this.ntaxrate = ntaxrate;
	}
	
	public Double getNprice() {
		return nprice;
	}

	public void setNprice(Double nprice) {
		this.nprice = nprice;
	}
	
	public Double getNdayprice() {
		return ndayprice;
	}

	public void setNdayprice(Double ndayprice) {
		this.ndayprice = ndayprice;
	}
	
	public Double getNmonthprice() {
		return nmonthprice;
	}

	public void setNmonthprice(Double nmonthprice) {
		this.nmonthprice = nmonthprice;
	}
	
	public Double getNyearprice() {
		return nyearprice;
	}

	public void setNyearprice(Double nyearprice) {
		this.nyearprice = nyearprice;
	}
	
	public Double getNallarea() {
		return nallarea;
	}

	public void setNallarea(Double nallarea) {
		this.nallarea = nallarea;
	}

	public Integer getHtstatus() {
		return htstatus;
	}

	public void setHtstatus(Integer htstatus) {
		this.htstatus = htstatus;
	}
	
	public String getExremarks() {
		return exremarks;
	}

	public void setExremarks(String exremarks) {
		this.exremarks = exremarks;
	}
	
	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}
	
	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
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

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getApprovedtime() {
		return approvedtime;
	}

	public void setApprovedtime(Date approvedtime) {
		this.approvedtime = approvedtime;
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
	
	public String getVdef6() {
		return vdef6;
	}

	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}
	
	public String getVdef7() {
		return vdef7;
	}

	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	
	public String getVdef8() {
		return vdef8;
	}

	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	
	public String getVdef9() {
		return vdef9;
	}

	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	
	public String getVdef10() {
		return vdef10;
	}

	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	
	public String getVsrcid() {
		return vsrcid;
	}

	public void setVsrcid(String vsrcid) {
		this.vsrcid = vsrcid;
	}
	
	public String getVsrctype() {
		return vsrctype;
	}

	public void setVsrctype(String vsrctype) {
		this.vsrctype = vsrctype;
	}
	
	public Integer getDr() {
		sqlMap.getWhere().and("dr", QueryType.EQ, "0");
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}

	public BdDept getPkDept() {
		return pkDept;
	}

	public void setPkDept(BdDept pkDept) {
		this.pkDept = pkDept;
	}

	public String getIswyht() {
		return iswyht;
	}

	public void setIswyht(String iswyht) {
		this.iswyht = iswyht;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDtzdate() {
		return dtzdate;
	}

	public void setDtzdate(Date dtzdate) {
		this.dtzdate = dtzdate;
	}

	public List<WgContractZzq> getWgContractZzqList() {
		return wgContractZzqList;
	}

	public void setWgContractZzqList(List<WgContractZzq> wgContractZzqList) {
		this.wgContractZzqList = wgContractZzqList;
	}
	
	public List<WgContractZqfy> getWgContractZqfyList() {
		return wgContractZqfyList;
	}

	public void setWgContractZqfyList(List<WgContractZqfy> wgContractZqfyList) {
		this.wgContractZqfyList = wgContractZqfyList;
	}
	
	public List<WgContractZltype> getWgContractZltypeList() {
		return wgContractZltypeList;
	}

	public void setWgContractZltypeList(List<WgContractZltype> wgContractZltypeList) {
		this.wgContractZltypeList = wgContractZltypeList;
	}
	
	public List<WgContractYj> getWgContractYjList() {
		return wgContractYjList;
	}

	public void setWgContractYjList(List<WgContractYj> wgContractYjList) {
		this.wgContractYjList = wgContractYjList;
	}
	
	public List<WgContractWytype> getWgContractWytypeList() {
		return wgContractWytypeList;
	}

	public void setWgContractWytypeList(List<WgContractWytype> wgContractWytypeList) {
		this.wgContractWytypeList = wgContractWytypeList;
	}
	
	public List<WgContractWyf> getWgContractWyfList() {
		return wgContractWyfList;
	}

	public void setWgContractWyfList(List<WgContractWyf> wgContractWyfList) {
		this.wgContractWyfList = wgContractWyfList;
	}
	
	public List<WgContractMzq> getWgContractMzqList() {
		return wgContractMzqList;
	}

	public void setWgContractMzqList(List<WgContractMzq> wgContractMzqList) {
		this.wgContractMzqList = wgContractMzqList;
	}
	
	public List<WgContractHouse> getWgContractHouseList() {
		return wgContractHouseList;
	}

	public void setWgContractHouseList(List<WgContractHouse> wgContractHouseList) {
		this.wgContractHouseList = wgContractHouseList;
	}
	
	public List<WgContractCust> getWgContractCustList() {
		return wgContractCustList;
	}

	public void setWgContractCustList(List<WgContractCust> wgContractCustList) {
		this.wgContractCustList = wgContractCustList;
	}

	public List<WgContractRentprice> getWgContractRentpriceList() {
		return wgContractRentpriceList;
	}

	public void setWgContractRentpriceList(
			List<WgContractRentprice> wgContractRentpriceList) {
		this.wgContractRentpriceList = wgContractRentpriceList;
	}

	public List<WgContractYwcf> getWgContractYwcfList() {
		return wgContractYwcfList;
	}

	public void setWgContractYwcfList(List<WgContractYwcf> wgContractYwcfList) {
		this.wgContractYwcfList = wgContractYwcfList;
	}

	public List<WgContractWyfcf> getWgContractWyfcfList() {
		return wgContractWyfcfList;
	}

	public void setWgContractWyfcfList(List<WgContractWyfcf> wgContractWyfcfList) {
		this.wgContractWyfcfList = wgContractWyfcfList;
	}

	public List<WgContractZqfycf> getWgContractZqfycfList() {
		return wgContractZqfycfList;
	}

	public void setWgContractZqfycfList(List<WgContractZqfycf> wgContractZqfycfList) {
		this.wgContractZqfycfList = wgContractZqfycfList;
	}

	public List<WgContractSrcf> getWgContractSrcfList() {
		return wgContractSrcfList;
	}

	public void setWgContractSrcfList(List<WgContractSrcf> wgContractSrcfList) {
		this.wgContractSrcfList = wgContractSrcfList;
	}
	
}