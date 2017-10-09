package com.zwg.huibenlib.db;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/14.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="com_huiben1.db";
    public static final int DB_VERSION=1;
    public static final String TABLE_VIDEO_NAME="VIDEO_LISTINFO";    //视频列表数据表
    //public static final String TABLE_TEZHENG_NAME="TEZHENG_LISTINFO";//特征库数据表
    public static final String TABLE_ISA_NAME="TEZHENG_LISTINFO";
    public static final String TABLE_VR_LIST="VRLIS_LISTINFO";

    public MyDataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "CREATE TABLE "+
                TABLE_VIDEO_NAME+ "("+
                DataTags.ID+ " integer,"+
                DataTags.LIS_TAG_NAME+ " text primary key,"+
                DataTags.LIS_ABSTRA+ " text,"+
                DataTags.LIS_MEA_NAME+ " text,"+
                DataTags. LIS_LENGTH+ " text,"+
                DataTags. LIS_MEDIA_URL+ " text,"+
                DataTags. LIS_ICOM_URL+ " text,"+
                DataTags. LIS_ID+ " text,"+
                DataTags. LIS_SAVE_NAME+ " text,"+
                DataTags. LIS_IV_NAME+ " text,"+
                DataTags. LIS_GET_NAME+ " text"+
                ")"
        );



        Log.e("===============", "CREATE TABLE "+
                TABLE_VIDEO_NAME+ "("+
                DataTags.ID+ " integer primary key,"+
                DataTags.LIS_TAG_NAME+ " text,"+
                DataTags.LIS_ABSTRA+ " text,"+
                DataTags.LIS_MEA_NAME+ " text,"+
                DataTags. LIS_LENGTH+ " text,"+
                DataTags. LIS_MEDIA_URL+ " text,"+
                DataTags. LIS_ICOM_URL+ " text,"+
                DataTags. LIS_ID+ " text,"+
                DataTags. LIS_SAVE_NAME+ " text,"+
                DataTags. LIS_IV_NAME+ " text,"+
                DataTags. LIS_GET_NAME+ " text"+
                ")");

        db.execSQL( "CREATE TABLE "+
                TABLE_ISA_NAME+ "("+
                DataTags.ID+ " integer primary key,"+
                DataTags.TAG_ACT+ " text"+
                ")"
        );
        db.execSQL( "CREATE TABLE "+
                TABLE_VR_LIST+ "("+
                DataTags.ID+ " integer,"+
                DataTags.VR_TAG+ " text,"+
                DataTags.VR_NAME+ " text,"+
                DataTags.VR_XMLA_URL+ " text,"+
                DataTags. VR_DATA_URL+ " text,"+
                DataTags. VR_IV_URL+ " text,"+
                DataTags. VR_MIAOSHU+ " text,"+
                DataTags. VR_BOOK_ID+ " text,"+
                DataTags. VR_SAXML_NAME+ " text,"+
                DataTags. VR_SADAT_NAME+ " text"+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
