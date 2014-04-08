package com.gqq.bean;

public class WeatherInfo2 {

	private String date;
	private String day;
	private String dayornight;
	private String weatherState;
	private String temper;
	private String wind_direct;
	private String wind;


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDayornight() {
		return dayornight;
	}

	public void setDayornight(String dayornight) {
		this.dayornight = dayornight;
	}

	public String getWeatherState() {
		return weatherState;
	}

	public void setWeatherState(String weatherState) {
		this.weatherState = weatherState;
	}

	public String getTemper() {
		return temper;
	}

	public void setTemper(String temper) {
		this.temper = temper;
	}

	public String getWind_direct() {
		return wind_direct;
	}

	public void setWind_direct(String wind_direct) {
		this.wind_direct = wind_direct;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	@Override
	public String toString() {
		return "WeatherInfo2 [date=" + date + ", day=" + day + ", dayornight=" + dayornight
				+ ", weatherState=" + weatherState + ", temper=" + temper + ", wind_direct="
				+ wind_direct + " wind=" + wind + "]";
	}

}
