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

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseFragment;
import site.hanschen.runwithyou.ui.home.userinfo.UserInfoActivity;

/**
 * @author HansChen
 */
public class SettingFragment extends RunnerBaseFragment {

    private FrameLayout mUserLayout;
    private TextView    mUsername;
    private ImageView   mUserIcon;

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
        mUserLayout = (FrameLayout) view.findViewById(R.id.fragment_setting_user_layout);
        mUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, UserInfoActivity.class));
            }
        });
        mUsername = (TextView) view.findViewById(R.id.fragment_setting_username);
        mUserIcon = (ImageView) view.findViewById(R.id.fragment_setting_user_icon);
    }
}
