package com.android.phone.safe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.phone.safe.R;

/**
 * Created by 罗勇 on 2016/8/21.
 */
public class SettingItem extends RelativeLayout implements ISettingItem {

    private TextView titil;
    private TextView desc;
    private CheckBox checked;

    private String titleDesc;
    private String descOn;
    private String descOff;
    private boolean checkBoxChecked;

    private ISettingItem iSettingItem = this;

    public SettingItem(Context context) {
        super(context);
        init(context);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        titleDesc = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "title");
        descOn = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "desc_on");
        descOff = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "desc_off");
        descOff = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "desc_off");
        checkBoxChecked = Boolean.parseBoolean(attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "checked"));
        titil.setText(titleDesc);
        if (checkBoxChecked) {
           setState(true);
        } else {
           setState(false);
        }
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化视图
     * @param context　应用上下文
     */
    private void init(Context context) {
        // 设置view
        View view = View.inflate(context, R.layout.item_setting, this);
        titil = (TextView) view.findViewById(R.id.title);
        desc = (TextView) view.findViewById(R.id.desc);
        checked = (CheckBox) view.findViewById(R.id.is_checked);
        checked.setClickable(false);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iSettingItem.onClick(SettingItem.this);
            }
        });
    }

    /**
     * 设置按钮点击的状态
     */
    public void setState(boolean state) {
        if (state) {
            checked.setChecked(state);
            desc.setText(descOn);
        } else {
            checked.setChecked(state);
            desc.setText(descOff);
        }

    }

    /**
     * 获得settingItem状态
     */
    public boolean getState() {
        return checked.isChecked();
    }

    /**
     * 设置监听器
     */
    public void setOnClickListener(ISettingItem iSettingItem) {
        this.iSettingItem = iSettingItem;
    }

    /**
     * 默认点击事件
     */
    @Override
    public void onClick(View view) {
        SettingItem si = (SettingItem) view;
        si.setState(!getState());
    }
}
