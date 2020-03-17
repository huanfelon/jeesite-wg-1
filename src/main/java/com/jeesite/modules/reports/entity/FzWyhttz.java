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
 * 房租物业合同台账
 * @author LY
 * @version 2019-10-31
 */
@Table
public class FzWyhttz extends DataEntity<FzWyhttz> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Office pkOrg;		//组织
	private ZlProject pkProject;		//项目
	private String pkContract;		//合同主键
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同编号", attrName="htcode", align=Align.LEFT, sort=10)
	private String htcode;		// 合同编号
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="租赁楼层及室号", attrName="room", align=Align.LEFT, sort=20)
	private String room;		//租赁楼层及室号
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同状态", attrName="htstatus", align=Align.LEFT, sort=30)
	private String htstatus;	//合同状态
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="签订日期", attrName="qydate", align=Align.LEFT, sort=40, dataFormat="yyyy-MM-dd")
	private Date qydate;	//签订日期
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="企业名称", attrName="custname", align=Align.LEFT, sort=50)
	private String custname;	//企业名称
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同面积/㎡", attrName="htarea", align=Align.RIGHT, sort=60, dataFormat="#,##0.00")
	private Double htarea;		//合同面积/㎡
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="免租区间", attrName="mzddate", align=Align.LEFT, sort=70)
	private String mzddate;		//免租区间
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同开始日期", attrName="htdstart", align=Align.LEFT, sort=80, dataFormat="yyyy-MM-dd")
	private Date htdstart;		//合同开始日期
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同结束日期", attrName="htdend", align=Align.LEFT, sort=90, dataFormat="yyyy-MM-dd")
	private Date htdend;		//合同结束日期
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同实际结束日期", attrName="htsjdend", align=Align.LEFT, sort=100, dataFormat="yyyy-MM-dd")
	private Date htsjdend;		//合同实际结束日期
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="租期/年", attrName="nhtyear", align=Align.RIGHT, sort=110, dataFormat="#,##0.00")
	private Double nhtyear;		//租期/年
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="付款方式", attrName="paystyle", align=Align.LEFT, sort=120)
	private String paystyle;		//付款方式
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="房租合同总价", attrName="nfzallhtmny", align=Align.RIGHT, sort=130, dataFormat="#,##0.00")
	private Double nfzallhtmny;		//房租合同总价
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="单价（/天/平）", attrName="nfzprice", align=Align.RIGHT, sort=140, dataFormat="#,##0.000")
	private Double nfzprice;		//单价（/天/平）
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="月租金", attrName="nfzmonthmny", align=Align.RIGHT, sort=150, dataFormat="#,##0.00")
	private Double nfzmonthmny;		//月租金
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="年租金", attrName="nfzyearmny", align=Align.RIGHT, sort=160, dataFormat="#,##0.00")
	private Double nfzyearmny;		//年租金
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="租金递增情况", attrName="vzjtz", align=Align.LEFT, sort=170)
	private String vzjtz;		//租金递增情况
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="物业合同总价", attrName="nwyallhtmny", align=Align.RIGHT, sort=180, dataFormat="#,##0.00")
	private Double nwyallhtmny;		//物业合同总价
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="单价（/平/月）", attrName="nwyprice", align=Align.RIGHT, sort=190, dataFormat="#,##0.00")
	private Double nwyprice;		//单价（/平/月）
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="月物业费", attrName="nwymonthmny", align=Align.RIGHT, sort=200, dataFormat="#,##0.00")
	private Double nwymonthmny;		//月物业费
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="年物业费", attrName="nwyyearmny", align=Align.RIGHT, sort=210, dataFormat="#,##0.00")
	private Double nwyyearmny;		//年物业费
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同押金", attrName="nyjht", align=Align.RIGHT, sort=220, dataFormat="#,##0.00")
	private Double nyjht;		//合同押金
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="设备押金", attrName="nyjsb", align=Align.RIGHT, sort=230, dataFormat="#,##0.00")
	private Double nyjsb;		//设备押金
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="续签情况", attrName="vxq", align=Align.LEFT, sort=240)
	private String vxq;		//续签情况
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="退押金", attrName="tyj", align=Align.LEFT, sort=250)
	private String tyj;		//退押金
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="备注", attrName="vmemo", align=Align.LEFT, sort=260)
	private String vmemo;		//备注
	
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="楼号", attrName="build", align=Align.LEFT, sort=270)
	private String build;	//楼号
	
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

	public String getPkContract() {
		return pkContract;
	}

	public void setPkContract(String pkContract) {
		this.pkContract = pkContract;
	}

	public String getBuild() {
		return build;
	}
	
	public void setBuild(String build) {
		this.build = build;
	}

	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getHtstatus() {
		return htstatus;
	}

	public void setHtstatus(String htstatus) {
		this.htstatus = htstatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getQydate() {
		return qydate;
	}

	public void setQydate(Date qydate) {
		this.qydate = qydate;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public Double getHtarea() {
		return htarea;
	}

	public void setHtarea(Double htarea) {
		this.htarea = htarea;
	}

	public String getMzddate() {
		return mzddate;
	}

	public void setMzddate(String mzddate) {
		this.mzddate = mzddate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getHtdstart() {
		return htdstart;
	}

	public void setHtdstart(Date htdstart) {
		this.htdstart = htdstart;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getHtdend() {
		return htdend;
	}

	public void setHtdend(Date htdend) {
		this.htdend = htdend;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getHtsjdend() {
		return htsjdend;
	}

	public void setHtsjdend(Date htsjdend) {
		this.htsjdend = htsjdend;
	}

	public Double getNhtyear() {
		return nhtyear;
	}

	public void setNhtyear(Double nhtyear) {
		this.nhtyear = nhtyear;
	}

	public String getPaystyle() {
		return paystyle;
	}

	public void setPaystyle(String paystyle) {
		this.paystyle = paystyle;
	}

	public Double getNfzallhtmny() {
		return nfzallhtmny;
	}

	public void setNfzallhtmny(Double nfzallhtmny) {
		this.nfzallhtmny = nfzallhtmny;
	}

	public Double getNfzprice() {
		return nfzprice;
	}

	public void setNfzprice(Double nfzprice) {
		this.nfzprice = nfzprice;
	}

	public Double getNfzmonthmny() {
		return nfzmonthmny;
	}

	public void setNfzmonthmny(Double nfzmonthmny) {
		this.nfzmonthmny = nfzmonthmny;
	}

	public Double getNfzyearmny() {
		return nfzyearmny;
	}

	public void setNfzyearmny(Double nfzyearmny) {
		this.nfzyearmny = nfzyearmny;
	}

	public String getVzjtz() {
		return vzjtz;
	}

	public void setVzjtz(String vzjtz) {
		this.vzjtz = vzjtz;
	}

	public Double getNwyallhtmny() {
		return nwyallhtmny;
	}

	public void setNwyallhtmny(Double nwyallhtmny) {
		this.nwyallhtmny = nwyallhtmny;
	}

	public Double getNwyprice() {
		return nwyprice;
	}

	public void setNwyprice(Double nwyprice) {
		this.nwyprice = nwyprice;
	}

	public Double getNwymonthmny() {
		return nwymonthmny;
	}

	public void setNwymonthmny(Double nwymonthmny) {
		this.nwymonthmny = nwymonthmny;
	}

	public Double getNwyyearmny() {
		return nwyyearmny;
	}

	public void setNwyyearmny(Double nwyyearmny) {
		this.nwyyearmny = nwyyearmny;
	}

	public Double getNyjht() {
		return nyjht;
	}

	public void setNyjht(Double nyjht) {
		this.nyjht = nyjht;
	}

	public Double getNyjsb() {
		return nyjsb;
	}

	public void setNyjsb(Double nyjsb) {
		this.nyjsb = nyjsb;
	}

	public String getVxq() {
		return vxq;
	}

	public void setVxq(String vxq) {
		this.vxq = vxq;
	}

	public String getTyj() {
		return tyj;
	}

	public void setTyj(String tyj) {
		this.tyj = tyj;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	
}