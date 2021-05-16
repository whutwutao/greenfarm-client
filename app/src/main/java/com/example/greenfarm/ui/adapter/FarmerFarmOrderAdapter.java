package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
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

public class FarmerFarmOrderAdapter extends BaseQuickAdapter<FarmOrderBean, BaseViewHolder> {
    private Context mContext;
    boolean flag = true;
    public FarmerFarmOrderAdapter(@Nullable List<FarmOrderBean> data, Context mContext) {
        super(R.layout.item_farm_order, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FarmOrderBean item) {
        ImageView imageView = helper.getView(R.id.iv_farm_order_select);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    imageView.setImageResource(R.mipmap.icon_select);
                    item.setSelected(true);
                } else {
                    imageView.setImageResource(R.mipmap.icon_un_select);
                    item.setSelected(false);
                }
                flag = !flag;
            }
        });
        ImageView farmPic = helper.getView(R.id.iv_farm_order_farm);
        String pictureUrl = item.getFarm().getPicturePath();
        if (pictureUrl != null) {
            Glide.with(mContext).load(HttpUtil.getUrl(pictureUrl)).override(120,90).into(farmPic);
        } else {
            Glide.with(mContext).load(R.mipmap.farm_default).override(120,90).into(farmPic);
        }
        helper.setText(R.id.tv_farm_order_customer_name,"客户用户名："+item.getCustomer().getUsername());
        helper.setText(R.id.tv_farm_order_farm_address,"农场地址："+item.getFarm().getAddress());
        helper.setText(R.id.tv_farm_order_customer_telephone,"客户手机号："+item.getCustomer().getTelephone());
        helper.setText(R.id.tv_farm_order_address,"收货地址："+item.getOrder().getAddress());
        Date date = item.getOrder().getUpdateTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
        String dateText = f.format(date);
        helper.setText(R.id.tv_farm_order_time,"下单时间："+dateText);

    }
}
