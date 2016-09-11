package com.android.phone.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.phone.safe.utils.PhoneContactUtils;

public class PhoneSafeSetup3 extends BasePhoneSafeActivity implements View.OnClickListener {

    private static final String TAG = "PhoneSafeSetup3";
    private SharedPreferences sp;

    private Button btnLast;
    private Button btnNext;
    private Button choiseContactBtn;
    private TextView phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe_setup3);

        sp = getSharedPreferences("config", PhoneSafeSetup3.MODE_PRIVATE);

        // 初始化控件
        btnLast = (Button) findViewById(R.id.last);
        btnNext = (Button) findViewById(R.id.next);
        choiseContactBtn = (Button) findViewById(R.id.choise_contact);
        phoneNumber = (TextView) findViewById(R.id.phone_number);

        // 添加事件
        btnLast.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        choiseContactBtn.setOnClickListener(this);
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 改变之前
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("securityPhoneNumber", charSequence + "");
                edit.commit();
                // 在改变
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 改变之后
            }
        });

        // 初始化数据
        phoneNumber.setText(sp.getString("securityPhoneNumber", ""));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                nextStep();
                break;
            case R.id.last:
                lastStep();
                break;
            case R.id.choise_contact:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(PhoneSafeSetup3.this);
                View selectContactView = View.inflate(PhoneSafeSetup3.this, R.layout.dialog_choise_contact, null);

                // 设置下拉列表
                ListView listView = (ListView) selectContactView.findViewById(R.id.contact_list);
                SimpleAdapter simpleAdapter = new SimpleAdapter(PhoneSafeSetup3.this,
                        PhoneContactUtils.getAllConctact(PhoneSafeSetup3.this), R.layout.item_contact,
                        new String[]{"name", "phone"}, new int[]{R.id.name, R.id.phone_number});
                listView.setAdapter(simpleAdapter);
                dialog.setView(selectContactView);
                final AlertDialog show = dialog.show();

                // 设置行的点击事件
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView tv = (TextView) view.findViewById(R.id.phone_number);
                        String number = tv.getText().toString();
                        phoneNumber.setText(number);
                        show.dismiss();
                    }
                });


                break;
            default:
                break;
        }
    }

    @Override
    public void lastStep() {
        Intent intent = new Intent(PhoneSafeSetup3.this, PhoneSafeSetup2.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pro_in, R.anim.tran_pro_out);
    }

    /**
     * 进入下一步设置的activity
     */
    @Override
    public void nextStep() {
        Intent intent = new Intent(PhoneSafeSetup3.this, PhoneSafeSetup4.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

}
