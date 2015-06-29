package com.todayinfo.event;

import com.todayinfo.model.Info;


/**
 * fm 打开事件
 * 
 * @author longtao.li
 * @param <E>
 * 
 * @param <T>
 */
public class EventOpenFM{

	public Class clazz; // 要跳转的fm
	public Info info; // 要携带的数据
	public boolean isTransparent = false;//要显示的Fragment是否是透明的
	
	/**
	 * 是否销毁当前层级的所有Fragment
	 */
	private boolean isDestoryAll;
	

	public boolean isDestoryAll() {
		return isDestoryAll;
	}

	public void setDestoryAll(boolean isDestoryAll) {
		this.isDestoryAll = isDestoryAll;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	public void setTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
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
