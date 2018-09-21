package com.zhj.retrofithelper.retrofit.callback;

/**
 * 介绍: (上传辅助类 计算上传写入进度)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/19
 */

public class RxCallBackHelper {


    private long mTotal;//上传文件总大小
    private long mCurrent;//当前总进度
    private long flag;//每一次图片的大小

    private RxResponseCallBack rxResponseCallBack;

    public void setRxResponseCallBack(RxResponseCallBack rxResponseCallBack) {
        this.rxResponseCallBack = rxResponseCallBack;
    }

    public RxCallBackHelper setTotal(long total) {
        this.mTotal = total;
        return this;
    }


    /**
     * 进度回调
     */
    private RxProgressCallBack rxProgressCallBack = new RxProgressCallBack() {
        @Override
        public void onProgress(long current, long total) {
            if (current != total) {//没有上传或者完成
                mCurrent = current + flag;
            } else {//上传或下载完成
                flag += total;
                mCurrent = flag;
            }
            if (null != rxResponseCallBack) {
                rxResponseCallBack.onProgress(mCurrent, mTotal);
            }
        }
    };

    public RxProgressCallBack getRxProgressCallBack() {
        return rxProgressCallBack;
    }
}
