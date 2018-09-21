package com.zhj.retrofithelper.retrofit.param;

import android.text.TextUtils;

import com.zhj.retrofithelper.retrofit.body.FileRequestBody;
import com.zhj.retrofithelper.retrofit.callback.RxResponseCallBack;
import com.zhj.retrofithelper.retrofit.callback.RxCallBackHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 介绍: (参数添加)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/19
 */

public class ParamsMap {


    private Map<String, String> mParamMap;//表单提交

    public ParamsMap() {
        mParamMap = new HashMap<>();
    }

    public static ParamsMap create() {
        return new ParamsMap();
    }

    /**
     * 表单提交
     *
     * @param key
     * @param value
     * @return
     */
    public ParamsMap put(String key, int value) {
        return put(key, value + "");
    }

    /**
     * 表单提交
     *
     * @param key
     * @param value
     * @return
     */
    public ParamsMap put(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            key = "";
        }
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        mParamMap.put(key, value);
        return this;
    }

    /**
     * 构建map 参数集
     *
     * @return
     */
    public Map<String, String> build() {
        return mParamMap;
    }

    /**
     * 构建RequestBody 参数集
     *
     * @return
     */
    public Map<String, RequestBody> buildRequestBody() {
        return buildParameter(mParamMap);
    }


    /**
     * 请求参数封装
     *
     * @param key
     * @param value
     * @return
     */
    public static Map<String, RequestBody> buildParameter(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            key = "";
        }
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return buildParameter(map);
    }


    /**
     * 请求参数封装
     *
     * @param map
     * @return
     */
    public static Map<String, RequestBody> buildParameter(Map<String, String> map) {
        Map<String, RequestBody> parameter = new HashMap<>();
        if (null != map) {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                parameter.put(next.getKey(), paramsToRequestBody(next.getValue()));
            }
        }
        return parameter;
    }

    /**
     * 参数转为请求体
     *
     * @param param
     * @return
     */
    public static RequestBody paramsToRequestBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), param);
    }


    public static List<MultipartBody.Part> buildImageFiles(List<File> files,RxResponseCallBack rxResponseCallBack) {
        return buildImageFiles("files", files, rxResponseCallBack);
    }

    /**
     * 转换成retrofit上传文件集合
     *
     * @param key
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> buildImageFiles(String key, List<File> files, RxResponseCallBack rxResponseCallBack) {
        if (files == null) {
            files = new ArrayList<>();
        }
        RxCallBackHelper uploadHelper = new RxCallBackHelper();
        long total = 0;//文件总大小
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {//遍历构建图片上传参数
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    key,
                    file.getName(),
                    rxResponseCallBack != null ? new FileRequestBody(requestBody, uploadHelper.getRxProgressCallBack()) : requestBody);
            parts.add(part);
            try {
                //计算文件总大小
                total += requestBody.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //设置回调
        uploadHelper.setTotal(total)
                .setRxResponseCallBack(rxResponseCallBack);
        return parts;
    }

}
