package com.todayinfo.ui.activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;

/**
 * 关于今日资讯界面
 * 
 * @author zhou.ni 2015年4月16日
 */
public class AboutActivity extends SuperActivity {

	private TextView agreement;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		agreement.scrollTo(1, 1);
	}
	
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		RelativeLayout leftBack = (RelativeLayout) headView.findViewById(R.id.back_left);
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		agreement = (TextView) this.findViewById(R.id.agreement);
		leftBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title.setText("关于今日资讯");
		agreement.setMovementMethod(ScrollingMovementMethod.getInstance()); 
	}

	@Override
	public void retry() {
		
	}

	@Override
	public void netError() {
		
	}

	@Override
	public void pwdError() {
		
	}

	@Override
	protected void obtainInfo() {
		
	}

}
