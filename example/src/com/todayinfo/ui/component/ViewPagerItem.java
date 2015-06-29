package com.todayinfo.ui.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 与Viewpager配合使用
 * 
 * @author zhou.ni 2015年4月25日
 */
public class ViewPagerItem extends LinearLayout {

	/**
	 * 圆点数量:默认为0
	 * */
	private int count = 0;
	
	/**
	 * 圆点选中索引：默认为0
	 * */
	private int index = 0;
	
	/**
	 * 设置圆点背景，未选中
	 * */
	private Bitmap noBitmap;
	
	/**
	 * 设置圆点背景，已选中
	 * */
	private Bitmap yesBitmap;
	/**
	 * 上下文
	 * */
	private Context context;
	/**
	 * 布局参数
	 * */
	private LinearLayout.LayoutParams layoutParamsWH = null;
	
	/***/
	private int margin = 5;
	
	public ViewPagerItem(Context context) {
		this(context, null);
	}
	
	public ViewPagerItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initItem();
	}

	/**
	 * 初始化布局以及数据
	 * */
	private void initItem(){
		layoutParamsWH = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(layoutParamsWH);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		
	}
	
	/**
	 * 设置两个圆点的显示图片
	 * @param no 默认图片
	 * @param yes 高亮图片
	 * */
	public void setBitmap(int no, int yes){
		try{
			noBitmap = BitmapFactory.decodeResource(getResources(), no);
			yesBitmap = BitmapFactory.decodeResource(getResources(), yes);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//绘制图片
	private void writeRound(){
		this.removeAllViews(); //重绘时移除所有子View
		if(yesBitmap!=null && noBitmap!=null){
			for(int i=0; i<count; i++){
				ImageView img = new ImageView(context);
				LinearLayout.LayoutParams img_Params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				//两个圆点之间的间距
				img_Params.leftMargin = margin;
				img_Params.rightMargin = margin;
				img.setLayoutParams(img_Params);
				this.addView(img, i);
				if(index==i){ //根据索引值改变圆点显示图片
					img.setImageBitmap(yesBitmap);
				} else {
					img.setImageBitmap(noBitmap);
				}
			}
		}
	}
	
	/**
	 * 更新显示UI
	 * @param index 索引
	 * */
	public void notifyDataSetChanged(int index){
		if(index<0) index=0; //索引值不能小于0
		setIndex(index); //设置索引
		writeRound(); //重新绘制
	}
	
	/**
	 * 设置需要显示的总数
	 * */
	public void setCount(int count) {
		if(count<0) count = 0; //总数不能小于0
		this.count = count;
	}
	
	/**
	 * 设置需要的改变的Item的索引:从0开始
	 * */
	public void setIndex(int index) {
		if(index<0) index=0; //索引值不能小于0
		this.index = index;
	}

	/**
	 * 设置圆点之间的距离
	 * */
	public void setMargin(int margin) {
		this.margin = margin;
	}
	
}
