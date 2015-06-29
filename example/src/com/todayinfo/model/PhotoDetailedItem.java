package com.todayinfo.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailedItem extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String docid;
	private String cid; 			// 图片的id
	private String isCollected;		// 是否收藏
	private String title;			// 图片标题
	private String date;			// 日期
	private String author;			// 作者
	private List<String> content = new ArrayList<String>(); // 详细内容
	private String docUrl;
	private List<String> picUrl = new ArrayList<String>();		// url集
	
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getIsCollected() {
		return isCollected;
	}
	public void setIsCollected(String isCollected) {
		this.isCollected = isCollected;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<String> getContent() {
		return content;
	}
	public void setContent(ArrayList<String> content) {
		this.content = content;
	}
	public String getDocUrl() {
		return docUrl;
	}
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	public List<String> getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(ArrayList<String> picUrl) {
		this.picUrl = picUrl;
	}
	@Override
	public String toString() {
		return "PhotoDetailedItem [docid=" + docid + ", cid=" + cid
				+ ", isCollected=" + isCollected + ", title=" + title
				+ ", date=" + date + ", author=" + author + ", content="
				+ content + ", docUrl=" + docUrl + ", picUrl=" + picUrl + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof PhotoDetailedItem) {   
			PhotoDetailedItem item = (PhotoDetailedItem) o;   
            return this.docid.equals(item.docid);   
        }  
		return super.equals(o);
	}
	
}
