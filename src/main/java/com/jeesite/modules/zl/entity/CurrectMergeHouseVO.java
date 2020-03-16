package com.jeesite.modules.zl.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CurrectMergeHouseVO  implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<String> housecode;
	private List<Map<String, Object>> houseList;

	
	public List<String> getHousecode() {
		return housecode;
	}
	public void setHousecode(List<String> housecode) {
		this.housecode = housecode;
	}
	public List<Map<String, Object>> getHouseList() {
		return houseList;
	}
	public void setHouseList(List<Map<String, Object>> houseList) {
		this.houseList = houseList;
	}



	
}
