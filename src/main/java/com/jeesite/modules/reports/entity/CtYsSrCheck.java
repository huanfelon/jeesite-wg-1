/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.reports.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.zl.entity.ZlProject;

/**
 * 检验表Entity
 * @author LY
 * @version 2019-10-31
 */
@Table
public class CtYsSrCheck extends DataEntity<CtYsSrCheck> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Office pkOrg;		// 组织名称
	private ZlProject pkProject;		//项目
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同号", attrName="htcode", align=Align.LEFT, sort=10)
	private String htcode;		//合同号
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="客户名称", attrName="custname", align=Align.LEFT, sort=15)
	private String custname;		//客户名称
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="收费项目", attrName="pkcost", align=Align.LEFT, sort=20)
	private String pkcost;		//收费项目
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同总价", attrName="htze", align=Align.RIGHT, sort=30, dataFormat="#,##0.00")
	private Double htze;		//合同总价
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同期限", attrName="htqj", align=Align.LEFT, sort=40)
	private String htqj;		//合同期限
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="实收款", attrName="ssk", align=Align.RIGHT, sort=50, dataFormat="#,##0.00")
	private Double ssk;		//实收款
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="发票项目", attrName="kpcostp", align=Align.LEFT, sort=60)
	private String kpcostp;		//发票项目
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="发票号", attrName="fpcode", align=Align.LEFT, sort=70)
	private String fpcode;		//发票号
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="开票不含税金额", attrName="kpnotax", align=Align.RIGHT, sort=80, dataFormat="#,##0.00")
	private Double kpnotax;		//开票不含税金额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="开票税金", attrName="kptax", align=Align.RIGHT, sort=90, dataFormat="#,##0.00")
	private Double kptax;		//开票税金
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="确认收入金额", attrName="qrsr", align=Align.RIGHT, sort=100, dataFormat="#,##0.00")
	private Double qrsr;		//确认收入金额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="合同总价与实收差额", attrName="ysce", align=Align.RIGHT, sort=110, dataFormat="#,##0.00")
	private Double ysce;		//合同总价与实收差额
	@com.jeesite.common.utils.excel.annotation.ExcelField(title="开票不含税与确认收入差额", attrName="srce", align=Align.RIGHT, sort=120, dataFormat="#,##0.00")
	private Double srce;		//开票不含税与确认收入差额

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

	public String getPkcost() {
		return pkcost;
	}

	public void setPkcost(String pkcost) {
		this.pkcost = pkcost;
	}

	public Double getHtze() {
		return htze;
	}

	public void setHtze(Double htze) {
		this.htze = htze;
	}

	public String getHtqj() {
		return htqj;
	}

	public void setHtqj(String htqj) {
		this.htqj = htqj;
	}

	public Double getSsk() {
		return ssk;
	}

	public void setSsk(Double ssk) {
		this.ssk = ssk;
	}

	public String getKpcostp() {
		return kpcostp;
	}

	public void setKpcostp(String kpcostp) {
		this.kpcostp = kpcostp;
	}

	public String getFpcode() {
		return fpcode;
	}

	public void setFpcode(String fpcode) {
		this.fpcode = fpcode;
	}

	public Double getKpnotax() {
		return kpnotax;
	}

	public void setKpnotax(Double kpnotax) {
		this.kpnotax = kpnotax;
	}

	public Double getKptax() {
		return kptax;
	}

	public void setKptax(Double kptax) {
		this.kptax = kptax;
	}

	public Double getQrsr() {
		return qrsr;
	}

	public void setQrsr(Double qrsr) {
		this.qrsr = qrsr;
	}

	public Double getYsce() {
		return ysce;
	}

	public void setYsce(Double ysce) {
		this.ysce = ysce;
	}

	public Double getSrce() {
		return srce;
	}

	public void setSrce(Double srce) {
		this.srce = srce;
	}
	
}