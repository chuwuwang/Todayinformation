package com.todayinfo.event;

import com.todayinfo.model.Info;

/**
 * fm 关闭事件
 * 
 * @author longtao.li
 * 
 * @param <T>
 */
public class EventCloseFM  {

	public Class clazz; // 要跳转的fm
	public Info info; // 要携带的数据
	
	/**
	 * 是否销毁当前层级的所有Fragment
	 */
	private boolean isDestoryAll;
	
	/**
	 * 是否销毁当前Fm
	 */
	private boolean isDestory; 
	
	public boolean isDestory() {
		return isDestory;
	}

	public void setDestory(boolean isDestroy) {
		this.isDestory = isDestroy;
	}
	

	public boolean isDestoryAll() {
		return isDestoryAll;
	}

	public void setDestoryAll(boolean isDestoryAll) {
		this.isDestoryAll = isDestoryAll;
	}


	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
}
