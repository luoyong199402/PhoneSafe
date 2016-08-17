package com.android.phone.safe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.phone.safe.utils.PackageManagerUtils;

public class SplashActivity extends AppCompatActivity {

    private TextView versionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        versionInfo = (TextView) findViewById(R.id.version_info);
        versionInfo.setText("版本号： " + PackageManagerUtils.getVersionNumber(this));
    }
}
