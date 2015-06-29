package com.todayinfo.ui.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase.OnPreRefreshingAnimListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.BlogItem;
import com.todayinfo.ui.activity.BolgDetailActivity;
import com.todayinfo.ui.component.CircleImageView;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.DateUtils;

/**
 * 最新博客
 * 
 * @author zhou.ni 2015年5月17日
 */
public class FreshBolgLayout extends RelativeLayout{
	
	private static final String TAG = "FreshBolgLayout";
	
	private Activity mActivity;
	
	private PullToRefreshListView freshListView;
	private ErrorHintView mErrorHintView;
	
	private static final String FRESH_PATH = "http://wcf.open.cnblogs.com/blog/sitehome/paged/";
	private int pageNext = 1;	//下一页页码
	private boolean flag = false;
	
	private List<BlogItem> mList = new ArrayList<BlogItem>();
	private FreshBolgAdapter adapter;
	
	private LinearLayout footerView;    //最后一条listview
	
	public static int VIEW_LIST = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	public FreshBolgLayout(Context context) {
		super(context);
		mActivity = (Activity) getContext();
		initView();
	}

	public FreshBolgLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (Activity) getContext();
		initView();
	}

	public FreshBolgLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mActivity = (Activity) getContext();
		initView();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		View view = View.inflate(getContext(), R.layout.layout_fresh_blog, this);
		freshListView = (PullToRefreshListView) view.findViewById(R.id.fresh_blog_list);
		mErrorHintView = (ErrorHintView) view.findViewById(R.id.hintView);
		setUpPullToList();
	}
	
	/**
	 * 设置listview的下拉刷新
	 */
	private void setUpPullToList(){
		adapter = new FreshBolgAdapter();
		freshListView.setAdapter(adapter);
		freshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					defParams();
					loadFreshBolgInfo(true);
				}
			}
		});
		
		freshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BlogItem item = mList.get(position-1);
				Intent intent = new Intent(mActivity, BolgDetailActivity.class);
				Bundle info = new Bundle();
				info.putSerializable("item", item);
				intent.putExtras(info);
				mActivity.startActivity(intent);
			}
		});
		
		freshListView.setOnPreRefreshingAnimListener(new OnPreRefreshingAnimListener() {
			@Override
			public void onPreRefreshingAnim() {
				freshListView.setFooterLoadingViewHeaderText("加载更多博客");
			}
		});
		
		freshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if ( pageNext<12 ) {
					//滑动到底部自动刷新
					flag = false;
					loadFreshBolgInfo(false);
				} else {
					//没有下一页了
					freshListView.onRefreshComplete();
					if ( footerView == null ) {
						footerView = (LinearLayout) View.inflate(mActivity, R.layout.item_pull_to_refresh_footer, null);
						TextView footer = (TextView) footerView.findViewById(R.id.footer);
						footer.setText("到底了呢-共" + mList.size() + "条博客");
						freshListView.getRefreshableView().addFooterView(footerView);  
					}
				}
			}
		});
		
	}
	
	/**
	 * 加载最新博客信息
	 */
	public void loadFreshBolgInfo(final boolean clean){
		showLoading(VIEW_LOADING);
		
		String url = FRESH_PATH + pageNext + "/10";
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				if ( footerView!=null ){
					freshListView.getRefreshableView().removeFooterView(footerView);
					footerView = null;
				}
				
				freshListView.onRefreshComplete();	
				
				try {
					showLoading(VIEW_LIST);
					if ( clean ) {
						mList.clear();
					}
					ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBody);
					parseFreshBolgXml(inputStream);
					adapter.notifyDataSetChanged();
					pageNext ++;
				}catch (Exception e) {
					e.printStackTrace();
					showLoading(VIEW_LOADFAILURE);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				showLoading(VIEW_WIFIFAILUER);
			}
		});
	}
	
	/**
	 * 解析最新博客
	 * @param in
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	private void parseFreshBolgXml(InputStream in) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(in, "UTF-8");
		int event = parser.getEventType();
		BlogItem item = null;
		
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if ("entry".equals(parser.getName())) {
					flag = true;
					item = new BlogItem();
				}
				if (flag) {
					if ("id".equals(parser.getName())) {
						String id = parser.nextText();
						item.setId(id);
					} else if ("title".equals(parser.getName())) {
						String title = parser.nextText();
						item.setTitle(title);
					} else if ("summary".equals(parser.getName())) {
						String summary = parser.nextText();
						item.setSummary(summary);
					} else if ("published".equals(parser.getName())) {
						String published = parser.nextText();
						item.setPublished(published);
					} else if ("updated".equals(parser.getName())) {
						String updated = parser.nextText();
						item.setUpdated(updated);
					}else if("name".equals(parser.getName())){
						String name = parser.nextText();
						item.setName(name);
					}else if("uri".equals(parser.getName())){
						String uri = parser.nextText();
						item.setUri(uri);
					}else if("avatar".equals(parser.getName())){
						String avatar = parser.nextText();
						item.setAvatar(avatar);
						Log.i(TAG, "---------------->>>"+avatar);
					} else if ("link".equals(parser.getName())) {
						String href = parser.getAttributeValue(1);
						item.setHref(href);
					} else if ("diggs".equals(parser.getName())) {
						String diggs = parser.nextText();
						item.setDiggs(diggs);
					} else if ("views".equals(parser.getName())) {
						String views = parser.nextText();
						item.setViews(views);
					} else if ("comments".equals(parser.getName())) {
						String comments = parser.nextText();
						item.setComments(comments);
					}
				}
				break;
			case XmlPullParser.TEXT:
				break;
			case XmlPullParser.END_TAG:
				if ("entry".equals(parser.getName())) {
					mList.add(item);
				}
				break;
			}
			event = parser.next();
		}
		
	}
	
	private void defParams(){
		pageNext = 1;
		flag = false;
	}
	
	private void showLoading(int i){
		mErrorHintView.setVisibility(View.GONE);
		freshListView.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			freshListView.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					defParams();
					showLoading(VIEW_LOADING);
					loadFreshBolgInfo(true);
				}
			});
			break;
			
		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					defParams();
					showLoading(VIEW_LOADING);
					loadFreshBolgInfo(true);
				}
			});
			break;
			
		case 4:
			mErrorHintView.loadingData();
			break;
		}
	}
	
	class FreshBolgAdapter extends BaseAdapter {

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
				convertView = View.inflate(mActivity, R.layout.item_fresh_bolg_lv, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.uPic = (CircleImageView) convertView.findViewById(R.id.image);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.published = (TextView) convertView.findViewById(R.id.publish);
				holder.summary = (TextView) convertView.findViewById(R.id.summary);
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
			
			BlogItem item = mList.get(position);
			holder.title.setText(item.getTitle());
			holder.name.setText(item.getName());
			holder.summary.setText(item.getSummary());
			
			if( !TextUtils.isEmpty(item.getAvatar()) ) {
				Picasso.with(mActivity).load(item.getAvatar()).placeholder(R.drawable.touxiang).error(R.drawable.touxiang).into(holder.uPic);
			}
			
			if ( !TextUtils.isEmpty(item.getPublished()) ) {
				holder.published.setText( DateUtils.convertGMTToLoacale(item.getPublished()) );
			} 
			
			if ( !TextUtils.isEmpty(item.getViews()) ) {
				holder.viewsTv.setText(item.getViews());
			} else {
				holder.viewsTv.setText("");
			}
			
			if ( !TextUtils.isEmpty(item.getComments()) ) {
				holder.commentsTv.setText(item.getComments());
			} else {
				holder.commentsTv.setText("");
			}

			if ( !TextUtils.isEmpty(item.getDiggs()) ) {
				holder.diggsTv.setText(item.getDiggs());
			} else {
				holder.diggsTv.setText("");
			}
			
//			if(position%2 == 0){
//				convertView.setBackgroundResource(R.color.white);
//			}else{
//				convertView.setBackgroundResource(R.color.freshblogsbg);
//			}
			
			return convertView;
		}

	}
	
	static class ViewHolder {
		TextView title;
		CircleImageView uPic;
		TextView summary;
		TextView published;
		TextView viewsTv;
		TextView diggsTv;
		TextView name;
		TextView commentsTv;
		TextView textViews;
		LinearLayout views;
		LinearLayout diggs;
		LinearLayout comment;
	}

	
}
