package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Product;
import com.example.greenfarm.utils.HttpUtil;

import java.util.List;

public class ProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    private Context mContext;

    public ProductAdapter(@Nullable List<Product> data, Context mContext) {
        super(R.layout.product_item,data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        String pictureUrl = item.getPictureUrl();
        ImageView imageView = helper.getView(R.id.iv_product_item);
        if (pictureUrl != null) {
            Glide.with(mContext).load(HttpUtil.getUrl(pictureUrl)).into(imageView);
        }
        helper.setText(R.id.tv_product_name,item.getName());
        helper.setText(R.id.tv_product_description,item.getDescription());
        helper.setText(R.id.tv_product_price,"￥"+ item.getPrice());
    }
}
