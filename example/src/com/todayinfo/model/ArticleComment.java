package com.todayinfo.model;

public class ArticleComment extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uid;
	private String username;
	private String date;
	private String avatar;
	private String content;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ArticleComment [uid=" + uid + ", username=" + username
				+ ", date=" + date + ", avatar=" + avatar + ", content="
				+ content + "]";
	}

}
