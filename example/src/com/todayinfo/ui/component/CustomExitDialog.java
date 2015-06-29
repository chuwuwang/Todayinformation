package com.todayinfo.ui.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jinghua.todayinformation.R;

/**
 * <p>
 * Title: CustomDialog
 * </p>
 * <p>
 * Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件）
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * 
 * @author archie
 * @version 1.0
 */
public class CustomExitDialog extends Dialog implements OnClickListener {
	int layoutRes;// 布局文件
	Context context;
	Activity mActivity;
	/** 确定按钮 **/
	private Button confirmBtn;
	/** 取消按钮 **/
	private Button cancelBtn;

	public CustomExitDialog(Context context) {
		super(context);
		this.context = context;
		mActivity = (Activity) context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public CustomExitDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
		mActivity = (Activity) context;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CustomExitDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		mActivity = (Activity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);

		// 根据id在布局中找到控件对象
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);

		// 设置按钮的文本颜色
		confirmBtn.setTextColor(0xff1E90FF);
		cancelBtn.setTextColor(0xff1E90FF);

		// 为按钮绑定点击事件监听器
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_btn:
			mActivity.finish();
			System.exit(0);
			break;
			
		case R.id.cancel_btn:
			dismiss();
			break;
		}
	}
}