package com.example.greenfarm.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Category;
import com.example.greenfarm.pojo.Product;
import com.example.greenfarm.ui.adapter.CategoryAdapter;
import com.example.greenfarm.ui.adapter.ProductAdapter;
import com.example.greenfarm.ui.custom.CustomLoadMoreView;
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

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private final static int NETWORK_ERR = -1;
    private final static int GET_CATEGORY_FAIL = 0;
    private final static int GET_CATEGORY_SUCCEED = 1;
    private final static int GET_PRODUCT_FAIL = 2;
    private final static int GET_PRODUCT_SUCCEED = 3;
    private final static int GET_MORE_PRODUCT_FAIL = 4;
    private final static int GET_MORE_PRODUCT_SUCCEED = 5;


    /**
     * 总的页数
     */
    private int totalPage = 0;

    /**
     * 每页显示的最大数量
     */
    private int mPageSize = 10;

    /**
     * 类别列表，用于存储所有类别
     */
    private List<Category> categoryList = new ArrayList<>();

    /**
     * 产品列表，存储产品数据源
     */
    private List<Product> productList = new ArrayList<>();

    /**
     * 用于显示产品类别的recyclerview
     */
    private RecyclerView categoryRecyclerView;

    /**
     * 刷新产品
     */
    private SwipeRefreshLayout productSwipeRefreshLayout;
    /**
     * 用于显示产品的recyclerview
     */
    private RecyclerView productRecyclerView;

    /**
     * 类别适配器
     */
    private CategoryAdapter categoryAdapter;

    /**
     * 产品适配器
     */
    private ProductAdapter productAdapter;


    /**
     * 用于从网络初始化UI的Handler
     * @return
     */
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NETWORK_ERR://网络错误
                    Toast.makeText(getContext(),"网络连接错误",Toast.LENGTH_SHORT);
                    break;
                case GET_CATEGORY_FAIL://数据类别获取失败
                    Toast.makeText(getContext(),"获取类别数据失败",Toast.LENGTH_SHORT);
                    break;
                case GET_CATEGORY_SUCCEED:
                    Toast.makeText(getContext(), "获取类别数据成功",Toast.LENGTH_SHORT).show();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                    categoryRecyclerView.setLayoutManager(linearLayoutManager);
                    categoryAdapter = new CategoryAdapter(categoryList,getContext());
                    categoryRecyclerView.setAdapter(categoryAdapter);
                    break;
                case GET_PRODUCT_FAIL:
                    Toast.makeText(getContext(),"获取农产品数据失败",Toast.LENGTH_SHORT);
                    break;
                case GET_PRODUCT_SUCCEED:
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
//                    productRecyclerView.setLayoutManager(gridLayoutManager);
//                    productAdapter = new ProductAdapter(productList,getContext());
//                    productRecyclerView.setAdapter(productAdapter);
                    productAdapter.setNewData(productList);
                    productSwipeRefreshLayout.setRefreshing(false);//刷新进度条停止旋转
                    break;
                case GET_MORE_PRODUCT_FAIL:
                    productAdapter.loadMoreEnd();
                    break;
                case GET_MORE_PRODUCT_SUCCEED:
                    productAdapter.setNewData(productList);
                    productAdapter.loadMoreComplete();
                    break;
            }
        }
    };





//    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.home_fragment, container, false);
        initView(root);

        //初始化类别列表
        initCategoryList();
        //初始化产品列表
        initProductList(true);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    //完成控件的绑定
    private void initView(View view) {
        categoryRecyclerView = view.findViewById(R.id.recyclerview_product_category);
        productRecyclerView = view.findViewById(R.id.recyclerview_product);
        productSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_product);
        productSwipeRefreshLayout.setOnRefreshListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productAdapter = new ProductAdapter(productList,getContext());
        productAdapter.setLoadMoreView(new CustomLoadMoreView());
        productAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initProductList(false);
                    }
                },1000);
            }
        },productRecyclerView);
        productRecyclerView.setAdapter(productAdapter);
        EditText etSearch = view.findViewById(R.id.et_search_product_home);
        etSearch.setFocusable(false);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SearchProductActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 完成类别数据的初始化
     */
    private void initCategoryList() {
        try {
            HttpUtil.sendOkHttpRequest(HttpUtil.getUrl("/getAllCategory"),new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message msg = Message.obtain();
                    msg.what = NETWORK_ERR;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    categoryList = new Gson().fromJson(body,new TypeToken<List<Category>>(){}.getType());
                    Message msg = Message.obtain();
                    if (categoryList == null) {
                        msg.what = GET_CATEGORY_FAIL;
                    } else {
                        msg.what = GET_CATEGORY_SUCCEED;
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前选择的类别
     * @return
     */
    private Category getSelectedCategory() {
        for (Category category : categoryList) {
            if (category.isSelected()) {
                return category;
            }
        }
        return null;
    }

    /**
     * 初始化农产品列表
     */
    private void initProductList(boolean isRefresh) {
        Category category = getSelectedCategory();
        if (isRefresh) {//刷新
            try {
                int start = 0;
                int len = 6;
                HashMap<String,String> request = new HashMap<>();
                request.put("category",new Gson().toJson(category));
                request.put("start",String.valueOf(start));
                request.put("len",String.valueOf(len));
                String jsonToServer = new Gson().toJson(request);
                HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getProductByCategoryLimit"), jsonToServer, new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message msg = Message.obtain();
                        msg.what = NETWORK_ERR;
                        mHandler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String body = response.body().string();
                        productList = new Gson().fromJson(body, new TypeToken<List<Product>>(){}.getType());
                        Message msg = Message.obtain();
                        if (productList == null) {
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
        } else {//加载更多数据
            try {
                int start = productList.size();
                int len = 6;
                HashMap<String,String> request = new HashMap<>();
                request.put("category",new Gson().toJson(category));
                request.put("start",String.valueOf(start));
                request.put("len",String.valueOf(len));
                String jsonToServer = new Gson().toJson(request);
                HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getProductByCategoryLimit"), jsonToServer, new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message msg = Message.obtain();
                        msg.what = NETWORK_ERR;
                        mHandler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String body = response.body().string();
                        Log.d("LoadMoreProduct",body);
                        List<Product> list = new Gson().fromJson(body, new TypeToken<List<Product>>(){}.getType());

                        Message msg = Message.obtain();
                        if (list == null || list.isEmpty()) {
                            msg.what = GET_MORE_PRODUCT_FAIL;
                        } else {
                            msg.what = GET_MORE_PRODUCT_SUCCEED;
                            for (Product product : list) {
                                productList.add(product);
                            }
                        }
                        mHandler.sendMessage(msg);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initProductList(true);
            }
        },1000);

    }
}