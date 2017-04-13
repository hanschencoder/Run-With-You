package site.hanschen.runwithyou.ui;


import android.content.Context;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.widget.MediaController;

/**
 * @author HansChen
 */
public class ActionbarMediaController extends MediaController {

    private ActionBar mActionBar;

    public ActionbarMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionbarMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public ActionbarMediaController(Context context) {
        super(context);
    }

    public void setSupportActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
        if (mActionBar == null) {
            return;
        }
        if (isShowing()) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    @Override
    public void show(int timeout) {
        super.show(timeout);
        if (mActionBar != null) {
            mActionBar.show();
        }
    }

    @Override
    public void show() {
        super.show();
        if (mActionBar != null) {
            mActionBar.show();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (mActionBar != null) {
            mActionBar.hide();
        }
    }
}
