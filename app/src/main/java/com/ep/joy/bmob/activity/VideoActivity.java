package com.ep.joy.bmob.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.base.BaseActivity;
import com.ep.joy.bmob.bean.Day;
import com.ep.joy.bmob.utils.GlideProxy;
import com.ep.joy.bmob.utils.NetWorkUtil;
import com.ep.joy.bmob.weight.CustomTextView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * author   Joy
 * Date:  2016/7/21.
 * version:  V1.0
 * Description:
 */
public class VideoActivity extends BaseActivity {
    private ImageView videoLin;
    private ImageView videoImg;
    private ImageView videoPaly;
    private TextView videoDetailTitle;
    private TextView videoDetailTime;
    private TextView videoDetailDesc;
    private TextView videoDetailTvFav;
    private TextView videoDetailTvShare;
    private Toolbar toolbar;
    private AppBarLayout mbar;
    private Day.IssueListEntity.ItemListEntity mdatas;

    @Override
    protected int setContentView() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        initToolBar();
        videoLin = (ImageView) findViewById(R.id.video_lin);
        videoImg = (ImageView) findViewById(R.id.video_img);
        videoPaly = (ImageView) findViewById(R.id.video_paly);
        videoDetailTitle = (TextView) findViewById(R.id.video_detail_title);
        videoDetailTime = (TextView) findViewById(R.id.video_detail_time);
        videoDetailDesc = (TextView) findViewById(R.id.video_detail_desc);
        videoDetailTvFav = (TextView) findViewById(R.id.video_detail_tv_fav);
        videoDetailTvShare = (TextView) findViewById(R.id.video_detail_tv_share);
    }

    @Override
    protected void initData() {
        mdatas = (Day.IssueListEntity.ItemListEntity) getIntent().getSerializableExtra("bean");
        String time = getIntent().getStringExtra("time");
        videoDetailTitle.setText(mdatas.getData().getTitle());
        videoDetailDesc.setText(mdatas.getData().getDescription());
        videoDetailTvFav.setText(mdatas.getData().getConsumption().getCollectionCount() + "");
        videoDetailTvShare.setText(mdatas.getData().getConsumption().getShareCount() + "");
        videoDetailTime.setText(time);
        GlideProxy.getInstance().loadUriImage(this, Uri.parse(mdatas.getData().getCover().getBlurred()), videoLin);
        GlideProxy.getInstance().loadUriImage(this, Uri.parse(mdatas.getData().getCover().getFeed()), videoImg);

        RxView.clicks(videoPaly).debounce(1, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (NetWorkUtil.isNetConnected(VideoActivity.this)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("video", mdatas.getData().getPlayUrl());
                    bundle.putString("title", mdatas.getData().getTitle());
                    readyGo(VideoPlayActivity.class, bundle);
                } else {
                    toast("网络异常，请稍后再试");
                }
            }
        });
    }

    private void initToolBar() {

        mbar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        CustomTextView title = (CustomTextView) findViewById(R.id.toolbar_tv);
        title.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

            case R.id.action_share:
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
