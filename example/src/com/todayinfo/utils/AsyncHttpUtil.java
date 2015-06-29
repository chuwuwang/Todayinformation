package com.todayinfo.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AsyncHttpUtil {
	
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象

	static {
		client.setTimeout(10000); // 设置链接超时，如果不设置，默认为10s
	}

	/**
	 *  get 用一个完整url获取一个string对象
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, AsyncHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * get url里面带参数
	 * @param urlString
	 * @param params
	 * @param res
	 */
	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	/**
	 * get 不带参数，获取json对象或者数组
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, JsonHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * get 带参数，获取json对象或者数组
	 * @param urlString
	 * @param params
	 * @param res
	 */
	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	/**
	 * get 下载数据使用，会返回byte数据
	 * @param uString
	 * @param bHandler
	 */
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
	
	/**
	 * post 带参数，获取json对象或者数组
	 * @param url
	 * @param params
	 * @param res
	 */
	public static void post(String url, RequestParams params,
			JsonHttpResponseHandler res) {
		client.post(url, params, res);
	}
	
	/**
	 * post url里面带参数
	 * @param urlString
	 * @param params
	 * @param res
	 */
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}
	
}
