package com.zwg.huibenlib;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import org.xutils.x;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MyApplication extends Application{
    private static MyApplication app;

    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        app=this;
        x.Ext.init(this);//初始化
        x.Ext.setDebug(true);//设置是否输出Debug.

    }

    public static MyApplication getInstance(){
        return app;
    }

    public void showToast(String msg){
        Toast.makeText(app,msg,Toast.LENGTH_LONG).show();
    }
}
