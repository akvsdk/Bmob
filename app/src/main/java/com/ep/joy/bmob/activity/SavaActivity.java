package com.ep.joy.bmob.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ep.joy.bmob.R;
import com.ep.joy.bmob.bean.ImageInfo;
import com.ep.joy.bmob.utils.FileUtil;
import com.ep.joy.bmob.utils.GlideProxy;
import com.ep.joy.bmob.utils.ShareElement;
import com.ep.joy.bmob.utils.ShareUtil;
import com.jiongbull.jlog.JLog;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * author   Joy
 * Date:  2016/6/8.
 * version:  V1.0
 * Description:
 */
public class SavaActivity extends AppCompatActivity {
    public static final String BEAN = "bean";
    private ImageView mImageView;
    public ImageInfo mDatas;
    private Toolbar toolbar;
    private AppBarLayout mbar;
    public String format;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimg);
        mDatas = getIntent().getParcelableExtra(BEAN);
        initToolBar();
        mImageView = (ImageView) findViewById(R.id.img);
        mImageView.setImageDrawable(ShareElement.shareDrawable);
        ViewCompat.setTransitionName(mImageView, "share");

        String[] ss = mDatas.getImgurl().split("\\.");
        format = ss[ss.length - 1];
        if (format.equals("gif")) {

            GlideProxy.getInstance().loadImage(this, mDatas.getImgurl(), mImageView);
        } else {
            GlideProxy.getInstance().loadbitmap(this, mDatas.getImgurl(), new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mImageView.setImageBitmap(resource);

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareElement.shareDrawable = null;
    }

    private void initToolBar() {

        mbar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(mDatas.getContent());
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_girl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (!FileUtil.isSDCardEnable()) {
                    Toast.makeText(this, "未检测到内存卡!", Toast.LENGTH_SHORT).show();
                } else {
                    saveImg(false);
                }
                break;
            case R.id.action_share:
                //  presenter.shareGirlImage(girl, DateUtil.toDateString(meizi.publishedAt).toString());
                saveImg(true);
                break;
            case android.R.id.home:
                onBackPressed();
                ShareElement.shareDrawable = null;
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void saveImg(final boolean isShare) {
        Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                subscriber.onNext(GlideProxy.getInstance().savegif(SavaActivity.this, mDatas.getImgurl()));
            }
        })
                .map(new Func1<File, Uri>() {
                    @Override
                    public Uri call(File file) {
                        return FileUtil.saveBitmapToSDCard(file, mDatas.getContent(), format);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        if (!isShare) {
                            Toast.makeText(SavaActivity.this, "保存成功,请在手机根目录下查看..", Toast.LENGTH_SHORT).show();
                        } else {
                            ShareUtil.shareImage(SavaActivity.this, uri, "Joy");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        JLog.e(throwable);
                    }
                });

    }


}
