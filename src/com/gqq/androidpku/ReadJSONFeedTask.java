package com.gqq.androidpku;

import org.json.*;

import com.gqq.util.*;

import android.os.*;

/**
 * 测试json的类。不必在意
 * 
 * @author gqq
 * 
 */
public class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... urls) {
		return NetUtil.readJSONFeed(urls[0]);
	}

	protected void onPostExecute(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			// TextView tv = (TextView) findViewById(R.id.results);

			// System.out.println(jsonArray.length());
			// ---print out the content of the json feed---
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
