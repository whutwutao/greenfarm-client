package com.example.greenfarm.ui.farm.farmInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.ui.chat.ChatActivity;
import com.example.greenfarm.ui.payment.PayForFarmActivity;
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

    ImageView ivChat;

    Button btnRentNow;

    Farm mFarm;

    User owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_info);

        Intent intent = getIntent();
        String strFarm = intent.getStringExtra("farm");
        Gson gson = new Gson();
        Farm farm = gson.fromJson(strFarm,Farm.class);
        mFarm = farm;
        imageView = findViewById(R.id.iv_farm_info);
        Glide.with(this).load(HttpUtil.getUrl(farm.getPicturePath())).into(imageView);

        tvDescription = findViewById(R.id.tv_farm_info_description);
        tvDescription.setText(farm.getDescription());

        tvAddress = findViewById(R.id.tv_farm_info_address);
        tvAddress.setText(farm.getAddress());

        tvServiceLife = findViewById(R.id.tv_farm_info_service_life);
        tvServiceLife.setText(String.valueOf(farm.getServiceLife()) + "（年）");

        tvPrice = findViewById(R.id.tv_farm_info_price);
        tvPrice.setText(String.valueOf(farm.getPrice()) + "（元）");

        tvArea = findViewById(R.id.tv_farm_info_area);
        tvArea.setText(String.valueOf(farm.getArea()) + "（亩）");

        tvTelephone = findViewById(R.id.tv_farm_info_telephone);
        getOwner(strFarm);

        btnRentNow = findViewById(R.id.btn_rent_now);//立即租赁按钮
        btnRentNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rentFarm(farm.getId(),UserManager.currentUser.getId());
                Intent intent1 = new Intent(FarmInfoActivity.this, PayForFarmActivity.class);
                intent1.putExtra("farm",new Gson().toJson(mFarm));
                startActivity(intent1);
            }
        });

        //联系商家按钮
        ivChat = findViewById(R.id.iv_chat);
        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FarmInfoActivity.this, ChatActivity.class);
                intent.putExtra("friend",new Gson().toJson(owner));//将商家对象传入ChatActivity
                startActivity(intent);
            }
        });
    }

    private void getOwner(String strFarm) {


        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getOwner"),strFarm, new okhttp3.Callback() {
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

                    HashMap<String,User> result = gson.fromJson(body,new TypeToken<HashMap<String,User>>(){}.getType());

                    owner = result.get("owner");
                    Log.d("FarmInfoActivity", owner.toString());
                    final String telephone = owner.getTelephone();
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


    private void rentFarm(int farmId, int customerId) {
        HashMap<String,Integer> farmOrder = new HashMap<>();
        //使用map传递订单的农场编号和客户编号信息
        farmOrder.put("farmId",farmId);
        farmOrder.put("customerId",customerId);
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(farmOrder);

        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/addFarmOrder"),jsonToServer, new okhttp3.Callback() {
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
                    Log.d("FarmInfoActivity",body);
                    HashMap<String,String> result = gson.fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("succeed".equals(result.get("result"))) {
                                Toast.makeText(FarmInfoActivity.this,"订单已提交",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(FarmInfoActivity.this,"订单提交失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}