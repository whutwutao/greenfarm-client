package com.example.greenfarm.ui.mine.productManage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.greenfarm.R;

public class ProductManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
    }

    /**
     * 跳转至添加产品
     * @param view
     */
    public void jumpToAddProductActivity(View view) {
        Intent intent = new Intent(this,AddProductActivity.class);
        startActivity(intent);
    }
}