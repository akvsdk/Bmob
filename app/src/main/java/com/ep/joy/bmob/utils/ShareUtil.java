package com.ep.joy.bmob.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 分享相关工具类
 * Created by panl on 16/1/8.
 */
public class ShareUtil {

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

}
