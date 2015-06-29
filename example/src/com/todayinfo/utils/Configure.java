package com.todayinfo.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
/**
 * 一些常用的系统配置信息 获取自定义的配置信息 类
 *
 */
public class Configure{

	public static final int NOTE1 = 1;
	
	public static int screenHeight=0;
	public static int screenWidth=0;
	public static float screenDensity=0;
	public static int densityDpi = 0;
	public static int version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
	
	public static void init(Activity context) {
			if(screenDensity==0||screenWidth==0||screenHeight==0){
				DisplayMetrics dm = new DisplayMetrics();
				context.getWindowManager().getDefaultDisplay().getMetrics(dm);
				Configure.screenDensity = dm.density;
				Configure.screenHeight = dm.heightPixels;
				Configure.screenWidth = dm.widthPixels;
				Configure.densityDpi = dm.densityDpi;
			}
			Log.i("SCREEN CONFIG", "screenHeight:"+screenHeight+";screenWidth:"+screenWidth
					+";screenDensity:"+screenDensity+";densityDpi:"+densityDpi);
	}
    
}