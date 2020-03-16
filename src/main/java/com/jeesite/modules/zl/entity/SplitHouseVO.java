package com.jeesite.modules.zl.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SplitHouseVO  implements Serializable{

	private static final long serialVersionUID = 1L;

	private String houseCode;
	private List<Map<String, Object>> houseList;
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	public List<Map<String, Object>> getHouseList() {
		return houseList;
	}
	public void setHouseList(List<Map<String, Object>> houseList) {
		this.houseList = houseList;
	}




	





	
}
