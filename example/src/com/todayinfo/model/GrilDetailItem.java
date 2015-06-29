package com.todayinfo.model;

/**
 * 每一条美女图片详情
 * 
 * @author zhou.ni 2015年4月25日
 */
public class GrilDetailItem extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url;
	private String text;
	private String width;
	private String height;
	private String aid;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	@Override
	public String toString() {
		return "GrilDetailItem [url=" + url + ", text=" + text + ", width="
				+ width + ", height=" + height + ", aid=" + aid + "]";
	}

}
