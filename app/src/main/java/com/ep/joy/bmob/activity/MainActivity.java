package com.ep.joy.bmob.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.base.BaseActivity;
import com.ep.joy.bmob.bean.MyUser;
import com.ep.joy.bmob.fragment.FragmentFour;
import com.ep.joy.bmob.fragment.FragmentOne;
import com.ep.joy.bmob.fragment.FragmentThree;
import com.ep.joy.bmob.fragment.FragmentTwo;
import com.ep.joy.bmob.utils.FileUtil;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.ProgressCallback;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends BaseActivity {

    private MyUser mBmobUser;
    public static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;
    private List<File> mImgs = new ArrayList<>();
    private ViewPager viewPager;


    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mBmobUser = BmobUser.getCurrentUser(MyUser.class);
        Log.e("Joy", mBmobUser.getObjectId());
        Log.e("Joy", mBmobUser.getIcon().getFilename());
        viewPager = (ViewPager) findViewById(R.id.vp);
        TabFragmentPagerAdapter mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_send), Color.parseColor(colors[0]))
                        .title("首页")
                        .build());
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_gallery), Color.parseColor(colors[1]))
                        .title("视频")
                        .build());
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth), Color.parseColor(colors[2]))
                        .title("新闻")
                        .build());
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_manage), Color.parseColor(colors[3]))
                        .title("设置")
                        .build());

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setBehaviorEnabled(true);
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final String title = String.valueOf(new Random().nextInt(15));
                            if (!model.isBadgeShowed()) {
                                model.setBadgeTitle(title);
                                model.showBadge();
                            } else model.updateBadgeTitle(title);
                        }
                    }, i * 100);
                }

                coordinatorLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Snackbar snackbar = Snackbar.make(navigationTabBar, "有点nice^_^", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                                .setTextColor(Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }
                }, 1000);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        Picker.from(this)
//                .count(3)
//                .enableCamera(true)
//                .setEngine(new GlideEngine())
//                .forResult(REQUEST_CODE_CHOOSE);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                mImgs.add(FileUtil.getAbsoluteImagePath(u, MainActivity.this));
            }

            final BmobFile bmobFile = new BmobFile(mImgs.get(0));
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            bmobFile.uploadObservable(new ProgressCallback() {
                @Override
                public void onProgress(Integer integer, long l) {
                    progressDialog.setProgress(integer);
                    progressDialog.setTitle("上传图片");
                    progressDialog.setMessage("上传中……");
                    progressDialog.show();
                    Log.e("Joy", integer + "***********");
                }
            })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            toast("上传成功");
                            progressDialog.dismiss();
                        }
                    })
                    .concatMap(new Func1<Void, Observable<Void>>() {
                        @Override
                        public Observable<Void> call(Void aVoid) {
                            mBmobUser.setIcon(bmobFile);
                            return mBmobUser.updateObservable();
                        }
                    })
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void s) {
                            toast("更新成功");
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            toast(throwable.getMessage());
                        }
                    });

        }
    }


    private static class TabFragmentPagerAdapter extends FragmentPagerAdapter {


        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return FragmentOne.newInstance(0);
                }
                case 1: {
                    return FragmentTwo.newInstance(1);
                }
                case 2: {
                    return FragmentThree.newInstance(2);
                }
                case 3: {
                    return FragmentFour.newInstance(3);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

}
