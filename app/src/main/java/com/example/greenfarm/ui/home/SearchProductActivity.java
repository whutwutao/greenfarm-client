package com.example.greenfarm.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Condition;
import com.example.greenfarm.pojo.Product;
import com.example.greenfarm.ui.adapter.ProductAdapter;
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

public class SearchProductActivity extends AppCompatActivity {

    private final static int NETWORK_ERR = -1;
    private final static int GET_PRODUCT_FAIL = 2;
    private final static int GET_PRODUCT_SUCCEED = 3;
    private final static int GET_MORE_PRODUCT_FAIL = 4;
    private final static int GET_MORE_PRODUCT_SUCCEED = 5;

    /**
     * 产品列表
     */
    private List<Product> productList = new ArrayList<>();

    private RecyclerView productRecyclerView;

    private ProductAdapter productAdapter;

    private ImageView ivSearch;

    private EditText etSearch;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what) {
                case NETWORK_ERR:
                    Toast.makeText(SearchProductActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                    break;
                case GET_PRODUCT_FAIL:
                    etSearch.setText("搜索结果为空");
                    break;
                case GET_PRODUCT_SUCCEED:
                    productAdapter.setNewData(productList);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        initView();
    }

    private void initView() {
        ivSearch = findViewById(R.id.iv_search_product_arrow);
        etSearch = findViewById(R.id.et_search_product);
        RecyclerView productRecyclerView = findViewById(R.id.recyclerview_search_product);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productAdapter = new ProductAdapter(productList,this);
        productRecyclerView.setAdapter(productAdapter);
//        ivSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("click","clicked");
//                if (!etSearch.getText().toString().isEmpty()) {
//                    getProductList();
//                }
//            }
//        });
    }

    public void search(View view) {
        Log.d("click","clicked");
        if (!etSearch.getText().toString().isEmpty()) {
            getProductList();
        }
    }

    private void getProductList() {
        String name = etSearch.getText().toString();
        HashMap<String,String> request = new HashMap<>();
        request.put("name",name);
        String jsonToServer = new Gson().toJson(request);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getProductByName"),jsonToServer,new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message msg = Message.obtain();
                    msg.what = NETWORK_ERR;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("searchProduct",body);
                    productList = new Gson().fromJson(body,new TypeToken<List<Product>>(){}.getType());
                    Log.d("productList",productList.toString());
                    Message msg = Message.obtain();
                    if (productList.isEmpty()) {
                        msg.what = GET_PRODUCT_FAIL;
                    } else {
                        msg.what = GET_PRODUCT_SUCCEED;
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}