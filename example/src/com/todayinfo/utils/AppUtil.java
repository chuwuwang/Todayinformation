package com.todayinfo.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * 手机本身的一些操作，包括：是否连接网络，屏幕分辨率等；
 * 
 * @author zhou.ni 2015年4月25日
 */
public class AppUtil {
	
	/** 是否开启debug模式 */
	public static boolean debug = true; 
	
	/**
	 * 设置是否开启debug模式
	 * */
	public static void setDebug(boolean debug){
		AppUtil.debug = debug;
	}
	
	/**
	 * 判断网络是否已经连接
	 * @param context 上下文
	 * @return true 已经连接网络， false 网络连接失效
	 * */
	public static boolean isNetworkAvailable(Context context){
		try{
			ConnectivityManager cm = (ConnectivityManager)
					context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm != null){
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if(ni!=null && ni.isConnected()){
					if(ni.getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		} catch (Exception e){
			return false;
		}
		return false;
	}
	
	/**
	 * 判断当前网络是否是WIFI
	 * */
	public static boolean isWIFI(Context context){
		try{
			ConnectivityManager cm = (ConnectivityManager) 
					context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm != null){
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if(ni!=null && ni.getType() == ConnectivityManager.TYPE_WIFI){
					return true;
				}
			}
		} catch (Exception e){
			return false;
		}
		return false;
	}
	
	/**
	 * 判断当前网络是否是GPRS
	 * */
	public static boolean is3G(Context context){
		try{
			ConnectivityManager cm = 
					(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm != null){
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if(ni!=null && ni.getType() == ConnectivityManager.TYPE_MOBILE){
					return true;
				}
			}
		} catch (Exception e){
			
		}
		return false;
	}
	
	/**
	 * 获取手机分辨率--W
	 * */
	public static int getPhoneHW(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int disW = dm.widthPixels;
		return disW;
	}
	
	
	/**
	 * 获取手机分辨率--H
	 * */
	public static int getPhoneWH(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int disH = dm.heightPixels;
		return disH;
	}
	
	/**
	 * 获取CPU数量
	 * */
	public static int cpuNums(){
		int nums = 1;
		try{
			File file = new File("/sys/devices/system/cpu/");
			File[] files = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File arg0) {
					if(Pattern.matches("cpu[0-9]", arg0.getName())){
						return true;
					}
					return false;
				}
			});
			nums = files.length;
		} catch (Exception e){
			e.printStackTrace();
		}
		return nums;
	}
	
	/**
	 * 获取当前sdk版本
	 * @return
	 */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e){
        	e.printStackTrace();
        }
        return version;
    }
	
}
