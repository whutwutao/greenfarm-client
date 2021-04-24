package com.example.greenfarm.ui.mine.myFarm.farmer.management.monitor.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.farm.farminfo.FarmInfoActivity;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.monitor.MonitorActivity;
import com.google.gson.Gson;

import java.util.List;

public class FarmMonitorAdapter extends BaseQuickAdapter<Farm, BaseViewHolder> {
    private Context mContext;
    public FarmMonitorAdapter(Context context, int layoutResId, @Nullable List<Farm> data) {
        super(layoutResId, data);
        mContext = context;
        Log.d("FarmMonitorAdapter",data.toString());
    }

    @Override
    protected void convert(BaseViewHolder helper, Farm item) {
        Log.d("FarmMonitorAdapter","convert");
        String pictureUrl = item.getPicturePath();
        ImageView imageView = helper.getView(R.id.iv_farm_item);

        if (pictureUrl != null) {
            Glide.with(mContext).load(pictureUrl).override(120,90).into(imageView);
        } else {
            Glide.with(mContext).load(R.mipmap.farm_default).override(120,90).into(imageView);
        }


        helper.setText(R.id.tv_farm_description, item.getDescription());
        helper.setText(R.id.tv_farm_address, item.getAddress());
        helper.setText(R.id.tv_farm_area, Double.toString(item.getArea()) + "亩");
        helper.setText(R.id.tv_farm_price, Double.toString(item.getPrice()) + "万");
        helper.setText(R.id.tv_farm_service_life, Integer.toString(item.getServiceLife()) + "年");

        LinearLayout llOfItem = helper.getView(R.id.rl_recyclerview_item_farm);
        llOfItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MonitorActivity.class);
                Gson gson = new Gson();
                item.setUpdateTime(null);//防止因为Date类型无法转换而出错
                String strFarm = gson.toJson(item);
                intent.putExtra("farm",strFarm);
                mContext.startActivity(intent);
            }
        });
    }
}
