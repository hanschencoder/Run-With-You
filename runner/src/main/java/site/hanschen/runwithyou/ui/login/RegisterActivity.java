package site.hanschen.runwithyou.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import site.hanschen.api.user.RegisterReply;
import site.hanschen.api.user.UserCenterApi;
import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.common.utils.ResourceUtils;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseActivity;

/**
 * @author HansChen
 */
public class RegisterActivity extends RunnerBaseActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordRepeat;
    private Button   mRegisterBtn;

    @Inject
    UserCenterApi mUserCenterApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarCompat.setColor(RegisterActivity.this, ResourceUtils.getColor(mContext, R.color.background_login), 0);
        initViews();

        DaggerRegisterComponent.builder()
                               .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                               .registerModule(new RegisterModule())
                               .build()
                               .inject(RegisterActivity.this);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        toolbar.setTitle("帐号注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEmail = findView(R.id.activity_register_email);
        mEmail.addTextChangedListener(mTextWatcher);
        mPassword = findView(R.id.activity_register_password);
        mPassword.addTextChangedListener(mTextWatcher);
        mPasswordRepeat = findView(R.id.activity_register_password_repeat);
        mPasswordRepeat.addTextChangedListener(mTextWatcher);
        mRegisterBtn = findView(R.id.activity_register_btn);
        mRegisterBtn.setEnabled(false);
        mRegisterBtn.setOnClickListener(onBtnClick);
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
            String email = mEmail.getEditableText().toString();
            String password = mPassword.getEditableText().toString();
            String passwordRepeat = mPasswordRepeat.getEditableText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || !password.equals(passwordRepeat)) {
                mRegisterBtn.setEnabled(false);
            } else {
                mRegisterBtn.setEnabled(true);
            }
        }
    };

    private View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_register_btn:
                    doRegister(mEmail.getEditableText().toString(), mPassword.getEditableText().toString());
                    break;
                default:
                    break;
            }
        }
    };

    private void doRegister(final String email, final String password) {
        Observable.create(new ObservableOnSubscribe<RegisterReply>() {
            @Override
            public void subscribe(ObservableEmitter<RegisterReply> e) throws Exception {
                RegisterReply reply = mUserCenterApi.register(email, password);
                e.onNext(reply);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<RegisterReply>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RegisterReply reply) {
                if (reply.getSucceed()) {
                    new MaterialDialog.Builder(mContext).title("注册成功").content("注册成功，欢迎您使用").build().show();
                } else {
                    new MaterialDialog.Builder(mContext).title("注册失败").content(reply.getErrCode().toString()).build().show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext.getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
