package com.todayinfo.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.loopj.android.http.RequestParams;
import com.todayinfo.model.ArticleComment;
import com.todayinfo.model.PhonePaging;
import com.todayinfo.ui.adapter.TechCommentAdapter;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.Contacts;
import com.todayinfo.utils.JsonUtils;
import com.todayinfo.utils.MD5Utils;

/**
 * 科技评论
 * 
 * @author zhou.ni 2015年5月21日
 */
public class PhoneTechCommentActivity extends SuperActivity {
	
	private PullToRefreshListView mListView;
	private ErrorHintView mErrorHintView;
	
	private List<ArticleComment> ArticleData = new ArrayList<ArticleComment>();
	private TechCommentAdapter adapter;
	
	private int offset = 0;				//第N条数据
	private boolean isPage = true;		//是否还有下一页
	private LinearLayout footerView;    //最后一条listview
	private String docid;
	
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
		setContentView(R.layout.activity_phone_tech_comment);
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
		TextView mTitle = (TextView) headView.findViewById(R.id.head_title);
		mTitle.setText("更多评论");
		mListView = (PullToRefreshListView) this.findViewById(R.id.list);
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		
		Intent intent = getIntent();
		docid = intent.getStringExtra("docid");
		
		setUpPullToList();
		showLoading(VIEW_LOADING);
		loadPhoneTechComments(true);
	}
	

	/**
	 * 设置listview的下拉刷新
	 */
	private void setUpPullToList(){
		adapter = new TechCommentAdapter(ArticleData, mContext);
		mListView.setAdapter(adapter);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					defParams();
					showLoading(VIEW_LOADING);
					loadPhoneTechComments(true);
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
				if ( isPage ) {
					//滑动到底部自动刷新
					loadPhoneTechComments(false);
				} else {
					//没有下一页了
					mListView.onRefreshComplete();
					if ( footerView == null ) {
						footerView = (LinearLayout) View.inflate(mContext, R.layout.item_pull_to_refresh_footer, null);
						TextView footer = (TextView) footerView.findViewById(R.id.footer);
						footer.setText("到底了呢-共" + ArticleData.size() + "条评论");
						mListView.getRefreshableView().addFooterView(footerView);  
					}
				}
			}
		});
	}
	
	private void loadPhoneTechComments(final boolean flag){
		RequestParams params = new RequestParams();
		String t = String.valueOf(System.currentTimeMillis() / 1000);	//获取当前时间
		params.put("timestamp", t);				//当前时间戳
		String token1 = MD5Utils.md5("d19cf361181f5a169c107872e1f5b722" + t);
		params.put("token1", token1);			//token1算法
		
		
		params.put("apiid", 3);
		params.put("module", "api_libraries_sjdbg_commentlist");
		params.put("avatar", "middle");
		params.put("docid", docid);
		params.put("returnformat", "json");
		params.put("encoding", "utf8");
//		params.put("onetime", timeStamp);			//分页用，传第一条新闻的时间戳，第一页下不用传值。
		params.put("offset", offset );				//分页用，数据记录的起始行数。0为第一条记录。
		params.put("rows", 10);						//分页用，数据记录每次取得的行数。不传此参数则默认获取10条记录。
	
		AsyncHttpUtil.get(Contacts.PHONE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if ( footerView!=null ){
					mListView.getRefreshableView().removeFooterView(footerView);
					footerView = null;
				}
				
				mListView.onRefreshComplete();
				
				if ( arg2!=null && arg2.length>0 ){
					try {
						JSONObject obj = new JSONObject(new String(arg2));
						String status = obj.getString("status");
						if ( TextUtils.equals("1", status) ) {
							offset += 10;
							JSONObject data = obj.getJSONObject("data");
							
							JSONArray list = data.getJSONArray("result");
							if ( list!=null ) {
								List<ArticleComment> instance = JsonUtils.getInstance(ArticleComment.class, list);
								if ( instance!=null && instance.size()>0) {
									if ( flag ) {
										ArticleData.clear();
									} 
									ArticleData.addAll(instance);
									if ( ArticleData.size()==0 ) {
										showLoading(VIEW_NODATA);
									} else {
										showLoading(VIEW_LIST);
										adapter.notifyDataSetChanged();
									}
								} else {
									showLoading(VIEW_LOADFAILURE);
								}
							}
							
							JSONObject paging = data.getJSONObject("paging");
							if ( paging!=null ){
								PhonePaging instance = JsonUtils.getInstance(PhonePaging.class, paging);
								if( instance!=null ) {
									try {
										int total = Integer.parseInt(instance.getTotal());
										if ( offset >= total ) {
											isPage = false;
										} else {
											isPage = true;
										}
									} catch (Exception e) {
										isPage = true;
									}
								}
							}
							
						} else {
							showLoading(VIEW_LOADFAILURE);
						}
					} catch (Exception e) {
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
	 * 恢复默认参数
	 */
	private void defParams(){
		offset = 0;
		isPage = true;
	}
	
	/**
	 * 显示动态加载的view
	 * 
	 * @param i
	 */
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
					loadPhoneTechComments(true);
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
					loadPhoneTechComments(true);
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
