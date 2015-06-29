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
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase.OnPreRefreshingAnimListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.todayinfo.model.BolgComment;
import com.todayinfo.ui.adapter.BolgCommentAdapter;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;

/**
 * 新闻评论
 * 
 * @author zhou.ni 2015年5月18日
 */
public class NewsCommentActivity extends SuperActivity {
	
	private PullToRefreshListView mListView;
	private ErrorHintView mErrorHintView;
	private TextView mTitle;
	
	private List<BolgComment> bolgList = new ArrayList<BolgComment>();
	private List<BolgComment> list = new ArrayList<BolgComment>();
	private BolgCommentAdapter adapter;
	
	private int pageNext = 1;	//下一页页码
	private boolean flag = false;
	private String id;
	private LinearLayout footerView;    //最后一条listview
	
	private static final String STR_BEFORE = "http://wcf.open.cnblogs.com/news/item/";
	private static final String STR_AFTER = "/comments/";
	
	public static int VIEW_LIST = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	public static int VIEW_NODATA = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bolg_comment);
		getWindow().setBackgroundDrawable(null);
		
		initLayout();
	}

	/**
	 * 初始化控件
	 */
	private void initLayout() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitle = (TextView) headView.findViewById(R.id.head_title);
		mListView = (PullToRefreshListView) this.findViewById(R.id.list);
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		String title = intent.getStringExtra("title");
		mTitle.setText(title);
		
		setUpPullToList();
		showLoading(VIEW_LOADING);
		loadBolgCommentInfo(true);
	}
	
	/**
	 * 设置listview的下拉刷新
	 */
	private void setUpPullToList(){
		adapter = new BolgCommentAdapter(mContext, bolgList);
		mListView.setAdapter(adapter);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					pageNext = 1;
					flag = false;
					loadBolgCommentInfo(true);
				}
			}
		});
		
		mListView.setOnPreRefreshingAnimListener(new OnPreRefreshingAnimListener() {
			@Override
			public void onPreRefreshingAnim() {
				mListView.setFooterLoadingViewHeaderText("加载更多评论");
			}
		});
		
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if ( pageNext==0 ) {
					//没有下一页了
					mListView.onRefreshComplete();
					if ( footerView == null ) {
						footerView = (LinearLayout) View.inflate(mContext, R.layout.item_pull_to_refresh_footer, null);
						TextView footer = (TextView) footerView.findViewById(R.id.footer);
						footer.setText("到底了呢-共" + bolgList.size() + "条评论");
						mListView.getRefreshableView().addFooterView(footerView);  
					}
				} else {
					//滑动到底部自动刷新
					flag = false;
					loadBolgCommentInfo(false);
				}
			}
		});
	}
	
	/**
	 * 加载评论内容
	 * @param url
	 */
	private void loadBolgCommentInfo(final boolean clean){
		String url = STR_BEFORE + id + STR_AFTER + pageNext +"/20";
		
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				if ( footerView!=null ){
					mListView.getRefreshableView().removeFooterView(footerView);
					footerView = null;
				}
				
				mListView.onRefreshComplete();	
				
				if ( responseBody!=null && responseBody.length>0 ){
					try {
						if ( clean ){
							bolgList.clear();
						}
						list.clear();
						ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBody);
						parseCommentXml(inputStream);
						if ( list.size()==0 && list.size()<20 ) {
							pageNext = 0;
						} else {
							pageNext ++;
						}
						bolgList.addAll(list);
						if ( bolgList.size()==0 ) {
							showLoading(VIEW_NODATA);
						} else {
							showLoading(VIEW_LIST);
							adapter.notifyDataSetChanged();
						}
					} catch (Exception e) {
						e.printStackTrace();
						showLoading(VIEW_LOADFAILURE);
					}
					
				} else {
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
	 * 解析评论内容
	 * 
	 * @param in
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void parseCommentXml(InputStream in) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(in, "UTF-8");
		int event = parser.getEventType();
		BolgComment item = null;
		
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
				
			case XmlPullParser.START_TAG:
				if ("entry".equals(parser.getName())) {
					flag = true;
					item = new BolgComment();
				}
				if (flag) {
					if ("id".equals(parser.getName())) {
						String id = parser.nextText();
						item.setId(id);
					} else if ("title".equals(parser.getName())) {
						String title = parser.nextText();
						item.setTitle(title);
					} else if ("published".equals(parser.getName())) {
						String published = parser.nextText();
						item.setPublished(published);
					} else if ("updated".equals(parser.getName())) {
						String updated = parser.nextText();
						item.setUpdated(updated);
					} else if("author".equals(parser.getName())){
						
					} else if ("name".equals(parser.getName())) {
						String name = parser.nextText();
						item.setName(name);
					} else if ("uri".equals(parser.getName())) {
						String uri = parser.nextText();
						item.setUri(uri);
					} else if ("content".equals(parser.getName())) {
						String content = parser.nextText();
						item.setContent(content);
					} 
				}
				break;

			case XmlPullParser.TEXT:
				break;

			case XmlPullParser.END_TAG:
				if ("entry".equals(parser.getName())) {
					list.add(item);
				}
				break;
			}
			event = parser.next();
		}
		
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
					pageNext = 1;
					flag = false;
					showLoading(VIEW_LOADING);
					loadBolgCommentInfo(true);
				}
			});
			break;
			
		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					pageNext = 1;
					flag = false;
					showLoading(VIEW_LOADING);
					loadBolgCommentInfo(true);
				}
			});
			break;
			
		case 4:
			mErrorHintView.loadingData();
			break;
			
		case 5:
			mErrorHintView.hideLoading();
			mErrorHintView.noData();
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

}
