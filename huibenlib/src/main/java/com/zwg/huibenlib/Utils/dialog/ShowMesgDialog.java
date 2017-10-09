package com.zwg.huibenlib.Utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;


/**
 * Created by Administrator on 2016/11/11.
 */
public class ShowMesgDialog  {

    private static Dialog dialog;

    public static void show(final Context context, String msg, final int type){


        if(dialog==null) {
            dialog = new Dialog(context, R.style.NobackDialog);
            View view = LayoutInflater.from(context).inflate(R.layout.mesg_dialog_layout, null);


            TextView mesgTv = (TextView) view.findViewById(R.id.show_dia_mesg_tv);
            TextView quxiaoTv = (TextView) view.findViewById(R.id.msg_dia_quxiao_btn);

            mesgTv.setText(msg);
            TextView quedingTv = (TextView) view.findViewById(R.id.msg_dia_queding_btn);
            quxiaoTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (type == 1) {
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                }
            });
            quedingTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(new ComponentName("com.zwg.rs2_setting", "com.wsh.setting.MainActivity"));
                        intent.putExtra("item", "wifi");
                        context.startActivity(intent);
                    } catch (Exception e) {
                        MyApplication.getInstance().showToast("跳转失败");
                        e.printStackTrace();
                    }

                }
            });


            dialog.setContentView(view);
            //dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }else {
            if(!dialog.isShowing()){
                dialog.show();
            }
        }

    }


}
