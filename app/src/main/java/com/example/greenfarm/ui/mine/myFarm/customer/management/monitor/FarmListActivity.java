package com.example.greenfarm.ui.mine.myFarm.customer.management.monitor;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.mine.myFarm.customer.management.monitor.adapter.FarmMonitorAdapter;
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

public class FarmListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Farm> farmList = new ArrayList<>();

    private FarmMonitorAdapter monitorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);

        getData();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerview_farm_to_monitor);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("FarmListActivity",farmList.toString());
        monitorAdapter = new FarmMonitorAdapter(this,R.layout.recyclerview_item_farm,farmList);

        recyclerView.setAdapter(monitorAdapter);

    }

    private void getData() {
        Gson gson = new Gson();
        HashMap<String,Integer> map = new HashMap<>();
        map.put("ownerId", UserManager.currentUser.getId());
        String jsonToServer = gson.toJson(map);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFarmByOwnerId"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("FarmListActivity","网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("FarmListActivity",body);
                    Gson gson1 = new Gson();
                    List<Farm> farms = gson1.fromJson(body,new TypeToken<List<Farm>>(){}.getType());
                    if (farms != null) {
                        if (!farms.isEmpty()) {
                            for (Farm farm : farms) {
                                farmList.add(farm);
                            }
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}