package com.lz.mobilefollow.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lz.mobilefollow.R;
import com.lz.mobilefollow.dao.UserEntity;
import com.lz.mobilefollow.lzcore.base.BaseActivity;
import com.lz.mobilefollow.lzcore.constant.Constant;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;
import com.lz.mobilefollow.lzcore.framework.okhttp3.ResultCallback;
import com.lz.mobilefollow.lzcore.util.MatcheUtils;
import com.lz.mobilefollow.lzcore.util.SharedPreferenceUtils;
import com.lz.mobilefollow.utils.HttpClient;

public class LoginActivity extends BaseActivity {

    /**登录布局*/
    private LinearLayout ll_login_content;
    /**注册布局*/
    private LinearLayout ll_register_content;

    /**登录邮箱*/
    private TextInputLayout login_user_email_wrapper;
    /**登录邮箱*/
    private EditText login_user_email;

    /**登录密码*/
    private TextInputLayout login_user_password_wrapper;
    /**登录密码*/
    private EditText login_user_password;

    /**登录按钮*/
    private Button bt_login;
    /**去注册*/
    private TextView tv_to_register;


    /**注册用户名*/
    private TextInputLayout re_user_name_wrapper;
    /**注册用户名*/
    private EditText re_user_name;

    /**注册邮箱*/
    private TextInputLayout re_user_email_wrapper;
    /**注册邮箱*/
    private EditText re_user_email;

    /**注册密码*/
    private TextInputLayout re_user_password_wrapper;
    /**注册密码*/
    private EditText re_user_password;

    /**注册按钮*/
    private Button bt_register;
    /**去登录*/
    private TextView tv_to_login;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        setToolbarTitle("欢迎");
        ll_login_content= (LinearLayout) this.findViewById(R.id.ll_login_content);
        ll_register_content= (LinearLayout) this.findViewById(R.id.ll_register_content);

        login_user_email_wrapper= (TextInputLayout) this.findViewById(R.id.login_user_email_wrapper);
        login_user_email= (EditText) this.findViewById(R.id.login_user_email);
        login_user_password_wrapper= (TextInputLayout) this.findViewById(R.id.login_user_password_wrapper);
        login_user_password= (EditText) this.findViewById(R.id.login_user_password);
        bt_login= (Button) this.findViewById(R.id.bt_login);
        tv_to_register= (TextView) this.findViewById(R.id.tv_to_register);

        re_user_name_wrapper= (TextInputLayout) this.findViewById(R.id.re_user_name_wrapper);
        re_user_name= (EditText) this.findViewById(R.id.re_user_name);
        re_user_email_wrapper= (TextInputLayout) this.findViewById(R.id.re_user_email_wrapper);
        re_user_email= (EditText) this.findViewById(R.id.re_user_email);
        re_user_password_wrapper= (TextInputLayout) this.findViewById(R.id.re_user_password_wrapper);
        re_user_password= (EditText) this.findViewById(R.id.re_user_password);
        bt_register= (Button) this.findViewById(R.id.bt_register);
        tv_to_login= (TextView) this.findViewById(R.id.tv_to_login);

    }
    @Override
    protected void initData() {
        login_user_email_wrapper.setHint("邮箱");
        login_user_password_wrapper.setHint("密码");

        re_user_name_wrapper.setHint("用户名");
        re_user_email_wrapper.setHint("邮箱");
        re_user_password_wrapper.setHint("密码");
       /* login_user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateLogin();
            }
        });
        login_user_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateLogin();
            }
        });

        re_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateRegister();
            }
        });
        re_user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateRegister();
            }
        });
        re_user_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateRegister();
            }
        });*/

    }

    /**登录表单校验*/
    public boolean validateLogin(){
        String loginEmail=login_user_email.getText().toString().trim();
        String loginPassword=login_user_password.getText().toString().trim();

        boolean flag=true;

        if(!MatcheUtils.isEmail(loginEmail)){
            flag=false;
            login_user_email_wrapper.setError("邮箱格式错误");
        }else{
            login_user_email_wrapper.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(loginPassword)){
            flag=false;
            login_user_password_wrapper.setError("请输入正确密码");
        }else{
            login_user_password_wrapper.setErrorEnabled(false);
        }
       /* if(flag){
            bt_register.setEnabled(true);
        }else{
            bt_register.setEnabled(false);
        }*/

        return flag;
    }
    /**注册表单校验*/
    public boolean validateRegister(){
        String reUserName=re_user_name.getText().toString().trim();
        String reEmail=re_user_email.getText().toString().trim();
        String rePassword=re_user_password.getText().toString().trim();

        boolean flag=true;

        if(TextUtils.isEmpty(reUserName)){
            flag=false;
            re_user_name_wrapper.setError("请输入正确用户名");
        }else{
            re_user_name_wrapper.setErrorEnabled(false);
        }

        if(!MatcheUtils.isEmail(reEmail)){
            flag=false;
            re_user_email_wrapper.setError("邮箱格式错误");
        }else{
            re_user_email_wrapper.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(rePassword)){
            flag=false;
            re_user_password_wrapper.setError("请输入正确密码");
        }else{
            re_user_password_wrapper.setErrorEnabled(false);
        }
       /* if(flag){
            bt_register.setEnabled(true);
        }else{
            bt_register.setEnabled(false);
        }*/
        return flag;
    }


    /**登录方法*/
    public void login(View view) {
        if(validateLogin()){
            LogUtils.d("-----------------login");
            httpLogin();
        }

    }
    /**登录方法*/
    public void register(View view) {
        if(validateRegister()){
            LogUtils.d("-----------------register");
        }
    }

    /**去注册*/
    public void toRegister(View view) {
        Animation a1=AnimationUtils.loadAnimation(this,R.anim.slide_left_out);
        Animation a2=AnimationUtils.loadAnimation(this,R.anim.slide_right_in);
        ll_login_content.startAnimation(a1);
        ll_login_content.setVisibility(View.GONE);
        ll_register_content.startAnimation(a1);
        ll_register_content.setVisibility(View.VISIBLE);
    }
    /**去登录*/
    public void toLogin(View view) {
        Animation a1=AnimationUtils.loadAnimation(this,R.anim.slide_left_in);
        Animation a2=AnimationUtils.loadAnimation(this,R.anim.slide_right_out);
        ll_register_content.startAnimation(a1);
        ll_register_content.setVisibility(View.GONE);
        ll_login_content.startAnimation(a1);
        ll_login_content.setVisibility(View.VISIBLE);
    }

    /*public void httpLogin(){
        String email=login_user_email.getText().toString().trim();
        String password=login_user_password.getText().toString().trim();
        HttpClient.getInstance().login(this, email, password, new ResultCallback<ResponseModel>() {
            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
            }

            @Override
            public void onResponse(ResponseModel response) {
                super.onResponse(response);
                if(null!=response){
                    if(response.getValiate().getRsponseCode()== MessageConfig.RESPONSE_SUCCESS){
                        String userStr=(String)response.getData();
                        UserEntity user=new Gson().fromJson(userStr,UserEntity.class);
                        int id=user.getId();
                        SharedPreferenceUtils.setSPString(LoginActivity.this,"userId",String.valueOf(id));
                        LogUtils.d("------------------user.id:"+id);
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(Call call, Exception e) {
                super.onFailure(call, e);
            }
        });
    }*/
    public void httpLogin(){
        String email=login_user_email.getText().toString().trim();
        String password=login_user_password.getText().toString().trim();
        HttpClient.getInstance().login2(this, email, password,new ResultCallback(){
            @Override
            public void onStart() {
                super.onStart();
                LogUtils.d("-----------httpstart:");
            }

            @Override
            public void onSuccess(Object object) {
                LogUtils.d("----------object:"+object.toString());
                UserEntity user=new Gson().fromJson(object.toString(),UserEntity.class);
                int id=user.getId();
                Constant.AUTHEN_SECRET_KEY=id;
                SharedPreferenceUtils.setSPInt(LoginActivity.this,"userId",id);
                SharedPreferenceUtils.setSPBoolean(LoginActivity.this, Constant.SHARED_KEY_IF_LOGIN,true);
                onBackPressed();
            }

            @Override
            public void onFailure(String message) {
                LogUtils.d("-----------http onFailure()"+message);
            }

            @Override
            public void onError(Exception e) {
                LogUtils.d("-----------http onError()"+e.toString());
            }
        });
    }
}
