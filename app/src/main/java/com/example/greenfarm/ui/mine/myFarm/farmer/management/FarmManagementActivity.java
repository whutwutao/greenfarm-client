package com.example.greenfarm.ui.mine.myFarm.farmer.management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.add.AddFarmActivity;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.monitor.FarmListActivity;

public class FarmManagementActivity extends AppCompatActivity {

    RelativeLayout rlFarmMonitor;

    RelativeLayout rlAddFarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_management);

        rlFarmMonitor = findViewById(R.id.rl_farm_management_monitor);
        rlFarmMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FarmManagementActivity.this, FarmListActivity.class);
                startActivity(intent);
            }
        });

        rlAddFarm = findViewById(R.id.rl_add_farm);
        rlAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FarmManagementActivity.this, AddFarmActivity.class);
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