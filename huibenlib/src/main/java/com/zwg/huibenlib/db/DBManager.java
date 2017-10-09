package com.zwg.huibenlib.db;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/4/21.
 */

public class DBManager {
    private String DB_NAME = "com_huiben1.db";
    private Context mContext;

    public DBManager(Context mContext) {
        this.mContext = mContext;
    }
    //把assets目录下的db文件复制到dbpath下
    public void DBManager(String packName) {
        String dbPath = "/data/data/" + packName
                + "/databases/" + DB_NAME;
       // if (!new File(dbPath).exists()) {
            try {
                FileOutputStream out = new FileOutputStream(dbPath);
                InputStream in = mContext.getAssets().open("com_huiben1.db");
                byte[] buffer = new byte[1024];
                int readBytes = 0;
                while ((readBytes = in.read(buffer)) != -1)
                    out.write(buffer, 0, readBytes);
                in.close();
                out.close();
                Log.e("=======","1");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("=======","3");
            }
      /*  }else {
            Log.e("=======","2");
        }*/
    }
}
