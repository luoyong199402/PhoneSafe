package com.android.phone.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class PhoneSafeFinishPage extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_finish_page);
        sp = getSharedPreferences("config", PhoneSafeFinishPage.this.MODE_PRIVATE);

        //验证是否配置手机防盗
        if (!sp.getBoolean("is_config_phone_safe", false)) {
            enterPhoneSafeSetting(null);
        }
        // 加载信息
    }

    public void enterPhoneSafeSetting(View view) {
        Intent intent = new Intent(PhoneSafeFinishPage.this, PhoneSafeSetup1.class);
        startActivity(intent);
        finish();
    }
}
