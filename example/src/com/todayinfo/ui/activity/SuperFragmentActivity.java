package com.todayinfo.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.widget.Toast;

import com.todayinfo.ui.api.PwdErrorListener;
import com.todayinfo.ui.api.RetryNetwork;
import com.todayinfo.ui.api.Task;
import com.todayinfo.utils.ThreadPoolManager;

public abstract class SuperFragmentActivity extends FragmentActivity implements RetryNetwork, PwdErrorListener{
	private static final String TAG = "BaseActivity";
	
	protected boolean isAvtive = false;
	protected Context mContext;
	
	ThreadPoolManager mThreadPoolManager;
	
	/**
	 * 当前最后执行的线程任务,task的ID属性可以用于判断线程启动的先后
	 */
	protected Task lastTask = new Task(0) {

		@Override
		public void run() {

		}
	};
	
	/**
	 * 碎片管理器
	 */
	protected FragmentManager fmm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		isAvtive = true;
		
		fmm = getSupportFragmentManager();
		mThreadPoolManager = ThreadPoolManager.getInstance();
	}
	
	@Override
	public void pwdError() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//TODO 弹出密码错误dialog
				
			}
		});
	
	}
	
	
	public ProgressDialog getProgressDialog(String msg) {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(true);
		return progressDialog;
	}
	
	protected void showToast(final String msg){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(SuperFragmentActivity.this, msg, 1).show();
				
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isAvtive = false;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		isAvtive = true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isAvtive = false;
	}
	
	public boolean getAvtive(){
		return isAvtive;
	}
	
	
	protected void hintNoNetWork(){
		//TODO 弹出网络错误提示框
	}
	
	protected float getDimen(int id){
		float dimension = getResources().getDimension(id);
		return dimension;
	}
	
	/**
	 * 子线程执行一个任务
	 * 
	 * @param task
	 */
	protected void executeTask(Task task) {
		this.lastTask = task;
		mThreadPoolManager.executeTask(task);
	}
	
	protected void executeTask(Runnable run){
        mThreadPoolManager.executeTask(run);
	}
	
}
