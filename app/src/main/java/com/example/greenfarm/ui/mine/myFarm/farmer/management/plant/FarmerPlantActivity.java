package com.example.greenfarm.ui.mine.myFarm.farmer.management.plant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.Plant;
import com.example.greenfarm.ui.adapter.FarmerPlantAdapter;
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

public class FarmerPlantActivity extends AppCompatActivity {

    private List<Plant> plantList = new ArrayList<>();

    private RecyclerView recyclerView;

    private FarmerPlantAdapter adapter;

    private Farm mFarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_plant);

        mFarm = new Gson().fromJson(getIntent().getStringExtra("farm"),Farm.class);

        getData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerview_farmer_plantList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FarmerPlantAdapter(R.layout.recyclerview_item_plant_product,plantList,this);

        recyclerView.setAdapter(adapter);

    }

    private void getData() {
        Gson gson = new Gson();

        HashMap<String,Integer> dataToServer = new HashMap<>();
        dataToServer.put("farmId",mFarm.getId());

        String jsonToServer = gson.toJson(dataToServer);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFarmerPlantList"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showToast("网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("FarmerPlantActivity",body);
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
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FarmerPlantActivity.this,msg,Toast.LENGTH_SHORT).show();
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