package com.example.greenfarm.ui.cart;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.CartAdapterItem;
import com.example.greenfarm.ui.adapter.CartAdapter;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class CartFragment extends Fragment {
    private CartViewModel mViewModel;

    private final static int NETWORK_ERR = -1;
    private final static int GET_CART_DATA_FAIL = 0;
    private final static int GET_CART_DATA_SUCCEED = 1;
    private final static int DELETE_CART_FAIL = 2;
    private final static int DELETE_CART_SUCCEED = 3;

    private TextView tvEditCart;//编辑按钮

    private boolean isEdit = false;//是否处于编辑状态

    private LinearLayout llTotalMoney;//结算总金额的父布局

    private TextView tvTotalMoney;//结算总金额

    private Button btnPay;//去结算

    private Button btnDeleteCart;//删除

    private List<CartAdapterItem> cartAdapterItemList = new ArrayList<>();

    private RecyclerView cartRecyclerView;

    private CartAdapter cartAdapter;

    private double total;//总金额

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NETWORK_ERR:
                    showToast("网络连接错误");
                    break;
                case GET_CART_DATA_FAIL:
                    showToast("购物车信息获取失败");
                    break;
                case GET_CART_DATA_SUCCEED:
                    showToast("购物车信息获取成功");
                    cartAdapter.setNewData(cartAdapterItemList);
                    break;
                case DELETE_CART_FAIL:
                    showToast("删除购物车失败");
                    break;
                case DELETE_CART_SUCCEED:
                    showToast("删除购物车成功");
//                    for (int i = 0; i < cartAdapterItemList.size(); i++) {
//                        if (cartAdapterItemList.get(i).isSelected()) {
//                            total -= cartAdapterItemList.get(i).getCart().getMoney();
//                            cartAdapterItemList.remove(i);
//                        }
//                    }
                    initCartData();
                    total = 0;
                    tvTotalMoney.setText("￥" + total);
                    break;
            }
        }
    };


    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.cart_fragment, container, false);
        initView(root);

        initCartData();

        return root;
    }

    private void initView(View view) {
        tvEditCart = view.findViewById(R.id.tv_edit_cart);
        llTotalMoney = view.findViewById(R.id.ll_cart_total_money);
        tvTotalMoney = view.findViewById(R.id.tv_cart_total_money);
        btnPay = view.findViewById(R.id.btn_pay_for_cart);
        btnPay.setVisibility(View.VISIBLE);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CartAdapterItem> dataList = new ArrayList<>();
                for (CartAdapterItem cartAdapterItem : cartAdapterItemList) {
                    if (cartAdapterItem.isSelected()) {
                        dataList.add(cartAdapterItem);
                    }
                }
                if (dataList.isEmpty()) {
                    showToast("您还没有选择商品");
                    return;
                }
                Intent intent = new Intent(getContext(),EditCartOrderActivity.class);
                intent.putExtra("dataList",new Gson().toJson(dataList));
                intent.putExtra("total",total);
                startActivityForResult(intent,1);
            }
        });

        btnDeleteCart = view.findViewById(R.id.btn_delete_cart);
        btnDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEmpty = true;
                for (CartAdapterItem cartAdapterItem : cartAdapterItemList) {
                    if (cartAdapterItem.isSelected()) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    showToast("请选择要删除的商品");
                    return;
                }
                deleteCartList();
            }
        });
        btnDeleteCart.setVisibility(View.GONE);
        cartRecyclerView = view.findViewById(R.id.recyclerview_cart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartRecyclerView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(cartAdapterItemList,getContext());
        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onSelect(int position) {
                total += cartAdapterItemList.get(position).getCart().getMoney();
                tvTotalMoney.setText("￥" + total);
            }

            @Override
            public void onUnSelect(int position) {
                if (total >= cartAdapterItemList.get(position).getCart().getMoney()) {
                    total -= cartAdapterItemList.get(position).getCart().getMoney();
                    tvTotalMoney.setText("￥" + total);
                }
            }
        });
        cartRecyclerView.setAdapter(cartAdapter);

        tvEditCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {//如果处于编辑状态，则此时的显示为"完成"，点击让其变为"编辑"，并更新界面，变成选择支付
                    tvEditCart.setText("编辑");
                    llTotalMoney.setVisibility(View.VISIBLE);
                    btnDeleteCart.setVisibility(View.GONE);
                    btnPay.setVisibility(View.VISIBLE);
                } else {//如果不处于编辑状态，则此时的显示为"编辑"，点击让其变为"完成"，并更新界面，变成编辑购物车
                    tvEditCart.setText("完成");
                    llTotalMoney.setVisibility(View.GONE);
                    btnDeleteCart.setVisibility(View.VISIBLE);
                    btnPay.setVisibility(View.GONE);
                }
                isEdit = !isEdit;
            }
        });
    }

    private void initCartData() {
        HashMap<String,String> request = new HashMap<>();
        request.put("customerId",String.valueOf(UserManager.currentUser.getId()));
        String jsonToServer = new Gson().toJson(request);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getCartAdapterItem"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message msg = Message.obtain();
                    msg.what = NETWORK_ERR;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("cartData",body);
                    cartAdapterItemList = new Gson().fromJson(body, new TypeToken<List<CartAdapterItem>>(){}.getType());
                    Message msg = Message.obtain();
                    if (!cartAdapterItemList.isEmpty()) {
                        msg.what = GET_CART_DATA_SUCCEED;
                    } else {
                        msg.what = GET_CART_DATA_FAIL;
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    showToast("结算成功");
                    deleteCartList();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 删除购物车
     */
    private void deleteCartList() {
        List<Integer> cartIdList = new ArrayList<>();//将要删除的Cart的id列表
        for (CartAdapterItem cartAdapterItem : cartAdapterItemList) {
            if (cartAdapterItem.isSelected()) {
                cartIdList.add(cartAdapterItem.getCart().getId());
            }
        }
        String jsonCartIdList = new Gson().toJson(cartIdList);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/deleteCartList"),jsonCartIdList,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message msg = Message.obtain();
                    msg.what = NETWORK_ERR;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("删除购物车",body);
                    HashMap<String,String> res = new Gson().fromJson(body,new TypeToken<HashMap<String,String>>(){}.getType());
                    Message msg = Message.obtain();
                    if (res.get("result").equals("success")) {
                        msg.what = DELETE_CART_SUCCEED;
                    } else {
                        msg.what = DELETE_CART_FAIL;
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        // TODO: Use the ViewModel
    }

}