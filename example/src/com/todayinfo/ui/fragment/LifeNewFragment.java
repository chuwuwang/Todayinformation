package com.todayinfo.ui.fragment;

import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase.OnPreRefreshingAnimListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.LifeNews;
import com.todayinfo.model.LifeNewsList;
import com.todayinfo.ui.activity.LifeNewsDetailActivity;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.LocalDisplay;

public class LifeNewFragment extends SuperFragment {
	
	private PullToRefreshListView mListView;
	
	private NewsAdapter mAdapter;
	private ArrayList<LifeNews> mList = new ArrayList<LifeNews>();
	
	private static final String BEFORE_PATH = "http://content.2500city.com/Json?method=GetNewsListByChannelId&appVersion=3.4&numPerPage=30&adNum=50&orderType=3&channelId=";
	private static final String AFTER_PATH = "&requiredPage=";
	
	private int channelId; // 新闻 = 5，社区 = 27，房产 = 23,娱乐 = 21，汽车 = 24，
	private int requiredPage = 1;
	
	private RelativeLayout viewContainer;
	private LinearLayout footerView;    //最后一条listview
	
	public LifeNewFragment() {
		
	}

	public LifeNewFragment(int channelId) {
		this.channelId = channelId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fm_life_news_list, container, false);
		viewContainer = (RelativeLayout) rootView.findViewById(R.id.container);
		
		LocalDisplay.init(mContext);
		loadLifeNewsInfo(true);
		return rootView;
	}
	
	/**
	 * 加载生活新闻信息
	 */
	public void loadLifeNewsInfo(final boolean clean){
		String url = BEFORE_PATH + channelId + AFTER_PATH + requiredPage ;
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				try {
					if ( clean ) {
						mList.clear();
					}
					
					Gson gson = new Gson();
					LifeNewsList mLifeNewsList = gson.fromJson(new String(responseBody), LifeNewsList.class);
					if ( mLifeNewsList!=null ) {
						ArrayList<LifeNews> list = mLifeNewsList.getList();
						if ( list!=null && list.size()>0 ){
							requiredPage ++;
							mList.addAll(list);
							initListView(); // 初始化页面
						} else {
							requiredPage = 0;
							if ( mList==null && mList.size()==0 ) {
								initNetErro();
							}
						}
					} 
				} catch (Exception e) {
					e.printStackTrace();
					if ( mList==null && mList.size()==0 ) {
						initNetErro();
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				if ( mList==null && mList.size()==0 ) {
					initNetErro();
				}
			}
		});
	}
	
	private void initNetErro() {
		View netErroView = LayoutInflater.from(mContext).inflate(R.layout.layout_load_noorder, null);
		if (viewContainer != null) {
			viewContainer.removeAllViews();
			viewContainer.addView(netErroView);
		}

	}
	
	private void initListView() {
		if ( mContext == null )
			return;
		if ( footerView!=null ){
			mListView.getRefreshableView().removeFooterView(footerView);
			footerView = null;
		}
		if ( mListView!=null )
			mListView.onRefreshComplete();	
		
		View view = LayoutInflater.from(mContext).inflate(R.layout.fm_header_with_list_view, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.news_list);
		mAdapter = new NewsAdapter();
		mListView.setAdapter(mAdapter);
		
		if ( viewContainer!=null ) {
			viewContainer.removeAllViews();
			viewContainer.addView(view);
		}
		
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					requiredPage = 1;
					loadLifeNewsInfo(true);
				}
			}
		});
		
		mListView.setOnPreRefreshingAnimListener(new OnPreRefreshingAnimListener() {
			@Override
			public void onPreRefreshingAnim() {
				mListView.setFooterLoadingViewHeaderText("加载更多信息");
			}
		});
		
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if ( requiredPage!=0 ) {
					//滑动到底部自动刷新
					loadLifeNewsInfo(false);
				} else {
					//没有下一页了
					mListView.onRefreshComplete();
					if ( footerView == null ) {
						footerView = (LinearLayout) View.inflate(mActivity, R.layout.item_pull_to_refresh_footer, null);
						TextView footer = (TextView) footerView.findViewById(R.id.footer);
						footer.setText("到底了呢-共" + mList.size() + "条新闻");
						mListView.getRefreshableView().addFooterView(footerView);  
					}
				}
			}
		});
		
	}
	
	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}

	@Override
	public void pwdError() {

	}
	
	class NewsAdapter extends BaseAdapter {

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if ( convertView == null ) {
				convertView = View.inflate(mContext, R.layout.item_life_news_lv, null);
				holder = new ViewHolder();
				holder.imgNewsPic = (ImageView) convertView.findViewById(R.id.imgNewsPic);
				holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
				holder.description = (TextView) convertView.findViewById(R.id.tvDescription);
				holder.date_text = (TextView) convertView.findViewById(R.id.date_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
				
			LifeNews item = mList.get(position);
			if ( !TextUtils.isEmpty(item.getImage().getSrc()) ) {
				Picasso.with(mContext).load(item.getImage().getSrc()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(holder.imgNewsPic);
			}
			holder.title.setText(item.getTitle());
			holder.date_text.setText(item.getDate());
			holder.description.setText(item.getContent());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, LifeNewsDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("newsId", mList.get(position).getNewsId());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
					getActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

				}
			});
			
			return convertView;
		}
		
	}

	class ViewHolder {
		ImageView imgNewsPic;
		TextView title;
		TextView date_text;
		TextView description;
	}

}
