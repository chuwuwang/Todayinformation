package com.todayinfo.ui.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.todayinfo.model.NewItem;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.DateUtils;
import com.todayinfo.utils.LogUtil;

/**
 * 新闻焦点
 * 
 * @author zhou.ni 2015年3月17日
 */
public class NewsFocusActivity extends SuperActivity {
	
	private PullToRefreshListView mListView;
	private ErrorHintView mErrorHintView;
	
	private static final String PATH = "http://wcf.open.cnblogs.com/news/recommend/paged/";
	private int pageNext = 1;	//下一页页码
	private boolean flag = false;
	
	private NewsFocusAdapter adapter;
	private List<NewItem> mList = new ArrayList<NewItem>();
	
	private LinearLayout footerView;    //最后一条listview
	
	public static int VIEW_LIST = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_focus);
		getWindow().setBackgroundDrawable(null);
		
		initView();
	}

	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});;
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("焦点新闻");
		
		mListView = (PullToRefreshListView) this.findViewById(R.id.list);
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		setUpPullToList();
		
		showLoading(VIEW_LOADING);
		loadNewsFocusInfo(true);
	}
	
	/**
	 * 设置listview的下拉刷新
	 */
	private void setUpPullToList(){
		adapter = new NewsFocusAdapter();
		mListView.setAdapter(adapter);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					defParams();
					loadNewsFocusInfo(true);
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewItem item = mList.get(position-1);
				Intent intent = new Intent(mContext, NewsDetailActivity.class);
				Bundle info = new Bundle();
				info.putSerializable("item", item);
				intent.putExtras(info);
				startActivity(intent);
			}
		});
		
		mListView.setOnPreRefreshingAnimListener(new OnPreRefreshingAnimListener() {
			@Override
			public void onPreRefreshingAnim() {
				mListView.setFooterLoadingViewHeaderText("加载更多博客");
			}
		});
		
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if ( pageNext<12 ) {
					//滑动到底部自动刷新
					flag = false;
					loadNewsFocusInfo(false);
				} else {
					//没有下一页了
					mListView.onRefreshComplete();
					if ( footerView == null ) {
						footerView = (LinearLayout) View.inflate(mContext, R.layout.item_pull_to_refresh_footer, null);
						TextView footer = (TextView) footerView.findViewById(R.id.footer);
						footer.setText("到底了呢-共" + mList.size() + "条资讯");
						mListView.getRefreshableView().addFooterView(footerView);  
					}
				}
			}
		});
		
	}
	
	/**
	 * 加载新闻焦点的数据
	 */
	private void loadNewsFocusInfo(final boolean clean){
		String url = PATH + pageNext + "/10";
		
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				if ( footerView!=null ){
					mListView.getRefreshableView().removeFooterView(footerView);
					footerView = null;
				}
				
				mListView.onRefreshComplete();	
                try {
                	showLoading(VIEW_LIST);
                	if ( clean ) {
                		mList.clear();
                	}
                	ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBody);
                	parseNewsFocusXml(inputStream);
					adapter.notifyDataSetChanged();
					pageNext ++;
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.d("error", "hots news XmlPullParserException error");
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
	 * 解析新闻焦点 
	 * @param in
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	private void parseNewsFocusXml(InputStream in) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		
		parser.setInput(in, "UTF-8");
		int event = parser.getEventType();
		NewItem item = null;
		
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if ("entry".equals(parser.getName())) {
					flag = true;
					item = new NewItem();
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
					} else if ("link".equals(parser.getName())) {
						String link = parser.getAttributeValue(1);  //获得第二个属性值
						item.setLink(link);
					} else if ("diggs".equals(parser.getName())) {
						String diggs = parser.nextText();
						item.setDiggs(diggs);
					} else if ("views".equals(parser.getName())) {
						String views = parser.nextText();
						item.setViews(views);
					} else if ("comments".equals(parser.getName())) {
						String comments = parser.nextText();
						item.setComments(comments);
					} else if ("topicIcon".equals(parser.getName())) {
						String topicIcon = parser.nextText();
						item.setTopicIcon(topicIcon);
					}  
					else if ("sourceName".equals(parser.getName())) {
						String sourceName = parser.nextText();
						item.setSourceName(sourceName);
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
		mListView.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			mListView.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					defParams();
					showLoading(VIEW_LOADING);
					loadNewsFocusInfo(true);
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
					loadNewsFocusInfo(true);
				}
			});
			break;
			
		case 4:
			mErrorHintView.loadingData();
			break;
		}
	}

	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}

	@Override
	protected void obtainInfo() {

	}
	
	class NewsFocusAdapter extends BaseAdapter {

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
				convertView = View.inflate(mContext, R.layout.item_news_focus_lv, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.sourceName = (TextView) convertView.findViewById(R.id.sourceName);
				holder.published = (TextView) convertView.findViewById(R.id.publish);
				holder.diggsTv = (TextView) convertView.findViewById(R.id.diggs_tx);
				holder.commentsTv = (TextView) convertView.findViewById(R.id.comments);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			NewItem item = mList.get(position);
			
			holder.title.setText(item.getTitle());
			holder.sourceName.setText("" + item.getSourceName());
			
			if( !TextUtils.isEmpty(item.getTopicIcon()) ) {
				Picasso.with(mContext).load(item.getTopicIcon()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(holder.image);
			}
			
			if ( !TextUtils.isEmpty(item.getPublished()) ) {
				holder.published.setText( DateUtils.convertGMTToLoacale(item.getPublished()) );
			} 
			
			if ( !TextUtils.isEmpty(item.getComments()) ) {
				holder.commentsTv.setText("评论 " + item.getComments());
			} else {
				holder.commentsTv.setText("");
			}

			if ( !TextUtils.isEmpty(item.getDiggs()) ) {
				holder.diggsTv.setText(item.getDiggs());
			} else {
				holder.diggsTv.setText("");
			}
			
			return convertView;
		}

	}

	static class ViewHolder {
		TextView title;
		TextView published;
		TextView diggsTv;
		TextView sourceName;
		TextView commentsTv;
		ImageView image;
	}

}
