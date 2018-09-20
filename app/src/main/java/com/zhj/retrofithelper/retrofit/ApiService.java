package com.zhj.retrofithelper.retrofit;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 介绍: (api参数设置)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/10
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("")
    Observable<ResponseBody> getVersion(@Field("version") String version);

    @GET("")
    Observable<ResponseBody> getTag(@Query("page") int version);

    @FormUrlEncoded
    @POST("")
    Observable<ResponseBody> getBookDetail(@Field("bid") String bid);

    @FormUrlEncoded
    @POST("")
    Observable<ResponseBody> getBookDetail(@FieldMap() Map<String, String> map);

    @Multipart  //上传图片和文本测试
    @POST("")
    Observable<ResponseBody> postImage(@Part List<MultipartBody.Part> parts, @PartMap Map<String, RequestBody> map);

    @GET
    Observable<ResponseBody> download(@Url String url);

}
