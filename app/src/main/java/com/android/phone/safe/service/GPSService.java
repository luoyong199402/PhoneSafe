package com.android.phone.safe.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class GPSService extends Service {
    private static final String TAG = "GPSService";
    private static SharedPreferences sp;

    public GPSService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onBind: onCreate");
        sp = this.getSharedPreferences("config", MODE_PRIVATE);
        LocationManager localtionManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String proveder = localtionManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: show");
        }

        localtionManager.requestLocationUpdates(proveder, 0, 0, new MyLocationListener());
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class MyLocationListener implements LocationListener {

        /**
         * 当位置改变的时候回调
         *
         * @param location 改变后的位置
         */
        @Override
        public void onLocationChanged(Location location) {
            String longitude = location.getLongitude() + "";   // 经度
            String latitude = location.getLatitude() + "";   // 纬度
            String accuracy = location.getAccuracy() + ""; // 精确度

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("GPSLaction", "w:" + longitude + ",j:" + latitude);
            edit.commit();
        }

        /**
         * 当状态发生改变的时候回调 开启--关闭 ；关闭--开启
         *
         * @param provider
         * @param status
         * @param bundle
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {
            Log.d(TAG, "onProviderDisabled: ");
        }

        /**
         * 某一个位置提供者可以使用了
         *
         * @param provider
         */
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");
        }

        /**
         * 某一个位置提供者不可以使用了
         *
         * @param provider
         */
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");
        }
    }

}
