package com.todayinfo.model;

/**
 * 封装用户信息
 * 
 * @author longtao.li
 *
 */
public class User extends SuperBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String uId;			// 用户ID
	public String sinaId;		//新浪ID
	public String qqId;			//qqID
	public String name; 		// 用户名称
	public String picUrl; 		// 用户头像URL
	public String phone; 		// 绑定的手机号
	public int gender; 			// 性别(0:没有填写性别,1:男,2:女)
	public long birthday; 		// 生日(unix时间戳)
	public String province;		//省份
	public String country; 		//国家
	public String city;			//城市
	public String language;		//语言

}
