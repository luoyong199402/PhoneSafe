package com.android.phone.safe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联系人的内容提供者的工具类
 * Created by 罗勇 on 2016/9/9.
 */
public class PhoneContactUtils {
    /**
     * 获得所有的联系人
     *
     * @param context 应用上下文
     * @return 联系人集合
     */
    public static List<Map<String, String>> getAllConctact(Context context) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uriData = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = contentResolver.query(uri, new String[]{"contact_id"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex("contact_id"));
                if (contactId != null) {
                    //具体的某一个联系人
                    Map<String, String> map = new HashMap<String, String>();

                    Cursor dataCursor = contentResolver.query(uriData, new String[]{"data1", "mimetype"},
                            "contact_id=?", new String[]{contactId}, null);
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);

                        if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            //联系人的姓名
                            map.put("name", data1);
                        } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            //联系人的电话号码
                            map.put("phone", data1);
                        }
                    }
                    list.add(map);
                }
            }
        }
        return list;
    }
}
