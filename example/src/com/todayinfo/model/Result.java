package com.todayinfo.model;


/**
 * 与后台交互的返回结果,封装单个实体
 * @author longtao.li
 *
 * @param <T>
 */
public class Result<T> extends SuperBean{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 页码
	 */
	public int page;
	/**
	 * 返回码
	 */
	public int code = 0;
	/**
	 * 结果信息
	 */
	public String msg;
	/**
	 * 结果的实体
	 */
	public T data;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
