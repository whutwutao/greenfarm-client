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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.farm.farmInfo.FarmInfoActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.example.greenfarm.utils.alipay.OrderInfoUtil2_0;
import com.example.greenfarm.utils.alipay.PayResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PayForFarmActivity extends AppCompatActivity {

    private HashMap<String, String> requestData;//获取orderInfo的请求参数
    private Farm mFarm;//即将租赁的农场
    private static final int SDK_PAY_FLAG = 1;
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
                        Log.d("PayForFarmActivity","支付成功");
                    } else {
                        Log.d("PayForFarmActivity","支付失败");
                    }
                    break;
                }
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
        String jsonFarm = intent.getStringExtra("farm");
        mFarm = new Gson().fromJson(jsonFarm,Farm.class);
        TextView tvTotalAmount = findViewById(R.id.tv_payment_amount);
        tvTotalAmount.setText("￥"+ mFarm.getPrice() * mFarm.getServiceLife());
    }

    /**
     * 支付宝支付业务示例
     */
    public void payV2(View v) {
        sendPayRequest();//从服务器获得orderInfo

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//		showToast(this,orderInfo);
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayForFarmActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void sendPayRequest() {
        HashMap<String,String> request = new HashMap<>();

        request.put("total_amount",String.valueOf(mFarm.getPrice()*mFarm.getServiceLife()));
        request.put("subject",mFarm.getDescription());
        String jsonToServer = new Gson().toJson(request);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/alipayTest"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("PayDemoActivity","网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("PayDemoActivity",body);
                    HashMap<String,String> result = new Gson().fromJson(body, new TypeToken<HashMap<String,String>>(){}.getType());
                    orderInfo = result.get("orderInfo");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void rentFarm(int farmId, int customerId) {
        HashMap<String,Integer> farmOrder = new HashMap<>();
        //使用map传递订单的农场编号和客户编号信息
        farmOrder.put("farmId",farmId);
        farmOrder.put("customerId",customerId);
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(farmOrder);

        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/addFarmOrder"),jsonToServer, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(FarmInfoActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    Log.d("PayForFarmActivity",body);
                    HashMap<String,String> result = gson.fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("succeed".equals(result.get("result"))) {
//                                Toast.makeText(FarmInfoActivity.this,"订单已提交",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
//                                Toast.makeText(FarmInfoActivity.this,"订单提交失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}