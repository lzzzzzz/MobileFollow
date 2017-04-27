package com.lz.mobilefollow.lzcore.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class PackageUtils {

	/**
	 * 获取版本名称
	 */
	public static String getAppVersion(Context context) {
		String version = "0";
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("SystemTool the application not found");
		}
		return version;
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = 0;
		try {
			PackageInfo pinfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
			versionCode = pinfo.versionCode;
		} catch (NameNotFoundException e) {
			// System.out.println("------>>>Utils getVersionCode() versionCode no found");
		}
		return versionCode;
	}
	
	
	/**
	 * Process signature param
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 获取当前屏幕的宽高
	 * @param activity
	 * @return
	 */
	public static int[] getScreenWidthandHeight(Activity activity) {
		int[] screen = new int[2];
//		WindowManager wm = (WindowManager) activity
//				.getSystemService(Context.WINDOW_SERVICE);
//		screen[0] = wm.getDefaultDisplay().getWidth();
//		screen[1] = wm.getDefaultDisplay().getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen[0] = dm.widthPixels;// 获取屏幕分辨率宽度
		screen[1] = dm.heightPixels;
		return screen;
	}
}
