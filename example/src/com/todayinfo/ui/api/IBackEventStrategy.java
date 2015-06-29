package com.todayinfo.ui.api;

/**
 * 对硬件返回按钮事件进行处理的策略接口 需要自行对返回按钮事件进行处理的fragment需要实现此接口
 * 
 * @author zhou.ni 2015年4月6日
 */
public interface IBackEventStrategy {
	/**
	 * @return true：吃掉事件,不交给上层处理。false：未处理返回事件,上交给上传处理
	 */
	public boolean backOperate();
}