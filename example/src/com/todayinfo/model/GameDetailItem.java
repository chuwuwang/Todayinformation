package com.todayinfo.model;


public class GameDetailItem extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String icon;
	private int pf;
	private String filesize;
	private String title;
	private String price;
	private String flashurl;
	private	String[] morepic;
	private String star;
	private int totaldown;
	private String pachagename;
	private String version;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getPf() {
		return pf;
	}
	public void setPf(int pf) {
		this.pf = pf;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFlashurl() {
		return flashurl;
	}
	public void setFlashurl(String flashurl) {
		this.flashurl = flashurl;
	}
	public String[] getMorepic() {
		return morepic;
	}
	public void setMorepic(String[] morepic) {
		this.morepic = morepic;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public int getTotaldown() {
		return totaldown;
	}
	public void setTotaldown(int totaldown) {
		this.totaldown = totaldown;
	}
	public String getPachagename() {
		return pachagename;
	}
	public void setPachagename(String pachagename) {
		this.pachagename = pachagename;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "GameDetailItem [id=" + id + ", icon=" + icon + ", pf=" + pf
				+ ", filesize=" + filesize + ", title=" + title + ", price="
				+ price + ", flashurl=" + flashurl + ", morepic=" + morepic
				+ ", star=" + star + ", totaldown=" + totaldown
				+ ", pachagename=" + pachagename + ", version=" + version
				+ ", getId()=" + getId() + ", getIcon()=" + getIcon()
				+ ", getPf()=" + getPf() + ", getFilesize()=" + getFilesize()
				+ ", getTitle()=" + getTitle() + ", getPrice()=" + getPrice()
				+ ", getFlashurl()=" + getFlashurl() + ", getMorepic()="
				+ getMorepic() + ", getStar()=" + getStar()
				+ ", getTotaldown()=" + getTotaldown() + ", getPachagename()="
				+ getPachagename() + ", getVersion()=" + getVersion()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
}
