package com.todayinfo.model;

/**
 * 每一条游戏bean
 * 
 * @author zhou.ni 2015年5月17日
 */
public class GameItem extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String ztid;			//id
	public String ztname;		//name
	public String ztimg;		//image
	
	public String getZtid() {
		return ztid;
	}
	public void setZtid(String ztid) {
		this.ztid = ztid;
	}
	public String getZtname() {
		return ztname;
	}
	public void setZtname(String ztname) {
		this.ztname = ztname;
	}
	public String getZtimg() {
		return ztimg;
	}
	public void setZtimg(String ztimg) {
		this.ztimg = ztimg;
	}
	
	@Override
	public String toString() {
		return "GameItem [ztid=" + ztid + ", ztname=" + ztname + ", ztimg="
				+ ztimg + "]";
	}
	
	
}
