package com.gxu.lepao.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.gxu.lepao.R;
import com.gxu.lepao.model.UserInfo;

/**
 * Created by ljy on 2017-05-22.
 * 设置密码
 */

public class SetPwdActivity extends BaseActivity implements View.OnClickListener {

    //提交按钮
    private Button submit;
    //密码
    private EditText passwordEdit;
    //确认密码
    private EditText confirmPwdEdit;
    //phone
    private String phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SetPwdActivity","Task id is " + this.getTaskId());
        setContentView(R.layout.activity_setpwd);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        Log.d("验证成功:",phone);
        //初始化界面
        initView();
    }

    //初始化界面
    protected void initView(){
        submit = (Button) findViewById(R.id.submit);
        //设置监听器
        submit.setOnClickListener(SetPwdActivity.this);
        passwordEdit =(EditText) findViewById(R.id.password);
        confirmPwdEdit = (EditText) findViewById(R.id.confirmPwd);
    }

    //点击监听
    public void onClick(View view){
        switch(view.getId()){
            case R.id.submit:
                String password = passwordEdit.getText().toString();
                String confirmPwd = confirmPwdEdit.getText().toString();
                if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPwd)){
                    if(checkPwd(password,confirmPwd)){
                        try{
                            UserInfo userInfo = new UserInfo();
                            userInfo.setPhone(phone);
                            userInfo.setPassword(password);
                            userInfo.save();
                        }catch(Exception e){
                            toast("提交失败");
                        }finally {
                            Intent intent = new Intent(SetPwdActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                break;
        }
    }

    //检查密码和确认密码是否一致
    public boolean checkPwd(String pwd,String confirmPwd){
        boolean isConfirm = false;
        if(pwd.equals(confirmPwd)){
            isConfirm = true;
        }else{
            toast("两次输入的密码不一致，请确认后提交");
        }
        return isConfirm;
    }

    // 吐司
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetPwdActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
