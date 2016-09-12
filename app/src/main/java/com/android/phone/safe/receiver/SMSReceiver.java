package com.android.phone.safe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.phone.safe.R;
import com.android.phone.safe.service.GPSService;

/**
 * 短信的广播接受者
 * Created by 罗勇 on 2016/9/11.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";
    private DevicePolicyManager dpm;

    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
            String sender = sms.getOriginatingAddress();
            String content = sms.getMessageBody();
            if (sp.getString("securityPhoneNumber", "").equals(sender)) {  // 表示安全号码一致
                if ("#*localtion*#".equals(content)) {
                    Log.d(TAG, "onReceive: 发送地址");
                    // 启动服务 (启动gps追踪服务)
                    Intent GPSIntent = new Intent(context, GPSService.class);
                    context.startService(GPSIntent);

                    String gpsLaction = sp.getString("GPSLaction", null);
                    if (gpsLaction == null) {
                        SmsManager.getDefault().sendTextMessage(sender, null, "geting loaction.....", null, null);
                    } else {
                        SmsManager.getDefault().sendTextMessage(sender, null, gpsLaction, null, null);
                    }
                    abortBroadcast();
                } else if ("#*alarm*#".equals(content)) {
                    Log.d(TAG, "onReceive: 打开报警音乐");
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setLooping(false);
                    player.setVolume(1.0f, 1.0f);
                    player.start();
                    abortBroadcast();
                } else if ("#*wipedata*#".equals(content)) {
                    Log.d(TAG, "onReceive: 删除数据");
                    dpm.wipeData(DevicePolicyManager.WIPE_RESET_PROTECTION_DATA);
                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(content)) {
                    Log.d(TAG, "onReceive: 锁屏");
                    dpm.lockNow();
                    abortBroadcast();
                }
            }

        }
    }
}
