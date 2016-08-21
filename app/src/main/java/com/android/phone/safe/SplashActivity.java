package com.android.phone.safe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.phone.safe.utils.PackageManagerUtils;
import com.android.phone.safe.utils.PropertiesUtils;
import com.android.phone.safe.utils.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    /**
     * 日志消息
     */
    private static final String TAG = "SplashActivity";

    /**
     * 进入主页的时间管理器
     */
    private long timeManager;

    /**
     * 配置文件管理器
     */
    private SharedPreferences sp;

    /**
     * 检测更新
     */
    private static final int NEED_UPDATE = 0;
    private static final int URL_ILLEGAL = 1;
    private static final int JSON_CONVERT_EXECPTION = 2;
    private static final int PROTOCOL_EXCEPTION = 3;
    private static final int IO_EXCEPTION = 4;
    private static final int NOT_UPDATE = 5;


    /**
     * 下载管理
     */
    private static final int DOWNLOAD_SUCCESS = 0;
    private static final int SDCARD_INEXISTENCE = 1;
    private static final int DOWNLOAD_FAILUER = 2;

    /**
     * Android下的组件
     */
    private TextView versionInfo;

    /**
     * 检测版本更新的hander
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEED_UPDATE:   // 下载apk, 并安装
                    String url = (String) msg.obj;
                    showUpdateDialog(url);
                    break;
                case URL_ILLEGAL:
                    Toast.makeText(SplashActivity.this, "url非法", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
                case JSON_CONVERT_EXECPTION:
                    Toast.makeText(SplashActivity.this, "json转换异常", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
                case PROTOCOL_EXCEPTION:
                    Toast.makeText(SplashActivity.this, "协议异常", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
                case IO_EXCEPTION:
                    Toast.makeText(SplashActivity.this, "io异常", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
                case NOT_UPDATE:
                    Toast.makeText(SplashActivity.this, "版本相同不需要更新", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
                default:
                    Toast.makeText(SplashActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;

            }
        }

    };

    /**
     * 下载管理Hander
     */
    private Handler downloadHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:  // 下载成功， 安装apk
                    Toast.makeText(SplashActivity.this, "安装apk", Toast.LENGTH_SHORT).show();
                    PackageManagerUtils.installAPK(SplashActivity.this, (File) msg.obj);
                    break;
                case SDCARD_INEXISTENCE:    // sdcard不存在进入主页
                    Toast.makeText(SplashActivity.this, "版本相同不需要更新", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
                case DOWNLOAD_FAILUER:      // 下载失败进入主页
                    Toast.makeText(SplashActivity.this, "版本相同不需要更新", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
                default:        // 系统错误进入主页
                    Toast.makeText(SplashActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    enterHone();
                    break;
            }
        }
    };

    /**
     * 进入Home页面
     */
    private void enterHone() {
        long totolTime = System.currentTimeMillis() - timeManager;
        final long needWaitTime = 2000L - totolTime;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (needWaitTime > 0) {
                    try {
                        Thread.sleep(needWaitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }).start();

    }

    /**
     * 显示升级的对话框
     *
     * @param url 身价访问的url地址
     */
    private void showUpdateDialog(final String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
        dialog.setTitle("版本更新");
        dialog.setMessage("是否更新版本");
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enterHone();
            }
        });
        dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadAPK(url);
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("config", SplashActivity.MODE_PRIVATE);

        // 动态修改应用程序的版本信息
        Log.d(TAG, "onCreate: 获取版本信息");
        versionInfo = (TextView) findViewById(R.id.version_info);
        versionInfo.setText("版本号： " + PackageManagerUtils.getVersionName(this));

        timeManager = System.currentTimeMillis();

        // 判断是否自动更新
        if (sp.getBoolean("app_auto_update", false)) {
            // 联网检查更新
            Log.d(TAG, "onCreate: 联网检测更新");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    checkVersionUpdate();
                }
            }).start();
        } else {
            enterHone();
        }

    }

    /**
     * 联网检测应用程序的更新
     */
    private void checkVersionUpdate() {
        Message msg = new Message();
        String urlStr = PropertiesUtils.getPropertiesInfoByKey(this, "config.properties", "app.update.address");
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(3000);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            String val = StreamTools.readFromStream(inputStream);
            System.out.println(val);
            JSONObject jsonObject = new JSONObject(val);
            String str = (String) jsonObject.get("url");
            String code = (String) jsonObject.get("code");
            Log.d(TAG, "checkVersionUpdate: " + code);

            // 表示版本号相同不需要更新
            msg.what = PackageManagerUtils.getVersionNumber(SplashActivity.this)
                    == Integer.parseInt(code) ? NOT_UPDATE : NEED_UPDATE;
            msg.obj = str;

        } catch (MalformedURLException e) {
            msg.what = URL_ILLEGAL;
            Log.d(TAG, "checkVersionUpdate: url非法");
        } catch (JSONException e) {
            msg.what = JSON_CONVERT_EXECPTION;
            Log.d(TAG, "checkVersionUpdate: json转换数据异常");
        } catch (ProtocolException e) {
            msg.what = PROTOCOL_EXCEPTION;
            Log.d(TAG, "checkVersionUpdate: 连接协议异常");
        } catch (IOException e) {
            msg.what = IO_EXCEPTION;
            Log.d(TAG, "checkVersionUpdate: 读写异常");
        } finally {
            handler.sendMessage(msg);
        }

    }

    /**
     * 下载apk文件
     */
    private void downloadAPK(String url) {
        final Message msg = new Message();
        FinalHttp http = new FinalHttp();

        String path = null;
        try {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + PropertiesUtils.getPropertiesInfoByKey(SplashActivity.this,
                    "config.properties", "app.download.file");
        } catch (Exception e){
            msg.what = SDCARD_INEXISTENCE;
            downloadHander.sendMessage(msg);
            return ;
        }

        http.download(url, path, new AjaxCallBack<File>() {
            @Override
            public void onSuccess(File file) {
                super.onSuccess(file);
                msg.what = DOWNLOAD_SUCCESS;
                msg.obj = file;
                downloadHander.sendMessage(msg);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                msg.what = DOWNLOAD_FAILUER;
                downloadHander.sendMessage(msg);
            }
        });
    }


}

