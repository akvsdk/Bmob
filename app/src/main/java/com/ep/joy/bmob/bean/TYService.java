package com.ep.joy.bmob.bean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;
import rx.Observable;

/**
 * author   Joy
 * Date:  2016/7/19.
 * version:  V1.0
 * Description:
 */
public interface TYService {
    @GET("http://baobab.wandoujia.com/api/v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    Observable<Day> getImg(@Header("Cache-Control") String cacheControl);

    @GET
    Observable<Day> getNextImg(@Header("Cache-Control") String cacheControl,@Url String url);
}
