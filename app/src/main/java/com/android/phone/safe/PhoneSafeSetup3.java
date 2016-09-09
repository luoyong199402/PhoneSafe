package com.android.phone.safe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PhoneSafeSetup3 extends BasePhoneSafeActivity implements View.OnClickListener {

    private Button btnLast;
    private Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_setup3);

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

    @Override
    public void lastStep() {
        Intent intent = new Intent(PhoneSafeSetup3.this, PhoneSafeSetup2.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pro_in, R.anim.tran_pro_out);
    }

    /**
     * 进入下一步设置的activity
     */
    @Override
    public void nextStep() {
        Intent intent = new Intent(PhoneSafeSetup3.this, PhoneSafeSetup4.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
}
