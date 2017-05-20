package com.gxu.lepao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.gxu.lepao.R;
import com.gxu.lepao.model.UserInfo;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

/**
 * Created by ljy on 2017-05-19.
 * 首页
 */

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity","Task; id is " + getTaskId());
        setContentView(R.layout.activity_findsong);

    }
}
