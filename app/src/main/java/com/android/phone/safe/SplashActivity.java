package com.android.phone.safe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.phone.safe.utils.PackageManagerUtils;
import com.android.phone.safe.utils.PropertiesUtils;
import com.android.phone.safe.utils.StreamTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private TextView versionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 动态修改应用程序的版本信息
        Log.d(TAG, "onCreate: 获取版本信息");
        versionInfo = (TextView) findViewById(R.id.version_info);
        versionInfo.setText("版本号： " + PackageManagerUtils.getVersionName(this));

        // 联网检查更新
        Log.d(TAG, "onCreate: 联网检测更新");
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkVersionUpdate();
            }
        }).start();

    }

    /**
     * 联网检测应用程序的更新
     */
    private void checkVersionUpdate() {
        String urlStr = PropertiesUtils.getPropertiesInfoByKey(this, "config.properties", "app.update.address");
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(3000);
            InputStream inputStream = conn.getInputStream();
            String val = StreamTools.readFromStream(inputStream);
            System.out.println(val);
            JSONObject jsonObject = new JSONObject(val);
            String str = (String) jsonObject.get("url");
            String code = (String) jsonObject.get("code");
            Log.d(TAG, "checkVersionUpdate: " + code);

        } catch (MalformedURLException e) {
            Log.d(TAG, "checkVersionUpdate: url非法");
        } catch (JSONException e) {
            Log.d(TAG, "checkVersionUpdate: json转换数据异常");
        } catch (ProtocolException e) {
            Log.d(TAG, "checkVersionUpdate: 连接协议异常");
        } catch (IOException e) {
            Log.d(TAG, "checkVersionUpdate: 读写异常");
        }

    }

}
