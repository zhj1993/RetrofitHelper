package com.zhj.retrofithelper.retrofit.rx;

import android.os.Environment;
import android.util.Log;

import com.zhj.retrofithelper.FIleHelper;
import com.zhj.retrofithelper.retrofit.callback.RxResponseCallBack;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 介绍: (这里用一句话描述这个类的作用)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/19
 */

public class RxHelper {


    /**
     * 线程切换
     */
    private static ObservableTransformer observableTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };


    /**
     * 下载文件
     *
     * @param observable
     * @param rxresponseCallBack
     */
    public static void download(Observable<ResponseBody> observable, final RxResponseCallBack rxresponseCallBack) {
        if (null != observable) {
            if (null != rxresponseCallBack) {
                rxresponseCallBack.onStart();
            }
            observable.compose(observableTransformer)
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(@NonNull ResponseBody responseBody) throws Exception {
                            if (null != responseBody) {
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/retrofit.apk");
                                Log.d("apk", "path-->" + file.getAbsolutePath());
                                boolean b = FIleHelper.saveFile(file, responseBody.byteStream());
                                if (b && file.exists()) {
                                    if (null != rxresponseCallBack) {
                                        rxresponseCallBack.onSuccess(file);
                                    }
                                } else {
                                    if (null != rxresponseCallBack) {
                                        rxresponseCallBack.onError(-1, "下载失败");
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Log.d("oye", "error--->" + throwable.getMessage());
                            if (null != rxresponseCallBack) {
                                rxresponseCallBack.onError(-1, "");
                                rxresponseCallBack.onNext();
                            }
                        }
                    });
        }
    }

    public static void post(Observable<ResponseBody> observable, final RxResponseCallBack rxresponseCallBack) {
        post(observableTransformer, observable, rxresponseCallBack);
    }

    /**
     * 请求
     *
     * @param observableTransformer
     * @param observable
     * @param rxresponseCallBack
     */
    public static void post(ObservableTransformer observableTransformer,
                            Observable<ResponseBody> observable,
                            final RxResponseCallBack rxresponseCallBack) {
        if (null != observable) {
            if (null != rxresponseCallBack) {
                rxresponseCallBack.onStart();
            }
            observable.compose(observableTransformer)
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(@NonNull ResponseBody responseBody) throws Exception {
                            if (null != responseBody) {
                                String string = responseBody.string();
                                Log.d("oye", "json--->" + string);
                                if (null != rxresponseCallBack) {
                                    rxresponseCallBack.onSuccess(string);
                                    rxresponseCallBack.onNext();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Log.d("oye", "error--->" + throwable.getMessage());
                            if (null != rxresponseCallBack) {
                                rxresponseCallBack.onError(-1, "");
                                rxresponseCallBack.onNext();
                            }
                        }
                    });
        }
    }

}
