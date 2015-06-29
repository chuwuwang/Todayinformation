package com.todayinfo.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.todayinfo.ui.adapter.SuperViewPagerAdapter;
import com.todayinfo.utils.SharedpreferncesUtil;

/**
 * 欢迎界面
 * 
 * @author zhou.ni 2015年4月13日
 */
public class WelcomeActivity extends SuperActivity {
	private ViewPager introductVp;						// 介绍页面
	private SuperViewPagerAdapter viewPagerAdapter;
	private List<View> views = new ArrayList<View>();   // Tab页面列表
//	private ImageView layout_introduct_iv1;
//	private ImageView layout_introduct_iv2;
//	private ImageView layout_introduct_iv3;
	private TextView start;
	
	private TextView guide1Tx1;
	private TextView guide1Tx2;
	private TextView guide2Tx1;
	private TextView guide2Tx2;
	private TextView guide3Tx1;
	private TextView guide3Tx2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		initView();
	}

	private void initView() {

		introductVp = (ViewPager) findViewById(R.id.viewpager);
//		layout_introduct_iv1 = (ImageView) findViewById(R.id.layout_introduct_iv1);
//		layout_introduct_iv2 = (ImageView) findViewById(R.id.layout_introduct_iv2);
//		layout_introduct_iv3 = (ImageView) findViewById(R.id.layout_introduct_iv3);
		introductVp.setOffscreenPageLimit(3);

		View view1 = View.inflate(this, R.layout.item_introduct_v1, null);
		guide1Tx1 = (TextView) view1.findViewById(R.id.guide1_tx1);
		guide1Tx2 = (TextView) view1.findViewById(R.id.guide1_tx2);
		
		View view2 = View.inflate(this, R.layout.item_introduct_v2, null);
		guide2Tx1 = (TextView) view2.findViewById(R.id.guide2_tx1);
		guide2Tx2 = (TextView) view2.findViewById(R.id.guide2_tx2);
		
		View view3 = View.inflate(this, R.layout.item_introduct_v3, null);
		guide3Tx1 = (TextView) view3.findViewById(R.id.guide3_tx1);
		guide3Tx2 = (TextView) view3.findViewById(R.id.guide3_tx2);
		
		guide1Tx2.setText("最新资讯");
		guide1Tx1.setText("随时随地获取");
		startAlphaAnimation(guide1Tx1);
		startAlphaAnimation(guide1Tx2);

		// 监听登录立即体验按钮
		start = (TextView) view3.findViewById(R.id.item_introduct_finish);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SharedpreferncesUtil.getGuided(mContext)) {
					WelcomeActivity.this.finish();
					cancelAlphaAnimation();
				} else {
					SharedpreferncesUtil.setGuided(mContext);
					Intent intent = new Intent(mContext, HomeActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
					cancelAlphaAnimation();
				}
			}
		});

		views.add(view1);
		views.add(view2);
		views.add(view3);
		viewPagerAdapter = new SuperViewPagerAdapter(views);
		introductVp.setAdapter(viewPagerAdapter);
		introductVp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				setItemFocus(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	
	/**
	 * 根据传入参数设置焦点
	 * 
	 * @param index
	 */
	private void setItemFocus(int index) {
//		layout_introduct_iv1.setImageResource(R.drawable.icon_introduct_focus);
//		layout_introduct_iv2.setImageResource(R.drawable.icon_introduct_focus);
//		layout_introduct_iv3.setImageResource(R.drawable.icon_introduct_focus);
//		layout_introduct_iv1.setVisibility(View.VISIBLE);
//		layout_introduct_iv2.setVisibility(View.VISIBLE);
//		layout_introduct_iv3.setVisibility(View.VISIBLE);
		switch (index) {
		case 0:
//			layout_introduct_iv1.setImageResource(R.drawable.icon_introduct_unfocus);
			guide1Tx2.setText("最新资讯");
			guide1Tx1.setText("随时随地获取");
			guide2Tx2.setText("");
			guide2Tx1.setText("");
			guide3Tx2.setText("");
			guide3Tx1.setText("");
			startAlphaAnimation(guide1Tx1);
			startAlphaAnimation(guide1Tx2);
			break;
		case 1:
//			layout_introduct_iv2.setImageResource(R.drawable.icon_introduct_unfocus);
			guide2Tx2.setText("新鲜图片");
			guide2Tx1.setText("每时每分浏览");
			guide1Tx2.setText("");
			guide1Tx1.setText("");
			guide3Tx2.setText("");
			guide3Tx1.setText("");
			startAlphaAnimation(guide2Tx1);
			startAlphaAnimation(guide2Tx2);
			break;
		case 2:
			guide3Tx2.setText("改变你的阅读习惯");
			guide3Tx1.setText("从现在");
			guide1Tx2.setText("");
			guide1Tx1.setText("");
			guide2Tx2.setText("");
			guide2Tx1.setText("");
			startAlphaAnimation(guide3Tx1);
			startAlphaAnimation(guide3Tx2);
//			layout_introduct_iv1.setVisibility(View.GONE);
//			layout_introduct_iv2.setVisibility(View.GONE);
//			layout_introduct_iv3.setVisibility(View.GONE);
			break;
		}
	}
	
	private AlphaAnimation animation;
	
	/**
	 * 淡入淡出开始动画
	 */
	private void startAlphaAnimation(TextView textShow){
		if ( animation == null ) {
			// 创建一个AlphaAnimation对象  
			animation = new AlphaAnimation(0.01f, 1f);  
			// 设置动画执行的时间（单位：毫秒）  
			animation.setDuration(800);  
			// 设置重复次数 
//			animation.setRepeatCount(5);
		}
		// 把动画设置给控件
		textShow.setAnimation(animation);
		// 开始动画 
		animation.start();
	}
	
	/**
	 * 结束动画
	 */
	private void cancelAlphaAnimation(){
		if ( animation!=null ) {
			animation.cancel();
		}
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
