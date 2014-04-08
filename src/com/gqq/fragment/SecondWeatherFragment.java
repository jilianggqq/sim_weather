package com.gqq.fragment;

import java.util.*;

import com.gqq.bean.*;
import com.gqq.util.*;

public class SecondWeatherFragment extends WeatherFragment {

	@Override
	public void doUpdateFragment(int days, WeatherInfo2 weatherInfo) {
		if (days >= 4 && days <= 6)
			updateFragment(mFragmentInfos.get(days - 4), weatherInfo);
	}

	@Override
	public void initWeeks() {
		// TODO Auto-generated method stub
		weekTv1.setText(DateUtil.getAfterDay(4));
		weekTv2.setText(DateUtil.getAfterDay(5));
		weekTv3.setText(DateUtil.getAfterDay(6));
	}

}
