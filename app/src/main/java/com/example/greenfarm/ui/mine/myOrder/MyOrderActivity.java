package com.example.greenfarm.ui.mine.myOrder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.greenfarm.R;

public class MyOrderActivity extends AppCompatActivity {

    private RelativeLayout rlProductOrder;

    private RelativeLayout rlFarmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        rlProductOrder = findViewById(R.id.rl_farmer_product_order);

        rlFarmOrder = findViewById(R.id.rl_farmer_farm_order);
        rlFarmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyOrderActivity.this, FarmOrderActivity.class);
                startActivity(intent);


            }
        });

    }
}