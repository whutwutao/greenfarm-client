package com.example.greenfarm.ui.cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.CartAdapterItem;
import com.example.greenfarm.ui.adapter.CartPayAdapter;
import com.example.greenfarm.ui.home.EditProductOrderActivity;
import com.example.greenfarm.ui.payment.PaymentActivity;
import com.example.greenfarm.utils.addressPicker.AddressPickTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class EditCartOrderActivity extends AppCompatActivity {


    private TextView tvBriefAddress;

    private EditText etDetailedAddress;

    private RecyclerView recyclerView;

    private CartPayAdapter cartPayAdapter;

    private TextView tvTotal;

    private Button btnPay;


    private List<CartAdapterItem> cartAdapterItemList = new ArrayList<>();

    private double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart_order);

        Intent intent = getIntent();
        String strDataList = intent.getStringExtra("dataList");
        cartAdapterItemList = new Gson().fromJson(strDataList,new TypeToken<List<CartAdapterItem>>(){}.getType());
        total = intent.getDoubleExtra("total",0);

        initView();
    }

    private void initView() {
        tvBriefAddress = findViewById(R.id.tv_cart_order_brief_address);
        etDetailedAddress = findViewById(R.id.et_cart_order_detailed_address);
        recyclerView = findViewById(R.id.recyclerview_cart_order);
        tvTotal = findViewById(R.id.tv_cart_order_total_money);
        btnPay = findViewById(R.id.btn_pay_for_cart_order);

        tvBriefAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressPickTask task = new AddressPickTask(EditCartOrderActivity.this);
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
                        } else {
                            tvBriefAddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                        }
                    }
                });
                task.execute("贵州", "毕节", "纳雍");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        cartPayAdapter = new CartPayAdapter(cartAdapterItemList,this);
        recyclerView.setAdapter(cartPayAdapter);

        tvTotal.setText("￥" + total);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvBriefAddress.getText().toString().isEmpty() || etDetailedAddress.getText().toString().isEmpty()) {
                    Toast.makeText(EditCartOrderActivity.this,"地址信息不全",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(EditCartOrderActivity.this, PaymentActivity.class);
                String jsonCateAdapterItemList = new Gson().toJson(cartAdapterItemList);
                intent.putExtra("type",2);
                intent.putExtra("cartAdapterItemList",jsonCateAdapterItemList);
                intent.putExtra("money",total);
                intent.putExtra("address",tvBriefAddress.getText().toString()+etDetailedAddress.getText().toString());
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
//                    Toast.makeText(EditCartOrderActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();

                }
                break;
            default:
                break;
        }
    }
}