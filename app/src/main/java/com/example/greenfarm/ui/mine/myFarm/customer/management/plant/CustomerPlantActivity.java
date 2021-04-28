package com.example.greenfarm.ui.mine.myFarm.customer.management.plant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.Plant;
import com.example.greenfarm.ui.adapter.CustomerPlantAdapter;
import com.example.greenfarm.utils.HttpUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CustomerPlantActivity extends AppCompatActivity {

    private List<Plant> plantList = new ArrayList<>();

    private RecyclerView recyclerView;

    private CustomerPlantAdapter adapter;

    private FloatingActionButton btnAddPlant;

    private Farm mFarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_plant);

        mFarm = new Gson().fromJson(getIntent().getStringExtra("farm"),Farm.class);

        getData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerview_customer_plant_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CustomerPlantAdapter(R.layout.recyclerview_item_plant_product,plantList,this);
        recyclerView.setAdapter(adapter);

        btnAddPlant = findViewById(R.id.floating_btn_add_plant);
        btnAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerPlantActivity.this, CustomerAddPlantActivity.class);
                intent.putExtra("farm",new Gson().toJson(mFarm));
                startActivity(intent);
            }
        });

    }

    private void getData() {
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(UserManager.currentUser);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getCustomerPlantList"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showToast("网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("CustomerPlantActivity",body);
                    showToast(body);
                    List<Plant> plants = gson.fromJson(body,new TypeToken<List<Plant>>(){}.getType());
                    if (plants != null && !plants.isEmpty()) {
                        plantList.clear();
                        for (Plant plant : plants) {
                            plantList.add(plant);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String msg) {
        final Activity activity= CustomerPlantActivity.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.setNewData(plantList);
    }
}