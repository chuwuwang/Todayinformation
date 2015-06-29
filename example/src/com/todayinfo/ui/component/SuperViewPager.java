package com.todayinfo.ui.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 插入控件的viewpager
 * 
 * @author zhou.ni 2015年4月8日
 */
public class SuperViewPager extends ViewPager {

	/** true 可以滑动 false 不可以滑动 **/
	private boolean enabled;

	public SuperViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = false;
	}

	// 触摸没有反应就可以了
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
