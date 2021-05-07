package com.example.greenfarm.ui.mine.myOrder.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.FarmOrderBean;
import com.example.greenfarm.ui.adapter.CustomerFarmOrderAdapter;
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

public class CustomerFarmOrderProcessedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private CustomerFarmOrderAdapter adapter;

    private List<FarmOrderBean> farmOrderBeanList = new ArrayList<>();

    private TextView tvNoOrderWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_farm_order_processed);
        onRefresh();

        recyclerView = findViewById(R.id.recyclerview_customer_farm_order_processed);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_customer_farm_order_processed);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new CustomerFarmOrderAdapter(farmOrderBeanList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        tvNoOrderWarning = findViewById(R.id.tv_customer_no_farm_order_processed_warning);
    }

    private void getData() {
        HashMap<String,Integer> requestData = new HashMap<String,Integer>();
        requestData.put("customerId", UserManager.currentUser.getId());
        String jsonToServer = new Gson().toJson(requestData);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getCustomerProcessedFarmOrderBeanList"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showToast("网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
//                    showToast(body);
                    Log.d("CustomerNotProcess",body);
                    farmOrderBeanList.clear();
                    List<FarmOrderBean> farmOrderBeans = new Gson().fromJson(body,new TypeToken<List<FarmOrderBean>>(){}.getType());
                    if (farmOrderBeans != null && !farmOrderBeans.isEmpty()) {
                        for (FarmOrderBean farmOrderBean : farmOrderBeans) {
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
                if (farmOrderBeanList.isEmpty()) {
                    tvNoOrderWarning.setVisibility(View.VISIBLE);
                } else {
                    tvNoOrderWarning.setVisibility(View.GONE);
                }
                adapter.setNewData(farmOrderBeanList);
                swipeRefreshLayout.setRefreshing(false);
            }
        },500);
    }

    private void showToast(String msg) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CustomerFarmOrderProcessedActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}