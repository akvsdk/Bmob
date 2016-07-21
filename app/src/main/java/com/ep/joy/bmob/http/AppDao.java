package com.ep.joy.bmob.http;

import com.ep.joy.bmob.bean.Day;
import com.ep.joy.bmob.utils.RxUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * author   Joy
 * Date:  2016/7/19.
 * version:  V1.0
 * Description:
 */
public class AppDao {

    public static void getImg(Subscriber<Day> subscriber) {
        Observable observable = Factory.provideImgService().getImg(HttpClient.getCacheControl());
        RxUtils.toSubscribe(observable, subscriber);
    }

    public static void getNextImg(String url, Subscriber<Day> subscriber) {
        Observable observable = Factory.provideImgService().getNextImg(HttpClient.getCacheControl(),url);
        RxUtils.toSubscribe(observable, subscriber);
    }
}
