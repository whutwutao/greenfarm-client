package com.example.greenfarm.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Product;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.ui.chat.ChatActivity;
import com.example.greenfarm.ui.farm.farmInfo.FarmInfoActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class ProductInfoActivity extends AppCompatActivity {

    private final static int NETWORK_ERR = -1;//网络错误
    private final static int GET_FARMER_FAIL = 0;//获取商家信息失败
    private final static int GET_FARMER_SUCCEED = 1;//获取商家信息成功

    private Product mProduct;//当前展示的产品

    private User mFarmer;//商家

    private ImageView ivProductPicture;//产品图片

    private TextView tvProductName;//产品名称

    private TextView tvProductDescription;//产品描述

    private TextView tvProductPrice;//产品价格

    private TextView tvPurchaseAmount;//购买数量


    private int amount = 1;//当前选择的购买数量

    private Handler mHandler;//处理异步消息


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what) {
                    case NETWORK_ERR:
                        showToast("网络连接错误");
                        break;
                    case GET_FARMER_FAIL:
                        showToast("商家信息获取失败");
                        break;
                    case GET_FARMER_SUCCEED:
                        showToast("商家信息获取成功");
                        break;
                }
            }
        };

        /**
         * 获取产品信息
         */
        Intent intent = getIntent();
        mProduct = new Gson().fromJson(intent.getStringExtra("product"), Product.class);
        /**
         * 初始化界面
         */
        initView();
        /**
         * 获取商家信息
         */
        getFarmer();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        ivProductPicture = findViewById(R.id.iv_product_info_picture);
        tvProductName = findViewById(R.id.tv_product_info_name);
        tvProductDescription = findViewById(R.id.tv_product_info_description);
        tvProductPrice = findViewById(R.id.tv_product_info_price);
        tvPurchaseAmount = findViewById(R.id.tv_product_info_amount);

        Glide.with(this).load(HttpUtil.getUrl(mProduct.getPictureUrl())).into(ivProductPicture);
        tvProductName.setText(mProduct.getName());
        tvProductDescription.setText(mProduct.getDescription());
        tvProductPrice.setText("￥"+mProduct.getPrice());

    }

    /**
     * 减少数量
     * @param view
     */
    public void decrease(View view) {
        if (amount > 1) {
            tvPurchaseAmount.setText(String.valueOf(--amount));
        }
    }

    /**
     * 增加数量
     * @param view
     */
    public void increase(View view) {
        tvPurchaseAmount.setText(String.valueOf(++amount));
    }

    /**
     * 获取商家信息
     */
    private void getFarmer() {
        HashMap<String,String> request = new HashMap<>();
        request.put("farmerId",String.valueOf(mProduct.getFarmerId()));
        String jsonToServer = new Gson().toJson(request);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getProductFarmer"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message = Message.obtain();
                    message.what = NETWORK_ERR;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("getFarmer",body);
                    mFarmer = new Gson().fromJson(body,User.class);
                    Message message = Message.obtain();
                    message.what = GET_FARMER_SUCCEED;
                    mHandler.sendMessage(message);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 联系商家图标的点击事件,跳转至聊天界面
     * @param view
     */
    public void chatWithFarmer(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("friend",new Gson().toJson(mFarmer));//将商家对象传入ChatActivity
        startActivity(intent);
    }

    /**
     * 立即购买的点击事件
     * @param view
     */
    public void buyNow(View view) {
        Intent intent = new Intent(this, EditProductOrderActivity.class);
        String jsonProduct = new Gson().toJson(mProduct);
        String jsonFarmer = new Gson().toJson(mFarmer);
        /**
         * 将产品、数量传入订单填写页面
         */
        intent.putExtra("product",jsonProduct);
        intent.putExtra("amount",amount);
        startActivity(intent);
    }

    /**
     * 封装Toast显示方法
     * @param msg
     */
    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}