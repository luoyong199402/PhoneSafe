package com.android.phone.safe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PhoneSafeSetup1 extends BasePhoneSafeActivity implements View.OnClickListener {

    // 下一步按钮
    private Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_setup1);

        btnNext = (Button) findViewById(R.id.next);

        btnNext.setOnClickListener(this);
    }

    /**
     * 点击按钮的回调方法
     * @param view 点击的视图对象
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                nextStep();
                break;
            default:
                break;
        }
    }

    /**
     * 进入下一步设置的activity
     * nextStep
     */
    @Override
    public void nextStep() {
        Intent intent = new Intent(PhoneSafeSetup1.this, PhoneSafeSetup2.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void lastStep() {
    }
}
