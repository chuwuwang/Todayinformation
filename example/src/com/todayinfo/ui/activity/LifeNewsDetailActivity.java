package com.todayinfo.ui.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.LifeNewsContent;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.Contacts;
import com.todayinfo.utils.SharedpreferncesUtil;

public class LifeNewsDetailActivity extends SuperActivity implements OnClickListener{
	
	public static final int TEXT_TYPE = 0;
	public static final int IMG_TYPE = 1;
	
	private RelativeLayout title_bar;
	private TextView time_smallfont;
	private TextView auther_smallfon;
	private TextView news_title_name;
	private ListView mListView;
	private DetailAdapter mAdapter;
	private ArrayList<LifeNewsContent> mLifeItem = new ArrayList<LifeNewsContent>();
	
	private LinearLayout menu_layout;
	private ImageButton back, comment;
	private ImageButton read_mode;
	private ImageButton change_text_size;
	
	private PopupWindow popupWindow;
	private SeekBar fontseek;
	private TextView textFont;
	
	public static final String NEWS_DETAIL = "http://content.2500city.com/news/Clientview/";
	private String newsId;
	
	private boolean readerMode; // 阅读模式（夜间，白天）
	private int fontsize = 17; // 字体大小
	
	private ErrorHintView mErrorHintView;
	
	public static int VIEW_LIST = 1;
	public static int VIEW_LOADING = 2;
	public static int VIEW_NODATA = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_life_news_detail);
		
		initView();
		showLoading(VIEW_LOADING);
		loadLifeNewsInfo();
	}
	
	/**
	 * 加载生活新闻信息
	 */
	public void loadLifeNewsInfo(){
		String url = NEWS_DETAIL + newsId;
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				try {
					if ( responseBody!=null && responseBody.length>0 ){
						ArrayList<LifeNewsContent> list = getNewsContent(new String(responseBody));
						mLifeItem.clear();
						if (list != null && list.size() > 0) {
							showLoading(VIEW_LIST);
							mLifeItem.addAll(list);
							notifyAdapter();
						} else {
							showLoading(VIEW_NODATA);
						}
					}
					
				}catch (Exception e) {
					e.printStackTrace();
					
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
		});
	}
	
	/**
	 * 解析新闻类容
	 * 
	 * @param result
	 * @return
	 */
	private ArrayList<LifeNewsContent> getNewsContent(String result) {
		ArrayList<LifeNewsContent> contents = new ArrayList<LifeNewsContent>();
		LifeNewsContent ncv = null;

		Document document = Jsoup.parse(result);
		Elements info = document.getElementsByTag("span");
		for (Element element : info) {
			ncv = new LifeNewsContent();
			ncv.setIsImg(0);
			ncv.setContentList(element.text());
			contents.add(ncv);
		}
		Elements elements = document.getElementsByTag("p");
		Elements media = document.select("[src]");

		int i = 1;
		for (Element element : elements) {

			if (element.hasText()) {
				ncv = new LifeNewsContent();
				ncv.setIsImg(0);
				ncv.setContentList(element.text());
				contents.add(ncv);
			} else {
				if (element.hasAttr("align") && media != null
						&& media.size() > 0 && media.size() > i) {
					Element src = media.get(i);
					if (src.tagName().equals("img")) {
						ncv = new LifeNewsContent();
						ncv.setIsImg(1);
						ncv.setContentList(src.attr("src"));
						contents.add(ncv);
					}
					i++;
				}
			}
		}

		return contents;
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		readerMode = SharedpreferncesUtil.getReadMode(mContext, Contacts.READER_MODE, false);
		fontsize = SharedpreferncesUtil.getFontSize(mContext, Contacts.FONT_SIZE, 17); // 初始化文字大小

		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});;
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("新闻追踪");
		
		Bundle myBundle = getIntent().getExtras();
		newsId = myBundle.getString("newsId");
		
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		mListView = (ListView) this.findViewById(R.id.listview_text);
		View head = View.inflate(mContext, R.layout.item_container_headview, null);
		title_bar = (RelativeLayout) head.findViewById(R.id.title_bar);
		time_smallfont = (TextView) head.findViewById(R.id.time_smallfont);
		auther_smallfon = (TextView) head.findViewById(R.id.auther_smallfon);
		news_title_name = (TextView) head.findViewById(R.id.news_title_name);
		mListView.addHeaderView(head);
		
		mAdapter = new DetailAdapter();
		mListView.setAdapter(mAdapter);
		
		// 初始化底部菜单
		menu_layout = (LinearLayout) this.findViewById(R.id.menu_layout);
		change_text_size = (ImageButton) this.findViewById(R.id.change_text_size);
		change_text_size.setOnClickListener(this);
		read_mode = (ImageButton) this.findViewById(R.id.read_mode);
		read_mode.setOnClickListener(this);
		back = (ImageButton) this.findViewById(R.id.back_img);
		back.setOnClickListener(this);
		comment = (ImageButton) this.findViewById(R.id.comment_img);
		comment.setOnClickListener(this);
		if (readerMode) {
			// 夜间模式
			readerModeNight();
		} else {
			readerMode();
		}
		
	}
	
	private void notifyAdapter() {
		if (mAdapter != null){
			time_smallfont.setText(mLifeItem.get(0).getContentList());
			auther_smallfon.setText(mLifeItem.get(1).getContentList());
			news_title_name.setText(mLifeItem.get(2).getContentList());
			mLifeItem.subList(0, 3).clear();
			mAdapter.notifyDataSetChanged();
		}
	}
	
	// 白天模式修改界面
	public void readerMode() {
		title_bar.setBackgroundColor(-1);
		mListView.setBackgroundColor(-1); // #000000
		time_smallfont.setTextColor(-13421773);
		news_title_name.setTextColor(-13421773);
		read_mode.setImageResource(R.drawable.bottom_menu_mode_light1);
		menu_layout.setBackgroundColor(getResources().getColor(R.color.menu_bottom_bg));
	}
	
	// 夜间模式修改界面
	public void readerModeNight() {
		title_bar.setBackgroundColor(-13947856);
		mListView.setBackgroundColor(-13947856); // #2b2c30
		time_smallfont.setTextColor(-7895161);
		news_title_name.setTextColor(-7895161);
		read_mode.setImageResource(R.drawable.bottom_menu_mode_light2);
		menu_layout.setBackgroundColor(-13947856);
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
			mErrorHintView.loadingData();
			break;
			
		case 3:
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_text_size: // 修改字体大小
			View layout = View.inflate(mContext, R.layout.pop_text_size, null);
			popupWindow = new PopupWindow(mContext);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
			popupWindow.setHeight(getWindowManager().getDefaultDisplay().getHeight() / 6);
			popupWindow.setAnimationStyle(R.style.AnimationPreview);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setFocusable(true);// 响应回退按钮事件
			popupWindow.setContentView(layout);

			int[] location = new int[2];
			v.getLocationOnScreen(location);
			popupWindow.showAtLocation(v.findViewById(R.id.change_text_size),
					Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());

			fontseek = (SeekBar) layout.findViewById(R.id.settings_font);
			fontseek.setMax(20);
			fontseek.setProgress(fontsize - 10);
			fontseek.setSecondaryProgress(0);
			textFont = (TextView) layout.findViewById(R.id.fontSub);
			textFont.setText(fontsize + "");
			fontseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					fontsize = progress + 10;
					textFont.setText("" + fontsize);
					if (mAdapter != null)
						mAdapter.notifyDataSetChanged();
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					SharedpreferncesUtil.putFontSize(mContext, Contacts.FONT_SIZE, fontsize);
				}
			});
			break;
			
		case R.id.read_mode:
			if ( readerMode ) {
				readerMode = false;
				readerMode();
				if (mAdapter != null)
					mAdapter.notifyDataSetChanged();
				// 保存数据
				SharedpreferncesUtil.putReadMode(mContext, Contacts.READER_MODE, false);
			} else {
				readerMode = true;
				readerModeNight();
				if (mAdapter != null)
					mAdapter.notifyDataSetChanged();
				// 保存数据
				SharedpreferncesUtil.putReadMode(mContext, Contacts.READER_MODE, true);
			}
			break;
			
		case R.id.back_img:
			finish();
			break;
			
		case R.id.comment_img:
			Intent inte = new Intent(Intent.ACTION_SEND);    
			inte.setType("image/*");    
            inte.putExtra(Intent.EXTRA_SUBJECT, "Share");    
            inte.putExtra(Intent.EXTRA_TEXT,  "I would like to share this with you...");    
            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
            startActivity(Intent.createChooser(inte, "i love you"));
			break;
		}

	}
	
	class DetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mLifeItem == null ? 0 : mLifeItem.size();
		}

		@Override
		public Object getItem(int position) {
			return mLifeItem == null ? null : mLifeItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public boolean isEnabled(int position) {
			return false;
		}

		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public int getItemViewType(int position) {
			LifeNewsContent vo = (LifeNewsContent) getItem(position);
			return vo.getIsImg();
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final LifeNewsContent item = (LifeNewsContent) getItem(position);
			int type = getItemViewType(position);// 获取当前位置对应的类别
			if (convertView == null) {
				switch (type) {
				case TEXT_TYPE:
					convertView = View.inflate(mContext, R.layout.item_news_content_textview, null);
					break;
				case IMG_TYPE:
					convertView = View.inflate(mContext, R.layout.item_news_content_image, null);
					break;
				}
			}
			if (item != null) {
				switch (type) {
				case TEXT_TYPE:
					// 对应设置文字内容F
					TextView tv = (TextView) convertView.findViewById(R.id.content_textView1);
					String text = item.getContentList();
					tv.setTextSize(fontsize);
					if (readerMode) {
						tv.setTextColor(-7895161);// #878787
					} else {
						tv.setTextColor(-13421773);
					}
					tv.setText(Html.fromHtml(text));
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					break;
				case IMG_TYPE:
					// 加载图片
					ImageView iv = (ImageView) convertView.findViewById(R.id.content_imageView1);
					String url = item.getContentList();
					if (item.getContentList().startsWith("http:")) {
						url = item.getContentList();
					} else {
						url = "http://content.2500city.com" + item.getContentList();
					}
					if ( !TextUtils.isEmpty(url) ) {
						Picasso.with(mContext).load(url).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(iv);
					}
					break;
				default:
					break;
				}
			}
			return convertView;
		}

	}
	
}
