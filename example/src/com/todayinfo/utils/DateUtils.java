package com.todayinfo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 * 
 * @author longtao.li
 * 
 */
public class DateUtils {

	/**
	 * 获取格林威治时间(unix时间戳 即1970年至今的秒数)
	 * 
	 */
	public static long getGMTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Etc/Greenwich"));
		String format = sdf.format(new Date());
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date gmDate = null;
		try {
			gmDate = sdf1.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return gmDate.getTime() / 1000;
	}

	/**
	 * 获取格林威治时间(unix时间戳 即1970年至今的秒数)
	 * 
	 */
	public static long _getGMTime() {
		int round = (int) (System.currentTimeMillis() / 1000);
		return round;
	}

	/**
	 * 获取时间时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		String time = null;
		long round = System.currentTimeMillis() / 1000;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(round * 1000);
		String[] split = date.split("\\s");
		if (split.length > 1) {
			time = split[1];
		}
		return time;
	}

	/**
	 * 得到昨天的日期
	 * 
	 * @return
	 */
	public static String getYestoryDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String yestoday = sdf.format(calendar.getTime());
		return yestoday;
	}
	
	/**
	 * 得到明天的日期
	 * 
	 * @return
	 */
	public static String getTomorrowDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrow = sdf.format(calendar.getTime());
		return tomorrow;
	}

	/**
	 * 得到今天的日期
	 * 
	 * @return
	 */
	public static String getTodayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		return date;
	}

	/**
	 * 时间戳转化为时间格式
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String timeStampToStr(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(timeStamp * 1000);
		return date;
	}

	/**
	 * 时间戳转化为时间格式
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String timeStampToStr1(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String date = sdf.format(timeStamp * 1000);
		return date;
	}
	
	/**
	 * 时间戳转化为时间(几点)
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String timeStampToTime(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String date = sdf.format(timeStamp * 1000);
		return date;
	}
	
	/**
	 * 将日期格式转化为时间戳(秒数)
	 * @param time
	 * @return
	 */
	public static long getStringToDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime()/1000;
	}
	
	/**
	 * 将日期格式转化为时间戳(秒数)
	 * @param time
	 * @return
	 */
	public static long getString2Date(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime()/1000;
	}
	
	/**
	 * 判断是否大于当前时间
	 * @param time
	 * @return
	 */
	public static boolean judgeCurrTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = sdf.parse(time);
			long t = date.getTime();
			long round = System.currentTimeMillis();
			if ( t-round > 0 ) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
	
	/**
	 * 判断是否大于当前时间
	 * @param time
	 * @return
	 */
	public static boolean judgeCurrTime(long time) {
		long round = System.currentTimeMillis();
		if (time - round > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 *	比较后面的时间是否大于前面的时间
	 * @param time
	 * @return
	 */
	public static boolean judgeTime2Time(String time1,  String time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			long l1 = date1.getTime()/1000;
			long l2 = date2.getTime()/1000;
			if ( l2-l1 > 0 ) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	

	/**
	 * 得到日期 yyyy-MM-dd
	 * 
	 * @param timeStamp
	 *            时间戳
	 * @return
	 */
	public static String formatDate(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(timeStamp * 1000);
		return date;
	}

	/**
	 * 得到时间 HH:mm:ss
	 * 
	 * @param timeStamp
	 *            时间戳
	 * @return
	 */
	public static String getTime(long timeStamp) {
		String time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(timeStamp * 1000);
		String[] split = date.split("\\s");
		if (split.length > 1) {
			time = split[1];
		}
		return time;
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String convertTimeToFormat(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = curTime - timeStamp;

		if (time < 60 && time >= 0) {
			return "刚刚";
		} else if (time >= 60 && time < 3600) {
			return time / 60 + "分钟前";
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + "小时前";
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			return time / 3600 / 24 + "天前";
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 + "个月前";
		} else if (time >= 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 / 12 + "年前";
		} else {
			return "刚刚";
		}
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，(多少分钟)
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String timeStampToFormat(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = curTime - timeStamp;
		return time / 60 + "";
	}

	/**
	 * 获得当前时间差
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static int nowCurrentTime(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = timeStamp - curTime;
		return (int) time;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String nowCurrentPoint(boolean flag) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String date = sdf.format(System.currentTimeMillis());
		String[] split = date.split(":");
		String hour = null;
		String minute = null;
		if ( flag ) {
			if (split.length > 1) {
				hour = split[0];
				return hour;
			}
		} else {
			if (split.length > 1) {
				minute = split[1];
				return minute;
			}
		}
		return null;
	}
	
	/**
	 * 将标准时间格式HH:mm:ss化为当前的时间差值
	 * 
	 * @param str
	 * @return
	 */
	public static String StandardFormatStr(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = sdf.parse(str);
			long timeStamp = d.getTime();

			long curTime = System.currentTimeMillis() / (long) 1000;
			long time = curTime - timeStamp / 1000;
			return time / 60 + "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final int WEEKDAYS = 7;

	public static String[] WEEK = { "周日", "周一", "周二", "周三",
			"周四", "周五", "周六" };

	/**
	 * 日期变量转成对应的星期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String DateToWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) {
			return null;
		}

		return WEEK[dayIndex - 1];
	}
	

	/**
	 * 日期变量转成对应的星期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String DateToWeek(long timeStamp) {
		Date date = new Date(timeStamp);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) {
			return null;
		}
		return WEEK[dayIndex - 1];
	}

	public static String convertGMTToLoacale(String gmt){
		String s1 = gmt.substring(0, 10);
		String s2 = gmt.substring(11, 19);
		return s1 + " " + s2;
	}
	
}
