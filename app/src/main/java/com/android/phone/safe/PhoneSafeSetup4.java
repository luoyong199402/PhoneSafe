package com.android.phone.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class PhoneSafeSetup4 extends BasePhoneSafeActivity implements View.OnClickListener {

    private SharedPreferences sp;

    private Button btnLast;
    private Button btnNext;
    private CheckBox ckAntitheftProtection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_setup4);
        sp = getSharedPreferences("config", PhoneSafeSetup4.MODE_PRIVATE);

        // 初始化控件
        btnLast = (Button) findViewById(R.id.last);
        btnNext = (Button) findViewById(R.id.next);
        ckAntitheftProtection = (CheckBox) findViewById(R.id.antitheft_protection);

        // 初始化事件
        btnLast.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ckAntitheftProtection.setOnClickListener(this);

        // 初始化视图
        ckAntitheftProtection.setChecked(sp.getBoolean("isOpenAntitheftProtection", false));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:  // 下一步
                nextStep();
                break;
            case R.id.last: // 上一步
                lastStep();
                break;
            case R.id.antitheft_protection:  // 防盗保护按钮
                SharedPreferences.Editor edit = sp.edit();
                if (ckAntitheftProtection.isChecked()) {  // 防盗按钮是否被点击
                    edit.putBoolean("isOpenAntitheftProtection", true);
                } else {
                    edit.putBoolean("isOpenAntitheftProtection", false);
                }
                edit.commit();
                break;
            default:
                break;
        }
    }

    public void lastStep() {
        Intent intent = new Intent(PhoneSafeSetup4.this, PhoneSafeSetup3.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pro_in, R.anim.tran_pro_out);
    }

    /**
     * 进入下一步设置的activity
     */
    public void nextStep() {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isConfigPhoneSafe", true);
        edit.commit();

        Intent intent = new Intent(PhoneSafeSetup4.this, PhoneSafeFinishPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
}
