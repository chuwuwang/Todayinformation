package com.todayinfo.model;

/**
 * frag之间数据传递的载体
 * @author longtao.li
 *
 * @param <T>
 */
public class Info<T> extends SuperBean{

	public Info(T data) {
		this.data = data;
	}
	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}
