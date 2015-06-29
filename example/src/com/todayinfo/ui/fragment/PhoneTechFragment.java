package com.todayinfo.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.squareup.picasso.Picasso;
import com.todayinfo.model.ArticleItem;
import com.todayinfo.model.FocusPicture;
import com.todayinfo.model.PhonePaging;
import com.todayinfo.ui.activity.PhoneTechDetailActivity;
import com.todayinfo.ui.adapter.IndexIntoAdapter;
import com.todayinfo.ui.api.DataTask;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.ui.component.jazzviewpager.JazzyViewPager;
import com.todayinfo.ui.component.jazzviewpager.JazzyViewPager.TransitionEffect;
import com.todayinfo.ui.component.jazzviewpager.OutlineContainer;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.Contacts;
import com.todayinfo.utils.JsonUtils;
import com.todayinfo.utils.MD5Utils;

/**
 * 科技资讯
 * 
 * @author zhou.ni 2015年3月18日
 */
public class PhoneTechFragment extends SuperFragment {

	private View view;
	private JazzyViewPager mViewPager;
	private LinearLayout mIndicator;
	private PullToRefreshListView mListView;
	private ErrorHintView mErrorHintView;
	private RelativeLayout mIndexrl;
	private ImageView mIndexImage;
	
	private MyPagerAdapter pagerAdapter;
	private List<FocusPicture> picturesList = new ArrayList<FocusPicture>();  //广告界面的数据源
	/** 装指引的ImageView数组 **/
	private ImageView[] mIndicators;
	/** 装ViewPager中ImageView的数组 **/
	private ImageView[] mImageViews;
	/** 图片自动切换时间 */
	private static final int PHOTO_CHANGE_TIME = 2000;
	private static final int MSG_CHANGE_PHOTO = 1;
	
	private List<ArticleItem> ArticleData = new ArrayList<ArticleItem>();
	private IndexIntoAdapter indexAdapter;
 	
	private boolean isViewPager = true;
	private int offset = 0;				//第N条数据
	private boolean isPage = true;		//是否还有下一页
	private LinearLayout footerView;    //最后一条listview
	
	public static int VIEW_LIST = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	public static int VIEW_INDEXIMAGE = 5;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = mViewPager.getCurrentItem();
				if (index == picturesList.size() - 1) {
					index = -1;
				}
				mViewPager.setCurrentItem(index + 1);
				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
			}
		}
		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_phone_tech, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		mViewPager = (JazzyViewPager) view.findViewById(R.id.phone_viewpager);
		mIndicator = (LinearLayout) view.findViewById(R.id.indicator);
		mListView = (PullToRefreshListView) view.findViewById(R.id.list);
		mErrorHintView = (ErrorHintView) view.findViewById(R.id.hintView);
		mIndexrl = (RelativeLayout) view.findViewById(R.id.index_bg);
		mIndexImage = (ImageView) view.findViewById(R.id.index_image);
		mIndexrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showLoading(VIEW_LIST);
			}
		});
		
		setupIdleListener();
		
		showLoading(VIEW_LOADING);
		refreshData(true);
	}
	
	/**
	 *  初始化ViewPager
	 */
	private void initViewPager(){
		mIndicators = new ImageView[picturesList.size()];
		if (picturesList.size() <= 1) {
			mIndicator.setVisibility(View.GONE);
		}
		
		for (int i = 0; i < mIndicators.length; i++) {
			ImageView imageView = new ImageView(mContext);
			LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
			if (i != 0) {
				params.leftMargin = 5;
			}
			imageView.setLayoutParams(params);
			mIndicators[i] = imageView;
			if (i == 0) {
				mIndicators[i].setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mIndicators[i].setBackgroundResource(R.drawable.android_activities_gap);
			}

			mIndicator.addView(imageView);
		}
		
		mImageViews = new ImageView[picturesList.size()];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
		
		mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		mViewPager.setCurrentItem(0);
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);

		pagerAdapter = new MyPagerAdapter();
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				setImageBackground(arg0);	
				final FocusPicture item = picturesList.get(arg0);
				mImageViews[arg0].setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						if ( !TextUtils.isEmpty(item.getPicUrl()) ) {
							showProgressDialog();
							showLoading(VIEW_INDEXIMAGE);
							Picasso.with(mContext).load(item.getPicUrl()).placeholder(R.color.ECECEC)
												  .error(R.color.ECECEC).into(mIndexImage);
							dismissProgressDialog();
						}
					}
					
				});
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (picturesList.size() == 0 || picturesList.size() == 1)
					return true;
				else
					return false;
			}
		});
		
	}
	
	/**
	 * 初始化listview
	 */
	private void setupIdleListener() {
		indexAdapter = new IndexIntoAdapter(ArticleData, mActivity);
		mListView.setAdapter(indexAdapter);
		
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					defParams();
					showLoading(VIEW_LOADING);
					refreshData(true);
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArticleItem item = ArticleData.get(position-1);
				if ( item!=null && item.getType().equals("wz") ) {
					Intent intent = new Intent(mContext, PhoneTechDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ArticleItem", item);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
					
			}
		});
		
		mListView.setOnPreRefreshingAnimListener(new OnPreRefreshingAnimListener() {
			@Override
			public void onPreRefreshingAnim() {
				mListView.setFooterLoadingViewHeaderText("加载更多资讯");
			}
		});
		
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if ( isPage ) {
					//滑动到底部自动刷新
					refreshData(false);
				} else {
					//没有下一页了
					mListView.onRefreshComplete();
					if ( footerView == null ) {
						footerView = (LinearLayout) View.inflate(mContext, R.layout.item_pull_to_refresh_footer, null);
						TextView footer = (TextView) footerView.findViewById(R.id.footer);
						footer.setText("到底了呢-共" + ArticleData.size() + "条资讯");
						mListView.getRefreshableView().addFooterView(footerView);  
					}
				}
			}
		});
	}
	
	private void refreshData(boolean clean) {
		dataTask = new RefreshDataTask(dataTask.getID() + 1, clean);
		dataTask.run();
	}
	
	class RefreshDataTask extends DataTask {
		
		boolean clean = false;		// 是否清空原数据
		public RefreshDataTask(int id, boolean flag) {
			super(id);
			this.clean = flag;
		}

		@Override
		public void run() {
			// 判断是不是用户的最后操作,最后任务的ID如果比此任务的ID大则丢弃请求结果
			if (getID() < dataTask.getID()) {
				return;
			} else {
				loadIndexInfo(clean);
			}
			
		}

	}
	
	/**
	 * 加载首页内容
	 * @param flag
	 */
	private void loadIndexInfo(final boolean flag){
		RequestParams params = new RequestParams();
		String t = String.valueOf(System.currentTimeMillis() / 1000);	//获取当前时间
		params.put("timestamp", t);				//当前时间戳
		String token1 = MD5Utils.md5("d19cf361181f5a169c107872e1f5b722" + t);
		params.put("token1", token1);			//token1算法

		params.put("apiid", 3);
		params.put("module", "api_libraries_sjdbg_indexrecommend");
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
							
							//广告图片
							JSONObject data = obj.getJSONObject("data");
							JSONArray focusPicture = data.getJSONArray("focusPicture");
							if ( isViewPager ) {
								if ( focusPicture!=null ) {
									List<FocusPicture> instance = JsonUtils.getInstance(FocusPicture.class, focusPicture);
									if ( instance!=null && instance.size()>0){
										picturesList.addAll(instance);
										initViewPager();
										isViewPager = false;
									}
								}
							} 
							
							//文章列表
							JSONObject articleList = data.getJSONObject("articleList");
							JSONArray list = articleList.getJSONArray("list");
							if ( list!=null ) {
								List<ArticleItem> instance = JsonUtils.getInstance(ArticleItem.class, list);
								if ( instance!=null && instance.size()>0) {
									if ( flag ) {
										ArticleData.clear();
									} 
									ArticleData.addAll(instance);
									showLoading(VIEW_LIST);
									indexAdapter.notifyDataSetChanged();
								} else {
									showLoading(VIEW_LOADFAILURE);
								}
							}
							
							JSONObject paging = articleList.getJSONObject("paging");
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
	
	private void showLoading(int i){
		mErrorHintView.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		mViewPager.setVisibility(View.GONE);
		mIndicator.setVisibility(View.GONE);
		mIndexrl.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			mViewPager.setVisibility(View.VISIBLE);
			mIndicator.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					defParams();
					showLoading(VIEW_LOADING);
					refreshData(true);
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
					refreshData(true);
				}
			});
			break;
			
		case 4:
			mErrorHintView.loadingData();
			break;
			
		case 5:
			mErrorHintView.hideLoading();
			mIndexrl.setVisibility(View.VISIBLE);
			break;
			
		}
	}
	
	/**
	 * 恢复默认参数
	 */
	private void defParams(){
		offset = 0;
		isPage = true;
	}
	
	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < mIndicators.length; i++) {
			if (i == selectItemsIndex) {
				mIndicators[i]
						.setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mIndicators[i]
						.setBackgroundResource(R.drawable.android_activities_gap);
			}
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
	
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViewPager
					.findViewFromObject(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			FocusPicture item = picturesList.get(position);
			if ( !TextUtils.isEmpty(item.getPicUrl()) ){
				Picasso.with(mContext).load(item.getPicUrl()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(mImageViews[position]);
			} 
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			return mImageViews[position];
		}

	}
	
	
	
}
