package com.example.greenfarm.ui.mine.myFarm.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.adapter.FarmerFarmAdapter;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.FarmManagementActivity;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.add.AddFarmActivity;
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

public class MyFarmActivity extends AppCompatActivity {

    private List<Farm> farmList = new ArrayList<>();

    private RecyclerView recyclerView;

    private FarmerFarmAdapter adapter;

    private FloatingActionButton btnAddFarm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);

        getData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerview_farmer_farmList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new FarmerFarmAdapter(R.layout.recyclerview_item_farm,farmList,this);

        recyclerView.setAdapter(adapter);

        btnAddFarm = findViewById(R.id.floating_btn_add_farm);
        btnAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFarmActivity.this, AddFarmActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        String jsonToServer = new Gson().toJson(UserManager.currentUser);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFarmerFarmList"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showToast("网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
//                    showToast(body);
                    List<Farm> farms = new Gson().fromJson(body,new TypeToken<List<Farm>>(){}.getType());
                    if (farms != null && !farms.isEmpty()) {
                        farmList.clear();
                        for (Farm farm : farms) {
                            farmList.add(farm);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String text) {
        final Activity activity = MyFarmActivity.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
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
        adapter.setNewData(farmList);
    }
}