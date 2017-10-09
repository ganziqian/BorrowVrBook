package com.zwg.borrowvrbook.activity;

import android.content.Context;

import com.zwg.huibenlib.Utils.LogUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/3/23.
 */

public class DelVufusUtils {

    public static  void delVus(Context context){

        try {
           // if(readFile(context.getFilesDir()+"/vufsen.dat").equals("")){
                File file=new File(context.getFilesDir()+"/vufsen.dat");
                if(file.exists()){
                    file.delete();
                    LogUtils.log("----","del");
                }
          //  }
        }catch (Exception e){
            e.printStackTrace();
        }
    }















  /*  private static String readFile(String path)  {
        try {
            File file=new File(path);
            if(!file.exists()||file.isDirectory())
                throw new FileNotFoundException();
            BufferedReader br=new BufferedReader(new FileReader(file));
            String temp=null;
            StringBuffer sb=new StringBuffer();
            temp=br.readLine();
            while(temp!=null){
                sb.append(temp+" ");
                temp=br.readLine();
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }*/
}
