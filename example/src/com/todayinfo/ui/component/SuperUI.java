package com.todayinfo.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.jinghua.todayinformation.R;

/**
 * * UI 工具组件
 * 
 * @author 荣
 * 
 */
@SuppressLint("InflateParams") 
public class SuperUI {

	/**
	 * 显示收藏成功UI
	 */
	public static void showCollectionUI(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.custom_collection, null);
		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 显示取消收藏成功UI
	 */
	public static void showUncollectionUI(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.custom_uncollection, null);
		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
}
