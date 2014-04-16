package com.gqq.androidpku;

import android.appwidget.*;
import android.content.*;
import android.util.*;

import com.gqq.service.*;

public class WeatherWidget extends AppWidgetProvider {

	private final static String WEATHER_WIDGET = "WeatherWidget";
	public static final String TEXTINFO_LEFT_HOTAREA_ACTION = "TextInfoLeftHotArea";
	public static final String WEATHERICON_HOTAREA_ACTION = "WeatherIconHotArea";
	public static final String TEXTINFO_RIGHT_HOTAREA_ACTION = "TextInfoRightHotArea";
	public static final String TIME_LEFT_HOTAREA_ACTION = "TimeLeftHotArea";
	public static final String TIME_RIGHT_HOTAREA_ACTION = "TimeRightHotArea";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i(WEATHER_WIDGET, "onUpdate");
		// context.sta
		context.startService(new Intent(context, WeatherUpdateService.class));
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i(WEATHER_WIDGET, "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		Log.i(WEATHER_WIDGET, "onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		Log.i(WEATHER_WIDGET, "onDisabled");
		context.stopService(new Intent(context, WeatherUpdateService.class));
		super.onDisabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(WEATHER_WIDGET, "onReceive action=" + action);
		super.onReceive(context, intent);
		Log.i("WeatherWidget", "onReceive action = " + action);
		if (action.equals("android.intent.action.USER_PRESENT")) {// 用户唤醒设备时启动服务
			context.startService(new Intent(context, WeatherUpdateService.class));
		} else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			context.startService(new Intent(context, WeatherUpdateService.class));
		} else if (action.equals(TEXTINFO_LEFT_HOTAREA_ACTION)) {
			Log.d(WEATHER_WIDGET, "widget get weather action.........");
			Intent updateIntent = new Intent(context, WeatherUpdateService.class);
			updateIntent.setAction(TEXTINFO_LEFT_HOTAREA_ACTION);
			context.startService(updateIntent);
		}
		super.onReceive(context, intent);
	}
}
