package com.zhj.retrofithelper.retrofit.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.zhj.retrofithelper.App;
import com.zhj.retrofithelper.retrofit.RetrofitManager;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 介绍: (云端响应头拦截器，用来配置缓存策略)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/18
 */

public class RewriteCacheInterceptor implements Interceptor {

    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetworkAvailable(App.getInstance())) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            Log.e(RetrofitManager.TAG, "no network");
        }
        Response originalResponse = chain.proceed(request);

        if (isNetworkAvailable(App.getInstance())) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                    .removeHeader("Pragma")
                    .build();
        }
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // Context context = mActivity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
