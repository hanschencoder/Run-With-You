package site.hanschen.runwithyou.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import site.hanschen.runwithyou.R;


/**
 * @author HansChen
 */
public class TargetSeekBarPreference extends DialogPreference {

    private Context  mContext;
    private SeekBar  mSeekBar;
    private TextView mHintText;

    public TargetSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setDialogLayoutResource(R.layout.preference_target_seekbar);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        SharedPreferences pref = getSharedPreferences();

        int target = pref.getInt(mContext.getResources().getString(R.string.pref_target_step), 8000);
        mHintText = (TextView) view.findViewById(R.id.pref_seek_bar_hint);
        mHintText.setText(String.format(Locale.getDefault(), "每日运动目标：%d步", target));
        mSeekBar = (SeekBar) view.findViewById(R.id.pref_seek_bar_target);
        mSeekBar.setProgress(target);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHintText.setText(String.format(Locale.getDefault(), "每日运动目标：%d步", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(mContext.getResources().getString(R.string.pref_target_step), mSeekBar.getProgress());
            editor.commit();
        }
    }
}
