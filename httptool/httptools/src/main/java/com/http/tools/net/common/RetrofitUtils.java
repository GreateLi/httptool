package com.http.tools.net.common;

import android.util.Log;

import com.google.protobuf.ExtensionRegistry;

import com.http.tools.net.interceptor.NetworkInterceptor;
import com.http.tools.utils.HUtils;
import com.http.tools.utils.Utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.protobuf.ProtoConverterFactory;

/**
 * Created by zhpan on 2018/3/21.
 */

public class RetrofitUtils {
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        File cacheFile = new File(Utils.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10); //100Mb
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
               // .addInterceptor(new LoggingInterceptor())
                .addInterceptor(httpInterceptor)//添加拦截器
              //  .addInterceptor(new HttpHeaderInterceptor())
                .addNetworkInterceptor(new NetworkInterceptor())
                // .sslSocketFactory(SslContextFactory.getSSLSocketFactoryForTwoWay())  // https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
                // .hostnameVerifier(new SafeHostnameVerifier())
                .cache(cache);
        //只有debug版本打印网络请求日志
        if (HUtils.isDebug(Utils.getContext())) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder;
    }
    /**
     * 创建拦截器
     */
    private static Interceptor httpInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = addPublicParameter(chain.request());
            if (request.body() == null || request.header("Content-Encoding") != null) {
                return chain.proceed(request);
            }

            Request compressedRequest = request.newBuilder()
                 //   .header("Content-Encoding", "gzip")
                   // .method(request.method(), gzip(request.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

    };
    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        ExtensionRegistry registry = ExtensionRegistry.newInstance();
       // Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        OkHttpClient okHttpClient = getOkHttpClientBuilder().build();
        try {
            return new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(ProtoConverterFactory.createWithRegistry(registry))//一定要在gsonconvert的前面
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl);

        }catch (IllegalArgumentException e  )
        {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 给拦截器request路径添加公共参数
     * 登录前和登录后所添加的公共参数不一样
     *
     * @param request
     * @return
     */
    private static Request addPublicParameter(Request request) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        HttpUrl.Builder builder = request
                .url()
                .newBuilder()
                .addQueryParameter("timestamp",timestamp);//添加公共参数(每个接口都携带的参数，还可以继续add)
        Log.d("datacollectlib","url = " + builder.build());
        Request newRequest = request
                .newBuilder()
                .method(request.method(), request.body())
                .url(builder.build())
                .build();
        return newRequest;
    }

    private static RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return body.contentType();
            }

            @Override public long contentLength() {
                return -1; // 无法知道压缩后的数据大小
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
