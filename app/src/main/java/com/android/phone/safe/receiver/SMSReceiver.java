package com.android.phone.safe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * 短信的广播接受者
 * Created by 罗勇 on 2016/9/11.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", context.MODE_PRIVATE);

        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
            String sender = sms.getOriginatingAddress();
            String content = sms.getMessageBody();
            if (sp.getString("securityPhoneNumber", "").equals(sender)) {  // 表示安全号码一致
                if ("#*localtion*#".equals(content)) {
                    Log.d(TAG, "onReceive: 发送地址");
                    abortBroadcast();
                } else if ("#*alarm*#".equals(content)) {
                    Log.d(TAG, "onReceive: 打开报警音乐");
                    abortBroadcast();
                } else if ("#*wipedata*#".equals(content)) {
                    Log.d(TAG, "onReceive: 删除数据");
                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(content)) {
                    Log.d(TAG, "onReceive: 锁屏");
                    abortBroadcast();
                }
            }

        }
    }
}
