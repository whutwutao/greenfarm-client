package com.example.greenfarm.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenfarm.R;
import com.example.greenfarm.pojo.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> mCategoryList;

    private Context mContext;

    private int mSelectedPos = -1;//实现单选  方法二，变量保存当前选中的position

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCName;

        public ViewHolder(View view) {
            super(view);
            tvCName = view.findViewById(R.id.tv_category_name);
        }
    }

    public CategoryAdapter(List<Category> mCategoryList, Context context) {
        this.mCategoryList = mCategoryList;
        this.mContext = context;
        System.out.println("mCategoryList"+mCategoryList.size());
        System.out.println("first"+mCategoryList.get(0).isSelected());
        for (int i = 0; i < mCategoryList.size(); i++) {
            if (mCategoryList.get(i).isSelected()) {
                mSelectedPos = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = mCategoryList.get(position);
        holder.tvCName.setText(category.getCname());
        if (category.isSelected()) {
            holder.tvCName.setTextColor(0xFF6200EE);
        } else {
            holder.tvCName.setTextColor(0xFF000000);
        }
        holder.tvCName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedPos != position) {
                    if (mSelectedPos != -1) {
                        mCategoryList.get(mSelectedPos).setSelected(false);
                        notifyItemChanged(mSelectedPos);
                    }
                    mSelectedPos = position;
                    mCategoryList.get(mSelectedPos).setSelected(true);
                    notifyItemChanged(mSelectedPos);
                } else {
                    mCategoryList.get(position).setSelected(false);
                    notifyItemChanged(position);
                    mSelectedPos = -1;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }


}

