package site.hanschen.runwithyou.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.common.utils.ResourceUtils;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;

/**
 * @author HansChen
 */
public class LoginActivity extends RunnerBaseActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button   mLoginBtn;
    private TextView mForgetPasswordBtn;
    private TextView mRegisterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarCompat.setColor(LoginActivity.this, ResourceUtils.getColor(mContext, R.color.background_login), 0);
        initViews();

    }

    private void initViews() {
        mUsername = findView(R.id.activity_login_username);
        mPassword = findView(R.id.activity_login_password);
        mLoginBtn = findView(R.id.activity_login_login_btn);
        mForgetPasswordBtn = findView(R.id.activity_login_forget_password);
        mRegisterBtn = findView(R.id.activity_login_register);

        mLoginBtn.setOnClickListener(onBtnClick);
        mForgetPasswordBtn.setOnClickListener(onBtnClick);
        mRegisterBtn.setOnClickListener(onBtnClick);
    }

    private View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_login_login_btn:
                    break;
                case R.id.activity_login_forget_password:
                    break;
                case R.id.activity_login_register:
                    startActivity(new Intent(mContext, RegisterActivity.class));
                    break;
                default:
                    break;
            }
        }
    };
}
