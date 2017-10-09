package com.zwg.huibenlib.Utils.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zwg.huibenlib.R;


/**
 * �����ˣ�  ganziqian
 * ���ã�
 * ʱ�䣺2015/8/7
 */
public class ShowOtherDia extends Dialog{

    private   String title="";
    private  Context context;

    public ShowOtherDia(Context context, String title) {
        super(context);
        this.title=title;
        this.context=context;
    }

    public ShowOtherDia(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.title = title;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_mes_dialog);

        init();
    }

    public  void init(){

        TextView quxaioTv = (TextView) findViewById(R.id.other_quxiao_title_tv);
        TextView titleTv= (TextView) findViewById(R.id.other_mes_tv);
        titleTv.setText(title);

        quxaioTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(title.equals("请设置资源路径")){
                    Activity activity= (Activity) context;
                    activity.finish();
                    return;
                }
                if(title.equals("请设置父布局id")){
                    Activity activity= (Activity) context;
                    activity.finish();
                    return;
                }
                if(title.equals("应用视频视图加载失败")){
                    Activity activity= (Activity) context;
                    activity.finish();
                    return;
                }
                if(title.equals("识别库下载失败，请退出重试")){
                    Activity activity= (Activity) context;
                    activity.finish();
                    return;
                }
                if(title.equals("找不到封面识别库")){
                    Activity activity= (Activity) context;
                    activity.finish();
                    return;
                }
            }
        });

        //  TextView titleTv = (TextView) findViewById(R.id.load_title_tv);
        //  titleTv.setText("正在下载" + title);


    }

    public void setTimeDimss(final long time){

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(time);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int jj=msg.what;
            if(jj==1){
                if(isShowing()) {
                    dismiss();
                }
            }
        }
    };


}
