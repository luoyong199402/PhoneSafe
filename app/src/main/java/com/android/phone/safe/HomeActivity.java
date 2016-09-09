package com.android.phone.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.phone.safe.utils.PasswordUtils;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * 配置文件信息
     */
    private SharedPreferences sp;

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
     * 对话框信息(进入手机安全设置的对话框信息)
     */
    AlertDialog alertDialog = null;

    /**
     * 界面被创建时调用
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences("config", HomeActivity.MODE_PRIVATE);

        gridView = (GridView) findViewById(R.id.menu_list);
        // 设置适配器
        gridView.setAdapter(new MyAdapter());

        // 设置事件
        gridView.setOnItemClickListener(this);
    }

    /**
     * 显示进入手机安全的对话框
     */
    private void showPhoneSafeDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
        View dialogShow = null;
        // 表示第一进入这个页面， 设置密码
        if ("".equals(sp.getString("enter_phone_safe_pwd", ""))) {
            // 设置密码
            dialogShow = View.inflate(HomeActivity.this, R.layout.dialog_enter_phone_safe_set_password, null);
            Button cancel = (Button) dialogShow.findViewById(R.id.cancel);
            Button ok = (Button) dialogShow.findViewById(R.id.ok);
            final EditText pwd = (EditText) dialogShow.findViewById(R.id.pwd);
            final EditText pwdConfirm = (EditText) dialogShow.findViewById(R.id.pwd_confirm);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor edit = sp.edit();
                    String pwdVal = pwd.getText().toString().trim();
                    String pwdConfirmVal = pwdConfirm.getText().toString().trim();
                    if ("".equals(pwdVal) || "".equals(pwdConfirmVal)) {
                        Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                        return ;
                    }

                    if (pwdVal.equals(pwdConfirmVal)) {
                        // 保存密码
                        edit.putString("enter_phone_safe_pwd", PasswordUtils.encodeString(pwdVal));
                        edit.commit();
                        enterPhoneSafe();
                        alertDialog.dismiss();
                    } else {
                        // 清空密码
                        pwd.setText("");
                        pwdConfirm.setText("");
                        Toast.makeText(HomeActivity.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else { // 输入密码
            dialogShow = View.inflate(HomeActivity.this, R.layout.dialog_enter_phone_safe, null);
            Button cancel = (Button) dialogShow.findViewById(R.id.cancel);
            Button ok = (Button) dialogShow.findViewById(R.id.ok);
            final EditText pwd = (EditText) dialogShow.findViewById(R.id.pwd);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pwdVal = pwd.getText().toString().trim();
                    if (pwdVal.equals("")) {
                        Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                        return ;
                    }

                    if (PasswordUtils.encodeString(pwdVal).equals(sp.getString("enter_phone_safe_pwd", ""))) {
                        enterPhoneSafe();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        dialog.setView(dialogShow);
        alertDialog = dialog.show();
    }

    /**
     * 进入手机安全设置
     */
    private void enterPhoneSafe() {
        Intent intent = new Intent(HomeActivity.this, PhoneSafeFinishPage.class);
        HomeActivity.this.startActivity(intent);
    }

    /**
     * 主菜单点击的回调函数
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0: //  手机防盗
                showPhoneSafeDialog();
                break;
            case 8: //  设置中心
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                HomeActivity.this.startActivity(intent);
                break;
            default:
                break;
        }
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
         *
         * @param i         表示view的index值
         * @param view      表示复用的view
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
