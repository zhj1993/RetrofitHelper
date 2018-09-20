package com.zhj.retrofithelper.retrofit.interceptor;

import com.zhj.retrofithelper.retrofit.body.FileResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 介绍: (这里用一句话描述这个类的作用)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/20
 */

public class DownloadInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new FileResponseBody(originalResponse.body()))
                .build();
    }
}
