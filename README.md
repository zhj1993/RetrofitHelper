# RetrofitHelper
retrofit 简单封装 实现公共参数get和post统一处理,上传和下载带进度显示,图片和本文一起提交

1.普通请求
RxHelper.post(RetrofitManager.create(ApiService.class)
                        .getBookDetail(ParamsMap.create().put("bid", "b11192852").put("qqq", "111").build())
                , new RxResponseCallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        String json = (String) o;
                        Log.d("json", json);
                    }
                    @Override
                    public void onError(int code, String msg) {
                    }
                });
                
2.图片和参数一起提交
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
   
 3.下载文件 进度更新在FileResponseBody 可以使用Eventbus发送消息更新 具体可以查阅代码
  RxHelper.download(RetrofitManager.create(ApiService.class)
                .download(url), new RxResponseCallBack() {
            @Override
            public void onSuccess(Object file) {
             Log.d("","下载成功");
            }
            @Override
            public void onError(int code, String msg) {
                Log.d("","下载失败");
            }
        });
        
   只是简单封装，由于项目要更换网络框架所以分享出来了，部分借鉴了githup上的代码
