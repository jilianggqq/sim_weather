package com.gqq.adapter;

import java.util.*;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.gqq.bean.*;

public abstract class CityAdapter extends BaseAdapter {
	protected List<City> mList;

	protected Context mContext;
	protected LayoutInflater mInflater;

	public List<City> getCityList() {
		return mList;
	}
}
