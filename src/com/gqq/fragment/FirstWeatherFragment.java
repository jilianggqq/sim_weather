package com.gqq.fragment;

import com.gqq.bean.*;
import com.gqq.util.*;

public class FirstWeatherFragment extends WeatherFragment {

	@Override
	public void doUpdateFragment(int days, WeatherInfo2 weatherInfo) {
		if (days >= 1 && days <= 3)
			updateFragment(mFragmentInfos.get(days - 1), weatherInfo);
	}

	@Override
	public void initWeeks() {
		weekTv1.setText(DateUtil.getAfterDay(1));
		weekTv2.setText(DateUtil.getAfterDay(2));
		weekTv3.setText(DateUtil.getAfterDay(3));
	}

}
