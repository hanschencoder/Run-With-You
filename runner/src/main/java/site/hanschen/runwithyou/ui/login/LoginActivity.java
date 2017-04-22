package site.hanschen.runwithyou.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import site.hanschen.api.user.LoginReply;
import site.hanschen.api.user.UserCenterApi;
import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.common.utils.ResourceUtils;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.ui.home.HomeActivity;

/**
 * @author HansChen
 */
public class LoginActivity extends RunnerBaseActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button   mLoginBtn;
    private TextView mForgetPasswordBtn;
    private TextView mRegisterBtn;

    @Inject
    UserCenterApi mUserCenterApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarCompat.setColor(LoginActivity.this, ResourceUtils.getColor(mContext, R.color.colorPrimary), 0);
        DaggerLoginComponent.builder()
                            .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                            .loginModule(new LoginModule())
                            .build()
                            .inject(LoginActivity.this);
        initViews();
    }

    private void initViews() {
        mUsername = findView(R.id.activity_login_username);
        mPassword = findView(R.id.activity_login_password);
        mLoginBtn = findView(R.id.activity_login_login_btn);
        mForgetPasswordBtn = findView(R.id.activity_login_forget_password);
        mRegisterBtn = findView(R.id.activity_login_register);

        mUsername.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);
        mLoginBtn.setOnClickListener(onBtnClick);
        mForgetPasswordBtn.setOnClickListener(onBtnClick);
        mRegisterBtn.setOnClickListener(onBtnClick);

        setLoginBtnState();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setLoginBtnState();
        }
    };

    private void setLoginBtnState() {
        String username = mUsername.getEditableText().toString();
        String password = mPassword.getEditableText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            mLoginBtn.setEnabled(false);
        } else {
            mLoginBtn.setEnabled(true);
        }
    }

    private View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_login_login_btn:
                    doLogin(mUsername.getEditableText().toString(), mPassword.getEditableText().toString());
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

    private void doLogin(final String username, final String password) {
        Observable.create(new ObservableOnSubscribe<LoginReply>() {
            @Override
            public void subscribe(ObservableEmitter<LoginReply> e) throws Exception {
                LoginReply reply = mUserCenterApi.login(username, password);
                e.onNext(reply);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LoginReply>() {
            @Override
            public void onSubscribe(Disposable d) {
                showWaitingDialog();
            }

            @Override
            public void onNext(LoginReply loginReply) {
                if (loginReply.getSucceed()) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(mContext.getApplicationContext(), loginReply.getErrCode().toString(), Toast.LENGTH_SHORT)
                         .show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext.getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                dismissWaitingDialog();
            }
        });
    }
}
