package com.todayinfo.service;

import java.util.List;

import android.content.Context;

import com.todayinfo.model.User;
import com.todayinfo.utils.SharedpreferncesUtil;

/**
 * 封装用户 业务逻辑
 * 
 * @author longtao.li
 *
 */
public class UserController {
	
	private static UserController instance;
	private Context mContext;

	private UserController(Context context) {
		mContext = context;
	}

	public static UserController getInstance(Context context) {
		if (instance == null) {
			instance = new UserController(context);
		}
		return instance;
	}
	
	/**
	 * 是否已经登录
	 * @return
	 */
	public boolean isLogin(){
		if( getUserInfo() != null ){
			return true;
		}
		return false;
	}
	

	/**
	 * 获取本地缓存的用户信息 
	 * @return null 表示无用户信息
	 */
	public User getUserInfo(){
		return SharedpreferncesUtil.getUserInfo(mContext);
	}
	
	/**
	 * 缓存用户信息
	 * @param user
	 */
	public void saveUserInfo(User user){
		SharedpreferncesUtil.saveUserInfo(mContext, user);
	}
	
	/**
	 * 登出
	 */
	public void loginOut(){
		SharedpreferncesUtil.clearByKey(SharedpreferncesUtil.KEY_USER_INFO, mContext);
	}
	
	/**
	 * 获取本地缓存的收藏信息 
	 * 
	 * @return null 表示无用户信息
	 */
	public List<Object> getCollectionInfo(){
		return SharedpreferncesUtil.getCollectionInfo(mContext);
	}
	
	/**
	 * 缓存用户收藏信息
	 * @param user
	 */
	public void saveCollectionInfo(List<Object> list){
		SharedpreferncesUtil.saveCollectionInfo(mContext, list);
	}
	
	
}
