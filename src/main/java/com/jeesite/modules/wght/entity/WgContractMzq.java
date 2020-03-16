/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.wght.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 合同管理表体-免租期页签Entity
 * @author LY
 * @version 2019-10-31
 */
@Table(name="wg_contract_mzq", alias="a", columns={
		@Column(name="pk_contract_mzq", attrName="pkContractMzq", label="主键", isPK=true),
		@Column(name="pk_contract", attrName="pkContract.pkContract", label="表头主键"),
		@Column(name="dstartdate", attrName="dstartdate", label="免租开始日期"),
		@Column(name="denddate", attrName="denddate", label="免租结束日期"),
		@Column(name="dmonth", attrName="dmonth", label="免租月数"),
		@Column(name="iswy", attrName="iswy", label="是否免物业费"),
		@Column(name="isdqmz", attrName="isdqmz", label="是否当期免租"),
		@Column(name="vmemo", attrName="vmemo", label="备注"),
		@Column(name="vdef1", attrName="vdef1", label="自定义项1"),
		@Column(name="vdef2", attrName="vdef2", label="自定义项2"),
		@Column(name="vdef3", attrName="vdef3", label="自定义项3"),
		@Column(name="vdef4", attrName="vdef4", label="自定义项4"),
		@Column(name="vdef5", attrName="vdef5", label="自定义项5"),
		@Column(name="dr", attrName="dr", label="删除标识"),
		@Column(name="ts", attrName="ts", label="时间戳"),
	}, orderBy="a.pk_contract_mzq ASC"
)
public class WgContractMzq extends DataEntity<WgContractMzq> {
	
	private static final long serialVersionUID = 1L;
	private String pkContractMzq;		// 主键
	private WgContract pkContract;		// 表头主键 父类
	private Date dstartdate;		// 免租开始日期
	private Date denddate;		// 免租结束日期
	private Integer dmonth;		// 免租月数
	private String iswy;		// 是否免物业费
	private String isdqmz;		// 是否免物业费
	private String vmemo;		// 备注
	private String vdef1;		// 自定义项1
	private String vdef2;		// 自定义项2
	private String vdef3;		// 自定义项3
	private String vdef4;		// 自定义项4
	private String vdef5;		// 自定义项5
	private Integer dr;		//删除标识
	private Date ts;		//时间戳
	
	public WgContractMzq() {
		this(null);
	}

	public WgContractMzq(WgContract pkContract){
		this.pkContract = pkContract;
	}
	
	public String getPkContractMzq() {
		return pkContractMzq;
	}

	public void setPkContractMzq(String pkContractMzq) {
		this.pkContractMzq = pkContractMzq;
	}
	
	public WgContract getPkContract() {
		return pkContract;
	}

	public void setPkContract(WgContract pkContract) {
		this.pkContract = pkContract;
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
	
	public Integer getDmonth() {
		return dmonth;
	}

	public void setDmonth(Integer dmonth) {
		this.dmonth = dmonth;
	}
	
	public String getIswy() {
		return iswy;
	}

	public void setIswy(String iswy) {
		this.iswy = iswy;
	}
	
	public String getIsdqmz() {
		return isdqmz;
	}

	public void setIsdqmz(String isdqmz) {
		this.isdqmz = isdqmz;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
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