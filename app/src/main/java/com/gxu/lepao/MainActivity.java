package com.gxu.lepao;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity","Task; id is " + getTaskId());
        setContentView(R.layout.activity_login);
    }
}
