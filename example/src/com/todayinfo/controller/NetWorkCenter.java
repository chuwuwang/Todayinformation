package com.todayinfo.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.todayinfo.ui.api.RetryNetwork;

/**
 * 网络状态监听中心
 * @author longtao.li
 *
 */
public class NetWorkCenter {
	
	public  ConnectionChangeReceiver mConnectionChangeReceiver;
	
	private static RetryNetwork mRetry;
	
	/**
	 * 判断当前网络是否是wifi网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否是3G网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否有网络链接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	
	
	public  ConnectionChangeReceiver getConnectionChangeReceiver(){
	
		if( mConnectionChangeReceiver == null ){
			mConnectionChangeReceiver = new ConnectionChangeReceiver();
		}
		return mConnectionChangeReceiver;
		
	}
	
	
	
	public  void setRetryNetwork(RetryNetwork r){
		mRetry = r;
	}
	public void removeRetry(){
		mRetry = null;
	}
	
	public static class ConnectionChangeReceiver extends BroadcastReceiver {
		public boolean isNetworkConnected = true;

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager
					.getActiveNetworkInfo();
			// NetworkInfo mobNetInfo =
			// connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (activeNetInfo != null) {

				if (!isNetworkConnected) {
					if (mRetry != null) {
						mRetry.retry();
					}
				}

				isNetworkConnected = true;
			} else {
				isNetworkConnected = false;
				if(mRetry != null){
					mRetry.netError();
				}
				
			}
		}
	}
	
	
	

}
