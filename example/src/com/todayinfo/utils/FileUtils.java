package com.todayinfo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

public class FileUtils {

	/**
	 * 获取sd卡路径
	 * 
	 * @return
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		if (sdDir == null)
			return null;
		return sdDir.toString();

	}

	public static String getConfigFilePath() {
		String sdPath = getSDPath();
		if (sdPath == null)
			return null;
		return getSDPath() + "/biandang/ServerConfig.txt";
	}

	/**
	 * encodeBase64File:(将文件转成base64 字符串).
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 */

	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	public static String Base64Pic(String path) throws Exception {
		Bitmap bm = getSmallBitmap(path, 300, 300);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath, int w, int h) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, w, h);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 获取图片缓存大小
	public static String getDiskCacheSize(Context context) {
		int sizeSum = 0;
		File diskfile = Utils.createDefaultCacheDir(context);
		String[] diskfileList = diskfile.list();
		for (int i = 0; i < diskfileList.length; i++) {
			File filesize = new File(diskfile + "/" + diskfileList[i]);
			sizeSum = sizeSum + (int) filesize.length();
			System.out.println(sizeSum);
		}
		DecimalFormat df = new DecimalFormat(".00");
		double sizeSumKB = sizeSum / 1024;
		if (sizeSumKB < 1) {
			return String.valueOf(sizeSum) + "B";
		} else {
			double sizeSumMB = sizeSumKB / 1024;
			if (sizeSumMB < 1) {
				return String.valueOf((int) sizeSumKB) + "KB";
			} else {
				return String.valueOf(df.format(sizeSumMB)) + "MB";
			}
		}
	}
}
