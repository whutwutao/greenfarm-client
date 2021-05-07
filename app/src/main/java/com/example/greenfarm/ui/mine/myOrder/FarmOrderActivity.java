package com.example.greenfarm.ui.mine.myOrder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.ui.mine.myOrder.customer.CustomerFarmOrderNotProcessedActivity;
import com.example.greenfarm.ui.mine.myOrder.customer.CustomerFarmOrderProcessedActivity;
import com.example.greenfarm.ui.mine.myOrder.farmer.FarmOrderNotProcessedActivity;
import com.example.greenfarm.ui.mine.myOrder.farmer.FarmOrderProcessedActivity;

public class FarmOrderActivity extends AppCompatActivity {

    private RelativeLayout rlNotProcessedFarmOrder;

    private RelativeLayout rlProcessedFarmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_order);

        rlNotProcessedFarmOrder = findViewById(R.id.rl_farmer_not_processed_farm_order);
        rlNotProcessedFarmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (UserManager.currentUser.getIsFarmer() == 1) {
                    intent = new Intent(FarmOrderActivity.this, FarmOrderNotProcessedActivity.class);
                } else {
                    intent = new Intent(FarmOrderActivity.this, CustomerFarmOrderNotProcessedActivity.class);
                }
                startActivity(intent);

            }
        });

        rlProcessedFarmOrder = findViewById(R.id.rl_farmer_processed_farm_order);
        rlProcessedFarmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (UserManager.currentUser.getIsFarmer() == 1) {
                    intent = new Intent(FarmOrderActivity.this, FarmOrderProcessedActivity.class);
                } else {
                    intent = new Intent(FarmOrderActivity.this, CustomerFarmOrderProcessedActivity.class);
                }
                startActivity(intent);

            }
        });
    }
}