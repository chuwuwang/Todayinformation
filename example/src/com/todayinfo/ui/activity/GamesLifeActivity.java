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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.GameItem;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.JsonUtils;

/**
 * 游戏人生
 * 
 * @author zhou.ni 2015年3月17日
 */
public class GamesLifeActivity extends SuperActivity {
	
	private PullToRefreshListView gameListView;
	private ErrorHintView mErrorHintView;
	private List<GameItem> mList = new ArrayList<GameItem>();
	private GameListAdapter gameListAdapter;
	
	private static final String URL_PATH = "http://www.gamept.cn/yx_zt.php?currentPage=?";
	
	public static int VIEW_LIST = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		getWindow().setBackgroundDrawable(null);
		
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("最新火爆游戏");
		
		gameListView = (PullToRefreshListView) this.findViewById(R.id.list);
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		
		setupIdleListener();
		
		showLoading(VIEW_LOADING);
		loadGameInfo(true);
	}
	
	protected void setupIdleListener() {
		gameListAdapter = new GameListAdapter();
		gameListView.setAdapter(gameListAdapter);
		
		gameListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					loadGameInfo(true);
				}
			}
		});
		
		gameListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GameItem item = mList.get(position-1);
				Intent intent = new Intent(mContext, GameDetailActivity.class);
				intent.putExtra("ztid", item.getZtid());
				intent.putExtra("ztname", item.getZtname());
				mContext.startActivity(intent);
					
			}
		});
	}
	
	public void loadGameInfo(final boolean clean){
		AsyncHttpUtil.get(URL_PATH, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				gameListView.onRefreshComplete();	
				
				try {
					if ( responseBody!=null && responseBody.length>0 ){
						JSONObject object = new JSONObject(new String(responseBody));
						JSONArray array = object.getJSONArray("items");
						if ( array!=null ){
							List<GameItem> list = JsonUtils.getInstance(GameItem.class, array);
							if ( list!=null && list.size()>0) {
								if ( clean ) {
									mList.clear();
								} 
								mList.addAll(list);
								showLoading(VIEW_LIST);
								gameListAdapter.notifyDataSetChanged();
							} else {
								showLoading(VIEW_LOADFAILURE);
							}
						} else {
							showLoading(VIEW_LOADFAILURE);
						}
					}
					
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
	
	private void showLoading(int i){
		mErrorHintView.setVisibility(View.GONE);
		gameListView.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			gameListView.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadGameInfo(true);
				}
			});
			break;
			
		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadGameInfo(true);
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
	
	class GameListAdapter extends BaseAdapter {

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
			ViewHolder holder;
			if ( convertView == null ) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_game_lv, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.text = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			GameItem item = mList.get(position);
			
			if ( !TextUtils.isEmpty(item.getZtimg()) ){
				Picasso.with(mContext).load("http://www.gamept.cn/" + item.getZtimg()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(holder.img);
			} 
			
			if ( !TextUtils.isEmpty(item.getZtname()) ) {
				holder.text.setText(item.getZtname());
			} else {
				holder.text.setText("");
			}
			
			return convertView;
		}
		
	}
	
	static class ViewHolder {
		ImageView img;
		TextView text;
	}

}
