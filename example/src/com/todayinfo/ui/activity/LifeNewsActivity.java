package com.todayinfo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.todayinfo.ui.component.PagerSlidingTabStrip;
import com.todayinfo.ui.component.ZoomOutPageTransformer;
import com.todayinfo.ui.fragment.LifeNewFragment;

/**
 * 生活新闻
 * 
 * @author zhou.ni 2015年5月24日
 */
public class LifeNewsActivity extends SuperFragmentActivity {

	private ViewPager contentPager;
	private mPagerAdapter adapter;
	private PagerSlidingTabStrip tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_life_news);

		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});;
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("生活新闻");
		
		contentPager = (ViewPager) this.findViewById(R.id.content_pager);
		adapter = new mPagerAdapter(getSupportFragmentManager());
		contentPager.setAdapter(adapter);
		contentPager.setOffscreenPageLimit(2);
		contentPager.setPageTransformer(true, new ZoomOutPageTransformer());
		
		tabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);
		tabs.setTextColorResource(R.color.light_gray_text);
		tabs.setDividerColorResource(R.color.common_list_divider);
		// tabs.setUnderlineColorResource(R.color.common_list_divider);
		tabs.setIndicatorColorResource(R.color.red);
		tabs.setSelectedTextColorResource(R.color.red);
		// tabs.setIndicatorHeight(5);
		tabs.setViewPager(contentPager);
	}

	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}

	private class mPagerAdapter extends FragmentStatePagerAdapter {

		private String Title[] = { "新 闻", "便民", "社 区", "美食", "娱乐", "电影", "房 产",
				"汽车" };

		public mPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			int[] id = { 5, 18, 27, 37, 21, 36, 23, 24 }; 	// 新闻 = 5，便民 = 18 ，社区
															// = 27，美食 = 37 ，娱乐
															// = 21，电影 = 36，房产 =
															// 23，汽车 = 24
			return new LifeNewFragment(id[arg0]);
		}

		@Override
		public int getCount() {
			return Title.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return Title[position];
		}

	}

}
