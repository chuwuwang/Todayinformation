package com.todayinfo.ui.activity;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinghua.todayinformation.R;
import com.squareup.picasso.Picasso;
import com.todayinfo.model.User;
import com.todayinfo.ui.component.DateDialog;
import com.todayinfo.ui.component.DateDialog.DateBtnListener;
import com.todayinfo.ui.component.pulltozoomview.PullToZoomScrollViewEx;
import com.todayinfo.utils.DateUtils;
import com.todayinfo.utils.SharedpreferncesUtil;

/**
 * 用户信息界面
 * 
 * @author zhou.ni 2015年4月20日
 */
public class MyInfoActivity extends SuperActivity implements OnClickListener{
	
	private static final String TAG = "MyInfoActivity";
	
	private ImageView logo;
	private TextView nameText;
	private TextView sexText;
	private TextView birthdayText;
	
	private  ViewStub viewStubModifyName; 		//修改姓名
	private  ViewStub viewStubModifySex; 		//修改性别
	private  ViewStub viewStubExit; 			//退出账户
	private  ViewStub viewStubModifyPic;		//修改头像
	
	private PullToZoomScrollViewEx scrollView;
	
	private User user =  new User();
	private DateDialog dateDialog = new DateDialog();
	
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int PHOTO_GRAPH = 1; 				// 拍照
	private static final int PHOTO_ZOOM = 2; 				// 缩放
	private static final int PHOTO_RESOULT = 3; 			// 结果
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		getWindow().setBackgroundDrawable(null);
		initView();
	}
	
	/**
	 * 初始化控件
	 */
	@SuppressWarnings("unused")
	@SuppressLint("InflateParams") 
	private void initView() {
		RelativeLayout headView = (RelativeLayout) this.findViewById(R.id.head);
		RelativeLayout back = (RelativeLayout) headView.findViewById(R.id.back_left);
		TextView title = (TextView) headView.findViewById(R.id.head_title);
		
		scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
	    View zoomView = LayoutInflater.from(this).inflate(R.layout.include_info_head, null, false);
	    View contentView = LayoutInflater.from(this).inflate(R.layout.include_info_content, null, false);
	    scrollView.setZoomView(zoomView);
	    scrollView.setScrollContentView(contentView);
	        
		logo = (ImageView) zoomView.findViewById(R.id.logo);
		logo.setOnClickListener(this);
		ImageView carmera = (ImageView) zoomView.findViewById(R.id.camera_photo);
		carmera.setOnClickListener(this);
		
		nameText = (TextView) contentView.findViewById(R.id.name);
		RelativeLayout nameRl = (RelativeLayout) contentView.findViewById(R.id.name_rl);
		nameRl.setOnClickListener(this);
		sexText = (TextView) contentView.findViewById(R.id.sex);
		RelativeLayout sexRl = (RelativeLayout) contentView.findViewById(R.id.sex_rl);
		sexRl.setOnClickListener(this);
		birthdayText = (TextView) contentView.findViewById(R.id.brithday);
		RelativeLayout birthdayRl = (RelativeLayout) contentView.findViewById(R.id.brithday_rl);
		birthdayRl.setOnClickListener(this);
		TextView exit = (TextView) contentView.findViewById(R.id.exit);
		exit.setOnClickListener(this);
		
		back.setOnClickListener(this);
		title.setOnClickListener(this);
		title.setText("个人资料");
		
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
		
		// 更新用户生日
		dateDialog.setDateBtnListener(new DateBtnListener() {
			@Override
			public void pressDateBtn(int id) {
				switch (id) {
				case R.id.ok_button:
					String brithday = birthdayText.getText().toString();
					if ( !TextUtils.isEmpty(brithday) ){
						long date = DateUtils.getString2Date(brithday);
						Log.i(TAG, "" + date);
						user.birthday = date;
						SharedpreferncesUtil.saveUserInfo(mContext, user);
						Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			}
		});
		
		fullData();
	}
	
	/**
	 * 填充数据数据
	 * @param resul
	 */
	private void fullData(){
		User userInfo = mUserController.getUserInfo();
		if ( userInfo!=null ) {
			user  = userInfo;
			if( !TextUtils.isEmpty(userInfo.picUrl) ){
				Picasso.with(mContext).load(userInfo.picUrl).placeholder(R.drawable.touxing).error(R.drawable.touxing).into(logo);
			}
			
			if ( !TextUtils.isEmpty(userInfo.name) ) {
				nameText.setText(userInfo.name);
			} else {
				nameText.setText("");
			}
			
			if ( userInfo.gender==2 ) {
				sexText.setText("女");
			} else if( userInfo.gender==1 ){
				sexText.setText("男");
			}else {
				sexText.setText("-");
			}
			
			if( userInfo.birthday != 0 ){
				birthdayText.setText(DateUtils.formatDate(userInfo.birthday));
			}else{
				birthdayText.setText("");
			}
			
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_left:
		case R.id.head_title:
			finish();
			break;
			
		case R.id.name_rl:									//修改用户姓名
			if ( viewStubModifyName==null ) {
				viewStubModifyName = (ViewStub) this.findViewById(R.id.modify_name);
				View inflatedView = viewStubModifyName.inflate();
				TextView ok = (TextView) inflatedView.findViewById(R.id.ok_button);
				TextView cancel = (TextView) inflatedView.findViewById(R.id.cancel_button);
				RelativeLayout backarea = (RelativeLayout) inflatedView.findViewById(R.id.blankarea);
				final EditText nameEdit = (EditText) inflatedView.findViewById(R.id.name_edittext);
				if( mUserController.getUserInfo()!=null ) {
					nameEdit.setText(mUserController.getUserInfo().name);
				}
				backarea.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubModifyName.setVisibility(View.GONE);
					}
				});
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String newName = nameEdit.getText().toString();
						if ( !TextUtils.isEmpty(newName) ) {
							user.name = newName;
							SharedpreferncesUtil.saveUserInfo(mContext, user);
							Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
							viewStubModifyName.setVisibility(View.GONE);
						} else {
							showToast("用户名不能为空");
						}
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubModifyName.setVisibility(View.GONE);
					}
				});
			}
			viewStubModifyName.setVisibility(View.VISIBLE);
			break;
			
		case R.id.brithday_rl:								//修改用户生日
			dateDialog.showDatePicker(this, birthdayText);
			break;			
			
		case R.id.sex_rl:									//修改用户性别
			if ( viewStubModifySex==null ) {
				viewStubModifySex = (ViewStub) this.findViewById(R.id.modify_sex);
				View inflatedView = viewStubModifySex.inflate();
				TextView ok = (TextView) inflatedView.findViewById(R.id.ok_button);
				TextView cancel = (TextView) inflatedView.findViewById(R.id.cancel_button);
				final TextView radiobtnOne = (TextView) inflatedView.findViewById(R.id.nan_radiobtn);
				final TextView radiobtnTow = (TextView) inflatedView.findViewById(R.id.nv_radiobtn);
				RelativeLayout backarea = (RelativeLayout) inflatedView.findViewById(R.id.blankarea);
				LinearLayout nan = (LinearLayout) inflatedView.findViewById(R.id.nan);
				LinearLayout nv = (LinearLayout) inflatedView.findViewById(R.id.nv);
				nan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setRbCheck(radiobtnOne, radiobtnTow);
						user.gender = 1;//男
					}
				});
				nv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setRbCheck(radiobtnTow, radiobtnOne);
						user.gender = 2;//女
					}
				});
				if ( mUserController.getUserInfo()!=null ) {
					int gender = mUserController.getUserInfo().gender;
					if ( gender == 2 ) {
						radiobtnTow.setBackgroundResource(R.drawable.rediobox1);
					} else if( gender == 1 ) {
						radiobtnOne.setBackgroundResource(R.drawable.rediobox1);
					} 
				}
				backarea.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubModifySex.setVisibility(View.GONE);
					}
				});
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedpreferncesUtil.saveUserInfo(mContext, user);
						Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
						viewStubModifySex.setVisibility(View.GONE);
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubModifySex.setVisibility(View.GONE);
					}
				});
			}
			viewStubModifySex.setVisibility(View.VISIBLE);
			break;
			
		case R.id.exit:											//退出当前账户
			if ( viewStubExit==null ) {
				viewStubExit = (ViewStub) this.findViewById(R.id.confirm_exit);
				View inflatedView = viewStubExit.inflate();
				TextView ok = (TextView) inflatedView.findViewById(R.id.ok_button);
				TextView cancel = (TextView) inflatedView.findViewById(R.id.cancel_button);
				TextView phoneSelect = (TextView) inflatedView.findViewById(R.id.phone_select);
				RelativeLayout backarea = (RelativeLayout) inflatedView.findViewById(R.id.blankarea);
				backarea.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubExit.setVisibility(View.GONE);
					}
				});
				if ( mUserController.getUserInfo()!=null ) {
					phoneSelect.setText(mUserController.getUserInfo().phone);
				}
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {    
						// 清除用户信息缓存
						mUserController. loginOut();
						viewStubExit.setVisibility(View.GONE);
						finish();						
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubExit.setVisibility(View.GONE);
					}
				});
			} 
			viewStubExit.setVisibility(View.VISIBLE);
			break;
			
		case R.id.logo: 
		case R.id.camera_photo:     					 //换图片
			if ( viewStubModifyPic==null ) {
				viewStubModifyPic = (ViewStub) this.findViewById(R.id.modify_picture);
				View inflatedView = viewStubModifyPic.inflate();
				TextView cancel = (TextView) inflatedView.findViewById(R.id.select_cancel);
				TextView selectPicture = (TextView) inflatedView.findViewById(R.id.photo_album);
				TextView selectCamera = (TextView) inflatedView.findViewById(R.id.photo_camera);
				RelativeLayout backarea = (RelativeLayout) inflatedView.findViewById(R.id.blankarea);
				backarea.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubModifyPic.setVisibility(View.GONE);
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewStubModifyPic.setVisibility(View.GONE);
					}
				});
				selectPicture.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
						startActivityForResult(intent, PHOTO_ZOOM);
						viewStubModifyPic.setVisibility(View.GONE);
					}
				});
				selectCamera.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"temp.jpg")));
						startActivityForResult(intent, PHOTO_GRAPH);
						viewStubModifyPic.setVisibility(View.GONE);
					}
				});
			} 
			viewStubModifyPic.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == PHOTO_GRAPH && resultCode == -1) {		// 拍照
			// 设置文件保存路径
			File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
			String path = picture.getPath();
			if (path != null && !path.equals("")) {
				startPhotoZoom(Uri.fromFile(picture));
			}
		} else if (requestCode == PHOTO_ZOOM && data != null) {		 	// 读取相册缩放图片
			Uri originalUri = data.getData(); 					 		// 获得图片的uri
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.managedQuery(originalUri, proj, null, null, null);
			if (cursor != null) {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String path = cursor.getString(column_index);
				Log.i(TAG, "before:" + path);
				startPhotoZoom(data.getData());
			} else {
				Toast.makeText(mContext, "读取相册文件失败", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == PHOTO_RESOULT && data != null) { 	// 处理结果
			if ( mUserController.getUserInfo()!=null ) {
				//处理图片
				handlePicture(getTempUri());
				logo.setImageURI(getTempUri());
			}
			Log.i(TAG, getTempUri().getPath());
		}
	}
	
	/**
	 * 处理磁盘图片,如果大于512*512 则缩小图片
	 */
	private void handlePicture(final Uri uri){
		Rect rect = mBitmapUtils.getBitmapBounds(uri);
		int area = rect.right*rect.bottom;
		if( area > 512*512 ){
			Bitmap bitmap = mBitmapUtils.getBitmap(uri, 512, 512);
			mBitmapUtils.saveBitmap(bitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), TEMP_PHOTO_FILE, Bitmap.CompressFormat.JPEG);
		}
				
	}
	
	/**
	 * 收缩图片
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// //aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", 720);
//		intent.putExtra("outputY", 480);
		intent.putExtra("return-data", false);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, PHOTO_RESOULT);
	}
	
	/**
	 * 生成一个图片URI
	 * 
	 * @param position
	 *            图片角标
	 * @return
	 */
	private Uri getTempUri() {
		
		return Uri.fromFile(getTempFile());
		
	}
	
	public static final String TEMP_PHOTO_FILE = "vclubs_head_img";

	private File getTempFile() {

		if (isSDCARDMounted()) {

			File f = new File(Environment.getExternalStorageDirectory(),

			TEMP_PHOTO_FILE + ".jpg");

			try {

				f.createNewFile();

			} catch (IOException e) {

				Toast.makeText(mContext, "创建文件失败", Toast.LENGTH_LONG).show();

			}

			return f;

		} else {

			return null;

		}

	}
	
	private boolean isSDCARDMounted() {

		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED))

			return true;

		return false;

	}
	
	
	/**
	 * 单选按钮切换
	 * @param rbTrue
	 * @param rbFlase
	 */
	private void setRbCheck(TextView rbTrue, TextView rbFlase) {
		rbTrue.setBackgroundResource(R.drawable.rediobox1);
		rbFlase.setBackgroundResource(R.drawable.rediobox2);
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
	protected void obtainInfo() {
		
	}

}
