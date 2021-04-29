package com.example.greenfarm.ui.mine.myFarm.farmer.management.plant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Plant;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class PlantInfoActivity extends AppCompatActivity {

    Plant mPlant;

    TextView tvPlantName;

    TextView tvPlantDescription;

    TextView tvPlantAmount;

    TextView tvPlantStatus;

    Button btnPlantConfirm;

    Button btnPlantFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        mPlant = new Gson().fromJson(getIntent().getStringExtra("plant"),Plant.class);
        showToast(mPlant.toString());
        tvPlantName = findViewById(R.id.tv_plant_info_name_content);
        tvPlantName.setText(mPlant.getName());

        tvPlantDescription = findViewById(R.id.tv_plant_info_description_content);
        tvPlantDescription.setText(mPlant.getDescription());

        tvPlantAmount = findViewById(R.id.tv_plant_info_amount_content);
        tvPlantAmount.setText(mPlant.getAmount());

        tvPlantStatus = findViewById(R.id.tv_plant_info_status_content);

        btnPlantConfirm = findViewById(R.id.btn_plant_confirm);
        btnPlantFinish = findViewById(R.id.btn_plant_finish);

        if (mPlant.getStatus() == 0) {
            tvPlantStatus.setText("未种植");
            btnPlantConfirm.setVisibility(View.VISIBLE);
            btnPlantConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPlant.setStatus(1);
                    setPlantStatus(mPlant);
                }
            });
        } else if (mPlant.getStatus() == 1) {
            tvPlantStatus.setText("已种植");
            btnPlantFinish.setVisibility(View.VISIBLE);
            btnPlantFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPlant.setStatus(2);
                    setPlantStatus(mPlant);
                }
            });
        } else {
            tvPlantStatus.setText("已种好");
        }
    }

    private void setPlantStatus(Plant plant) {
        String jsonToServer = new Gson().toJson(plant);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/setPlantStatus"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showToast("网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    HashMap<String,String> result = new Gson().fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                    if ("succeed".equals(result.get("result"))) {
                        showToast("操作成功");
                        finish();
                    } else {
                        showToast("操作失败");
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
                Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

}