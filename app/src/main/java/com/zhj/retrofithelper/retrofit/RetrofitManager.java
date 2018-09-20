package com.zhj.retrofithelper.retrofit;

import com.zhj.retrofithelper.App;
import com.zhj.retrofithelper.retrofit.interceptor.DownloadInterceptor;
import com.zhj.retrofithelper.retrofit.interceptor.LoggingInterceptor;
import com.zhj.retrofithelper.retrofit.interceptor.QueryParameterInterceptor;
import com.zhj.retrofithelper.retrofit.interceptor.RewriteCacheInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 介绍: (这里用一句话描述这个类的作用)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/10
 */

public class RetrofitManager {

    public static final String TAG = "RetrofitManager";
    //链接超时时间
    private static long CONNECT_TIMEOUT = 60L;
    //读取超时时间
    private static long READ_TIMEOUT = 10L;
    //写入超时时间
    private static long WRITE_TIMEOUT = 10L;
    //全局OkHttpClient对象
    private static volatile OkHttpClient mOkHttpClient;
    private static volatile Retrofit mRetrofit;

    /**
     * 获取OkHttpClient实例
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                Cache cache = new Cache(new File(App.getInstance().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(new RewriteCacheInterceptor())//缓存
                            .addInterceptor(new QueryParameterInterceptor())//参数
                            .addInterceptor(new LoggingInterceptor())//log
                            .addInterceptor(new DownloadInterceptor())//下载
                            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }


    /**
     * 获取Service
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clazz) {
        if (null == mRetrofit) {
            synchronized (RetrofitManager.class) {
                mRetrofit = createRetrofit("");//baseurl
            }
        }
        return mRetrofit.create(clazz);
    }

    public static Retrofit createRetrofit(String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    /**
     * 获取Service
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clazz, String url) {
        return createRetrofit(url).create(clazz);
    }

}
