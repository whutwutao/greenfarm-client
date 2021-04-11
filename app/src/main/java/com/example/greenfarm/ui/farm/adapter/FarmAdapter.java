package com.example.greenfarm.ui.farm.adapter;

import android.service.autofill.UserData;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.pojo.Farm;

import java.util.List;

public class FarmAdapter extends BaseQuickAdapter<Farm, BaseViewHolder> {
    public FarmAdapter(int layoutResId, @Nullable List<Farm> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Farm item) {

    }
}
