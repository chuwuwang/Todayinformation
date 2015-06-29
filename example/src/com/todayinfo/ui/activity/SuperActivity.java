package com.todayinfo.ui.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jinghua.todayinformation.R;
import com.squareup.picasso.Picasso;
import com.todayinfo.controller.NetWorkCenter;
import com.todayinfo.service.UserController;
import com.todayinfo.ui.api.DataTask;
import com.todayinfo.ui.api.PwdErrorListener;
import com.todayinfo.ui.api.RetryNetwork;
import com.todayinfo.utils.BitmapUtils;
import com.todayinfo.utils.ShareUtils;
import com.todayinfo.utils.ThreadPoolManager;

public abstract class SuperActivity extends Activity implements RetryNetwork, PwdErrorListener {
	
	protected boolean isAvtive = false;
	
	protected Context mContext;
	
	protected boolean isPwdError = false;
	
	protected InputMethodManager imm;
	
	protected ShareUtils mShareUtils;
	
	protected UserController mUserController;
	
	NetWorkCenter mNetWorkCenter;
	
	ThreadPoolManager mThreadPoolManager;
	
	ProgressDialog progress;
	
	protected BitmapUtils mBitmapUtils;
	
	/**
	 * 用于判断APP是否是正常启动,还是奔溃后自动重启的
	 */
	protected static boolean isNormalStart = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		isAvtive = true;
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mShareUtils = new ShareUtils(this);
		mUserController = UserController.getInstance(mContext.getApplicationContext());
		mBitmapUtils = new BitmapUtils(mContext);
	}
	
	@Override
	public void pwdError() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				isPwdError = true;
			}
		});
	
	}
	
	/**
	 * UI线程执行一个任务
	 * 
	 * @param run
	 */
	protected void runOnUi(Runnable run) {
		runOnUiThread(run);
	}
	
	protected DataTask dataTask = new DataTask(0) {
		
	};
	
	/**
	 * 显示进度条
	 */
	void showProgressDialog(){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if ( progress == null) {
					progress = new ProgressDialog(mContext);
					progress.setMessage("正在加载,请稍后...");
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
					progress.setCancelable(true);
				}
				progress.show();
			}
		});
	}
	
	/**
	 * 隐藏进度条
	 */
	void dismissProgressDialog(){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if( progress!=null ){
					progress.dismiss();
				}
			}
		});
	}
	
	/**
	 * 得到一个进度条
	 * @param msg
	 * @return
	 */
	public ProgressDialog getProgressDialog(String msg) {
		progress = new ProgressDialog(this);
//		progressDialog.setIndeterminate(true);
		progress.setMessage(msg);
		progress.setCancelable(true);
		return progress;
	}
	
	/**
	 * 弹出吐司
	 * @param msg
	 */
	protected void showToast(final String msg) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isAvtive = false;
		if ( mNetWorkCenter!=null ) {
			mNetWorkCenter.removeRetry();
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		obtainInfo();
		isAvtive = true;
		
		if( !isNormalStart ){
			//非正常启动
//			Intent i = new Intent(this, FlashActivity.class);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	startActivity(i);
		}
		
		if( mNetWorkCenter!=null ) {
			mNetWorkCenter.setRetryNetwork(this);
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isAvtive = false;
		
		if ( mNetWorkCenter!=null ) {
			mNetWorkCenter.removeRetry();
		}
		
	}
	
	public boolean getAvtive(){
		return isAvtive;
	}
	
	
	protected void hintNoNetWork(){
	}
	
	protected float getDimen(int id){
		float dimension = getResources().getDimension(id);
		return dimension;
	}
	
	protected void executeTask(Runnable run){
		mThreadPoolManager.executeTask(run);
	}

	/**
	 * 返回对象本身
	 * 
	 * @return
	 */
	public Context This() {
		return this;
	}
	
	/**
	 * 接收数据 
	 */
	protected abstract void obtainInfo();
	
	/**
	 * 隐藏输入法
	 * 
	 * @param context
	 * @param achor
	 */
	public static void hideSoftInput(Context context, View achor) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(achor.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 加载图片
	 * 
	 * @param tuContainer
	 * @param item
	 */
	protected void loadIMG(ImageView img, String url) {
		Picasso.with(mContext).load(url).error(R.drawable.touxing).into(img);
	}
	
	/**
	 * 加载图片
	 * 
	 * @param tuContainer
	 * @param item
	 */
	protected void loadIMG(ImageView img, File file) {
		Picasso.with(mContext).load(file).error(R.drawable.touxing).into(img);
	}
	

}
