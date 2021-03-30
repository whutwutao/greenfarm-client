package com.example.greenfarm.ui.register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.pojo.UserMessage;
import com.example.greenfarm.ui.login.LoginActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.example.greenfarm.utils.RegexUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText registerUsernameText;

    private EditText registerPhoneText;

    private TextView registerPhoneWarning;

    private EditText registerPasswordText;

    private TextView registerPasswordWarning;

    private ImageView registerVerificationImage;

    private EditText registerVerificationCodeText;

    private Button registerSubmitButton;

    private String verificationCode;//验证码

    private boolean isUsernameLegal = false;

    private boolean isPhoneNumberLegal = false;

    private boolean isPasswordLegal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //用户名输入
        registerUsernameText = findViewById(R.id.et_register_username);
        registerUsernameText.setOnClickListener(this);



        //手机号输入
        registerPhoneText = findViewById(R.id.et_register_telephone);
        registerPhoneText.setOnClickListener(this);

        //手机号格式错误警告
        registerPhoneWarning = findViewById(R.id.tv_register_telephone_warning);

        //密码输入
        registerPasswordText = findViewById(R.id.et_register_password);
        registerPasswordText.setOnClickListener(this);

        //密码格式错误警告
        registerPasswordWarning = findViewById(R.id.tv_register_password_warning);

        registerVerificationCodeText = findViewById(R.id.et_register_verification_code);

        //验证码图片
        registerVerificationImage = findViewById(R.id.iv_verification_code);
        registerVerificationImage.setOnClickListener(this);

        //获取验证码
        verify();


        Intent intent = getIntent();
        registerPhoneText.setText(intent.getStringExtra("phoneNumber"));
        registerSubmitButton = findViewById(R.id.btn_register_submit);
        registerSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_register_submit: {//注册按钮点击事件
                if (registerUsernameText.getText().toString().isEmpty() || registerPhoneText.getText().toString().isEmpty() || registerPasswordText.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this,"信息未填写完整！",Toast.LENGTH_SHORT).show();
                    break;
                }

                String verificationCodeInput = registerVerificationCodeText.getText().toString();
                if (!verificationCodeInput.equals(verificationCode)) {
                    Toast.makeText(RegisterActivity.this,"验证码输入错误！",Toast.LENGTH_SHORT).show();
                    break;
                }


                String registerUrl = HttpUtil.networkProtocol + HttpUtil.serverIP + HttpUtil.serverPort + "/register";
                Gson gson = new Gson();
                User user = new User(registerUsernameText.getText().toString(),registerPasswordText.getText().toString(),registerPhoneText.getText().toString());
                String jsonToServer = gson.toJson(user);
                try {
                    HttpUtil.postWithOkHttp(registerUrl, jsonToServer, new okhttp3.Callback() {
                        //回调操作还是在子线程中运行的
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "注册失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String jsonFromServer = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    UserMessage msg = gson.fromJson(jsonFromServer, UserMessage.class);
                                    Log.d("RegisterActivity",jsonFromServer);
                                    Log.d("RegisterActivity",msg.toString());
                                    if (msg.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this,msg.getFailReason(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    System.out.println("用户注册异常");
                    e.printStackTrace();
                }
                break;
            }
            case R.id.iv_verification_code: {//验证码图片点击事件
                verify();//点击更换图片
                break;
            }
            case R.id.et_register_telephone: {//手机号输入格式检查
                boolean isPhoneNumber = RegexUtil.checkPhoneNumber(registerPhoneText.getText().toString());
                if (!isPhoneNumber) {
                    registerPhoneWarning.setVisibility(View.VISIBLE);
                } else {
                    registerPhoneWarning.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.et_register_password: {//密码输入格式检查
                int len = registerPasswordText.getText().toString().length();
                if (len < 8) {
                    registerPasswordWarning.setText("密码长度低于8位!");
                    registerPasswordWarning.setVisibility(View.VISIBLE);
                } else if (len > 18) {
                    registerPasswordWarning.setText("密码长度高于18位!");
                    registerPasswordWarning.setVisibility(View.VISIBLE);
                } else {
                    registerPasswordWarning.setVisibility(View.GONE);
                }
                break;
            }
            default:
                break;
        }
    }

    //获取验证码并进行验证
    private void verify() {
        String url = HttpUtil.getUrl("/getVerificationCode");
        try {
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            Log.d("Verify",jsonFromServer);
                            Gson gson = new Gson();
                            Map<String, String> hashMap = gson.fromJson(jsonFromServer,new TypeToken<HashMap<String,String>>(){}.getType());
                            verificationCode = hashMap.get("code");
                            String encodedImageText = hashMap.get("encodedImage");
                            final Base64.Decoder decoder = Base64.getDecoder();
                            byte[] imageBytes = decoder.decode(encodedImageText);
                            Glide.with(RegisterActivity.this).load(imageBytes).into(registerVerificationImage);
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
