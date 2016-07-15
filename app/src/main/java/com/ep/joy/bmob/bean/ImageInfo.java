package com.ep.joy.bmob.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author   Joy
 * Date:  2016/6/7.
 * version:  V1.0
 * Description:
 */
public class ImageInfo implements Parcelable {
    String imgurl;
    String content;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgurl);
        dest.writeString(this.content);
    }

    public ImageInfo() {
    }

    protected ImageInfo(Parcel in) {
        this.imgurl = in.readString();
        this.content = in.readString();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel source) {
            return new ImageInfo(source);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };
}
