package com.gqq.bean;

public class BasicCityInfo {
	private String citycode;
	private String province;
	private String cityname;

	public BasicCityInfo(String cityname, String province) {
		this.cityname = cityname;
		this.province = province;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
}
