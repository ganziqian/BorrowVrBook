package com.zwg.huibenlib.Utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/18.
 */

public class LogUtils {


    public static final boolean isLo=true;
    public static void log(String str) {
        if (isLo) {
            Log.e("==========>", str);
        }
    }
    public static void log(String ll,String str) {
        if (isLo) {
            Log.e(ll, str);
        }
    }
}
