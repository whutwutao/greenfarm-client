package com.example.greenfarm.ui.mine.myFarm.customer.management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenfarm.R;
import com.example.greenfarm.ui.mine.myFarm.customer.management.monitor.MonitorActivity;


public class CustomerFarmManagementActivity extends AppCompatActivity {

    RelativeLayout rlFarmMonitor;//监控

    RelativeLayout rlFarmPlant;//种植

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_farm_management);

        rlFarmMonitor = findViewById(R.id.rl_farm_management_monitor);
        rlFarmMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerFarmManagementActivity.this, MonitorActivity.class);
                startActivity(intent);
            }
        });

        rlFarmPlant = findViewById(R.id.rl_farm_management_plant);
        rlFarmPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    private void showToast(String text) {
        final Activity activity = CustomerFarmManagementActivity.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            }
        });
    }
}