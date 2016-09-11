package com.android.phone.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneSafeFinishPage extends AppCompatActivity {

    private SharedPreferences sp;

    private TextView securityPhoneNumber;
    private ImageView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_finish_page);
        sp = getSharedPreferences("config", PhoneSafeFinishPage.this.MODE_PRIVATE);

        //验证是否配置手机防盗
        if (!sp.getBoolean("isConfigPhoneSafe", false)) {
            enterPhoneSafeSetting(null);
        }

        // 初始化组件
        securityPhoneNumber = (TextView) findViewById(R.id.security_phone_number);
        state = (ImageView) findViewById(R.id.state);

        // 初始化视图
        securityPhoneNumber.setText(sp.getString("securityPhoneNumber", ""));
        state.setImageResource(sp.getBoolean("isOpenAntitheftProtection", false) ? R.drawable.lock :
                R.drawable.unlock);
    }

    /**
     * 进入防盗设置界面
     * @param view
     */
    public void enterPhoneSafeSetting(View view) {
        Intent intent = new Intent(PhoneSafeFinishPage.this, PhoneSafeSetup1.class);
        startActivity(intent);
        finish();
    }
}
