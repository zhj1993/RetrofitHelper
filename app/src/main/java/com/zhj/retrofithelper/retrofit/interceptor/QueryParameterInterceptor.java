package com.zhj.retrofithelper.retrofit.interceptor;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 介绍: (配置公共请求参数)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/18
 */

public class QueryParameterInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //原先请求Request
        Request originalRequest = chain.request();
        //新请求newRequest
        Request newRequest = null;
        //构建请求链接配置拼接参数
        HttpUrl httpUrl = originalRequest.url().newBuilder()
                .addEncodedQueryParameter("test", "test")
                .addEncodedQueryParameter("system", "android")
                .addEncodedQueryParameter("osv", "8.0.0")
                .addEncodedQueryParameter("version", "4.0.7")
                .addEncodedQueryParameter("device", "866229030652901")
                .addEncodedQueryParameter("nonce", "1537247608")
                .addEncodedQueryParameter("model", "MHA-AL00")
                .addEncodedQueryParameter("channel", "Nduo")
                .build();
        newRequest = originalRequest.newBuilder().url(httpUrl).build();
        if (originalRequest.method().equals("POST")) {
            if (originalRequest.body() instanceof FormBody) {//表单
                //添加post参数
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.addEncoded("test", "form");
                //构建formBody对象
                FormBody formBodyBuild = formBody.build();
                //原先body参数+新增body参数拼接
                String formBodyString = bodyToString(originalRequest.body());
                formBodyString += (formBodyString.length() > 0 ? "&" : "") + bodyToString(formBodyBuild);
                newRequest = originalRequest.newBuilder()
                        .url(httpUrl)
                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), formBodyString))
                        .build();
            } else if (originalRequest.body() instanceof MultipartBody) {//文件
                MultipartBody oldBodyMultipart = (MultipartBody) originalRequest.body();
                //文件个数
                List<MultipartBody.Part> oldPartList = oldBodyMultipart.parts();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                //设置类型
                builder.setType(MultipartBody.FORM);
                //遍历添加文件
                for (MultipartBody.Part part : oldPartList) {
                    builder.addPart(part);
                }
                //添加公共post参数
                builder.addFormDataPart("test", "multipart");
                newRequest = originalRequest.newBuilder()
                        .url(httpUrl)
                        .post(builder.build())
                        .build();
            }
            //返回新构建的请求体
        }
        return chain.proceed(newRequest);
    }


    /**
     * RequestBody转字符串
     *
     * @param request
     * @return
     */
    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null) {
                request.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
