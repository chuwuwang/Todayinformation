package com.todayinfo.ui.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.ArticleItem;
import com.todayinfo.model.BlogItem;
import com.todayinfo.model.PhonePhotoItem;
import com.todayinfo.ui.component.CircleImageView;
import com.todayinfo.ui.component.ErrorHintView;
import com.todayinfo.utils.DateUtils;

/**
 * 我的收藏界面
 * 
 * @author zhou.ni 2015年3月16日
 */
public class MyCollectionsActivity extends SuperActivity {
	
	private ErrorHintView mErrorHintView;
	private ListView mListView;
	
	private List<Object> mList = new ArrayList<Object>();
	private CollectionAdapter adapter;
	
	public static int VIEW_LIST = 1;
	public static int VIEW_LOADING = 2;
	public static int VIEW_NODATA = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		getWindow().setBackgroundDrawable(null);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		List<Object> list = mUserController.getCollectionInfo();
		if ( list==null || list.size()==0 ){
			showLoading(VIEW_NODATA);
		} else {
			showLoading(VIEW_LIST);
			mList.clear();
			mList.addAll(list);
			adapter = new CollectionAdapter();
			mListView.setAdapter(adapter);
		}
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		RelativeLayout leftBack = (RelativeLayout) headView.findViewById(R.id.back_left);
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("我的收藏");
		leftBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
		mListView = (ListView) this.findViewById(R.id.list);
		
		showLoading(VIEW_LOADING);
		List<Object> list = mUserController.getCollectionInfo();
		if ( list==null || list.size()==0 ){
			showLoading(VIEW_NODATA);
		} else {
			showLoading(VIEW_LIST);
			mList.clear();
			mList.addAll(list);
			adapter = new CollectionAdapter();
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = mList.get(position);
				if ( obj instanceof ArticleItem ) {
					ArticleItem techItem = ( ArticleItem )obj;
					Intent intent = new Intent(mContext, PhoneTechDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ArticleItem", techItem);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				} else if ( obj instanceof PhonePhotoItem ) {
					PhonePhotoItem photoItem = ( PhonePhotoItem ) obj;
					Intent intent = new Intent(mContext, PhonePhotoDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("PhonePhotoItem", photoItem);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				} else {
					BlogItem blogItem = (BlogItem) obj;
					Intent intent = new Intent(mContext, BolgDetailActivity.class);
					Bundle info = new Bundle();
					info.putSerializable("item", blogItem);
					intent.putExtras(info);
					mContext.startActivity(intent);
				}
			}
		});
		
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
	
	class CollectionAdapter extends BaseAdapter {
		
		private static final int VIEW_TYPE = 3;    
		private static final int TYPE_0 = 0;  //手机科技资讯		
		private static final int TYPE_1 = 1;  //图片资讯
		private static final int TYPE_2 = 2;  //新闻博客
		
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
		
		/**
		 * 每个convertView都会调用此方法，获得当前所需要的view样式  
		 */
		@Override
		public int getItemViewType(int position) {
			Object obj = mList.get(position);
	        if (obj instanceof ArticleItem ) {
	        	return TYPE_0;  
	        } else if ( obj instanceof PhonePhotoItem ) {
	        	return TYPE_1;
	        } else {
	        	return TYPE_2;
	        }
		}

		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TechHolder techHolder = null;
			PhotoHolder photoHolder = null;
			BlogHolder blogHolder = null;
			
			ArticleItem techItem = null;
			PhonePhotoItem photoItem = null;
			BlogItem blogItem = null;
			
			Object obj = mList.get(position);
			if ( obj instanceof ArticleItem ) {
				techItem = ( ArticleItem )obj;
			} else if ( obj instanceof PhonePhotoItem ) {
				photoItem = ( PhonePhotoItem ) obj;
			} else {
				blogItem = (BlogItem) obj;
			}
			
			int viewType = getItemViewType(position);
			
			if ( convertView == null ) {
				
				switch (viewType) {			//按当前所需的样式，确定new的布局
				case TYPE_0:				
					techHolder = new TechHolder();
					convertView = View.inflate(mContext, R.layout.item_index_intro_lv, null);
					techHolder.img = (ImageView) convertView.findViewById(R.id.img);
					techHolder.title = (TextView) convertView.findViewById(R.id.title);
					techHolder.body = (TextView) convertView.findViewById(R.id.body);
					techHolder.date = (TextView) convertView.findViewById(R.id.time);
					convertView.setTag(techHolder);
					break;
					
				case TYPE_1:				
					photoHolder = new PhotoHolder();
					convertView = View.inflate(mContext, R.layout.item_phone_photo_lv, null);
					photoHolder.img = (ImageView) convertView.findViewById(R.id.img);
					photoHolder.text = (TextView) convertView.findViewById(R.id.text);
					convertView.setTag(photoHolder);
					break;
					
				case TYPE_2:
					blogHolder = new BlogHolder();
					convertView = View.inflate(mContext, R.layout.item_fresh_bolg_lv, null);
					blogHolder.title = (TextView) convertView.findViewById(R.id.title);
					blogHolder.uPic = (CircleImageView) convertView.findViewById(R.id.image);
					blogHolder.name = (TextView) convertView.findViewById(R.id.name);
					blogHolder.published = (TextView) convertView.findViewById(R.id.publish);
					blogHolder.summary = (TextView) convertView.findViewById(R.id.summary);
					blogHolder.viewsTv = (TextView) convertView.findViewById(R.id.views_tx);
					blogHolder.diggsTv = (TextView) convertView.findViewById(R.id.diggs_tx);
					blogHolder.commentsTv = (TextView) convertView.findViewById(R.id.comment_tx);
					blogHolder.diggs = (LinearLayout) convertView.findViewById(R.id.hots_diggs);
					blogHolder.views = (LinearLayout) convertView.findViewById(R.id.hots_views);
					blogHolder.comment = (LinearLayout) convertView.findViewById(R.id.hots_comment);
					convertView.setTag(blogHolder);
					break;
				}
				
			} else {
				switch (viewType) {
				case TYPE_0:
					techHolder = (TechHolder) convertView.getTag();
					break;

				case TYPE_1:
					photoHolder = (PhotoHolder) convertView.getTag();
					break;
				
				case TYPE_2:
					blogHolder = (BlogHolder) convertView.getTag();
					break;
				}
			}	
			
			switch (viewType) {
			case TYPE_0:
				if ( techItem!=null ) {
					if ( !TextUtils.isEmpty(techItem.getPicUrl()) ){
						Picasso.with(mContext).load(techItem.getPicUrl()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(techHolder.img);
					} 
					if ( !TextUtils.isEmpty(techItem.getTitle()) ) {
						techHolder.title.setText(techItem.getTitle());
					} else {
						techHolder.title.setText("");
					}
					if ( !TextUtils.isEmpty(techItem.getTitle()) ) {
						techHolder.body.setText(techItem.getContent());
					} else {
						techHolder.body.setText("");
					}
					if ( !TextUtils.isEmpty(techItem.getTitle()) ) {
						techHolder.date.setText(techItem.getDate());
					} else {
						techHolder.date.setText("");
					}
				}
				break;
				
			case TYPE_1:
				if ( photoItem!=null ) {
					if ( !TextUtils.isEmpty(photoItem.getPic()) ){
						Picasso.with(mContext).load(photoItem.getPic()).placeholder(R.color.ECECEC).error(R.color.ECECEC).into(photoHolder.img);
					} 
					
					if ( !TextUtils.isEmpty(photoItem.getTitle()) ) {
						photoHolder.text.setText(photoItem.getTitle());
					} else {
						photoHolder.text.setText("");
					}
				}
				break;
				
			case TYPE_2:
				if ( blogItem!=null ) {
					blogHolder.title.setText(blogItem.getTitle());
					blogHolder.name.setText(blogItem.getName());
					blogHolder.summary.setText(blogItem.getSummary());
					
					if( !TextUtils.isEmpty(blogItem.getAvatar()) ) {
						Picasso.with(mContext).load(blogItem.getAvatar()).placeholder(R.drawable.touxiang).error(R.drawable.touxiang).into(blogHolder.uPic);
					}
					
					if ( !TextUtils.isEmpty(blogItem.getPublished()) ) {
						blogHolder.published.setText( DateUtils.convertGMTToLoacale(blogItem.getPublished()) );
					} 
					
					if ( !TextUtils.isEmpty(blogItem.getViews()) ) {
						blogHolder.viewsTv.setText(blogItem.getViews());
					} else {
						blogHolder.viewsTv.setText("");
					}
					
					if ( !TextUtils.isEmpty(blogItem.getComments()) ) {
						blogHolder.commentsTv.setText(blogItem.getComments());
					} else {
						blogHolder.commentsTv.setText("");
					}

					if ( !TextUtils.isEmpty(blogItem.getDiggs()) ) {
						blogHolder.diggsTv.setText(blogItem.getDiggs());
					} else {
						blogHolder.diggsTv.setText("");
					}
				}
				break;
			default:
				break;
			}
			
			return convertView;
			
		}
		
	}
	
	static class TechHolder {
		ImageView img;
		TextView title;
		TextView body;
		TextView date;
	}
	
	static class PhotoHolder{
		ImageView img;
		TextView text;
	}
	
	static class BlogHolder {
		TextView title;
		CircleImageView uPic;
		TextView summary;
		TextView published;
		TextView viewsTv;
		TextView diggsTv;
		TextView name;
		TextView commentsTv;
		TextView textViews;
		LinearLayout views;
		LinearLayout diggs;
		LinearLayout comment;
	}

}
