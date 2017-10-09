package com.zwg.huibenlib.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zwg.huibenlib.MyApplication;


/**
 * 创建人：  ganziqian
 * 作用：
 * 时间：2015/9/22
 */
public class SaveUtils {


    /**
     * 是否激活标记信息
     */
    public static void saveInfo1(String isac){
        SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("isactivity",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("isac",isac);
        editor.commit();
    }

    /**
     * 删除信息
     */
    public static void deletInfo(){
        SharedPreferences preferences=   MyApplication.getInstance().getSharedPreferences("isactivity", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    /**
     *
     */
    public static String  getisActivity1(){
        SharedPreferences sharedPreferences=  MyApplication.getInstance().getSharedPreferences("isactivity", Context.MODE_PRIVATE);
        String type=sharedPreferences.getString("isac", "");
        return type;
    }



    /**
     * 保存特征库名字信息
     */
    public static void savexmlName(String xml){
        SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("tezheng",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("texml",xml);
        editor.commit();
    }



    /**
     *
     */
    public static String  gettezhengStr(){
        SharedPreferences sharedPreferences=  MyApplication.getInstance().getSharedPreferences("tezheng", Context.MODE_PRIVATE);
        String type=sharedPreferences.getString("texml", "");
        return type;
    }



    /**
     * 是否第一次播放帮助视频
     */
    public static void saveoneHelp(String xml){
        SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("onehelp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("one",xml);
        editor.commit();
    }

    /**
     *
     */
    public static String  getOneHelp(){
        SharedPreferences sharedPreferences=  MyApplication.getInstance().getSharedPreferences("onehelp", Context.MODE_PRIVATE);
        String type=sharedPreferences.getString("one", "");
        return type;
    }




    public static void saveoneTishi(String sss){
        SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("onetishi",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("onetishi",sss);
        editor.commit();
    }

    /**
     *
     */
    public static String  getOnetishi(){
        SharedPreferences sharedPreferences=  MyApplication.getInstance().getSharedPreferences("onetishi", Context.MODE_PRIVATE);
        String type=sharedPreferences.getString("onetishi", "");
        return type;
    }



    public static void saveVideotag(String sss){
        SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("tagv",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        String type=preferences.getString("tagv", "");
        if(type.equals("")){
            editor.putString("tagv",sss);
        }else {
            editor.putString("tagv",type+"_"+sss);
        }

        editor.commit();
    }

    /**
     *
     */
    public static String[]  getvideotag(){
        SharedPreferences sharedPreferences=  MyApplication.getInstance().getSharedPreferences("tagv", Context.MODE_PRIVATE);
        String type=sharedPreferences.getString("tagv", "");

        String[] ss=type.split("_");
        return ss;
    }
    public static void saveoneSavedate(String sss){
        SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("onedat",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("one",sss);
        editor.commit();
    }
    /**
     *
     */
    public static String  getOnedate(){
        SharedPreferences sharedPreferences=  MyApplication.getInstance().getSharedPreferences("onedat", Context.MODE_PRIVATE);
        String type=sharedPreferences.getString("one", "");
        return type;
    }



    /**
     *保存安装apk地址
     */
    public static void savedelPath(String sss){
        SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("apkpath",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("delpp",sss);
        editor.commit();
    }
    /**
     *获取apk地址
     */
    public static String  getdelpath(){
        SharedPreferences sharedPreferences=  MyApplication.getInstance().getSharedPreferences("apkpath", Context.MODE_PRIVATE);
        String type=sharedPreferences.getString("delpp", "");
        return type;
    }

    /**
     * 删除信息
     */
    public static void deletdelpathh(){
        SharedPreferences preferences=   MyApplication.getInstance().getSharedPreferences("apkpath", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }
}
