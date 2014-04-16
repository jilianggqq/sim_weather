package com.gqq.app;

import java.io.*;
import java.util.*;

import android.app.*;
import android.os.*;
import android.text.*;
import android.util.*;

import com.baidu.location.*;
import com.gqq.androidpku.*;
import com.gqq.bean.*;
import com.gqq.util.*;

public class MyApplication extends Application {

	private final String AppTag = "MyApp";

	private static MyApplication myApplication;
	private HashMap<String, Integer> mWidgetWeatherIcon;// 插件天气图标

	private CityDB cityDB;

	private WeatherInfo mWeatherInfo;

	// 与定位有关的参数
	public LocationClient mLocationClient = null;
	// public GeofenceClient mGeofenceClient;

	private String current_city;

	public int getWidgetWeatherIcon(String climate) {
		int weatherRes = R.drawable.na;
		if (TextUtils.isEmpty(climate))
			return weatherRes;
		String[] strs = { "晴", "晴" };
		if (climate.contains("转")) {// 天气带转字，取前面那部分
			strs = climate.split("转");
			climate = strs[0];
			if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
				strs = climate.split("到");
				climate = strs[1];
			}
		}
		if (mWidgetWeatherIcon == null || mWidgetWeatherIcon.isEmpty())
			mWidgetWeatherIcon = initWidgetWeather();
		if (mWidgetWeatherIcon.containsKey(climate)) {
			weatherRes = mWidgetWeatherIcon.get(climate);
		}
		return weatherRes;
	}

	private HashMap<String, Integer> initWidgetWeather() {
		if (mWidgetWeatherIcon != null && !mWidgetWeatherIcon.isEmpty())
			return mWidgetWeatherIcon;
		mWidgetWeatherIcon = new HashMap<String, Integer>();
		mWidgetWeatherIcon.put("暴雪", R.drawable.w17);
		mWidgetWeatherIcon.put("暴雨", R.drawable.w10);
		mWidgetWeatherIcon.put("大暴雨", R.drawable.w10);
		mWidgetWeatherIcon.put("大雪", R.drawable.w16);
		mWidgetWeatherIcon.put("大雨", R.drawable.w9);

		mWidgetWeatherIcon.put("多云", R.drawable.w1);
		mWidgetWeatherIcon.put("雷阵雨", R.drawable.w4);
		mWidgetWeatherIcon.put("雷阵雨冰雹", R.drawable.w19);
		mWidgetWeatherIcon.put("晴", R.drawable.w0);
		mWidgetWeatherIcon.put("沙尘暴", R.drawable.w20);

		mWidgetWeatherIcon.put("特大暴雨", R.drawable.w10);
		mWidgetWeatherIcon.put("雾", R.drawable.w18);
		mWidgetWeatherIcon.put("小雪", R.drawable.w14);
		mWidgetWeatherIcon.put("小雨", R.drawable.w7);
		mWidgetWeatherIcon.put("阴", R.drawable.w2);

		mWidgetWeatherIcon.put("雨夹雪", R.drawable.w6);
		mWidgetWeatherIcon.put("阵雪", R.drawable.w13);
		mWidgetWeatherIcon.put("阵雨", R.drawable.w3);
		mWidgetWeatherIcon.put("中雪", R.drawable.w15);
		mWidgetWeatherIcon.put("中雨", R.drawable.w8);
		return mWidgetWeatherIcon;
	}

	@Override
	public void onCreate() {
		mLocationClient = new LocationClient(this);
		// mLocationClient.setAK("697f50541f8d4779124896681cb6584d");
		// 注意替换为自己的key
		mLocationClient.setAK("oj2PPuCMlZQWiU2Rir3aGR9s");

		super.onCreate();
		Log.i(AppTag, "oncreate");
		myApplication = this;

		initCityDB();
	}

	private CityDB initCityDB() {
		String path = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator
				+ "com.gqq.androidpku" + File.separator + "databases" + File.separator
				+ CityDB.CITY_DB_NAME;
		Log.i(AppTag, path);
		File db = new File(path);

		if (!db.exists()) {
//			if (db.exists())
//				db.delete();
			Log.i(AppTag, "file is not exsits");
			try {
				db.getParentFile().mkdirs();
				db.createNewFile();
				InputStream is = getAssets().open("city.db");
				FileOutputStream fos = new FileOutputStream(db, false);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
				Log.i(AppTag, "city.db已经被成功拷贝。地址为：" + path);
			} catch (Exception e) {
				Log.i(AppTag, e.getMessage());
				e.printStackTrace();
				T.showLong(myApplication, e.getMessage());
				System.exit(0);
			}

		} else {
			Log.i(AppTag, "file has already exsited");
		}
		return new CityDB(this, path);
	}

	public static MyApplication getInstance() {
		return myApplication;
	}

	/**
	 * @return the current_city
	 */
	public String getCurrent_city() {
		return current_city;
	}

	/**
	 * @param current_city the current_city to set
	 */
	public void setCurrent_city(String current_city) {
		this.current_city = current_city;
	}

	/**
	 * @return the mWeatherInfo
	 */
	public WeatherInfo getmWeatherInfo() {
		return mWeatherInfo;
	}

	/**
	 * @param mWeatherInfo the mWeatherInfo to set
	 */
	public void setmWeatherInfo(WeatherInfo mWeatherInfo) {
		this.mWeatherInfo = mWeatherInfo;
	}

	private SharePreferenceUtil mSpUtil;
	public synchronized SharePreferenceUtil getSharePreferenceUtil() {
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this, SharePreferenceUtil.CITY_SHAREPRE_FILE);
		return mSpUtil;
	}
}
