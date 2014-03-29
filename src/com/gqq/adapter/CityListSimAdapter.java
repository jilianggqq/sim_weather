package com.gqq.adapter;

import java.util.*;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.gqq.androidpku.*;
import com.gqq.bean.*;

public class CityListSimAdapter extends CityAdapter {

	public CityListSimAdapter(List<City> mList, /* MySectionIndexer mIndexer, */Context mContext) {
		this.mList = mList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.select_city_simp_item, null);

			holder = new ViewHolder();
			holder.province_name = (TextView) view.findViewById(R.id.province_name);
			holder.city_name = (TextView) view.findViewById(R.id.city_name);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		City city = mList.get(position);

		// 在列表上显示省、市信息。
		holder.province_name.setText(city.getProvince());
		holder.city_name.setText(city.getCity());

		return view;
	}

	public static class ViewHolder {
		public TextView province_name;
		public TextView city_name;
	}

}
