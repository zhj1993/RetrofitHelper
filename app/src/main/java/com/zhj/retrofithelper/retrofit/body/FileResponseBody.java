package com.zhj.retrofithelper.retrofit.body;

import android.util.Log;

import com.zhj.retrofithelper.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 介绍: (这里用一句话描述这个类的作用)
 * 作者: zhahaijun
 * 邮箱: zhahaijun@bearead.cn
 * 时间: 2018/9/20
 */

public class FileResponseBody extends ResponseBody {

    private final ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;

    public FileResponseBody(ResponseBody mResponseBody) {
        this.mResponseBody = mResponseBody;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            private MessageEvent messageEvent = new MessageEvent();
            private long mProgress = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                mProgress += bytesRead == -1 ? 0 : bytesRead;
                Log.d("apk", "byteCount-->" + mProgress);
                Log.d("apk", "tatalCount-->" + contentLength());
                messageEvent.setTotal(contentLength());
                messageEvent.setCurrent(mProgress);
                EventBus.getDefault().post(messageEvent);
                return bytesRead;
            }
        };
    }
}
