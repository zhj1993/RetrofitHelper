package com.zhj.retrofithelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.zhj.retrofithelper.retrofit.ApiService;
import com.zhj.retrofithelper.retrofit.RetrofitManager;
import com.zhj.retrofithelper.retrofit.callback.RxResponseCallBack;
import com.zhj.retrofithelper.retrofit.param.ParamsMap;
import com.zhj.retrofithelper.retrofit.rx.RxHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        EventBus.getDefault().register(this);
    }


    private String url = "http://apk.bearead.com/app-bearead-release.apk";

    public void downloadApk(View view) {
        RxHelper.download(RetrofitManager.create(ApiService.class)
                .download(url), new RxResponseCallBack() {
            @Override
            public void onSuccess(Object file) {
                if (file instanceof File) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile((File) file), "application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(install);
                }
            }

            @Override
            public void onError(int code, String msg) {
                Log.d("","下载失败");
            }
        });
    }


    private String path1 = "/storage/emulated/0/download/com.bearead.app/cache/1537255130939354.jpg";
    private String path2 = "/storage/emulated/0/download/com.bearead.app/cache/1537255138786215.jpg";
    private String path3 = "/storage/emulated/0/download/com.bearead.app/cache/1537411003218478.jpg";
    private String path4 = "/storage/emulated/0/download/com.bearead.app/cache/1537411013314599.jpg";
    private String path5 = "/storage/emulated/0/download/com.bearead.app/cache/153741101979725.jpg";
    private ProgressBar progressBar;

    public void postImage(View view) {
        List<File> files = new ArrayList<>();
        files.add(new File(path1));
        files.add(new File(path2));
        files.add(new File(path3));
        files.add(new File(path4));
        files.add(new File(path5));

        RxHelper.post(RetrofitManager
                .create(ApiService.class)
                .postImage(ParamsMap.buildImageFiles(files, new RxResponseCallBack() {
                    @Override
                    public void onProgress(long current, long total) {
                        Log.d("image", "current--->" + current);
                        Log.d("image", "total--->" + total);
                        int progress = (int) (100 * current / total);
                        progressBar.setProgress(progress);
                    }
                }), ParamsMap.create().put("aaa", "测试").put("bbb", "测试").buildRequestBody()), new RxResponseCallBack() {
            @Override
            public void onSuccess(Object o) {
                Log.d("image", "json----->" + o);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        long current = event.getCurrent();
        long total = event.getTotal();
        int progress = (int) (100 * current / total);
        progressBar.setProgress(progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
