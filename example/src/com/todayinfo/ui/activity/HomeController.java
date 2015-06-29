package com.todayinfo.ui.activity;import java.io.IOException;import java.util.ArrayList;import java.util.List;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentActivity;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentTransaction;import android.text.TextUtils;import android.util.Log;import android.view.KeyEvent;import android.view.View;import android.widget.ImageView;import com.jinghua.todayinformation.R;import com.todayinfo.event.EventCloseFM;import com.todayinfo.event.EventOpenFM;import com.todayinfo.model.Info;import com.todayinfo.ui.api.BackBan;import com.todayinfo.ui.api.IBackEventStrategy;import com.todayinfo.ui.api.IKeyEventStrategy;import com.todayinfo.ui.fragment.HotsNewFragment;import com.todayinfo.ui.fragment.PhonePhotoFragment;import com.todayinfo.ui.fragment.PhoneTechFragment;import com.todayinfo.ui.fragment.SuperFragment;import com.todayinfo.utils.CloneUtils;import de.greenrobot.event.EventBus;/** * HomeActivity的业务封装 *  * @author zhou.ni 2015年4月6日 */
public class HomeController {
	private static final String TAG = "HomeController";		/** 一级fragment容器 **/	public static Fragment[] fragments;		/** tab iv控件 **/	public static ImageView[] imageViews;		/** 图片的资源id **/	private int[] imgIds = new int[] { R.raw.phone_press, R.raw.news_press, R.raw.girl_press};		/** 做标签，记录当前是哪个fragment */	public int MARK = 0;		/** 二级容器fragment的层次关系 **/	private List<List<Fragment>> containerFms ;		private FragmentActivity mActivity;	
	public HomeController(FragmentActivity act) {
		mActivity = act;
		EventBus.getDefault().register(this);
		initFragment();
		initFoot();
	}

	public void onResume() {
	}

	public void onPause() {
	}

	public void tabOnclick(View v) {
		resetTab();
		switch (v.getId()) {
		case R.id.home_phone_iv:
			handlTabClick(0);
			break;
		case R.id.home_news_iv:
			handlTabClick(1);
			break;
		case R.id.home_girl_iv:			handlTabClick(2);			break;
		default:
			break;
		}
		
	}
	
	/** 处理下方导航item的点击事件 **/
	private void handlTabClick(int position) {
		FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
		Fragment showFm;
		showFm = fragments[position];
//		transaction.hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3]);
		hideFragment(transaction, fragments[MARK]);
		showFragment(showFm, transaction);
		transaction.commitAllowingStateLoss();		
		imageViews[position].setImageResource(imgIds[position]);
		MARK = position;
	}
	
	/** 初始化fragment */
	public void initFragment() {
		containerFms = new ArrayList<List<Fragment>>();
		containerFms.add(new ArrayList<Fragment>());
		containerFms.add(new ArrayList<Fragment>());		containerFms.add(new ArrayList<Fragment>());

		fragments = new Fragment[3];
		fragments[0] = new PhoneTechFragment();
		fragments[1] = new HotsNewFragment();
		fragments[2] = new PhonePhotoFragment();
		FragmentManager manager = mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.home_basiceFm_container, fragments[0], fragments[0].getClass().getName());
		transaction.add(R.id.home_basiceFm_container, fragments[1], fragments[1].getClass().getName());
		transaction.add(R.id.home_basiceFm_container, fragments[2], fragments[2].getClass().getName());
		transaction.hide(fragments[1]);
		transaction.hide(fragments[2]);
		showFragment(fragments[0], transaction);
		transaction.commitAllowingStateLoss();

	}
	
	/** 重置下面导航 */
	public void resetTab() {
		imageViews[0].setImageResource(R.raw.phone_normal);
		imageViews[1].setImageResource(R.raw.news_normal);
		imageViews[2].setImageResource(R.raw.girl_normal);
	}

	/** 初始化下面导航 */
	public void initFoot() {
		imageViews = new ImageView[3];
		imageViews[0] = (ImageView) mActivity.findViewById(R.id.home_phone_iv);
		imageViews[1] = (ImageView) mActivity.findViewById(R.id.home_news_iv);
		imageViews[2] = (ImageView) mActivity.findViewById(R.id.home_girl_iv);
		imageViews[0].setImageResource(R.raw.phone_press);
		imageViews[1].setImageResource(R.raw.news_normal);
		imageViews[2].setImageResource(R.raw.girl_normal);
	}
	
	/**
	 * 显示fragment的统一入口
	 * @param fragment
	 * @param transaction
	 */
	private void showFragment(Fragment fragment, FragmentTransaction transaction) {
		transaction.show(fragment);
	}
	 
	/**
	 * 隐藏fragment的统一入口 便于事件统计
	 * @param transaction
	 * @param fragment
	 */
	private void hideFragment(FragmentTransaction transaction, Fragment fragment) {
		transaction.hide(fragment);
	}		/**	 * 移除fragment的统一入口 便于事件统计	 * @param transaction	 * @param fragment	 */	private void removeFragment(FragmentTransaction transaction, Fragment fragment) {		transaction.remove(fragment);	}
	
	/**
	 * 回退到上一个FM
	 */
	public boolean backFm() {
		List<Fragment> list = containerFms.get(MARK);// 拿到当前页面的层次关系
		int size = list.size();
		if (size == 0) {
			return false;
		}
		
		Fragment fragment = list.get(size - 1);
		if( fragment instanceof IBackEventStrategy ){
			//自行对返回按钮事件进行处理,不交给上层处理
			boolean operate = new BackEventHandler((IBackEventStrategy) fragment).operate();
			if( operate ){
				return true;
			}
		}
		FragmentManager manager = mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		hideFragment(transaction, fragment);
		list.remove(fragment);

		size = list.size();
		if (size > 0) {
			// 如果当前层次还有其他Fm,则显示顶层的FM
			Fragment fm = list.get(size - 1);
			
			//判断是否是查询状态Fragment,是则返回底层页面
			if( fm instanceof BackBan ){
				transaction.commitAllowingStateLoss();
				destoryFmStack();
				return true;
			}else{
				String showTag = fm.getClass().getName();
				Fragment showFm = manager.findFragmentByTag(showTag);
				if (showFm == null) {
					showFm = fm;
					transaction.add(R.id.home_fm_container, showFm, showFm.getClass().getName());
				}
				showFragment(showFm, transaction);
			}
			
			
		} else {
			// 当前层次没有其他Fm
			showFragment(fragments[MARK], transaction);
		}
		transaction.commitAllowingStateLoss();

		return true;
	}
	
	/**
	 * 处理软键盘事件
	 * @param event
	 * @return
	 */
	public boolean handleKeyEvent(KeyEvent event) {
		List<Fragment> list = containerFms.get(MARK);// 拿到当前页面的层次关系
		int size = list.size();
		if (size == 0) {
			return false;
		}
		
		Fragment fragment = list.get(size - 1);
		if( fragment instanceof IKeyEventStrategy ){
			//自行对返回按钮事件进行处理,不交给上层处理
			boolean operate = new KeyEventHandler((IKeyEventStrategy) fragment).operate(event);
			if( operate ){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 锦囊 用于实施策略,处理软键盘按钮事件
	 * 
	 * @author longtao.li
	 * 
	 */
	class KeyEventHandler {

		IKeyEventStrategy strategy;

		public KeyEventHandler(IKeyEventStrategy strategy) {
			this.strategy = strategy;
		}

		public boolean operate(KeyEvent event) {
			return strategy.eventOperate(event);
		}

	}
	
	/**
	 * 锦囊 用于实施策略,处理硬件返回按钮事件
	 * 
	 * @author longtao.li
	 * 
	 */
	class BackEventHandler {

		IBackEventStrategy strategy;

		public BackEventHandler(IBackEventStrategy strategy) {
			this.strategy = strategy;
		}

		public boolean operate() {
			return strategy.backOperate();
		}

	}

	/**
	 * 隐藏当前的fragment层级,并显示底层fm
	 */
	private void destoryFmStack() {
		FragmentManager manager = mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		List<Fragment> list = containerFms.get(MARK);// 拿到当前页面的层次关系
		for(Fragment fm : list){
			Fragment fragment = manager.findFragmentByTag(fm.getClass().getName());
			if (fragment != null) {
                hideFragment(transaction, fragment);
			}
		}
		list.clear();
		showFragment(fragments[MARK], transaction);
		transaction.commitAllowingStateLoss();
	}
	
	/**
	 * 收到打开FM的事件
	 * 
	 * @param event
	 */
	public void onEvent(EventOpenFM event) {
		if (event == null || event.getClazz() == null)
			return;

		Info bean =  event.getInfo();
		Class<Fragment> clazz = event.getClazz();
		String tag = clazz.getName();
		for( int i=0;i<fragments.length;i++ ){
			boolean equals = TextUtils.equals(fragments[i].getClass().getName(), tag);
			if( equals ){//如果要显示最底层的3个FM之一
				destoryFmStack();//隐藏当前层级
				resetTab();
				handlTabClick(i);
				((SuperFragment) fragments[i]).receiveInfo(bean);
				return;
			}
		}
		
		if( event.isDestoryAll() ){
			_destoryFmStack();//隐藏当前层级
		}
		
		//显示非底层fragment
		showOtherFm(event);
	}
	
	/**
	 * 隐藏当前的fragment层级
	 */
	private void _destoryFmStack() {
		FragmentManager manager = mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		List<Fragment> list = containerFms.get(MARK);// 拿到当前页面的层次关系
		for(Fragment fm : list){
			Fragment fragment = manager.findFragmentByTag(fm.getClass().getName());
			if (fragment != null) {
                hideFragment(transaction, fragment);
			}
		}
		list.clear();
		transaction.commitAllowingStateLoss();
	}
	
	/**
	 * 显示非底层fragment
	 * @param bean
	 * @param clazz
	 * @param tag
	 */
	private void showOtherFm(EventOpenFM event) {
		//深度克隆数据
		Info bean = null;
		try {
			bean = (Info) CloneUtils.copy((Info) event.getInfo());
		} catch (ClassNotFoundException e1) {
			Log.e(TAG, "uncaughtException crash", e1);
		} catch (IOException e1) {
			Log.e(TAG, "uncaughtException crash", e1);
		}
		
		Class<Fragment> clazz = event.getClazz();
		boolean transparent = event.isTransparent();
		String tag = clazz.getName();
		FragmentManager manager = mActivity.getSupportFragmentManager();
		Fragment fragment = manager.findFragmentByTag(tag);
		FragmentTransaction transaction = manager.beginTransaction();
		
		if (fragment == null) {
			try {
				fragment = clazz.newInstance();// 反射得到Fragment实例
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			transaction.add(R.id.home_fm_container, fragment, tag);
		}
        	List<Fragment> list = containerFms.get(MARK);// 当前tab的层次关系
    		int size = list.size();
    		if (size > 0 && !transparent) {
    			//隐藏所有fm
    			for( Fragment fm : list ){
    				hideFragment(transaction, fm);
    			}
    		}
    		((SuperFragment) fragment).receiveInfo(bean); // 接收要传递的数据
    		if (!list.contains(fragment)) {
    			//将已经显示的fm加入到层级结构中
    			Log.i(TAG, "打开"+ fragment.getClass().getName());
    			list.add(fragment);
    		}
    		showFragment(fragment, transaction);
		
		transaction.commitAllowingStateLoss();
	}
	
	
	/**
	 * 收到关闭FM的事件
	 * 
	 * @param event
	 */
	public void onEvent(EventCloseFM event) {
		if (event == null || event.getClazz() == null)
			return;		
		Info bean =  event.getInfo();
		Class<Fragment> clazz = event.getClazz();
		boolean isDestory = event.isDestory();
		boolean isDestoryAll = event.isDestoryAll();
		String tag = clazz.getName();
		
		if( isDestoryAll ){
			destoryFmStack();
		}else{
			handleEventClose(bean, isDestory, tag);
		}
		System.gc();
	}
	
	/**
	 * 处理普通的关闭事件
	 * @param bean
	 * @param isDestory
	 * @param tag
	 */
	private void handleEventClose(Info bean, boolean isDestory, String tag) {
		FragmentManager manager = mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		List<Fragment> list = containerFms.get(MARK);// 拿到当前页面的层次关系
		Fragment fragment = manager.findFragmentByTag(tag);
		if (fragment != null) { 
			if( isDestory ){
				//隐藏fragment
				hideFragment(transaction, fragment);
			}else{
				hideFragment(transaction, fragment);
			}
			
			if (list.contains(fragment)) {
				list.remove(fragment);
			}
			int size = list.size();
			if (size > 0) {
				Fragment fm = list.get(size - 1);
				//判断是否是查询状态Fragment,是则返回底层页面
				if( fm instanceof BackBan ){
					transaction.commitAllowingStateLoss();
					destoryFmStack();
					return ;
				}
				String showTag = fm.getClass().getName();
				Fragment showFm = manager.findFragmentByTag(showTag);
				if (showFm == null) {
					showFm = fm;
					transaction.add(R.id.home_fm_container, showFm, showFm.getClass().getName());
				}
				((SuperFragment) showFm).receiveInfo(bean); // 接收要传递的数据
				showFragment(showFm, transaction);
			} else {
				((SuperFragment) fragments[MARK]).receiveInfo(bean); // 接收要传递的数据
				showFragment(fragments[MARK], transaction);
			}
			transaction.commitAllowingStateLoss();
		}
	}		/**	 * 打开一个Fragment	 * 	 * @param clazz	 * @param info	 */	public void openFragment(final Class clazz, final Info info) {		mActivity.runOnUiThread(new Runnable() {			@Override			public void run() {				EventOpenFM event = new EventOpenFM();				event.setClazz(clazz);				event.setInfo(info);				EventBus.getDefault().post(event);			}		});	}	/**	 * 关闭一个Fragment	 * 	 * @param clazz	 * @param info	 */	public void closeFragment(final Class clazz, final Info info) {		mActivity.runOnUiThread(new Runnable() {			@Override			public void run() {				EventCloseFM event = new EventCloseFM();				event.setClazz(clazz);				event.setInfo(info);				EventBus.getDefault().post(event);			}		});	}

}
