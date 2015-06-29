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
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.PhonePhotoItem;
import com.todayinfo.model.PhotoDetailedItem;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.ui.component.SuperUI;
import com.todayinfo.ui.component.ErrorHintView.OperateListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.Contacts;
import com.todayinfo.utils.MD5Utils;

/**
 * 手机图片详情界面
 * 
 * @author zhou.ni 2015年5月9日
 */
@SuppressWarnings("deprecation")
public class PhonePhotoDetailActivity extends SuperActivity implements OnClickListener{
	
	private Gallery mGallery;
	private TextView mContent;
	private TextView mTitle;
	private TextView mCount;
	private LinearLayout mPanel;
	private RelativeLayout mRelat;
	private ErrorHintView mErrorHintView;
	
	private List<String> picUrlList = new ArrayList<String>();
	private PhotoDetailedItem mItem;
	private ShowImageAdapter adapter;
	
	public static int VIEW_CONTENT = 1;
	/**显示断网**/
	public static int VIEW_WIFIFAILUER = 2;
	/** 显示加载数据失败 **/
	public static int VIEW_LOADFAILURE = 3;
	public static int VIEW_LOADING = 4;
	
	private String docid;
	private PhonePhotoItem photoItem = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_photo_detail); 
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
		title.setText("手机美图");
		LinearLayout share = (LinearLayout) this.findViewById(R.id.toolbar_share);
		LinearLayout comment = (LinearLayout) this.findViewById(R.id.toolbar_comment);
		LinearLayout save = (LinearLayout) this.findViewById(R.id.toolbar_save);
		leftBack.setOnClickListener(this);
		share.setOnClickListener(this);
		comment.setOnClickListener(this);
		save.setOnClickListener(this);
		
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		mGallery = (Gallery) this.findViewById(R.id.photo_detail_gallery);
		mContent = (TextView) this.findViewById(R.id.photo_detail_content);
		mCount = (TextView) this.findViewById(R.id.photo_detail_count);
		mTitle = (TextView) this.findViewById(R.id.photo_detail_title);
		mPanel = (LinearLayout) this.findViewById(R.id.ll);
		mRelat = (RelativeLayout) this.findViewById(R.id.rl);
	
		adapter = new ShowImageAdapter();
		mGallery.setAdapter(adapter);
		
		showLoading(VIEW_LOADING);
		loadphoneDetail();
		
	}
	
	/**
	 * 加载图片内容
	 */
	private void loadphoneDetail(){
		Intent intent = getIntent();
		photoItem = (PhonePhotoItem) intent.getSerializableExtra("PhonePhotoItem");
		docid = photoItem.getDocid();
		
		RequestParams params = new RequestParams();
		String t = String.valueOf(System.currentTimeMillis() / 1000);	//获取当前时间
		params.put("timestamp", t);				//当前时间戳
		String token1 = MD5Utils.md5("d19cf361181f5a169c107872e1f5b722" + t);
		params.put("token1", token1);			//token1算法

		params.put("apiid", 3);
		params.put("module", "api_libraries_sjdbg_detail");
		params.put("returnformat", "json");
		params.put("encoding", "utf8");
		params.put("docid", docid);			//文章id
//		params.put("uid", uid );			//用户id ，登陆的时候传，不登陆不传
		params.put("size", 800);			//文章内图片的尺寸可以传500 800默认为500
	
		AsyncHttpUtil.get(Contacts.PHONE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if ( arg2!=null && arg2.length>0 ) {
					try {
						JSONObject obj = new JSONObject(new String(arg2));
						String status = obj.getString("status");
						if ( TextUtils.equals("1", status) ) {
							showLoading(VIEW_CONTENT);
							mItem = new PhotoDetailedItem();
							
							JSONObject data = obj.getJSONObject("data");
							JSONObject info = data.getJSONObject("info");
							
							String cid = info.getString("cid");
							String isCollected = info.getString("isCollected");
							String title = info.getString("title");
							String date = info.getString("date");

							String author = info.getString("author");
							String docUrl = info.getString("docUrl");
							mItem.setAuthor(author);
							mItem.setDate(date);
							mItem.setIsCollected(isCollected);
							mItem.setTitle(title);
							mItem.setCid(cid);
							mItem.setDocUrl(docUrl);
							
							mTitle.setText(title);
							
							JSONArray contentJSON = info.getJSONArray("content");
							for (int i = 0; i < contentJSON.length(); i++) {
								String content = (String) contentJSON.get(i);
								mItem.getContent().add(content);
								mContent.setText(content);
							}
							
							JSONArray picUrlJSON = info.getJSONArray("picUrl");
							for (int i = 0; i < picUrlJSON.length(); i++) {
								String picUrl = (String) picUrlJSON.get(i);
								mItem.getPicUrl().add(picUrl);
							}
							
							picUrlList.addAll(mItem.getPicUrl());
							adapter.notifyDataSetChanged();
							
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
		mGallery.setVisibility(View.GONE);
		mContent.setVisibility(View.GONE);
		mRelat.setVisibility(View.GONE);
		mPanel.setVisibility(View.GONE);
		
		switch(i){
		case 1:
			mErrorHintView.hideLoading();
			mGallery.setVisibility(View.VISIBLE);
			mRelat.setVisibility(View.VISIBLE);
			mContent.setVisibility(View.VISIBLE);
			mPanel.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadphoneDetail();
				}
			});
			break;
			
		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					loadphoneDetail();
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
			
		case R.id.toolbar_share:
			Intent inte = new Intent(Intent.ACTION_SEND);    
			inte.setType("image/*");    
            inte.putExtra(Intent.EXTRA_SUBJECT, "Share");    
            inte.putExtra(Intent.EXTRA_TEXT,  "I would like to share this with you...");    
            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
            startActivity(Intent.createChooser(inte, mItem.getTitle()));
			break;
			
		case R.id.toolbar_comment:
			Intent intent = new Intent(this, PhoneTechCommentActivity.class);
			intent.putExtra("docid", docid);
			startActivity(intent);
			break;
			
		case R.id.toolbar_save:
			List<Object> list = mUserController.getCollectionInfo();
			if ( list==null ) {
				list = new ArrayList<Object>();
				list.add(photoItem);
				mUserController.saveCollectionInfo(list);
				SuperUI.showCollectionUI(mContext);
			} else {
				if ( list.contains(photoItem) ) {
					list.remove(photoItem);
					mUserController.saveCollectionInfo(list);
					SuperUI.showUncollectionUI(mContext);
				} else {
					list.add(photoItem);
					mUserController.saveCollectionInfo(list);
					SuperUI.showCollectionUI(mContext);
				}
			}
			break;
			
		default:
			break;
		}
	}
	
	class ShowImageAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return picUrlList.size();
		}

		@Override
		public Object getItem(int position) {
			return picUrlList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_photo_details_garlly_lv, null);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			String picUrl = picUrlList.get(position);
			if ( !TextUtils.isEmpty(picUrl) ) {
				Picasso.with(mContext).load(picUrl).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(holder.image);
			}
			mCount.setText((position + 1) + "/" + picUrlList.size());
			
			return convertView;
		}
	}
	
	static class ViewHolder {
		ImageView image;
	}

}
