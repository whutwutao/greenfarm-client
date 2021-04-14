package com.example.greenfarm.ui.mine.myFarm.farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;

public class MyFarmActivity extends AppCompatActivity {

    private TextView tvUsername;

    private TextView tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);

        tvUsername = findViewById(R.id.tv_my_farm_username);
        tvUsername.setText(UserManager.currentUser.getUsername());

        tvRole = findViewById(R.id.tv_my_farm_role);
        tvRole.setText("农场主");
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        tvUsername.setText(UserManager.currentUser.getUsername());
//    }
}