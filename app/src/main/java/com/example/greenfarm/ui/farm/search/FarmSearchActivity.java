package com.example.greenfarm.ui.farm.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.farm.adapter.FarmAdapter;
import com.example.greenfarm.ui.farm.adapter.FarmSearchAdapter;
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

public class FarmSearchActivity extends AppCompatActivity {

    private EditText etSearchFarm;//搜索输入框

    private ImageView ivSearch;//搜索右箭头

    private RecyclerView recyclerView;

    private List<Farm> farmList = new ArrayList<>();//农场数据列表

    private FarmSearchAdapter farmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_search);

        etSearchFarm = findViewById(R.id.et_search_farm);


        ivSearch = findViewById(R.id.iv_search_arrow);

        recyclerView = findViewById(R.id.recyclerview_search_farm);

        farmAdapter = new FarmSearchAdapter(R.layout.recyclerview_item_farm,farmList,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(farmAdapter);
        
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                farmAdapter.setNewData(farmList);
                if(farmList.isEmpty()) {
                    etSearchFarm.setText("搜索结果为空");
                }
            }
        });
    }


    private void getData() {
        Gson gson = new Gson();
        HashMap<String,String> map = new HashMap<>();
        map.put("condition",etSearchFarm.getText().toString());//将查询条件传到服务器
        String jsonToServer = gson.toJson(map);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/searchFarm"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("FarmSearchActivity","网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("FarmSearchActivity",body);
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