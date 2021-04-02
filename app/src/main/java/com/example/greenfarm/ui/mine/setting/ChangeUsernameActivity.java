package com.example.greenfarm.ui.mine.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ChangeUsernameActivity extends AppCompatActivity {

    private EditText editTextUsername;

    private Button buttonChangeUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        editTextUsername = findViewById(R.id.et_new_username);

        buttonChangeUsername = findViewById(R.id.btn_change_username);
        buttonChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User tmpUser = new User();
                tmpUser.setId(UserManager.currentUser.getId());
                tmpUser.setUsername(editTextUsername.getText().toString());
                changeUsername(tmpUser);
            }
        });
    }

    private void changeUsername(User user) {
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(user);
        String url = HttpUtil.getUrl("/changeUsername");

        try {
            HttpUtil.postWithOkHttp(url,jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Toast.makeText(ChangeUsernameActivity.this, "网络请求错误",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Gson gson1 = new Gson();
                    UserMessage msgFromServer = gson1.fromJson(jsonFromServer,UserMessage.class);
                    if (msgFromServer.isSuccessful()) {
                        UserManager.currentUser.setUsername(user.getUsername());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeUsernameActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeUsernameActivity.this, "修改失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}