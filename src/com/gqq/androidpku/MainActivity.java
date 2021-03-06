package com.gqq.androidpku;

import java.io.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;
import org.json.*;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.os.Process;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.baidu.location.*;
import com.google.gson.*;
import com.gqq.adapter.*;
import com.gqq.app.*;
import com.gqq.bean.*;
import com.gqq.fragment.*;
import com.gqq.service.*;
import com.gqq.util.*;
import com.umeng.analytics.*;
import com.umeng.fb.*;
import com.umeng.update.*;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
	String tag = "生命周期";

	public static final String UPDATE_WIDGET_WEATHER_ACTION = "com.gqq.action.update_weather";
	public static final String UPDATE_WIDGET_WEATHER_Test = "com.gqq.action.weather.test";
	CharSequence[] items = { "Google", "Apple", "Microsoft" };
	boolean[] itemsChecked = new boolean[items.length];
	ProgressDialog progressDialog;

	private final String WEATHER_URL_1 = "http://www.weather.com.cn/data/sk/";
	// 利用该Url可以获得天气状况。
	private final String WEATHER_URL_2 = "http://www.weather.com.cn/data/cityinfo/";
	// http://gqqapp.sinaapp.com/pm.php
	private final String WEATHER_URL_3 = "http://gqqapp.sinaapp.com/air.php";
	private final String WEATHER_URL_4 = "http://gqqapp.sinaapp.com/weatherapi2.php";
	private final String BEIJING_CITYCODE = "101010100";
	private final String HTML = ".html";
	private final String _N_A = "N/A";
	private final String CITYTAG = "Cities";
	public static final String SERVICE_TAG = "Services";
	private final String ALLWEATHERTAG = "All_weather";
	private final String ERROR = "ERROR";

	private WeatherInfo mWeatherInfo;
	private PicUtil mPicUtil = PicUtil.getInstance();
	private ArrayList<WeatherInfo2> mHistoryInfos;
	private String mWeatherState;
	private String mPM2_5;
	private String quality;
	private SharePreferenceUtil mSpUtil;
	// private String citycode;
	// private String province = "北京";
	// private String cityname = "北京";
	private BasicCityInfo cityInfo = new BasicCityInfo("北京", "北京");

	private Gson mGson;

	public static final int GET_WEATHER_RESULT = 3;
	private static final int GET_HISTORY_WEATHER_RESULT = 4;
	private static final int GET_CITY_FALSE = 98;
	private static final int RESUME_EXIT_FALSE = 99;
	private static final int REQUESTCODE = 1;
	private static final String baidumaptag = "baidumap";
	// protected static final int GET_WEATHER_SCUESS = 0;

	private boolean exitFlag = false;
	Animation operatingAnim;
	LinearInterpolator lin;
	private LocationClient mLocClient;
	FeedbackAgent agent;

	// 加入Fragment的代码
	private ViewPager mViewPager;
	private WeatherPagerAdapter mWeatherPagerAdapter;
	private List<WeatherFragment> fragments;
	private WeatherUpdateService updateService;

	// 定义一个Handler，用于线程同步。
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_WEATHER_RESULT:
				updateWeatherInfo();
				// 让图标停止旋转
				resumeUpdateIcon();
				// 保存缓存文档，下次进入后自动加载
				updateFile();
				// 更新服务
				updateService();
				break;
			case GET_CITY_FALSE:
				resumeUpdateIcon();
				break;
			case RESUME_EXIT_FALSE:
				exitFlag = false;
				break;
			case GET_HISTORY_WEATHER_RESULT:
				updateFragments();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 定义connection，用于activity和application之间的通信。
	 */
	ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(SERVICE_TAG, "connection connected");
			updateService = ((WeatherUpdateService.MsgBinder) service).getService();
			MyApplication.getInstance().setmWeatherInfo(mWeatherInfo);
			updateService.updateServiceByActivity();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(SERVICE_TAG, "connection disconnected");

		}
	};

	/**
	 * 更新服务端的内容，让服务更新widget
	 */
	private void updateService() {
		Log.d("BroadCast", "send broadcast");
		// Intent intent = new Intent(this, WeatherUpdateService.class);
		// // startService(intent);
		// bindService(intent, connection, Context.BIND_AUTO_CREATE);
		// startService(intent);
		sendBroadcast(new Intent(UPDATE_WIDGET_WEATHER_ACTION));
		sendBroadcast(new Intent(UPDATE_WIDGET_WEATHER_Test));
	}

	/**
	 * 恢复，转圈停止，rooter消失
	 */
	private void resumeUpdateIcon() {
		// mUpdate.setImageResource(R.drawable.title_update);
		findViewById(R.id.footer).setVisibility(View.GONE);
		mUpdate.clearAnimation();
		imgRefresh.clearAnimation();
	}

	/**
	 * 更行数据中，转圈，等待……
	 */
	private void rotateUpdateIcon() {
		rotateBigIcon();
		rotateSmallIcon();

	}

	private void rotateSmallIcon() {
		// 旋转更新按钮
		if (operatingAnim != null) {
			findViewById(R.id.footer).setVisibility(View.VISIBLE);
			// mUpdate.setImageResource(R.drawable.base_loading_large_icon);
			mUpdate.startAnimation(operatingAnim);
		}

	}

	private void rotateBigIcon() {
		if (operatingAnim != null)
			imgRefresh.startAnimation(operatingAnim);
	}

	// http://www.weather.cn
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置背景图
		// if (initContentView()) {
		// setContentView(R.layout.start);
		// return;
		// }

		// 通知用户有新的消息
		agent = new FeedbackAgent(this);
		agent.sync();

		requestWindowFeature(Window.FEATURE_NO_TITLE);//
		setContentView(R.layout.activity_main);
		init();

		mCityManagerBtn = (ImageView) findViewById(R.id.title_city_manager);
		mCityManagerBtn.setOnClickListener(this);

		mUpdate = (ImageView) findViewById(R.id.title_update);
		imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
		mUpdate.setOnClickListener(this);
		mFeedback = (ImageView) findViewById(R.id.title_feedback);
		mFeedback.setOnClickListener(this);
		mShare = (ImageView) findViewById(R.id.title_share);
		mShare.setOnClickListener(this);
		mSpUtil = MyApplication.getInstance().getSharePreferenceUtil();

		// 设置旋转变量
		operatingAnim = AnimationUtils.loadAnimation(this, R.anim.update_anim);
		lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);

		// 设置本地位置
		mLocClient = ((MyApplication) getApplication()).mLocationClient;
		mLocation = (ImageView) findViewById(R.id.title_location);
		mLocation.setOnClickListener(this);
		mLocClient.registerLocationListener(new myLocationListener());

		startService(new Intent(this, WeatherUpdateService.class));

		// 初始化fragments
		initFragments();

		// 第一次更新天气
		firstUpdateWeather();
		// UmengUpdateAgent.update(this);

		// 设置自动更新
		setAutoUpdate();
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragments() {
		fragments = new ArrayList<WeatherFragment>();
		fragments.add(new FirstWeatherFragment());
		fragments.add(new SecondWeatherFragment());
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mWeatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mWeatherPagerAdapter);

	}

	/**
	 * 设置自动更新
	 */
	private void setAutoUpdate() {
		// 不只是wifi下才更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
	}

	/**
	 * 刚进入地图，加载天气信息。
	 */
	private void firstUpdateWeather() {
		Log.d(tag, "onstart");
		SharedPreferences appPrefs = getSharedPreferences("selected_info", MODE_PRIVATE);
		cityInfo.setCitycode(appPrefs.getString("citycode", BEIJING_CITYCODE));
		cityInfo.setProvince(appPrefs.getString("province", "北京"));
		cityInfo.setCityname(appPrefs.getString("city", "北京"));

		Log.d("CityCode", cityInfo.getCitycode());
		updateWeather(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public void onRestart() {
		super.onRestart();
		Log.d(tag, "In the onRestart() event");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		Log.d(tag, "In the onResume() event");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		Log.d(tag, "pause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(tag, "onstop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// try {
		// unbindService(connection);
		//
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		Log.d(tag, "ondestroy");
	}

	/**
	 * 实现OnClickListener的onClick方法。 这个是View类执行performOnclick的回调函数。
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_city_manager:
			Intent i = new Intent(this, SelectCity.class);
			// startActivity(i);
			startActivityForResult(i, REQUESTCODE);
			break;

		case R.id.title_update:

			updateWeather(false);
			break;
		case R.id.title_location:
			updateLocalWeather();
			break;
		case R.id.title_feedback:
			agent.startFeedbackActivity();
			break;
		case R.id.title_share:
			// Drawable d = getResources().getDrawable(R.drawable.erweima);
			// d.
			// Bitmap b = (BitmapDrawable) d;

			// Resources r = this.getBaseContext().getResources();
			// InputStream is = r.openRawResource(R.drawable.erweima);
			// BitmapDrawable bmpDraw = new BitmapDrawable(is);
			// Bitmap bmp = bmpDraw.getBitmap();
			//
			// Intent intent = new Intent(Intent.ACTION_SEND);
			// intent.setType("image/*");
			// intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			// intent.putExtra(Intent.EXTRA_TEXT,
			// "下载地址：http://gqqapp.sinaapp.com/simweather.apk");
			// intent.putExtra(Intent.EXTRA_STREAM,is.);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(Intent.createChooser(intent, getTitle()));
			shareMsg(this, getTitle(), "下载地址", "http://gqqapp.sinaapp.com/simweather.apk");
			break;
		default:
			break;
		}
	}

	/**
	 * 分享功能
	 * 
	 * @param context
	 *            上下文
	 * @param charSequence
	 *            Activity的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param imgPath
	 *            图片路径，不分享图片则传null
	 */
	public void shareMsg(Context context, CharSequence charSequence, String msgTitle, String msgText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		// Resources res = this.getBaseContext().getResources();
		// Bitmap bitmap = BitmapFactory.decodeResource(res,
		// R.drawable.erweima);
		// intent.putExtra(Intent.EXTRA_STREAM, bitmap);
		// 分享drawable中的图片。
		Uri imageUri = Uri.parse("android.resource://com.gqq.androidpku/" + R.drawable.erweima);
		// 获取网上的图片还是不行啊
		// Uri imageUri = Uri.parse("http://gqqapp.sinaapp.com/erweima.png");
		Log.d("image", imageUri.toString());

		intent.setType("image/*");

		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.putExtra(Intent.EXTRA_STREAM, imageUri);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, charSequence));
	}

	/**
	 * 更新本地的天气
	 */
	private void updateLocalWeather() {
		Log.d(baidumaptag, "updateLocalWeather");
		setLocationOption();
		mLocClient.start();
		rotateBigIcon();
		T.showLong(this, "正在查找您的位置……");
		if (mLocClient != null && mLocClient.isStarted()) {
			// setLocationOption();
			mLocClient.requestLocation();
		} else
			Log.d(baidumaptag, "locClient is null or not started");
		Log.d(baidumaptag, "... mlocBtn onClick... pid=" + Process.myPid());
		Log.d(baidumaptag, "version:" + mLocClient.getVersion());

	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setScanSpan(500);
		option.setPoiDistance(1000);
		// option.setScanSpan(3000);

		// if (mPriorityCheck.isChecked()) {
		// option.setPriority(LocationClientOption.NetWorkFirst); //
		// } else {
		// option.setPriority(LocationClientOption.GpsFirst); //
		// }

		option.setPoiNumber(10);
		option.disableCache(true);
		mLocClient.setLocOption(option);
	}

	private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv,
			climateTv, windTv, provinceTv;
	private ImageView weatherImg, pmImg;

	private ImageView mCityManagerBtn, mUpdate, imgRefresh, mLocation, mFeedback, mShare;

	/**
	 * 如果从intent返回了数值，和文件中比对，看是否需要更新天气。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 2) {
			if (requestCode == REQUESTCODE) {
				// int request = data.getIntExtra("three", 0);
				// 接收返回数据
				// 接受到返回数据之后，和文件中的比对，如果citycode相同，则返回；如果不同，则刷新。
				cityInfo.setCityname(data.getStringExtra("city"));

				SharedPreferences appPrefs = getSharedPreferences("selected_info", MODE_PRIVATE);
				String filCity = appPrefs.getString("city", "");

				if (!filCity.equals(cityInfo.getCityname())) {
					cityInfo.setCitycode(data.getStringExtra("citycode"));
					cityInfo.setProvince(data.getStringExtra("province"));
					updateWeather(false);
				}
			}
		}
	}

	/**
	 * 更新shared preference文件。
	 */
	public void updateFile() {
		SharedPreferences appPrefs = getSharedPreferences("selected_info", MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = appPrefs.edit();
		prefsEditor.clear();
		prefsEditor.putString("city", cityInfo.getCityname());
		prefsEditor.putString("citycode", cityInfo.getCitycode());
		prefsEditor.putString("province", cityInfo.getProvince());
		prefsEditor.commit();
		WeatherInfo allWeather = mWeatherInfo;
		mSpUtil.setSimpleTemp(allWeather.getTemp() + "°");
		mSpUtil.setSimpleDate(allWeather.getDate().substring(5));
		mSpUtil.setSimpleDay(allWeather.getDay().replace("星期", "周"));
		mSpUtil.setSimpleClimate(allWeather.getWeatherState());
		mSpUtil.setCity(allWeather.getCity());
		mSpUtil.setSimpleLunar(allWeather.getLunar().substring(5));
	}

	/**
	 * 初始化显示页面
	 */
	private void init() {
		mWeatherInfo = null;

		cityTv = (TextView) findViewById(R.id.city);
		provinceTv = (TextView) findViewById(R.id.title_province);
		temperatureTv = (TextView) findViewById(R.id.temperature);
		humidityTv = (TextView) findViewById(R.id.humidity);
		timeTv = (TextView) findViewById(R.id.pub_time);
		weekTv = (TextView) findViewById(R.id.week_today);
		pmDataTv = (TextView) findViewById(R.id.pm_data);
		pmQualityTv = (TextView) findViewById(R.id.pm_quality);
		climateTv = (TextView) findViewById(R.id.climate);
		windTv = (TextView) findViewById(R.id.wind);

		weatherImg = (ImageView) findViewById(R.id.weather_img);
		pmImg = (ImageView) findViewById(R.id.pm_img);

		cityTv.setText("北京");
		timeTv.setText(_N_A);
		humidityTv.setText(_N_A);
		pmDataTv.setText(_N_A);
		pmQualityTv.setText(_N_A);
		weekTv.setText(_N_A);
		temperatureTv.setText(_N_A);
		climateTv.setText(_N_A);
		windTv.setText(_N_A);

	}

	/**
	 * 更新天气
	 */
	private void updateWeather(final boolean needUpdateCitycode) {

		rotateUpdateIcon();

		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			T.showLong(this, "网络不给力，请稍后。。。");
			return;
		}
		T.showShort(this, "正在更新天气");

		// 这种情况下，表示城市名是对的，但是城市的code不一定对，因为百度的接口没有返回code
		if (needUpdateCitycode) {
			if (!updateCitycode()) {
				mHandler.sendEmptyMessage(GET_CITY_FALSE);
				return;
			}
		}

		new Thread() {
			public void run() {
				super.run();
				getTodayWeatherInfo();
				mHandler.sendEmptyMessage(GET_WEATHER_RESULT);
			}
		}.start();

		/**
		 * 从网络获取数据，更新fragment
		 */
		new Thread() {
			public void run() {
				Log.d(ALLWEATHERTAG, "all infos ...");
				super.run();
				String url = WEATHER_URL_4 + "?citycode=" + cityInfo.getCitycode();
				Log.d(ALLWEATHERTAG, url);
				String allJsonWeathers = connServerForResult(url);
				// Log.d(ALLWEATHERTAG, allJsonWeathers);
				parseHistoryWeaterInfos(allJsonWeathers);
				mHandler.sendEmptyMessage(GET_HISTORY_WEATHER_RESULT);
			};
		}.start();
	}

	/**
	 * 通过城市名得到城市的citycode
	 * 
	 * @return
	 */
	private boolean updateCitycode() {
		CityDB cityDB = new CityDB(getBaseContext(), "city.db");
		try {
			City city = cityDB.getCity(cityInfo.getCityname());
			cityInfo.setCitycode(city.getNumber());
			cityDB.closeDB();
			return true;
		} catch (Exception e) {
			cityDB.closeDB();
			Log.d(baidumaptag, e.getMessage());
			return false;
		}

	};

	/**
	 * 获得现在的天气
	 */
	private void getTodayWeatherInfo() {
		String url = WEATHER_URL_1 + cityInfo.getCitycode() + HTML;
		System.out.println(url);
		String weatherResult = connServerForResult(url);
		parseTodayWeaterInfo(weatherResult);

		// 获得天气的状况
		String state = connServerForResult(WEATHER_URL_2 + cityInfo.getCitycode() + HTML);
		// System.out.println("state:" + state);
		try {

			JSONObject json = new JSONObject(state);
			JSONObject weatherinfo = json.getJSONObject("weatherinfo");

			mWeatherState = weatherinfo.getString("weather");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// 获得pm2.5的值
		String pmJson = connServerForResult(WEATHER_URL_3);
		// System.out.println("pmJson:" + state);
		try {

			if (!pmJson.isEmpty()) {
				// pmJson = pmJson.substring(77, pmJson.length() - 1);
				// System.out.println("pmJson:" + pmJson);
				// mPM2_5
				JSONArray array = new JSONArray(pmJson);

				boolean hasPmInfo = false;
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					String city = json.getString("city");
					if (city.equals(cityInfo.getCityname())) {
						mPM2_5 = json.getString("pm2_5");
						quality = json.getString("quality");
						hasPmInfo = true;
						break;
					}
				}

				if (!hasPmInfo) {
					mPM2_5 = null;
					quality = null;
				}

				// JSONObject json = array.getJSONObject(array.length() - 1);
				//
				// mPM2_5 = json.getString("pm2_5");
				// quality = json.getString("quality");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		mWeatherInfo.setQuality(quality);
		mWeatherInfo.setPm2_5(mPM2_5);
		mWeatherInfo.setWeatherState(mWeatherState);
		mWeatherInfo.setDay(DateUtil.getDay());
		mWeatherInfo.setDate(DateUtil.getTodaystr("yyyy/MM/dd"));
		Lunar lunar = new Lunar(DateUtil.getNow());
		mWeatherInfo.setLunar(lunar.toString());

	}

	/**
	 * 从网站上获得天气的json数据。
	 * 
	 * @param url
	 * @return
	 */
	private String connServerForResult(String url) {
		HttpGet httpRequest = new HttpGet(url);
		String strResult = "";
		if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					strResult = EntityUtils.toString(httpResponse.getEntity(), "utf-8");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return strResult;
	}

	/**
	 * 将取到的json数据转换为WeatherInfo的实例数据。 用到了Google的json库。
	 * 
	 * @param result
	 */
	private void parseTodayWeaterInfo(String result) {

		// System.out.println("parseTodayWeaterInfo:" + result);
		if (!TextUtils.isEmpty(result) && !result.contains("页面不存在")) {

			mGson = new Gson();
			TodayWeather today = mGson.fromJson(result, TodayWeather.class);

			mWeatherInfo = today.getWeatherInfo();
			// System.out.println("mWheatherInfo:" + mWeatherInfo);
		}
	}

	/**
	 * 将取到的json数据转换为WeatherInfo2的实例数据。 用到了Google的json库。
	 * 
	 * @param result
	 */
	private void parseHistoryWeaterInfos(String result) {

		// System.out.println("parseTodayWeaterInfo:" + result);
		if (!TextUtils.isEmpty(result) && !result.contains("error")) {

			JSONArray array;
			mGson = new Gson();
			mHistoryInfos = new ArrayList<WeatherInfo2>();

			if (result.contains("error")) {
				JSONObject jo;
				try {
					jo = new JSONObject(result);
					String errmsg = jo.getString("error");
					Log.d(ERROR, errmsg);
				} catch (JSONException e) {
					e.printStackTrace();
					Log.d(ERROR, e.getMessage());
				}
				return;
			}

			try {
				array = new JSONArray(result);

				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					String jostr = jo.toString();

					WeatherInfo2 info2 = mGson.fromJson(jostr, WeatherInfo2.class);
					mHistoryInfos.add(info2);
				}

				Log.d(ALLWEATHERTAG, mHistoryInfos.size() + "");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateFragments() {
		for (WeatherFragment wfg : fragments) {
			wfg.updateWeather(mHistoryInfos, DateUtil.getTodayCalendar());
		}
	}

	/**
	 * 通过主线程更新UI信息。
	 */
	private void updateWeatherInfo() {
		T.showLong(this, "数据接收成功");

		weekTv.setText("今天" + DateUtil.getDay());
		if (null != mWeatherInfo) {
			// 动态改变RelativeLayout的图片
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutParent);
			rl.setBackgroundResource(getBgImg());

			cityTv.setText(mWeatherInfo.getCity());
			temperatureTv.setText(mWeatherInfo.getTemp() + "℃");
			humidityTv.setText(mWeatherInfo.getSD());
			timeTv.setText("今日" + mWeatherInfo.getTime() + "发布");
			windTv.setText(mWeatherInfo.getWD() + " " + mWeatherInfo.getWS());
			weatherImg.setImageResource(mPicUtil.getWeatherIcon(mWeatherState));
			provinceTv.setText(cityInfo.getProvince());

		} else {
			cityTv.setText("北京");
			timeTv.setText(_N_A);
			humidityTv.setText(_N_A);
			pmDataTv.setText(_N_A);
			pmQualityTv.setText(_N_A);
			weekTv.setText(_N_A);
			temperatureTv.setText(_N_A);
			climateTv.setText(_N_A);
			windTv.setText(_N_A);
			T.showLong(this, "获取天气信息失败");
		}

		if (null != mWeatherState) {
			climateTv.setText(mWeatherState);

		} else {
			climateTv.setText(_N_A);
		}

		if (null != mPM2_5 && null != quality) {
			pmDataTv.setText(mPM2_5);
			pmQualityTv.setText(quality);
			if (Integer.parseInt(mPM2_5) >= 100) {
				pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
			} else {
				pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
			}
		} else {
			pmDataTv.setText(_N_A);
			pmQualityTv.setText(_N_A);
			pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
		}

		// 测试图片转换
		// updateWeatherIcon(mWheatherInfo.get);
	}

	/**
	 * 获得背景图片
	 * 
	 * @return
	 */
	public int getBgImg() {

		int weatherRes;
		if ("北京".equals(cityInfo.getProvince()))
			weatherRes = R.drawable.biz_plugin_weather_beijing_yin_bg;
		else {
			weatherRes = R.drawable.biz_plugin_weather_zg_yin_bg;
		}
		String climate = PicUtil.getClimate(mWeatherState);
		if (TextUtils.isEmpty(climate))
			return weatherRes;
		if ("北京".equals(cityInfo.getProvince()) && mPicUtil.getmBeijingBgImg().containsKey(climate)) {
			weatherRes = mPicUtil.getmBeijingBgImg().get(climate);
		} else {
			weatherRes = mPicUtil.getmOtherBgImg().get(climate);
		}

		return weatherRes;
	}

	/**
	 * 百度定位的Listener
	 * 
	 * @author gqq
	 * 
	 */
	class myLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// 下面这个StringBuffer是个测试的方法。
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				/**
				 * 格式化显示地址信息
				 */
				sb.append("\n省：");
				sb.append(location.getProvince());
				sb.append("\n市：");
				sb.append(location.getCity());
				sb.append("\n区/县：");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			Log.i(baidumaptag, sb.toString());
			// T.showLong(getBaseContext(), sb.toString());

			// 刷新天气
			String cityname = location.getDistrict();
			if (null == cityname)
				cityname = location.getCity();
			// 设置城市名
			cityname = cityname.substring(0, cityname.length() - 1);
			cityInfo.setCityname(cityname);
			// 设置省份
			String province = location.getProvince();
			province = province.substring(0, province.length() - 1);
			cityInfo.setProvince(province);
			// cityInfo.setCitycode(location.get())
			Log.d("citycode", location.getCityCode());
			// 更新已存在的文件
			mLocClient.stop();
			// 在子线程中，我们再去数据库中查询citycode，免得主线程太慢。
			updateWeather(true);
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}

	}

	/**
	 * 重写OnKeyDown，两次返回键才退出。
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean result = doExit();
			if (!result)
				return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 如果exitFlag为false，则弹出消息，继续等待。 如果exitFlag为true，直接退出程序
	 * 
	 * @return
	 */
	private boolean doExit() {
		if (!exitFlag) {
			T.showShort(this, "再返回一次退出程序……");
			exitFlag = true;
			mHandler.sendEmptyMessageDelayed(RESUME_EXIT_FALSE, 2000);
			return false;
		}
		return true;
	}

	/**
	 * 测试方法
	 * 
	 * @param v
	 */
	public void onClickProgress(View v) {
		showDialog(2);
		progressDialog.setProgress(0);
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 1; i <= 25; i++) {
					try {
						Thread.sleep(200);
						progressDialog.incrementProgressBy((int) (100 / 25));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 测试方法
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:

			return new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("This is a dialog with some simple text...")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Toast.makeText(getBaseContext(), "OK clicked!", Toast.LENGTH_SHORT)
									.show();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Toast.makeText(getBaseContext(), "Cancel clicked!", Toast.LENGTH_SHORT)
									.show();
						}
					})
					.setMultiChoiceItems(items, itemsChecked,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog, int which,
										boolean isChecked) {
									Toast.makeText(
											getBaseContext(),
											items[which]
													+ (isChecked ? " checked!" : " unchecked!"),
											Toast.LENGTH_SHORT).show();
								}
							}).create();

		case 2:
			progressDialog = new ProgressDialog(this);
			progressDialog.setIcon(R.drawable.ic_launcher);
			progressDialog.setTitle("Downloading files...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Toast.makeText(getBaseContext(), "OK clicked!", Toast.LENGTH_SHORT)
									.show();
						}
					});
			progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Toast.makeText(getBaseContext(), "Cancel clicked!", Toast.LENGTH_SHORT)
									.show();
						}
					});
			return progressDialog;
		}
		return null;
	}

	/**
	 * 测试拷贝数据库
	 */
	@SuppressLint("SdCardPath")
	public void doCopy() {
		try {
			String destPath = "/data/data/" + getPackageName() + "/databases";
			Log.d(CITYTAG, "destPath:" + destPath);
			File f = new File(destPath);
			if (!f.exists()) {
				Log.d(CITYTAG, "make dirs...");
				f.mkdirs();
				f.createNewFile();
				// ---copy the db from the assets folder into
				// the databases folder---
				CopyDB(getBaseContext().getAssets().open("city.db"), new FileOutputStream(destPath
						+ "/MyDB"));
			}
			Log.d(CITYTAG, "copy ended:" + destPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ---ge
	}

	public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
		// ---copy 1K bytes at a time---
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		inputStream.close();
		outputStream.close();
	}

}
