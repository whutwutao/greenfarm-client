package com.example.greenfarm.ui.mine;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.ui.mine.myFarm.customer.MyFarm1Activity;
import com.example.greenfarm.ui.mine.myFarm.farmer.MyFarmActivity;
import com.example.greenfarm.ui.mine.setting.SettingActivity;

public class MineFragment extends Fragment {

    private MineViewModel mViewModel;

    private TextView tvUsername;

    private TextView tvRole;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mine_fragment, container, false);

        tvUsername = root.findViewById(R.id.tv_mine_username);
        tvUsername.setText(UserManager.currentUser.getUsername());

        tvRole = root.findViewById(R.id.tv_mine_role);
        if (UserManager.currentUser.getIsFarmer() == 0) {
            tvRole.setText("会员");
        } else {
            tvRole.setText("农场主");
        }

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
//                showToast("点击了我的农场");
                Intent intent;
                if (UserManager.currentUser.getIsFarmer() == 0) {
                    intent = new Intent(getActivity(), MyFarm1Activity.class);
                } else {
                    intent = new Intent(getActivity(), MyFarmActivity.class);
                }
                startActivity(intent);

            }
        });

        RelativeLayout rlSetting = root.findViewById(R.id.rl_setting);
        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showToast("点击了设置");
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
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

    @Override
    public void onResume() {
        super.onResume();
        tvUsername.setText(UserManager.currentUser.getUsername());
    }
}