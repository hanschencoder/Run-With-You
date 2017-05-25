package site.hanschen.runwithyou.ui.home.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import site.hanschen.api.user.ResultReply;
import site.hanschen.api.user.Sex;
import site.hanschen.api.user.UserCenterApiWrapper;
import site.hanschen.api.user.UserInfo;
import site.hanschen.api.user.UserInfoReply;
import site.hanschen.runwithyou.Const;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.AuthManager;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.ui.login.LoginActivity;
import site.hanschen.runwithyou.utils.PreferencesUtils;

/**
 * @author HansChen
 */
public class UserInfoActivity extends RunnerBaseActivity {

    @BindView(R.id.user_info_nickname)
    EditText             mNickname;
    @BindView(R.id.user_info_phone)
    EditText             mPhone;
    @BindView(R.id.user_info_birthday)
    EditText             mBirthday;
    @BindView(R.id.user_info_sex)
    RadioGroup           mSexRadio;
    @BindView(R.id.user_info_sex_male)
    RadioButton          mMaleBtn;
    @BindView(R.id.user_info_sex_female)
    RadioButton          mFemaleBtn;
    @BindView(R.id.user_info_bio)
    EditText             mBio;
    @Inject
    UserCenterApiWrapper mUserCenterApi;

    private UserInfo mUserInfo;
    private Menu     mMenu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        DaggerUserInfoComponent.builder()
                               .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                               .userInfoModule(new UserInfoModule())
                               .build()
                               .inject(this);
        initViews();
        requestUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        mMenu = menu;
        mMenu.findItem(R.id.user_info_save).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_info_save:
                updateUserInfo();
                break;
            default:
                break;
        }
        return true;
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_user_info_toolbar);
        toolbar.setTitle("个人资料");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void requestUserInfo() {
        mUserCenterApi.requestUserInfo(AuthManager.getInstance().getToken()).subscribe(new Observer<UserInfoReply>() {
            @Override
            public void onSubscribe(Disposable d) {
                showWaitingDialog();
            }

            @Override
            public void onNext(UserInfoReply reply) {
                dismissWaitingDialog();
                if (reply.getSucceed()) {
                    mUserInfo = reply.getUserInfo();
                    updateUI(mUserInfo);
                } else {
                    toast("网络有问题，请重试");
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissWaitingDialog();
                toast("网络有问题，请重试");
                finish();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void updateUI(UserInfo userInfo) {
        mNickname.setText(userInfo.getNickname());
        mPhone.setText(userInfo.getPhone());
        mBirthday.setText(userInfo.getBirthday());
        if (userInfo.getSex() == Sex.MALE) {
            mMaleBtn.setChecked(true);
        } else {
            mFemaleBtn.setChecked(true);
        }
        mBio.setText(userInfo.getBio());
    }

    @OnClick(R.id.user_info_birthday)
    void onBirthdayClick() {
        int year = 1990;
        int monthOfYear = 1;
        int dayOfMonth = 1;
        if (!TextUtils.isEmpty(mBirthday.getEditableText().toString())) {
            String[] date = mBirthday.getEditableText().toString().split("-");
            year = Integer.valueOf(date[0]);
            monthOfYear = Integer.valueOf(date[1]) - 1;
            dayOfMonth = Integer.valueOf(date[2]);
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                mBirthday.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                refreshSaveBtn();
            }
        }, year, monthOfYear, dayOfMonth);
        dpd.setTitle("选择出生日期");
        dpd.show(getFragmentManager(), "DatePickerDialog");
    }

    private void logout() {
        PreferencesUtils.remove(this, Const.KEY_PASSWORD);
        AuthManager.getInstance().logout();
        RunnerApplication.getInstance().exit();
        startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.user_info_logout)
    void onLogoutClick() {
        new MaterialDialog.Builder(mContext).title("退出登录")
                                            .content("是否退出登录 ？")
                                            .positiveText("退出")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog,
                                                                    @NonNull DialogAction which) {
                                                    logout();
                                                }
                                            })
                                            .negativeText("取消")
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog,
                                                                    @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .build()
                                            .show();
    }

    private void refreshSaveBtn() {

        if (mMenu == null) {
            return;
        }

        if (!mUserInfo.getNickname().equals(mNickname.getEditableText().toString())
            || !mUserInfo.getPhone()
                         .equals(mPhone.getEditableText()
                                       .toString())
            || !mUserInfo.getBio().equals(mBio.getEditableText().toString())
            || !mUserInfo.getBirthday().equals(mBirthday.getEditableText().toString())
            || isSexChanged()) {
            mMenu.findItem(R.id.user_info_save).setVisible(true);
        } else {
            mMenu.findItem(R.id.user_info_save).setVisible(false);
        }
    }

    private boolean isSexChanged() {
        return (mUserInfo.getSex() == Sex.MALE && mFemaleBtn.isChecked()) || (mUserInfo.getSex() == Sex.FEMALE
                                                                              && mMaleBtn.isChecked());
    }

    @OnCheckedChanged({R.id.user_info_sex_male, R.id.user_info_sex_female})
    void onCheckedChanged() {
        refreshSaveBtn();
    }

    @OnTextChanged({R.id.user_info_nickname, R.id.user_info_phone, R.id.user_info_bio})
    void onTextChanged() {
        refreshSaveBtn();
    }

    private void updateUserInfo() {
        UserInfo.Builder builder = UserInfo.newBuilder();
        builder.setNickname(mNickname.getEditableText().toString());
        builder.setPhone(mPhone.getEditableText().toString());
        builder.setBirthday(mBirthday.getEditableText().toString());
        builder.setSex(mMaleBtn.isChecked() ? Sex.MALE : Sex.FEMALE);
        builder.setBio(mBio.getEditableText().toString());

        final UserInfo userInfo = builder.build();
        mUserCenterApi.updateUserInfo(AuthManager.getInstance().getToken(), userInfo).subscribe(new Observer<ResultReply>() {
            @Override
            public void onSubscribe(Disposable d) {
                showWaitingDialog();
            }

            @Override
            public void onNext(ResultReply resultReply) {
                dismissWaitingDialog();
                if (resultReply.getSecceed()) {
                    toast("资料更新成功");
                    mUserInfo = userInfo;
                    updateUI(mUserInfo);
                } else {
                    toast("资料更新失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissWaitingDialog();
                toast("网络异常");
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
