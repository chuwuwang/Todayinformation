package com.todayinfo.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.todayinfo.model.ArticleItem;
import com.todayinfo.model.TextArticleItem;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.ui.component.SuperUI;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.Contacts;
import com.todayinfo.utils.JsonUtils;
import com.todayinfo.utils.MD5Utils;

/**
 * 科技详情
 * 
 * @author zhou.ni 2015年5月18日
 */
public class PhoneTechDetailActivity extends SuperActivity implements OnClickListener{
	
	private TextView mTitle;
	private TextView mDate;
	private WebView mWeb;
	private ErrorHintView mErrorHintView;
	private ScrollView mScrollView;
	
	public static int VIEW_CONTENT = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	private TextArticleItem item;
	private ArticleItem articleItem = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_tech_detail);
		getWindow().setBackgroundDrawable(null);
		
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		RelativeLayout leftBack = (RelativeLayout) headView.findViewById(R.id.back_left);
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("文章详情");
		LinearLayout top = (LinearLayout) this.findViewById(R.id.toolbar_top);
		LinearLayout comment = (LinearLayout) this.findViewById(R.id.toolbar_comment);
		LinearLayout save = (LinearLayout) this.findViewById(R.id.toolbar_save);
		leftBack.setOnClickListener(this);
		top.setOnClickListener(this);
		comment.setOnClickListener(this);
		save.setOnClickListener(this);
		 
		mTitle = (TextView) this.findViewById(R.id.title);
		mDate = (TextView) this.findViewById(R.id.date);
		mWeb = (WebView) this.findViewById(R.id.content);
		
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		mScrollView = (ScrollView) this.findViewById(R.id.scroll);
		
		showLoading(VIEW_LOADING);
		loadtechDetail();
	}
	
	/**
	 * 加载文章内容
	 */
	private void loadtechDetail(){
		Intent intent = getIntent();
		articleItem = (ArticleItem) intent.getSerializableExtra("ArticleItem");
		String id = articleItem.getId();
		
		RequestParams params = new RequestParams();
		String t = String.valueOf(System.currentTimeMillis() / 1000);	//获取当前时间
		params.put("timestamp", t);				//当前时间戳
		String token1 = MD5Utils.md5("d19cf361181f5a169c107872e1f5b722" + t);
		params.put("token1", token1);			//token1算法

		params.put("apiid", 3);
		params.put("module", "api_libraries_sjdbg_detail");
		params.put("returnformat", "json");
		params.put("encoding", "utf8");
		params.put("docid", id);			//文章id
//		params.put("uid", uid );			//用户id ，登陆的时候传，不登陆不传
		params.put("size", 500);			//文章内图片的尺寸可以传500 800默认为500
	
		AsyncHttpUtil.get(Contacts.PHONE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if ( arg2!=null && arg2.length>0 ) {
					try {
						JSONObject obj = new JSONObject(new String(arg2));
						String status = obj.getString("status");
						if ( TextUtils.equals("1", status) ) {
							showLoading(VIEW_CONTENT);
							
							JSONObject data = obj.getJSONObject("data");
							JSONObject info = data.getJSONObject("info");
							item = JsonUtils.getInstance(TextArticleItem.class, info);
							if ( item!=null ) {
								mTitle.setText(item.getTitle());
								mDate.setText(item.getDate());
								initWebView(item.getContent());
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
	 * 初始化WebView
	 * @param data
	 */
	@SuppressLint("SetJavaScriptEnabled") 
	private void initWebView(String data) {
		// 如果访问的页面中有Javascript，则webview必须设置支持Javascript
		mWeb.getSettings().setJavaScriptEnabled(true);
		mWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		mWeb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 触摸焦点起作用
		mWeb.requestFocus();
		// 取消滚动条
		mWeb.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWeb.setHorizontalScrollBarEnabled(false);//水平不显示
		mWeb.setVerticalScrollBarEnabled(false); //垂直不显示
		// 设置WevView要显示的网页：
//		mWeb.loadUrl(url);
		mWeb.loadDataWithBaseURL(null, data,"text/html", "UTF-8", null);
		// 设置不可缩放
		mWeb.getSettings().setSupportZoom(false);
		mWeb.getSettings().setBuiltInZoomControls(false);

		mWeb.setWebViewClient(new MyWebViewClient());
		mWeb.setWebChromeClient(new WebChromeClient());
//		mWeb.addJavascriptInterface(this, "todayinfo");
	}
	
	class MyWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (!TextUtils.isEmpty(url)) {
				Log.d("shouldOverrideUrlLoading", "onPageFinished");
				mWeb.loadUrl(url);
			}
			return true;
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			String title = view.getTitle();
			if( TextUtils.isEmpty(title) ){
				return;
			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Log.d("onReceivedError", "onReceivedError");
		}
	}
	
	/**
	 * 显示正在加载界面
	 * 
	 * @param i
	 */
	private void showLoading(int i){
		mErrorHintView.setVisibility(View.GONE);
		mScrollView.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			mScrollView.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadtechDetail();
				}
			});
			break;
			
		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadtechDetail();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_left:
			finish();
			break;
			
		case R.id.toolbar_top:
			mScrollView.scrollTo(0, 0);
			break;
			
		case R.id.toolbar_comment:
			Intent intent = new Intent(this, PhoneTechCommentActivity.class);
			intent.putExtra("docid", item.getDocid());
			startActivity(intent);
			break;
			
		case R.id.toolbar_save:
			List<Object> list = mUserController.getCollectionInfo();
			if ( list==null ) {
				list = new ArrayList<Object>();
				list.add(articleItem);
				mUserController.saveCollectionInfo(list);
				SuperUI.showCollectionUI(mContext);
			} else {
				if ( list.contains(articleItem) ) {
					list.remove(articleItem);
					mUserController.saveCollectionInfo(list);
					SuperUI.showUncollectionUI(mContext);
				} else {
					list.add(articleItem);
					mUserController.saveCollectionInfo(list);
					SuperUI.showCollectionUI(mContext);
				}
			}
			break;
			
		default:
			break;
		}
	}

}
