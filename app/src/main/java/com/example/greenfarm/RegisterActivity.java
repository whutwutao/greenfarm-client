package com.example.greenfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenfarm.pojo.User;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText registerUserNameText;

    private EditText registerPhoneText;

    private EditText registerPasswordText;

    private Button registerSubmitButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUserNameText = findViewById(R.id.registerUserNameText);

        registerPhoneText = findViewById(R.id.registerPhoneText);

        registerPasswordText = findViewById(R.id.registerPasswordText);

        Intent intent = getIntent();
        registerPhoneText.setText(intent.getStringExtra("phoneNumber"));

        registerSubmitButton = findViewById(R.id.registerSubmitButton);
        registerSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.registerSubmitButton: {
                String registerResult = "";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String registerUrl = HttpUtil.serverIP + HttpUtil.serverPort + "/register";
//                        Gson gson = new Gson();
//                        User user = new User(registerUserNameText.getText().toString(),registerPasswordText.getText().toString(),registerPhoneText.getText().toString());
//                        String json = gson.toJson(user);
//                        try {
//                            HttpUtil.postWithOkHttp(registerUrl, json, new okhttp3.Callback() {
//                                @Override
//                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//                                }
//                            });
//                        } catch (IOException e) {
//                            System.out.println("用户注册异常");
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
                String registerUrl = HttpUtil.serverIP + HttpUtil.serverPort + "/register";
                Gson gson = new Gson();
                User user = new User(registerUserNameText.getText().toString(),registerPasswordText.getText().toString(),registerPhoneText.getText().toString());
                String json = gson.toJson(user);
                String response;
                try {
                    HttpUtil.postWithOkHttp(registerUrl, json, new okhttp3.Callback() {
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(RegisterActivity.this, response.body().string(),Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
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
            default:
                break;
        }
    }
}
