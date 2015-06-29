package com.todayinfo.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.todayinfo.ui.adapter.SuperViewPagerAdapter;
import com.todayinfo.ui.fragment.FreshBolgLayout;
import com.todayinfo.ui.fragment.HotsBolgLayout;

/**
 * 博客分享
 * 
 * @author zhou.ni 2015年3月17日
 */
public class BolgsShareActivity extends SuperActivity implements OnClickListener{
	
	private SuperViewPagerAdapter pagerAdapter;
	private ViewPager mViewPager;
	private TextView newsBlog;
	private TextView hotsBlog;
	
	private FreshBolgLayout mFreshBolgLayout;
	private HotsBolgLayout mHotsBolgLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blogshare);
		getWindow().setBackgroundDrawable(null);
		
		initView();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.back_left);
		layout.setOnClickListener(this);
		LinearLayout linear = (LinearLayout) this.findViewById(R.id.head_filter);
		newsBlog = (TextView) linear.findViewById(R.id.news_blog);
		hotsBlog = (TextView) linear.findViewById(R.id.hots_blog);
		newsBlog.setOnClickListener(this);
		hotsBlog.setOnClickListener(this);
		mViewPager = (ViewPager) this.findViewById(R.id.viewpager);
		
		initFilter();
		
		mFreshBolgLayout = new FreshBolgLayout(mContext);
		mHotsBolgLayout = new HotsBolgLayout(mContext);
		
		ArrayList<View> views = new ArrayList<View>();
		views.add(mFreshBolgLayout);
		views.add(mHotsBolgLayout);
		pagerAdapter = new SuperViewPagerAdapter(views);
		mViewPager.setAdapter(pagerAdapter);
		
		mFreshBolgLayout.loadFreshBolgInfo(true);
		mHotsBolgLayout.loadHotsBolgInfo(true);
	}
	
	/**
	 * 初始化导航过滤器
	 */
	private void initFilter(){
		newsBlog.setTextColor(mContext.getResources().getColor(R.color.white));
		newsBlog.setBackgroundResource(R.drawable.nofinish_filter_pressed_bg);
		hotsBlog.setTextColor(mContext.getResources().getColor(R.color.FF545A));
		hotsBlog.setBackgroundResource(R.drawable.finished_filter_normal_bg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_left:
			finish();
			break;
		case R.id.news_blog:
			handlerFilter(0);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.hots_blog:
			handlerFilter(1);
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 重置过滤器
	 */
	@SuppressWarnings("unused")
	private void resetTab(){
		newsBlog.setTextColor(mContext.getResources().getColor(R.color.FF545A));
		newsBlog.setBackgroundResource(R.drawable.finished_filter_normal_bg);
		hotsBlog.setTextColor(mContext.getResources().getColor(R.color.FF545A));
		hotsBlog.setBackgroundResource(R.drawable.finished_filter_normal_bg);
	}
	
	/**
	 * 处理选中的导航过滤器
	 * @param position
	 */
	private void handlerFilter(int position){
		switch (position) {
		case 0:
			newsBlog.setTextColor(mContext.getResources().getColor(R.color.white));
			newsBlog.setBackgroundResource(R.drawable.nofinish_filter_pressed_bg);
			hotsBlog.setTextColor(mContext.getResources().getColor(R.color.FF545A));
			hotsBlog.setBackgroundResource(R.drawable.finished_filter_normal_bg);
			break;
		case 1:
			newsBlog.setTextColor(mContext.getResources().getColor(R.color.FF545A));
			newsBlog.setBackgroundResource(R.drawable.nofinish_filter_normal_bg);
			hotsBlog.setTextColor(mContext.getResources().getColor(R.color.white));
			hotsBlog.setBackgroundResource(R.drawable.finished_filter_pressed_bg);
			break;
		default:
			break;
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
