package com.todayinfo.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.ArticleItem;

public class IndexIntoAdapter extends BaseAdapter{

	private Context mContext;
	private List<ArticleItem> mList;
	
	public IndexIntoAdapter(List<ArticleItem> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
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
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
	     	convertView = View.inflate(mContext, R.layout.item_index_intro_lv, null);
		    holder.img = (ImageView) convertView.findViewById(R.id.img);
		    holder.title = (TextView) convertView.findViewById(R.id.title);
		    holder.body = (TextView) convertView.findViewById(R.id.body);
		    holder.date = (TextView) convertView.findViewById(R.id.time);
		    convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		ArticleItem item = mList.get(position);
		
		if ( !TextUtils.isEmpty(item.getPicUrl()) ){
			Picasso.with(mContext).load(item.getPicUrl()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(holder.img);
		} 
		if ( !TextUtils.isEmpty(item.getTitle()) ) {
			holder.title.setText(item.getTitle());
		} else {
			holder.title.setText("");
		}
		if ( !TextUtils.isEmpty(item.getTitle()) ) {
			holder.body.setText(item.getContent());
		} else {
			holder.body.setText("");
		}
		if ( !TextUtils.isEmpty(item.getTitle()) ) {
			holder.date.setText(item.getDate());
		} else {
			holder.date.setText("");
		}
		return convertView;
	}
	
	static class ViewHolder {
		ImageView img;
		TextView title;
		TextView body;
		TextView date;
	}

}
