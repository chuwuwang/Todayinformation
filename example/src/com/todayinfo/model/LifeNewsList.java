package com.todayinfo.model;

import java.util.ArrayList;

/**
 * 
 * @author zhou.ni 2015年5月24日
 */
public class LifeNewsList extends SuperBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String errorMessage;
	private ArrayList<LifeNews> list;
	private ArrayList<?> list2;
	private String str;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ArrayList<LifeNews> getList() {
		return list;
	}

	public void setList(ArrayList<LifeNews> list) {
		this.list = list;
	}

	public ArrayList<?> getList2() {
		return list2;
	}

	public void setList2(ArrayList<?> list2) {
		this.list2 = list2;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
