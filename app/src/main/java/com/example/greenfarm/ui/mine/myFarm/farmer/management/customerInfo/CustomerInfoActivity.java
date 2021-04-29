package com.example.greenfarm.ui.mine.myFarm.farmer.management.customerInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class CustomerInfoActivity extends AppCompatActivity {

    TextView tvCustomerUsername;

    TextView tvCustomerTelephone;

    TextView tvNoCustomerWarning;

    Farm mFarm;

    User mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        tvCustomerTelephone = findViewById(R.id.tv_customer_info_telephone);

        tvCustomerUsername = findViewById(R.id.tv_customer_info_username);

        tvNoCustomerWarning = findViewById(R.id.tv_warning_no_customer);

        mFarm = new Gson().fromJson(getIntent().getStringExtra("farm"),Farm.class);

        getData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mCustomer == null) {

            tvNoCustomerWarning.setVisibility(View.VISIBLE);
        }

    }

    private void getData() {
        HashMap<String,Integer> requestData = new HashMap<>();
        requestData.put("farmId",mFarm.getId());
        String jsonToServer = new Gson().toJson(requestData);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getCustomer"), jsonToServer, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showToast("网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("CustomerInfoActivity",body);
                    mCustomer = new Gson().fromJson(body,User.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCustomer != null) {
                                tvCustomerUsername.setText(mCustomer.getUsername());
                                tvCustomerTelephone.setText(mCustomer.getTelephone());
                            }
                        }
                    });
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