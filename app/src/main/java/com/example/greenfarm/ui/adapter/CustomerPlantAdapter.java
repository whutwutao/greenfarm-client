package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Plant;

import java.util.List;

public class CustomerPlantAdapter extends BaseQuickAdapter<Plant, BaseViewHolder> {
    Context mContext;

    public CustomerPlantAdapter(int layoutResId, @Nullable List<Plant> data, Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, Plant item) {
        helper.setText(R.id.tv_plant_product_name,item.getName());
        helper.setText(R.id.tv_plant_product_description,item.getDescription());
        helper.setText(R.id.tv_plant_product_amount,item.getAmount());
        String strStatus = null;
        int status = item.getStatus();
        switch (status) {
            case 0:
                strStatus = "种植状态：未种植";
                break;
            case 1:
                strStatus = "种植状态：已种植";
                break;
            case 2:
                strStatus = "种植状态：已种好";
                break;
            default:
                break;
        }
        helper.setText(R.id.tv_plant_product_status,strStatus);

        LinearLayout llOfItem = helper.getView(R.id.ll_recyclerview_item_plant_product);
        llOfItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }
}
