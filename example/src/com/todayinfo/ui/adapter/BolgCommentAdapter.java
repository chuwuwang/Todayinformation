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
import com.todayinfo.model.BolgComment;
import com.todayinfo.utils.DateUtils;

public class BolgCommentAdapter extends BaseAdapter {

	private Context mContext;
	private List<BolgComment> bolgList;

	public BolgCommentAdapter(Context mContext, List<BolgComment> bolgList) {
		super();
		this.mContext = mContext;
		this.bolgList = bolgList;
	}

	@Override
	public int getCount() {
		return bolgList.size();
	}

	@Override
	public Object getItem(int position) {
		return bolgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_bolg_comment_lv,
					null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BolgComment item = bolgList.get(position);
		holder.name.setText(item.getName());
		if (!TextUtils.isEmpty(item.getPublished())) {
			holder.time.setText(DateUtils.convertGMTToLoacale(item
					.getPublished()));
		}
		holder.content.setText(Html.fromHtml(item.getContent()));
		return convertView;
	}
	
	static class ViewHolder {
		TextView name;
		TextView time;
		TextView content;
	}
	
}

	