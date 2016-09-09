package com.android.phone.safe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by 罗勇 on 2016/8/23.
 */
public abstract class BasePhoneSafeActivity extends AppCompatActivity {

    /**
     * 定义一个手势识别器
     */
    private GestureDetector detector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new GestureDetector(this, new MyGesture());
    }

    /**
     * 手在屏幕上的监控
     *
     * @param event 手势事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 注册手势监听器
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 下一步
     */
    public abstract void nextStep();

    /**
     * 上一步
     */
    public abstract void lastStep();

    /**
     * 定义手势识别器的回调类
     */
    private class MyGesture extends GestureDetector.SimpleOnGestureListener {

        /**
         * 当手指在上面滑动时回调
         * @param e1 手势起点的移动事件
         * @param e2 当前手势点的移动事件
         * @param velocityX 每秒x轴方向移动的像素
         * @param velocityY 每秒y轴方向移动的像素
         * @return 是否终止这个事件
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 滑动太慢
            if (velocityX < 200 && velocityX > -200) {
                return false;
            }
            // 在Y轴滑动太多
            if ((e2.getRawY() - e1.getRawY()) > 300) {
                return false;
            }
            //  上一页
            if ((e2.getRawX() - e1.getRawX()) > 200) {
                lastStep();
                return true;
            }
            //  下一页
            if ((e1.getRawX() - e2.getRawX()) > 200) {
                nextStep();
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}
