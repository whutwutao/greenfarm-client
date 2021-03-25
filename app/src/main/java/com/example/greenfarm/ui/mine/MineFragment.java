package com.example.greenfarm.ui.mine;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.myLayout.MineLayout;

public class MineFragment extends Fragment {

    private MineViewModel mViewModel;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mine_fragment, container, false);
        MineLayout personalInfo = root.findViewById(R.id.personalInfo);
        MineLayout orderInfo = root.findViewById(R.id.orderInfo);
        MineLayout setting = root.findViewById(R.id.setting);

        personalInfo.setTextContent("个人信息");

        orderInfo.setTextContent("我的订单");

        setting.setTextContent("设置");

        personalInfo.setOnRootClickListener(new MineLayout.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {
                Toast.makeText(getActivity(),"点击了个人信息",Toast.LENGTH_SHORT).show();
            }
        });

        orderInfo.setOnRootClickListener(new MineLayout.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {
                Toast.makeText(getActivity(),"点击了我的订单",Toast.LENGTH_SHORT).show();
            }
        });

        setting.setOnRootClickListener(new MineLayout.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {
                Toast.makeText(getActivity(),"点击了设置",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        // TODO: Use the ViewModel
    }

}