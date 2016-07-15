package com.ep.joy.bmob.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ep.joy.bmob.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * author meikoz on 2016/4/13.
 * email  meikoz@126.com
 */
public class GlideProxy {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private GlideProxy() {
    }

    private static class GlideControlHolder {
        private static GlideProxy instance = new GlideProxy();
    }

    public static GlideProxy getInstance() {
        return GlideControlHolder.instance;
    }

    // 将资源ID转为Uri
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }


    // 加载drawable图片
    public void loadResImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .placeholder(R.color.material_white)
                .error(R.color.material_white)
                .crossFade()
                .into(imageView);
    }

    //以bitmap加载图片
    public void loadbitmap(Context context, String url, SimpleTarget<Bitmap> listener) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(listener);
    }

    //以gif加载图片
    public void loadgif(Context context, String url, SimpleTarget<GifDrawable> listener) {
        Glide.with(context)
                .load(url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(listener);
    }

    //以gif保存图片
    public File savegif(Context context, String url) {
        File file = null;
        try {
            file = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (file != null) {
            return file;
        } else {
            return null;
        }

    }

    // 加载本地图片
    public void loadLocalImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load("file://" + path)
                .placeholder(R.color.material_white)
                .error(R.color.material_white)
                .crossFade()
                .into(imageView);
    }



    /*
    DiskCacheStrategy.SOURCE：缓存原始数据，
    DiskCacheStrategy.RESULT：缓存变换后的资源数据，
    DiskCacheStrategy.NONE：什么都不缓存，
    DiskCacheStrategy.ALL：缓存SOURC和RESULT。默认采用DiskCacheStrategy.RESULT策略，
    对于download only操作要使用DiskCacheStrategy.SOURCE。
     */

    // 加载网络图片
    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .animate(R.anim.image_load)
                .placeholder(R.color.material_white)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.color.material_white)
                .crossFade()
                .into(imageView);
    }


    // 加载网络圆型图片
    public void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .animate(R.anim.image_load)
                .placeholder(R.color.material_white)
                .error(R.color.material_white)
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    // 加载drawable圆型图片
    public void loadCircleResImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .placeholder(R.color.material_white)
                .error(R.color.material_white)
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    // 加载本地圆型图片
    public void loadCircleLocalImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load("file://" + path)
                .placeholder(R.color.material_white)
                .error(R.color.material_white)
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    // 清理缓存
    public static void clear(final Context context) {
        Glide.get(context).clearMemory();
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                Glide.get(context).clearDiskCache();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(context, "清理成功", Toast.LENGTH_SHORT).show();
                    }
                });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(context).clearDiskCache();
//            }
//        }).start();
    }

}
