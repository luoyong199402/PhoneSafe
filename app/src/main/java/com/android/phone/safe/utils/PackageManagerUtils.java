package com.android.phone.safe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by 罗勇 on 2016/8/17.
 * 获得应用程序的包信息
 */
public class PackageManagerUtils {

    private static final String TAG = "PackageManagerUtils";

    /**
     * 获得应用程序的版本号
     * @param context 应用上下文
     * @return 应用程序的版本号
     */
    public static String getVersionNumber(Context context) {
        String versionNumber = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionNumber = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "getVersionNumber: " + e.getMessage());
            versionNumber = "";
        }
        return versionNumber;
    }
}
