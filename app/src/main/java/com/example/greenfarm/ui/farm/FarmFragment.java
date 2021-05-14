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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Condition;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.ui.adapter.GirdDropDownAdapter;
import com.example.greenfarm.ui.adapter.ListDropDownAdapter;
import com.example.greenfarm.ui.custom.CustomLoadMoreView;
import com.example.greenfarm.ui.farm.adapter.FarmAdapter;
import com.example.greenfarm.ui.farm.search.FarmSearchActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyydjk.library.DropDownMenu;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    DropDownMenu mDropDownMenu;
    private String headers[] = {"地址", "面积", "价格", "适宜种植", "租期"};
    private List<View> popupViews = new ArrayList<>();

    private GirdDropDownAdapter addressAdapter;
    private ListDropDownAdapter areaAdapter;
    private ListDropDownAdapter priceAdapter;
    private ListDropDownAdapter plantAdapter;
    private ListDropDownAdapter serviceLifeAdapter;

    private String address[] = {
            "不限", "北京市", "天津市", "河北省", "山西省",
            "内蒙古自治区", "辽宁省", "吉林省", "黑龙江省",
            "上海市", "江苏省", "浙江省", "安徽省", "福建省",
            "江西省", "山东省", "河南省", "湖北省", "湖南省",
            "广东省", "广西省", "海南省", "重庆市", "四川省",
            "贵州省", "云南省", "西藏藏族自治区", "陕西省", "甘肃省",
            "青海省", "宁夏省", "新疆维吾尔自治区"
    };
    private String area[] = {"不限", "1-5亩", "6-10亩", "10亩以上"};
    private String price[] = {"不限", "1000-5000元/年","5000-10000元/年","10000元/年以上"};
    private String plant[] = {"不限", "番茄", "黄瓜", "白菜", "土豆", "茄子"};
    private String serviceLife[] = {"不限", "1-5年", "5年以上"};

    private Condition condition = new Condition();//查询条件

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


        //为condition设置默认值
        condition.setAddress("");
        condition.setDescription("");
        condition.setAreaMin(-1);
        condition.setAreaMax(10000000);
        condition.setPriceMin(-1);
        condition.setPriceMax(100000000);
        condition.setServiceLifeMin(-1);
        condition.setServiceLifeMax(10000);
        mDropDownMenu = root.findViewById(R.id.dropDownMenu);

        onRefresh();
        initView();
        return root;
    }


    private void initView() {
        final ListView addressView = new ListView(getContext());
        addressAdapter = new GirdDropDownAdapter(getContext(), Arrays.asList(address));
        addressView.setDividerHeight(0);
        addressView.setAdapter(addressAdapter);

        final ListView areaView = new ListView(getContext());
        areaAdapter = new ListDropDownAdapter(getContext(), Arrays.asList(area));
        areaView.setDividerHeight(0);
        areaView.setAdapter(areaAdapter);

        final ListView priceView = new ListView(getContext());
        priceAdapter = new ListDropDownAdapter(getContext(),Arrays.asList(price));
        priceView.setDividerHeight(0);
        priceView.setAdapter(priceAdapter);

        final ListView plantView = new ListView(getContext());
        plantAdapter = new ListDropDownAdapter(getContext(),Arrays.asList(plant));
        plantView.setDividerHeight(0);
        plantView.setAdapter(plantAdapter);

        final ListView serviceLifeView = new ListView(getContext());
        serviceLifeAdapter = new ListDropDownAdapter(getContext(),Arrays.asList(serviceLife));
        serviceLifeView.setDividerHeight(0);
        serviceLifeView.setAdapter(serviceLifeAdapter);

        popupViews.add(addressView);
        popupViews.add(areaView);
        popupViews.add(priceView);
        popupViews.add(plantView);
        popupViews.add(serviceLifeView);

        //add item click event
        addressView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addressAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : address[position]);
                condition.setAddress(position == 0 ? "" : address[position]);
                mDropDownMenu.closeMenu();
            }
        });

        areaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : area[position]);
                switch (position) {
                    case 0: {
                        condition.setAreaMin(-1);
                        condition.setAreaMin(10000000);
                        break;
                    }
                    case 1: {
                        condition.setAreaMin(1);
                        condition.setAreaMax(5);
                        break;
                    }
                    case 2: {
                        condition.setAreaMin(6);
                        condition.setAreaMax(10);
                        break;
                    }
                    case 3: {
                        condition.setAreaMin(11);
                        condition.setAreaMax(10000000);
                        break;
                    }
                    default:
                        break;
                }
                mDropDownMenu.closeMenu();
                onRefresh();
            }
        });

        priceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                priceAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : price[position]);
                switch (position) {
                    case 0: {
                        condition.setPriceMin(-1);
                        condition.setPriceMax(10000000);
                        break;
                    }
                    case 1: {
                        condition.setPriceMin(1000);
                        condition.setPriceMax(5000);
                        break;
                    }
                    case 2: {
                        condition.setPriceMin(5001);
                        condition.setPriceMax(10000);
                        break;
                    }
                    case 3: {
                        condition.setPriceMin(10001);
                        condition.setPriceMax(10000000);
                        break;
                    }
                    default:
                        break;
                }
                mDropDownMenu.closeMenu();
                onRefresh();
            }
        });

        plantView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                plantAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[3] : plant[position]);
                condition.setDescription(position == 0 ? "" : plant[position]);
                mDropDownMenu.closeMenu();
                onRefresh();
            }
        });

        serviceLifeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                serviceLifeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[4] : serviceLife[position]);
                switch (position) {
                    case 0: {
                        condition.setServiceLifeMin(-1);
                        condition.setServiceLifeMax(10000000);
                        break;
                    }
                    case 1: {
                        condition.setServiceLifeMin(1);
                        condition.setServiceLifeMax(5);
                        break;
                    }
                    case 2: {
                        condition.setServiceLifeMin(6);
                        condition.setServiceLifeMax(10000);
                        break;
                    }
                    default:
                        break;
                }
                mDropDownMenu.closeMenu();
                onRefresh();
            }
        });

//        init context view
        TextView contentView = new TextView(getContext());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        swipeRefreshLayout = new SwipeRefreshLayout(getContext());
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        swipeRefreshLayout.addView(recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);

        farmAdapter = new FarmAdapter(this, R.layout.recyclerview_item_farm,farmList);

//        farmAdapter.setLoadMoreView(new CustomLoadMoreView());
//
//        farmAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getData(false);
//                        try {
//                            Thread.sleep(500);//主线程睡半秒是因为getData中开启了子线程加载数据，要等数据加载完才能进行UI的操作
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (hasMore) {
//                            farmAdapter.setNewData(farmList);
//                            farmAdapter.loadMoreComplete();
//                        } else {
//                            farmAdapter.loadMoreEnd();
//                        }
//                    }
//                }, 1000);
//            }
//        }, recyclerView);

        recyclerView.setAdapter(farmAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);


        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, swipeRefreshLayout);
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

//        HashMap<String, Integer> map = new HashMap<>();

        Gson gson = new Gson();
        String jsonToServer = gson.toJson(condition);
        try {
            HttpUtil.postWithOkHttp(HttpUtil.getUrl("/getFarmByMultiCondition") , jsonToServer, new okhttp3.Callback() {
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