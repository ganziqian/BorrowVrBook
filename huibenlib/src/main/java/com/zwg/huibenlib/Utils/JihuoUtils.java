package com.zwg.huibenlib.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.zwg.huibenlib.R;
import com.zwg.huibenlib.Utils.dialog.ShowOtherDia;
import com.zwg.huibenlib.db.DataSaveUtils;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.http.MyCallBack;
import com.zwg.huibenlib.http.XutilsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 */

public class JihuoUtils {


    //验证激活���
    public static void postJihuo(final String myappkey, final Context context, final String key, final DataSaveUtils dataSaveUtils){
        Map<String,String> params=new HashMap<>();
        String mac= AppContents.getMacStr(context).toUpperCase().replace(":","");
        params.put("deviceid",mac );//E0:76:D0:95:A4:45
        Log.e("--mac----",mac);
        params.put("appkey",myappkey);

        XutilsHelper.post(AppContents.LIB_JIHUO_URL, params, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                Log.e("===121==",s);
                try {
                    JSONObject object=new JSONObject(s);
                    if(object!=null) {
                        String code = object.getString("code");
                        if (code.equals("1")) {
                            JSONArray array=object.getJSONArray("data");
                            if(array.length()!=0) {
                                JSONObject jsonObject2 = array.getJSONObject(0);
                                String resl = jsonObject2.getString("result");

                                if(resl!=null) {
                                    String rel="";
                                    try {
                                        rel= RSAUtil.getInstance().decrypt(resl,key);
                                        Log.e("r",rel);
                                    } catch (Exception e) {
                                        Log.e("e--------",e.toString());
                                        e.printStackTrace();
                                    }
                                    if(!rel.equals("")) {
                                        if (rel.equals("ok")) {


                                        } else {
                                            if(dataSaveUtils!=null){
                                                dataSaveUtils.delIsActivi();
                                            }
                                            ShowOtherDia showOtherDia = new ShowOtherDia(context, R.style.NobackDialog, "借阅服务已关闭,请重新开通再使用\n程序6秒后自动关闭");
                                            showOtherDia.show();
                                            showOtherDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    Activity activity= (Activity) context;
                                                    activity.finish();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        } else {
                            if(dataSaveUtils!=null){
                                dataSaveUtils.delIsActivi();
                            }
                            ShowOtherDia showOtherDia=new ShowOtherDia(context,R.style.NobackDialog,object.getString("msg")+"\n程序6秒后自动关闭");
                            showOtherDia.show();
                            showOtherDia.setTimeDimss(6000);
                            showOtherDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Activity activity= (Activity) context;
                                    activity.finish();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }
        });
    }
}
