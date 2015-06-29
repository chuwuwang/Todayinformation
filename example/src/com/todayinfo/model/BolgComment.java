package com.todayinfo.model;

/**
 * 博客评论
 * 
 * @author zhou.ni 2015年5月17日
 */
public class BolgComment extends SuperBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title; 		// 评论者的标题
	private String published; 	// 出版时间
	private String updated; 	// 更新时间
	private String name; 		// 评论者的名字
	private String uri; 		// 评论者的博客地址
	private String content; 	// 评论的内容

	public BolgComment() {
		super();
	}

	public BolgComment(String id, String title, String published, String updated,
			String name, String uri, String content) {
		super();
		this.id = id;
		this.title = title;
		this.published = published;
		this.updated = updated;
		this.name = name;
		this.uri = uri;
		this.content = content;
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Comments [id=" + id + ", title=" + title + ", published="
				+ published + ", updated=" + updated + ", name=" + name
				+ ", uri=" + uri + ", content=" + content + "]";
	}

}
