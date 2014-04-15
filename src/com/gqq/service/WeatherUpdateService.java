package com.gqq.service;

import java.text.*;
import java.util.*;

import javax.crypto.*;

import android.R.*;
import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import com.baidu.location.*;
import com.gqq.androidpku.*;
import com.gqq.bean.*;

public class WeatherUpdateService extends Service {


	private static final int TIME_FORMAT_24 = 0;
	private static final int TIME_FORMAT_AM = 1;
	private static final int TIME_FORMAT_PM = 2;
	private static String SERVICE_TAG = "Services";
	private RemoteViews remoteViews;
	private int[] timesImg = { R.drawable.nw0, R.drawable.nw1, R.drawable.nw2, R.drawable.nw3,
			R.drawable.nw4, R.drawable.nw5, R.drawable.nw6, R.drawable.nw7, R.drawable.nw8,
			R.drawable.nw9, };
	private int[] dateViews = { R.id.h_left, R.id.h_right, R.id.m_left, R.id.m_right };

	@Override
	public void onCreate() {
		super.onCreate();
		remoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.widget_4x2);// 实例化RemoteViews
		PendingIntent WeatherIconHotAreaPI = PendingIntent.getActivity(this, 0, new Intent(this,
				MainActivity.class), 0);
		remoteViews.setOnClickPendingIntent(R.id.WeatherIconHotArea, WeatherIconHotAreaPI);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(SERVICE_TAG, "onStartCommand");
		updateTime();
		updateWeather("大兴");
		registerReceiver();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void updateTime() {
		int timeFormat = getTimeFormat();
		// 定义SimpleDateFormat对象
		SimpleDateFormat df = new SimpleDateFormat("hhmm");
		if (timeFormat == TIME_FORMAT_24) {
			df = new SimpleDateFormat("HHmm");
		}
		// 将当前时间格式化成HHmm的形式
		String timeStr = df.format(new Date(System.currentTimeMillis()));
		for (int i = 0; i < timeStr.length(); i++) {
			// 将第i个数字字符转换为对应的数字
			int num2 = Integer.parseInt(timeStr.substring(i, i + 1));
			// 将第i个图片的设为对应的数字图片
			// Log.i("WeatherWidget", "时间：" + num2);
			remoteViews.setImageViewResource(dateViews[i], timesImg[num2]);
		}
		if (timeFormat == 1) {
			remoteViews.setImageViewResource(R.id.am_pm, R.drawable.w_amw);
		} else if (timeFormat == 2) {
			remoteViews.setImageViewResource(R.id.am_pm, R.drawable.w_pmw);
		}
		ComponentName componentName = new ComponentName(getApplication(), WeatherWidget.class);
		AppWidgetManager.getInstance(getApplication()).updateAppWidget(componentName, remoteViews);
	}

	/**
	 * 获取时间是24小时制还是12小时制
	 */
	private int getTimeFormat() {
		ContentResolver cv = this.getContentResolver();
		String strTimeFormat = android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.TIME_12_24);
		if (strTimeFormat != null) {
			if (strTimeFormat.equals("24")) {
				Log.i("MYAPP", "24小时制");
				return TIME_FORMAT_24;
			} else {
				// String amPmValues;
				Calendar c = Calendar.getInstance();
				if (c.get(Calendar.AM_PM) == 0) {
					// amPmValues = "AM";
					return TIME_FORMAT_AM;
				} else {
					// amPmValues = "PM";
					return TIME_FORMAT_PM;
				}
			}
		}
		return TIME_FORMAT_24;
	}

	private BroadcastReceiver mTimePickerBroadcast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateTime();
		}
	};


	private void registerReceiver() {
		IntentFilter updateIntent = new IntentFilter();
		updateIntent.addAction("android.intent.action.TIME_TICK");
		updateIntent.addAction("android.intent.action.TIME_SET");
		updateIntent.addAction("android.intent.action.DATE_CHANGED");
		updateIntent.addAction("android.intent.action.TIMEZONE_CHANGED");
		registerReceiver(mTimePickerBroadcast, updateIntent);
	}

	private void updateWeather(String city) {
		// String city = "大兴";
		String climate = "阴";
		String temp = 30 + "℃";
		Log.i("MYAPP", city + "" + climate + "" + temp);
		remoteViews.setTextViewText(R.id.weather_icon_left, city + "\r\n" + climate + " " + temp);
		remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.biz_plugin_weather_yin);
		ComponentName componentName = new ComponentName(getApplication(), WeatherWidget.class);
		AppWidgetManager.getInstance(getApplication()).updateAppWidget(componentName, remoteViews);
	}


}
