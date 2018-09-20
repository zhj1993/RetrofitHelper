package com.zhj.retrofithelper.retrofit.callback;

/**
 * 介绍: (上传进度回调)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/19
 */

public interface RxProgressCallBack {

    void onProgress(long current, long total);

}
