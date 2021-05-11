package com.example.greenfarm.ui.farm.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Farm;
import com.example.greenfarm.ui.farm.farmInfo.FarmInfoActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.google.gson.Gson;

import java.util.List;

public class FarmAdapter extends BaseQuickAdapter<Farm, BaseViewHolder> {
    Fragment mFragment;
    public FarmAdapter(Fragment fragment, int layoutResId, @Nullable List<Farm> data) {
        super(layoutResId, data);
        mFragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, Farm item) {
        String pictureUrl = item.getPicturePath();
        ImageView imageView = helper.getView(R.id.iv_farm_item);

        if (pictureUrl != null) {
            Glide.with(mFragment).load(HttpUtil.getUrl(pictureUrl)).override(120,90).into(imageView);
        } else {
            Glide.with(mFragment).load(R.mipmap.farm_default).override(120,90).into(imageView);
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
                Intent intent = new Intent(mFragment.getActivity(), FarmInfoActivity.class);
                Gson gson = new Gson();
                item.setUpdateTime(null);//防止因为Date类型无法转换而出错
                String strFarm = gson.toJson(item);
                intent.putExtra("farm",strFarm);
                mFragment.getActivity().startActivity(intent);
            }
        });
    }
}
