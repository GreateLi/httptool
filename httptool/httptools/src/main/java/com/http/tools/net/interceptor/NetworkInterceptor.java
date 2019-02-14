package com.http.tools.net.interceptor;

import com.http.tools.utils.LogUtils;
import com.http.tools.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class NetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (!NetworkUtils.isConnected()) {  //没网强制从缓存读取
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            LogUtils.d("Okhttp", "no network");
        }

        if (response.code() == 200) { //这里是网络拦截器，可以做错误处理
            MediaType mediaType = response.body().contentType(); //当调用此方法java.lang.IllegalStateException: closed，原因为OkHttp请求回调中
            // response.body().string()只能有效调用一次 //
           // String content = response.body().string();
            byte[] data = response.body().bytes();

            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();

                return response.newBuilder()
                        .body(ResponseBody.create(mediaType, data))
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return response.newBuilder()
                        .body(ResponseBody.create(mediaType, data))
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
            //return response.newBuilder().body(ResponseBody.create(mediaType, data)).build();
        } else {
            return response;
        }
    }


}

