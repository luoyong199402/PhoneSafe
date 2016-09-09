package com.android.phone.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PhoneSafeSetup4 extends BasePhoneSafeActivity implements View.OnClickListener {

    private SharedPreferences sp;

    private Button btnLast;
    private Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_setup4);
        sp = getSharedPreferences("config", PhoneSafeSetup4.MODE_PRIVATE);

        btnLast = (Button) findViewById(R.id.last);
        btnNext = (Button) findViewById(R.id.next);

        btnLast.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                nextStep();
                break;
            case R.id.last:
                lastStep();
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
        edit.putBoolean("is_config_phone_safe", true);
        edit.commit();
        Intent intent = new Intent(PhoneSafeSetup4.this, PhoneSafeFinishPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
}
