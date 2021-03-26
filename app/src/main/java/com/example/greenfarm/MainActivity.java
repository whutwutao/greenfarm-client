package com.example.greenfarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.greenfarm.pojo.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private User currentUser;//当前登录的用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_mine)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //获取从LoginActivity传来的当前登录用户的信息
        Intent intentFromLogin = getIntent();
        String jsonOfLoginUser = intentFromLogin.getStringExtra("loginUser");
        Gson gson = new Gson();
        //使用Gson将传来的字符串转化为User对象
        currentUser = gson.fromJson(jsonOfLoginUser,User.class);
        Log.d("MainActivity",currentUser.toString());
    }

    public User getCurrentUser() {
        return currentUser;
    }

}