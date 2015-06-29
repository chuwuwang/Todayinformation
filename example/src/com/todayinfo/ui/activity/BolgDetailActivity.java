package com.todayinfo.ui.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.todayinfo.model.BlogItem;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.SuperUI;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;

/**
 * 博客详情
 * 
 * @author zhou.ni 2015年5月17日
 */
public class BolgDetailActivity extends SuperActivity implements OnClickListener{

	private WebView mWeb;
	private TextView title;
	private ErrorHintView mErrorHintView;
	
	private BlogItem item;
	private String PATH = "http://wcf.open.cnblogs.com/blog/post/body/";
	
	public static int VIEW = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		getWindow().setBackgroundDrawable(null);

		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = this.getIntent(); 
		item = (BlogItem) intent.getSerializableExtra("item");
		if ( item!=null ) {
			title.setText(item.getTitle());
			url = PATH + item.getId();
			loadBolgDetailInfo(url);
		}
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		RelativeLayout leftBack = (RelativeLayout) headView.findViewById(R.id.back_left);
		title = (TextView) headView.findViewById(R.id.head_title);
		mWeb = (WebView) this.findViewById(R.id.webview);
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		LinearLayout top = (LinearLayout) this.findViewById(R.id.toolbar_top);
		LinearLayout comment = (LinearLayout) this.findViewById(R.id.toolbar_comment);
		LinearLayout save = (LinearLayout) this.findViewById(R.id.toolbar_save);
		leftBack.setOnClickListener(this);
		top.setOnClickListener(this);
		comment.setOnClickListener(this);
		save.setOnClickListener(this);
		ImageView saveImage = (ImageView) this.findViewById(R.id.save_image);
		TextView saveText = (TextView) this.findViewById(R.id.save_text);
		saveImage.setImageResource(R.drawable.toolbar_favorite);
		saveText.setText("收藏");
		
		Intent intent = this.getIntent(); 
		item = (BlogItem) intent.getSerializableExtra("item");
		if ( item!=null ) {
			title.setText(item.getTitle());
			url = PATH + item.getId();
			showLoading(VIEW_LOADING);
			loadBolgDetailInfo(url);
		}
		
 	}
	
	/**
	 * 加载详情内容
	 * @param url
	 */
	private void loadBolgDetailInfo(String url){
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				if ( responseBody!=null && responseBody.length>0 ){
					showLoading(VIEW);
					ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBody);
					String result = parseDetailContentXml(inputStream);
					initWebView(result);
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
	
	private String parseDetailContentXml(InputStream in) {
		String result = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(in, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("string".equals(parser.getName())){
						result = parser.nextText();
					}
					break;
				case XmlPullParser.TEXT:
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
	 * 显示动态加载界面
	 * 
	 * @param i
	 */
	private void showLoading(int i){
		mErrorHintView.setVisibility(View.GONE);
		mWeb.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			mWeb.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadBolgDetailInfo(url);
				}
			});
			break;
			
		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadBolgDetailInfo(url);
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
	public void pwdError() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_left:
			finish();
			break;
		case R.id.toolbar_top:
			mWeb.scrollTo(0, 0);
			break;
		case R.id.toolbar_comment:
			Intent intent = new Intent(mContext, BlogsCommentActivity.class);
			intent.putExtra("id", item.getId());
			intent.putExtra("title", item.getTitle());
			startActivity(intent);
			break;
		case R.id.toolbar_save:
			List<Object> list = mUserController.getCollectionInfo();
			if ( list==null ) {
				list = new ArrayList<Object>();
				list.add(item);
				mUserController.saveCollectionInfo(list);
				SuperUI.showCollectionUI(mContext);
			} else {
				if ( list.contains(item) ) {
					list.remove(item);
					mUserController.saveCollectionInfo(list);
					SuperUI.showUncollectionUI(mContext);
				} else {
					list.add(item);
					mUserController.saveCollectionInfo(list);
					SuperUI.showCollectionUI(mContext);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void obtainInfo() {
		
	}

}
