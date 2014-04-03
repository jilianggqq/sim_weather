package com.gqq.androidpku;

import java.util.*;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.gqq.adapter.*;
import com.gqq.bean.*;
import com.gqq.util.*;
import com.gqq.view.*;
import com.gqq.view.BladeView.OnItemClickListener;

public class SelectCity extends Activity implements View.OnClickListener {

	private static final int COPY_DB_SUCCESS = 10;
	protected static final int QUERY_CITY_FINISH = 12;
	private MySectionIndexer mIndexer;

	EditText edit_search;

	private List<City> cityList = new ArrayList<City>();

	private List<City> newCityList = new ArrayList<City>();
	public static String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/test/";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		/**
		 * 多线程返回后的处理
		 */
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case QUERY_CITY_FINISH:

				if (mAdapter == null) {
					displayAllCities();

				} else if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
				// edit_search.setSelected(false);
				// edit_search.clearFocus();
				break;
			case COPY_DB_SUCCESS:
				requestData();
				break;
			default:
				break;
			}
		}

	};
	private CityListAdapter mAdapter;
	// 搜索时PinnedHeaderListView需要绑定的Adapter
	private CityListSimAdapter simAdapter;
	private static final String ALL_CHARACTER = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	protected static final String TAG = "SelectCityNew";

	private String[] sections = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
			"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private int[] counts;
	private PinnedHeaderListView mListView;
	private ImageView mBackBtn, mDelete, mSearch;
	private BladeView mLetterListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 显示初始化时要加载的xml页面
		setContentView(R.layout.select_city);

		findView();

		// 搜索控件
		edit_search = (EditText) findViewById(R.id.citySelect);
		edit_search.addTextChangedListener(new TextWatcher_Enum());
		// edit_search.clearFocus();


		// 返回控件
		mBackBtn = (ImageView) findViewById(R.id.title_back);
		mBackBtn.setOnClickListener(this);

		mDelete = (ImageView) findViewById(R.id.imgDelete);
		mDelete.setOnClickListener(this);

		// 把焦点定位在其它控件上，这样焦点会很自然的离开EditText
		mSearch = (ImageView) findViewById(R.id.imgSearch);
		mSearch.setFocusable(true);
		mSearch.setFocusableInTouchMode(true);
		mSearch.requestFocus();
		mSearch.requestFocusFromTouch();

		requestData();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void requestData() {

		Runnable task = new Runnable() {

			@Override
			public void run() {

				CityDB cityDB = new CityDB(SelectCity.this, "city.db");

				List<City> all = cityDB.getAllCity(); // 全部城市

				cityDB.closeDB();

				if (all != null) {

					Collections.sort(all); // 排序

					cityList.addAll(all);
					// 初始化每个字母有多少个item
					counts = new int[sections.length];

					// counts[0] = hot.size(); // 热门城市 个数

					for (City city : all) { // 计算全部城市
						String firstCharacter = city.getSortKey();
						int index = ALL_CHARACTER.indexOf(firstCharacter);
						counts[index]++;
					}

					handler.sendEmptyMessage(QUERY_CITY_FINISH);
				}
			}
		};

		new Thread(task).start();
	}

	/**
	 * 找到页面要显示的控件，并绑定事件。
	 */
	private void findView() {

		mListView = (PinnedHeaderListView) findViewById(R.id.mListView);

		mLetterListView = (BladeView) findViewById(R.id.mLetterListView);

		// 绑定mListView的某一行的单击事件
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				// 通过阅读源码可以看出，arg0正好是OnItemClickListener的宿主，因此可以强制转换获得mListView对象
				List<City> tmpCities = ((CityAdapter) (((ListView) arg0).getAdapter()))
						.getCityList();
				// 将取得的信息存入到xml文件中。
				// SharedPreferences appPrefs =
				// getSharedPreferences("selected_info", MODE_PRIVATE);
				// SharedPreferences.Editor prefsEditor = appPrefs.edit();
				// prefsEditor.clear();
				// prefsEditor.putString("city", tmpCities.get(arg2).getCity());
				// prefsEditor.putString("citycode",
				// tmpCities.get(arg2).getNumber());
				// prefsEditor.putString("province",
				// tmpCities.get(arg2).getProvince());
				// prefsEditor.commit();

				Intent intent = new Intent();
				intent.putExtra("city", tmpCities.get(arg2).getCity());
				intent.putExtra("citycode", tmpCities.get(arg2).getNumber());
				intent.putExtra("province", tmpCities.get(arg2).getProvince());
				// 通过Intent对象返回结果，调用setResult方法
				setResult(2, intent);

				// Log.i(TAG, "save completed!");
				SelectCity.this.finish();
			}

		});

		mLetterListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(String s) {
				Log.i(TAG, "item clicked");
				if (s != null) {

					int section = ALL_CHARACTER.indexOf(s);

					int position = mIndexer.getPositionForSection(section);

					// Log.i(TAG, "s:" + s + ",section:" + section +
					// ",position:" + position);

					if (position != -1) {
						mListView.setSelection(position);
					} else {

					}
				} else {
					Log.i(TAG, "s是空的");
				}
			}
		});
	}

	// 当editetext变化时调用的方法，来判断所输入是否包含在所属数据中
	@SuppressLint("DefaultLocale")
	private List<City> getNewData(String input_info) {

		for (City cityString : cityList) {
			if (cityString.getAllFristPY().toLowerCase().contains((input_info.toLowerCase()))
					|| cityString.getAllPY().toLowerCase().contains(input_info.toLowerCase())
					|| cityString.getCity().contains(input_info)) {
				newCityList.add(cityString);
			}
		}
		return newCityList;
	}

	// TextWatcher接口
	class TextWatcher_Enum implements TextWatcher {

		// 文字变化前
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		// 文字变化时
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			newCityList.clear();
			if (edit_search.getText() != null) {
				// Log.d(TAG, "search.getText()" + edit_search.getText());
				String input_info = edit_search.getText().toString();
				Log.d("DisplayCity", "input_info[" + input_info + "]");
				// input_info = "";
				if (!isNullOrEmpty(input_info)) {
					Log.d("DisplayCity", "display selected cities");
					newCityList = getNewData(input_info);

					simAdapter = new CityListSimAdapter(newCityList, getApplicationContext());
					mListView.setAdapter(simAdapter);
					mListView.setPinnedHeaderView(null);

					// 取消边上的字母导航
					mLetterListView.setVisibility(View.GONE);
					// 显示右侧的那个叉。
					mDelete.setVisibility(View.VISIBLE);
				} else {
					Log.d("DisplayCity", "displayAllCities");
					displayAllCities();
				}
			} else {
				Log.d("DisplayCity", "textnull displayAllCities");
				displayAllCities();
			}
		}

		private boolean isNullOrEmpty(String str) {
			if (null == str || "".equals(str.trim()))
				return true;
			return false;
		}

		// 文字变化后
		@Override
		public void afterTextChanged(Editable s) {

		}

	}

	/**
	 * 显示所有的城市，可以使用拼音搜索
	 */
	private void displayAllCities() {
		mIndexer = new MySectionIndexer(sections, counts);

		mAdapter = new CityListAdapter(cityList, mIndexer, getApplicationContext());
		mListView.setAdapter(mAdapter);

		mListView.setOnScrollListener(mAdapter);

		// list_group_item为顶部的固定头部。
		mListView.setPinnedHeaderView(LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.list_group_item, mListView, false));
		mLetterListView.setVisibility(View.VISIBLE);
		// 隐藏右侧的那个叉。
		mDelete.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.imgDelete:
			edit_search.setText("");
			displayAllCities();
			break;

		default:
			break;
		}
	}

}
