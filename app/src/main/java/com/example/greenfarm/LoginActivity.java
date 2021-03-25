package com.example.greenfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenfarm.pojo.User;
import com.example.greenfarm.pojo.UserMessage;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //手机号输入栏
    private EditText loginPhoneText;

    //密码输入栏
    private EditText loginPasswordText;

    //登录按钮
    private Button loginButton;

    //注册按钮
    private Button registerButton;

    //IP输入栏
    private EditText ipText;

    //MainActivity测试按钮
    private Button mainActivityTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show();

        loginPhoneText = findViewById(R.id.loginPhoneText);

        loginPasswordText = findViewById(R.id.loginPasswordText);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        ipText = findViewById(R.id.ipText);
        HttpUtil.serverIP = ipText.getText().toString();//设置IP

        mainActivityTest = findViewById(R.id.mainActivityTest);
        mainActivityTest.setOnClickListener(this);
    }


    /**
     * 为各个控件设置点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loginButton: {
                if (loginPhoneText.getText().toString().isEmpty() || loginPasswordText.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "信息输入不完整",Toast.LENGTH_SHORT).show();
                    break;
                }
                User user = new User(null,loginPasswordText.getText().toString(),loginPhoneText.getText().toString());
                Gson gson = new Gson();
                String jsonToServer = gson.toJson(user);
                String loginUrl = HttpUtil.networkProtocol + HttpUtil.serverIP + HttpUtil.serverPort + "/login";
                try {
                    HttpUtil.postWithOkHttp(loginUrl, jsonToServer, new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
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
                                    UserMessage msgFromServer = gson.fromJson(jsonFromServer,UserMessage.class);
                                    Log.d("LoginActivity",msgFromServer.toString());
                                    Log.d("LoginActivity",jsonFromServer);
                                    if (msgFromServer.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this,msgFromServer.getFailReason(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                } catch(IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.registerButton: {
                String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(ipText.getText().toString());
                boolean isIP = matcher.matches();
                if (isIP) {
                    HttpUtil.serverIP = ipText.getText().toString();//设置IP
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("phoneNumber",loginPhoneText.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this,"IP地址格式错误",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.mainActivityTest: {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            default:
                break;
        }
    }
}