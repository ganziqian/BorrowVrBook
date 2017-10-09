package com.zwg.huibenlib.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.wsh.sdk.android.AppStoreHelper;
import com.zwg.huibenlib.inteface.StopCallBackLinstener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author ZZQ
 * @version 2017/3/29
 * @Version Copyright (c) 2017 广州智惟高教育科技. All rights reserved.
 */
public class OtherReceiverHelper {

    private static String TAG = "OtherReceiverHelper";

    Context context;
    BroadcastReceiver huanxingReceiver;
    IntentFilter intentFilter;

    private StopCallBackLinstener stopCallBackLinstener;

    public static final int SURVEI_START = 1;
    public static final int SURVEI_STOP = 2;

    public OtherReceiverHelper (Context context,StopCallBackLinstener stopCallBackLinstener) {
        this.context = context;
        this.stopCallBackLinstener=stopCallBackLinstener;
    }

    public void init1() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppStoreHelper.SURVEILLANCE);
        huanxingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(AppStoreHelper.SURVEILLANCE)) {
                    /** 监控广播 */
                    String json = intent.getStringExtra(AppStoreHelper.EXTRA_DATA);
                    Log.v(TAG, "json:"+json);

                    //{"state":"start","msg":"监控开始"}
                    String state = "";
                    try {
                        JSONObject jsonObject=new JSONObject(json);
                        state=jsonObject.getString("state");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    if (state.equals("start")) {
                        // 开始监控
                        if(stopCallBackLinstener!=null){
                            stopCallBackLinstener.myCallBack(1);
                        }

                    } else if (state.equals("stop")) {
                        // 退出监控
                        if(stopCallBackLinstener!=null){
                            stopCallBackLinstener.myCallBack(2);
                        }
                    }
                }
            }
        };
    }



    public void register () {
        context.registerReceiver (huanxingReceiver, intentFilter);
    }

    public void unregister () {
        context.unregisterReceiver(huanxingReceiver);
    }
}
