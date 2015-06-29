package com.todayinfo.model;


/**
 * 单条新闻
 * 
 * @author zhou.ni 2015年3月22日
 */
public class NewItem extends SuperBean {

	private static final long serialVersionUID = 1L;

	private String id; 				// id
	private String title; 			// 标题
	private String summary; 		// 简介
	private String published; 		// 发布时间（格林威治时间）
	private String updated; 		// 更新时间
	private String link; 			// 链接
	private String diggs; 			// 获得赞数
	private String views; 			// 赞同观点数
	private String comments; 		// 评论数
	private String topic; 			// 置顶
	private String topicIcon; 		// 置顶图标
	private String sourceName; 		// 文章来源

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTopicIcon() {
		return topicIcon;
	}

	public void setTopicIcon(String topicIcon) {
		this.topicIcon = topicIcon;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", summary=" + summary
				+ ", published=" + published + ", updated=" + updated
				+ ", link=" + link + ", diggs=" + diggs + ", views=" + views
				+ ", comments=" + comments + ", topic=" + topic
				+ ", topicIcon=" + topicIcon + ", sourceName=" + sourceName
				+ "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof NewItem ) {
			NewItem item = (NewItem) o;
			return this.id.equals(item.id);
		}
		return super.equals(o);
	}

}
