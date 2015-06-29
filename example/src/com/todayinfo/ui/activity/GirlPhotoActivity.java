package com.todayinfo.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase.OnPreRefreshingAnimListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.todayinfo.model.GrilPhotoItem;
import com.todayinfo.ui.adapter.GrilListAdapter;
import com.todayinfo.ui.api.DataTask;
import com.todayinfo.ui.component.fastscroll.FastScrollView;
import com.todayinfo.ui.component.fastscroll.IdleListDetector;
import com.todayinfo.ui.component.fastscroll.IdleListener;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.JsonUtils;
import com.todayinfo.utils.LogUtil;

/**
 * 美女写真界面
 * 
 * @author zhou.ni  2015年3月18日
 */
public class GirlPhotoActivity extends SuperActivity implements OnClickListener{
	
	private static final String TAG = "GirlPhotoActivity";
	
	private PullToRefreshListView grilListView;
	private FastScrollView fastScroller;				// 快速滚动器
	private IdleListener idleListener;					// 空闲监听器
	@SuppressWarnings("unused")
	private IdleListDetector mPostScrollLoader;			// 空闲状态解析器
	
	private GrilListAdapter grilListAdapter;
	private List<GrilPhotoItem> grilList = new ArrayList<GrilPhotoItem>();

	private int pageNext = 1;
	private LinearLayout footerView;    //最后一条listview
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fm_grilphoto);
		initView();
		refreshData(true);
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		headView.findViewById(R.id.back_left).setOnClickListener(this);;
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		title.setText("写真集");
		
		grilListView = (PullToRefreshListView) this.findViewById(R.id.gril_list);
		grilListAdapter = new GrilListAdapter(mContext, grilList);
		grilListView.setAdapter(grilListAdapter);
		setupIdleListener(grilListView.getRefreshableView());
	}
	
	protected void setupIdleListener(AbsListView list) {
		idleListener = new IdleListener(list, 0);
		mPostScrollLoader = new IdleListDetector(idleListener);
		fastScroller = (FastScrollView) grilListView.getParent();
//		fastScroller.setOnIdleListDetector(mPostScrollLoader);
		fastScroller.setScrollListener(grilListView);
		
		grilListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					pageNext = 1;
					refreshData(true);
				}
			}
		});
		
		grilListView.setOnPreRefreshingAnimListener(new OnPreRefreshingAnimListener() {
			@Override
			public void onPreRefreshingAnim() {
				grilListView.setFooterLoadingViewHeaderText("加载更多图片");
			}
		});
		
		grilListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if( pageNext == 0 ){				//没有下一页了
					grilListView.onRefreshComplete();
					if ( footerView == null ) {
						footerView = (LinearLayout) View.inflate(mContext, R.layout.item_pull_to_refresh_footer, null);
						TextView footer = (TextView) footerView.findViewById(R.id.footer);
						footer.setText("到底了呢-共" + grilList.size() + "条订单");
						grilListView.getRefreshableView().addFooterView(footerView);  
					}
				}else{
					//滑动到底部自动刷新
					refreshData(false);
				}
			}
		});
	}
	
	private void refreshData(boolean clean) {
		dataTask = new RefreshDataTask(dataTask.getID() + 1, clean);
		dataTask.run();
	}
	
	/**
	 * 丢弃老数据重新刷新
	 * 
	 * @author longtao.li
	 * 
	 */
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
				loadGrilInfo(clean);
			}
			
		}

	}
	
	/**
	 * 加载美女图片的数据
	 */
	private void loadGrilInfo(final boolean flag){
		String url = "http://www.6mm.cc/api/list.php?p=" + pageNext;
		AsyncHttpUtil.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				if ( footerView!=null ){
					grilListView.getRefreshableView().removeFooterView(footerView);
					footerView = null;
				}
				
				grilListView.onRefreshComplete();
				
				if ( responseBody!=null && responseBody.length>0 ){
					try {
						JSONArray jsonArray = new JSONArray(new String(responseBody));
						List<GrilPhotoItem> list = JsonUtils.getInstance(GrilPhotoItem.class, jsonArray);
						if ( list!=null && list.size()>0 ){
							if ( flag ) {
								grilList.clear();
							} 
							LogUtil.i(TAG, list.get(0).toString());
							pageNext ++;
							grilList.addAll(list);
							grilListAdapter.notifyDataSetChanged();
						}  else {
							pageNext = 0;
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
	public void pwdError() {
		
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

		default:
			break;
		}
	}
	
}
