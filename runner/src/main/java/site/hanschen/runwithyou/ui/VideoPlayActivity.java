package site.hanschen.runwithyou.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.VideoView;

import site.hanschen.runwithyou.R;

public class VideoPlayActivity extends AppCompatActivity {

    private static final String KEY_TITLE = "KEY_TITLE";
    private static final String KEY_PATH  = "KEY_PATH";
    private static final String KEY_URL   = "KEY_URL";

    public static void playLocal(Context context, String title, String path) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(KEY_PATH, path);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }

    public static void playNetwork(Context context, String title, String url) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }

    private VideoView                mVideoView;
    private ProgressBar              mProgressBar;
    private ActionbarMediaController mMediaController;
    private String                   mTitle;
    private String                   mPath;
    private String                   mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        extraBundle();
        initViews();
    }

    private void extraBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || (TextUtils.isEmpty(mUrl = bundle.getString(KEY_URL)) && TextUtils.isEmpty(mPath = bundle.getString(
                KEY_PATH)))) {
            throw new IllegalArgumentException("bundle must contain url or path");
        }
        mTitle = bundle.getString(KEY_TITLE);
    }

    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.activity_video_play_loading);
        mVideoView = (VideoView) findViewById(R.id.activity_video_play_video);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.GONE);
                mp.start();
                mMediaController.show(3000);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mProgressBar.setVisibility(View.GONE);
                mMediaController.show(0);
                return false;
            }
        });

        if (!TextUtils.isEmpty(mPath)) {
            mVideoView.setVideoPath(mPath);
        } else {
            mVideoView.setVideoURI(Uri.parse(mUrl));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_video_play_toolbar);
        if (!TextUtils.isEmpty(mTitle)) {
            toolbar.setTitle(mTitle);
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mMediaController = new ActionbarMediaController(this);
        mMediaController.setSupportActionBar(getSupportActionBar());

        mVideoView.setMediaController(mMediaController);
        mMediaController.setMediaPlayer(mVideoView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // TODO:横屏
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // TODO:竖屏
        }
    }
}
