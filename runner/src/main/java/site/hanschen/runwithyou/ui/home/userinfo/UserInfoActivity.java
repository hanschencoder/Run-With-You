package site.hanschen.runwithyou.ui.home.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;

/**
 * @author HansChen
 */
public class UserInfoActivity extends RunnerBaseActivity {

    @BindView(R.id.user_info_birthday)
    EditText mBirthday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initViews();
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
            }
        }, year, monthOfYear, dayOfMonth);
        dpd.setTitle("选择出生日期");
        dpd.show(getFragmentManager(), "DatePickerDialog");
    }
}
