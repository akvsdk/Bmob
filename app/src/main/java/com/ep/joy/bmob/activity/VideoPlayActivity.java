package com.ep.joy.bmob.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.base.BaseActivity;
import com.ep.joy.bmob.weight.CustomMediaController;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * author   Joy
 * Date:  2016/7/21.
 * version:  V1.0
 * Description:
 */
public class VideoPlayActivity extends BaseActivity implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {
    private Uri uri;
    private ProgressBar pb;
    private TextView downloadRateView, loadRateView;
    private MediaController mMediaController;
    private CustomMediaController mCustomMediaController;
    private VideoView mVideoView;


    @Override
    protected int setContentView() {
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = VideoPlayActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //必须写这个，初始化加载库文件
        Vitamio.isInitialized(this);
        //设置视频解码监听
//        if (!LibsChecker.checkVitamioLibs(this)) {
//            return;
//        }
        return R.layout.activity_show_video;
    }

    @Override
    protected void initView() {
        mVideoView = (VideoView) findViewById(R.id.buffer);
        mMediaController= new MediaController(this);
        mCustomMediaController=new CustomMediaController(this,mVideoView,this);

        pb = (ProgressBar) findViewById(R.id.probar);
        downloadRateView = (TextView) findViewById(R.id.download_rate);
        loadRateView = (TextView) findViewById(R.id.load_rate);
    }

    @Override
    protected void initData() {
        String video = getIntent().getStringExtra("video");//视频播放地址
        String title = getIntent().getStringExtra("title");//视频标题
        mCustomMediaController.setVideoName(title);
        uri = Uri.parse(video);
        mVideoView.setVideoURI(uri);//设置视频播放地址
        mVideoView.setMediaController(mCustomMediaController);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
        mMediaController.show(5000);
        mVideoView.requestFocus();
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    pb.setVisibility(View.VISIBLE);
                    downloadRateView.setText("");
                    loadRateView.setText("");
                    downloadRateView.setVisibility(View.VISIBLE);
                    loadRateView.setVisibility(View.VISIBLE);

                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mVideoView.start();
                pb.setVisibility(View.GONE);
                downloadRateView.setVisibility(View.GONE);
                loadRateView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                downloadRateView.setText("" + extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        loadRateView.setText(percent + "%");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //屏幕切换时，设置全屏
        if (mVideoView != null){
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        }
        super.onConfigurationChanged(newConfig);
    }
}
