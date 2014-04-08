package com.gqq.fragment;

import java.util.*;

import com.gqq.bean.*;

import android.support.v4.app.*;

public abstract class WeatherFragment extends Fragment {

	public abstract void updateWeather(ArrayList<WeatherInfo2> infos, Calendar today);
}
