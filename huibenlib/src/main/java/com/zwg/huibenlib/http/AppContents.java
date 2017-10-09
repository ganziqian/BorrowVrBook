package com.zwg.huibenlib.http;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/9/12.
 */
public class AppContents {

  // public final static String URL="http://192.168.1.188:8090/";
    public static final String URL="http://api.xjkb.com:8090/";

    public final static String ACTIVITY_URL="machine/app_use_verify.do";   //激活验证
    public final static String TEZHENG_URL="machine/machine_yanggsj_init_bat.do";   //特征库数据
    public final static String VIDEOINFO_URL="machine/machine_yanggsj_media.do";   //视频列表数据
    public final static String JIHUO_URL="machine/machine_code_makeorder.do";   //
    public final static String LIB_JIHUO_URL="machine/machine_school_libraryapp_verify.do";   //图书借阅激活
    public final static String LIB_VRDATA_URL="machine/machine_library_init_dat.do";   //识别库列表
    public final static String CHEK_UPDATE_URL="machine/machine_app_upgraded.do";   //更新
    public final static String LIB_LSIT_URL="machine/machine_library_media.do";   //专辑列表
    public final static String LIB_ALLLSIT_URL="machine/machine_library_mediabypage.do";   //专辑列表分页


  //  public final static String APP_KEY="6e40fa04ec93a523457d9b4e9aeb7e59".toUpperCase();


    /**
     * 判断文件是否存在
     * @param name
     * @return
     */
    public static boolean isExit(String name) {
        try{
            File f=new File(name);
            if(!f.exists()){
                return false;
            }
        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }




    /**
     * 判断文件是否存在
     * @param name
     * @return
     */
    public static boolean isexit2(String name) {
        try{
            File f=new File(name);
            if(!f.exists()){
                return false;
            }
        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            //versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
            return "未知";
        }
        return versionName;
    }



    /**
     * 返回当前程序版本名
     */
    public static int getAppVersioncode(Context context) {
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
            //versioncode = pi.versionCode;

        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
            return versioncode;
        }
        return versioncode;
    }


    /**
     * 获取mac地址
     * @param context
     * @return
     */
    public static String getMacStr(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }

    /**
     * 判断是否连接wifi
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
        return false ;
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获得U盘路径
     * @return 路径
     */
    public static String getUDiskPath(){
        String udisk=null;
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            udisk = (String) getMethod.invoke(classType, new Object[]{"ro.product.udisk"});
        } catch (Exception e) {
            Log.e("Exception", e.getMessage(), e);
            udisk="?";
        }
        return udisk;
    }
    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context context) {
       // File path = new File(getUDiskPath());

        StatFs stat = new StatFs(getUDiskPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }


}
