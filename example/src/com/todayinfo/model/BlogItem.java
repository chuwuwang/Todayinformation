package com.todayinfo.model;


/**
 * 每一条博客
 * @author zhou.ni	2015年4月8日
 */
public class BlogItem extends SuperBean{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title; 			//标题
	private String summary; 		// 简介
	private String published; 		//发布时间
	private String updated;			//更新时间
	private String name;			//博主姓名
	private String uri;				//博主家园的url
	private String avatar;			//博主头像的url
	private String href; 			//这篇博客内容链接
	private String blogapp;
	private String diggs; 			//赞
	private String views; 			//浏览
	private String comments; 		//评论
	
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getBlogapp() {
		return blogapp;
	}
	public void setBlogapp(String blogapp) {
		this.blogapp = blogapp;
	}
	public String getDiggs() {
		return diggs;
	}
	public void setDiggs(String diggs) {
		this.diggs = diggs;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "BlogItem [id=" + id + ", title=" + title + ", summary="
				+ summary + ", published=" + published + ", updated=" + updated
				+ ", name=" + name + ", uri=" + uri + ", avatar=" + avatar
				+ ", href=" + href + ", blogapp=" + blogapp + ", diggs="
				+ diggs + ", views=" + views + ", comments=" + comments + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof BlogItem ) {
			BlogItem item = (BlogItem) o;
			return this.id.equals(item.id);
		}
		return super.equals(o);
	}
	
}
