/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.reports.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 应收账款表Entity
 * @author LY
 * @version 2019-10-31
 */
@Table
public class ChargeYsZk extends DataEntity<ChargeYsZk> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Office pkOrg;		// 组织名称
	private ZlProject pkProject;		//项目
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="应缴日期", attrName="yfdate", align=Align.LEFT, sort=10, dataFormat="yyyy-MM-dd")
	private Date yfdate;		// 应缴日期
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="客户名称", attrName="custname", align=Align.LEFT, sort=20)
	private String custname;		// 客户名称
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="地点", attrName="room", align=Align.LEFT, sort=30)
	private String room;		// 地点
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同号", attrName="htcode", align=Align.CENTER, sort=40)
	private String htcode;		// 合同号
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="租赁费用", attrName="yszl", align=Align.RIGHT, sort=50, dataFormat="#,##0.00")
	private Double yszl;		//租赁费用
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="物业费用", attrName="yswy", align=Align.RIGHT, sort=60, dataFormat="#,##0.00")
	private Double yswy;		//物业费用
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="其他费用", attrName="ysqt", align=Align.RIGHT, sort=70, dataFormat="#,##0.00")
	private Double ysqt;		//其他
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合计", attrName="yshj", align=Align.RIGHT, sort=80, dataFormat="#,##0.00")
	private Double yshj;		//合计
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="费用开始日期", attrName="dstart", align=Align.LEFT, sort=90, dataFormat="yyyy-MM-dd")
	private Date dstart;		//费用开始日期
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="费用截止日期", attrName="dend", align=Align.LEFT, sort=100, dataFormat="yyyy-MM-dd")
	private Date dend;		//费用结束日期
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="已到房租", attrName="sszl", align=Align.RIGHT, sort=110, dataFormat="#,##0.00")
	private Double sszl;		//已到房租
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="已到物业", attrName="sswy", align=Align.RIGHT, sort=120, dataFormat="#,##0.00")
	private Double sswy;		//已到物业
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="已到其他", attrName="ssqt", align=Align.RIGHT, sort=140, dataFormat="#,##0.00")
	private Double ssqt;		//已到其他
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="累计收款金额", attrName="sshj", align=Align.RIGHT, sort=150, dataFormat="#,##0.00")
	private Double sshj;		//累计收款金额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="未到金额", attrName="wsmny", align=Align.RIGHT, sort=160, dataFormat="#,##0.00")
	private Double wsmny;		//未到金额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="到账时间", attrName="ddzdate", align=Align.LEFT, sort=170, dataFormat="yyyy-MM-dd")
	private Date ddzdate;		//到账时间
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="到账进度", attrName="dzjd", align=Align.CENTER, sort=180, dataFormat="#,##0;[红色]-#,##0")
	private Integer dzjd;		//到账进度

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
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getYfdate() {
		return yfdate;
	}

	public void setYfdate(Date yfdate) {
		this.yfdate = yfdate;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
	}

	public Double getYszl() {
		return yszl;
	}

	public void setYszl(Double yszl) {
		this.yszl = yszl;
	}

	public Double getYswy() {
		return yswy;
	}

	public void setYswy(Double yswy) {
		this.yswy = yswy;
	}

	public Double getYsqt() {
		return ysqt;
	}

	public void setYsqt(Double ysqt) {
		this.ysqt = ysqt;
	}

	public Double getYshj() {
		return yshj;
	}

	public void setYshj(Double yshj) {
		this.yshj = yshj;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDstart() {
		return dstart;
	}

	public void setDstart(Date dstart) {
		this.dstart = dstart;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDend() {
		return dend;
	}

	public void setDend(Date dend) {
		this.dend = dend;
	}

	public Double getSszl() {
		return sszl;
	}

	public void setSszl(Double sszl) {
		this.sszl = sszl;
	}

	public Double getSswy() {
		return sswy;
	}

	public void setSswy(Double sswy) {
		this.sswy = sswy;
	}

	public Double getSsqt() {
		return ssqt;
	}

	public void setSsqt(Double ssqt) {
		this.ssqt = ssqt;
	}

	public Double getSshj() {
		return sshj;
	}

	public void setSshj(Double sshj) {
		this.sshj = sshj;
	}

	public Double getWsmny() {
		return wsmny;
	}

	public void setWsmny(Double wsmny) {
		this.wsmny = wsmny;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDdzdate() {
		return ddzdate;
	}

	public void setDdzdate(Date ddzdate) {
		this.ddzdate = ddzdate;
	}

	public Integer getDzjd() {
		return dzjd;
	}

	public void setDzjd(Integer dzjd) {
		this.dzjd = dzjd;
	}
	
}