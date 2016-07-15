package com.ep.joy.bmob.utils;

import android.net.Uri;
import android.os.Environment;

import com.jiongbull.jlog.JLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * author   Joy
 * Date:  2016/6/8.
 * version:  V1.0
 * Description:
 */
public class FileUtil {
    private FileUtil() {
        throw new UnsupportedOperationException("can not be instanced");
    }

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static Uri saveBitmapToSDCard(File mfile, String title, String format) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "zhuangB");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = title.replace("/", "-") + "." + format;
        File file = new File(appDir, fileName);

        try {
            int bytesum = 0;
            int byteread = 0;
            if (mfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(mfile); //读入原文件
                FileOutputStream fs = new FileOutputStream(file);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    JLog.d(bytesum + "************");
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(file);
    }
}
