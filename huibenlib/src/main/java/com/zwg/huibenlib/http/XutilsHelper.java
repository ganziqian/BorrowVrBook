package com.zwg.huibenlib.http;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/15.
 */
public class XutilsHelper {

    public static void post(String posUrl, Map<String,String> map,Callback.CommonCallback<String> callback){
        RequestParams params=new RequestParams(AppContents.URL+posUrl);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.addBodyParameter(entry.getKey(),  entry.getValue());
        }
        params.addBodyParameter("systemtype","3");
        params.setConnectTimeout(10000);

        x.http().post(params, callback);

    }
}
