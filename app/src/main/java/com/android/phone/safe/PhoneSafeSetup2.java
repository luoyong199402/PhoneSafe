package com.android.phone.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.android.phone.safe.ui.SettingItem;

public class PhoneSafeSetup2 extends BasePhoneSafeActivity implements View.OnClickListener {

    /**
     * 操作配置文件对象
     */
    private SharedPreferences sp;

    /**
     * 上一步，下一步按钮
     */
    private Button btnLast, btnNext;

    /**
     * 自定以组合对象
     */
    private SettingItem bindSim;

    /**
     * 手机管理
     */
    private TelephonyManager telephonyManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_setup2);

        // 初始化控件
        btnLast = (Button) findViewById(R.id.last);
        btnNext = (Button) findViewById(R.id.next);
        bindSim = (SettingItem) findViewById(R.id.bindSIM);
        sp = getSharedPreferences("config", PhoneSafeSetup2.MODE_PRIVATE);
        telephonyManager = (TelephonyManager) getSystemService(PhoneSafeSetup2.TELEPHONY_SERVICE);

        // 绑定事件
        btnLast.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        bindSim.setOnClickListener(this);

        // 初始化界面 (绑定sim卡)
        if (sp.getBoolean("isBindSim", false)) {
            bindSim.setState(true);
        } else {
            bindSim.setState(false);
        }
    }

    /**
     * 按钮和自定组合控件点击回调
     * @param view 被点击的视图
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:     //执行下一步操作
                nextStep();
                break;
            case R.id.last:     // 执行上一步操作
                lastStep();
                break;
            case R.id.bindSIM:  //  执行BindSim卡操作
                boolean state = bindSim.getState();
                SharedPreferences.Editor edit = sp.edit();
                if (!state) {  // 修改状态并设置值sim卡的值
                    edit.putBoolean("isBindSim", true);
                    edit.putString("simValue", telephonyManager.getSimSerialNumber());
                    bindSim.setState(true);
                } else {
                    edit.putBoolean("isBindSim", false);
                    edit.putString("simValue", null);
                    bindSim.setState(false);
                }
                edit.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void lastStep() {
        Intent intent = new Intent(PhoneSafeSetup2.this, PhoneSafeSetup1.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pro_in, R.anim.tran_pro_out);
    }

    /**
     * 进入下一步设置的activity
     */
    @Override
    public void nextStep() {
        Intent intent = new Intent(PhoneSafeSetup2.this, PhoneSafeSetup3.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
}
