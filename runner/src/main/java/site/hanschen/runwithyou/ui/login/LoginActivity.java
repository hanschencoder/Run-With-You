package site.hanschen.runwithyou.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import site.hanschen.api.user.LoginReply;
import site.hanschen.api.user.UserCenterApiWrapper;
import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.common.utils.ResourceUtils;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.AuthManager;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.ui.home.HomeActivity;

/**
 * @author HansChen
 */
public class LoginActivity extends RunnerBaseActivity {

    @BindView(R.id.activity_login_username)
    EditText mUsername;
    @BindView(R.id.activity_login_password)
    EditText mPassword;
    @BindView(R.id.activity_login_login_btn)
    Button   mLoginBtn;

    @Inject
    UserCenterApiWrapper mUserCenterApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarCompat.setColor(LoginActivity.this, ResourceUtils.getColor(mContext, R.color.colorPrimary), 0);
        DaggerLoginComponent.builder()
                            .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                            .loginModule(new LoginModule())
                            .build()
                            .inject(LoginActivity.this);
        setLoginBtnState();
    }

    private void setLoginBtnState() {
        String username = mUsername.getEditableText().toString();
        String password = mPassword.getEditableText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            mLoginBtn.setEnabled(false);
        } else {
            mLoginBtn.setEnabled(true);
        }
    }

    @OnTextChanged({R.id.activity_login_username, R.id.activity_login_password})
    void onTextChanged() {
        setLoginBtnState();
    }

    @OnClick({R.id.activity_login_login_btn, R.id.activity_login_forget_password, R.id.activity_login_register})
    void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_login_btn:
                doLogin(mUsername.getEditableText().toString(), mPassword.getEditableText().toString());
                break;
            case R.id.activity_login_forget_password:
                toast("开发中，敬请期待");
                break;
            case R.id.activity_login_register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            default:
                break;
        }
    }

    private void clearErrorInfo() {
        mUsername.setError(null);
        mPassword.setError(null);
    }

    private void doLogin(final String username, final String password) {
        mUserCenterApi.login(username, password).subscribe(new Observer<LoginReply>() {
            @Override
            public void onSubscribe(Disposable d) {
                clearErrorInfo();
                showWaitingDialog();
            }

            @Override
            public void onNext(LoginReply loginReply) {
                dismissWaitingDialog();
                if (loginReply.getSucceed()) {
                    AuthManager.getInstance().setAuth(username, loginReply.getToken());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    switch (loginReply.getErrCode()) {
                        case ACCOUNT_PASSWORD_INCORRECT:
                            mPassword.setError("帐号或密码不正确");
                            break;
                        case ACCOUNT_EMPTY:
                            mUsername.setError("帐号不能为空");
                            break;
                        case PASSWORD_EMPTY:
                            mPassword.setError("密码不能为空");
                            break;
                        default:
                            toast("登录失败:" + loginReply.getErrCode().toString());
                            break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissWaitingDialog();
                toast("网络错误:" + e.toString());
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
