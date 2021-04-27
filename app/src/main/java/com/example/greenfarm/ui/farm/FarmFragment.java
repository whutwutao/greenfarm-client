package com.example.greenfarm.ui.farm;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.ui.custom.CustomLoadMoreView;
import com.example.greenfarm.ui.farm.adapter.FarmAdapter;
import com.example.greenfarm.ui.farm.search.FarmSearchActivity;
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

public class FarmFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private FarmViewModel mViewModel;

    private EditText etSearchFarm;//农场搜索输入框

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private List<Farm> farmList = new ArrayList<>();

    private boolean hasMore = false;

    private FarmAdapter farmAdapter;

    public static FarmFragment newInstance() {
        return new FarmFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.farm_fragment, container, false);

        etSearchFarm = root.findViewById(R.id.et_search);
        etSearchFarm.setFocusable(false);
        etSearchFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FarmSearchActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = root.findViewById(R.id.refresh_farm_item);

        recyclerView = root.findViewById(R.id.recyclerview_farm);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);

        farmAdapter = new FarmAdapter(this, R.layout.recyclerview_item_farm,farmList);

        farmAdapter.setLoadMoreView(new CustomLoadMoreView());

        farmAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(false);
                        try {
                            Thread.sleep(500);//主线程睡半秒是因为getData中开启了子线程加载数据，要等数据加载完才能进行UI的操作
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (hasMore) {
                            farmAdapter.setNewData(farmList);
                            farmAdapter.loadMoreComplete();
                        } else {
                            farmAdapter.loadMoreEnd();
                        }
                    }
                }, 1000);
            }
        }, recyclerView);

        recyclerView.setAdapter(farmAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();

        return root;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(FarmViewModel.class);
//        // TODO: Use the ViewModel
//    }

    private void getData(boolean isRefresh) {
        if (isRefresh) {
            farmList.clear();
        }
        int start = farmList.size(), len = 10;
        HashMap<String, Integer> map = new HashMap<>();
        map.put("start", start);
        map.put("len", len);
        Gson gson = new Gson();
        String jsonToServer = gson.toJson(map);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFarmLimit") , jsonToServer, new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("FarmFragment", "网络连接错误");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("FarmFragment", body);
                    Gson gson1 = new Gson();
                    List<Farm> farms = gson1.fromJson(body,new TypeToken<List<Farm>>(){}.getType());
                    if (farms != null) {
                        if (!farms.isEmpty()) {
                            hasMore = true;
                            for (Farm farm : farms) {
                                farmList.add(farm);
                            }
                        } else {
                            hasMore = false;
                        }

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(true);
                try {
                    Thread.sleep(500);//主线程睡半秒是因为getData中开启了子线程加载数据，要等数据加载完才能进行UI的操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                farmAdapter.setNewData(farmList);
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }

}