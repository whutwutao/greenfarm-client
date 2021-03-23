package com.example.greenfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenfarm.utils.HttpUtil;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText loginPhoneText;

    private Button loginButton;

    private Button registerButton;

    private EditText ipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show();

        loginPhoneText = findViewById(R.id.loginPhoneText);
        loginPhoneText.setOnClickListener(this);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        ipText = findViewById(R.id.ipText);
    }


    /**
     * 为各个控件设置点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loginPhoneText: {
                loginPhoneText.setText("");
                break;
            }
            case R.id.loginButton: {
                Toast.makeText(LoginActivity.this, "点击了登录",Toast.LENGTH_SHORT).show();
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
            default:
                break;
        }
    }
}