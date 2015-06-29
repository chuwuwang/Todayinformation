package com.todayinfo.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.todayinfo.model.PhonePhotoItem;
import com.todayinfo.model.PhonePaging;
import com.todayinfo.ui.activity.PhonePhotoDetailActivity;
import com.todayinfo.ui.api.DataTask;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.Contacts;
import com.todayinfo.utils.JsonUtils;
import com.todayinfo.utils.MD5Utils;

/**
 * 手机美图
 * 
 * @author zhou.ni 2015年5月5日
 */
public class PhonePhotoFragment extends SuperFragment {

	private View view;
	private PullToRefreshListView mListView;
	private ErrorHintView mErrorHintView;
	private List<PhonePhotoItem> photoList = new ArrayList<PhonePhotoItem>();
	private PhonePhotoAdapter photoAdapter;
	
	private int offset = 0;				//第N条数据
	private boolean isPage = true;		//是否还有下一页
	private LinearLayout footerView;    //最后一条listview
	
	public static int VIEW_LIST = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view  = inflater.inflate(R.layout.fm_phonephoto, container, false);
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
		mListView = (PullToRefreshListView) view.findViewById(R.id.phone_photo_list);
		mErrorHintView = (ErrorHintView) view.findViewById(R.id.hintView);
		photoAdapter = new PhonePhotoAdapter();
		mListView.setAdapter(photoAdapter);
		setupIdleListener();
		showLoading(VIEW_LOADING);
		refreshData(true);
	}
	
	protected void setupIdleListener() {
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					defParams();
					refreshData(true);
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PhonePhotoItem item = photoList.get(position-1);
				Intent intent = new Intent(mContext, PhonePhotoDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("PhonePhotoItem", item);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
					
			}
		});
		
		mListView.setOnPreRefreshingAnimListener(new OnPreRefreshingAnimListener() {
			@Override
			public void onPreRefreshingAnim() {
				mListView.setFooterLoadingViewHeaderText("加载更多图片");
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
						footer.setText("到底了呢-共" + photoList.size() + "条资讯");
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
				loadPhonePhotoInfo(clean);
			}
			
		}

	}
	
	/**
	 * 加载最新的手机资讯图片
	 * @param clean
	 */
	private void loadPhonePhotoInfo(final boolean clean){
		RequestParams params = new RequestParams();
		String t = String.valueOf(System.currentTimeMillis() / 1000);	//获取当前时间
		params.put("timestamp", t);				//当前时间戳
		String token1 = MD5Utils.md5("d19cf361181f5a169c107872e1f5b722" + t);
		params.put("token1", token1);			//token1算法

		params.put("apiid", 3);
		params.put("module", "api_libraries_sjdbg_aimeizhi");
		params.put("returnformat", "json");
		params.put("encoding", "utf8");
//		params.put("onetime", timeStamp);			//分页用，传第一条新闻的时间戳，第一页下不用传值。
		params.put("offset", offset );				//分页用，数据记录的起始行数。0为第一条记录。
		params.put("rows", 10);						//分页用，数据记录每次取得的行数。不传此参数则默认获取10条记录。
		
		AsyncHttpUtil.get(Contacts.PHONE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				if ( footerView!=null ){
					mListView.getRefreshableView().removeFooterView(footerView);
					footerView = null;
				}
				
				mListView.onRefreshComplete();
				
				if ( responseBody!=null && responseBody.length>0 ){
					try {
						JSONObject object = new JSONObject(new String(responseBody));
						String status = object.getString("status");
						if ( TextUtils.equals("1", status) ) {
							offset += 10;
							JSONObject data = object.getJSONObject("data");
							JSONArray result = data.getJSONArray("result");
							JSONObject paging = data.getJSONObject("paging");
							if ( result!=null ){
								List<PhonePhotoItem> list = JsonUtils.getInstance(PhonePhotoItem.class, result);
								if ( list!=null && list.size()>0) {
									if ( clean ) {
										photoList.clear();
									} 
									photoList.addAll(list);
									showLoading(VIEW_LIST);
									photoAdapter.notifyDataSetChanged();
								} else {
									showLoading(VIEW_LOADFAILURE);
								}
							} else {
								showLoading(VIEW_LOADFAILURE);
							}
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
								
						} 
						
					} catch (Exception e) {
						showLoading(VIEW_LOADFAILURE);
					}
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
	
	/**
	 * 恢复默认参数
	 */
	private void defParams(){
		offset = 0;
		isPage = true;
	}
	
	class PhonePhotoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return photoList.size();
		}

		@Override
		public Object getItem(int position) {
			return photoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ViewHolder holder = null;;
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if ( convertView == null ) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_phone_photo_lv, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.text = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			PhonePhotoItem item = photoList.get(position);
			holder.item = item;
			
			if ( !TextUtils.isEmpty(item.getPic()) ){
				Picasso.with(mContext).load(item.getPic()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(holder.img);
			} 
			
			if ( !TextUtils.isEmpty(item.getTitle()) ) {
				holder.text.setText(item.getTitle());
			} else {
				holder.text.setText("");
			}
			
			return convertView;
		}
		
	}
	
	static class ViewHolder {
		ImageView img;
		TextView text;
		
		PhonePhotoItem item;
	}

}
