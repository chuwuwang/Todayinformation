package com.todayinfo.ui.activity;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghua.todayinformation.R;
import com.todayinfo.utils.FileUtils;
import com.todayinfo.utils.Utils;

/**
 * 设置界面
 * 
 * @author zhou.ni 2015年3月16日
 */
public class SettingsActivity extends SuperActivity implements OnClickListener{

	private TextView version;
	private TextView cache;
	
	private ViewStub cacheViewStub;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		cache.setText(FileUtils.getDiskCacheSize(this));
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		RelativeLayout leftBack = (RelativeLayout) headView.findViewById(R.id.back_left);
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		leftBack.setOnClickListener(this);
		title.setText("设置");
		RelativeLayout checkUpdate = (RelativeLayout) this.findViewById(R.id.check_update);
		version = (TextView) this.findViewById(R.id.version);
		RelativeLayout clearCache = (RelativeLayout) this.findViewById(R.id.clear_cache);
		cache = (TextView) this.findViewById(R.id.cache);
		RelativeLayout contact = (RelativeLayout) this.findViewById(R.id.contact_person);
		RelativeLayout feedback = (RelativeLayout) this.findViewById(R.id.feed_back);
		RelativeLayout about = (RelativeLayout) this.findViewById(R.id.about);
		checkUpdate.setOnClickListener(this);
		clearCache.setOnClickListener(this);
		contact.setOnClickListener(this);
		feedback.setOnClickListener(this);
		about.setOnClickListener(this);
		RelativeLayout wel = (RelativeLayout) this.findViewById(R.id.enter_wel);
		wel.setOnClickListener(this);
		
		cache.setText(FileUtils.getDiskCacheSize(mContext));
		version.setText("当前版本V" + GetVersion(mContext));
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_left:
			finish();
			break;
			
		case R.id.check_update:				//检查跟新
			showToast("已经更新到最新版本");	
			break;
			
		case R.id.clear_cache:			   //清除缓存
			if ( cacheViewStub==null ) {
				cacheViewStub = (ViewStub) this.findViewById(R.id.cache_view);
				View inflate = cacheViewStub.inflate();
				TextView ok = (TextView) inflate.findViewById(R.id.ok_button);
				TextView cancel = (TextView) inflate.findViewById(R.id.cancle_button);
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 清除缓存图片代码 
						File file = Utils.createDefaultCacheDir(mContext);
						delAllFile(file.getPath());
						cache.setText(FileUtils.getDiskCacheSize(mContext));
						cacheViewStub.setVisibility(View.GONE);
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cacheViewStub.setVisibility(View.GONE);
					}
				});
			}
			cacheViewStub.setVisibility(View.VISIBLE);
			break;
			
		case R.id.contact_person:		//联系开发人员
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18616742809"));
			startActivity(intent);		// 通知activtity处理传入的call服务
			break;
			
		case R.id.feed_back:			//意见反馈
			Intent i = new Intent(this, FeedBackActivity.class);
			startActivity(i);
			break;
			
		case R.id.about:				//关于
			Intent in = new Intent(this, AboutActivity.class);
			startActivity(in);
			break;
		case R.id.enter_wel:
			Intent inte = new Intent(this, WelcomeActivity.class);
			startActivity(inte);
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path 文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);		// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);		// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 删除文件夹
	 * 
	 * @param folderPath  文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); 		// 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); 		// 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void obtainInfo() {

	}
	
    /**
     *  取得版本号
     * @param context
     * @return
     */
    public static String GetVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) { 
			return "Unknown";  
		} 
	}

}
