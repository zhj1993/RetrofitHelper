package com.zhj.retrofithelper.retrofit.body;

import android.os.Handler;
import android.os.Looper;

import com.zhj.retrofithelper.retrofit.callback.RxProgressCallBack;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 介绍: (上传文件进度)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/19
 */

public class FileRequestBody extends RequestBody {

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 实体请求体
     */
    private RequestBody requestBody;

    /**
     * 上传回调接口
     */
    private RxProgressCallBack callback;
    /**
     * 文件
     */


    private BufferedSink bufferedSink;


    public FileRequestBody(RequestBody requestBody, RxProgressCallBack callback) {
        super();
        this.requestBody = requestBody;
        this.callback = callback;

    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer) {
            //因为项目重写了日志拦截器，而日志拦截器里面调用了 RequestBody.writeTo方法，但是 它的sink类型是Buffer类型，所以直接写入
            //如果不这么做的话，上传进度最终会达到200%，因为被调用2次，而且日志拦截的writeTo是直接写入到 buffer 对象中，所以会很快；
            requestBody.writeTo(sink);
            return;
        }
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink
     * @return
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, final long byteCount) throws IOException {
                super.write(source, byteCount);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (contentLength == 0) {
                            //获得contentLength的值，后续不再调用
                            try {
                                contentLength = contentLength();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //增加当前写入的字节数
                        bytesWritten += byteCount;
                        //回调
                        if (callback != null) {
                            callback.onProgress(bytesWritten, contentLength);
                        }
                    }
                });
            }
        };
    }

}
