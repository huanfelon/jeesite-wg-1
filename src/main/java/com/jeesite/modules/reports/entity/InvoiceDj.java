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
 * 开票登记表Entity
 * @author LY
 * @version 2019-10-31
 */
@Table
public class InvoiceDj extends DataEntity<InvoiceDj> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Office pkOrg;		// 组织名称
	private ZlProject pkProject;		//项目
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="开票名称", attrName="kpname", align=Align.LEFT, sort=10)
	private String kpname;		//开票名称
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同号", attrName="htcode", align=Align.LEFT, sort=20)
	private String htcode;		//合同号
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="租赁地址", attrName="house", align=Align.LEFT, sort=30)
	private String house;		//租赁地址
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="收款期间", attrName="dskqj", align=Align.LEFT, sort=40)
	private String dskqj;		//收款期间
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="实际付款人", attrName="fkman", align=Align.LEFT, sort=50)
	private String fkman;		//实际付款人
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="开票日期", attrName="dsqdate", align=Align.LEFT, sort=60, dataFormat="yyyy-MM-dd")
	private Date dsqdate;		//开票日期
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="发票种类", attrName="fptype", align=Align.LEFT, sort=70, dictType="wg_fptype")
	private String fptype;		//发票种类
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="发票号码", attrName="fpcode", align=Align.LEFT, sort=80)
	private String fpcode;		//发票号码
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="发票项目", attrName="pkcost", align=Align.LEFT, sort=90)
	private String pkcost;		//发票项目
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="实际项目", attrName="kpcostp", align=Align.LEFT, sort=100)
	private String kpcostp;		//实际项目
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="税率(%)", attrName="taxrate", align=Align.RIGHT, sort=110, dataFormat="#,##0.00")
	private Double taxrate;		//税率
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="开票金额", attrName="nkpmny", align=Align.RIGHT, sort=120, dataFormat="#,##0.00")
	private Double nkpmny;		//开票金额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="不含税金额", attrName="nnotaxmny", align=Align.RIGHT, sort=130, dataFormat="#,##0.00")
	private Double nnotaxmny;		//不含税金额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="发票税额", attrName="ntaxmny", align=Align.RIGHT, sort=140, dataFormat="#,##0.00")
	private Double ntaxmny;		//发票税额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="开票期间", attrName="dkpqj", align=Align.LEFT, sort=150)
	private String dkpqj;		//开票期间

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

	public String getKpname() {
		return kpname;
	}

	public void setKpname(String kpname) {
		this.kpname = kpname;
	}

	public String getHtcode() {
		return htcode;
	}

	public void setHtcode(String htcode) {
		this.htcode = htcode;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getDskqj() {
		return dskqj;
	}

	public void setDskqj(String dskqj) {
		this.dskqj = dskqj;
	}

	public String getFkman() {
		return fkman;
	}

	public void setFkman(String fkman) {
		this.fkman = fkman;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDsqdate() {
		return dsqdate;
	}

	public void setDsqdate(Date dsqdate) {
		this.dsqdate = dsqdate;
	}

	public String getFptype() {
		return fptype;
	}

	public void setFptype(String fptype) {
		this.fptype = fptype;
	}

	public String getFpcode() {
		return fpcode;
	}

	public void setFpcode(String fpcode) {
		this.fpcode = fpcode;
	}

	public String getPkcost() {
		return pkcost;
	}

	public void setPkcost(String pkcost) {
		this.pkcost = pkcost;
	}

	public String getKpcostp() {
		return kpcostp;
	}

	public void setKpcostp(String kpcostp) {
		this.kpcostp = kpcostp;
	}

	public Double getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}

	public Double getNkpmny() {
		return nkpmny;
	}

	public void setNkpmny(Double nkpmny) {
		this.nkpmny = nkpmny;
	}

	public Double getNnotaxmny() {
		return nnotaxmny;
	}

	public void setNnotaxmny(Double nnotaxmny) {
		this.nnotaxmny = nnotaxmny;
	}

	public Double getNtaxmny() {
		return ntaxmny;
	}

	public void setNtaxmny(Double ntaxmny) {
		this.ntaxmny = ntaxmny;
	}

	public String getDkpqj() {
		return dkpqj;
	}

	public void setDkpqj(String dkpqj) {
		this.dkpqj = dkpqj;
	}
	
}