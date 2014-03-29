package com.gqq.bean;

public class TodayWeather {

	WeatherInfo weatherinfo;

	public WeatherInfo getWeatherInfo() {
		return weatherinfo;
	}

	public void setWeatherInfo(WeatherInfo weatherInfo) {
		this.weatherinfo = weatherInfo;
	}

	@Override
	public String toString() {
		return "TodayWeather [weatherInfo=" + weatherinfo + "]";
	}

}
