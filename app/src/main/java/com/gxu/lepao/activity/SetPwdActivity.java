package com.gxu.lepao.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gxu.lepao.R;

/**
 * Created by ljy on 2017-05-22.
 * 设置密码
 */

public class SetPwdActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SetPwdActivity","Task id is " + this.getTaskId());
        setContentView(R.layout.activity_setpwd);
    }
}
