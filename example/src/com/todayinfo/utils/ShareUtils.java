package com.todayinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.jinghua.todayinformation.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 友盟分享工具类
 * 
 * @author zhou.ni 2015年5月13日
 */
public class ShareUtils {
	
	private Context mContext;
	
	// 整个平台的Controller,负责管理整个SDK的配置、操作等处理
    private final UMSocialService mController = UMServiceFactory.getUMSocialService(Contacts.DESCRIPTOR);
    
    public ShareUtils(Context context) {
    	
    	this.mContext = context;
    	
    	// 配置需要分享的相关平台
    	configPlatforms();
    	
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
	}

	/**
	 * 获取分享控制对象
	 * @return
	 */
	public UMSocialService getmController() {
		return mController;
	}

    /**
     * 设置分享内容
     */
    public void setShareProgram(String params,String tile, String context){
    	String shareTitle = tile;
    	String shareContext = context;
    	String shareUrl= createAppShareUrl();
        UMImage urlImage = new UMImage(mContext, R.drawable.ic_launcher);
        //微信分享
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle(shareTitle);
        weixinContent.setShareContent(shareContext);
        weixinContent.setTargetUrl(shareUrl);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareContent(shareContext);
        circleMedia.setTargetUrl(shareUrl);
        circleMedia.setShareMedia(urlImage);
        mController.setShareMedia(circleMedia);
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setTitle(shareTitle);
        qzone.setShareContent(shareContext);
        qzone.setTargetUrl(shareUrl);
        qzone.setShareMedia(urlImage);
        mController.setShareMedia(qzone);
        //QQ分享
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setTitle(shareTitle);
        qqShareContent.setShareContent(shareContext);
        qqShareContent.setTargetUrl(shareUrl);
        qqShareContent.setShareMedia(urlImage);
        mController.setShareMedia(qqShareContent);
        //腾讯微博
        TencentWbShareContent tencent = new TencentWbShareContent();
        tencent.setTitle(shareTitle);
        tencent.setShareContent(shareContext);
        tencent.setTargetUrl(shareUrl);
        tencent.setShareMedia(urlImage);
        mController.setShareMedia(tencent);
        //新浪微博
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setTitle(shareTitle);
        sinaContent.setShareContent(shareContext);
        sinaContent.setTargetUrl(shareUrl);
        sinaContent.setShareMedia(urlImage);
        mController.setShareMedia(sinaContent);
    
    }
    
    /**
     * 配置分享平台参数
     */
    private void configPlatforms() {
    	// 设置新浪SSO handler
//    	mController.getConfig().setSsoHandler(new SinaSsoHandler());
    	
    	// 添加腾讯微博SSO授权
//    	mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
    	
        // 添加QQ、QZone平台
        addQQQZonePlatform();
        
        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }

    /**
     * 
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     *       
     * 参数1为当前Activity， 
     * 参数2为开发者在QQ互联申请的APP ID，
     * 参数3为开发者在QQ互联申请的APP kEY.      
     * @return
     * 
     */
    private void addQQQZonePlatform() {
    	String appId = Contacts.QQ_APP_ID;
		String appSecret = Contacts.QQ_APP_SECRET;
		
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, appId, appSecret);
        qqSsoHandler.setTargetUrl("http://www.woyouwaimai.com/d");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, appId, appSecret);
        qZoneSsoHandler.addToSocialSDK();
    }
    
    /**
     * @功能描述 : 添加微信平台分享
     * 
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//		String appId = Contacts.APP_ID;
//		String appSecret = Contacts.APP_SECRET;
		
        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(mContext, appId, appSecret);
//        wxHandler.addToSocialSDK();
//        wxHandler.setRefreshTokenAvailable(false);
//        
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appId, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
        
    }
    
    /**
     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
     */
    public void directShare(SHARE_MEDIA platform) {
    	
        mController.directShare(mContext, platform, new SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败 [" + eCode + "]";
                }
                Toast.makeText(mContext, showText, Toast.LENGTH_SHORT).show();
            }
        });
        
    }
    
    
    public String createAppShareUrl(){
    	String url = "http://android.myapp.com/myapp/detail.htm?apkName=com.jinghua.todayinformation";
		return url;
    }
}
