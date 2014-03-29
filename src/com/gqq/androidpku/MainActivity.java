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
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.google.gson.*;
import com.gqq.bean.*;
import com.gqq.util.*;

public class MainActivity extends Activity implements View.OnClickListener {
	String tag = "生命周期";
	CharSequence[] items = { "Google", "Apple", "Microsoft" };
	boolean[] itemsChecked = new boolean[items.length];
	ProgressDialog progressDialog;

	private final String WEATHER_URL_1 = "http://www.weather.com.cn/data/sk/";
	// 利用该Url可以获得天气状况。
	private final String WEATHER_URL_2 = "http://www.weather.com.cn/data/cityinfo/";
	private final String WEATHER_URL_3 = "http://gqqapp.sinaapp.com/pm.php";
	private final String BEIJING_CITYCODE = "101010100";
	private final String HTML = ".html";
	private final String _N_A = "N/A";
	private final String CITYTAG = "Cities";

	private WeatherInfo mWeatherInfo;
	private String mWeatherState;
	private String mPM2_5;
	private String quality;
	private String citycode;
	private String province = "北京";

	private Gson mGson;
	private Map<String, Integer> mWeatherIcon;// 天气图标
	private static final int GET_WEATHER_RESULT = 3;
	private static final int RESUME_EXIT_FALSE = 99;
	private static final int REQUESTCODE = 1;

	private boolean exitFlag = false;
	Animation operatingAnim;
	LinearInterpolator lin;

	// 定义一个Handler，用于线程同步。
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_WEATHER_RESULT:
				updateWeatherInfo();
				// 让图标停止旋转
				resumeUpdateIcon();

				break;

			case RESUME_EXIT_FALSE:
				exitFlag = false;
				break;
			default:
				break;
			}
		}

	};

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
		// 旋转更新按钮
		if (operatingAnim != null) {
			findViewById(R.id.footer).setVisibility(View.VISIBLE);
			// mUpdate.setImageResource(R.drawable.base_loading_large_icon);
			mUpdate.startAnimation(operatingAnim);
			imgRefresh.startAnimation(operatingAnim);
		}
	}

	/**
	 * 初始化图片字典
	 */
	private void initWeatherIconMap() {
		mWeatherIcon = new HashMap<String, Integer>();
		mWeatherIcon.put("暴雪", R.drawable.biz_plugin_weather_baoxue);
		mWeatherIcon.put("暴雨", R.drawable.biz_plugin_weather_baoyu);
		mWeatherIcon.put("大暴雨", R.drawable.biz_plugin_weather_dabaoyu);
		mWeatherIcon.put("大雪", R.drawable.biz_plugin_weather_daxue);
		mWeatherIcon.put("大雨", R.drawable.biz_plugin_weather_dayu);

		mWeatherIcon.put("多云", R.drawable.biz_plugin_weather_duoyun);
		mWeatherIcon.put("雷阵雨", R.drawable.biz_plugin_weather_leizhenyu);
		mWeatherIcon.put("雷阵雨冰雹", R.drawable.biz_plugin_weather_leizhenyubingbao);
		mWeatherIcon.put("晴", R.drawable.biz_plugin_weather_qing);
		mWeatherIcon.put("沙尘暴", R.drawable.biz_plugin_weather_shachenbao);

		mWeatherIcon.put("特大暴雨", R.drawable.biz_plugin_weather_tedabaoyu);
		mWeatherIcon.put("雾", R.drawable.biz_plugin_weather_wu);
		mWeatherIcon.put("小雪", R.drawable.biz_plugin_weather_xiaoxue);
		mWeatherIcon.put("小雨", R.drawable.biz_plugin_weather_xiaoyu);
		mWeatherIcon.put("阴", R.drawable.biz_plugin_weather_yin);

		mWeatherIcon.put("雨夹雪", R.drawable.biz_plugin_weather_yujiaxue);
		mWeatherIcon.put("阵雪", R.drawable.biz_plugin_weather_zhenxue);
		mWeatherIcon.put("阵雨", R.drawable.biz_plugin_weather_zhenyu);
		mWeatherIcon.put("中雪", R.drawable.biz_plugin_weather_zhongxue);
		mWeatherIcon.put("中雨", R.drawable.biz_plugin_weather_zhongyu);
	}

	// http://www.weather.cn
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWeatherIconMap();
		requestWindowFeature(Window.FEATURE_NO_TITLE);//
		setContentView(R.layout.activity_main);
		init();

		mCityManagerBtn = (ImageView) findViewById(R.id.title_city_manager);
		mCityManagerBtn.setOnClickListener(this);

		mUpdate = (ImageView) findViewById(R.id.title_update);
		imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
		mUpdate.setOnClickListener(this);

		// 设置旋转变量
		operatingAnim = AnimationUtils.loadAnimation(this, R.anim.update_anim);
		lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);

		// 第一次更新天气
		firstUpdateWeather();
	}

	private void firstUpdateWeather() {
		// TODO Auto-generated method stub
		Log.d(tag, "onstart");
		SharedPreferences appPrefs = getSharedPreferences("selected_info", MODE_PRIVATE);
		citycode = appPrefs.getString("citycode", "");
		province = appPrefs.getString("province", "北京");
		if (null == citycode || "".equals(citycode))
			citycode = BEIJING_CITYCODE;

		Log.d("CityCode", citycode);
		updateWeather();
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
		Log.d(tag, "In the onResume() event");
	}

	@Override
	protected void onPause() {
		super.onPause();
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

			updateWeather();
			break;
		default:
			break;
		}
	}

	private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv,
			climateTv, windTv, provinceTv;
	private ImageView weatherImg, pmImg;

	private ImageView mCityManagerBtn, mUpdate, imgRefresh;

	/**
	 * 如果从intent返回了数值，和文件中比对，看是否需要更新天气。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 2) {
			if (requestCode == REQUESTCODE) {
				// int request = data.getIntExtra("three", 0);
				// 接收返回数据
				// 接受到返回数据之后，和文件中的比对，如果citycode相同，则返回；如果不同，则刷新。
				String resCity = data.getStringExtra("city");

				SharedPreferences appPrefs = getSharedPreferences("selected_info", MODE_PRIVATE);
				String filCity = appPrefs.getString("city", "");

				if (!filCity.equals(resCity)) {
					SharedPreferences.Editor prefsEditor = appPrefs.edit();
					prefsEditor.clear();
					prefsEditor.putString("city", data.getStringExtra("city"));
					prefsEditor.putString("citycode", data.getStringExtra("citycode"));
					prefsEditor.putString("province", data.getStringExtra("province"));
					prefsEditor.commit();
					citycode = data.getStringExtra("citycode");
					province = data.getStringExtra("province");
					updateWeather();
				}
			}
		}
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
	private void updateWeather() {

		rotateUpdateIcon();

		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			T.showLong(this, "网络不给力，请稍后。。。");
			return;
		}
		T.showShort(this, "正在更新天气");
		new Thread() {
			public void run() {
				super.run();
				// 测试转圈的代码，
				// try {
				// this.sleep(1000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				getTodayWeatherInfo();
				mHandler.sendEmptyMessage(GET_WEATHER_RESULT);
			};
		}.start();

	}

	/**
	 * 获得现在的天气
	 */
	private void getTodayWeatherInfo() {
		String url = WEATHER_URL_1 + citycode + HTML;
		System.out.println(url);
		String weatherResult = connServerForResult(url);
		parseTodayWeaterInfo(weatherResult);

		// 获得天气的状况
		String state = connServerForResult(WEATHER_URL_2 + citycode + HTML);
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
				JSONObject json = array.getJSONObject(array.length() - 1);

				mPM2_5 = json.getString("pm2_5");
				quality = json.getString("quality");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
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
	 * 通过主线程更新UI信息。
	 */
	private void updateWeatherInfo() {
		T.showLong(this, "数据接收成功");

		weekTv.setText("今天" + DateUtil.getDay());
		if (null != mWeatherInfo) {
			cityTv.setText(mWeatherInfo.getCity());
			temperatureTv.setText(mWeatherInfo.getTemp() + "℃");
			humidityTv.setText(mWeatherInfo.getSD());
			timeTv.setText("今日" + mWeatherInfo.getTime() + "发布");
			windTv.setText(mWeatherInfo.getWD() + " " + mWeatherInfo.getWS());
			weatherImg.setImageResource(getWeatherIcon());
			provinceTv.setText(province);

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
		} else {
			pmDataTv.setText(_N_A);
			pmQualityTv.setText(_N_A);
		}

		// 测试图片转换
		// updateWeatherIcon(mWheatherInfo.get);
	}

	/**
	 * 获得图片图标。
	 * 
	 * @return
	 */
	public int getWeatherIcon() {
		String climate = mWeatherState;
		// climate = "小雨";
		int weatherRes = R.drawable.biz_plugin_weather_qing;
		if (TextUtils.isEmpty(climate))
			return weatherRes;
		String[] strs = { "晴", "晴" };
		if (climate.contains("转")) {// 天气带转字，取前面那部分
			strs = climate.split("转");
			climate = strs[0];
			if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
				strs = climate.split("到");
				climate = strs[1];
			}
		}
		if (mWeatherIcon.containsKey(climate)) {
			weatherRes = mWeatherIcon.get(climate);
		}
		return weatherRes;
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
