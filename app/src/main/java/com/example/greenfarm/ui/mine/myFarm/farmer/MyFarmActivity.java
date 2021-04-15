package com.example.greenfarm.ui.mine.myFarm.farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.FarmManagementActivity;

public class MyFarmActivity extends AppCompatActivity {

    private TextView tvUsername;

    private TextView tvRole;

    private RelativeLayout rlFarmRent;

    private RelativeLayout rlFarmManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);

        tvUsername = findViewById(R.id.tv_my_farm_username);
        tvUsername.setText(UserManager.currentUser.getUsername());

        tvRole = findViewById(R.id.tv_my_farm_role);
        tvRole.setText("农场主");

        rlFarmRent = findViewById(R.id.rl_farm_rent);
        rlFarmRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("点击了农场出租");
            }
        });

        rlFarmManage = findViewById(R.id.rl_farm_manage);
        rlFarmManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFarmActivity.this, FarmManagementActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvUsername.setText(UserManager.currentUser.getUsername());
    }

    private void showToast(String text) {
        final Activity activity = MyFarmActivity.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            }
        });
    }

}