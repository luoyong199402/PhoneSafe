package com.android.phone.safe.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 罗勇 on 2016/8/17.
 * 获取配置文件的信息
 */
public class PropertiesUtils {

    /**
     * 通过文件名（必须在assert目录下）和key获得配置文件的value值
     *
     * @param context  应用上下文
     * @param fileName 配置文件的文件名（文件必须在assert目录下）
     * @param key      配置文件的key
     * @return 配置文件的value
     */
    public static String getPropertiesInfoByKey(Context context, String fileName, String key) {
        AssetManager assets = context.getAssets();
        try {
            InputStream is = assets.open(fileName);
            Properties properties = new Properties();
            properties.load(is);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
