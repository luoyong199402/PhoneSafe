package com.android.phone.safe.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by 罗勇 on 2016/8/17.
 * 获得应用程序的包信息
 */
public class PackageManagerUtils {

    private static final String TAG = "PackageManagerUtils";

    /**
     * 获得当前应用程序的版本名字
     *
     * @param context 应用上下文
     * @return 应用程序的版本名字
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? "" : packageInfo.versionName;
    }

    /**
     * 获得当前应用程序的版本号
     *
     * @param context 应用上下文
     * @return 应用程序的版本号
     */
    public static int getVersionNumber(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? -1 : packageInfo.versionCode;
    }

    /**
     * 获得当前应用程序的包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "getVersionNumber: " + e.getMessage());
        }
        return packageInfo;
    }

    /**
     * 安装apk的工具方法
     *
     * @param context 应用上下文
     * @param file    新的apk的文件
     */
    public static void installAPK(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


}
