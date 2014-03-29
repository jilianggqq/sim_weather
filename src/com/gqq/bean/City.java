package com.gqq.bean;

import java.io.Serializable;

public class City implements Serializable, Comparable<City> {
	private static final long serialVersionUID = 1L;
	private String province;
	private String city;
	private String number;
	private String firstPY;
	private String allPY;
	private String allFristPY;
	private String citycode;

	public City(String province, String city, String number, String firstPY, String allPY,
			String allFristPY, String citycode) {
		super();
		this.province = province;
		this.city = city;
		this.number = number;
		this.firstPY = firstPY;
		this.allPY = allPY;
		this.allFristPY = allFristPY;
		this.citycode = citycode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFirstPY() {
		return firstPY;
	}

	public void setFirstPY(String firstPY) {
		this.firstPY = firstPY;
	}

	public String getAllPY() {
		return allPY;
	}

	public void setAllPY(String allPY) {
		this.allPY = allPY;
	}

	public String getAllFristPY() {
		return allFristPY;
	}

	public void setAllFristPY(String allFristPY) {
		this.allFristPY = allFristPY;
	}

	@Override
	public String toString() {
		return "City [province=" + province + ", city=" + city + ", number=" + number
				+ ", firstPY=" + firstPY + ", allPY=" + allPY + ", allFristPY=" + allFristPY
				+ ", toString()=" + super.toString() + "]";
	}

	public String getSortKey() {
		return firstPY;
	}

	/**
	 * @return the citycode
	 */
	public String getCitycode() {
		return citycode;
	}

	/**
	 * @param citycode
	 *            the citycode to set
	 */
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	@Override
	public int compareTo(City another) {
		return this.getSortKey().compareTo(another.getSortKey());
	}

}
