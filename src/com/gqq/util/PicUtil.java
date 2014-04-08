package com.gqq.util;

import java.util.*;

import android.R.*;
import android.text.*;

import com.gqq.androidpku.*;

public class PicUtil {

	// 使用单件模式，减少图片占用的内存
	public static PicUtil mPicUtil = new PicUtil();

	public static PicUtil getInstance() {
		return mPicUtil;
	}

	private PicUtil() {
		initBeijingBgMap();
		initOtherBgMap();
		initWeatherIconMap();
	}

	private Map<String, Integer> mWeatherIcon;// 天气图标
	private Map<String, Integer> mBeijingBgImg;// 天气图标
	private Map<String, Integer> mOtherBgImg;// 天气图标

	public Map<String, Integer> getmWeatherIcon() {
		return mWeatherIcon;
	}

	public void setmWeatherIcon(Map<String, Integer> mWeatherIcon) {
		this.mWeatherIcon = mWeatherIcon;
	}

	public Map<String, Integer> getmBeijingBgImg() {
		return mBeijingBgImg;
	}

	public void setmBeijingBgImg(Map<String, Integer> mBeijingBgImg) {
		this.mBeijingBgImg = mBeijingBgImg;
	}

	public Map<String, Integer> getmOtherBgImg() {
		return mOtherBgImg;
	}

	public void setmOtherBgImg(Map<String, Integer> mOtherBgImg) {
		this.mOtherBgImg = mOtherBgImg;
	}



	/**
	 * 初始化图片字典
	 */
	private void initWeatherIconMap() {
		mWeatherIcon = new HashMap<String, Integer>();
		mWeatherIcon.put("暴雪", R.drawable.biz_plugin_weather_baoxue);
		mWeatherIcon.put("暴雨", R.drawable.biz_plugin_weather_baoyu);
		mWeatherIcon.put("大暴雨", R.drawable.biz_plugin_weather_dabaoyu);
		mWeatherIcon.put("大雪", R.drawable.biz_plugin_weather_daxue);
		mWeatherIcon.put("大雨", R.drawable.biz_plugin_weather_dayu);

		mWeatherIcon.put("多云", R.drawable.biz_plugin_weather_duoyun);
		mWeatherIcon.put("雷阵雨", R.drawable.biz_plugin_weather_leizhenyu);
		mWeatherIcon.put("雷阵雨冰雹", R.drawable.biz_plugin_weather_leizhenyubingbao);
		mWeatherIcon.put("晴", R.drawable.biz_plugin_weather_qing);
		mWeatherIcon.put("沙尘暴", R.drawable.biz_plugin_weather_shachenbao);

		mWeatherIcon.put("特大暴雨", R.drawable.biz_plugin_weather_tedabaoyu);
		mWeatherIcon.put("雾", R.drawable.biz_plugin_weather_wu);
		mWeatherIcon.put("小雪", R.drawable.biz_plugin_weather_xiaoxue);
		mWeatherIcon.put("小雨", R.drawable.biz_plugin_weather_xiaoyu);
		mWeatherIcon.put("阴", R.drawable.biz_plugin_weather_yin);

		mWeatherIcon.put("雨夹雪", R.drawable.biz_plugin_weather_yujiaxue);
		mWeatherIcon.put("阵雪", R.drawable.biz_plugin_weather_zhenxue);
		mWeatherIcon.put("阵雨", R.drawable.biz_plugin_weather_zhenyu);
		mWeatherIcon.put("中雪", R.drawable.biz_plugin_weather_zhongxue);
		mWeatherIcon.put("中雨", R.drawable.biz_plugin_weather_zhongyu);
	}

	/**
	 * 初始化北京背景图片
	 */
	private void initBeijingBgMap() {
		mBeijingBgImg = new HashMap<String, Integer>();
		mBeijingBgImg.put("暴雪", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("暴雨", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("大暴雨", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("大雪", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("大雨", R.drawable.biz_plugin_weather_beijing_yu_bg);

		mBeijingBgImg.put("多云", R.drawable.biz_plugin_weather_beijing_yin_bg);
		mBeijingBgImg.put("雷阵雨", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("雷阵雨冰雹", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("晴", R.drawable.biz_plugin_weather_beijing_qing_bg);
		mBeijingBgImg.put("沙尘暴", R.drawable.biz_plugin_weather_beijing_mai_bg);

		mBeijingBgImg.put("特大暴雨", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("雾", R.drawable.biz_plugin_weather_beijing_mai_bg);
		mBeijingBgImg.put("霾", R.drawable.biz_plugin_weather_beijing_mai_bg);
		mBeijingBgImg.put("小雪", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("小雨", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("阴", R.drawable.biz_plugin_weather_beijing_yin_bg);

		mBeijingBgImg.put("雨夹雪", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("阵雪", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("阵雨", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("中雪", R.drawable.biz_plugin_weather_beijing_yu_bg);
		mBeijingBgImg.put("中雨", R.drawable.biz_plugin_weather_beijing_yu_bg);
	}

	/**
	 * 初始化其它各地背景图片
	 */
	private void initOtherBgMap() {
		mOtherBgImg = new HashMap<String, Integer>();
		mOtherBgImg.put("暴雪", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("暴雨", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("大暴雨", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("大雪", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("大雨", R.drawable.biz_plugin_weather_zg_yu_bg);

		mOtherBgImg.put("多云", R.drawable.biz_plugin_weather_zg_yin_bg);
		mOtherBgImg.put("雷阵雨", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("雷阵雨冰雹", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("晴", R.drawable.biz_plugin_weather_zg_qing_bg);
		mOtherBgImg.put("沙尘暴", R.drawable.biz_plugin_weather_zg_yin_bg);

		mOtherBgImg.put("特大暴雨", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("雾", R.drawable.biz_plugin_weather_zg_yin_bg);
		mOtherBgImg.put("霾", R.drawable.biz_plugin_weather_zg_yin_bg);
		mOtherBgImg.put("小雪", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("小雨", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("阴", R.drawable.biz_plugin_weather_zg_yin_bg);

		mOtherBgImg.put("雨夹雪", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("阵雪", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("阵雨", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("中雪", R.drawable.biz_plugin_weather_zg_yu_bg);
		mOtherBgImg.put("中雨", R.drawable.biz_plugin_weather_zg_yu_bg);
	}

	public static String getClimate(String mWeatherState) {
		String climate = mWeatherState;
		String[] strs = { "晴", "晴" };
		if (climate.contains("转")) {// 天气带转字，取前面那部分
			strs = climate.split("转");
			climate = strs[0];
			if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
				strs = climate.split("到");
				climate = strs[1];
			}
		}
		return climate;
	}

	/**
	 * 获得图片图标。
	 * 
	 * @return
	 */
	public int getWeatherIcon(String mWeatherState) {
		// climate = "小雨";
		int weatherRes = R.drawable.biz_plugin_weather_qing;
		String climate = getClimate(mWeatherState);
		if (TextUtils.isEmpty(climate))
			return weatherRes;
		if (mWeatherIcon.containsKey(climate)) {
			weatherRes = mWeatherIcon.get(climate);
		}
		return weatherRes;
	}
}
