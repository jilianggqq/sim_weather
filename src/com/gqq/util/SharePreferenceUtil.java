package com.gqq.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	public static final String CITY_SHAREPRE_FILE = "service_city";
	private static final String CASH_CITY = "_city";
	private static final String SIMPLE_CLIMATE = "simple_climate";
	private static final String SIMPLE_TEMP = "simple_temp";
	private static final String SIMPLE_DATE = "simple_date";
	private static final String SIMPLE_DAY = "simple_day";
	private static final String TIMESAMP = "timesamp";
	private static final String TIME = "time";
	private static final String SIMPLE_LUNAR = "simple_lunar";
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// city
	public void setCity(String city) {
		editor.putString(CASH_CITY, city);
		editor.commit();
	}

	public String getCity() {
		return sp.getString(CASH_CITY, "");
	}

	// SimpleClimate
	public void setSimpleClimate(String climate) {
		editor.putString(SIMPLE_CLIMATE, climate);
		editor.commit();
	}

	public String getSimpleClimate() {
		return sp.getString(SIMPLE_CLIMATE, "N/A");
	}

	// SimpleTemp
	public void setSimpleTemp(String temp) {
		editor.putString(SIMPLE_TEMP, temp);
		editor.commit();
	}

	// SimpleTemp
	public void setSimpleDate(String date) {
		editor.putString(SIMPLE_DATE, date);
		editor.commit();
	}

	// SimpleTemp
	public void setSimpleDay(String day) {
		editor.putString(SIMPLE_DAY, day);
		editor.commit();
	}

	public void setSimpleLunar(String lunar) {
		editor.putString(SIMPLE_LUNAR, lunar);
		editor.commit();
	}

	public String getSimpleLunar() {
		return sp.getString(SIMPLE_LUNAR, "");
	}

	public String getSimpleTemp() {
		return sp.getString(SIMPLE_TEMP, "");
	}

	public String getSimpleDate() {
		return sp.getString(SIMPLE_DATE, "");
	}

	public String getSimpleDay() {
		return sp.getString(SIMPLE_DAY, "");
	}

	public void setTimeSamp(long time) {
		editor.putLong(TIMESAMP, time);
		editor.commit();
	}

	public long getTimeSamp() {
		return sp.getLong(TIMESAMP, System.currentTimeMillis());
	}

	// time
	public void setTime(String time) {
		editor.putString(TIME, time);
		editor.commit();
	}

	public String getTime() {
		return sp.getString(TIME, "");
	}
}
