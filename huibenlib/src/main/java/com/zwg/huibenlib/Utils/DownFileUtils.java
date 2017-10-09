package com.zwg.huibenlib.Utils;

import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2016/10/10.
 */
public class DownFileUtils {

    public Callback.Cancelable cancelable;

    public void down(String url,String cache,Callback.ProgressCallback<File> callback){

        //"http://116.199.115.41/mp4files/1165000004BF015E/movie.ks.js.cn/flv/other/1_0.mp4"
        RequestParams params = new RequestParams(url);
        params.setAutoResume(true);//设置是否在下载是自动断点续传
        params.setAutoRename(false);//设置是否根据头信息自动命名文件
        params.setSaveFilePath(cache);
        params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
        params.setCancelFast(true);//是否可以被立即停止.

        cancelable = x.http().get(params,callback);


    }


    public void stopdown(){
        if(cancelable!=null) {
            cancelable.cancel();//暂停下载
        }
    }

    public boolean isCal(){
        if(cancelable!=null) {
           return cancelable.isCancelled();
        }
        return false;
    }






}
