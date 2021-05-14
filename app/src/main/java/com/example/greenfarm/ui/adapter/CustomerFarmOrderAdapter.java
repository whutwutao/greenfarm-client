package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.FarmOrderBean;
import com.example.greenfarm.utils.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerFarmOrderAdapter extends BaseQuickAdapter<FarmOrderBean, BaseViewHolder> {
    private Context mContext;

    public CustomerFarmOrderAdapter(@Nullable List<FarmOrderBean> data, Context mContext) {
        super(R.layout.item_farm_order,data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FarmOrderBean item) {
        ImageView imageView = helper.getView(R.id.iv_farm_order_select);
        imageView.setVisibility(View.GONE);
        ImageView farmPic = helper.getView(R.id.iv_farm_order_farm);
        String pictureUrl = item.getFarm().getPicturePath();
        Log.d("CustomerFarmOrder",HttpUtil.getUrl(pictureUrl));
        if (pictureUrl != null) {
            Glide.with(mContext).load(HttpUtil.getUrl(pictureUrl)).override(120,90).into(farmPic);
        } else {
            Glide.with(mContext).load(R.mipmap.farm_default).override(120,90).into(farmPic);
        }
        TextView tvUserName = helper.getView(R.id.tv_farm_order_customer_name);
        tvUserName.setVisibility(View.GONE);
        TextView tvTelephone = helper.getView(R.id.tv_farm_order_customer_telephone);
        tvTelephone.setVisibility(View.GONE);

        helper.setText(R.id.tv_farm_order_farm_address,item.getFarm().getAddress());
        Date date = item.getOrder().getUpdateTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
        String dateText = f.format(date);
        helper.setText(R.id.tv_farm_order_time,"下单时间："+dateText);
    }
}
