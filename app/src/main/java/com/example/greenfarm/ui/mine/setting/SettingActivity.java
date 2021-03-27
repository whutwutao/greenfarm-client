package com.example.greenfarm.ui.mine.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.myLayout.MineItemLayout;

public class SettingActivity extends AppCompatActivity {

    private MineItemLayout usernameItem;

    private MineItemLayout telephoneItem;

    private MineItemLayout passwordItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        usernameItem = findViewById(R.id.username_item);

        usernameItem.setTextContent("用户名").setTextContentValue(UserManager.currentUser.getUsername());

        telephoneItem = findViewById(R.id.telephone_item);

        telephoneItem.setTextContent("手机号").setTextContentValue(UserManager.currentUser.getTelephone());

        passwordItem = findViewById(R.id.password_item);

        passwordItem.setTextContent("修改密码");
        passwordItem.setOnRootClickListener(new MineItemLayout.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {
//                Toast.makeText(SettingActivity.this,"修改密码",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }


}