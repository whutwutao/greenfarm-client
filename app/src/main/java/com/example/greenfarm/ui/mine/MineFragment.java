package com.example.greenfarm.ui.mine;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.myLayout.MineItemLayout;
import com.example.greenfarm.ui.mine.setting.SettingActivity;

public class MineFragment extends Fragment {

    private MineViewModel mViewModel;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mine_fragment, container, false);

        RelativeLayout rlMyOrder = root.findViewById(R.id.rl_my_order);
        rlMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("点击了我的订单");
            }
        });

        RelativeLayout rlMyFarm = root.findViewById(R.id.rl_my_farm);
        rlMyFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("点击了我的农场");
            }
        });

        RelativeLayout rlSetting = root.findViewById(R.id.rl_setting);
        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("点击了设置");
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

//        MineItemLayout orderInfo = root.findViewById(R.id.orderInfo);
//        MineItemLayout setting = root.findViewById(R.id.setting);
//        MineItemLayout farmInfo = root.findViewById(R.id.farmInfo);
//
//
//        orderInfo.setTextContent("我的订单");
//
//        farmInfo.setTextContent("我的农场");
//
//        setting.setTextContent("设置");
//
//        setting.setDividerTopHeight(20);
//
//        orderInfo.setOnRootClickListener(new MineItemLayout.OnRootClickListener() {
//            @Override
//            public void onRootClick(View view) {
//                Toast.makeText(getActivity(),"点击了我的订单",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        setting.setOnRootClickListener(new MineItemLayout.OnRootClickListener() {
//            @Override
//            public void onRootClick(View view) {
//                Toast.makeText(getActivity(),"点击了设置",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(requireActivity(), SettingActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        farmInfo.setOnRootClickListener(new MineItemLayout.OnRootClickListener() {
//            @Override
//            public void onRootClick(View view) {
//                Toast.makeText(getActivity(),"点击了我的农场",Toast.LENGTH_SHORT).show();
//            }
//        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        // TODO: Use the ViewModel
    }

    //简化Toast代码
    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}