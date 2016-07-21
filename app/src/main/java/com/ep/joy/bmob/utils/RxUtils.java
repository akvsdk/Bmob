package com.ep.joy.bmob.utils;



import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author   Joy
 * Date:  2016/5/17.
 * version:  V1.0
 * Description:
 */
public class RxUtils {

    private RxUtils() {
    }

    /**
     * 解除订阅
     *
     * @param subscription 订阅者
     */
    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }


    /**
     * 指定网络线程
     *
     * @param observable 观察者
     * @param subscriber 订阅者
     * @param <T>        泛型
     */

    public static <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }



    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
//    public static class HttpResultFunc<T> implements Func1<JsonResult<T>, T> {
//
//        @Override
//        public T call(JsonResult<T> httpResult) {
//            if (!httpResult.isStatus()) {
//                throw new ApiException(100);
//            }
//            return httpResult.gettngou();
//        }
//    }
}
