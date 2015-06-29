package com.todayinfo.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.GameDatail;
import com.todayinfo.model.GameDetailItem;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.JsonUtils;

/**
 * 游戏详情界面
 * 
 * @author zhou.ni 2015年5月17日
 */
public class GameDetailActivity extends SuperActivity {
	
	private static final String URL_PATH = "http://www.gamept.cn/yx_zt.php?ztid=";
	
	private ErrorHintView mErrorHintView;
//	private ListView mListView;
	private LinearLayout containLayout;
	private ImageView mImageView;
	private TextView mTitle;
	private TextView mIntro;
	
//	private GameDetailAdapter adapter;
	private List<GameDetailItem> mList = new ArrayList<GameDetailItem>();
	
	public static int VIEW = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	private String ztid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_detail);
		
		initView();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		Intent intent = getIntent();
		ztid = intent.getStringExtra("ztid");
		
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("游戏专题");
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
//		mListView = (ListView) this.findViewById(R.id.list);
		containLayout = (LinearLayout) this.findViewById(R.id.list);
		mTitle = (TextView) this.findViewById(R.id.name);
		mImageView = (ImageView) this.findViewById(R.id.image);
		mIntro = (TextView) this.findViewById(R.id.intro);
		
//		ScrollViewUtils.setListViewHeightBasedOnChildren(mListView);
//		adapter = new GameDetailAdapter();
//		mListView.setAdapter(adapter);
		
		showLoading(VIEW_LOADING);
		loadGameDetailData();
	}
	
	/**
	 * 加载美女详情的图片
	 */
	private void loadGameDetailData(){
		if ( containLayout != null ) {
			containLayout.removeAllViews();
		}
		
		String url = URL_PATH + ztid;
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				try {
					if ( responseBody!=null && responseBody.length>0 ){
						JSONObject obj = new JSONObject(new String(responseBody));
						JSONArray array = obj.getJSONArray("items");
						GameDatail gameDatail = JsonUtils.getInstance(GameDatail.class, obj);
						showLoading(VIEW);
						if ( gameDatail!=null ){
							if ( !TextUtils.isEmpty(gameDatail.getZtimg()) ){
								Picasso.with(mContext).load("http://www.gamept.cn/" + gameDatail.getZtimg()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(mImageView);
							} 
							mTitle.setText(gameDatail.getZtname());
							mIntro.setText(gameDatail.getIntro());
						} 
						
						List<GameDetailItem> list = JsonUtils.getInstance(GameDetailItem.class, array);
						if ( list!=null && list.size()>0 ){
							mList.clear();
							mList.addAll(list);
//							adapter.notifyDataSetChanged();
							addListView();
						}
					} else {
						showLoading(VIEW_LOADFAILURE);
					}
					
				}catch (Exception e) {
					e.printStackTrace();
					showLoading(VIEW_LOADFAILURE);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				showLoading(VIEW_WIFIFAILUER);
			}
		
		});
	}
	
	/**
	 * 添加游戏列表
	 */
	private void addListView(){
		for (int i = 0; i < mList.size(); i++) {
			View view = View.inflate(mContext, R.layout.item_game_detail_lv, null);
			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			TextView name = (TextView) view.findViewById(R.id.name);
			ImageView star = (ImageView) view.findViewById(R.id.star);
			TextView people = (TextView) view.findViewById(R.id.people);
			TextView download = (TextView) view.findViewById(R.id.download);
			TextView size = (TextView) view.findViewById(R.id.size);
		
			final GameDetailItem item = mList.get(i);
			if ( !TextUtils.isEmpty(item.getIcon()) ){
				Picasso.with(mContext).load("http://www.gamept.cn/" + item.getIcon()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(icon);
			} 
			
			name.setText(item.getTitle());
			
			if ( !TextUtils.isEmpty(item.getStar()) ){
				setPraiseRate(star, item.getStar());
			} else {
				star.setImageResource(R.drawable.start5);
			}
			
			people.setText(item.getTotaldown()+"次");
			
			size.setText(item.getFilesize());
			
			download.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();        
			        intent.setAction("android.intent.action.VIEW");    
			        Uri content_url = Uri.parse("http://www.gamept.cn/" + item.getFlashurl());   
			        intent.setData(content_url);  
			        startActivity(intent);
				}
			});
			
			containLayout.addView(view);
		}
	}
	
	/**
	 * 评分星级
	 * 
	 * @param img
	 * @param score
	 */
	private void setPraiseRate(ImageView img, String score) {
		if( score.endsWith("0") ){
			img.setImageResource(R.drawable.start0);
		}else if( score.endsWith("0.5") ){
			img.setImageResource(R.drawable.start0_5);
		}else if( score.endsWith("1") ){
			img.setImageResource(R.drawable.start1);
		}else if( score.endsWith("1.5") ){
			img.setImageResource(R.drawable.start1_5);
		}else if( score.endsWith("2") ){
			img.setImageResource(R.drawable.start2);
		}else if( score.endsWith("2.5") ){
			img.setImageResource(R.drawable.start2_5);
		}else if( score.endsWith("3") ){
			img.setImageResource(R.drawable.start3);
		}else if( score.endsWith("3.5") ){
			img.setImageResource(R.drawable.start3_5);
		}else if( score.endsWith("4") ){
			img.setImageResource(R.drawable.start4);
		}else if( score.endsWith("4.5") ){
			img.setImageResource(R.drawable.start4_5);
		}else if( score.endsWith("5") ){
			img.setImageResource(R.drawable.start5);
		} else {
			img.setImageResource(R.drawable.start5);
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
	
	/**
	 * 显示动态加载界面
	 * 
	 * @param i
	 */
	private void showLoading(int i){
		mErrorHintView.setVisibility(View.GONE);
		mImageView.setVisibility(View.GONE);
		mTitle.setVisibility(View.GONE);
		mIntro.setVisibility(View.GONE);
//		mListView.setVisibility(View.GONE);
		containLayout.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			containLayout.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.VISIBLE);
			mTitle.setVisibility(View.VISIBLE);
			mIntro.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadGameDetailData();
				}
			});
			break;
			
		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadGameDetailData();
				}
			});
			break;
			
		case 4:
			mErrorHintView.loadingData();
			break;
		}
	}
	
	class GameDetailAdapter extends BaseAdapter {

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
			if ( convertView==null ) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_game_detail_lv, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.star = (ImageView) convertView.findViewById(R.id.star);
				holder.people = (TextView) convertView.findViewById(R.id.people);
				holder.download = (TextView) convertView.findViewById(R.id.download);
				holder.size = (TextView) convertView.findViewById(R.id.size);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
				
			GameDetailItem item = mList.get(position);
			if ( !TextUtils.isEmpty(item.getIcon()) ){
				Picasso.with(mContext).load("http://www.gamept.cn/" + item.getIcon()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(holder.icon);
			} 
			
			holder.name.setText(item.getTitle());
			
			if ( !TextUtils.isEmpty(item.getStar()) ){
				setPraiseRate(holder.star, item.getStar());
			} else {
				holder.star.setImageResource(R.drawable.start5);
			}
			
			holder.people.setText(item.getTotaldown()+"次");
			
			holder.size.setText(item.getFilesize());
			
			return convertView;
		}
		
		private void setPraiseRate(ImageView img, String score) {
			if( score.endsWith("0") ){
				img.setImageResource(R.drawable.start0);
			}else if( score.endsWith("0.5") ){
				img.setImageResource(R.drawable.start0_5);
			}else if( score.endsWith("1") ){
				img.setImageResource(R.drawable.start1);
			}else if( score.endsWith("1.5") ){
				img.setImageResource(R.drawable.start1_5);
			}else if( score.endsWith("2") ){
				img.setImageResource(R.drawable.start2);
			}else if( score.endsWith("2.5") ){
				img.setImageResource(R.drawable.start2_5);
			}else if( score.endsWith("3") ){
				img.setImageResource(R.drawable.start3);
			}else if( score.endsWith("3.5") ){
				img.setImageResource(R.drawable.start3_5);
			}else if( score.endsWith("4") ){
				img.setImageResource(R.drawable.start4);
			}else if( score.endsWith("4.5") ){
				img.setImageResource(R.drawable.start4_5);
			}else if( score.endsWith("5") ){
				img.setImageResource(R.drawable.start5);
			} else {
				img.setImageResource(R.drawable.start5);
			}
		}
		
	}
	
	static class ViewHolder {
		ImageView icon;
		TextView name;
		ImageView star;
		TextView people;
		TextView download;
		TextView size;
	}
	
}
