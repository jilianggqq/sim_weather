package com.gqq.androidpku;

import com.gqq.service.*;

import android.appwidget.*;
import android.content.*;
import android.util.*;

public class WeatherWidget extends AppWidgetProvider {

	private final static String WEATHER_WIDGET = "WeatherWidget";

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
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.i(WEATHER_WIDGET, "onReceive action=" + action);
		super.onReceive(context, intent);
	}
}
