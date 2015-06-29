package com.todayinfo.ui.adapter;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.GrilPhotoItem;
import com.todayinfo.ui.activity.GrilDetailActivity;

/**
 * 美女写真的适配器
 * 
 * @author zhou.ni 2015年4月26日
 */
public class GrilListAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<GrilPhotoItem> mList;
	
//	private Drawable defDrawable;
	
	public GrilListAdapter(Context mContext, List<GrilPhotoItem> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		
//		Resources res = mContext.getResources();
//		Bitmap bitmap = readBitMap(mContext, R.drawable.default_ptr_rotate);
//		defDrawable = new BitmapDrawable(res, bitmap);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if( convertView==null ) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_home_gril_photo, null);
			holder.pic = (ImageView) convertView.findViewById(R.id.gril_image);
			holder.coner = (ImageView) convertView.findViewById(R.id.coner);
			holder.title = (TextView) convertView.findViewById(R.id.gril_title);
			convertView.setTag(holder);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ViewHolder holder = (ViewHolder) v.getTag();
					Intent intent = new Intent(mContext, GrilDetailActivity.class);
					intent.putExtra("id", holder.item.getId());
					intent.putExtra("title", holder.item.getTitle());
					mContext.startActivity(intent);
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		GrilPhotoItem item = mList.get(position);
		holder.item = item;
		
		if ( !TextUtils.isEmpty(item.getLitpic()) ){
			loadIMG(holder.pic, item.getLitpic());
		} 
		
		if ( !TextUtils.isEmpty(item.getTitle()) ) {
			holder.title.setText(item.getTitle());
		} else {
			holder.title.setText("");
		}
		
		return convertView;
	}
	
	/**
	 * 加载图片
	 * 
	 * @param tuContainer
	 * @param item
	 */
	private  void loadIMG(final ImageView img, String url) {
		Picasso.with(mContext).load(url).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(img);
	}
	
	@SuppressWarnings("deprecation")
	public Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
	static class ViewHolder{
		ImageView pic;
		TextView title;
		ImageView coner;
		
		GrilPhotoItem item;
	}

}
