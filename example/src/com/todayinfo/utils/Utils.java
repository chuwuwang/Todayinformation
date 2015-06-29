package com.todayinfo.utils;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

/**
 * 封装常用方法的工具类
 * 
 * @author longtao.li
 * 
 */
public class Utils {

	private static final String PICASSO_CACHE = "picasso-cache";

	/**
	 * 判断app是否在后台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackgroundRunning(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static File createDefaultCacheDir(Context context) {
		File cache = new File(context.getApplicationContext().getCacheDir(),PICASSO_CACHE);
		if (!cache.exists()) {
			cache.mkdirs();
		}
		return cache;
	}

}
