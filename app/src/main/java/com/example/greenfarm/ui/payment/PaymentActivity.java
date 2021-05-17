package com.example.greenfarm.ui.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.Product;
import com.example.greenfarm.ui.farm.FarmFragment;
import com.example.greenfarm.ui.main.MainActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.example.greenfarm.utils.alipay.PayResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PaymentActivity extends AppCompatActivity {

    private HashMap<String, String> requestData;//获取orderInfo的请求参数
    private int paymentType;//支付类型
    private Product mProduct;//即将购买的产品
    private Farm mFarm;//即将租赁的农场
    private String address;//收货地址
    private static final int NETWORK_ERR = 0;
    private static final int SDK_PAY_FLAG = 1;
    private static final int GET_ORDER_INFO_SUCCEED = 2;
    private static final int GET_ORDER_INFO_FAIL = 3;
    private static final int RENT_FARM_SUCCEED = 4;
    private static final int RENT_FARM_FAIL = 5;
    private String orderInfo;



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        rentFarm(mFarm.getId(), UserManager.currentUser.getId());
                    } else {
                        Toast.makeText(PaymentActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }
                case NETWORK_ERR:
                    Toast.makeText(PaymentActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                    break;
                case GET_ORDER_INFO_SUCCEED:
                    /**
                     * 订单信息获取成功，进行支付
                     */
                    payV2();
                    break;
                case GET_ORDER_INFO_FAIL:
                    Toast.makeText(PaymentActivity.this,"订单信息获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case RENT_FARM_SUCCEED:
                    finish();
                case RENT_FARM_FAIL:
                    finish();
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_farm);
        Intent intent = getIntent();
        paymentType = intent.getIntExtra("type",-1);
        if (paymentType == 0) {
            String jsonFarm = intent.getStringExtra("farm");
            mFarm = new Gson().fromJson(jsonFarm,Farm.class);
            address = intent.getStringExtra("address");
            TextView tvTotalAmount = findViewById(R.id.tv_payment_amount);
            tvTotalAmount.setText("￥"+ mFarm.getPrice() * mFarm.getServiceLife());
        } else {

        }

    }

    /**
     * 支付按钮点击事件
     * @param view
     */
    public void submit(View view) {
        getOrderInfo();//从服务器获得orderInfo
    }

    /**
     * 支付宝支付业务示例
     */
    public void payV2() {
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PaymentActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        /**
         * 必须异步调用
         */
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 获取orderInfo
     */
    private void getOrderInfo() {
        HashMap<String,String> request = new HashMap<>();
        if (paymentType == 0) {
            request.put("total_amount",String.valueOf(mFarm.getPrice()*mFarm.getServiceLife()));
            request.put("subject",mFarm.getDescription());
        } else {

        }
        String jsonToServer = new Gson().toJson(request);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/alipayTest"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message msg = Message.obtain();
                    msg.what = NETWORK_ERR;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("PayDemoActivity",body);
                    HashMap<String,String> result = new Gson().fromJson(body, new TypeToken<HashMap<String,String>>(){}.getType());
                    orderInfo = result.get("orderInfo");
                    Message msg = Message.obtain();
                    if (orderInfo != null) {
                        msg.what = GET_ORDER_INFO_SUCCEED;

                    } else {
                        msg.what = GET_ORDER_INFO_FAIL;
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void rentFarm(int farmId, int customerId) {
        HashMap<String,String> farmOrder = new HashMap<>();
        //使用map传递订单的农场编号和客户编号信息
        farmOrder.put("farmId",String.valueOf(farmId));
        farmOrder.put("customerId",String.valueOf(customerId));
        farmOrder.put("address",address);
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(farmOrder);

        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/addFarmOrder"),jsonToServer, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            msg.what = NETWORK_ERR;
                            mHandler.sendMessage(msg);
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    Log.d("PayForFarmActivity",body);
                    HashMap<String,String> result = gson.fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                    Message msg = Message.obtain();
                    if ("succeed".equals(result.get("result"))) {
                        msg.what = RENT_FARM_SUCCEED;
                    } else {
                        msg.what = RENT_FARM_FAIL;
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}