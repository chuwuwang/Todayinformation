package com.todayinfo.model;

/**
 * 与后台交互的返回结果,封装单个实体
 * 
 * @author longtao.li
 * 
 * @param <T>
 */
public class CodeResult<T> extends SuperBean {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 返回码
	 */
	public String code;
	
	/**
	 * 结果信息
	 */
	public String message;
	
	/**
	 * 状态结果
	 */
	public String status;
	
	/**
	 * 结果的实体
	 */
	public T data;

	

}
