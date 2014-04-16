package com.gqq.bean;

public class WeatherInfo {

	private String city;
	private String cityid;
	private String temp;
	private String WD;
	private String WS;
	private String SD;
	private String WSE;
	private String time;
	private String isRadar;
	private String Radar;

	private String weatherState;
	private String day;
	private String date;
	private String lunar;

	public String getWeatherState() {
		return weatherState;
	}

	public void setWeatherState(String weatherState) {
		this.weatherState = weatherState;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getPm2_5() {
		return pm2_5;
	}

	public void setPm2_5(String pm2_5) {
		this.pm2_5 = pm2_5;
	}

	private String quality;
	private String pm2_5;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityId() {
		return cityid;
	}

	public void setCityId(String cityId) {
		this.cityid = cityId;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getWD() {
		return WD;
	}

	public void setWD(String wD) {
		WD = wD;
	}

	public String getWS() {
		return WS;
	}

	public void setWS(String wS) {
		WS = wS;
	}

	public String getSD() {
		return SD;
	}

	public void setSD(String sD) {
		SD = sD;
	}

	public String getWSE() {
		return WSE;
	}

	public void setWSE(String wSE) {
		WSE = wSE;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsRadar() {
		return isRadar;
	}

	public void setIsRadar(String isRadar) {
		this.isRadar = isRadar;
	}

	public String getRadar() {
		return Radar;
	}

	public void setRadar(String radar) {
		Radar = radar;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "WeatherInfo [city=" + city + ", cityid=" + cityid + ", temp=" + temp + ", WD=" + WD
				+ ", WS=" + WS + ", SD=" + SD + ", WSE=" + WSE + ", time=" + time + ", isRadar="
				+ isRadar + ", Radar=" + Radar + ", quality=" + quality + ", pm2_5=" + pm2_5
				+ ", weatherState=" + weatherState + ", day=" + day + ", date=" + date + ", lunar="
				+ lunar + "]";
	}

	/**
	 * @return the lunar
	 */
	public String getLunar() {
		return lunar;
	}

	/**
	 * @param lunar
	 *            the lunar to set
	 */
	public void setLunar(String lunar) {
		this.lunar = lunar;
	}
}
