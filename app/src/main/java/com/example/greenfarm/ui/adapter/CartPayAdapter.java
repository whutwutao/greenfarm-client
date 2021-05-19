package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Cart;
import com.example.greenfarm.pojo.CartAdapterItem;
import com.example.greenfarm.pojo.Product;
import com.example.greenfarm.utils.HttpUtil;

import java.util.List;

public class CartPayAdapter extends BaseQuickAdapter<CartAdapterItem, BaseViewHolder> {

    private Context mContext;

    public CartPayAdapter(@Nullable List<CartAdapterItem> data, Context mContext) {
        super(R.layout.cart_item, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, CartAdapterItem item) {
        Cart cart = item.getCart();
        Product product = item.getProduct();
        String description = product.getName() + "(" + product.getDescription() + ")";
        helper.setText(R.id.tv_cart_description,description);
        helper.setText(R.id.tv_cart_single_price,"￥"+product.getPrice());
        helper.setText(R.id.tv_cart_amount,cart.getAmount()+"份");

        ImageView ivSelect = helper.getView(R.id.iv_cart_select);
        ivSelect.setVisibility(View.GONE);

        ImageView ivPicture = helper.getView(R.id.iv_cart_picture);
        Glide.with(mContext).load(HttpUtil.getUrl(product.getPictureUrl())).into(ivPicture);

    }
}
