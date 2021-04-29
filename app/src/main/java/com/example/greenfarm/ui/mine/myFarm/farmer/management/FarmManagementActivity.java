package com.example.greenfarm.ui.mine.myFarm.farmer.management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.mine.myFarm.customer.management.monitor.FarmListActivity;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.customerInfo.CustomerInfoActivity;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.plant.FarmerPlantActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class FarmManagementActivity extends AppCompatActivity {

    private Farm mFarm;

    private RelativeLayout rlCustomerInfo;

    private RelativeLayout rlPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_farm_management);

        mFarm = new Gson().fromJson(getIntent().getStringExtra("farm"),Farm.class);
//        showToast(mFarm.toString());
        rlCustomerInfo = findViewById(R.id.rl_farmer_farm_management_customer_info);
        rlCustomerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FarmManagementActivity.this, CustomerInfoActivity.class);
                intent.putExtra("farm",new Gson().toJson(mFarm));
                startActivity(intent);
            }
        });

        rlPlant = findViewById(R.id.rl_farmer_farm_management_plant);
        rlPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FarmManagementActivity.this, FarmerPlantActivity.class);
                intent.putExtra("farm",new Gson().toJson(mFarm));
                startActivity(intent);
            }
        });
    }


    private void showToast(String text) {
        final Activity activity = FarmManagementActivity.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            }
        });
    }
}