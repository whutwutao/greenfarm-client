package com.example.greenfarm.ui.mine.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.pojo.UserMessage;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editOldPassword;

    private EditText editNewPassword;

    private EditText editConfirmNewPassword;

    private Button buttonChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editOldPassword = findViewById(R.id.et_old_password);

        editNewPassword = findViewById(R.id.et_new_password);

        editConfirmNewPassword = findViewById(R.id.et_confirm_new_password);

        buttonChangePassword = findViewById(R.id.btn_change_password);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editOldPassword.getText().toString().equals(UserManager.currentUser.getPassword())) {
                    //判断新密码和确认密码是否正确
                    if (editNewPassword.getText().toString().equals(editConfirmNewPassword.getText().toString())) {
                        //使用一个临时对象存储currentUser的id和密码，不能立即就把currentUser的密码改掉，因为网络可能发生错误，或者服务器端修改失败
                        User tmpUser = new User();
                        tmpUser.setId(UserManager.currentUser.getId());
                        tmpUser.setPassword(editNewPassword.getText().toString());
                        changePassword(tmpUser);
                    } else {
                        Toast.makeText(ChangePasswordActivity.this,"新密码与确认密码不一致",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this,"原密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword(User user) {
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(user);
        Log.d("ChangePasswordActivity",jsonToServer);
        String url = HttpUtil.getUrl("/changePassword");
        try {
            HttpUtil.postWithOkHttp(url,jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Log.d("ChangePassworActivity",jsonFromServer);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(ChangePasswordActivity.this,"请求成功",Toast.LENGTH_SHORT).show();
                            UserMessage msgFromServer = gson.fromJson(jsonFromServer,UserMessage.class);
                            Log.d("ChangePasswordActivity",msgFromServer.toString());
                            if (msgFromServer.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                                //服务器端密码修改成功之后才把UserManager.currentUser的密码改掉
                                UserManager.currentUser.setPassword(editNewPassword.getText().toString());
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}