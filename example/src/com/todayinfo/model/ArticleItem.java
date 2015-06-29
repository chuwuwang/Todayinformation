package com.todayinfo.model;

/**
 * 首推文章实体类
 * @author Administrator
 *
 */
public class ArticleItem extends SuperBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;			//文章id
	private String title;		//文章标题
	private String picUrl;		//导读图地址
	private String type;		//tu  调用光影集的接口 wz 调用详细页节接口
	private String date;		//时间
	private String content;		//简介
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "ArticleList [id=" + id + ", title=" + title + ", picUrl="
				+ picUrl + ", type=" + type + ", date=" + date + ", content="
				+ content + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof ArticleItem ) {
			ArticleItem item = (ArticleItem) o;
			return this.id.equals(item.id);
		}
		return super.equals(o);
	}
	
}
