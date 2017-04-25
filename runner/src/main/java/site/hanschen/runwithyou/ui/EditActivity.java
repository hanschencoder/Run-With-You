package site.hanschen.runwithyou.ui;

import android.app.Activity;
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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;

/**
 * @author HansChen
 */
public class EditActivity extends RunnerBaseActivity {

    private static final String KEY_TITLE   = "KEY_TITLE";
    private static final String KEY_CONTENT = "KEY_CONTENT";
    private static final String KEY_HINT    = "KEY_HINT";
    public static final  String KEY_RESULT  = "KEY_RESULT";

    @BindView(R.id.edit_toolbar)
    Toolbar  mToolbar;
    @BindView(R.id.edit_input)
    EditText mInputText;
    @BindView(R.id.edit_hint)
    TextView mHintText;

    private Menu mMenu;

    private String mTitle;
    private String mContent;
    private String mHint;

    public static void edit(@NonNull Activity activity,
                            @NonNull String title,
                            @NonNull String content,
                            @NonNull String hint,
                            int requestCode) {
        Intent i = new Intent(activity, EditActivity.class);
        i.putExtra(KEY_TITLE, title);
        i.putExtra(KEY_CONTENT, content);
        i.putExtra(KEY_HINT, hint);
        activity.startActivityForResult(i, requestCode);
    }

    private void parseData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null
            || (mTitle = bundle.getString(KEY_TITLE)) == null
            || (mContent = bundle.getString(KEY_CONTENT)) == null
            || (mHint = bundle.getString(KEY_HINT)) == null) {
            throw new IllegalArgumentException("");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        parseData();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        mMenu = menu;
        mMenu.findItem(R.id.edit_save).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_save:
                Intent intent = new Intent();
                intent.putExtra(KEY_RESULT, mInputText.getEditableText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void initViews() {
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                onBackPressed();
            }
        });
        mHintText.setText(mHint);
        mInputText.setText(mContent);
    }

    @OnTextChanged(R.id.edit_input)
    void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mMenu == null) {
            return;
        }
        if (s.toString().equals(mContent) || TextUtils.isEmpty(s.toString())) {
            mMenu.findItem(R.id.edit_save).setVisible(false);
        } else {
            mMenu.findItem(R.id.edit_save).setVisible(true);
        }
    }
}
