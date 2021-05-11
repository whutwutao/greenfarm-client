package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Talk;
import com.example.greenfarm.pojo.User;
import com.example.greenfarm.ui.chat.ChatActivity;
import com.google.gson.Gson;

import java.util.List;

public class TalkAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    private Context mContext;

    public TalkAdapter(@Nullable List<User> data, Context mContext) {
        super(R.layout.item_talk,data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        TextView tvReceiverName = helper.getView(R.id.tv_receiver_name);
        tvReceiverName.setText(item.getUsername());
        LinearLayout llTalk = helper.getView(R.id.ll_talk);
        llTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("friend",new Gson().toJson(item));
                mContext.startActivity(intent);
            }
        });
    }
}
