package com.zwg.huibenlib.Utils;

import org.xutils.common.Callback;

import java.io.File;

/**
 * Created by Administrator on 2016/10/17.
 */
public abstract class MyProgressCallback implements Callback.ProgressCallback<File> {
    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public abstract void onLoading(long l, long l1, boolean b);

    @Override
    public abstract void onSuccess(File file);

    @Override
    public abstract void onError(Throwable throwable, boolean b);

    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }
}
