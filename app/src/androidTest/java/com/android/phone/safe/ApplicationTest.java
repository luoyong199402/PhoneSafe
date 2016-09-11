package com.android.phone.safe;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.android.phone.safe.utils.PhoneContactUtils;

import java.util.List;
import java.util.Map;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static final String TAG = "ApplicationTest";

    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * 测试获取联系人
     */
    public void test1() {
        List<Map<String, String>> allConctact = PhoneContactUtils.getAllConctact(this.getContext());
        Log.d(TAG, "test1: " + allConctact);
    }

}