package com.gqq.util;

import java.text.*;
import java.util.*;

import android.R.*;

public final class DateUtil {

	static Calendar now;
	static Date today;

	static {
		// 取得当前的时间
		now = Calendar.getInstance();
		// 取得今天的日期，时间从0点开始算起。
		today = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE)).getTime();
	}

	/**
	 * 返回当前时间。
	 * 
	 * @return
	 */
	public static Calendar getNow() {
		return now;
	}

	/**
	 * 变为Unix时间戳
	 * 
	 * @param cd
	 * @return
	 */
	public static long toUnixTime(Date dt) {

		return dt.getTime() / 1000;
	}

	/**
	 * 变为Unix时间戳
	 * 
	 * @param dtStr
	 *            ：必须是yyyy-MM-dd格式的字符串。
	 * @return
	 */
	public static long toUnixTime(String dtStr) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = df.parse(String.valueOf(dtStr));
			return toUnixTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * 将具体的时间格式变为Unix时间戳
	 * 
	 * @param dtStr
	 *            ：必须是yyyy-MM-dd HH:mm:ss格式的字符串。
	 * @return
	 */
	public static long toUnixTimeDetail(String dtStr) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = df.parse(String.valueOf(dtStr));
			return toUnixTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * 把Unix时间戳转化为java日期
	 * 
	 * @param uTime
	 * @return
	 */
	public static Date fromUnixTime(long uTime) {
		return new java.util.Date(uTime * 1000);
	}

	/**
	 * 把Unix时间戳转化为java日期，并格式化为yyyy-MM-dd HH:mm:ss的形式。
	 * 
	 * @param uTime
	 * @return
	 */
	public static String fromUnixTimeStr(long day) {
		return fromUnixTimeStr(day, "yyyy-MM-dd");
	}

	/**
	 * 把Unix时间戳转化为java日期，并格式化为yyyy-MM-dd HH:mm:ss的形式。
	 * 
	 * @param uTime
	 * @return
	 */
	public static String fromUnixTimeStr(long day, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(fromUnixTime(day));

	}

	/**
	 * 以Unixtime的形式返回今天日期
	 * 
	 * @return
	 */
	public static long getTodayLong() {
		return toUnixTime(today);
	}

	/**
	 * 以日期的形式返回今天日期
	 * 
	 * @return
	 */
	public static Date getToday() {
		return today;
	}

	/**
	 * 以字符串的形式返回今天日期
	 * 
	 * @return
	 */
	public static String getTodaystr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(today);
	}

	/**
	 * 获得今天是星期几
	 * 
	 * @return
	 */
	public static String getDay() {
		return getDay(today.getDay());
	}

	private static String getDay(int day) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		return dayNames[day];
	}

	public static String getAfterDay(int i) {
		Calendar after = Calendar.getInstance();
		// int a1 = after.get(Calendar.DAY_OF_WEEK);
		after.add(Calendar.DATE, i);
		int a2 = after.get(Calendar.DAY_OF_WEEK);
		return getDay(a2 - 1);
	}

	public static Calendar getTodayCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		return c;
	}


}
