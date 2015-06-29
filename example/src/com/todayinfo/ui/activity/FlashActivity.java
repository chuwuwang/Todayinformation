package com.todayinfo.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jinghua.todayinformation.R;
import com.todayinfo.utils.SharedpreferncesUtil;

/**
 * 启动界面
 * @author zhou.ni 2015年4月13日
 */
public class FlashActivity extends SuperActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flash);

		isNormalStart = true;

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					public void run() {
						if (SharedpreferncesUtil.getGuided(mContext)) {
							Intent intent = new Intent(mContext,
									HomeActivity.class);
							startActivity(intent);
							FlashActivity.this.finish();
						} else {
							Intent intent = new Intent(FlashActivity.this,
									WelcomeActivity.class);
							FlashActivity.this.startActivity(intent);
							FlashActivity.this.finish();
						}

					}
				});
			}
		}, 2000);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void retry() {
	}

	@Override
	public void netError() {
	}

	@Override
	protected void obtainInfo() {
	}

}
