package com.todayinfo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jinghua.todayinformation.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenu.OnMenuListener;
import com.special.ResideMenu.ResideMenuItem;
import com.todayinfo.service.UserController;
import com.todayinfo.ui.component.CustomExitDialog;
import com.todayinfo.utils.Configure;
import com.todayinfo.utils.LogUtil;

/**
 * 承载所有信息的主界面
 * 
 * @author zhou.ni 2015年3月16日
 */
public class HomeActivity extends SuperFragmentActivity implements OnClickListener{
	
	private static final String TAG = "HomeActivity";
	
	private UserController mUserController;
	
	private HomeController mHomeController;
	
	private long exitTime = 0;
	
	private ResideMenu mResideMenu;
	
	//左边
	private ResideMenuItem home;   //首页
	private ResideMenuItem newsFocusItem;	          //焦点新闻			
	private ResideMenuItem bolgsShareItem;  		  //博客分享	
	private ResideMenuItem lifeNewsItem;			  //生活新闻
	private ResideMenuItem gameslifeItem;			  //游戏人生
//	private ResideMenuItem grilItem;			 	  //美女写真集
	
	//右边
	private ResideMenuItem myInfoItem;				  //我的	
	private ResideMenuItem settingsItem;			  //设置	
	private ResideMenuItem exitedItem;				  //退出	
	private ResideMenuItem collectionItem;			  //收藏
	
	private RelativeLayout leftMenu;
	private RelativeLayout rightMenu;
	private ImageView phoneImg, newsImg, girlImg;
	
	//左边添加的activity
	private NewsFocusActivity mNewsFocusActivity;
	private BolgsShareActivity mBolgsShareActivity;
	private GamesLifeActivity mGamesLifeActivity;
	private GirlPhotoActivity mGirlPhotoActivity;
	private LifeNewsActivity mLifeNewsActivity;
	
	//右边添加的activity
	private SettingsActivity mSettingsActivity;
	private LoginActivity mLoginActivity;
	private MyInfoActivity mInfoActivity;
	private MyCollectionsActivity mCollectionActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		
		Configure.init(this);
		mUserController = UserController.getInstance(this);
		initView();
		mHomeController = new HomeController(this);
		setUpMenu();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		//上面导航
		LinearLayout homeTop = (LinearLayout) this.findViewById(R.id.home_top);
		leftMenu = (RelativeLayout) homeTop.findViewById(R.id.home_left);
		rightMenu = (RelativeLayout) homeTop.findViewById(R.id.home_right);
		leftMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		rightMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mResideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			}
		});
		
		//下面导航
		LinearLayout homeFoot = (LinearLayout) this.findViewById(R.id.home_foot);
		phoneImg = (ImageView) homeFoot.findViewById(R.id.home_phone_iv);
		newsImg = (ImageView) homeFoot.findViewById(R.id.home_news_iv);
		girlImg = (ImageView) homeFoot.findViewById(R.id.home_girl_iv);
		
		phoneImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHomeController.tabOnclick(v);
			}
		});
		
		newsImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHomeController.tabOnclick(v);
			}
		});
		
		girlImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHomeController.tabOnclick(v);
			}
		});
		
	}
	
	/**
	 * 上面导航的配置
	 */
	private void setUpMenu() {
		// attach to current activity;
		mResideMenu = new ResideMenu(this);
		mResideMenu.setBackground(R.raw.bg);
		mResideMenu.attachToActivity(this);
		mResideMenu.setMenuListener(new OnMenuListener() {
			@Override
			public void openMenu() {
				
			}
			
			@Override
			public void closeMenu() {
				
			}
		});
		
		//valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        mResideMenu.setScaleValue(0.6f);
        
        // create left menu items;
        home = new ResideMenuItem(this, R.raw.icon_home, "首页");
        newsFocusItem = new ResideMenuItem(this, R.raw.icon_news,     "焦点新闻");
        bolgsShareItem = new ResideMenuItem(this, R.raw.icon_bolgs,  "博客分享");
        lifeNewsItem = new ResideMenuItem(this, R.raw.icon_movie, "生活新闻");
        gameslifeItem = new ResideMenuItem(this, R.raw.icon_games, "游戏人生");
//        grilItem = new ResideMenuItem(this, R.raw.icon_gril, "美女写真");
        
        // create right menu items;
        myInfoItem = new ResideMenuItem(this, R.raw.wo, "登录");
        collectionItem = new ResideMenuItem(this, R.raw.icon_collection, "收藏");
        settingsItem = new ResideMenuItem(this, R.raw.icon_settings, "设置");
        exitedItem = new ResideMenuItem(this, R.raw.icon_exit, "退出");
        
        //left click event
        home.setOnClickListener(this);
        newsFocusItem.setOnClickListener(this);
        bolgsShareItem.setOnClickListener(this);
        lifeNewsItem.setOnClickListener(this);
        gameslifeItem.setOnClickListener(this);
//        grilItem.setOnClickListener(this);
        
        //right click event
        myInfoItem.setOnClickListener(this);
        settingsItem.setOnClickListener(this);
        exitedItem.setOnClickListener(this);
        collectionItem.setOnClickListener(this);
        
        //左边添加布局内容
        mResideMenu.addMenuItem(home, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(newsFocusItem, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(bolgsShareItem, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(lifeNewsItem, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(gameslifeItem, ResideMenu.DIRECTION_LEFT);
//        mResideMenu.addMenuItem(grilItem, ResideMenu.DIRECTION_LEFT);
        
        //右边添加布局内容
        mResideMenu.addMenuItem(myInfoItem, ResideMenu.DIRECTION_RIGHT);
        mResideMenu.addMenuItem(collectionItem, ResideMenu.DIRECTION_RIGHT);
        mResideMenu.addMenuItem(settingsItem, ResideMenu.DIRECTION_RIGHT);
        mResideMenu.addMenuItem(exitedItem, ResideMenu.DIRECTION_RIGHT);
        
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT); //设置向右滑动没有反映
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
	}
	
	private void changeActivity(Activity targetActivity){
		mResideMenu.clearIgnoredViewList();
		Intent intent = new Intent(HomeActivity.this, targetActivity.getClass());
		startActivity(intent);
    }

	
    private void changeFragment(Fragment targetFragment){
        mResideMenu.clearIgnoredViewList();
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fm_container, targetFragment);
        fragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
        
    }

	@Override
	public void onClick(View v) {
		
		if (v == newsFocusItem) {
			if ( mNewsFocusActivity==null ){
				mNewsFocusActivity = new NewsFocusActivity();
			}
			changeActivity(mNewsFocusActivity);
		} 
		else if (v == bolgsShareItem) {
			if ( mBolgsShareActivity==null ) {
				mBolgsShareActivity = new BolgsShareActivity();
			}
			changeActivity(mBolgsShareActivity);
		} 
		else if (v == home) {
			
		} 
		else if (v == gameslifeItem) {
			if ( mGamesLifeActivity==null ) {
				mGamesLifeActivity = new GamesLifeActivity();
			}
			changeActivity(mGamesLifeActivity);
		} 
//		else if ( v == grilItem ) {
//			if ( mGirlPhotoActivity==null ) {
//				mGirlPhotoActivity = new GirlPhotoActivity();
//			}
//			changeActivity(mGirlPhotoActivity);
//		}
		else if ( v == lifeNewsItem ) {
			if ( mLifeNewsActivity==null ) {
				mLifeNewsActivity = new LifeNewsActivity();
			}
			changeActivity(mLifeNewsActivity);
		}
		else if ( v == myInfoItem ) {
			if ( mUserController.getUserInfo()!=null ) {
				if ( mInfoActivity==null ) {
					mInfoActivity = new MyInfoActivity();
				}
				changeActivity(mInfoActivity);
			} else {
				if ( mLoginActivity==null ) {
					mLoginActivity = new LoginActivity();
				}
				changeActivity(mLoginActivity);
			}
		}
		else if ( v==settingsItem ){							//设置
			if ( mSettingsActivity==null ) {
				mSettingsActivity = new SettingsActivity();
			}
			changeActivity(mSettingsActivity);
		}
		else if( v==exitedItem ) {								//退出
			this.finish();
			System.gc();
		}
		else if ( v==collectionItem ) {
			if ( mCollectionActivity==null ){
				mCollectionActivity = new MyCollectionsActivity();
			}
			changeActivity(mCollectionActivity);
		}
		
		mResideMenu.closeMenu();

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			boolean flag = mHomeController.backFm();
			if (!flag) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					exitTime = System.currentTimeMillis();
					Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
		            exitTime = System.currentTimeMillis();   
				} else {
					CustomExitDialog exitDialog = new CustomExitDialog(mContext, R.style.customExitDailogStyle, R.layout.custom_exit_dialog);
					exitDialog.show();
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mResideMenu.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() != KeyEvent.ACTION_UP) {
			LogUtil.i(TAG, "dispatchKeyEvent:" + event.getKeyCode());
			boolean flag = mHomeController.handleKeyEvent(event);
			if (flag) {
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void retry() {
		
	}

	@Override
	public void netError() {
		
	}
	
}
