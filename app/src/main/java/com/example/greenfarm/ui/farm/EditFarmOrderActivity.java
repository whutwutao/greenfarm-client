package com.example.greenfarm.ui.farm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.payment.PayForFarmActivity;
import com.example.greenfarm.utils.addressPicker.AddressPickTask;
import com.google.gson.Gson;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class EditFarmOrderActivity extends AppCompatActivity {

    private Farm mFarm;
    private TextView tvBriefAddress;//省市区
    private EditText etDetailedAddress;//详细地址
    private TextView tvTotal;//总金额
    private Button btnSubmitOrder;//提交订单按钮

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_farm_order);
        Intent intent = getIntent();
        mFarm = new Gson().fromJson(intent.getStringExtra("farm"),Farm.class);
        tvBriefAddress = findViewById(R.id.tv_brief_address);

//        etBriefAddress.setFocusable(false);
        tvBriefAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressPickTask task = new AddressPickTask(EditFarmOrderActivity.this);
                task.setHideProvince(false);
                task.setHideCounty(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
//                        showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
//                            showToast(province.getAreaName() + city.getAreaName());
                        } else {
//                            showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                            tvBriefAddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                        }
                    }
                });
                task.execute("贵州", "毕节", "纳雍");
            }
        });


        etDetailedAddress = findViewById(R.id.et_detailed_address);
        tvTotal = findViewById(R.id.tv_farm_total);
        if (mFarm != null) {
            tvTotal.setText("￥"+mFarm.getServiceLife() * mFarm.getPrice());
        }

        btnSubmitOrder = findViewById(R.id.btn_submit_farm_order);
        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvBriefAddress.getText().toString().isEmpty() || etDetailedAddress.getText().toString().isEmpty()) {
                    Toast.makeText(EditFarmOrderActivity.this,"地址输入不完整",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(EditFarmOrderActivity.this, PayForFarmActivity.class);
                    intent.putExtra("farm",new Gson().toJson(mFarm));
                    String address = tvBriefAddress.getText().toString() + etDetailedAddress.getText().toString();
                    intent.putExtra("address",address);
                    startActivity(intent);
                }
            }
        });
    }
}