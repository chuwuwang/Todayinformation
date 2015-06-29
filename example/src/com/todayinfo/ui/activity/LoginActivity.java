package com.todayinfo.ui.activity;

import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinghua.todayinformation.R;
import com.todayinfo.model.User;
import com.todayinfo.utils.CharCheckUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMSsoHandler;

public class LoginActivity extends SuperActivity implements OnClickListener {

	private View view;
	private EditText phone;
	private EditText vCode;
	private TextView getVCode;
	private TextView login;
	private ImageView delete; 			// 输入的删除按钮
	private ImageView check;
	
	private boolean flag = true;

	private int countSeconds = 60;		// 倒数秒数

	@SuppressLint("HandlerLeak") 
	private Handler mCountHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (countSeconds > 0) {
				--countSeconds;
				getVCode.setText("发送验证码" + "(" + countSeconds + ")");
				mCountHanlder.sendEmptyMessageDelayed(0, 1000);
			} else {
				countSeconds = 60;
				getVCode.setText("发送验证码");
			}

		}
	};

	// 开始倒计时
	private void startCountBack() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getVCode.setText(countSeconds + "");
				mCountHanlder.sendEmptyMessage(0);
			}
		});

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(mContext, R.layout.activity_login, null);
		setContentView(view);
		
		phone = (EditText) view.findViewById(R.id.input_phone);
		vCode = (EditText) view.findViewById(R.id.code);
		login = (TextView) view.findViewById(R.id.login);
		getVCode = (TextView) view.findViewById(R.id.get_code);
		ImageView exit = (ImageView) view.findViewById(R.id.exit);
		delete = (ImageView) view.findViewById(R.id.input_delete);
		exit.setOnClickListener(this);
		delete.setOnClickListener(this);
		getVCode.setOnClickListener(this);
		login.setOnClickListener(this);
		delete.setVisibility(View.GONE);

		phone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					delete.setVisibility(View.VISIBLE);
				} else {
					delete.setVisibility(View.GONE);
				}
			}
		});
		
		check = (ImageView) this.findViewById(R.id.check);
		check.setOnClickListener(this);
		this.findViewById(R.id.user_tx).setOnClickListener(this);
		this.findViewById(R.id.user_ll).setOnClickListener(this);
		this.findViewById(R.id.login_qq).setOnClickListener(this);
		this.findViewById(R.id.login_weibo).setOnClickListener(this);
		
	}

	@Override
	public void retry() {
		
	}

	@Override
	public void netError() {
		
	}

	@Override
	public void pwdError() {
		
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_code:					// 获取手机号发送请求向手机发送验证码
			hideSoftInput(this, phone);
			if (countSeconds != 60) {
				showToast("您的验证码已发送!");
				return;
			}
			String phoneStr = phone.getText().toString();
			if (CharCheckUtil.isPhoneNum(phoneStr)) {
				showProgressDialog();
				showToast("发送成功");
				startCountBack();
			} else {
				showToast("输入的手机号码有误!");
			}
			break;
			
		case R.id.login:
			hideSoftInput(this, vCode);
			if ( LoginCheck() ){
				User user = new User();
				user.name = "资讯爱好者"; 
				user.uId = "111842201";
				user.gender = 1;
				user.birthday = System.currentTimeMillis()/1000;
				user.phone = phone.getText().toString();
				Toast.makeText(mContext, "您好，" + user.name, Toast.LENGTH_SHORT).show();
				mUserController.saveUserInfo(user);
				Intent intent =new Intent(mContext, MyInfoActivity.class);
				startActivity(intent);
				finish();
			}
			break;
			
		case R.id.exit:
			finish();
			break;
		case R.id.input_delete:
			phone.setText("");
			delete.setVisibility(View.GONE);
			break;
			
		case R.id.check:					//是否勾选用户协议
			if ( flag ) {
				flag = false;
				login.setEnabled(false);
				login.setBackgroundResource(R.color.C8C8C8);
				check.setImageResource(R.drawable.check_2);
			} else {
				flag = true;
				login.setEnabled(true);
				login.setBackgroundColor(Color.WHITE);
				check.setImageResource(R.drawable.check_1);
			}
			break;
			
		case R.id.user_ll:					//用户协议
		case R.id.user_tx:
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.login_qq:					//QQ登录
			QQLogin(SHARE_MEDIA.QQ);
			break;
		case R.id.login_weibo:				//微博登录
			SinaLogin(SHARE_MEDIA.SINA);
			break;
		default:
			break;
		}

	}
	
	/**
	 * QQ登录
	 * @param platform
	 */
	private void QQLogin(final SHARE_MEDIA platform) {
		final UMSocialService mController = mShareUtils.getmController();
		mController.doOauthVerify(this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
//				Toast.makeText(LoginActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
//				Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
//				 Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
				 String uid = value.getString("uid"); 	// 获取uid
				 final User qqUser = new User();
				 qqUser.qqId = uid;
				 if ( !TextUtils.isEmpty(uid) ) {
					 //获取相关授权信息
					 mController.getPlatformInfo(mContext, platform, new UMDataListener() {
						 
						 @Override
						 public void onStart() {
//							 Toast.makeText(mContext, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
						 }
						 
						 @Override
						 public void onComplete(int status, Map<String, Object> info) {
							 if(status == 200 && info != null){
								 StringBuilder sb = new StringBuilder();
								 Set<String> keys = info.keySet();
								 for(String key : keys){
									 sb.append(key+"="+info.get(key).toString()+",");
								 }
								 Log.d("TestData",sb.toString());
									qqUser.name = info.get( "screen_name").toString();
									if ( info.get( "gender").toString().equals("男") ) {
										qqUser.gender = 1;
									} else if ( info.get( "gender").toString().equals("女") ) {
										qqUser.gender = 2;
									} else {
										qqUser.gender = 0;
									}
									qqUser.picUrl = info.get("profile_image_url").toString();
									qqUser.city = info.get("city").toString();
									qqUser.province = info.get("province").toString();
									
									Toast.makeText(mContext, "您好，" + qqUser.name, Toast.LENGTH_SHORT).show();
									mUserController.saveUserInfo(qqUser);
									Intent intent =new Intent(mContext, MyInfoActivity.class);
									startActivity(intent);
									finish();
									
							 }else{
								 Log.d("TestData","发生错误："+status);
							 }
						 }
					 });
				 }
	
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
//				Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	/**
	 * Sina登录
	 * @param platform
	 */
	private void SinaLogin(final SHARE_MEDIA platform) {
		final UMSocialService mController = mShareUtils.getmController();
		mController.doOauthVerify(this, platform, new UMAuthListener() {
			
			@Override
			public void onStart(SHARE_MEDIA arg0) {
//				Toast.makeText(LoginActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
//				Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				final User sinaUser = new User();
				if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
//					Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT).show();
					String uid = value.getString("uid"); 	// 获取uid
					sinaUser.sinaId = uid;
					//获取相关授权信息
					mController.getPlatformInfo(mContext, platform, new UMDataListener() {
						
						@Override
						public void onStart() {
//							 Toast.makeText(mContext, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onComplete(int status, Map<String, Object> info) {
							if(status == 200 && info != null){
				                StringBuilder sb = new StringBuilder();
				                Set<String> keys = info.keySet();
				                for(String key : keys){
				                   sb.append(key+"="+info.get(key).toString()+"\r\n");
				                }
				                Log.d("TestData",sb.toString());
				                sinaUser.name = info.get( "screen_name").toString();
								if ( info.get( "gender").toString().equals("1") ) {
									sinaUser.gender = 1;
								} else if ( info.get( "gender").toString().equals("2") ) {
									sinaUser.gender = 2;
								} else {
									sinaUser.gender = 0;
								}
								sinaUser.picUrl = info.get("profile_image_url").toString();
								sinaUser.city = info.get("location").toString();
									
								Toast.makeText(mContext, "您好，" + sinaUser.name, Toast.LENGTH_SHORT).show();
								mUserController.saveUserInfo(sinaUser);
								Intent intent =new Intent(mContext, MyInfoActivity.class);
								startActivity(intent);
								finish();
				            }else{
				               Log.d("TestData","发生错误："+status);
				           }
						}
					});
					
				} else {
//					Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void onCancel(SHARE_MEDIA arg0) {
//				Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
			}
		});

	}

	/**
	 * 登录参数校验
	 * 
	 * @param phone
	 * @param pwd
	 * @return false || true
	 */
	public boolean LoginCheck() {
		if (!CharCheckUtil.isPhoneNum(phone.getText().toString())) {
			showToast("您输入的手机号码有误，请重新输入!");
			return false;
		}
		if (!CharCheckUtil.isAllDigit(vCode.getText().toString())) {
			showToast("您输入的验证码有误，请重新输入!");
			return false;
		}
		return true;
	}

	/**
	 *  如果有使用任一平台的SSO授权, 则必须在对应的activity中实现onActivityResult方法, 并添加如下代码
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mShareUtils.getmController().getConfig().getSsoHandler(resultCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	protected void obtainInfo() {
		
	}

}
