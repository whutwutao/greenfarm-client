package com.example.greenfarm.ui.farm.farminfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class FarmInfoActivity extends AppCompatActivity {

    ImageView imageView;

    TextView tvDescription;

    TextView tvAddress;

    TextView tvServiceLife;

    TextView tvPrice;

    TextView tvArea;

    TextView tvTelephone;

    Button btnAddToCart;

    Button btnRentNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_info);

        Intent intent = getIntent();
        String strFarm = intent.getStringExtra("farm");
        Gson gson = new Gson();
        Farm farm = gson.fromJson(strFarm,Farm.class);

        imageView = findViewById(R.id.iv_farm_info);
        Glide.with(this).load(farm.getPicturePath()).into(imageView);

        tvDescription = findViewById(R.id.tv_farm_info_description);
        tvDescription.setText(farm.getDescription());

        tvAddress = findViewById(R.id.tv_farm_info_address);
        tvAddress.setText(farm.getAddress());

        tvServiceLife = findViewById(R.id.tv_farm_info_service_life);
        tvServiceLife.setText(String.valueOf(farm.getServiceLife()) + "（年）");

        tvPrice = findViewById(R.id.tv_farm_info_price);
        tvPrice.setText(String.valueOf(farm.getPrice()) + "（万）");

        tvArea = findViewById(R.id.tv_farm_info_area);
        tvArea.setText(String.valueOf(farm.getArea()) + "（亩）");

        tvTelephone = findViewById(R.id.tv_farm_info_telephone);
        getOwnerTelephone(strFarm);
    }

    private void getOwnerTelephone(String strFarm) {


        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getOwnerTelephone"),strFarm, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FarmInfoActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Gson gson = new Gson();

                    HashMap<String,String> result = gson.fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                    final String telephone = result.get("telephone");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTelephone.setText(telephone);
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}