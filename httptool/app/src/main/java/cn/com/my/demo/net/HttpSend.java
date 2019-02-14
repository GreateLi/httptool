package cn.com.my.demo.net;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * 网络发送器
 * Created by HDL on 2017/11/20.
 *
 * @author HDL
 */

public class HttpSend {

    /**
     * 统一线程处理 HTTP 请求
     *
     * @param <T>
     * @param observable
     */
    public static  <T> Observable<T> toSubscribe(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
