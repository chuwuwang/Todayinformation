package com.todayinfo.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;

/**
 * 意见反馈
 * 
 * @author zhou.ni 2015年4月16日
 */
public class FeedBackActivity extends SuperActivity implements OnClickListener{
	
	private EditText problemText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		problemText.setText("");
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		RelativeLayout leftBack = (RelativeLayout) headView.findViewById(R.id.back_left);
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		leftBack.setOnClickListener(this);
		title.setText("意见反馈");
		problemText = (EditText) this.findViewById(R.id.problem_text);
		TextView submit = (TextView) this.findViewById(R.id.sumbit);
		submit.setOnClickListener(this);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_left:
			finish();
			break;
			
		case R.id.sumbit:
			if ( !TextUtils.isEmpty(problemText.getText().toString()) ) {
				showToast("您的建议已经成功提交,谢谢您的提议");
				problemText.setText("");
			} else {
				showToast("请输入您要提交的意见");
			}
			break;
			
		default:
			break;
		}
	}

	@Override
	protected void obtainInfo() {
		
	}

}
