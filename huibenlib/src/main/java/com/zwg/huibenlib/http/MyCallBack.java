package com.zwg.huibenlib.http;


import org.xutils.common.Callback;

/**
 * Created by Administrator on 2016/10/15.
 */
public abstract class MyCallBack implements Callback.CommonCallback<String>{
    @Override
    public abstract void onSuccess(String s);

    @Override
    public abstract void onError(Throwable throwable, boolean b) ;
    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }
}
