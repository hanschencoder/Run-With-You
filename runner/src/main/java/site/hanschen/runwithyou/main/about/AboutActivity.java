package site.hanschen.runwithyou.main.about;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;

/**
 * @author HansChen
 */
public class AboutActivity extends RunnerBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        StatusBarCompat.setColor(AboutActivity.this, Color.parseColor("#404040"));
        FrameLayout container = (FrameLayout) findViewById(R.id.about_container);

        AboutView view = AboutBuilder.with(this)
                                     .setPhoto(R.drawable.hanschen)
                                     .setCover(R.mipmap.profile_cover)
                                     .setName("HansChen")
                                     .setBrief("博观而约取，厚积而薄发")
                                     .setLinksColumnsCount(4)
                                     .addGitHubLink("shensky711")
                                     .addWebsiteLink("http://blog.hanschen.site/")
                                     .addSkypeLink("+8613825633225")
                                     .addGoogleLink("shensky711")
                                     .addEmailLink("shensky711@gmail.com")
                                     .addGooglePlusLink("+shensky711")
                                     .addLinkedInLink("航-陈-a7843b118")
                                     .addAndroidLink("shensky711")
                                     .setAppIcon(R.mipmap.ic_launcher)
                                     .setAppName(R.string.app_name)
                                     .addFiveStarsAction()
                                     .addShareAction(R.string.app_name)
                                     .addFeedbackAction("shensky711@gmail.com")
                                     .addHelpAction((Intent) null)
                                     .addChangeLogAction((Intent) null)
                                     .setActionsColumnsCount(3)
                                     .setLinksAnimated(true)
                                     .setBackgroundColor(R.color.dark_content_background)
                                     .setDividerDashGap(12)
                                     .setVersionNameAsAppSubTitle()
                                     .setWrapScrollView(true)
                                     .setShowAsCard(true)
                                     .build();
        container.addView(view);
    }
}
