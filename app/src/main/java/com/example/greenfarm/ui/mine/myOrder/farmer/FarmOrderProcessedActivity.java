package com.example.greenfarm.ui.mine.myOrder.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.FarmOrderBean;
import com.example.greenfarm.ui.adapter.FarmerProcessedFarmOrderAdapter;
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

public class FarmOrderProcessedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private List<FarmOrderBean> farmOrderBeanList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private FarmerProcessedFarmOrderAdapter adapter;

    private TextView tvNoProcessedFarmOrderWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_order_processed);

        onRefresh();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_farm_order_processed);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.recyclerview_farm_order_processed);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FarmerProcessedFarmOrderAdapter(farmOrderBeanList,this);
        recyclerView.setAdapter(adapter);
        tvNoProcessedFarmOrderWarning = findViewById(R.id.tv_farmer_no_processed_farm_order_warning);
    }

    private void getData() {
        HashMap<String,Integer> requestData = new HashMap<>();
        requestData.put("farmerId", UserManager.currentUser.getId());
        String jsonToServer = new Gson().toJson(requestData);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFarmerProcessedFarmOrderBean"),jsonToServer, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("FarmOrderProcess","网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("FarmOrderProcess",body);
                    farmOrderBeanList.clear();
                    List<FarmOrderBean> list = new Gson().fromJson(body,new TypeToken<List<FarmOrderBean>>(){}.getType());
                    if (list != null && !list.isEmpty()) {
                        for (FarmOrderBean farmOrderBean : list) {
                            farmOrderBeanList.add(farmOrderBean);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter.setNewData(farmOrderBeanList);
                swipeRefreshLayout.setRefreshing(false);
                if (farmOrderBeanList.isEmpty()) {
                    tvNoProcessedFarmOrderWarning.setVisibility(View.VISIBLE);
                } else {
                    tvNoProcessedFarmOrderWarning.setVisibility(View.GONE);
                }
            }
        },500);
    }
}