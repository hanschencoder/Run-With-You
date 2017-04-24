package site.hanschen.runwithyou.ui.home.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseFragment;
import site.hanschen.runwithyou.ui.home.userinfo.UserInfoActivity;

/**
 * @author HansChen
 */
public class SettingFragment extends RunnerBaseFragment {

    @BindView(R.id.fragment_setting_user_layout)
    FrameLayout mUserLayout;
    @BindView(R.id.fragment_setting_username)
    TextView    mUsername;
    @BindView(R.id.fragment_setting_user_icon)
    ImageView   mUserIcon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(SettingFragment.this, view);
    }

    @OnClick(R.id.fragment_setting_user_layout)
    void onUserLayoutClick() {
        startActivity(new Intent(mContext, UserInfoActivity.class));
    }
}
