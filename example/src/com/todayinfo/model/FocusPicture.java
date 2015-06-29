package com.todayinfo.model;

/**
 * 首页头条焦点图实体类
 * 
 * @author Administrator
 * 
 */
public class FocusPicture extends SuperBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title;
	private String picUrl;
	private String date;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "FocusPicture [id=" + id + ", title=" + title + ", picUrl="
				+ picUrl + ", date=" + date + ", type=" + type + "]";
	}

}
