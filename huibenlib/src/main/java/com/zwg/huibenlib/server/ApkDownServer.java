package com.zwg.huibenlib.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.wsh.tools.systemservice.SystemServiceHelper;
import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.Utils.SaveUtils;
import com.zwg.huibenlib.http.AppContents;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 创建人：  ganziqian
 * 作用：
 * 时间：2016/1/28
 */
public class ApkDownServer extends Service {

    private MyHandler myHandler;
    private int download_precent=0;

    private int count=0;
    private String  apkname="";

    private String apkpath="";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

      /*  PendingIntent contentIntent=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        notification.setLatestEventInfo(this,"","", contentIntent);*/
        //将下载任务添加到任务栏中

        myHandler=new MyHandler(Looper.myLooper(),this);
        //初始化下载任务内容views
        Message message=myHandler.obtainMessage(3,0);
        myHandler.sendMessage(message);
        String rl="";
        if(intent!=null) {
             rl = intent.getStringExtra("url");
            apkname=intent.getStringExtra("name");
        }else {
            stopSelf();
        }




       // downloadAPK(rl);
        down(rl);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("=====","dia");
    }


    public void down(String url){



        RequestParams params = new RequestParams(url);
        params.setAutoRename(true);//断点下载

        params.setAutoResume(true);//设置是否在下载是自动断点续传
        params.setAutoRename(false);//设置是否根据头信息自动命名文件
        params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
        params.setCancelFast(true);//是否可以被立即停止.

        if(apkname!=null){
            if(apkname.equals("")){
                apkpath = "/mnt/sdcard/" + AppContents.getAppVersionName(getBaseContext()) + "broow.apk";
            }else {
                apkpath = "/mnt/sdcard/" + apkname;
            }
        }else {
            apkpath = "/mnt/sdcard/" + AppContents.getAppVersionName(getBaseContext()) + "broow.apk";
        }
        params.setSaveFilePath(apkpath);
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                Message message=myHandler.obtainMessage(4,"下载更新文件失败");
                myHandler.sendMessage(message);
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(File arg0) {
                Message message=myHandler.obtainMessage(2,arg0);
                myHandler.sendMessage(message);

            }


            @Override
            public void onLoading(long l, long l1, boolean b) {
                Log.e("=====","dia");

                download_precent = (int) (l1 * 1.0 / l * 100);
                if((download_precent-count)>3){
                    count=(int) (l1 * 1.0 / l * 100);
                    Message message=myHandler.obtainMessage(3,l1);
                    myHandler.sendMessage(message);
                }
            }

            @Override
            public void onStarted() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub

            }
        });

    }







    class MyHandler extends Handler {
        private Context context;
        public MyHandler(Looper looper,Context c){
            super(looper);
            this.context=c;
        }
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg!=null){
                switch(msg.what){
                    case 0:
                       // Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        break;
                    case 2:
                        //下载完成后清除所有下载信息，执行安装提示
                        download_precent=0;


                        MyApplication.getInstance().showToast("正在安装更新文件，程序即将退出，稍后请重新打开应用");
                      //  SystemServiceHelper.getInstance(getApplicationContext()).silenceUninstall("com.zwg.borrowvrbook");
                        SystemServiceHelper.getInstance(getApplicationContext()).silenceInstall("com.zwg.borrowvrbook",apkpath);
                        SaveUtils.savedelPath(apkpath);
                        stopSelf();
                        break;
                    case 3:
                        //更新状态栏上的下载进度信息

                        break;
                    case 4:

                        break;
                }
            }
        }
    }




}
