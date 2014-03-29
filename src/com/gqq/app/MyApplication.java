package com.gqq.app;

import java.io.*;

import org.w3c.dom.ls.*;

import com.gqq.util.*;

import android.R.*;
import android.app.*;
import android.os.*;
import android.util.*;

public class MyApplication extends Application {

	private final String AppTag = "MyApp";

	private static MyApplication myApplication;

	private CityDB cityDB;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(AppTag, "oncreate");
		myApplication = this;

		initCityDB();
	}

	private CityDB initCityDB() {
		String path = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator
				+ "com.gqq.androidpku" + File.separator + "databases" + File.separator
				+ CityDB.CITY_DB_NAME;
		Log.i(AppTag, path);
		File db = new File(path);

		if (!db.exists()) {
//			if (db.exists())
//				db.delete();
			Log.i(AppTag, "file is not exsits");
			try {
				db.getParentFile().mkdirs();
				db.createNewFile();
				InputStream is = getAssets().open("city.db");
				FileOutputStream fos = new FileOutputStream(db, false);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
				Log.i(AppTag, "city.db已经被成功拷贝。地址为：" + path);
			} catch (Exception e) {
				Log.i(AppTag, e.getMessage());
				e.printStackTrace();
				T.showLong(myApplication, e.getMessage());
				System.exit(0);
			}

		} else {
			Log.i(AppTag, "file has already exsited");
		}
		return new CityDB(this, path);
	}

	public static MyApplication getInstance() {
		return myApplication;
	}

}
