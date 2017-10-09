package com.zwg.huibenlib.Utils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;


/**
 */
public class ShowLoadDia extends Dialog{

    private   String title="";

    public ShowLoadDia(Context context,String title) {
        super(context);
        this.title=title;
    }

    public ShowLoadDia(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proobar_layout);

        init();
    }

    public  void init(){

          /*  LinearLayout layout = (LinearLayout) findViewById(R.id.show_re_layout);
            layout.getBackground().setAlpha(120);*/

            TextView quxaioTv = (TextView) findViewById(R.id.quxiao_title_tv);
            quxaioTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                }
            });

            TextView titleTv = (TextView) findViewById(R.id.down_dia_tv);
            titleTv.setText("已添加《" + title+"》到任务列表");

        setTimeDimss(4000);

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

    public void dimssDia(){
       dismiss();
    }



}
