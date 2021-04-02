package com.example.greenfarm.ui.mine.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.myLayout.MineItemLayout;

public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        RelativeLayout rlChangeUsername = findViewById(R.id.rl_change_username);
        rlChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangeUsernameActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout rlChangeTelephone = findViewById(R.id.rl_change_telephone);
        rlChangeTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangeTelephoneActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout rlChangePassword = findViewById(R.id.rl_change_password);
        rlChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}