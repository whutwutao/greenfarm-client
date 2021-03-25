package com.example.greenfarm.myLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.greenfarm.R;
import com.example.greenfarm.utils.DensityUtils;

public class MineLayout extends LinearLayout {

    private View dividerTop, dividerBottom;//上下分隔线
    private LinearLayout llRoot;
    private TextView tvTextContent;
    private ImageView ivRightIcon;

    public MineLayout(Context context) {
        super(context);
    }

    public MineLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MineLayout init() {
        LayoutInflater.from(getContext()).inflate(R.layout.mine_item, this, true);
        llRoot = findViewById(R.id.ll_root);
        dividerTop = findViewById(R.id.divider_top);
        dividerBottom = findViewById(R.id.divider_bottom);
        tvTextContent = findViewById(R.id.tv_text_content);
        ivRightIcon = findViewById(R.id.iv_right_icon);
        return this;
    }

    /**
     * 设置上下分隔线的显示和隐藏情况
     * @param showDividerTop
     * @param showDividerBottom
     * @return
     */
    public MineLayout showDivider(Boolean showDividerTop, Boolean showDividerBottom) {
        if (showDividerTop) {
            dividerTop.setVisibility(VISIBLE);
        } else {
            dividerTop.setVisibility(GONE);
        }
        if (showDividerBottom) {
            dividerBottom.setVisibility(VISIBLE);
        } else {
            dividerBottom.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 设置上分割线的颜色
     *
     * @return
     */
    public MineLayout setDividerTopColor(int dividerTopColorRes) {
        dividerTop.setBackgroundColor(getResources().getColor(dividerTopColorRes));
        return this;
    }

    /**
     * 设置上分割线的高度
     *
     * @return
     */
    public MineLayout setDividerTopHeight(int dividerTopHeightDp) {
        ViewGroup.LayoutParams layoutParams = dividerTop.getLayoutParams();
        layoutParams.height = DensityUtils.dp2px(getContext(), dividerTopHeightDp);
        dividerTop.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 设置root的paddingTop 与 PaddingBottom 从而控制整体的行高
     * paddingLeft 与 paddingRight 保持默认 20dp
     */
    public MineLayout setRootPaddingTopBottom(int paddintTop, int paddintBottom) {
        llRoot.setPadding(DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), paddintTop),
                DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), paddintBottom));
        return this;
    }

    /**
     * 设置右边Icon 的宽高
     */
    public MineLayout setLeftIconSize(int widthDp, int heightDp) {
        ViewGroup.LayoutParams layoutParams = ivRightIcon.getLayoutParams();
        layoutParams.width = DensityUtils.dp2px(getContext(), widthDp);
        layoutParams.height = DensityUtils.dp2px(getContext(), heightDp);
        ivRightIcon.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 设置中间的文字内容
     *
     * @param textContent
     * @return
     */
    public MineLayout setTextContent(String textContent) {
        tvTextContent.setText(textContent);
        return this;
    }

    /**
     * 设置中间的文字颜色
     *
     * @return
     */
    public MineLayout setTextContentColor(int colorRes) {
        tvTextContent.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置中间的文字大小
     *
     * @return
     */
    public MineLayout setTextContentSize(int textSizeSp) {
        tvTextContent.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public MineLayout setOnRootClickListener(final OnRootClickListener onRootClickListener) {
        llRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRootClickListener.onRootClick(llRoot);
            }
        });
        return this;
    }

}
