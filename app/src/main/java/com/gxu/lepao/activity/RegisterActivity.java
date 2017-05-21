package com.gxu.lepao.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gxu.lepao.R;
import com.gxu.lepao.model.UserInfo;
import org.litepal.crud.DataSupport;//LitePal开源数据库框架
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
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
    private EventHandler eventHandler;
    private Handler handler;
    private CountDownTimer countDownTimerdt;
    private int TIME = 10;
    public String country = "86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RegisterActivity","Task; id is " + getTaskId());
        setContentView(R.layout.activity_register);
        // 初始化
        initView();
        getSms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean isLogin = false;
                //判断手机号码是否已注册过
                isLogin = isRegister();
                if(!isLogin && !TextUtils.isEmpty(phoneEdit.toString())){
                    handler = new Handler();
                    //初始化SMS
                    initSms();
                    getSmsOnclick();
                }else{
                    Toast.makeText(RegisterActivity.this,"此手机号码已经注册过",Toast.LENGTH_SHORT).show();
                }
            }
        });
        submitSms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sms = smsEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                if(!TextUtils.isEmpty(sms)){

                }else{
                    Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //初始化
    private void initView(){
        phoneEdit = (EditText) findViewById(R.id.phone);
        smsEdit = (EditText) findViewById(R.id.sms);
        getSms = (Button) findViewById(R.id.getSms);
        submitSms = (Button) findViewById(R.id.submitSms);
    }

    //初始化SMS
    private void initSms() {
        SMSSDK.initSDK(RegisterActivity.this, "1de6c588f0eb8", "a07c0113a5a095c05fbdf4316afd2a91");
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }
    public void handleMessage(Message msg){

    }


    private void getSmsOnclick(){
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("发送短信")
                .setMessage("我们将把验证码发送到以下号码:\n"+country+"-"+phoneEdit.getText().toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss(); //关闭dialog
                        SMSSDK.getVerificationCode(country, phoneEdit.getText().toString());
                        Toast.makeText(RegisterActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                        getSms.setClickable(false);
                        countDownTimerdt = new CountDownTimer((TIME+1)*1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                getSms.setText(TIME--+"秒后再次获取验证码");
                            }
                            @Override
                            public void onFinish() {
                                getSms.setClickable(true);
                                getSms.setText("点击获取短信验证码");
                            }
                        };
                        countDownTimerdt.start();

                    }
                })
                .create()
                .show();
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


}
