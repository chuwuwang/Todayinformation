package com.todayinfo.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.ArticleComment;
import com.todayinfo.ui.component.CircleImageView;

public class TechCommentAdapter extends BaseAdapter {

	private List<ArticleComment> mList;
	private Context mContext;

	public TechCommentAdapter(List<ArticleComment> mList, Context mContext) {
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
		if ( convertView==null ){
			convertView = View.inflate(mContext, R.layout.item_comment_lv, null);
			holder = new ViewHolder();
			holder.uPic = (CircleImageView) convertView.findViewById(R.id.comment_image);
			holder.name = (TextView) convertView.findViewById(R.id.comment_name);
			holder.time = (TextView) convertView.findViewById(R.id.comment_date);
			holder.content = (TextView) convertView.findViewById(R.id.comment_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ArticleComment item = mList.get(position);
		if ( !TextUtils.isEmpty(item.getAvatar()) ){
			Picasso.with(mContext).load(item.getAvatar()).placeholder(R.drawable.touxiang).error(R.drawable.touxiang).into(holder.uPic);
		}
		holder.name.setText(item.getUsername());
		holder.time.setText(item.getDate());
		holder.content.setText(Html.fromHtml(item.getContent()));
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView time;
		TextView content;
		CircleImageView uPic;
	}

}
