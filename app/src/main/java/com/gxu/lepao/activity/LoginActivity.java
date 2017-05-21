package com.gxu.lepao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxu.lepao.R;
import com.gxu.lepao.model.UserInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by ljy on 2017-05-20.
 * 登录页面
 */

public class LoginActivity extends BaseActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText phoneEdit;
    private EditText passwordEdit;
    private Button login;
    private TextView register;
    private TextView reset;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LoginActivity","Task; id is " + getTaskId());
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        phoneEdit = (EditText) findViewById(R.id.phone);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Boolean isLogin = false;
                String phone = phoneEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                Log.d("phone:",phone);
                List<UserInfo> userInfo = DataSupport.select("phone").limit(1).where("phone == ? and password == ?",phone,password).find(UserInfo.class);
                for(UserInfo userinfo:userInfo){
                    Log.d("phone:",userinfo.getPhone());
                    if(userinfo.getPhone().equals(phone)){
                        isLogin = true;
                    }
                }
                if(isLogin){
                    //登录成功，将用户名和密码保存在本地
                    editor = pref.edit();
                    editor.putString("phone",phone);
                    editor.putString("password",password);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
}
