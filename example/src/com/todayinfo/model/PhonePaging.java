package com.todayinfo.model;

/**
 * 手机分页返回值
 * 
 * @author zhou.ni 2015年5月5日
 */
public class PhonePaging extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String total;			//多少篇文章
	private String currentPage;		//当前第几页
	private String pages;
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	
	@Override
	public String toString() {
		return "PhonePhotoPaging [total=" + total + ", currentPage="
				+ currentPage + ", pages=" + pages + "]";
	}
	
	
	
}
