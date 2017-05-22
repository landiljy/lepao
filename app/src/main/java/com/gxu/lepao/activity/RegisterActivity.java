package com.gxu.lepao.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gxu.lepao.R;
import com.gxu.lepao.model.UserInfo;
import org.litepal.crud.DataSupport;//LitePal开源数据库框架
import java.util.List;
import cn.smssdk.EventHandler;//Mob短信验证码
import cn.smssdk.SMSSDK;//Mob短信验证码

/**
 * Created by ljy on 2017-05-21.
 * 获取和验证短信验证码界面
 */

public class RegisterActivity extends BaseActivity {

    private EditText phoneEdit;
    private EditText smsEdit;
    private Button getSms;
    private Button submitSms;
    private CountDownTimer countDownTimer;
    //倒计时
    private int TIME = 60;
    //这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    public String country = "86";
    //APPKEY
    private static String appKey = "1e0d15118480d";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String appSecret = "daa97d794e2436abcae624ac3785d2ef";
    //表示是否使用了registerEventHandler
    private boolean ready;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RegisterActivity","Task; id is " + getTaskId());
        setContentView(R.layout.activity_register);
        // 初始化界面
        initView();
        //初始化SMSSDK
        initSDK();
        getSms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean isLogin = false;
                //判断手机号码是否已注册过
                isLogin = isRegister();
                if(!TextUtils.isEmpty(phoneEdit.getText().toString())){
                    if(!isLogin){
                         //发送短信权限
                        if(ContextCompat.checkSelfPermission(RegisterActivity.this
                                ,Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
                            //显示申请权限弹窗
                            showRequestPermission();
                        }else{
                            //弹窗确认
                            alterWarning();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,"此手机号码已经注册过",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
                }
            }
        });
        submitSms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sms = smsEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                if(!TextUtils.isEmpty(sms)){
                    SMSSDK.submitVerificationCode( country,  phone,  sms);
                    Intent intent = new Intent(RegisterActivity.this,SetPwdActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //申请权限弹窗
    private void showRequestPermission(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        //接收短信权限
                        ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.RECEIVE_SMS},1);
                       //发送短信权限
                        ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.SEND_SMS},2);
                        //弹窗确认
                        alterWarning();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(RegisterActivity.this, "拒绝" + which, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);  //先得到构造器
        builder.setTitle("申请权限"); //设置标题
        builder.setMessage("要允许lepao接收验证码短信吗?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("允许",dialogOnclicListener);
        builder.setNegativeButton("拒绝", dialogOnclicListener);
        builder.create().show();
    }

    //申请权限
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //initSDK();
                    Toast.makeText(this,"你授权lepao接收短信权限",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"你拒绝lepao接收短信权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"你授权lepao发送短信权限",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"你拒绝lepao发送短信权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    // 初始化短信SDK
    private void initSDK() {
        SMSSDK.initSDK(this, appKey, appSecret, true);
        final Handler handler = new Handler();
        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
        ready = true;
    }

    //销毁短信注册
    @Override
    protected void onDestroy() {
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        if(ready){
            SMSSDK.unregisterAllEventHandler();
        }
        super.onDestroy();
    }

    //初始化界面
    private void initView(){
        phoneEdit = (EditText) findViewById(R.id.phone);
        smsEdit = (EditText) findViewById(R.id.sms);
        getSms = (Button) findViewById(R.id.getSms);
        submitSms = (Button) findViewById(R.id.submitSms);
    }

    //弹窗确认
    private void alterWarning(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        dialog.dismiss();
                        //通过sdk发送短信验证（请求获取短信验证码，在监听（eventHandle）中返回）
                        SMSSDK.getVerificationCode(country, phoneEdit.getText().toString());
                        Toast.makeText(RegisterActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                        //设置获取验证码按钮不能点击
                        getSms.setClickable(false);
                        //倒计时，执行次数为（TIME+1）*1000/1000,countDownTimer每次执行间隔：1000（单位为毫秒）
                        countDownTimer  = new CountDownTimer((TIME+1)*1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                getSms.setText(TIME-- + "秒后再次获取验证码");
                            }

                            @Override
                            public void onFinish() {
                                //设置获取验证码按钮可以点击
                                getSms.setClickable(true);
                                getSms.setText("点击获取短信验证码");
                            }
                        };
                        countDownTimer.start();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("发送短信"); //设置标题
        builder.setMessage("我们将把验证码发送到以下号码:\n"+country+"-"+phoneEdit.getText().toString()); //设置内容
        builder.setIcon(R.drawable.icon);//设置图标，图片id即可
        builder.setPositiveButton("确认",dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }

    //判断手机号码是否已注册过
    public boolean isRegister(){
        boolean isLogin = false;
        String phone = phoneEdit.getText().toString();
        List<UserInfo> userInfo = DataSupport.select("phone").limit(1).where("phone == ?",phone).find(UserInfo.class);
        for(UserInfo userinfo:userInfo){
            Log.d("phone:",userinfo.getPhone());
            if(userinfo.getPhone().equals(phone)){
                isLogin = true;
            }
        }
        return isLogin;
    }

    //    public boolean handleMessage(Message msg) {
//        int event = msg.arg1;
//        int result = msg.arg2;
//        Object data = msg.obj;
//        //回调完成
//        if (result == SMSSDK.RESULT_COMPLETE)
//        {
//            //验证码验证成功
//            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
//            {
//                Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_LONG).show();
//
//            }
//            //已发送验证码
//            else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
//            {
//                Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
//            } else
//            {
//                ((Throwable) data).printStackTrace();
//            }
//        }
//        if(result==SMSSDK.RESULT_ERROR) {
//
//        }
//        return false;
//    }



}
