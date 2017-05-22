package com.gxu.lepao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import com.gxu.lepao.R;
import com.gxu.lepao.model.UserInfo;
import org.litepal.crud.DataSupport;
import java.util.List;

/**
 * Created by ljy on 2017-05-19.
 * 启动画面
 */

public class HelloActivity extends BaseActivity{

    private SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HelloActivity","Task id is " + getTaskId());
        setContentView(R.layout.activity_loginnext);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        //延迟两秒跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLogin = false;
                String phone = pref.getString("phone","");
                String password = pref.getString("password","");
                List<UserInfo> userInfo = DataSupport.select("phone").limit(1).where("phone == ? and password == ?",phone,password).find(UserInfo.class);
                for(UserInfo userinfo:userInfo){
                    Log.d("phone:",userinfo.getPhone());
                    if(userinfo.getPhone().equals(phone)){
                        isLogin = true;
                    }
                }
                if(isLogin){
                    Intent intent = new Intent(HelloActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(HelloActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);


    }

}
