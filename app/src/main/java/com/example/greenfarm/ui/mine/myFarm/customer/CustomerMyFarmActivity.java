package com.example.greenfarm.ui.mine.myFarm.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.adapter.CustomerFarmAdapter;
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

public class CustomerMyFarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Farm> farmList = new ArrayList<>();//农场数据列表

    private CustomerFarmAdapter adapter;

    private TextView tvNoFarmWarning;//提示没有农场
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_my_farm);

        getData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(farmList);
        recyclerView = findViewById(R.id.recyclerview_customer_my_farm);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomerFarmAdapter(R.layout.recyclerview_item_farm,farmList,this);
        recyclerView.setAdapter(adapter);

        tvNoFarmWarning = findViewById(R.id.tv_warning_no_farm);

        if (farmList.isEmpty()) {
            tvNoFarmWarning.setVisibility(View.VISIBLE);
        }
    }


    private void getData() {
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(UserManager.currentUser);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getCustomerFarmList"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("CustomerMyFarmActivity","网络错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("CustomerMyFarmActivity",body);
                    Gson gson1 = new Gson();
                    List<Farm> farms = gson1.fromJson(body,new TypeToken<List<Farm>>(){}.getType());
                    if (farms != null) {
                        if (!farms.isEmpty()) {
                            farmList.clear();
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