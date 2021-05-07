package com.example.greenfarm.ui.mine.myOrder.farmer;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.FarmOrderBean;
import com.example.greenfarm.ui.adapter.FarmerFarmOrderAdapter;
import com.example.greenfarm.ui.custom.CustomLoadMoreView;
import com.example.greenfarm.utils.HttpUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class FarmOrderNotProcessedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<FarmOrderBean> farmOrderBeanList = new ArrayList<>();

    private List<Integer> farmOrderIdList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private FarmerFarmOrderAdapter adapter;

    private FloatingActionButton btnProcess;

    private TextView tvNoOrderWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_order_not_processed);
        onRefresh();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_farm_order_not_processed);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recyclerview_farm_order_not_processed);
        recyclerView.setLayoutManager(layoutManager);

        tvNoOrderWarning = findViewById(R.id.tv_farmer_farm_order_warning);

        adapter = new FarmerFarmOrderAdapter(farmOrderBeanList,this);

        recyclerView.setAdapter(adapter);

        btnProcess = findViewById(R.id.floating_btn_process);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (FarmOrderBean farmOrderBean : farmOrderBeanList) {
                    Log.d("FarmOrderNotProcess",String.valueOf(farmOrderBean.isSelected()));
                    if (farmOrderBean.isSelected()) {
                        farmOrderIdList.add(farmOrderBean.getOrder().getId());
                    }
                }

                String jsonOrderIdList = new Gson().toJson(farmOrderIdList);
                try {
                    HttpUtil.postWithOkHttp(HttpUtil.getUrl("/processFarmerFarmOrder"),jsonOrderIdList,new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            showToast("网络连接错误");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String body = response.body().string();
                            HashMap<String,String> result = new Gson().fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                            if ("success".equals(result.get("result"))) {
                                showToast("操作成功");
                            } else {
                                showToast("操作失败");
                            }
//                            showToast(body);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onRefresh();
            }
        });

    }

    private void getData() {
        HashMap<String,Integer> requestData = new HashMap<>();
        requestData.put("farmerId", UserManager.currentUser.getId());
        String jsonToServer = new Gson().toJson(requestData);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFarmerFarmOrderBean"),jsonToServer, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("FarmOrderNotProcess","网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("FarmOrderNotProcess",body);
                    List<FarmOrderBean> list = new Gson().fromJson(body,new TypeToken<List<FarmOrderBean>>(){}.getType());
                    farmOrderBeanList.clear();
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
                    tvNoOrderWarning.setVisibility(View.VISIBLE);
                } else {
                    tvNoOrderWarning.setVisibility(View.GONE);
                }
            }
        },500);
    }

    private void showToast(String msg) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}