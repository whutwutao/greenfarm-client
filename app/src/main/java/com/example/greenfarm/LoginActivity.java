package com.example.greenfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText loginPhoneText;

    private Button loginButton;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPhoneText = findViewById(R.id.loginPhoneText);
        loginPhoneText.setOnClickListener(this);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
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
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("phoneNumber",loginPhoneText.getText().toString());
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}