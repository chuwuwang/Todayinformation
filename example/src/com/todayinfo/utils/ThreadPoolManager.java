package com.todayinfo.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理工具类
 * 
 * @author zhou.ni 2015年4月29日
 */
public class ThreadPoolManager {
	private ExecutorService service;

	private ThreadPoolManager() {
		int num = Runtime.getRuntime().availableProcessors();
		// service = Executors.newFixedThreadPool(num * 3);
		service = Executors.newCachedThreadPool();
	}

	private static final ThreadPoolManager manager = new ThreadPoolManager();

	public static ThreadPoolManager getInstance() {
		return manager;
	}

	public void executeTask(Runnable runnable) {
		service.execute(runnable);
	}

}
