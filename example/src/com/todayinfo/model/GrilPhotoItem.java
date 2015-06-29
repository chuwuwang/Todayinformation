package com.todayinfo.model;

/**
 * 每一张美女图片
 * 
 * @author zhou.ni 2015年4月1日
 */
public class GrilPhotoItem {
	private String id; 				// id
	private String title; 			// 标题
	private String litpic; 			// 图片url
	private String click; 			// 点击率
	private String description;		// 描述
	private String lit_width; 		// 宽度
	private String lit_height; 		// 高度

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

	public String getLitpic() {
		return litpic;
	}

	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLit_width() {
		return lit_width;
	}

	public void setLit_width(String lit_width) {
		this.lit_width = lit_width;
	}

	public String getLit_height() {
		return lit_height;
	}

	public void setLit_height(String lit_height) {
		this.lit_height = lit_height;
	}

	@Override
	public String toString() {
		return "GrilPhotoItem [id=" + id + ", title=" + title + ", litpic="
				+ litpic + ", click=" + click + ", description=" + description
				+ ", lit_width=" + lit_width + ", lit_height=" + lit_height
				+ "]";
	}

}
