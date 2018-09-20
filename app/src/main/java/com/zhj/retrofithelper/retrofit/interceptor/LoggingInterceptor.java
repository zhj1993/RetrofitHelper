package com.zhj.retrofithelper.retrofit.interceptor;

import android.util.Log;

import com.zhj.retrofithelper.retrofit.RetrofitManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 介绍: (这里用一句话描述这个类的作用)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/18
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        Buffer requestBuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestBuffer);
        } else {
            Log.d("LogTAG", "request.body() == null");
        }
        //打印url信息
        Log.w(RetrofitManager.TAG,
                "intercept: " + request.url() + (request.body() != null ? "?"
                        + parseParams(request.body(), requestBuffer) : ""));
        final Response response = chain.proceed(request);
        return response;
    }


    private static String parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
    }

}
