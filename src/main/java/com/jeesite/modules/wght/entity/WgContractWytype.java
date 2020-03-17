/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.bd.entity.BdProject;

/**
 * 合同管理表体-物业支付方式页签Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_wytype", alias="a", columns={
		@Column(name="pk_contract_wytype", attrName="pkContractWytype", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="pk_ysproject", attrName="pkYsproject.pkProject", label="预算项目"),
		@Column(name="dstartdate", attrName="dstartdate", label="租赁开始日期"),
		@Column(name="paystyle", attrName="paystyle", label="付款方式"),
		@Column(name="payyear", attrName="payyear", label="付款方式年"),
		@Column(name="paymonth", attrName="paymonth", label="付款方式月"),
		@Column(name="payday", attrName="payday", label="付款方式天"),
		@Column(name="vmemo", attrName="vmemo", label="备注"),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=BdProject.class, attrName="pkYsproject", alias="cp1",
		on="cp1.pk_project = a.pk_ysproject", columns={
			@Column(name="pk_project", label="主键", isPK=true, isQuery=false),
			@Column(name="code", label="编码", isQuery=false),
			@Column(name="name", label="名称", isQuery=false),
		}),
}, orderBy="a.pk_contract_wytype ASC"
)
public class WgContractWytype extends DataEntity<WgContractWytype> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractWytype;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private BdProject pkYsproject;		// 预算项目
	private Date dstartdate;		// 租赁开始日期
	private Integer paystyle;		// 付款方式
	private Integer payyear;		//付款方式年
	private Integer paymonth;		//付款方式月
	private Integer payday;		//付款方式天
	private String vmemo;		// 备注
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractWytype() {
		this(null);
	}

	public WgContractWytype(WgContract pkContract){
		this.pkContract = pkContract;
	}
	
	public String getPkContractWytype() {
		return pkContractWytype;
	}

	public void setPkContractWytype(String pkContractWytype) {
		this.pkContractWytype = pkContractWytype;
	}
	
	public WgContract getPkContract() {
		return pkContract;
	}

	public void setPkContract(WgContract pkContract) {
		this.pkContract = pkContract;
	}
	
	public BdProject getPkYsproject() {
		return pkYsproject;
	}

	public void setPkYsproject(BdProject pkYsproject) {
		this.pkYsproject = pkYsproject;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}
	
	public Integer getPaystyle() {
		return paystyle;
	}

	public void setPaystyle(Integer paystyle) {
		this.paystyle = paystyle;
	}
	
	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	
	public Integer getPayyear() {
		return payyear;
	}

	public void setPayyear(Integer payyear) {
		this.payyear = payyear;
	}

	public Integer getPaymonth() {
		return paymonth;
	}

	public void setPaymonth(Integer paymonth) {
		this.paymonth = paymonth;
	}

	public Integer getPayday() {
		return payday;
	}

	public void setPayday(Integer payday) {
		this.payday = payday;
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
	
}