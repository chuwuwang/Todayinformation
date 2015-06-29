package com.todayinfo.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.todayinfo.event.EventCloseFM;
import com.todayinfo.event.EventOpenFM;
import com.todayinfo.model.Info;
import com.todayinfo.ui.api.DataTask;
import com.todayinfo.ui.api.IBackEventStrategy;
import com.todayinfo.ui.api.PwdErrorListener;
import com.todayinfo.ui.api.RetryNetwork;
import com.todayinfo.ui.api.Task;
import com.todayinfo.utils.LogUtil;
import com.todayinfo.utils.ThreadPoolManager;

import de.greenrobot.event.EventBus;

/**
 * 所有的fragment都继承于它
 * 
 * @author zhou.ni 2015年2月1日
 */
public abstract class SuperFragment extends Fragment implements RetryNetwork, PwdErrorListener, OnTouchListener, IBackEventStrategy {
	private static final String TAG = "SuperFragment";
	
	ThreadPoolManager mThreadPoolManager;
	
	protected Context mContext;
	
	protected Activity mActivity;
	
	protected ProgressDialog progress;
	
	protected Info mInfo;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		mActivity = getActivity();
		mThreadPoolManager = ThreadPoolManager.getInstance();
		progress = getProgressDialog("正在加载,请稍后...");
		progress.setCancelable(true);

		obtainInfo();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			obtainInfo();
		} else {
			
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.setOnTouchListener(this);
		super.onViewCreated(view, savedInstanceState);
	}
	
	public ProgressDialog getProgressDialog(String msg) {
		ProgressDialog progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(true);
		return progressDialog;
	}

	/**
	 * 弹出吐司
	 * @param msg
	 */
	protected void showToast(final String msg) {
		runOnUI(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	/**
	 * 当前最后执行的线程任务,task的ID属性可以用于判断线程启动的先后
	 */
	protected Task lastTask = new Task(0) {

		@Override
		public void run() {

		}
	};
	
	protected DataTask dataTask = new DataTask(0) {
		
	};
	
	/**
	 * UI线程执行一个任务
	 * 
	 * @param run
	 */
	protected void runOnUI(Runnable run) {
		if (mActivity != null) {
			mActivity.runOnUiThread(run);
		}
	}

	/**
	 * 子线程执行一个任务
	 * 
	 * @param run
	 */
	protected void executeTask(Task task) {
		this.lastTask = task;
		mThreadPoolManager.executeTask(task);
	}
	
	/**
	 * 
	 * @param run
	 */
	protected void executeTask(Runnable run) {
		mThreadPoolManager.executeTask(run);
	}
	
	/**
	 * 隐藏输入法
	 * @param context
	 * @param achor
	 */
	public static void hideSoftInput( Context context, View achor ){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(achor.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void receiveInfo(Info info) {
		this.mInfo = info;
	}
	
	@Override
	public void onDestroy() {
		if (progress != null) {
			progress.dismiss();
		}
		super.onDestroy();
	}

	/**
	 * 是否有数据传递过来
	 * 
	 * @return
	 */
	protected boolean hasInfo() {
		if (mInfo != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 打开一个Fragment
	 * 
	 * @param clazz
	 * @param info
	 */
	protected void openFragment(final Class clazz, final Info info) {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				EventOpenFM event = new EventOpenFM();
				event.setClazz(clazz);
				event.setInfo(info);
				EventBus.getDefault().post(event);
			}
		});

	}

	/**
	 * 关闭一个Fragment
	 * 
	 * @param clazz
	 * @param info
	 */
	protected void closeFragment(final Class clazz, final Info info) {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				EventCloseFM event = new EventCloseFM();
				event.setClazz(clazz);
				event.setInfo(info);
				EventBus.getDefault().post(event);
				mInfo = null;
			}
		});

	}
	
	/**
	 * 隐藏进度条
	 */
	void dismissProgressDialog() {
		runOnUI(new Runnable() {
			@Override
			public void run() {
				if (progress != null) {
					progress.dismiss();
				}

			}
		});
	}
	
	/**
	 * 显示进度条
	 */
	void showProgressDialog() {
		runOnUI(new Runnable() {

			@Override
			public void run() {
				if (progress == null) {
					progress = new ProgressDialog(mContext);
					progress.setMessage("正在加载,请稍后...");
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				}
				progress.setCancelable(true);
				try {
					progress.show();
				} catch (Exception e) {
					LogUtil.e(TAG, "progress show exception");
				}

			}
		});

	}
	
	/**
	 * 获取数据bean的逻辑统一放在这里
	 */
	protected void obtainInfo() {

	}
	
	@Override
	public boolean backOperate() {
		mInfo = null;
		return false;
	}

	/**
	 * 防止View的事件穿透
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

}
