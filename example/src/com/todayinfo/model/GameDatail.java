package com.todayinfo.model;

/**
 * 游戏详细内容
 * 
 * @author zhou.ni 2015年5月17日
 */
public class GameDatail extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int ztstate;		//状态	
	private int ztid;
	private String ztname;
	private String intro;   	//简介
	private String ztimg;
	private int currentPage;
	private int totalCount;
	private int pageCount;
	
	public int getZtstate() {
		return ztstate;
	}
	public void setZtstate(int ztstate) {
		this.ztstate = ztstate;
	}
	public int getZtid() {
		return ztid;
	}
	public void setZtid(int ztid) {
		this.ztid = ztid;
	}
	public String getZtname() {
		return ztname;
	}
	public void setZtname(String ztname) {
		this.ztname = ztname;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getZtimg() {
		return ztimg;
	}
	public void setZtimg(String ztimg) {
		this.ztimg = ztimg;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	@Override
	public String toString() {
		return "GameDatail [ztstate=" + ztstate + ", ztid=" + ztid
				+ ", ztname=" + ztname + ", intro=" + intro + ", ztimg="
				+ ztimg + ", currentPage=" + currentPage + ", totalCount="
				+ totalCount + ", pageCount=" + pageCount + "]";
	}
	
}
