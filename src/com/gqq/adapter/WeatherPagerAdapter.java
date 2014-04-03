package com.gqq.adapter;

import java.util.*;

import android.support.v4.app.*;

public class WeatherPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	public WeatherPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
