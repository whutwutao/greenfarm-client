package com.example.greenfarm.ui.mine.myFarm.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;

public class MyFarm1Activity extends AppCompatActivity {

    private TextView tvUsername;

    private TextView tvRole;

    private RelativeLayout rlFarmRent;

    private RelativeLayout rlFarmManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm1);

        tvUsername = findViewById(R.id.tv_my_farm_username);
        tvUsername.setText(UserManager.currentUser.getUsername());

        tvRole = findViewById(R.id.tv_my_farm_role);
        tvRole.setText("会员");

        rlFarmRent = findViewById(R.id.rl_farm_rent);


        rlFarmManage = findViewById(R.id.rl_farm_manage);


    }
}