package com.gxu.lepao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ljy on 2017-05-19.
 * 启动画面
 */

public class HelloActivity extends BaseActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HelloActivity","Task id is " + getTaskId());
        setContentView(R.layout.activity_loginnext);
        //延迟两秒跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HelloActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    public void run(){

    }
}
