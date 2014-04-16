package com.gqq.service;

import java.text.*;
import java.util.*;

import android.annotation.*;
import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import com.gqq.androidpku.*;
import com.gqq.app.*;
import com.gqq.bean.*;
import com.gqq.util.*;

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
	private SharePreferenceUtil mSpUtil;
	private MyApplication mApplication;
	private WeatherInfoReceiver weatherReceiver;

	// public static final String WEATHERUPDATE = "com.gqq.WEATHERUPDATE";

	// private String cityName;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MainActivity.GET_WEATHER_RESULT:
				WeatherInfo allWeather = mApplication.getmWeatherInfo();
				if (allWeather != null) {
					Log.d("Climate", "weatherInfo:" + allWeather.toString());
					mSpUtil.setSimpleTemp(allWeather.getTemp() + "°");
					mSpUtil.setSimpleDate(allWeather.getDate().substring(5));
					mSpUtil.setSimpleDay(allWeather.getDay().replace("星期", "周"));
					mSpUtil.setSimpleClimate(allWeather.getWeatherState());
					mSpUtil.setCity(allWeather.getCity());
					mSpUtil.setSimpleLunar(allWeather.getLunar().substring(5));
					// String time = allWeather.getTime();
					// mSpUtil.setTimeSamp(DateUtil.getLongTime(time));
					updateWeather();
					T.show(mApplication, "天气更新成功", Toast.LENGTH_SHORT);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 通过activity更新service
	 */
	public void updateServiceByActivity() {
		WeatherInfo allWeather = mApplication.getmWeatherInfo();
		if (allWeather != null) {
			// Log.d("Climate", "temp:" + allWeather.getTemp());
			// if (!TextUtils.isEmpty(allWeather.getTemp())) {
			// mSpUtil.setSimpleTemp(allWeather.getTemp().replace("~",
			// "/").replace("℃", "°"));// 保存一下温度信息，用于小插件
			// } else {
			// mSpUtil.setSimpleTemp(allWeather.getTemp().replace("~",
			// "/").replace("℃", "°"));
			// }
			Log.d("Climate", "weatherInfo:" + allWeather.toString());
			mSpUtil.setSimpleTemp(allWeather.getTemp() + "°");
			mSpUtil.setSimpleDate(allWeather.getDate().substring(5));
			mSpUtil.setSimpleDay(allWeather.getDay().replace("星期", "周"));
			mSpUtil.setSimpleClimate(allWeather.getWeatherState());
			mSpUtil.setCity(allWeather.getCity());
			mSpUtil.setSimpleLunar(allWeather.getLunar().substring(5));
			// String time = allWeather.getTime();
			// mSpUtil.setTimeSamp(DateUtil.getLongTime(time));
			updateWeather();
			T.show(mApplication, "天气更新成功", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = MyApplication.getInstance();
		mSpUtil = mApplication.getSharePreferenceUtil();

		// 动态注册广播接收器
		// weatherReceiver = new WeatherInfoReceiver();
		// IntentFilter intentFilter = new IntentFilter();
		// intentFilter.addAction(WEATHERUPDATE);
		// registerReceiver(weatherReceiver, intentFilter);

		remoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.widget_4x2);// 实例化RemoteViews
		PendingIntent WeatherIconHotAreaPI = PendingIntent.getActivity(this, 0, new Intent(this,
				MainActivity.class), 0);
		remoteViews.setOnClickPendingIntent(R.id.WeatherIconHotArea, WeatherIconHotAreaPI);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(SERVICE_TAG, "onStartCommand");
		updateTime();
		updateWeather();
		registerReceiver();

		weatherReceiver = new WeatherInfoReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MainActivity.UPDATE_WIDGET_WEATHER_Test);
		registerReceiver(weatherReceiver, intentFilter);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(mTimePickerBroadcast);
			unregisterReceiver(weatherReceiver);
		} catch (Exception e) {
			Log.d("Service", "service is not registed");
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MsgBinder();
	}

	public class MsgBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public WeatherUpdateService getService() {
			return WeatherUpdateService.this;
		}
	}

	@SuppressLint("SimpleDateFormat")
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
				// Log.i("Service", "24小时制");
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
			String action = intent.getAction();
			if (action.equals(MainActivity.UPDATE_WIDGET_WEATHER_ACTION)) {
				Log.d("BroadCast", "activity更新消息");
				updateWeather();
			} else {
				Log.d("BroadCast", "时间更新消息");
				updateTime();
			}
		}
	};

	private void registerReceiver() {
		IntentFilter updateIntent = new IntentFilter();
		updateIntent.addAction(MainActivity.UPDATE_WIDGET_WEATHER_ACTION);
		updateIntent.addAction("android.intent.action.TIME_TICK");
		updateIntent.addAction("android.intent.action.TIME_SET");
		updateIntent.addAction("android.intent.action.DATE_CHANGED");
		updateIntent.addAction("android.intent.action.TIMEZONE_CHANGED");
		registerReceiver(mTimePickerBroadcast, updateIntent);
	}

	private void updateWeather() {
		String city = mSpUtil.getCity();
		String climate = PicUtil.getClimate(mSpUtil.getSimpleClimate());
		String temp = mSpUtil.getSimpleTemp();
		String date = mSpUtil.getSimpleDate();
		String day = mSpUtil.getSimpleDay();
		String lunar = mSpUtil.getSimpleLunar();

		// L.i(city + " " + climate + " " + temp);
		remoteViews.setTextViewText(R.id.weather_icon_left, city + "\r\n" + climate + " " + temp);
		remoteViews.setTextViewText(R.id.weather_icon_right, date + " " + day + "\r\n" + lunar);
		remoteViews.setImageViewResource(R.id.weather_icon, MyApplication.getInstance()
				.getWidgetWeatherIcon(mSpUtil.getSimpleClimate()));
		ComponentName componentName = new ComponentName(getApplication(), WeatherWidget.class);
		AppWidgetManager.getInstance(getApplication()).updateAppWidget(componentName, remoteViews);

		// String cityName = MyApplication.getInstance().getCurrent_city();
		// if (null == cityName)
		// cityName = "大学";
		// Log.d(MainActivity.SERVICE_TAG, "city:" + cityName);
		// // String city = "大兴";
		// String climate = "阴";
		// String temp = 30 + "℃";
		// Log.i("Service", cityName + "" + climate + "" + temp);
		// remoteViews.setTextViewText(R.id.weather_icon_left, cityName + "\r\n"
		// + climate + " "
		// + temp);
		// remoteViews.setImageViewResource(R.id.weather_icon,
		// R.drawable.biz_plugin_weather_yin);
		// ComponentName componentName = new ComponentName(getApplication(),
		// WeatherWidget.class);
		// AppWidgetManager.getInstance(getApplication()).updateAppWidget(componentName,
		// remoteViews);
	}

	/**
	 * 定义一个测试的广播接收器
	 * 
	 * @author len
	 * 
	 */
	public class WeatherInfoReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// // 拿到进度，更新UI
			// int progress = intent.getIntExtra("progress", 0);
			// mProgressBar.setProgress(progress);
			if (MainActivity.UPDATE_WIDGET_WEATHER_Test.equals(intent.getAction()))
				Log.d("BroadCast", "test successful");
		}

	}

}
