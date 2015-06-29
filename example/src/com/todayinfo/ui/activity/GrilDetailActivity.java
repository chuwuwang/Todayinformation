package com.todayinfo.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.todayinfo.model.GrilDetailItem;
import com.todayinfo.ui.component.ViewPagerItem;
import com.todayinfo.ui.component.gesture.PhotoViewAttacher;
import com.todayinfo.utils.AppUtil;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.JsonUtils;

/**
 * 查看美女图片详情
 * 
 * @author zhou.ni 2015年4月25日
 */
public class GrilDetailActivity extends SuperActivity implements OnClickListener{

	private GrilDetailAdaper detailAdaper;
	private List<GrilDetailItem> detailList = new ArrayList<GrilDetailItem>();
	private String path = "http://www.6mm.cc/api/detail.php?aid=";
	
	private ViewPager mViewPager;
	private ViewPagerItem pagerItem; 
	
	private int w;			//屏幕宽度
	private String aid;		//图片id
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gril_detail);
		initView();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		Intent intent = getIntent();
		aid = intent.getStringExtra("id");
		String tit = intent.getStringExtra("title");
		
		//获取屏幕宽度
		w = AppUtil.getPhoneHW(mContext);  
		
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(this);;
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText(tit);
		mViewPager = (ViewPager) this.findViewById(R.id.gril_viewpage);
		pagerItem = (ViewPagerItem) this.findViewById(R.id.viewpage_item);
		pagerItem.setBitmap(R.raw.no_icon, R.raw.yes_icon);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				pagerItem.notifyDataSetChanged(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
			}
		});
		
		detailAdaper = new GrilDetailAdaper();
		mViewPager.setAdapter(detailAdaper);
		loadGrilPhotoDetailData();
	}
	
	/**
	 * 加载美女详情的图片
	 */
	private void loadGrilPhotoDetailData(){
		String url = path + aid;
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if ( arg2!=null && arg2.length>0 ) {
					try {
						JSONArray jsonArray = new JSONArray(new String(arg2));
						List<GrilDetailItem> list = JsonUtils.getInstance(GrilDetailItem.class, jsonArray);
						if ( list!=null && list.size()>0 ) {
							detailList.addAll(list);
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									detailAdaper.notifyDataSetChanged();
									pagerItem.setCount(detailList.size());
									pagerItem.notifyDataSetChanged(0);
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(mContext, "请检查您的网络", Toast.LENGTH_SHORT).show();
			}
		});
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
	
	class GrilDetailAdaper extends PagerAdapter{

		@Override
		public int getCount() {
			return detailList!=null ? detailList.size() : 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (View)arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager)container).removeView((View)object); 
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(mContext, R.layout.item_gril_photo_detail_lv, null);
			ImageView image = (ImageView)view.findViewById(R.id.photo_icon);
			@SuppressWarnings("unused")
			PhotoViewAttacher attacher = new PhotoViewAttacher(image);
			final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.pro_bar);
			
			GrilDetailItem item = detailList.get(position);
			
			if ( !TextUtils.isEmpty(item.getUrl()) ) {
				Picasso.with(mContext)
				.load(item.getUrl())
				.transform(new CropSquareTransformation())
				.into(image, new Callback() {
					@Override
					public void onSuccess() {
						progressBar.setVisibility(View.GONE);
					}
					@Override
					public void onError() {
						progressBar.setVisibility(View.GONE);
					}
				});
			}
			((ViewPager) container).addView(view, 0);
			
			return view;
		}
		
	}
	
	public class CropSquareTransformation implements Transformation {
		  @Override 
		  public Bitmap transform(Bitmap source) {
			  int width = source.getWidth();
			  int height = source.getHeight();
			  
			  int h_new = height*w/width;
			  // 计算缩放比例
			  float scaleWidth = ((float) w) / width;
			  float scaleHeight = ((float) h_new) / height;
			  // 取得想要缩放的matrix参数
			  Matrix matrix = new Matrix();
			  matrix.postScale(scaleWidth, scaleHeight);
			  // 得到新的图片
			  Bitmap result = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
			 
			  if (result != source) {
				  source.recycle();
			  }
			  return result;
		  }
		 
		  @Override 
		  public String key() { 
			  return "square()";
		  }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_left:
			finish();
			break;

		default:
			break;
		}
	}

}
