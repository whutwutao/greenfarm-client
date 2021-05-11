package com.example.greenfarm.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.ui.main.MainActivity;
import com.example.greenfarm.R;
import com.example.greenfarm.ui.register.RegisterActivity;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.pojo.UserMessage;
import com.example.greenfarm.utils.HttpUtil;
import com.example.greenfarm.utils.RegexUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;
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

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show();

        loginPhoneText = findViewById(R.id.et_login_telephone);

        loginPasswordText = findViewById(R.id.et_login_password);

        loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);

        registerButton = findViewById(R.id.btn_register);
        registerButton.setOnClickListener(this);

        ipText = findViewById(R.id.ipText);
        HttpUtil.serverIP = ipText.getText().toString();//设置IP

        LitePal.getDatabase();//创建数据库

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String telephone = pref.getString("telephone","");
            String password = pref.getString("password","");
            loginPhoneText.setText(telephone);
            loginPasswordText.setText(password);
        }
    }


    /**
     * 为各个控件设置点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login: {
                if (loginPhoneText.getText().toString().isEmpty() || loginPasswordText.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "信息输入不完整",Toast.LENGTH_SHORT).show();
                    break;
                }
                //设置服务器ip
                HttpUtil.serverIP = ipText.getText().toString();
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
                                    e.printStackTrace();
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
                                    User loginUser = msgFromServer.getUser();
                                    if (msgFromServer.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                        Gson gson1 = new Gson();
                                        UserManager.currentUser = gson1.fromJson(jsonFromServer,UserMessage.class).getUser();
                                        Log.d("LoginActivity",UserManager.currentUser.toString());
                                        editor = pref.edit();
                                        editor.putBoolean("remember_password", true);
                                        editor.putString("telephone",UserManager.currentUser.getTelephone());
                                        editor.putString("password",UserManager.currentUser.getPassword());
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
            case R.id.btn_register: {
                boolean isIP = RegexUtil.checkIP(ipText.getText().toString());
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
            default:
                break;
        }
    }
}