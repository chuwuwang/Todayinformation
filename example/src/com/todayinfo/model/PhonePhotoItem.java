package com.todayinfo.model;

/**
 * 每一条手机图片
 * 
 * @author zhou.ni 2015年5月5日
 */
public class PhonePhotoItem extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String content;		//文章内容
	private String author;
	private String docid;		//文章id
	private String title;		//文章标题
	private String pic;
	private String date;		//时间
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "PhonePhotoItem [content=" + content + ", author=" + author
				+ ", docid=" + docid + ", title=" + title + ", pic=" + pic
				+ ", date=" + date + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof PhonePhotoItem) {   
			PhonePhotoItem item = (PhonePhotoItem) o;   
            return this.docid.equals(item.docid);   
        }  
		return super.equals(o);
	}
	
}
