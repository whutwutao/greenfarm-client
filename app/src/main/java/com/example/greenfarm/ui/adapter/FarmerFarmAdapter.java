package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.mine.myFarm.customer.management.CustomerFarmManagementActivity;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.FarmManagementActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;

import java.util.List;

public class FarmerFarmAdapter extends BaseQuickAdapter<Farm, BaseViewHolder> {
    private Context mContext;

    public FarmerFarmAdapter(int layoutResId, @Nullable List<Farm> data, Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, Farm item) {
        String pictureUrl = item.getPicturePath();
        ImageView imageView = helper.getView(R.id.iv_farm_item);

        if (pictureUrl != null) {
            Glide.with(mContext).load(HttpUtil.getUrl(pictureUrl)).override(120,90).into(imageView);
        } else {
            Glide.with(mContext).load(R.mipmap.farm_default).override(120,90).into(imageView);
        }


        helper.setText(R.id.tv_farm_description, item.getDescription());
        helper.setText(R.id.tv_farm_address, item.getAddress());
        helper.setText(R.id.tv_farm_area, Double.toString(item.getArea()) + "亩");
        helper.setText(R.id.tv_farm_price, Double.toString(item.getPrice()) + "元");
        helper.setText(R.id.tv_farm_service_life, Integer.toString(item.getServiceLife()) + "年");

        LinearLayout llOfItem = helper.getView(R.id.rl_recyclerview_item_farm);
        llOfItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FarmManagementActivity.class);
                intent.putExtra("farm",new Gson().toJson(item));
                mContext.startActivity(intent);
            }
        });
    }
}
