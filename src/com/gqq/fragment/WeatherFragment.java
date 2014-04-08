package com.gqq.fragment;

import java.text.*;
import java.util.*;

import android.annotation.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import com.gqq.androidpku.*;
import com.gqq.bean.*;
import com.gqq.util.*;

public abstract class WeatherFragment extends Fragment {

	protected TextView weekTv1, weekTv2, weekTv3;
	protected ImageView weather_imgIv1, weather_imgIv2, weather_imgIv3;
	protected TextView temperatureTv1, temperatureTv2, temperatureTv3;
	protected TextView climateTv1, climateTv2, climateTv3;
	protected TextView windTv1, windTv2, windTv3;

	protected PicUtil mPicUtil = PicUtil.getInstance();

	public abstract void doUpdateFragment(int days, WeatherInfo2 weatherInfo);

	public abstract void initWeeks();

	protected ArrayList<FragmentInfo> mFragmentInfos;

	public void SetFragmentNA() {
		for (FragmentInfo fInfo : mFragmentInfos) {
			updateFragmentNA(fInfo);
		}
	}

	public void initFragments() {
		mFragmentInfos = new ArrayList<WeatherFragment.FragmentInfo>();
		FragmentInfo fi = new FragmentInfo(climateTv1, temperatureTv1, windTv1, weather_imgIv1);
		mFragmentInfos.add(fi);
		fi = new FragmentInfo(climateTv2, temperatureTv2, windTv2, weather_imgIv2);
		mFragmentInfos.add(fi);
		fi = new FragmentInfo(climateTv3, temperatureTv3, windTv3, weather_imgIv3);
		mFragmentInfos.add(fi);

		SetFragmentNA();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.weather_item, container, false);
		View view1 = view.findViewById(R.id.subitem1);
		View view2 = view.findViewById(R.id.subitem2);
		View view3 = view.findViewById(R.id.subitem3);

		weekTv1 = (TextView) view1.findViewById(R.id.week);
		weekTv2 = (TextView) view2.findViewById(R.id.week);
		weekTv3 = (TextView) view3.findViewById(R.id.week);

		weather_imgIv1 = (ImageView) view1.findViewById(R.id.weather_img);
		weather_imgIv2 = (ImageView) view2.findViewById(R.id.weather_img);
		weather_imgIv3 = (ImageView) view3.findViewById(R.id.weather_img);
		temperatureTv1 = (TextView) view1.findViewById(R.id.temperature);
		temperatureTv2 = (TextView) view2.findViewById(R.id.temperature);
		temperatureTv3 = (TextView) view3.findViewById(R.id.temperature);

		climateTv1 = (TextView) view1.findViewById(R.id.climate);
		climateTv2 = (TextView) view2.findViewById(R.id.climate);
		climateTv3 = (TextView) view3.findViewById(R.id.climate);

		windTv1 = (TextView) view1.findViewById(R.id.wind);
		windTv2 = (TextView) view2.findViewById(R.id.wind);
		windTv3 = (TextView) view3.findViewById(R.id.wind);


		initWeeks();

		initFragments();
		return view;
	}

	@SuppressLint("SimpleDateFormat")
	public void updateWeather(ArrayList<WeatherInfo2> infos, Calendar today) {
		if (infos != null && infos.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 此处需要根据天气数据更新相应的组件中的内容
			try {
				for (WeatherInfo2 weatherInfo : infos) {
					Calendar c = Calendar.getInstance();
					c.setTime(sdf.parse(weatherInfo.getDate()));
					int days = c.get(Calendar.DATE) - today.get(Calendar.DATE);
					// if (days >= 1 && days <= 3)
					doUpdateFragment(days, weatherInfo);
					// updateFragment(mFragmentInfos.get(days - 1),
					// weatherInfo);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			// 此处需要设置图片资源
			SetFragmentNA();
		}
	}

	public void updateFragment(FragmentInfo fInfo, WeatherInfo2 weatherInfo) {
		fInfo.getClimateTv().setText(weatherInfo.getWeatherState());
		fInfo.getTemperatureTv().setText(weatherInfo.getTemper());
		fInfo.getWindTv().setText(weatherInfo.getWind());
		fInfo.getWeather_imgIv().setImageResource(
				mPicUtil.getWeatherIcon(weatherInfo.getWeatherState()));
	}

	public void updateFragmentNA(FragmentInfo fInfo) {
		fInfo.getClimateTv().setText("N/A");
		fInfo.getTemperatureTv().setText("N/A");
		fInfo.getWindTv().setText("N/A");
	}

	protected class FragmentInfo {
		private ImageView weather_imgIv;
		private TextView temperatureTv;
		private TextView climateTv;
		private TextView windTv;

		public FragmentInfo(TextView cli, TextView tem, TextView wind, ImageView weather_img) {
			climateTv = cli;
			temperatureTv = tem;
			windTv = wind;
			weather_imgIv = weather_img;
		}

		public ImageView getWeather_imgIv() {
			return weather_imgIv;
		}

		public void setWeather_imgIv(ImageView weather_imgIv) {
			this.weather_imgIv = weather_imgIv;
		}

		public TextView getTemperatureTv() {
			return temperatureTv;
		}

		public void setTemperatureTv(TextView temperatureTv) {
			this.temperatureTv = temperatureTv;
		}

		public TextView getClimateTv() {
			return climateTv;
		}

		public void setClimateTv(TextView climateTv) {
			this.climateTv = climateTv;
		}

		public TextView getWindTv() {
			return windTv;
		}

		public void setWindTv(TextView windTv) {
			this.windTv = windTv;
		}
	}
}
