package com.gxu.lepao;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by ljy on 2017-05-19.
 * 首页
 */

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity","Task; id is " + getTaskId());
        setContentView(R.layout.activity_login);
    }
}
