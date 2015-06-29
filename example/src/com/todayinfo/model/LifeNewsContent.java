package com.todayinfo.model;

/**
 * 
 * @author zhou.ni 2015年5月25日
 */
public class LifeNewsContent extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int isImg; // 0 是文字 1是图片
	private String contentList;

	public int getIsImg() {
		return isImg;
	}

	public void setIsImg(int isImg) {
		this.isImg = isImg;
	}

	public String getContentList() {
		return contentList;
	}

	public void setContentList(String contentList) {
		this.contentList = contentList;
	}

}
