package site.hanschen.runwithyou.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.common.utils.ResourceUtils;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;

/**
 * @author HansChen
 */
public class RegisterActivity extends RunnerBaseActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordRepeat;
    private Button   mRegisterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarCompat.setColor(RegisterActivity.this, ResourceUtils.getColor(mContext, R.color.background_login), 0);

        initViews();
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

        mUsername = findView(R.id.activity_register_username);
        mUsername.addTextChangedListener(mTextWatcher);
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
            String username = mUsername.getEditableText().toString();
            String password = mPassword.getEditableText().toString();
            String passwordRepeat = mPasswordRepeat.getEditableText().toString();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || !password.equals(passwordRepeat)) {
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
                    doRegister(mUsername.getEditableText().toString(), mPassword.getEditableText().toString());
                    break;
                default:
                    break;
            }
        }
    };

    private void doRegister(String username, String password) {

    }
}
