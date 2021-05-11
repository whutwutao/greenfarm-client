package com.example.greenfarm.ui.mine.myMessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.ui.adapter.TalkAdapter;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity {

    private List<User> friendList = new ArrayList<>();

    private RecyclerView recyclerView;

    private TalkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getFriends();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (User user : friendList) {
            Log.d("MessageActivity",user.toString());
        }
        recyclerView = findViewById(R.id.recyclerview_message_talk);

        adapter = new TalkAdapter(friendList,MessageActivity.this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getFriends() {
        Log.d("MessageActivity","开始获取数据");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("sender", UserManager.currentUser.getId());
        String jsonToServer = new Gson().toJson(map);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFriends"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showToast("网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("MessageActivity",body);
                    List<User> users = new Gson().fromJson(body,new TypeToken<List<User>>(){}.getType());
                    friendList.clear();
                    if (users != null) {
                        for (User user : users) {
                            friendList.add(user);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String msg) {
        final Activity activity = MessageActivity.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
            }
        });

    }
}