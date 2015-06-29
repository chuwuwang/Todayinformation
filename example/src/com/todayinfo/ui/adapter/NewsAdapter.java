package com.todayinfo.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.todayinfo.model.NewItem;
import com.todayinfo.utils.DateUtils;

public class NewsAdapter extends BaseAdapter {

	private Context mContext;
	private List<NewItem> mList;

	public NewsAdapter(Context mContext, List<NewItem> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
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
		if ( convertView == null ) {
			convertView = View.inflate(mContext, R.layout.item_home_news, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.hots_title);
			holder.sourceName = (TextView) convertView.findViewById(R.id.hots_sourceName);
			holder.published = (TextView) convertView.findViewById(R.id.hots_publish);
			holder.summary = (TextView) convertView.findViewById(R.id.hots_summary);
			holder.viewsTv = (TextView) convertView.findViewById(R.id.views_tx);
			holder.diggsTv = (TextView) convertView.findViewById(R.id.diggs_tx);
			holder.commentsTv = (TextView) convertView.findViewById(R.id.comment_tx);
			holder.diggs = (LinearLayout) convertView.findViewById(R.id.hots_diggs);
			holder.views = (LinearLayout) convertView.findViewById(R.id.hots_views);
			holder.comment = (LinearLayout) convertView.findViewById(R.id.hots_comment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		NewItem item = mList.get(position);
		holder.title.setText(item.getTitle());
		holder.sourceName.setText("来自:" + item.getSourceName());
		holder.summary.setText(item.getSummary());
		
		if ( !TextUtils.isEmpty(item.getPublished()) ) {
			holder.published.setText( DateUtils.convertGMTToLoacale(item.getPublished()) );
		} 
		
		if ( !TextUtils.isEmpty(item.getViews()) ) {
			holder.viewsTv.setText(item.getViews());
		} else {
			
		}
		
		if ( !TextUtils.isEmpty(item.getComments()) ) {
			holder.commentsTv.setText(item.getComments());
		} else {
			
		}

		if ( !TextUtils.isEmpty(item.getDiggs()) ) {
			holder.diggsTv.setText(item.getDiggs());
		} else {
	
		}
		
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView summary;
		TextView published;
		TextView viewsTv;
		TextView diggsTv;
		TextView sourceName;
		TextView commentsTv;
		TextView textViews;
		LinearLayout views;
		LinearLayout diggs;
		LinearLayout comment;
	}

}
