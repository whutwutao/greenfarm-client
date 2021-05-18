package com.example.greenfarm.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Product;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.ui.farm.EditFarmOrderActivity;
import com.example.greenfarm.ui.payment.PaymentActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.example.greenfarm.utils.addressPicker.AddressPickTask;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class EditProductOrderActivity extends AppCompatActivity {

    private Product mProduct;//当前展示的产品

    private int amount = 1;//当前选择的购买数量

    private double total = 0;//总金额

    private TextView tvBriefAddress;//所在地区

    private EditText etDetailedAddress;//详细地址

    private ImageView ivPicture;//产品图片

    private TextView tvDescription;//产品名+描述

    private TextView tvSinglePrice;//单件商品的价格

    private TextView tvAmount;//购买总数量

    private ImageView ivMinus;//减号

    private ImageView ivPlus;//加号

    private TextView tvTotal;//总金额

    private DecimalFormat df = new DecimalFormat( "0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_order);
        Intent intent = getIntent();
        mProduct = new Gson().fromJson(intent.getStringExtra("product"),Product.class);
        amount = intent.getIntExtra("amount",1);

        initView();
    }

    private void initView() {
        tvBriefAddress = findViewById(R.id.tv_product_order_brief_address);
        etDetailedAddress = findViewById(R.id.et_product_order_detailed_address);
        ivPicture = findViewById(R.id.iv_product_order_picture);
        tvDescription = findViewById(R.id.tv_product_order_description);
        tvSinglePrice = findViewById(R.id.tv_product_order_single_price);
        tvAmount = findViewById(R.id.tv_product_order_amount);
        ivMinus = findViewById(R.id.iv_product_order_minus);
        ivPlus = findViewById(R.id.iv_product_order_plus);
        tvTotal = findViewById(R.id.tv_product_order_total);

        Glide.with(this).load(HttpUtil.getUrl(mProduct.getPictureUrl())).into(ivPicture);
        tvDescription.setText(mProduct.getName() + "(" + mProduct.getDescription() +")");
        tvSinglePrice.setText("￥" + mProduct.getPrice());
        tvAmount.setText(String.valueOf(amount));
        tvTotal.setText("￥" + mProduct.getPrice()*amount);
    }

    /**
     * 选择地址
     * @param view
     */
    public void chooseAddress(View view) {
        AddressPickTask task = new AddressPickTask(EditProductOrderActivity.this);
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

    /**
     * 增加数量
     * @param view
     */
    public void increase(View view) {
        tvAmount.setText(String.valueOf(++amount));
        tvTotal.setText("￥"+df.format(mProduct.getPrice()*amount));
    }

    /**
     * 减少数量
     * @param view
     */
    public void decrease(View view) {
        if (amount > 1) {
            tvAmount.setText(String.valueOf(--amount));
            tvTotal.setText("￥"+df.format(mProduct.getPrice()*amount));
        }
    }

    /**
     * 提交订单按钮的点击事件
     * @param view
     */
    public void submitProductOrder(View view) {
        if (tvBriefAddress.getText().toString().isEmpty() || etDetailedAddress.getText().toString().isEmpty()) {
            Toast.makeText(EditProductOrderActivity.this,"地址输入不完整",Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("type",1);//支付类型设置为为产品支付
            intent.putExtra("product",new Gson().toJson(mProduct));
            intent.putExtra("amount",amount);
            intent.putExtra("money",df.format(mProduct.getPrice()*amount));
            String address = tvBriefAddress.getText().toString() + etDetailedAddress.getText().toString();
            intent.putExtra("address",address);
            startActivity(intent);
        }
    }
}