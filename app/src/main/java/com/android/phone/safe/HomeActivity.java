package com.android.phone.safe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    /**
     * 功能菜单的组件
     */
    private GridView gridView;

    /**
     * 菜单的名字
     */
    private static String[] menus = {
            "手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };

    /**
     * 菜单的id
     */
    private static int[] menusIds = {
            R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
    };

    /**
     * 界面被创建时调用
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gridView = (GridView) findViewById(R.id.menu_list);
        // 设置适配器
        gridView.setAdapter(new MyAdapter());

        // 设置事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * 主菜单点击的回调函数
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: //  手机防盗
                        break;
                    case 8: //  设置
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        HomeActivity.this.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 主页菜单的适配器类
     */
    private class MyAdapter extends BaseAdapter {

        /**
         * 获得view的条数
         *
         * @return view的条数
         */
        @Override
        public int getCount() {
            return menus.length;
        }


        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        /**
         * 创建list调用， 获得每个View
         * @param i 表示view的index值
         * @param view  表示复用的view
         * @param viewGroup 表示view所属的组
         * @return 返回该index上的view
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(HomeActivity.this, R.layout.item_home_menu, null);

            }
            ImageView iv = (ImageView) view.findViewById(R.id.menu_img);
            TextView tv = (TextView) view.findViewById(R.id.menu_info);
            iv.setImageResource(menusIds[i]);
            tv.setText(menus[i]);
            return view;
        }
    }
}
