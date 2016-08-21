package com.android.phone.safe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.phone.safe.ui.ISettingItem;
import com.android.phone.safe.ui.SettingItem;

public class SettingActivity extends AppCompatActivity implements ISettingItem {

    /**
     * 日志信息
     */
    private static final String TAG = "SettingActivity";

    /**
     * 配置文件类
     */
    private SharedPreferences sp;

    /**
     * 使用自动组合控件
     */
    private SettingItem autoUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", SettingActivity.this.MODE_PRIVATE);
        autoUpdate = (SettingItem) findViewById(R.id.auto_update);
        autoUpdate.setOnClickListener(this);

        // 检测是否自动更新
        boolean appAutoUpdate = sp.getBoolean("app_auto_update", false);
        autoUpdate.setState(appAutoUpdate);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor edit = sp.edit();
        switch (view.getId()) {
            case R.id.auto_update:
                if (autoUpdate.getState()) {
                    edit.putBoolean("app_auto_update", false);
                    autoUpdate.setState(false);
                } else {
                    autoUpdate.setState(true);
                    edit.putBoolean("app_auto_update", true);
                }
                edit.commit();
                break;
            case R.id.test:
                break;
            default:
                Log.d(TAG, "onClick: 系统错误");
                break;
        }
    }
}
