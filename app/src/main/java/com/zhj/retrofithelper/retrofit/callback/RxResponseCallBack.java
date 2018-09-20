package com.zhj.retrofithelper.retrofit.callback;

/**
 * 介绍: (网络请求回调)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/19
 */

public abstract class RxResponseCallBack<T> {


    /**
     * 请求开始
     */
    public void onStart(){

    }

    /**
     * 下载进度或上传进度
     * @param current 当前
     * @param total 总大小
     */
    public void onProgress(long current, long total) {

    }

    /**
     * 请求成功
     * @param t
     */
    public void onSuccess(T t){

    }

    /**
     * 请求失败
     * @param code
     * @param msg
     */
    public void onError(int code,String msg){

    }

    /**
     * 成功或者失败之后dosomething
     */
    public void onNext(){

    }

}
