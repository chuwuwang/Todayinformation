package com.todayinfo.model;

/**
 * 每一篇纯文字文章
 * 
 * @author zhou.ni 2015年5月20日
 */
public class TextArticleItem extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String content;
	private String docid;
	private String title;
	private String docUrl;
	private String date;
	private String type;// 返回doc是普通文章浏览形式 返回pic是文章图片浏览形式
	private String cid; // 收藏分类id,1为谍报收藏 2为生活收藏 3为图片收藏
	private int isCollected; // 表示是否收藏，1为收藏，0为未收藏

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public int getIsCollected() {
		return isCollected;
	}

	public void setIsCollected(int isCollected) {
		this.isCollected = isCollected;
	}

	@Override
	public String toString() {
		return "TextArticleItem [content=" + content + ", docid=" + docid
				+ ", title=" + title + ", docUrl=" + docUrl + ", date=" + date
				+ ", type=" + type + ", cid=" + cid + ", isCollected="
				+ isCollected + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof TextArticleItem ) {
			TextArticleItem item = (TextArticleItem) o;
			return this.docid.equals(item.docid);
		}
		return super.equals(o);
	}

}
