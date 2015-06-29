package com.todayinfo.ui.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jinghua.todayinformation.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.todayinfo.model.NewItem;
import com.todayinfo.ui.activity.NewsDetailActivity;
import com.todayinfo.ui.adapter.NewsAdapter;
import com.todayinfo.utils.AsyncHttpUtil;
import com.todayinfo.utils.LogUtil;

/**
 * 最新的新闻资讯碎片
 * 
 * @author zhou.ni 2015年3月18日
 */
public class HotsNewFragment extends SuperFragment {
	
	private View view;
	private PullToRefreshListView hotNewsList;
	private NewsAdapter newsAdapter;
	private List<NewItem> newList = new ArrayList<NewItem>();
	private static String URL_PATH = "http://wcf.open.cnblogs.com/news/hot/40";
	private boolean flag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_hotsnew, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		hotNewsList = (PullToRefreshListView) view.findViewById(R.id.hots_news_list);
		setUpPullToList();
		loadHotsNewsInfo(false);
	}
		
	/**
	 * 设置listview的下拉刷新
	 */
	private void setUpPullToList(){
		newsAdapter = new NewsAdapter(mContext, newList);
		hotNewsList.setAdapter(newsAdapter);
		hotNewsList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()){
					flag = false;
					loadHotsNewsInfo(true);
				}
			}
		});
		
		hotNewsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewItem item = newList.get(position-1);
				Intent intent = new Intent(mContext, NewsDetailActivity.class);
				Bundle info = new Bundle();
				info.putSerializable("item", item);
				intent.putExtras(info);
				startActivity(intent);
			}
		});
		
	}
	
	/**
	 * 加载热门新闻的数据
	 */
	private void loadHotsNewsInfo(final boolean clean){
		AsyncHttpUtil.get(URL_PATH, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] responseBody) {
				hotNewsList.onRefreshComplete();
				
                try {
                	if ( clean ) {
                		newList.clear();
                	}
                	ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBody);
					parseHotsNewsXml(inputStream);
					newsAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.d("error", "hots news XmlPullParserException error");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
		});
        
	}
	
	/**
	 * 解析热门新闻 
	 * @param in
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	private void parseHotsNewsXml(InputStream in) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		
		parser.setInput(in, "UTF-8");
		int event = parser.getEventType();
		NewItem item = null;
		
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if ("entry".equals(parser.getName())) {
					flag = true;
					item = new NewItem();
				}
				if (flag) {
					if ("id".equals(parser.getName())) {
						String id = parser.nextText();
						item.setId(id);
					} else if ("title".equals(parser.getName())) {
						String title = parser.nextText();
						item.setTitle(title);
					} else if ("summary".equals(parser.getName())) {
						String summary = parser.nextText();
						item.setSummary(summary);
					} else if ("published".equals(parser.getName())) {
						String published = parser.nextText();
						item.setPublished(published);
					} else if ("updated".equals(parser.getName())) {
						String updated = parser.nextText();
						item.setUpdated(updated);
					} else if ("link".equals(parser.getName())) {
						String link = parser.getAttributeValue(1);  //获得第二个属性值
						item.setLink(link);
					} else if ("diggs".equals(parser.getName())) {
						String diggs = parser.nextText();
						item.setDiggs(diggs);
					} else if ("views".equals(parser.getName())) {
						String views = parser.nextText();
						item.setViews(views);
					} else if ("comments".equals(parser.getName())) {
						String comments = parser.nextText();
						item.setComments(comments);
					} else if ("sourceName".equals(parser.getName())) {
						String sourceName = parser.nextText();
						item.setSourceName(sourceName);
					}
				}
				break;
			case XmlPullParser.TEXT:
				break;
			case XmlPullParser.END_TAG:
				if ("entry".equals(parser.getName())) {
					newList.add(item);
				}
				break;
			}
			event = parser.next();
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
	
}
