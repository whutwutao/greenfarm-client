package com.example.greenfarm.ui.mine.myFarm.customer.management.plant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.Plant;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class CustomerAddPlantActivity extends AppCompatActivity {

    private Farm mFarm;

    private EditText etPlantName;

    private EditText etPlantDescription;

    private EditText etPlantAmount;

    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_plant);
        mFarm = new Gson().fromJson(getIntent().getStringExtra("farm"),Farm.class);
        showToast(mFarm.toString());
        etPlantName = findViewById(R.id.et_customer_add_plant_name);
        etPlantDescription = findViewById(R.id.et_customer_add_plant_description);
        etPlantAmount = findViewById(R.id.et_customer_add_plant_amount);


        btnFinish = findViewById(R.id.btn_customer_add_plant_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etPlantName.getText().toString();
                String description = etPlantDescription.getText().toString();
                String amount = etPlantAmount.getText().toString();
                Plant plant = new Plant();
                plant.setName(name);
                plant.setDescription(description);
                plant.setAmount(amount);
                plant.setCustomerId(UserManager.currentUser.getId());
                plant.setFarmId(mFarm.getId());
                plant.setStatus(0);
                String jsonToServer = new Gson().toJson(plant);
                try {
                    HttpUtil.postWithOkHttp(HttpUtil.getUrl("/addPlant"),jsonToServer,new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            showToast("网络连接错误");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String body = response.body().string();
                            HashMap<String,String> result = new Gson().fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                            if ("succeed".equals(result.get("result"))) {
                                showToast("添加成功");
                                finish();
                            } else {
                                showToast("添加失败");
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showToast(String msg) {
        final Activity activity= CustomerAddPlantActivity.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}